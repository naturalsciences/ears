/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.comparator;

import be.naturalsciences.bmdc.ears.entities.ICountry;
import java.util.Comparator;

/**
 *
 * @author thomas
 */
public class CountryComparator implements Comparator<ICountry> {

    @Override
    public int compare(ICountry a, ICountry b) {
        try {
            return a.getName().compareToIgnoreCase(b.getName());
        } catch (NullPointerException e) {
            return 0;//cannot compare them, they are equals
        }

    }
}
