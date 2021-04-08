/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.rest;

import be.naturalsciences.bmdc.ears.entities.CurrentURL;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.RestMessage;
import be.naturalsciences.bmdc.ears.entities.NavBean;
import be.naturalsciences.bmdc.ears.entities.ThermosalBean;
import be.naturalsciences.bmdc.ears.entities.UnderwayBean;
import be.naturalsciences.bmdc.ears.entities.WeatherBean;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ears.utils.WebserviceUtils;
import be.naturalsciences.bmdc.ontology.EarsException;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.openide.util.Utilities;

/**
 *
 * @author Thomas Vandenberghe
 */
public abstract class RestClient implements Serializable {

    private static final long serialVersionUID = 1L;

    public static UnderwayBean getUnderway(RestClientUnderway rest, OffsetDateTime ts) {
        try {
            UnderwayBean bean = rest != null ? rest.getNearestUnderway(ts) : null;
            return bean;
        } catch (ConnectException ex) {
            Messaging.report("Could not connect to the web service for underway to get the data. Info not added.", ex, RestClient.class, true);
        }
        return null;
    }

    public static ThermosalBean getThermosal(RestClientThermosal rest, OffsetDateTime ts) {
        try {
            ThermosalBean bean = rest != null ? rest.getNearestThermosal(ts) : null;
            return bean;
        } catch (ConnectException ex) {
            Messaging.report("Could not connect to the web service for thermosal to get the data. Info not added.", ex, RestClient.class, true);
        }
        return null;
    }

    public static NavBean getNavigation(RestClientNav rest, OffsetDateTime ts) {
        try {
            NavBean bean = rest != null ? rest.getNearestNav(ts) : null;
            return bean;
        } catch (ConnectException ex) {
            Messaging.report("Could not connect to the web service for navigation to get the data. Info not added.", ex, RestClient.class, true);
        }
        return null;
    }

    public static WeatherBean getWeather(RestClientWeather rest, OffsetDateTime ts) {
        try {
            WeatherBean bean = rest != null ? rest.getNearestWeather(ts) : null;
            return bean;
        } catch (ConnectException ex) {
            Messaging.report("Could not connect to the web service for weather to get the data. Info not added.", ex, RestClient.class, true);
        }
        return null;
    }

    private boolean isOnline() {

        return getBaseURL() != null && WebserviceUtils.testWS("ears3/alive");
    }

    protected static URL baseURL;

    protected boolean isHttps = false;

    /**
     * Cache all incoming results for later use
     */
    protected boolean cache;

    public RestClient(boolean cache) throws ConnectException, EarsException {
        if (!isOnline()) {
            throw new EarsException("The base url for the EARS web services is undefined or unreachable. The application won't work correctly.");
        }
        baseURL = getBaseURL();

        if (baseURL.getProtocol().equals("https")) {
            isHttps = true;
        } else {
            isHttps = false;
        }
        /* if (!isOnline()) {
            throw new ConnectException("Connecting to " + WebserviceUtils.getCurrentURL() + " failed");
        }*/
        this.cache = cache;
    }

    protected static HttpClient createAllTrustingClient() throws GeneralSecurityException {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

        TrustStrategy trustStrategy = new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        };
        SSLSocketFactory factory = new SSLSocketFactory(trustStrategy, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        registry.register(new Scheme("https", 443, factory));

        ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
        mgr.setMaxTotal(1000);
        mgr.setDefaultMaxPerRoute(1000);

        DefaultHttpClient client = new DefaultHttpClient(mgr, new DefaultHttpClient().getParams());
        return client;
    }

    public static URI getBaseURI() {
        try {
            return getBaseURL().toURI();
        } catch (NullPointerException | URISyntaxException ex) {
            return null;
        }
    }

    public static URL getBaseURL() {
        CurrentURL lookup = Utilities.actionsGlobalContext().lookup(CurrentURL.class);
        if (lookup != null) {
            return lookup.getConcept();
        }
        return null;
    }

    public <E> IResponseMessage<E> performPost(ResteasyWebTarget target, Class<E> cls, E o) {
        IResponseMessage<E> responseMessage = null;
        if (isOnline()) {
            Gson g = Converters.registerOffsetDateTime(new GsonBuilder()).create();
            String payload = g.toJson(o);
            //  payload = payload.replaceAll(":(\\d{2})\\.\\d+", ":$1");
            payload = payload.replaceAll("(\\+|\\-)(\\d{2}):(\\d{2})", "+$2$3"); //+01:00->+0100
            Response response = target.request().post(Entity.json(payload));
            GenericType<RestMessage<E>> type = new GenericType<RestMessage<E>>() {
            };
            try {
                responseMessage = response.readEntity(type);
                if (responseMessage.isOk()) {
                    responseMessage.setMessage(cls.getSimpleName() + " " + responseMessage.getIdentifier() + " created/modified");
                } else {
                    responseMessage.setMessage(response.getStatusInfo().getReasonPhrase() + " (" + responseMessage.getExceptionType() + ") - " + cls.getSimpleName() + " not created/modified");
                }
            } catch (Exception e) {
                responseMessage = new RestMessage(response.getStatus(), response.getStatus() + " (" + response.getStatusInfo().getReasonPhrase() + ")");
            } finally {
                int status = response.getStatus();
                response.close();
                printResponse(target, status, cls, responseMessage);
            }
        }
        return responseMessage;
    }

    public static String printResponse(ResteasyWebTarget target, int status, Class cls, IResponseMessage responseMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("------------\n");
        sb.append("|Tried URL: ").append(target.getUri().toASCIIString()).append("\n");
        sb.append("|Server response status code: ").append(status).append("\n");
        if (responseMessage == null) {
            sb.append("|Response message: There is a problem with the web service and no responseMessage was returned." + "\n");
        } else {
            sb.append("|Response message: ").append(responseMessage.getMessage()).append("\n");
        }
        sb.append("------------\n");
        Messaging.report(sb.toString(), Message.State.INFO, cls, false);
        return sb.toString();
    }

    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RestClientEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
