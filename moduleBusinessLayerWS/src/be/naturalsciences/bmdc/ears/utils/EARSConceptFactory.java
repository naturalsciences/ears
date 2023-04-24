/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.utils;

import be.naturalsciences.bmdc.ears.entities.EARSConcept;
import java.lang.reflect.Method;
import java.util.Map;
import org.openide.util.Exceptions;

/**
 *
 * @author thomas
 */
public class EARSConceptFactory {

    public static <C extends EARSConcept> C create(Map<Method, Object> map, Class<C> cls) {
        C c = null;
        try {
            c = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
        if (c != null) {
            for (Map.Entry<Method, Object> entry : map.entrySet()) {
                Method method = entry.getKey();
                Object value = entry.getValue();
                try {
                    method.invoke(c, value);
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (c.isLegal()) {
                return c;
            } else {
                return null;
            }
        }
        return null;
    }
}
