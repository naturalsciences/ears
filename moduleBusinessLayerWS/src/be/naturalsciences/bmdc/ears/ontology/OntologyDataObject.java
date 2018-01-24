/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

import be.naturalsciences.bmdc.ontology.IOntologyModel;
import java.io.File;

/**
 *
 * @author Thomas Vandenberghe
 */
public interface OntologyDataObject {

    public IOntologyModel getOntModel();
    
    public String getName();
    
    public File getFile();
}
