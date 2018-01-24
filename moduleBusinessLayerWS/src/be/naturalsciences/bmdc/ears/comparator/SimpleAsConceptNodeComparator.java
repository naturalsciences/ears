/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.comparator;

import be.naturalsciences.bmdc.ears.ontology.gui.SimpleAsConceptNode;
import java.util.Comparator;

/**
 *
 * @author thomas
 */
public class SimpleAsConceptNodeComparator  implements Comparator<SimpleAsConceptNode> {

    @Override
    public int compare(SimpleAsConceptNode a, SimpleAsConceptNode b) {
        try {
            return a.getDisplayName().compareToIgnoreCase(b.getDisplayName());
        } catch (NullPointerException e) {
            return 0;//cannot compare them, they are equals
        }

    }
    
}
