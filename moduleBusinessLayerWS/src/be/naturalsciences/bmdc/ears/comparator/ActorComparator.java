/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.comparator;

import be.naturalsciences.bmdc.ears.entities.Actor;
import java.util.Comparator;

/**
 *
 * @author thomas
 */
/**
 *
 * @author Thomas Vandenberghe
 */
public class ActorComparator  implements Comparator<Actor> {

    @Override
    public int compare(Actor a, Actor b) {
        try {
            return a.getLastNameFirstName().compareToIgnoreCase(b.getLastNameFirstName());
        } catch (NullPointerException e) {
            return 0;//cannot compare them, they are equals
        }
    }
}
