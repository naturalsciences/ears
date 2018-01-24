package be.naturalsciences.bmdc.ears.rest;//ys

import be.naturalsciences.bmdc.ears.entities.WeatherBean;
import static be.naturalsciences.bmdc.ears.rest.RestClient.createAllTrustingClient;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
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
        if (isHttps) {
            ApacheHttpClient4Engine engine = null;
            try {
                engine = new ApacheHttpClient4Engine(createAllTrustingClient());
            } catch (GeneralSecurityException ex) {
                Exceptions.printStackTrace(ex);
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
            throw new EarsException("The base url for the web services is invalid. The navigation won't work correctly.", ex);
        }
        if (uri != null) {
            getLastWeatherXmlTarget = client.target(uri.resolve("ears2Nav/getLast24hMet"));
            getNearestWeatherTarget = client.target(uri.resolve("ears2Nav/getNearestMet"));
        }
        /*else {
            throw new EarsException("The base url for the web services has not been set correctly; the application won't work properly.");
        }*/
    }

    public WeatherBean getLastWeather() {
        WeatherBean wt = new WeatherBean();
        try {
            Response response = getLastWeatherXmlTarget.request().get();
            // Check Status
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            wt = response.readEntity(WeatherBean.class);

            response.close();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
        return wt;
    }

    public WeatherBean getNearestWeather(OffsetDateTime time) {
        WeatherBean wt = null;
        try {
            Response response = getNearestWeatherTarget.queryParam("date", encodeUrl(time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))).request().get();
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            wt = response.readEntity(WeatherBean.class);

            response.close();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
        return wt;
    }

    public Collection<WeatherBean> getWeatherByDates(String fromDate, String toDate) {
        Collection<WeatherBean> wts = null;
        try {
            Response response = getLastWeatherXmlTarget.queryParam("initDate", fromDate)
                    .queryParam("endDate", toDate)
                    .request().get();
            // Check Status
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "  + response.getStatus());
            }
            wts = response.readEntity(new GenericType<Collection<WeatherBean>>() {});
            response.close();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
        return wts;
    }

}
