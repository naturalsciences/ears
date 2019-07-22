/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.comparator;

import be.naturalsciences.bmdc.ears.entities.EventBean;
import be.naturalsciences.bmdc.ears.entities.PropertyBean;
import java.util.Comparator;

/**
 *
 * @author thomas
 */
public class EventPropertyComparator implements Comparator<PropertyBean> {

    @Override
    public int compare(PropertyBean t, PropertyBean t1) {
        int result = t.getName().compareTo(t1.getName());

        if (result == 0) {
            return t.getValue().compareTo(t1.getValue());
        } else {
            return result;
        }
    }

}
