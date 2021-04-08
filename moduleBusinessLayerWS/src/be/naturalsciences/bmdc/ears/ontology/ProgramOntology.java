/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

import be.naturalsciences.bmdc.ears.properties.Constants;
import be.naturalsciences.bmdc.ears.rest.RestClientOnt;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IProgramOntology;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Date;

/**
 *
 * @author thomas
 */
public class ProgramOntology extends OntologyModel implements IProgramOntology {

    public ProgramOntology(File localFile, boolean killModelAfterNodeCalculation, boolean reasoning) throws FileNotFoundException, IOException {
        super(localFile, killModelAfterNodeCalculation, reasoning);
    }

    public ProgramOntology(File localFile) {
        super(localFile);
    }

    public static String getPreferredName() {
        return null;
    }

    @Override
    public String getName() {
        return getScopedTo();
    }

    @Override
    public File downloadLatestVersion(String name) {
        return downloadLatestVersion(new File(Constants.TREES_DIR), name);
    }

    @Override
    public Boolean isOutdated(Date ownDate) throws ConnectException, EarsException {
        RestClientOnt client = null;
        client = new RestClientOnt();
        Date remoteDate = client.getOntologyDate(getName());
        if (remoteDate != null) {
            return ownDate.before(remoteDate);
        } else {
            return false;
        }
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public boolean isPasswordProtected() {
        return false;
    }

}
