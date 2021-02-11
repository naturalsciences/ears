/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.netbeans.services;

import be.naturalsciences.bmdc.ears.entities.CurrentSingleton;
import be.naturalsciences.bmdc.ears.entities.IActor;
import be.naturalsciences.bmdc.ears.entities.ICruise;
import be.naturalsciences.bmdc.ears.entities.IProgram;
import be.naturalsciences.bmdc.ears.entities.IVessel;
import java.util.ArrayList;
import java.util.Collection;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author Thomas Vandenberghe
 */
public class SingletonResult<T extends CurrentSingleton, E extends Object> {

    public boolean matches(LookupEvent ev) {
        if (ev.getSource().equals(result)) {
            return true;
        }
        Collection c = ((Lookup.Result) ev.getSource()).allInstances();
        for (Object object : c) {
            if (c.getClass().equals(this.cls)) {
                return true;
            }
        }
        /* Set<Class> classes = ((Lookup.Result) ev.getSource()).allClasses();
        if (classes.contains(cls)) {
            return true;
        } else {
            return false;
        }*/
        return false;
    }

    private Lookup.Result<T> result;
    private Lookup.Result<E> result2;

    private Class<T> cls;

    /*public Lookup.Result<T> getResult() {
     return result;
     }

     public Lookup.Result<E> getResult2() {
     return result2;
     }*/
    public SingletonResult(Class<T> cls, LookupListener listener) {
        this.cls = cls;
        result = Utilities.actionsGlobalContext().lookupResult(cls);
        //result.allInstances()
        result.addLookupListener(listener);
        for (Class c : cls.getInterfaces()) {

            if (c.equals(IVessel.class) || c.equals(ICruise.class) || c.equals(IProgram.class) || c.equals(IActor.class)) {
                result2 = Utilities.actionsGlobalContext().lookupResult(c);
                result2.addLookupListener(listener);
            }
        }
    }

    public Collection<T> allInstances() {
        return (Collection<T>) result.allInstances();
    }

    public T getCurrent() {
        if (this.allInstances().size() > 0) {
            return ((T) new ArrayList(this.allInstances()).get(0));
        }
        return null;
    }

    /*public T getCurrentObject() {
        if (this.allInstances().size() > 0) {
            return new ArrayList<>(this.allInstances()).get(0);
        }
        return null;
    }*/
}
