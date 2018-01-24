/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.properties;

import be.naturalsciences.bmdc.ears.entities.CountryBean;
import be.naturalsciences.bmdc.ears.entities.HarbourBean;
import be.naturalsciences.bmdc.ears.entities.IHarbour;
import be.naturalsciences.bmdc.ears.entities.IOrganisation;
import be.naturalsciences.bmdc.ears.entities.IProject;
import be.naturalsciences.bmdc.ears.entities.ISeaArea;
import be.naturalsciences.bmdc.ears.entities.IVessel;
import be.naturalsciences.bmdc.ears.entities.OrganisationBean;
import be.naturalsciences.bmdc.ears.entities.ProjectBean;
import be.naturalsciences.bmdc.ears.entities.SeaAreaBean;
import be.naturalsciences.bmdc.ears.entities.VesselBean;
import be.naturalsciences.bmdc.ears.ontology.entities.Action;
import be.naturalsciences.bmdc.ears.ontology.entities.Country;
import be.naturalsciences.bmdc.ears.ontology.entities.EarsTerm;
import be.naturalsciences.bmdc.ears.ontology.entities.EventDefinition;
import be.naturalsciences.bmdc.ears.ontology.entities.GenericEventDefinition;
import be.naturalsciences.bmdc.ears.ontology.entities.Harbour;
import be.naturalsciences.bmdc.ears.ontology.entities.Incident;
import be.naturalsciences.bmdc.ears.ontology.entities.ObjectValue;
import be.naturalsciences.bmdc.ears.ontology.entities.Organisation;
import be.naturalsciences.bmdc.ears.ontology.entities.Parameter;
import be.naturalsciences.bmdc.ears.ontology.entities.Process;
import be.naturalsciences.bmdc.ears.ontology.entities.ProcessStep;
import be.naturalsciences.bmdc.ears.ontology.entities.Property;
import be.naturalsciences.bmdc.ears.ontology.entities.SeaArea;
import be.naturalsciences.bmdc.ears.ontology.entities.SpecificEventDefinition;
import be.naturalsciences.bmdc.ears.ontology.entities.Subject;
import be.naturalsciences.bmdc.ears.ontology.entities.Tool;
import be.naturalsciences.bmdc.ears.ontology.entities.ToolCategory;
import be.naturalsciences.bmdc.ears.ontology.entities.Vessel;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.OntologyConstants;
import be.naturalsciences.bmdc.ontology.OntologyConstants.ConceptMD;
import be.naturalsciences.bmdc.ontology.entities.EARSThing;
import be.naturalsciences.bmdc.ontology.writer.EARSOntologyRetrievalException;
import be.naturalsciences.bmdc.ontology.writer.EARSOntologyRetriever;
import be.naturalsciences.bmdc.ontology.writer.FileUtils;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Exceptions;

/**
 *
 * @author Thomas Vandenberghe
 */
public class Constants {

    /**
     * *
     * Find the latest online ontology axiom filename on the ontology server. If
     * not found, return null.
     *
     * @return
     */
    private static String getLatestOnlineOntologyAxiomFileName() {
        if (EARS_ONTOLOGY_RETRIEVER != null) {
            return EARS_ONTOLOGY_RETRIEVER.getLatestOntologyAxiomFileName();
        } else {
            return null;
        }
    }

    /**
     * *
     * Find the latest ontology axiom filename that is stored in config/onto. If
     * not found, return null.
     *
     * @return
     */
    private static String getLatestLocalOntologyAxiomFileName() {
        File result = FileUtils.findLastFileInDirByWildCard(ONTO_DIR, OntologyConstants.ONTOLOGY_AXIOM_FILENAME_BASE + "*", "xml");
        if (result != null && result.exists() && !result.isDirectory()) {
            return result.getName();
        }
        return null;
    }

    public static final File BASE_DIR_FILE;
    private static final File USER_DIR_FILE;
    /**
     * The object that enables retrieving information from the ontology server.
     * Is constructed as null if the application is offline or if the ears
     * ontology web server is down.
     */
    public static EARSOntologyRetriever EARS_ONTOLOGY_RETRIEVER;

    public static final String TREES_FOLDER_PATH = "trees";
    public static final String CONFIG_FOLDER_PATH = "config";

    public static final String SEPARATOR = System.getProperty("file.separator");

    static {
        try {
            EARS_ONTOLOGY_RETRIEVER = new EARSOntologyRetriever();
        } catch (ConnectException | EARSOntologyRetrievalException | MalformedURLException ex) {
            EARS_ONTOLOGY_RETRIEVER = null;
        } catch (Exception ex) {
            EARS_ONTOLOGY_RETRIEVER = null;
        }
        if (new File(System.getProperty("user.dir"), "bin").exists()) {
            BASE_DIR_FILE = new File(System.getProperty("user.dir"));
        } else {
            BASE_DIR_FILE = new File(System.getProperty("user.dir")).getParentFile();
        }
        if (System.getProperty("netbeans.user") != null) {
            if (new File(System.getProperty("netbeans.user")).exists()) {
                USER_DIR_FILE = new File(System.getProperty("netbeans.user"));
            } else {
                USER_DIR_FILE = new File(System.getProperty("netbeans.user")).getParentFile();
            }
        } else {
            USER_DIR_FILE = BASE_DIR_FILE;
        }
        File listsDir = new File(USER_DIR_FILE + SEPARATOR + "config" + SEPARATOR + "lists");
        File ontoDir = new File(USER_DIR_FILE + SEPARATOR + "config" + SEPARATOR + "onto");
        if (!listsDir.exists()) {
            File f2 = InstalledFileLocator.getDefault().locate("config/lists", null, false); //get a copy of the files that come preinstalled

            if (f2 != null && f2.exists()) {
                /* try {
                    FileUtils.copyDirectory(f2, listsDir);
                    FileUtils.deleteDirectory(f2);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }*/
            } else {
                listsDir.mkdirs();
            }
        }
        if (!ontoDir.exists()) {
            File f3 = InstalledFileLocator.getDefault().locate("config/onto", null, false); //get a copy of the files that come preinstalled
            if (f3 != null && f3.exists()) {
                try {
                    FileUtils.copyDirectory(f3, ontoDir);
                    FileUtils.deleteDirectory(f3);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else {
                ontoDir.mkdirs();  //they are not packaged along so make the dir, as they will later be downloaded
            }

        }

    }

    public static final String BASE_DIR = USER_DIR_FILE.getAbsolutePath();

    //public static final String HIDDEN_ONTO_PATH = BASE_DIR + SEPARATOR + CONFIG_FOLDER_PATH + SEPARATOR + "onto";
    public static final String SDN_LISTS_PATH = CONFIG_FOLDER_PATH + SEPARATOR + "lists";

    public static final String CONFIG_FILENAME = "config.properties";
    public static final String ACTORS_FILENAME = "actor.properties";
    public static final String REST_FILENAME = "rest.properties";
    public static final String CONFIG_LOCATION = BASE_DIR + SEPARATOR + CONFIG_FOLDER_PATH + SEPARATOR + CONFIG_FILENAME;
    public static final String ACTORS_LOCATION = BASE_DIR + SEPARATOR + CONFIG_FOLDER_PATH + SEPARATOR + ACTORS_FILENAME;
    public static final String REST_LOCATION = BASE_DIR + SEPARATOR + CONFIG_FOLDER_PATH + SEPARATOR + REST_FILENAME;

    public static final Map<Class, String> METADATA_FILES = new THashMap<>();

    static {
        METADATA_FILES.put(IVessel.class, BASE_DIR + SEPARATOR + SDN_LISTS_PATH + SEPARATOR + "C17.json");
        METADATA_FILES.put(ISeaArea.class, BASE_DIR + SEPARATOR + SDN_LISTS_PATH + SEPARATOR + "C19.json");
        METADATA_FILES.put(IHarbour.class, BASE_DIR + SEPARATOR + SDN_LISTS_PATH + SEPARATOR + "C38.json");
        METADATA_FILES.put(IOrganisation.class, BASE_DIR + SEPARATOR + SDN_LISTS_PATH + SEPARATOR + "EDMO.json");
        METADATA_FILES.put(IProject.class, BASE_DIR + SEPARATOR + SDN_LISTS_PATH + SEPARATOR + "EDMERP.json");

        METADATA_FILES.put(VesselBean.class, BASE_DIR + SEPARATOR + SDN_LISTS_PATH + SEPARATOR + "C17.json");
        METADATA_FILES.put(SeaAreaBean.class, BASE_DIR + SEPARATOR + SDN_LISTS_PATH + SEPARATOR + "C19.json");
        METADATA_FILES.put(HarbourBean.class, BASE_DIR + SEPARATOR + SDN_LISTS_PATH + SEPARATOR + "C38.json");
        METADATA_FILES.put(OrganisationBean.class, BASE_DIR + SEPARATOR + SDN_LISTS_PATH + SEPARATOR + "EDMO.json");
        METADATA_FILES.put(ProjectBean.class, BASE_DIR + SEPARATOR + SDN_LISTS_PATH + SEPARATOR + "EDMERP.json");
        METADATA_FILES.put(CountryBean.class, BASE_DIR + SEPARATOR + SDN_LISTS_PATH + SEPARATOR + "countries.json");
    }

    public static final String ONTO_DIR = BASE_DIR + SEPARATOR + CONFIG_FOLDER_PATH + SEPARATOR + "onto" + SEPARATOR;
    public static final String TREES_DIR = BASE_DIR + SEPARATOR + TREES_FOLDER_PATH + SEPARATOR;

    public static final String VESSEL_ONTOLOGY_LOCATION = ONTO_DIR + OntologyConstants.VESSEL_ONTOLOGY_FILENAME;
    public static final String PROGRAM_ONTOLOGY_LOCATION = ONTO_DIR + OntologyConstants.VESSEL_ONTOLOGY_FILENAME;
    public static final String BASE_ONTOLOGY_LOCATION = ONTO_DIR + OntologyConstants.BASE_ONTOLOGY_FILENAME;
    public static final String STATIC_ONTOLOGY_LOCATION = ONTO_DIR + OntologyConstants.STATIC_ONTOLOGY_FILENAME;
    public static String LATEST_ONLINE_ONTOLOGY_AXIOM_LOCATION = ONTO_DIR + getLatestOnlineOntologyAxiomFileName();
    public static String ACTUAL_LOCAL_ONTOLOGY_AXIOM_LOCATION = ONTO_DIR + getLatestLocalOntologyAxiomFileName();

    public static final URI VESSEL_ONTOLOGY_URI;

    public static final File ONTO_DIR_FILE = new File(ONTO_DIR);

    public static final File BASE_ONTOLOGY_FILE = new File(BASE_ONTOLOGY_LOCATION);
    public static final File STATIC_ONTOLOGY_FILE = new File(STATIC_ONTOLOGY_LOCATION);
    public static final File VESSEL_ONTOLOGY_FILE = new File(VESSEL_ONTOLOGY_LOCATION);
    public static final File PROGRAM_ONTOLOGY_DIR = new File(BASE_DIR, TREES_FOLDER_PATH + SEPARATOR);

    public static final File ACTORS_FILE = new File(ACTORS_LOCATION);
    public static final File CONFIG_FILE = new File(CONFIG_LOCATION);
    public static final File REST_FILE = new File(REST_LOCATION);

    static {
        try {
            VESSEL_ONTOLOGY_URI = new URI("");
        } catch (URISyntaxException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public final static Map<Class<? extends EARSThing>, ConceptMD> ALL_CLASSNAMES;
    public final static Map<Class<? extends EARSThing>, ConceptMD> EARS_CLASSNAMES;
    public final static Map<Class<? extends EARSThing>, ConceptMD> STATIC_CLASSNAMES;
    public final static Map<Class<? extends EARSThing>, ConceptMD> EARS_ASCONCEPT_CLASSNAMES;

    static {
        EARS_CLASSNAMES = new THashMap<>();
        EARS_CLASSNAMES.put(EarsTerm.class, OntologyConstants.EARSTERM);
        EARS_CLASSNAMES.put(ToolCategory.class, OntologyConstants.TOOLCATEGORY);
        EARS_CLASSNAMES.put(Tool.class, OntologyConstants.TOOL);
        EARS_CLASSNAMES.put(Vessel.class, OntologyConstants.VESSEL);
        EARS_CLASSNAMES.put(Process.class, OntologyConstants.PROCESS);
        EARS_CLASSNAMES.put(Action.class, OntologyConstants.ACTION);
        EARS_CLASSNAMES.put(Incident.class, OntologyConstants.INCIDENT);
        EARS_CLASSNAMES.put(ProcessStep.class, OntologyConstants.PROCESSSTEP);
        EARS_CLASSNAMES.put(EventDefinition.class, null);
        EARS_CLASSNAMES.put(GenericEventDefinition.class, OntologyConstants.GENERICEVENTDEFINITION);
        EARS_CLASSNAMES.put(SpecificEventDefinition.class, OntologyConstants.SPECIFICEVENTDEFINITION);
        EARS_CLASSNAMES.put(Property.class, OntologyConstants.PROPERTY);
        EARS_CLASSNAMES.put(ObjectValue.class, OntologyConstants.OBJECTVALUE);
        EARS_CLASSNAMES.put(Subject.class, OntologyConstants.SUBJECT);

        STATIC_CLASSNAMES = new THashMap<>();
        STATIC_CLASSNAMES.put(Country.class, OntologyConstants.COUNTRY);
        STATIC_CLASSNAMES.put(Harbour.class, OntologyConstants.HARBOUR);
        STATIC_CLASSNAMES.put(Organisation.class, OntologyConstants.ORGANISATION);
        STATIC_CLASSNAMES.put(Parameter.class, OntologyConstants.PARAMETER);
        STATIC_CLASSNAMES.put(SeaArea.class, OntologyConstants.SEAAREA);
        STATIC_CLASSNAMES.put(Vessel.class, OntologyConstants.VESSEL);

        ALL_CLASSNAMES = new THashMap<>();

        ALL_CLASSNAMES.putAll(EARS_CLASSNAMES);
        ALL_CLASSNAMES.putAll(STATIC_CLASSNAMES);

        EARS_ASCONCEPT_CLASSNAMES = new THashMap<>();
        EARS_ASCONCEPT_CLASSNAMES.putAll(EARS_CLASSNAMES);
        EARS_ASCONCEPT_CLASSNAMES.remove(EarsTerm.class);
        EARS_ASCONCEPT_CLASSNAMES.remove(EventDefinition.class);
        EARS_ASCONCEPT_CLASSNAMES.remove(GenericEventDefinition.class);
    }

    public static Set<Class<? extends EARSThing>> getStaticClasses() {
        return new THashSet<>(STATIC_CLASSNAMES.keySet());
    }

    public static Set<Class<? extends EARSThing>> getEARSClasses() {
        return new THashSet<>(EARS_CLASSNAMES.keySet());
    }

    public static Set<Class<? extends EARSThing>> getEARSAsConceptClasses() {
        return new THashSet<>(EARS_ASCONCEPT_CLASSNAMES.keySet());
    }

    public static Set<Class<? extends EARSThing>> getClassesNeededByClass(Class<? extends EARSThing> cls) {
        if (EARS_CLASSNAMES.keySet().contains(cls)) {
            return new THashSet<>(EARS_CLASSNAMES.keySet());
        } else {
            return new THashSet<>(STATIC_CLASSNAMES.keySet());
        }
    }

    public static void report() {
        Messaging.report("BASE_DIR: " + BASE_DIR, Message.State.INFO, Constants.class, false);
        Messaging.report("CONFIG_FILE: " + CONFIG_FILE, Message.State.INFO, Constants.class, false);
        Messaging.report("PROGRAM_ONTOLOGY_DIR: " + PROGRAM_ONTOLOGY_DIR, Message.State.INFO, Constants.class, false);

        /* Messaging.report("EDMO_LOCATION: " + EDMO_LOCATION, Message.State.INFO, Constants.class, false);
         Messaging.report("C19_LOCATION: " + C19_LOCATION, Message.State.INFO, Constants.class, false);
         Messaging.report("EDMERP_LOCATION: " + EDMERP_LOCATION, Message.State.INFO, Constants.class, false);
         Messaging.report("C17_LOCATION: " + C17_LOCATION, Message.State.INFO, Constants.class, false);
         Messaging.report("L06_LOCATION: " + L06_LOCATION, Message.State.INFO, Constants.class, false);
         Messaging.report("C38_LOCATION: " + C38_LOCATION, Message.State.INFO, Constants.class, false);*/
    }

}
