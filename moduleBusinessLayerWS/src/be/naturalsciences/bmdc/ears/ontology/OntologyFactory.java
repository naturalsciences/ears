/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.OntologyConstants;
import be.naturalsciences.bmdc.ontology.writer.ScopeMap;
import be.naturalsciences.bmdc.ontology.writer.ScopeMap.Scope;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author thomas
 */
public class OntologyFactory {

    public static IOntologyModel getOntology(OntologyDataObject object) {
        return getOntology(object.getFile());
    }

    public static IOntologyModel getOntology(File file) {
        Scope scope = null;
        try {
            scope = ScopeMap.Scope.valueOf(IOntologyModel.getStaticStuff(file).get(IOntologyModel.SCOPE));
        } catch (NullPointerException | FileNotFoundException e) {
            //throw new IOException("NullPointerException when trying to read the scope of file " + file.getAbsolutePath() + ".");

            if (file.getName().equals(OntologyConstants.BASE_ONTOLOGY_FILENAME)) {
                // scope = Scope.BASE;
                return new BaseOntology(file);
            } else if (file.getName().equals(OntologyConstants.VESSEL_ONTOLOGY_FILENAME)) {
                // scope = Scope.VESSEL;
                return new VesselOntology(file);
            } else if (file.getName().equals(OntologyConstants.TEST_ONTOLOGY_FILENAME)) {
                // scope = Scope.TEST;
                return new TestOntology(file);
            } else if (file.getName().equals(OntologyConstants.STATIC_ONTOLOGY_FILENAME)) {
                //scope = Scope.STATIC;
                return new StaticOntology(file);
            } else {
                // scope = Scope.PROGRAM;
                return new ProgramOntology(file);
            }
        }
        if (scope == null) {
            return null;
        }
        try {
            if (scope.equals(ScopeMap.Scope.BASE)) {
                return new BaseOntology(file, true, true);
            } else if (scope.equals(ScopeMap.Scope.VESSEL)) {
                return new VesselOntology(file, false, true);
            } else if (scope.equals(ScopeMap.Scope.PROGRAM)) {
                return new ProgramOntology(file, false, true);
            } else if (scope.equals(ScopeMap.Scope.STATIC)) {
                return new StaticOntology(file, true, true);
            } else if (scope.equals(ScopeMap.Scope.TEST)) {
                return new TestOntology(file, false, true);
            }
        } catch (IOException ex) {
            return null;
        }
        return null;
    }
}
