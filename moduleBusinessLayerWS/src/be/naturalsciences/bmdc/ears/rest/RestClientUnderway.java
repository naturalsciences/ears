package be.naturalsciences.bmdc.ears.rest;//ys

import be.naturalsciences.bmdc.ears.entities.NavBean;
import be.naturalsciences.bmdc.ears.entities.UnderwayBean;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.openide.util.Exceptions;
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

    public RestClientUnderway() throws ConnectException, EarsException {
        super();
        if (!WebserviceUtils.testWS("ears2Nav/getLastUndXml")) {
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
            throw new EarsException("The base url for the web services is invalid. The underway web service won't work correctly.", ex);
        }
        if (uri != null) {
            getLastUndXmlTarget = client.target(uri.resolve("ears2Nav/getLastUndXml"));
            getNearestUndTarget = client.target(uri.resolve("ears2Nav/getNearestUndXml"));
        }
    }

    public UnderwayBean getLastUndXml() {
        throw new NotImplementedException();
    }

    public UnderwayBean getNearestUnderway(OffsetDateTime time) throws ConnectException {
        UnderwayBean und = null;

        Response response = getNearestUndTarget.queryParam("date", encodeUrl(time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))).request().get();
        if (response.getStatus() != 200) {
            throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getNearestUndTarget.getUri().toString() + ")");
        }
        und = response.readEntity(UnderwayBean.class);

        response.close();

        return und;
    }

    public Collection<NavBean> getNavByDatesXml(String fromDate, String toDate) {
        throw new NotImplementedException();
    }

}
