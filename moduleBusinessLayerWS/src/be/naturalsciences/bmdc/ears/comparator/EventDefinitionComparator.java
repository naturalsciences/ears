package be.naturalsciences.bmdc.ears.comparator;

import be.naturalsciences.bmdc.ears.ontology.entities.EventDefinition;
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
public class EventDefinitionComparator implements Comparator<EventDefinition> {

    @Override
    public int compare(EventDefinition a, EventDefinition b) {
        /* try {
         int i = a.getProcess().getUri().toASCIIString().compareToIgnoreCase(b.getProcess().getUri().toASCIIString());

         if (i != 0) {
         return i;
         }
         return a.getAction().getUri().toASCIIString().compareToIgnoreCase(b.getAction().getUri().toASCIIString());

         } catch (NullPointerException e) {
         return 0;//cannot compare them, they are equals
         }*/
        return 0;
    }
}
