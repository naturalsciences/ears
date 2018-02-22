/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.utils;

import gnu.trove.map.hash.THashMap;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author thomas
 */
public class ReflectionUtils {

    /***
     * For the given Class, retrieve all methods annotated with the SetterField annotation. 
     * Returns a Map with as key the SetterField name of the setter and as value a reference to the method itself. 
     * This method can be used to bind the key of a json key-value pair to the appropriate setter and add the json value as an argument to the setter.
     * @param type
     * @return 
     */
    public static Map<String, Method> getSettersAndFields(final Class<?> type) {
        final Map<String, Method> setters = new THashMap<>();
        Class<?> klass = type;
        //while (klass != Object.class) { // need to iterated thought hierarchy in order to retrieve methods from above the current instance
        // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
        final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getMethods()));
        for (final Method method : allMethods) {
            if (method.isAnnotationPresent(SetterField.class)) {
                SetterField annotInstance = method.getAnnotation(SetterField.class);
                setters.put(annotInstance.name(), method);
            }
        }
        // move to the upper class in the hierarchy in search for more methods
        // klass = klass.getSuperclass();
        //}
        return setters;
    }
}
