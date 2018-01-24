/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

import be.naturalsciences.bmdc.ears.comparator.TermKindComparator;
import be.naturalsciences.bmdc.ears.ontology.gui.AsConceptNode;
import be.naturalsciences.bmdc.ontology.AsConceptEvent;
import be.naturalsciences.bmdc.ontology.IIndividuals;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.IEarsTerm;
import be.naturalsciences.bmdc.ontology.entities.Term;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import org.openide.nodes.Node;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

/**
 *
 * @author thomas
 */
public class Individuals implements IIndividuals {

    private final Map<Class<? extends AsConcept>, Set<AsConcept>> all;
    private final IOntologyModel model;
    // private Set<AsConcept> modifieds;
    //private Map<Class<? extends AsConcept>, Set<AsConcept>> laters;

    /* public Set<AsConcept> getModifieds() {
        return modifieds;
    }*/
    private List<NodeListener> nodeListeners = new ArrayList<NodeListener>();
    //private List<AsConceptEventListener> asConceptEventListeners = new ArrayList<AsConceptEventListener>();
    private List<PropertyChangeListener> propertyChangeListeners = new ArrayList<PropertyChangeListener>();

    public void addNodeListener(NodeListener nl) {
        this.nodeListeners.add(nl);
    }

    public void removeNodeListener(NodeListener nl) {
        this.nodeListeners.remove(nl);
    }

    /*public void addAsConceptEventListener(AsConceptEventListener nl) {
        this.asConceptEventListeners.add(nl);
    }

    public void removeAsConceptEventListener(AsConceptEventListener nl) {
        this.asConceptEventListeners.remove(nl);
    }*/
    public void removeNodeListeners() {
        this.nodeListeners.clear();
    }

    public void addPropertyChangeListener(PropertyChangeListener nl) {
        this.propertyChangeListeners.add(nl);
    }

    public void removePropertyChangeListener(PropertyChangeListener nl) {
        this.propertyChangeListeners.remove(nl);
    }

    public void removePropertyChangeListeners() {
        this.propertyChangeListeners.clear();
    }

    private void notifyListeners(Object object, String property, String oldValue, String newValue) {

    }

    public Individuals(Map<Class, Set<AsConcept>> all, IOntologyModel model) {
        this.all = new THashMap<>();
        for (Class cls : all.keySet()) {
            all.get(cls).removeAll(Collections.singleton(null));
            this.all.put(cls, all.get(cls));
        }
        this.model = model;
        // this.modifieds = new THashSet<>();
    }

    @Override
    public <C extends AsConcept> void remove(C c) {
        Class cls = c.getClass();
        Set<AsConcept> allConceptsOfClass = all.get(cls);
        if (allConceptsOfClass == null) {
            allConceptsOfClass = new THashSet<>();
        }
        allConceptsOfClass.remove(c);
        all.put(cls, allConceptsOfClass);
//        decreaseLocalhighestIdOfClass(cls);
    }

    @Override
    public void add(EventObject evt) {
        if (evt instanceof NodeMemberEvent) {
            NodeMemberEvent ev = (NodeMemberEvent) evt;

            if (ev.isAddEvent() && ev.getNode() instanceof AsConceptNode) {
                for (Node subNode : ev.getDelta()) {
                    if (subNode instanceof AsConceptNode) {
                        AsConceptNode subConceptNode = (AsConceptNode) subNode;
                        add(subConceptNode.getConcept()/*, null, null*/);
                    }
                }
                for (NodeListener nodeListeners : nodeListeners) {
                    nodeListeners.childrenAdded(ev);
                }
            }
        }
        if (evt instanceof AsConceptEvent) {
            AsConceptEvent ev = (AsConceptEvent) evt;
            if (ev.getConceptThatChanged() != null) {
                add(ev.getConceptThatChanged()/*, ev.getConceptId(), ev.getTermId()*/);
                /*  for (AsConceptEventListener asConceptEventListener : asConceptEventListeners) {
                    asConceptEventListener.nodeAdded(ev);
                }*/
            }
        }
    }

    @Override
    public void remove(EventObject evt) {
        if (evt instanceof NodeMemberEvent) {
            NodeMemberEvent ev = (NodeMemberEvent) evt;

            if (!ev.isAddEvent() && ev.getNode() instanceof AsConceptNode) {
                for (Node subNode : ev.getDelta()) {
                    if (subNode instanceof AsConceptNode) {
                        AsConceptNode subConceptNode = (AsConceptNode) subNode;
                        remove(subConceptNode.getConcept());
                    }
                }
                for (NodeListener nodeListeners : nodeListeners) {
                    nodeListeners.childrenRemoved(ev);
                }
            }
        }
        if (evt instanceof AsConceptEvent) {
            AsConceptEvent ev = (AsConceptEvent) evt;
            if (ev.getConceptThatChanged() != null) {
                remove(ev.getConceptThatChanged());
                /*  for (AsConceptEventListener asConceptEventListener : asConceptEventListeners) {
                    asConceptEventListener.nodeDestroyed(ev);
                }*/
            }
        }
    }

    @Override
    public void change(EventObject evt) {
        if (evt instanceof PropertyChangeEvent) {
            PropertyChangeEvent ev = (PropertyChangeEvent) evt;
            if ("prefLabel".equals(ev.getPropertyName())) {
                if (ev.getSource() instanceof AsConceptNode) {
                    AsConceptNode node = (AsConceptNode) ev.getSource();
                    remove(node.getConcept());
                    add(node.getConcept()/*, null, null*/);
                    for (PropertyChangeListener propertyChangeListener : propertyChangeListeners) {
                        propertyChangeListener.propertyChange(ev);
                    }
                }
            }
        }
    }

    @Override
    public <C extends AsConcept> void add(C c/*, Long conceptId, Long termId*/) {
        Class cls = c.getClass();

        Set<AsConcept> allConceptsOfClass = all.get(cls);
        if (allConceptsOfClass == null) {
            allConceptsOfClass = new THashSet<>();
        }
        boolean hasBeenAdded = allConceptsOfClass.add(c);
        if (hasBeenAdded) {
            all.put(cls, allConceptsOfClass);
            /*globalHighestId.put(cls, conceptId);
            globalHighestEarsTermId = termId;*/
        }
    }

    public Map<Class<? extends AsConcept>, Set<AsConcept>> all() {
        return all;
    }

    @Override
    public IOntologyModel getModel() {
        return model;
    }

    public String getModelFileName() {
        return model.getFile().getName();
    }

    public static Individuals getIndividualsByFile(Collection<Individuals> individualGroup, File file) {
        for (Individuals individuals : individualGroup) {
            if (individuals.getModel().getFile().equals(file)) {
                return individuals;
            }
        }
        return null;
    }

    /**
     * *
     * Verify whether this object contains the given concept.
     *
     * @param <C>
     * @param object<
     * @return
     */
    public <C extends AsConcept> boolean contains(C object) {
        Set<? extends AsConcept> results = all.get(object.getClass());
        return results.contains(object);
    }

    /**
     * *
     * Get all individuals in this object of provided Class cls, represented as
     * a Set<AsConcept>.
     *
     * @param <C>
     * @param cls
     * @return
     */
    public <C extends AsConcept> Set<C> get(Class<C> cls) {
        return (Set<C>) all.get(cls);
    }

    /**
     * *
     * Get all individuals in this object, represented as a Set<AsConcept>.
     *
     * @return
     */
    public Set<AsConcept> get() {
        Set<AsConcept> result = new THashSet<>();
        for (Map.Entry<Class<? extends AsConcept>, Set<AsConcept>> entry : all.entrySet()) {
            result.addAll(entry.getValue());
        }
        return result;
    }

    /**
     * *
     * Retrieve an individual by its prefLabel in any language.
     *
     * @param s
     * @return
     */
    @Override
    public AsConcept getByPrefLabel(String s) {
        Set<AsConcept> results = get();

        for (AsConcept result : results) {
            Term term = result.getTermRef();
            if (term != null) {
                String enPrefLabel = term.getEarsTermLabel(IEarsTerm.Language.en) != null ? term.getEarsTermLabel(IEarsTerm.Language.en).getPrefLabel() : null;
                if (s.equals(enPrefLabel)) {
                    return result;
                }
                String dePrefLabel = term.getEarsTermLabel(IEarsTerm.Language.de) != null ? term.getEarsTermLabel(IEarsTerm.Language.de).getPrefLabel() : null;
                if (s.equals(dePrefLabel)) {
                    return result;
                }
                String esPrefLabel = term.getEarsTermLabel(IEarsTerm.Language.es) != null ? term.getEarsTermLabel(IEarsTerm.Language.es).getPrefLabel() : null;
                if (s.equals(esPrefLabel)) {
                    return result;
                }
                String frPrefLabel = term.getEarsTermLabel(IEarsTerm.Language.fr) != null ? term.getEarsTermLabel(IEarsTerm.Language.fr).getPrefLabel() : null;
                if (s.equals(frPrefLabel)) {
                    return result;
                }
                String itPrefLabel = term.getEarsTermLabel(IEarsTerm.Language.it) != null ? term.getEarsTermLabel(IEarsTerm.Language.it).getPrefLabel() : null;
                if (s.equals(itPrefLabel)) {
                    return result;
                }
                String nlPrefLabel = term.getEarsTermLabel(IEarsTerm.Language.nl) != null ? term.getEarsTermLabel(IEarsTerm.Language.nl).getPrefLabel() : null;
                if (s.equals(nlPrefLabel)) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * *
     * Retrieve an individual by a partial match of its uri with the given
     * String. For instance can be used on the fragment identifier (eg.
     * #dev_700).
     *
     * @param s
     * @return
     */
    @Override
    public AsConcept getByUri(String s) {
        Set<AsConcept> results = get();

        for (AsConcept result : results) {
            if (result.getUri().toASCIIString().contains(s)) {
                return result;
            }
        }
        return null;
    }

    @Override
    public AsConcept getByUrn(String s) {
        Set<AsConcept> results = get();

        for (AsConcept result : results) {
            if (result.getUrn().equals(s)) {
                return result;
            }
        }
        return null;
    }

    /**
     * *
     * Retrieve all individuals, grouped per class in a map, from a provided
     * Collection<Individuals> superset, by specifying their class. The result
     * can be sorted.
     *
     * @param superset
     * @param cls
     * @param sorted
     * @return
     */
    public static Map<String, Set<AsConcept>> getMappedConcepts(Collection<Individuals> superset, Class cls, boolean sorted) {
        Map<String, Set<AsConcept>> result = new THashMap<>();
        Set<AsConcept> r;
        if (sorted) {
            r = new TreeSet<>(new TermKindComparator());
        } else {
            r = new THashSet<>();
        }
        for (Individuals set : superset) {
            if (set.get(cls) != null) {

                r.addAll(set.get(cls));
            }
            result.put(set.getModelFileName(), r);
        }
        return result;
    }

    /**
     * *
     * Retrieve all individuals, grouped per class in a map, from a provided
     * Collection<Individuals> superset, by specifying their prefLabel. The
     * method searches in all languages. The result can be sorted.
     *
     * @param superset
     * @param s
     * @param sorted
     * @return
     */
    public static Map<String, Set<AsConcept>> getMappedConcepts(Collection<Individuals> superset, String s, boolean sorted) {
        Map<String, Set<AsConcept>> result = new THashMap<>();
        Set<AsConcept> r;
        if (sorted) {
            r = new TreeSet<>(new TermKindComparator());
        } else {
            r = new THashSet<>();
        }
        for (IIndividuals set : superset) {
            AsConcept get = set.getByPrefLabel(s);
            if (get != null) {
                r.add(get);
            }
            if (!r.isEmpty() && set.getModel() != null && set.getModel().getFile() != null) {
                result.put(set.getModel().getFile().getName(), r);
            }
        }
        return result;
    }

    /**
     * *
     * Retrieve all individuals, grouped per class in a map, from a provided
     * Collection<Individuals> superset, by specifying their uri. The result can
     * be sorted.
     *
     * @param superset
     * @param uri
     * @param sorted
     * @return
     */
    public static Map<String, Set<AsConcept>> getMappedConcepts(Collection<Individuals> superset, URI uri, boolean sorted) {
        String uriString = uri.toASCIIString();
        Map<String, Set<AsConcept>> result = new THashMap<>();
        Set<AsConcept> r;
        if (sorted) {
            r = new TreeSet<>(new TermKindComparator());
        } else {
            r = new THashSet<>();
        }
        for (IIndividuals set : superset) {
            AsConcept get = set.getByUri(uriString);
            if (get != null) {
                r.add(get);
            }
            if (!r.isEmpty() && set.getModel() != null && set.getModel().getFile() != null) {
                result.put(set.getModel().getFile().getName(), r);
            }
        }
        return result;
    }

    /**
     * *
     * Retrieve all individuals from a provided Collection<Individuals>
     * superset, by specifying their uri. The result can be sorted.
     *
     * @param superset
     * @param uri
     * @param sorted
     * @return
     */
    public static <C extends AsConcept> Set<C> getConcepts(Collection<Individuals> superset, URI uri, boolean sorted, Class<C> cls) {
        if (uri == null) {
            throw new IllegalArgumentException("URI cannot be null");
        }
        Set<C> result = new THashSet<>();
        if (sorted) {
            result = new TreeSet<>(new TermKindComparator());
        } else {
            result = new THashSet<>();
        }
        for (IIndividuals set : superset) {
            C get = (C) set.getByUri(uri.toASCIIString());
            if (get != null) {
                result.add(get);
            }
        }
        return result;
    }

    /**
     * *
     * Retrieve all individuals from a provided Collection<Individuals>
     * superset, by specifying their uri. The result can be sorted.
     *
     * @param superset
     * @param uri
     * @param sorted
     * @return
     */
    public static Set<AsConcept> getConcepts(Collection<Individuals> superset, URI uri, boolean sorted) {
        if (uri == null) {
            throw new IllegalArgumentException("URI cannot be null");
        }
        Set<AsConcept> result = new THashSet<>();
        if (sorted) {
            result = new TreeSet<>(new TermKindComparator());
        } else {
            result = new THashSet<>();
        }
        for (IIndividuals set : superset) {
            AsConcept get = set.getByUri(uri.toASCIIString());
            if (get != null) {
                result.add(get);
            }
        }
        return result;
    }

    public static AsConcept getConcept(Collection<Individuals> allIndividuals, List<Class<? extends OntologyModel>> modelTypes, Class<? extends OntologyModel> preferredModel, URI uri) {
        allIndividuals = Individuals.getIndividualsByOntologyType(allIndividuals, modelTypes);
        Map<String, Set<AsConcept>> get = Individuals.getMappedConcepts(allIndividuals, uri, false);
        if (get.size() == 1) {
            Map.Entry<String, Set<AsConcept>> entry = get.entrySet().iterator().next();
            List<AsConcept> list = new ArrayList(entry.getValue());
            return list.get(0);
        } else {
            for (Map.Entry<String, Set<AsConcept>> entry : get.entrySet()) {
                String ontologyName = entry.getKey();
                String preferredName = null;
                try {
                    Method method = preferredModel.getMethod("getPreferredName", null);
                    preferredName = (String) method.invoke(null);
                } catch (NoSuchMethodException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (SecurityException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IllegalAccessException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (InvocationTargetException ex) {
                    Exceptions.printStackTrace(ex);
                }
                List<AsConcept> list = new ArrayList(entry.getValue());
                if (ontologyName.equals(preferredName)) {
                    return list.get(0);
                }
            }
        }
        return null;
    }

    public static Collection<Individuals> getIndividualsByOntologyType(Collection<Individuals> individuals, Collection<Class<? extends OntologyModel>> modelTypes) {
        Collection result = new THashSet<>();
        for (Individuals individual : individuals) {
            for (Class<? extends OntologyModel> modelType : modelTypes) {
                if (individual.getModel().getClass().equals(modelType)) {
                    result.add(individual);
                }
            }
        }
        return result;
    }

    /**
     * *
     * Replaces this Object in the global lookup holder.
     */
    @Override
    public void refresh() {
        //  GlobalActionContextProxy.getInstance().replace(this);
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof Individuals)) {
            return false;
        }
        Individuals other = (Individuals) object;
        return this.getModel().equals(other.getModel());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.getModel());
        return hash;
    }

    public <C extends AsConcept> long getLocalhighestIdOfClass(Class<C> cls) {

        long id = 0;
        long max = 0;
        //      if (localHighestId.get(cls) == null/* || localHighestId.get(cls) == 0*/) {

        Set<C> allConceptsOfSameClass = get(cls);

        for (C concept : allConceptsOfSameClass) {
            id = concept.getId();
            if (max < id) {
                max = id;
            }
        }
        return max;
    }

    public static Map<Class, Long> globalHighestId = new THashMap<>();
    ;
    public static Long globalHighestEarsTermId = 0L;

    public long getGlobalHighestEarsTermId() {

        if (globalHighestEarsTermId != null) {
            return globalHighestEarsTermId;
        } else {
            Collection<Individuals> allIndividuals = (Collection< Individuals>) Utilities.actionsGlobalContext().lookupAll(Individuals.class);
            long max = 0;
            long localMax = 0;
            for (Individuals individuals : allIndividuals) {
                for (Map.Entry<Class<? extends AsConcept>, Set<AsConcept>> entry : individuals.all.entrySet()) {
                    Set<AsConcept> asConceptSet = entry.getValue();
                    for (AsConcept asConcept : asConceptSet) {

                        if (asConcept != null && asConcept.getTermRef() != null && asConcept.getTermRef().getId() != null) {
                            localMax = asConcept.getTermRef().getId();
                            /*      allTermIds.add(localMax);

                            if (!asConcept.getTermRef().isBodcTerm()) {
                                int a = 5;
                            }*/
                        }

                        if (max < localMax) {
                            max = localMax;
                        }
                    }

                }

            }
            return max;
        }
    }

    @Override
    public <C extends AsConcept> long getGlobalHighestIdOfClass(Class<C> cls) {
        Long storedId = globalHighestId.get(cls);
        if (storedId != null) {
            return storedId;
        } else {
            Collection<Individuals> allIndividuals = (Collection< Individuals>) Utilities.actionsGlobalContext().lookupAll(Individuals.class);
            long max = 0;
            long localMax = 0;
            for (Individuals individuals : allIndividuals) {
                localMax = individuals.getLocalhighestIdOfClass(cls);
                if (max < localMax) {
                    max = localMax;
                }
            }
            globalHighestId.put(cls, max);
            return max;
        }

    }

    public static String nameExists(String label) {
        Collection lookupAll = Utilities.actionsGlobalContext().lookupAll(Individuals.class);
        Map<String, Set<AsConcept>> foundConcept = Individuals.getMappedConcepts(lookupAll, label, true);

        if (!foundConcept.isEmpty()) {
            StringBuilder sb = new StringBuilder("Cannot create or rename a term with new name '");
            sb.append(label);
            sb.append("'. Term(s) with this name (in en, es, de, it, fr or nl) found in: ");
            int i = 0;
            for (Map.Entry<String, Set<AsConcept>> entrySet : foundConcept.entrySet()) {

                String ontologyFile = entrySet.getKey();
                Set<AsConcept> concepts = entrySet.getValue();
                for (AsConcept concept : concepts) {
                    sb.append(ontologyFile);
                    sb.append(" as ");
                    sb.append(concept.getUri().toASCIIString());
                    if (i < foundConcept.entrySet().size() - 1) {
                        sb.append(" || ");
                    }
                    i++;
                }
            }
            sb.append(". Please copy the term over from these ontologies.");
            return sb.toString();
        } else {
            return null;
        }
    }

}
