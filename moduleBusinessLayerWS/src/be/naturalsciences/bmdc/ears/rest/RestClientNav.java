package be.naturalsciences.bmdc.ears.rest;//ys

import be.naturalsciences.bmdc.ears.entities.NavBean;
import static be.naturalsciences.bmdc.ears.rest.RestClient.createAllTrustingClient;
import be.naturalsciences.bmdc.ears.utils.WebserviceUtils;
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
        if (!WebserviceUtils.testWS("ears2Nav/getLastNavXml")) {
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
            // uri = getBaseURL().toURI();
        } catch (URISyntaxException ex) {
            throw new EarsException("The base url for the web services is invalid. The navigation web service won't work correctly.", ex);
        }
        if (uri != null) {
            getLastNavXmlTarget = client.target(uri.resolve("ears2Nav/getLastNavXml"));
            getNearestNavTarget = client.target(uri.resolve("ears2Nav/getNearestNavXml"));
        }
        /*else {
            throw new EarsException("The base url for the web services has not been set correctly; the application won't work properly.");
        }*/
    }

    public NavBean getLastNavXml() throws ConnectException {

        // Nav Using RESTEasy API
        NavBean nav = new NavBean();

        //ResteasyClient client = new ResteasyClientBuilder().build();
        //ResteasyWebTarget target = client.target(getBaseURL().resolve("getLastNavXml"));
        Response response = getLastNavXmlTarget.request().get();
        // Check Status
        if (response.getStatus() != 200) {
            throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getLastNavXmlTarget.getUri().toString() + ")");
        }
        nav = response.readEntity(NavBean.class);

        response.close();

        return nav;
    }

    public NavBean getNearestNav(OffsetDateTime time) throws ConnectException {
        NavBean nav = null;

        Response response = getNearestNavTarget.queryParam("date", encodeUrl(time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))).request().get();
        if (response.getStatus() != 200) {
            throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getNearestNavTarget.getUri().toString() + ")");
        }
        nav = response.readEntity(NavBean.class);

        response.close();

        return nav;
    }

    public Collection<NavBean> getNavByDatesXml(String fromDate, String toDate) throws ConnectException {
        Collection<NavBean> navs = null;

        Response response = getLastNavXmlTarget.queryParam("initDate", fromDate)
                .queryParam("endDate", toDate)
                .request().get();
        // Check Status
        if (response.getStatus() != 200) {
            throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getLastNavXmlTarget.getUri().toString() + ")");
        }
        navs = response.readEntity(new GenericType<Collection<NavBean>>() {
        });
        response.close();

        return navs;
    }

}
