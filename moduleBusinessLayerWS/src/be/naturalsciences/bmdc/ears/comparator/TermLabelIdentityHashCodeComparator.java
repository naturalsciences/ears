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
public class TermLabelIdentityHashCodeComparator implements Comparator<AsConcept> {

    @Override
    public int compare(AsConcept a, AsConcept b) {

        try {
            int result = 0;
            if (a.getUri() != null) {
                result = a.getUri().toASCIIString().compareToIgnoreCase(b.getUri().toASCIIString());
            }
            if (result == 0) {
                return Integer.compare(System.identityHashCode(a), System.identityHashCode(b));
            } else {
                return result;
            }
        } catch (NullPointerException e) {
            return 0;//cannot compare them, they are equals
        }

    }
}
