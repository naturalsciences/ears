/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.utils;

import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author thomas
 */
public class Cloner<T extends AsConcept> {

    public T original;
    public T copy;

    public IdentityHashMap<Object, Object> clonedObjects;

    /**
     * *
     * Create a new Cloner object capable of both shallow and deep cloning.
     *
     * @param original
     * @param clonedObjects
     */
    public Cloner(T original, IdentityHashMap<Object, Object> clonedObjects) {
        if (clonedObjects == null) {
            throw new IllegalArgumentException("clonedObjects cannot be null.");
        }
        this.original = original;
        this.clonedObjects = clonedObjects;
    }

    /**
     * *
     * Make a shallow clone of the provided AsConcept, init its collection
     * members with empty new collections, and return it.
     *
     * @param original
     * @return
     */
    public static AsConcept clone(AsConcept original) {
        com.rits.cloning.Cloner cloner = new com.rits.cloning.Cloner();
        AsConcept copy = cloner.shallowClone(original);
        copy.init();
        return copy;
    }

    /**
     * *
     * Make a shallow clone of the provided AsConcept, init its collection
     * members with empty new collections, and return it. Put it in a collection
     * so that subsequent calls of this method do not reclone the object. This
     * has 2 purposes: cloning happens faster and there are no endless cloning
     * loops between parents trying to clone all their children and children
     * trying to clone all their parents.
     *
     * @return
     * @throws CloneNotSupportedException
     */
    public T cloneOriginal() throws CloneNotSupportedException {
        com.rits.cloning.Cloner cloner = new com.rits.cloning.Cloner();
        this.copy = cloner.shallowClone(this.original);
        this.copy.init();
        if (this.clonedObjects.get(this.original) == null) {
            this.clonedObjects.put(this.original, this.copy);
        }

        return this.copy;
    }

    static int i = 0;

    /**
     * *
     * Clone the collections provided in the Map. The Map
     * collectionIdentityHashMap has as key the original collection and as value
     * the newly cloned collection.
     *
     * @param collectionIdentityHashMap
     * @throws CloneNotSupportedException when there has not yet been a copy
     * made. First call this.cloneOriginal()!
     */
    public void cloneCollection(Map<Collection, Collection> collectionIdentityHashMap) throws CloneNotSupportedException {
        if (this.copy == null) {
            throw new IllegalStateException("There is no copy yet made.");
        }
        for (Map.Entry<Collection, Collection> entry : collectionIdentityHashMap.entrySet()) {
            Collection<AsConcept> oldCollection = entry.getKey();
            Collection<AsConcept> newCollection = entry.getValue();
            if (oldCollection != null) {

                Iterator<AsConcept> iter = oldCollection.iterator();

                while (iter.hasNext() && newCollection.size() < oldCollection.size()) {
                    AsConcept eChild = null;

                    eChild = iter.next();

                    //  for (Object child : oldCollection) {
                    //      if (child instanceof AsConcept) {
                    //  AsConcept eChild = (AsConcept) child;
                    AsConcept clonedChildOfE = null;
                    if (this.clonedObjects.get(eChild) != null) {
                        clonedChildOfE = (AsConcept) clonedObjects.get(eChild);
                    } else {
                        clonedChildOfE = eChild.clone(clonedObjects);
                        this.clonedObjects.put(eChild, clonedChildOfE);
                    }
                    newCollection.add(clonedChildOfE);
                }
                // }
            }
        }
    }
}
