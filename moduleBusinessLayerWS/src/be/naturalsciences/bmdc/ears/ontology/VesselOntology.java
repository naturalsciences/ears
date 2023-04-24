/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.properties.Constants;
import be.naturalsciences.bmdc.ears.rest.RestClientOnt;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IVesselOntology;
import be.naturalsciences.bmdc.ontology.OntologyConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Date;
import org.openide.util.Utilities;

/**
 *
 * @author thomas
 */
public class VesselOntology extends OntologyModel implements IVesselOntology {

    public VesselOntology(File localFile, boolean killModelAfterNodeCalculation, boolean reasoning) throws FileNotFoundException, IOException {
        super(localFile, killModelAfterNodeCalculation, reasoning);
    }

    public VesselOntology(File localFile) {
        super(localFile);
    }

    public static String getPreferredName() {
        return OntologyConstants.VESSEL_ONTOLOGY_FILENAME_NO_EXT;
    }

    @Override
    public String getName() {
        return getPreferredName();
    }

    @Override
    public File downloadLatestVersion(String name) {
        return downloadLatestVersion(new File(Constants.ONTO_DIR), getName() + ".rdf");
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
        CurrentVessel currentVessel = Utilities.actionsGlobalContext().lookup(CurrentVessel.class);
        if (currentVessel != null && currentVessel.getConcept() != null) {
            return currentVessel.getConcept().getCode().equals(scopeMap.getScopedTo()); //only editable if the current vessel is the same as this ontologies' scopedTo value.
        }
        return false;
    }

    @Override
    public boolean isPasswordProtected() {
        return true;
    }

}
