package be.naturalsciences.bmdc.ears.rest;//ys

import be.naturalsciences.bmdc.ears.entities.UnderwayBean;
import static be.naturalsciences.bmdc.ears.rest.RestClient.createAllTrustingClient;
import be.naturalsciences.bmdc.ears.utils.WebserviceUtils;
import be.naturalsciences.bmdc.ontology.EarsException;
import gnu.trove.map.hash.THashMap;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.openide.util.NotImplementedException;

/**
 * *
 * An RBINS specific class to gather underway data from the FerryBox.
 *
 * @author thomas
 */
public class RestClientUnderway extends RestClient {

    protected ResteasyClient client;

    protected ResteasyWebTarget getLastUndXmlTarget;

    protected ResteasyWebTarget getNearestUndTarget;

    private boolean isOnline() {
        return getBaseURL() != null && WebserviceUtils.testWS("ears3Nav/und/getLast/datagram");
    }

    public RestClientUnderway(boolean cache) throws ConnectException, EarsException {
        super(cache);
        if (!isOnline()) {
            throw new EarsException("The navigation web service can't be reached. The application won't work correctly.");
        }
        if (isHttps) {
            ApacheHttpClient4Engine engine = null;
            try {
                engine = new ApacheHttpClient4Engine(createAllTrustingClient());
            } catch (GeneralSecurityException ex) {
                throw new EarsException("Security problem when connecting.", ex);
            }
            client = new ResteasyClientBuilder().httpEngine(engine).build();
        } else {
            client = new ResteasyClientBuilder().build();
        }
        URI uri = null;
        try {
            uri = baseURL.toURI();
        } catch (URISyntaxException ex) {
            throw new EarsException("The base url for the web services is invalid. The underway web service won't work correctly.", ex);
        }
        if (uri != null) {
            getLastUndXmlTarget = client.target(uri.resolve("ears3Nav/und/getLast/xml"));
            getNearestUndTarget = client.target(uri.resolve("ears3Nav/und/getNearest/xml"));
        }
    }

    public UnderwayBean getLastUndXml() {
        throw new NotImplementedException();
    }

    public UnderwayBean getNearestUnderway(OffsetDateTime time) throws ConnectException {
        ResteasyWebTarget target = getNearestUndTarget.queryParam("date", encodeUrl(time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
        return readOneEntity(target, time);
    }

    static Map<OffsetDateTime, UnderwayBean> results = new THashMap();

    /**
     * Build one entity at a given time for the given url target
     *
     * @param response
     * @param time The time at which the acquisition object is gathered. If
     * null, is completed with the actual time of the acquisition object
     * @return
     */
    public UnderwayBean readOneEntity(ResteasyWebTarget target, OffsetDateTime time) throws ConnectException {
        UnderwayBean nav;
        if (cache && time != null && results.containsKey(time)) {
            nav = results.get(time);
        } else {
            Response response = target.request().get();
            if (response.getStatus() != 200) {
                response.close();
                throw new ConnectException(response.getStatus() + "(" + response.getStatusInfo().getReasonPhrase() + ") - " + target.getUri().toString() + ")");

            }
            nav = response.readEntity(UnderwayBean.class);

            response.close();
            if (time == null) {
                time = nav.getOffsetDateTime();
            }
            if (cache) {
                results.put(time, nav);
            }
        }
        return nav;
    }

    public Collection<UnderwayBean> getUndByDatesXml(String fromDate, String toDate) {
        throw new NotImplementedException();
    }

}
