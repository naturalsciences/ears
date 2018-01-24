package be.naturalsciences.bmdc.ears.comparator;

import be.naturalsciences.bmdc.ontology.entities.AsConcept;
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
public class TermUriComparator implements Comparator<AsConcept> {

    @Override
    public int compare(AsConcept a, AsConcept b) {
        try {
            return a.getUri().toASCIIString().compareToIgnoreCase(b.getUri().toASCIIString());
        } catch (NullPointerException e) {
            return 0;//cannot compare them, they are equals
        }

    }
}
