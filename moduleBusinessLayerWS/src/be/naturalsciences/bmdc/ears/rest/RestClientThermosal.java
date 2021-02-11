package be.naturalsciences.bmdc.ears.rest;//ys

import be.naturalsciences.bmdc.ears.entities.MessageBean;
import be.naturalsciences.bmdc.ears.entities.ThermosalBean;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.openide.util.Exceptions;

public class RestClientThermosal extends RestClient {

    protected ResteasyClient client;// = new ResteasyClientBuilder().build();

    protected ResteasyWebTarget getLastThermosalXmlTarget;

    protected ResteasyWebTarget getNearestThermosalTarget;

    private boolean isOnline() {
        return getBaseURL() != null && WebserviceUtils.testWS("ears3Nav/tss/getLast/datagram");
    }

    public RestClientThermosal(boolean cache) throws ConnectException, EarsException {
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
            // uri = getBaseURL().toURI();
        } catch (URISyntaxException ex) {
            throw new EarsException("The base url for the web services is invalid. The thermosal web service won't work correctly.", ex);
        }
        if (uri != null) {
            getLastThermosalXmlTarget = client.target(uri.resolve("ears3Nav/tss/getLast/xml"));
            getNearestThermosalTarget = client.target(uri.resolve("ears3Nav/tss/getNearest/xml"));
        }
        /*else {
            throw new EarsException("The base url for the web services has not been set correctly; the application won't work properly.");
        }*/
    }

    public ThermosalBean getLastThermosal() throws ConnectException {
        return readOneEntity(getLastThermosalXmlTarget, null);
    }

    public ThermosalBean getNearestThermosal(OffsetDateTime time) throws ConnectException {
        ResteasyWebTarget target = getNearestThermosalTarget.queryParam("date", encodeUrl(time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
        return readOneEntity(target, time);
    }

    static Map<OffsetDateTime, ThermosalBean> results = new THashMap();

    /**
     * Build one entity at a given time for the given url target
     *
     * @param response
     * @param time The time at which the acquisition object is gathered. If
     * null, is completed with the actual time of the acquisition object
     * @return
     */
    public ThermosalBean readOneEntity(ResteasyWebTarget target, OffsetDateTime time) throws ConnectException {
        ThermosalBean nav;
        if (cache && time != null && results.containsKey(time)) {
            nav = results.get(time);
        } else {
            Response response = target.request().get();
            if (response.getStatus() != 200) {
                throw new ConnectException("response.getStatus() + \"(\" + response.getStatusInfo().getReasonPhrase() + \") - " + target.getUri().toString() + ")");
            }
            nav = response.readEntity(ThermosalBean.class);
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

    public Collection<ThermosalBean> getThermosalByDates(String fromDate, String toDate) throws ConnectException {
        Collection<ThermosalBean> ths = null;

        Response response = getLastThermosalXmlTarget.queryParam("initDate", fromDate)
                .queryParam("endDate", toDate)
                .request().get();
        // Check Status
        if (response.getStatus() != 200) {
            throw new ConnectException("response.getStatus() + \"(\" + response.getStatusInfo().getReasonPhrase() + \") - " + getLastThermosalXmlTarget.getUri().toString() + ")");
        }
        ths = response.readEntity(new GenericType<Collection<ThermosalBean>>() {
        });
        response.close();

        return ths;
    }

}
