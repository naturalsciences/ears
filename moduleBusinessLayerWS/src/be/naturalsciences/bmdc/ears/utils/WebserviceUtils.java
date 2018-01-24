/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.utils;

import be.naturalsciences.bmdc.ears.entities.CurrentURL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.openide.util.Utilities;

/**
 *
 * @author BMDC
 */
public class WebserviceUtils {
    
    public static final int HTTP_REQUEST_TIMEOUT = 6000;
    public static final int HTTP_READ_TIMEOUT = 6000;

    public static boolean testWS(URL baseUrl, String path) {
        try {
            baseUrl = new URL(baseUrl, path);
        } catch (MalformedURLException ex) {
            return false;
        }
        if (baseUrl != null && baseUrl.getProtocol() != null && baseUrl.getHost() != null && baseUrl.getPath() != null) {
            try {
                HttpURLConnection con = (HttpURLConnection) baseUrl.openConnection();
                con.setRequestMethod("GET");
                int responseCode;
                con.setReadTimeout(HTTP_READ_TIMEOUT);
                con.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
                responseCode = con.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK;
            } catch (Exception ex) {
                //Messaging.report("The given url (" + baseUrl.toString() + ") could not be reached", ex, WebserviceUtils.class, true);
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean testWS(String path) {
        CurrentURL baseUrl = Utilities.actionsGlobalContext().lookup(CurrentURL.class);
        if (baseUrl != null) {
            return testWS(baseUrl.getConcept(), path);
        }
        return false;

    }

    /*public static boolean localWebServicesAreAvailable() throws MalformedURLException {
        return FileUtils.websiteIsAvailable(Configs.getRestURL());
    }*/
}
