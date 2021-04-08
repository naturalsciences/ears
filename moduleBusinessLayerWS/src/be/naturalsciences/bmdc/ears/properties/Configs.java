/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.properties;

import be.naturalsciences.bmdc.ears.base.StaticMetadataSearcher;
import be.naturalsciences.bmdc.ears.entities.Actor;
import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.entities.User;
import be.naturalsciences.bmdc.ears.entities.VesselBean;
import com.google.common.base.Charsets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.prefs.BackingStoreException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

public class Configs {

    public static void persistActor(Actor actor) {
        persistActor(actor.getId(), actor.getFirstName(), actor.getLastName(), actor.getEmail());
    }

    private static void persistActor(int key, String firstName, String lastName, String email) {
        String data = "<firstName>" + firstName + "</firstName><lastName>" + lastName + "</lastName>" + "<email>" + email + "</email>";
        NbPreferences.forModule(Configs.class).put("actor" + key, data);
    }

    private static Map decodeBase64(final String encodedString) {
        final byte[] decodedBytes = Base64.decodeBase64(encodedString.getBytes());
        final String pair = new String(decodedBytes);
        final String[] userDetails = pair.split(":", 2);
        Map<String, String> map = new HashMap();
        if (userDetails.length == 2) {
            map.put("username", userDetails[0]);
            map.put("password", userDetails[1]);
        }
        return map;
    }

    public static void persistUser(User user) {
        persistUser(user.getUsername(), user.getPassword());
    }

    public static void persistUser(String username, String password) {
        String encodedPW = new String(Base64.encodeBase64(password.getBytes(Charsets.UTF_8)));
        NbPreferences.forModule(Configs.class).put("username", username);
        NbPreferences.forModule(Configs.class).put("password", encodedPW);
    }

    public static User getCurrentUser() {
        String username = NbPreferences.forModule(Configs.class).get("username", "");
        String encodedPW = NbPreferences.forModule(Configs.class).get("password", "");
        final byte[] decodedBytes = Base64.decodeBase64(encodedPW.getBytes(Charsets.UTF_8));
        String password = new String(decodedBytes);
        User user = null;
        if (password != null && username != null) {
            user = new User(username, password);
        }
        return user;
    }

    public static void removeActor(Actor actor) {
        removeActor(actor.getId());
    }

    private static void removeActor(int key) {
        NbPreferences.forModule(Configs.class).remove("actor" + key);
    }

    public static void persistVessel(VesselBean vessel) {
        persistVessel(vessel.getCode(), vessel.getVesselName());
    }

    private static void persistVessel(String vesselSDNCode, String vesselName) {//c17 //key, label
        NbPreferences.forModule(Configs.class).put("vessel", vesselSDNCode);
    }

    public static void persistRestURL(String url) {
        NbPreferences.forModule(Configs.class).put("url", url);
    }

    public static void persistRestURL(URL url) {
        NbPreferences.forModule(Configs.class).put("url", url.toString());
    }

    public static String getRestURLString() {
        return NbPreferences.forModule(Configs.class).get("url", "");
    }

    public static URL getRestURL() throws MalformedURLException {
        if (getRestURLString() == null || getRestURLString().equals("")) {
            return null;
        } else {
            return new URL(getRestURLString());
        }
    }

    public static void persistCountries(Collection<String> countries) {
        NbPreferences.forModule(Configs.class).put("countries", String.join(", ", countries));
    }

    public static void persistOverrideEventsAsAnonymous(boolean value) {
        NbPreferences.forModule(Configs.class).putBoolean("overrideEventsAsAnonymous", value);
    }

    /*public static boolean getOverrideEventsAsAnonymous() {
        return NbPreferences.forModule(Configs.class).getBoolean("overrideEventsAsAnonymous", true);
    }*/
    public static String getCountryString() {
        return NbPreferences.forModule(Configs.class).get("countries", "");
    }

    public static Set<String> getCountries() {
        String countries = NbPreferences.forModule(Configs.class).get("countries", "");
        if (countries == null || countries.equals("")) {
            return null;
        } else {
            return new HashSet(Arrays.asList(countries.split("\\s*,\\s*")));
        }
    }

    /**
     * *
     * Retrieve the current vessel name from the configuration file in
     * config/config.properties. Throws a FileNotFoundException if the file is
     * not found or the file contents could not be read.
     *
     * @return
     * @throws FileNotFoundException
     * @throws java.text.ParseException
     */
    public static CurrentVessel getCurrentVessel() throws FileNotFoundException, ParseException {
        String code = NbPreferences.forModule(Configs.class).get("vessel", "");
        if (code == null || code.equals("")) {
            return null;
        } else {
            VesselBean vessel = StaticMetadataSearcher.getInstance().getVessel(code);
            if (vessel != null) {
                return CurrentVessel.getInstance(vessel);
            } else {
                return null;
            }
        }
    }

    /*public static String getCurrentVesselCode() throws FileNotFoundException {
     return getCurrentVessel().getConcept().getCode();
     }*/
    /**
     * Returns a list of the actors present in the config file.
     *
     * @return
     * @throws IOException
     */
    public static Set<Actor> getAllActors() throws IOException {
        Set<Actor> actors = new TreeSet<>();
        Pattern p1 = Pattern.compile("<firstName>(.+?)</firstName>");
        Pattern p2 = Pattern.compile("<lastName>(.+?)</lastName>");
        Pattern p3 = Pattern.compile("<email>(.+?)</email>");
        try {
            for (String key : NbPreferences.forModule(Configs.class).keys()) {
                String idS = null;
                if (key.startsWith("actor")) {
                    idS = key.replaceAll("actor", "");
                    int id = Integer.parseInt(idS);
                    String actorXml = NbPreferences.forModule(Configs.class).get(key, "");
                    Matcher m1 = p1.matcher(actorXml);
                    Matcher m2 = p2.matcher(actorXml);
                    Matcher m3 = p3.matcher(actorXml);

                    if (m1.find() && m2.find() && m3.find()) {
                        Actor actor = new Actor(id, m1.group(1), m2.group(1), m3.group(1));
                        actors.add(actor);
                    }
                }
            }
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
        return actors;
    }

    /**
     * *
     * Get the current vessel this class is being run on. It is not the value of
     * the scope of the underlying ontology!
     *
     * @return String
     * @throws java.io.FileNotFoundException
     */
    /*public static String getCurrentVessel() throws FileNotFoundException {
     return getProperty("vesselSDNCode");
     }*/
    /**
     * *
     * Get the value of a property in a certain file.
     *
     * @param property
     * @return
     * @throws FileNotFoundException
     */
    public static String getProperty(String property) throws FileNotFoundException {
        return NbPreferences.forModule(Configs.class).get(property, "");
    }

}
