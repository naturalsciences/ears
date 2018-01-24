package be.naturalsciences.bmdc.ears.comparator;

import be.naturalsciences.bmdc.ears.ontology.entities.Tool;
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
public class TermLabelAndDeckIdentifierComparator implements Comparator<AsConcept> {

    @Override
    public int compare(AsConcept a, AsConcept b) {
        try {
            int result = 0;
            if (a instanceof Tool && b instanceof Tool) {
                Tool toolA = (Tool) a;
                Tool toolB = (Tool) b;
                if (toolA.getSerialNumber() != null) {
                    result = toolA.getSerialNumber().compareTo(toolB.getSerialNumber());
                }
                if (result == 0 && toolB.getSerialNumber() != null) {
                    result = -toolB.getSerialNumber().compareTo(toolA.getSerialNumber());
                }
                if (result == 0 && toolA.getToolIdentifier() != null) {
                    result = toolA.getToolIdentifier().compareTo(toolB.getToolIdentifier());
                }
                if (result == 0 && toolB.getToolIdentifier() != null) {
                    result = -toolB.getToolIdentifier().compareTo(toolA.getToolIdentifier());
                }
            }
            if (result == 0) {
                return a.getTermRef().getEarsTermLabel().getPrefLabel().compareToIgnoreCase(b.getTermRef().getEarsTermLabel().getPrefLabel());
            } else {
                return result;
            }
        } catch (NullPointerException e) {
            return -1;//cannot compare them, they are DIFFERENT
        }
    }
}
