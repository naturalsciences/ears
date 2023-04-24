package be.naturalsciences.bmdc.ears.rest;//ys

import be.naturalsciences.bmdc.ears.entities.NavBean;
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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

public class RestClientNav extends RestClient {

    protected ResteasyClient client;// = new ResteasyClientBuilder().build();

    protected ResteasyWebTarget getLastNavXmlTarget;

    protected ResteasyWebTarget getNearestNavTarget;

    private boolean isOnline() {
        return getBaseURL() != null && WebserviceUtils.testWS("ears3Nav/nav/getLast/datagram");
    }

    public RestClientNav(boolean cache) throws ConnectException, EarsException {
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
            throw new EarsException("The base url for the web services is invalid. The navigation web service won't work correctly.", ex);
        }
        if (uri != null) {
            getLastNavXmlTarget = client.target(uri.resolve("ears3Nav/nav/getLast/xml"));
            getNearestNavTarget = client.target(uri.resolve("ears3Nav/nav/getNearest/xml"));
        }
        /*else {
            throw new EarsException("The base url for the web services has not been set correctly; the application won't work properly.");
        }*/
    }

    static Map<OffsetDateTime, NavBean> results = new THashMap();

    /**
     * Build one entity at a given time for the given url target
     *
     * @param response
     * @param time The time at which the acquisition object is gathered. If
     * null, is completed with the actual time of the acquisition object
     * @return
     */
    public NavBean readOneEntity(ResteasyWebTarget target, OffsetDateTime time) throws ConnectException {
        NavBean nav;
        if (cache && time != null && results.containsKey(time)) {
            nav = results.get(time);
        } else {
            Response response = target.request().get();
            if (response.getStatus() != 200) {
                throw new ConnectException(response.getStatus() + "(" + response.getStatusInfo().getReasonPhrase() + ") - " + target.getUri().toString() + ")");
            }
            nav = response.readEntity(NavBean.class);
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

    public NavBean getLastNavXml() throws ConnectException {
        return readOneEntity(getLastNavXmlTarget, null);
    }

    public NavBean getNearestNav(OffsetDateTime time) throws ConnectException {
        ResteasyWebTarget target = getNearestNavTarget.queryParam("date", encodeUrl(time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
        return readOneEntity(target, time);
    }

    public Collection<NavBean> getNavByDatesXml(String fromDate, String toDate) throws ConnectException {
        Collection<NavBean> navs = null;

        Response response = getLastNavXmlTarget.queryParam("initDate", fromDate)
                .queryParam("endDate", toDate)
                .request().get();
        // Check Status
        if (response.getStatus() != 200) {
            throw new ConnectException(response.getStatus() + "(" + response.getStatusInfo().getReasonPhrase() + ") - z" + getLastNavXmlTarget.getUri().toString() + ")");
        }
        navs = response.readEntity(new GenericType<Collection<NavBean>>() {
        });
        response.close();

        return navs;
    }

}
