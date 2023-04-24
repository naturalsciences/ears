/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.validators;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Thomas Vandenberghe
 */
public class SimpleValidator<E> {

    private E object;

    private Map<String, String> errors;

    public SimpleValidator(Object object) {
    }

    SimpleValidator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addError(String property, String text) {
        errors.put(object.hashCode() + "/" + property, text);
    }

    public boolean hasErrors() {
        for (Entry entry : errors.entrySet()) {
            String key = (String) entry.getKey();
            int hash = Integer.parseInt(key.split("/")[0]);

            if (object.hashCode() == hash) {
                return true;
            }
        }
        return false;
    }

    public Map<String, String> getErrors() {
        Map r = new HashMap();
        for (Entry entry : errors.entrySet()) {
            String key = (String) entry.getKey();
            int hash = Integer.parseInt(key.split("/")[0]);
            String prop = key.split("/")[1];
            if (object.hashCode() == hash) {
                r.put(prop, entry.getValue());
            }
        }
        return r;
    }

    public void clearErrors() {
        for (Entry entry : errors.entrySet()) {
            String key = (String) entry.getKey();
            int hash = Integer.parseInt(key.split("/")[0]);
            if (object.hashCode() == hash) {
                errors.remove(key);
            }
        }
    }

    public E getValidatedObject() {
        if (!hasErrors()) {
            return object;
        } else {
            return null;
        }
    }

}
