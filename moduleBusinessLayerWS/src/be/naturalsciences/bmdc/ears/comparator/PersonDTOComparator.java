/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.comparator;

import eu.eurofleets.ears3.dto.PersonDTO;
import java.util.Comparator;

/**
 *
 * @author thomas
 */
public class PersonDTOComparator implements Comparator<PersonDTO> {

    @Override
    public int compare(PersonDTO a, PersonDTO b) {
        try {
            return a.getLastNameFirstName().compareToIgnoreCase(b.getLastNameFirstName());
        } catch (NullPointerException e) {
            return 0;//cannot compare them, they are equals
        }
    }
}
