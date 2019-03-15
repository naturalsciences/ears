/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.entities.VesselBean;
import be.naturalsciences.bmdc.ears.properties.Constants;
import be.naturalsciences.bmdc.ears.rest.RestClientProgram;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import gnu.trove.set.hash.THashSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.openide.util.Exceptions;

/**
 *
 * @author thomas
 */
public class OntologySynchronizer {

    public static void synchronizeLatestOntologyAxiom() throws IOException {
        if (Constants.EARS_ONTOLOGY_RETRIEVER != null) { //if I am online
            File f = new File(Constants.LATEST_ONLINE_ONTOLOGY_AXIOM_LOCATION);
            if (!f.exists() && !f.isDirectory()) {
                Constants.EARS_ONTOLOGY_RETRIEVER.downloadLatestOntologyAxiom(new File(Constants.ONTO_DIR));
            }
        }
    }

    public static void synchronizeBaseOntology() {
        String defaultName = BaseOntology.getPreferredName();
        File baseOntologyFile = new File(Constants.ONTO_DIR, defaultName + ".rdf");
        try {
            synchronizeOntology(baseOntologyFile);
        } catch (ConnectException ex) {
            Messaging.report("Could not synchronize the base ontology.", ex, OntologySynchronizer.class, true);
        } catch (EarsException ex) {
            Messaging.report("Could not synchronize the base ontology.", ex, OntologySynchronizer.class, true);
        }
    }

    public static void synchronizeVesselOntology() {
        String defaultName = VesselOntology.getPreferredName();
        File vesselOntologyFile = new File(Constants.ONTO_DIR, defaultName + ".rdf");
        try {
            synchronizeOntology(vesselOntologyFile);
        } catch (ConnectException ex) {
            Messaging.report("Could not synchronize the vessel ontology.", ex, OntologySynchronizer.class, true);
        } catch (EarsException ex) {
            Messaging.report("Could not synchronize the vessel ontology.", ex, OntologySynchronizer.class, true);
        }
    }

    public static void synchronizeAllProgramOntologies(VesselBean vessel) {

        RestClientProgram programClient;
        Collection<ProgramBean> programs = null;
        try {
            programClient = new RestClientProgram();
            //programs = programClient.getProgramByVessel(vessel);
            programs = programClient.getAllPrograms(); //faster
        } catch (ConnectException ex) {
            Messaging.report("Could not read the programs from the webservice because it is offline or its url is incorrect.", ex, OntologySynchronizer.class, true);
        } catch (EarsException ex) {
            Messaging.report(ex.getMessage(), ex, OntologySynchronizer.class, true);
        }
        Set<String> programIds = new THashSet<>();
        if (programs != null) {
            for (ProgramBean programBean : programs) {
                programIds.add(programBean.cleanName());
            }
            for (String programId : programIds) {
                File trialFile = new File(Constants.TREES_DIR, programId + ".rdf");
                try {
                    synchronizeOntology(trialFile);
                } catch (ConnectException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (EarsException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

    }

    /**
     * *
     * Download an ontologyFile of any kind given a File. Use this if the
     * ontology has not yet been downloaded.
     *
     * @param ontologyFile
     * @param defaultFileName
     */
    public static void synchronizeOntology(File ontologyFile) throws ConnectException, EarsException {
        IOntologyModel ontology = OntologyFactory.getOntology(ontologyFile);
        synchronizeOntology(ontology);
    }

    /**
     * *
     * Download an ontologyFile of any kind given an ontology. Use this if the
     * ontology has already been downloaded.
     *
     * @param ontologyFile
     * @param defaultFileName
     */
    public static void synchronizeOntology(IOntologyModel ontology) throws ConnectException, EarsException {
        File ontologyFile = ontology.getFile();
        String fileName = ontologyFile.getName();
        // Path folder = ontologyFile.toPath().getParent();
        try {
            Map<String, String> staticStuff = IOntologyModel.getStaticStuff(ontologyFile);
            Date localDate = OntologyModel.getMostSpecificDate(staticStuff);

            if (ontology.isOutdated(localDate)) {
                File file = ontology.downloadLatestVersion(fileName);

                if (file != null) {
                    Message msg = new Message("Newer version of the file " + ontologyFile.getName() + " found and downloaded.", null, OntologySynchronizer.class,
                            false, Message.State.INFO);
                    Messaging.report(msg);
                }
            }

        } catch (FileNotFoundException ex) {
            File file = ontology.downloadLatestVersion(fileName);

            if (file != null) {
                Message msg = new Message("First time the file " + ontologyFile.getName() + " was encountered; file downloaded.", null, OntologySynchronizer.class,
                        false, Message.State.INFO);
                Messaging.report(msg);
            }
        }
    }

}
