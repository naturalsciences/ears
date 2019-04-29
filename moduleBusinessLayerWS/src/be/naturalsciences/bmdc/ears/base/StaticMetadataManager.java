/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.base;

import be.naturalsciences.bmdc.ears.entities.CountryBean;
import be.naturalsciences.bmdc.ears.entities.EARSConcept;
import be.naturalsciences.bmdc.ears.entities.HarbourBean;
import be.naturalsciences.bmdc.ears.entities.OrganisationBean;
import be.naturalsciences.bmdc.ears.entities.ProjectBean;
import be.naturalsciences.bmdc.ears.entities.SeaAreaBean;
import be.naturalsciences.bmdc.ears.entities.VesselBean;
import be.naturalsciences.bmdc.ears.properties.Configs;
import be.naturalsciences.bmdc.ears.properties.Constants;
import be.naturalsciences.bmdc.ears.utils.EARSConceptFactory;
import be.naturalsciences.bmdc.ears.utils.ReflectionUtils;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.OntologyConstants;
import be.naturalsciences.bmdc.ontology.writer.JSONReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gnu.trove.map.hash.THashMap;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openide.util.Exceptions;

/**
 *
 * @author thomas
 * @param <C>
 */
public class StaticMetadataManager<C extends EARSConcept> {

    private class JsonWrapper<C extends EARSConcept> {

        public String date;
        public List<C> concepts;

        public JsonWrapper(String date, List<C> concepts) {
            this.date = date;
            this.concepts = concepts;
        }

        public Date toDate() {
            return Date.from(Instant.parse(date));
        }
    }

    public static final String vesselQuery = "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
            + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
            + "PREFIX ears2: <http://ontologies.ef-ears.eu/ears2/1#>\n"
            + "SELECT ?code ?vesselName WHERE {\n"
            + "  ?s a ears2:Vessel .\n"
            + "?s ears2:asConcept ?c . \n"
            + "?c dc:identifier ?_i .\n"
            + "?c skos:prefLabel ?_l\n"
            + "\n"
            + "bind( str(?_i) as ?code )\n"
            + "bind( str(?_l) as ?vesselName )    \n"
            + "}ORDER BY ASC(?vesselName)";

    public static final String seaAreaQuery = "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
            + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
            + "PREFIX ears2: <http://ontologies.ef-ears.eu/ears2/1#>\n"
            + "SELECT ?code ?name WHERE {\n"
            + "  ?s a ears2:SeaArea .\n"
            + "?s ears2:asConcept ?c . \n"
            + "?c dc:identifier ?_i .\n"
            + "?c skos:prefLabel ?_l\n"
            + "\n"
            + "bind( str(?_i) as ?code )\n"
            + "bind( str(?_l) as ?name )    \n"
            + "}ORDER BY ASC(?name)";

    public static final String harbourQuery = "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
            + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
            + "PREFIX ears2: <http://ontologies.ef-ears.eu/ears2/1#>\n"
            + "PREFIX geo: <http://www.geonames.org/ontology#> \n"
            + "SELECT ?code ?name ?i2 ?country WHERE {\n"
            + "  ?s a ears2:Harbour .\n"
            + "?s geo:parentCountry ?ct .\n"
            + "?s ears2:asConcept ?c . \n"
            + "?ct ears2:asConcept ?c2 .\n"
            + "?c dc:identifier ?_i .\n"
            + "?c skos:prefLabel ?_l.\n"
            + "?c2 dc:identifier ?_i2 .\n"
            + "?c2 skos:prefLabel ?_l2\n"
            + "FILTER ( ?country IN (%s) )\n"
            + "bind( str(?_i) as ?code )\n"
            + "bind( str(?_i2) as ?i2 )\n"
            + "bind( str(?_l) as ?name )    \n"
            + "bind( str(?_l2) as ?country )   \n"
            + "}ORDER BY ASC(?name)";

    public static final String organisationQuery = "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
            + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
            + "PREFIX ears2: <http://ontologies.ef-ears.eu/ears2/1#>\n"
            + "PREFIX geo: <http://www.geonames.org/ontology#> \n"
            + "SELECT ?code ?name ?i2 ?country WHERE {\n"
            + "?s a ears2:Organisation .\n"
            + "?s geo:parentCountry ?ct .\n"
            + "?s ears2:asConcept ?c . \n"
            + "?ct ears2:asConcept ?c2 .\n"
            + "?c dc:identifier ?_i .\n"
            + "?c skos:prefLabel ?_l.\n"
            + "?c2 dc:identifier ?_i2 .\n"
            + "?c2 skos:prefLabel ?_l2 .\n"
            + "FILTER ( ?country IN (%s) )\n"
            + "bind( str(?_i) as ?code )\n"
            + "bind( str(?_i2) as ?i2 )\n"
            + "bind( str(?_l) as ?name )    \n"
            + "bind( str(?_l2) as ?country )   \n"
            + "}ORDER BY ASC(?name)";

    public static final String projectQuery = "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
            + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
            + "PREFIX ears2: <http://ontologies.ef-ears.eu/ears2/1#>\n"
            + "PREFIX geo: <http://www.geonames.org/ontology#> \n"
            + "PREFIX dbpedia: <http://dbpedia.org/ontology/>\n"
            + "SELECT ?code ?name ?acronym ?oi ?organisation ?ci ?country WHERE {\n"
            + "?p a ears2:Project .\n"
            + "?p geo:parentCountry ?c .\n"
            + "?p dbpedia:projectCoordinator ?o .\n"
            + "?p ears2:asConcept ?pc .\n"
            + "?pc dc:identifier ?_pi .\n"
            + "?pc skos:prefLabel ?_pl .\n"
            + "?pc skos:altLabel ?_pal .\n"
            + "?o ears2:asConcept ?oc .\n"
            + "?oc dc:identifier ?_oi .\n"
            + "?oc skos:prefLabel ?_ol .\n"
            + "?c ears2:asConcept ?cc .\n"
            + "?cc dc:identifier ?_ci .\n"
            + "?cc skos:prefLabel ?_cl .\n"
            + "FILTER ( ?country IN (%s) )\n"
            + "bind( str(?_pal) as ?acronym )\n"
            + "bind( str(?_pi) as ?code )\n"
            + "bind( str(?_oi) as ?oi )\n"
            + "bind( str(?_ci) as ?ci )\n"
            + "bind( str(?_pl) as ?name )\n"
            + "bind( str(?_ol) as ?organisation )\n"
            + "bind( str(?_cl) as ?country )\n"
            + "} ORDER BY ?cl ?pl";

    public static final String countryQuery = "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
            + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
            + "PREFIX ears2: <http://ontologies.ef-ears.eu/ears2/1#>\n"
            + "PREFIX geo: <http://www.geonames.org/ontology#> \n"
            + "\n"
            + "SELECT DISTINCT ?country {\n"
            + "{SELECT distinct ?country WHERE {\n"
            + "?s a ears2:Harbour .\n"
            + "?s geo:parentCountry ?ct .\n"
            + "?ct ears2:asConcept ?c2 .\n"
            + "?c2 dc:identifier ?_i2 .\n"
            + "?c2 skos:prefLabel ?_l2   \n"
            + "bind( str(?_l2) as ?country )   \n"
            + "}}\n"
            + "UNION\n"
            + "{SELECT distinct ?country WHERE {\n"
            + "?s a ears2:Organisation .\n"
            + "?s geo:parentCountry ?ct .\n"
            + "?ct ears2:asConcept ?c2 .\n"
            + "?c2 dc:identifier ?_i2 .\n"
            + "?c2 skos:prefLabel ?_l2   \n"
            + "bind( str(?_l2) as ?country )   \n"
            + "}}\n"
            + "UNION\n"
            + "{SELECT distinct ?country WHERE {\n"
            + "?s a ears2:Project .\n"
            + "?s geo:parentCountry ?ct .\n"
            + "?ct ears2:asConcept ?c2 .\n"
            + "?c2 dc:identifier ?_i2 .\n"
            + "?c2 skos:prefLabel ?_l2   \n"
            + "bind( str(?_l2) as ?country )   \n"
            + "}} \n"
            + "} ORDER BY ?country";

    public static final Map<Class<? extends EARSConcept>, String> sparqlQueries;

    static {
        sparqlQueries = new THashMap<>();
        sparqlQueries.put(VesselBean.class, vesselQuery);
        sparqlQueries.put(SeaAreaBean.class, seaAreaQuery);
        sparqlQueries.put(HarbourBean.class, harbourQuery);
        sparqlQueries.put(OrganisationBean.class, organisationQuery);
        sparqlQueries.put(ProjectBean.class, projectQuery);
        sparqlQueries.put(CountryBean.class, countryQuery);
    }

    private final Class<C> type;

    private List<C> concepts;

    JsonWrapper<C> wrapper;

    public StaticMetadataManager(Class<C> type) {
        this.type = type;
    }

    public List<C> getConcepts() {
        return concepts;
    }

    public List<C> readMetadataFromFile() throws FileNotFoundException {
        /* catch (FileNotFoundException ex) {
            Messaging.report("The static vessel metadata can't be found.", ex, this.getClass(), true);
        }*/
        JsonWrapper<C> wrapper = null;

        wrapper = readJsonMetadata(this.type);
        concepts = wrapper.concepts;
        return concepts;
    }

    /**
     * *
     * Renews the metadata file for the given class. Executes a sparql query to
     * the EARS sparql endpoint to build the necessary json files locally.
     *
     *
     * @param overwriteFileEvenIfNotOutdated overwrite the metadata file even if it is not outdated
     * @return Returns a list of entities contained in the json if the list was
     * updated. Returns a list without entries if the list needed no update (the
     * file was up-to-date and the ontology server was online).
     * @throws EarsException. Throw an exception when the EARS ontology server
     * is offline (eithe rthe user is not connected or the system is down).
     */
    public List<C> refreshMetadataFile(boolean overwriteFileEvenIfNotOutdated) throws EarsException {
        JsonWrapper<C> wrapper = null;
        try {
            wrapper = readMetadataDate(this.type);
            if (Constants.EARS_ONTOLOGY_RETRIEVER != null) {

                //if (EARSOntologyRetriever.ontologyServiceIsAvailable()) {
                //if (wrapper == null || wrapper.toDate() == null || EARSOntologyRetriever.retrieveLatestOntologyDate().after(wrapper.toDate())) {
                if (overwriteFileEvenIfNotOutdated || wrapper == null || wrapper.toDate() == null || Constants.EARS_ONTOLOGY_RETRIEVER.getLatestOntologyDate().after(wrapper.toDate())) {
                    //t1 = System.nanoTime();
                    concepts = writeJsonMetadata(this.type);
                    //t2 = System.nanoTime();// / 1000000000.0;
                } else {
                    return new ArrayList();
                }
            } else if (wrapper == null || wrapper.toDate() == null) {
                throw new EarsException("The static metadata for type '" + type + "' can't be found and the remote ontology service can't be reached. The program won't function properly.");
            } else {
                concepts = readMetadataFromFile();
            }
        } catch (FileNotFoundException ex) { //if the ontology server fails to parse its messages or if the file is not found
            concepts = writeJsonMetadata(this.type); //try to retrieve questions from the SPARQL point
        }
        return concepts;
    }

    private <C extends EARSConcept> List<C> writeJsonMetadata(Class<C> cls) {
        List<C> concepts = new ArrayList();
        try {
            concepts = readConceptsFromSparql(sparqlQueries.get(cls), cls);
            String fileLocation = Constants.METADATA_FILES.get(cls);
            try (Writer writer = new FileWriter(fileLocation)) {
                JsonWrapper<C> output = new JsonWrapper(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT), concepts);
                Gson gson = new GsonBuilder().create();

                gson.toJson(output, writer);
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return concepts;
    }

    /***
     * Retrieve a list of Class cls objects by the given SPARQL query.
     * @param <C>
     * @param sparqlQuery
     * @param cls
     * @return
     * @throws IOException 
     */
    private static <C extends EARSConcept> List<C> readConceptsFromSparql(String sparqlQuery, Class<C> cls) throws IOException {

        sparqlQuery = sparqlQuery.replace("\n", " ");

        Set<String> countries = Configs.getCountries();
        Set<String> countries2 =new HashSet();
        
        String countryQuery = null;
        if (countries == null) {
            countryQuery = "\"Belgium\", \"Spain\",\"Netherlands\",\"France\", \"Italy\",\"Germany\", \"Sweden\"";
        } else {
            for (String country : countries) {
                StringBuilder countryStringBuilder = new StringBuilder();
                countryStringBuilder.append("\"");
                countryStringBuilder.append(country);
                countryStringBuilder.append("\"");
                country = countryStringBuilder.toString();
                countries2.add(country);
            }
            countryQuery = String.join(",", countries2);
        }

        sparqlQuery = String.format(sparqlQuery, countryQuery);

        try {
            sparqlQuery = URLEncoder.encode(sparqlQuery, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Exceptions.printStackTrace(ex);
        }

        StringBuilder stringBuilder = new StringBuilder(sparqlQuery);
        stringBuilder.insert(0, OntologyConstants.SPARQL_ENDPOINT);
        stringBuilder.append("&output=json");
        String url = stringBuilder.toString();

        JSONObject obj = JSONReader.readJsonFromUrl(url);
        //String pageName = obj.getJSONObject("pageInfo").getString("pageName");

        JSONArray arr = obj.getJSONObject("results").getJSONArray("bindings");
        int l = arr.length();
        List<C> result = new ArrayList(l);
        Map<String, Method> setters = ReflectionUtils.getSettersAndFields(cls);
        Map<Method, Object> setterValues = new THashMap<>();
        for (int i = 0; i < l; i++) {

            JSONObject vessel = arr.getJSONObject(i);
            Set<String> fields = vessel.keySet();
            for (String field : fields) {
                Method setter = setters.get(field);
                if (setter != null) {
                    String value = vessel.getJSONObject(field).getString("value");
                    setterValues.put(setter, value);
                }
            }

            C c = EARSConceptFactory.create(setterValues, cls);
            result.add(c);
        }
        return result;
    }

    private static void ConnectMetadata(List<OrganisationBean> organisations, List<ProjectBean> projects, List<HarbourBean> harbours, Set<CountryBean> countries) {
        //countries = new TreeSet();
        if (countries != null) {
            for (OrganisationBean org : organisations) {
                if (org != null) {
                    if (org.getCountry() != null) {
                        CountryBean country = org.getCountryObject();
                        country.setHasOrganisations(true);
                        countries.add(country);
                    }
                    String organisationName = org.getName();
                    if (organisationName != null && !organisationName.isEmpty()) {

                        if (organisationName.contains("\r\n") || organisationName.contains("\n")) {
                            organisationName = organisationName.replaceAll("(\r\n|\n)", " ");
                            org.setName(organisationName);
                        }
                        for (ProjectBean project : projects) {
                            String projectOrgFullName = project.getOrganisation(); //it's inited from the full org name but we need the edmo code
                            if (projectOrgFullName != null && !projectOrgFullName.isEmpty()) {
                                if (projectOrgFullName.equalsIgnoreCase(organisationName)) {
                                    project.setOrganisationObject(org);
                                }
                            }
                        }

                    }
                }
            }
            List noMatch = new ArrayList();
            for (ProjectBean project : projects) {
                if (project.getOrganisation() != null && project.getOrganisationObject() == null) {
                    noMatch.add(project.getOrganisation());
                    /*OrganisationBean newOrg = new OrganisationBean("NO_SDN_CODE_" + c, project.getOrganisation(), project.getCountry());
                 project.setOrganisationObject(newOrg);
                 if (organisations.add(newOrg)) {
                 c++;
                 }*/
                }
            }
            //  for (ICountry country : countries) {
            for (HarbourBean harbour : harbours) {
                if (harbour != null && harbour.getCountry() != null) {
                    CountryBean country = harbour.getCountryObject();
                    country.setHasHarbours(true);
                    boolean hasOrganisations = false;
                    for (int i = 0; i < countries.size(); i++) {
                        ArrayList<CountryBean> list = new ArrayList(countries);
                        if (list.get(i).equals(country)) {
                            countries.remove(country);
                            hasOrganisations = list.get(i).hasOrganisations();
                        }
                    }
                    country.setHasOrganisations(hasOrganisations);
                    countries.add(country);
                }
            }
        } else {
            throw new IllegalArgumentException("Countries can't be null.");
        }
    }

    /**
     * *
     * Read the Metadate json file and retrieve its date
     *
     * @param cls
     * @return
     * @throws FileNotFoundException
     */
    private JsonWrapper readMetadataDate(Class<C> cls) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        String fileLocation = Constants.METADATA_FILES.get(cls);
        FileReader fileReader = new FileReader(fileLocation);
        com.google.gson.stream.JsonReader reader = new com.google.gson.stream.JsonReader(fileReader);
        JsonObject root = parser.parse(reader).getAsJsonObject();
        String date = root.get("date").getAsString();

        return new JsonWrapper(date, null);
    }

    /**
     * *
     * Read the Metadate json file and retrieve its date and all its elements
     *
     * @param <C>
     * @param cls
     * @return
     * @throws FileNotFoundException
     */
    private <C extends EARSConcept> JsonWrapper<C> readJsonMetadata(Class<C> cls) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        String fileLocation = Constants.METADATA_FILES.get(cls);
        if (fileLocation == null) {
            throw new IllegalArgumentException("Can't find json metadata file for class " + cls);
        }
        FileReader fileReader = new FileReader(fileLocation);
        com.google.gson.stream.JsonReader reader = new com.google.gson.stream.JsonReader(fileReader);
        JsonObject root = parser.parse(reader).getAsJsonObject();
        String date = root.get("date").getAsString();
        JsonArray concepts = root.get("concepts").getAsJsonArray();
        // JsonArray conceptElements = concepts.getAsJsonArray();
        List results = new ArrayList();
        for (JsonElement conceptElement : concepts) {
            C c = gson.fromJson(conceptElement, cls);
            if (c != null) {
                results.add(c);
            }
        }
        return new JsonWrapper(date, results);
    }

}
