/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

import be.naturalsciences.bmdc.ears.properties.Constants;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IBaseOntology;
import be.naturalsciences.bmdc.ontology.OntologyConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Date;
import org.openide.util.Exceptions;

/**
 *
 * @author thomas
 */
public class BaseOntology extends OntologyModel implements IBaseOntology {

    public BaseOntology(File localFile, boolean killModelAfterNodeCalculation, boolean reasoning) throws FileNotFoundException, IOException {
        super(localFile, killModelAfterNodeCalculation, reasoning);
    }

    public BaseOntology(File localFile) {
        super(localFile);
    }

    public static String getPreferredName() {
        return OntologyConstants.BASE_ONTOLOGY_FILENAME_NO_EXT;
    }

    @Override
    public String getName() {
        return getPreferredName();
    }

    public static File downloadLatestBaseOntologyVersion() {
        File downloadedFile = null;
        if (Constants.EARS_ONTOLOGY_RETRIEVER != null) {
            try {
                downloadedFile = Constants.EARS_ONTOLOGY_RETRIEVER.downloadLatestOntology(new File(Constants.ONTO_DIR));
                if (downloadedFile == null) {
                    Messaging.report("There was a problem with getting the base tree. It wasn't found on the server or couldn't be written to disk.", Message.State.BAD, BaseOntology.class, true);
                }
            } catch (IOException ex) {
                Messaging.report("There was a problem with getting the base tree. It wasn't found on the server or couldn't be written to disk.", ex, BaseOntology.class, true);
            }
        } else {
            Messaging.report("There was a problem with getting the base tree. No valid EARSOntologRetriever instance available.", Message.State.BAD, BaseOntology.class, true);
        }
        return downloadedFile;
    }

    @Override
    public File downloadLatestVersion(String name) {
        return downloadLatestBaseOntologyVersion();
    }

    public Boolean isOutdated(Date date) throws ConnectException, EarsException {
        if (Constants.EARS_ONTOLOGY_RETRIEVER == null) {
            throw new ConnectException("The remote ontology service (http://ontologies.ef-ears.eu/ears2/1/) is not available");
        } else {
            return Constants.EARS_ONTOLOGY_RETRIEVER.getLatestOntologyDate().after(date);
        }
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public boolean isPasswordProtected() {
        return true;
    }

}
