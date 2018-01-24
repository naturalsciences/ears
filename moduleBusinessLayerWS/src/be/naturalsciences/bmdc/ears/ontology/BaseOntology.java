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
        if (Constants.EARS_ONTOLOGY_RETRIEVER != null) {
            File downloadedFile = Constants.EARS_ONTOLOGY_RETRIEVER.downloadLatestOntology(new File(Constants.ONTO_DIR));
            if (downloadedFile == null) {
                Messaging.report("There was a problem with the downloaded vessel tree.", Message.State.BAD, BaseOntology.class, true);
            }
            return downloadedFile;
        } else {
            Messaging.report("The BASE tree is outdated, but downloading a replacement tree failed.", Message.State.BAD, BaseOntology.class, false);
        }
        return null;
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

}
