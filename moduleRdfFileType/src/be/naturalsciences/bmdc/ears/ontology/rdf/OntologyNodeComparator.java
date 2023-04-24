package be.naturalsciences.bmdc.ears.ontology.rdf;

import java.util.Comparator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Thomas Vandenberghe
 */
public class OntologyNodeComparator implements Comparator<OntologyNode> {

    @Override
    public int compare(OntologyNode a, OntologyNode b) {
        return a.getFileName().compareToIgnoreCase(b.getFileName());
    }
}
