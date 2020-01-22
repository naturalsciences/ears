package be.naturalsciences.bmdc.ears.rest;//ys

import be.naturalsciences.bmdc.ears.entities.WeatherBean;
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

public class RestClientWeather extends RestClient {

    protected ResteasyClient client;// = new ResteasyClientBuilder().build();

    protected ResteasyWebTarget getLastWeatherXmlTarget;

    protected ResteasyWebTarget getNearestWeatherTarget;

    public RestClientWeather(boolean cache) throws ConnectException, EarsException {
        super(cache);
        if (!WebserviceUtils.testWS("ears2Nav/getLast24hMet")) {
            online = false;
            throw new ConnectException();
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
            throw new EarsException("The base url for the web services is invalid. The weather web service won't work correctly.", ex);
        }
        if (uri != null) {
            getLastWeatherXmlTarget = client.target(uri.resolve("ears2Nav/getLast24hMet"));
            getNearestWeatherTarget = client.target(uri.resolve("ears2Nav/getNearestMet"));
        }
        /*else {
            throw new EarsException("The base url for the web services has not been set correctly; the application won't work properly.");
        }*/
    }

    public WeatherBean getLastWeather() throws ConnectException {
        return readOneEntity(getLastWeatherXmlTarget, null);
    }

    public WeatherBean getNearestWeather(OffsetDateTime time) throws ConnectException {
        ResteasyWebTarget target = getNearestWeatherTarget.queryParam("date", encodeUrl(time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
        return readOneEntity(target, time);
    }

    static Map<OffsetDateTime, WeatherBean> results = new THashMap();

    /**
     * Build one entity at a given time for the given url target
     *
     * @param response
     * @param time The time at which the acquisition object is gathered. If
     * null, is completed with the actual time of the acquisition object
     * @return
     */
    public WeatherBean readOneEntity(ResteasyWebTarget target, OffsetDateTime time) throws ConnectException {
        WeatherBean nav;
        if (cache && time != null && results.containsKey(time)) {
            nav = results.get(time);
        } else {
            Response response = target.request().get();
            if (response.getStatus() != 200) {
                throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + target.getUri().toString() + ")");
            }
            nav = response.readEntity(WeatherBean.class);
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

    public Collection<WeatherBean> getWeatherByDates(String fromDate, String toDate) throws ConnectException {
        Collection<WeatherBean> wts = null;

        Response response = getLastWeatherXmlTarget.queryParam("initDate", fromDate)
                .queryParam("endDate", toDate)
                .request().get();
        // Check Status
        if (response.getStatus() != 200) {
            throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getLastWeatherXmlTarget.getUri().toString() + ")");
        }
        wts = response.readEntity(new GenericType<Collection<WeatherBean>>() {
        });
        response.close();

        return wts;
    }

}
