/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IStaticOntology;
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
public class StaticOntology extends OntologyModel implements IStaticOntology {

    public StaticOntology(File localFile, boolean killModelAfterNodeCalculation, boolean reasoning) throws FileNotFoundException, IOException {
        super(localFile, killModelAfterNodeCalculation, reasoning);
    }

    public StaticOntology(File localFile) {
        super(localFile);
    }

    public static String getPreferredName() {
        return OntologyConstants.STATIC_ONTOLOGY_FILENAME_NO_EXT;
    }

    @Override
    public String getName() {
        return getPreferredName();
    }

    @Override
    public File downloadLatestVersion(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean isOutdated(Date date) throws ConnectException, EarsException {
        return false;
    }

}
