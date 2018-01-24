package be.naturalsciences.bmdc.ears.netbeans.services;

import be.naturalsciences.bmdc.ears.entities.Actor;
import be.naturalsciences.bmdc.ears.entities.CurrentSingleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.modules.openide.windows.GlobalActionContextImpl;
import org.openide.util.ContextGlobalProvider;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * This class proxies the original ContextGlobalProvider. It provides the
 * ability to add and remove objects from the application-wide global selection.
 *
 * To use this class you must edit the Windows System API module dependency:
 * change the dependency to an implementation version so that the
 * org.netbeans.modules.openide.windows package is on the classpath.
 */
@ServiceProvider(service = ContextGlobalProvider.class,
        supersedes = "org.netbeans.modules.openide.windows.GlobalActionContextImpl")
public class GlobalActionContextProxy implements ContextGlobalProvider {

    /**
     * The native NetBeans global context Lookup provider
     */
    private final GlobalActionContextImpl globalContextProvider;
    /**
     * The primary lookup managed by the platform
     */
    private Lookup globalContextLookup;
    /**
     * The application-wide bulk lookup managed by this class
     */
    private Lookup bulkLookup;
    /**
     * The application-wide singleton lookup managed by this class
     */
    //private Map<Class, Lookup> singletonLookups;
    /**
     * The actual Lookup returned by this class
     */
    private Lookup proxyLookup;
    /**
     * The additional content for our proxy lookup
     */
    private final InstanceContent bulkContent;

    /**
     * *
     * Instantiate a new Global lookup. This lookup contains both the lookup of
     * the active TopComponent and a separate Lookup to which items can be added
     * and removed.
     */
    public GlobalActionContextProxy() {
        // singletonLookups = new HashMap();
        this.bulkContent = new InstanceContent();
        this.bulkLookup = new AbstractLookup(this.bulkContent);
        // Create the default GlobalContextProvider
        this.globalContextProvider = new GlobalActionContextImpl();
        this.globalContextLookup = this.globalContextProvider.createGlobalContext();

        /*this.resultCruise = this.globalContextLookup.lookupResult(ICruise.class);
         this.resultPrograms = this.globalContextLookup.lookupResult(IProgram.class);
         this.resultActor = this.globalContextLookup.lookupResult(Actor.class);

         this.resultListener = new LookupListenerImpl();

         this.resultCruise.addLookupListener(this.resultListener);
         this.resultPrograms.addLookupListener(this.resultListener);
         this.resultActor.addLookupListener(this.resultListener);*/
    }

    /**
     * *
     * Return a static reference to the Global lookup
     *
     * @return
     */
    public static GlobalActionContextProxy getInstance() {
        return CentralLookupHolder.INSTANCE;
    }

    /**
     * Returns a ProxyLookup that adds the application-wide content to the
     * original lookup returned by Utilities.actionsGlobalContext().
     *
     * @return a ProxyLookup that includes the default global context plus our
     * own content
     */
    @Override
    public Lookup createGlobalContext() {
        if (this.proxyLookup == null) {
            // Merge the two lookups that make up the proxy
            //this.bulkLookup = new AbstractLookup(this.bulkContent);
            this.proxyLookup = new ProxyLookup(this.globalContextLookup, this.bulkLookup);
        }
        return this.proxyLookup;
    }
    
    int bulkCount = 0;

    /**
     * Return an instance from the application scope global lookup of class C.
     *
     * @param <C>
     * @param cls
     * @return
     */
    public <C> C lookup(Class<C> cls) {
        return this.bulkLookup.lookup(cls);
    }

    /**
     * Return an instance from the application scope global lookup of superclass
     * C.
     *
     * @param <C>
     * @param cls
     * @return
     */
    public <C> Collection<? extends C> lookupAll(Class<C> cls) {
        this.bulkLookup.lookupAll(Object.class);
        return this.bulkLookup.lookupAll(cls);
    }

    /**
     * Adds an Object to the application scope global lookup.
     *
     * @param obj
     */
    public void add(Object obj) {
        //  if (obj instanceof CurrentSingleton) {
        //this.singletonLookups.put(obj.getClass(), Lookups.singleton(obj));
        //this.remove(this.bulkLookup.lookup(obj.getClass()));// causes the lookup.result to register an unnecessary change
        //  } else {
        if (obj != null) {
            this.bulkLookup.lookupAll(Actor.class);
            this.bulkContent.add(obj);
            this.bulkLookup.lookupAll(Actor.class);
        }
        //  }
    }

    /**
     * Adds a CurrentSingleton to the application scope global lookup.
     *
     * @param obj
     */
    public void add(CurrentSingleton obj) {
        this.bulkContent.remove(obj);
        this.bulkContent.add(obj);
       // EARSConcept concept = obj.getConcept();

        /*if (contains(concept)) { //if it's already there
         this.bulkContent.remove(concept); //remove it, to force the Lookup listeners
         } else {
         if (obj.getConcept() != null) {
         this.bulkContent.add(concept); //add it, to force the Lookup listeners}}
         }
         }*/
    }

    /**
     * *
     * Adds an Object to the application scope global lookup. If a previous
     * object of the same type is present in the lookup, it is removed first.
     *
     * @param obj
     */
    public void addEnsureOne(Object obj) {
        removeAll(obj.getClass());
        add(obj);
    }

    /**
     * Adds a provided Object to the application scope global lookup if it is
     * not yet present. Presence is tested by the provided Tester method.
     *
     * @param obj
     */
    public void addUnique(Object obj, Tester test) {
        Collection lookupAll = lookupAll(obj.getClass());
        boolean add = true;
        for (Object object : lookupAll) {
            if (test.test(object, obj)) {
                add = false;
                break;
            }
        }
        if (add) {
            add(this);
        }
    }

    /**
     * Replaces an Object with the same Object in the application scope global
     * lookup if it is not yet present.
     *
     * @param obj
     */
    public void replace(Object obj) {
        remove(obj);
        add(obj);
    }

    /**
     * *
     * Set the application scope global lookup to contain only one object, the
     * provided object.
     *
     * @param obj
     */
    public void set(Object obj) {
        this.bulkContent.set(Collections.singleton(obj), null);
    }

    /**
     * Test whether the application scope global lookup contains the provided
     * object.
     *
     * @param obj
     * @return
     */
    public boolean contains(Object obj) {
        Collection coll = null;
        try {
            coll = this.bulkLookup.lookupAll(obj.getClass());
        } catch (Exception e) {
            return false;
        }
        //if (obj != null && this.bulkLookup.lookupAll(obj.getClass()) != null) {
        if (coll != null) {
            return coll.contains(obj);
        } else {
            return false;
        }
    }

    /**
     * Adds all objects in the provided collection to the application scope
     * global lookup.
     *
     * @param coll
     */
    public void addAll(Collection coll) {
        for (Object obj : coll) {
            this.add(obj);
        }
        
    }

    /**
     * Removes an Object from the application scope global lookup.
     */
    public void remove(Object obj) {
        if (obj != null) {
            this.bulkContent.remove(obj);
        }
    }
    
    public void removeAll(List<Object> list) {
        for (Object o : list) {
            remove(o);
        }
    }

    /**
     * Removes all objects of the provided Class from the application scope
     * global lookup.
     *
     * @param cls
     */
    public void removeAll(Class cls) {
        Collection lookedUp = null;
        try {
            lookedUp = bulkLookup.lookupAll(cls);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        if (lookedUp != null) {
            if (lookedUp.size() > 0) {
                removeAll(getAllOfType(lookedUp, cls));
            }
        }
    }

    /**
     * *
     * Retrieve a List of all the elements that are present in the provided
     * Collection and are of type Class.
     *
     * @param <C>
     * @param coll
     * @param cls
     * @return
     */
    private static <C> List<C> getAllOfType(Collection coll, Class cls) {
        List<C> result = new ArrayList();
        for (Object o : coll) {
            if (o.getClass().equals(cls)) {
                C c = (C) o;
                result.add(c);
            }
        }
        return result;
    }
    
    private static class CentralLookupHolder {

        //private static final CentralLookup INSTANCE = new CentralLookup();
        private static final GlobalActionContextProxy INSTANCE = Lookup.getDefault().lookup(GlobalActionContextProxy.class);
    }
}
