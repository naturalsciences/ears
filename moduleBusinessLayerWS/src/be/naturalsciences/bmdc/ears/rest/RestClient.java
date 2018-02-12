/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.rest;

import be.naturalsciences.bmdc.ears.entities.CurrentURL;
import be.naturalsciences.bmdc.ears.entities.ExceptionMessage;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.MessageBean;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ears.utils.WebserviceUtils;
import be.naturalsciences.bmdc.ontology.EarsException;
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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.MediaType;
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

    protected boolean online = true;

    protected static URL baseURL;

    protected boolean isHttps = false;

    public RestClient(URL url) throws ConnectException, EarsException {
        if (url == null) {
            online = false;
            throw new EarsException("The base url for the EARS web services is null. The application won't work correctly.");
        }
        this.baseURL = url;

        if (baseURL.getProtocol().equals("https")) {
            isHttps = true;
        } else {
            isHttps = false;
        }
        if (!WebserviceUtils.testWS(url, "ears2/getCruise")) {
            online = false;
            throw new ConnectException();
        }
    }

    public RestClient() throws ConnectException, EarsException {
        if (getBaseURL() == null) {
            online = false;
            throw new EarsException("The base url for the EARS web services is null. The application won't work correctly.");
        }
        baseURL = getBaseURL();

        if (baseURL.getProtocol().equals("https")) {
            isHttps = true;
        } else {
            isHttps = false;
        }
        if (!WebserviceUtils.testWS("ears2/getCruise")) {
            online = false;
            throw new ConnectException();
        }
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

    public IResponseMessage performGetWhichIsActuallyAPost(ResteasyWebTarget target, Class cls) {

        IResponseMessage responseMessage = null;
        if (online) {
            Response response = null;
            response = target.request(MediaType.APPLICATION_XML).get();
            try {
                responseMessage = response.readEntity(MessageBean.class);
            } catch (ProcessingException e) {
                response = target.request(MediaType.APPLICATION_XML).get();

                try {
                    responseMessage = response.readEntity(ExceptionMessage.class);

                } catch (ProcessingException e2) {
                    response = target.request(MediaType.APPLICATION_XML, MediaType.TEXT_HTML).get();
                    String responseAsString = response.readEntity(String.class);

                    if (responseAsString == null || responseAsString.isEmpty()) {
                        responseMessage = new ExceptionMessage(new Date().toString(), "An exception occured while posting this entity: likely a DataIntegrityViolationException");
                    } else {
                        responseMessage = new ExceptionMessage(new Date().toString(), responseAsString);
                    }
                }
            }
            printResponse(target, response, cls, responseMessage);
            response.close();
            // MessageBean res = new MessageBean(event.getId(), "Cruise inserted");
            /*MessageBean res = (MessageBean) response.readEntity(new GenericType<MessageBean>() {
             });*/
            //System.out.println("DEBUG MESSAGES:" + res.getCode() + ", " + res.getDescription());
            // Response code
            //System.out.println(response.getStatus());
            //response.close();
        }
        return responseMessage;
    }

    public static String printResponse(ResteasyWebTarget target, Response res, Class cls, IResponseMessage responseMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("------------\n");
        sb.append("|Tried URL: ").append(target.getUri().toASCIIString()).append("\n");
        if (res != null) {
            sb.append("|Server response status code: ").append(res.getStatus()).append("\n");
        } else {
            sb.append("|No server response status code." + "\n");
        }
        if (responseMessage == null) {
            sb.append("|Response message: There is a problem with the web service and no responseMessage was returned." + "\n");
        } else {
            sb.append("|Response message: ").append(responseMessage.getSummary()).append("\n");
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
