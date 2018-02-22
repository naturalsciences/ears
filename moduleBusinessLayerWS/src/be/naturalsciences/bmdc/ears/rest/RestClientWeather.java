package be.naturalsciences.bmdc.ears.rest;//ys

import be.naturalsciences.bmdc.ears.entities.WeatherBean;
import static be.naturalsciences.bmdc.ears.rest.RestClient.createAllTrustingClient;
import be.naturalsciences.bmdc.ears.utils.WebserviceUtils;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
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

    public RestClientWeather() throws ConnectException, EarsException {
        super();
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
        WeatherBean wt = new WeatherBean();

        Response response = getLastWeatherXmlTarget.request().get();
        // Check Status
        if (response.getStatus() != 200) {
            throw new ConnectException("Failed : HTTP error code : " + response.getStatus());
        }
        wt = response.readEntity(WeatherBean.class);

        response.close();

        return wt;
    }

    public WeatherBean getNearestWeather(OffsetDateTime time) throws ConnectException {
        WeatherBean wt = null;

        Response response = getNearestWeatherTarget.queryParam("date", encodeUrl(time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))).request().get();
        if (response.getStatus() != 200) {
            throw new ConnectException("Failed : HTTP error code : " + response.getStatus());
        }
        wt = response.readEntity(WeatherBean.class);

        response.close();

        return wt;
    }

    public Collection<WeatherBean> getWeatherByDates(String fromDate, String toDate) throws ConnectException {
        Collection<WeatherBean> wts = null;

        Response response = getLastWeatherXmlTarget.queryParam("initDate", fromDate)
                .queryParam("endDate", toDate)
                .request().get();
        // Check Status
        if (response.getStatus() != 200) {
            throw new ConnectException("Failed : HTTP error code : " + response.getStatus());
        }
        wts = response.readEntity(new GenericType<Collection<WeatherBean>>() {
        });
        response.close();

        return wts;
    }

}
