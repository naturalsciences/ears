package be.naturalsciences.bmdc.ears.rest;//ys

import be.naturalsciences.bmdc.ears.entities.NavBean;
import static be.naturalsciences.bmdc.ears.rest.RestClient.createAllTrustingClient;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.openide.util.Exceptions;

public class RestClientNav extends RestClient {

    protected ResteasyClient client;// = new ResteasyClientBuilder().build();

    protected ResteasyWebTarget getLastNavXmlTarget;
    
    protected ResteasyWebTarget getNearestNavTarget;

    public RestClientNav() throws ConnectException, EarsException {
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
            getLastNavXmlTarget = client.target(uri.resolve("ears2Nav/getLastNavXml"));
            getNearestNavTarget = client.target(uri.resolve("ears2Nav/getNearestNavXml"));
        }
        /*else {
            throw new EarsException("The base url for the web services has not been set correctly; the application won't work properly.");
        }*/
    }

    public NavBean getLastNavXml() {

        // Nav Using RESTEasy API
        NavBean nav = new NavBean();
        try {
            //ResteasyClient client = new ResteasyClientBuilder().build();
            //ResteasyWebTarget target = client.target(getBaseURL().resolve("getLastNavXml"));

            Response response = getLastNavXmlTarget.request().get();
            // Check Status
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            nav = response.readEntity(NavBean.class);

            response.close();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
        return nav;
    }

    public NavBean getNearestNav(OffsetDateTime time) {
        NavBean nav = null;
        try {
            Response response = getNearestNavTarget.queryParam("date", encodeUrl(time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))).request().get();
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            nav = response.readEntity(NavBean.class);

            response.close();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
        return nav;
    }

    public NavBean getNearestNavXml(String date) {

        // Nav Using RESTEasy API
        NavBean nav = new NavBean();
        try {
            //ResteasyClient client = new ResteasyClientBuilder().build();

            //ResteasyWebTarget target = client.target(getBaseURL().resolve("getLastNavXml"));
            Response response = getLastNavXmlTarget.queryParam("date", date)
                    .request().get();
            // Check Status
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            nav = response.readEntity(NavBean.class);

            response.close();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
        return nav;
    }

    public NavBean getNavByDateXml(String pDate) {

        // Nav Using RESTEasy API
        NavBean nav = new NavBean();
        try {
            //ResteasyClient client = new ResteasyClientBuilder().build();

            //ResteasyWebTarget target = client.target(getBaseURL().resolve("getLastNavXml"));
            //prepare Date time for parameter
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date dateParsed = sdf.parse(pDate);
            Response response = getLastNavXmlTarget.queryParam("date", sdf.format(dateParsed)).request().get();
            // Check Status
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            nav = response.readEntity(NavBean.class);

            response.close();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }

        return nav;
    }

    public Collection<NavBean> getNavByDatesXml(String fromDate, String toDate) {

        // Nav Using RESTEasy API
        Collection<NavBean> navs = null;
        try {
            //ResteasyClient client = new ResteasyClientBuilder().build();

            //ResteasyWebTarget target = client.target(getBaseURL().resolve("getLastNavXml"));
            Response response = getLastNavXmlTarget.queryParam("initDate", fromDate)
                    .queryParam("endDate", toDate)
                    .request().get();
            // Check Status
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            navs = response.readEntity(new GenericType<Collection<NavBean>>() {
            });
            //System.out.println(navs);
            response.close();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
        return navs;
    }

}
