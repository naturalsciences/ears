/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.comparator;

import be.naturalsciences.bmdc.ears.entities.EventBean;
import java.util.Comparator;

/**
 *
 * @author thomas
 */
public class EventPropertyComparator implements Comparator<EventBean.Property> {

    @Override
    public int compare(EventBean.Property t, EventBean.Property t1) {
        int result = t.name.compareTo(t1.name);

        if (result == 0) {
            return t.value.compareTo(t1.value);
        } else {
            return result;
        }
    }

}
