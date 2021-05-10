package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ears.comparator.TermKindComparator;
import be.naturalsciences.bmdc.ears.comparator.TermLabelComparator;
import be.naturalsciences.bmdc.ears.ontology.AsConceptFlavor;
import be.naturalsciences.bmdc.ears.utils.Cloner;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.OntologyConstants;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.ITool;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

/**
 *
 * @author Thomas Vandenberghe
 *
 * A class representing a Tool. It corresponds to a Tool in EARS1 and in the
 * webservices. A tool in the EARS context is an actual tool, i.e. an actual
 * instance of an abstract tool, as it used on board. Ie. on board one might use
 * multiple Oceanographic Buckets of same or different sizes. Each is
 * referenceable as a different instance of a the generic "Oceanographic
 * Bucket". To achieve this, the url fragment contains both a generic part and
 * an instance part after a slash.
 */
@Namespace("http://ontologies.ef-ears.eu/ears2/1#")
@RdfType("Tool")
public class Tool implements Transferable, ITool<EarsTerm, ToolCategory, Tool, Process, SpecificEventDefinition, GenericEventDefinition>, Serializable {

    public static int lastId;

    //private static final long serialVersionUID = 1L;
    @Id
    protected URI uri;

    //protected Long id;
    /*@RdfProperty("http://ontologies.ef-ears.eu/ears2/1#isSensor")
     private boolean isSensor;

     @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#isComposite")
     private boolean isComposite;*/
    @RdfProperty(value = "http://ontologies.ef-ears.eu/ears2/1#isMemberOf")
    private Collection<ToolCategory> toolCategoryCollection;
    /**
     * The hosted tools of this tool, ie. the other tools that are subordinate
     * to it.
     */

    @RdfProperty(value = "http://ontologies.ef-ears.eu/ears2/1#canHost")
    private Collection<Tool> hostedCollection;
    /**
     * The host tools of this tool, ie. the other tools on which it is attached.
     */

    @RdfProperty(value = "http://ontologies.ef-ears.eu/ears2/1#canBeAttachedOn")
    private Collection<Tool> hostsCollection;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#asConcept")
    private EarsTerm termRef;

    @RdfProperty(value = "http://ontologies.ef-ears.eu/ears2/1#involvedInEvent", inverseOf = "http://ontologies.ef-ears.eu/ears2/1#withTool")
    private Collection<SpecificEventDefinition> specificEventDefinitionCollection;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#toolIdentifier")
    private String toolIdentifier;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#serialNumber")
    private String serialNumber;

    public Tool() {
    }

    @Override
    public void init() {
        toolCategoryCollection = new ArrayList();
        hostedCollection = new ArrayList();
        hostsCollection = new ArrayList();
        specificEventDefinitionCollection = new ArrayList();
    }

    /**
     * *
     *
     * @return
     */
    @Override
    public Long getId() {

        String[] tokens = uri.getRawFragment().split("_|\\/");
        String idCategory = tokens[0];
        String idPart = tokens[1];
        Long id = null;
        try {
            id = Long.parseLong(idPart);
        } catch (Exception e) {
            return null;
        }
        if (tokens.length > 2) {
            String instancePart = tokens[2];
        }
        return id;

    }

    @Override
    public void setId(Long id) {
        //this.id = id;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public void setUri(URI uri) {
        if (uri.getRawFragment() == null) {
            throw new IllegalArgumentException("The URI should have a fragment part (#).");
        }
        this.uri = uri;
    }

    @Override
    public String getUrn() {
        return this.getTermRef().getPublisherUrn() == null ? this.getTermRef().getOrigUrn() : this.getTermRef().getPublisherUrn();
    }

    @Override
    public String getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public void setSerialNumber(String serialNumber) {
        if ("<null value>".equals(serialNumber)) {
            serialNumber = null;
        }
        this.serialNumber = serialNumber;
    }

    @Override
    public String getToolIdentifier() {
        return this.toolIdentifier;
    }

    @Override
    public void setToolIdentifier(String toolIdentifier) {
        if ("<null value>".equals(toolIdentifier)) {
            toolIdentifier = null;
        }
        this.toolIdentifier = toolIdentifier;
    }

    @Override
    public Collection<ToolCategory> getToolCategoryCollection() {
        return toolCategoryCollection;
    }

    @Override
    public void setToolCategoryCollection(Collection<ToolCategory> toolCategoryCollection) {
        this.toolCategoryCollection = toolCategoryCollection;
    }

    @Override
    public Collection<Tool> getHostedCollection() {
        return hostedCollection;
    }

    @Override
    public void setHostedCollection(Collection<Tool> hostedCollection) {
        this.hostedCollection = hostedCollection;
    }

    @Override
    public Collection<Tool> getHostsCollection() {
        return hostsCollection;
    }

    @Override
    public void setHostsCollection(Collection<Tool> hostsCollection) {
        this.hostsCollection = hostsCollection;
    }

    @Override
    public EarsTerm getTermRef() {
        return termRef;
    }

    @Override
    public void setTermRef(EarsTerm termRef) {
        this.termRef = termRef;
    }

    @Override
    public Collection<SpecificEventDefinition> getSpecificEventDefinitionCollection() {
        return specificEventDefinitionCollection;
    }

    @Override
    public void setSpecificEventDefinitionCollection(Collection<SpecificEventDefinition> specificEventDefinitionCollection) {
        this.specificEventDefinitionCollection = specificEventDefinitionCollection;
    }

    @Override
    public int getLastId() {
        return ++lastId;
    }

    public static void setLastId(int lastId) {
        Tool.lastId = lastId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.uri);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tool other = (Tool) obj;
        if (!Objects.equals(this.uri, other.uri)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id=" + getId() + ";hash=" + System.identityHashCode(this) + ";name=" + ((this.getTermRef() != null) ? this.getTermRef().getName() : "no name");
    }

    public String print() {

        StringBuilder sb = new StringBuilder();
        sb.append("tool: ");
        sb.append(this.getTermRef().getEarsTermLabel().getPrefLabel());
        sb.append(" (");
        sb.append(this.getTermRef().getUri());
        sb.append(")");
        sb.append(System.getProperty("line.separator"));
        sb.append("Specific EventDefinitions");
        sb.append(System.getProperty("line.separator"));
        for (SpecificEventDefinition sev : getSpecificEventDefinitionCollection()) {
            sb.append("\t");
            sb.append(sev.print());
            sb.append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }

    public Tool clone(IdentityHashMap<Object, Object> clonedObjects) throws CloneNotSupportedException {
        Cloner<Tool> cc = new Cloner(this, clonedObjects);
        Tool shallowClone = cc.cloneOriginal();

        Map<Collection, Collection> collectionIdentityHashMap = new THashMap<>();
        collectionIdentityHashMap.put(this.toolCategoryCollection, shallowClone.toolCategoryCollection);
        collectionIdentityHashMap.put(this.hostedCollection, shallowClone.hostedCollection);
        collectionIdentityHashMap.put(this.hostsCollection, shallowClone.hostsCollection);
        collectionIdentityHashMap.put(this.specificEventDefinitionCollection, shallowClone.specificEventDefinitionCollection);
        cc.cloneCollection(collectionIdentityHashMap);
        return shallowClone;
    }

    /**
     * Adds the given category to the tool, and bidirectionally adds the tool to
     * the category.
     *
     */
    @Override
    public void addToCategory(ToolCategory tc) {
        if (this.toolCategoryCollection == null) {
            this.setToolCategoryCollection(new ArrayList());
        }
        if (!this.getToolCategoryCollection().contains(tc)) {
            this.getToolCategoryCollection().add(tc);
        }
        tc.addTool(this);
    }

    public Collection<EventDefinition> getEventDefinitionCollection() {
        Set<EventDefinition> result = new THashSet();
        result.addAll(getGenericEventDefinitionCollection());
        result.addAll(getSpecificEventDefinitionCollection());
        return result;
    }

    public Collection<GenericEventDefinition> getGenericEventDefinitionCollection() {
        Collection<GenericEventDefinition> generics = new ArrayList();
        if (this.getToolCategoryCollection() != null && this.getToolCategoryCollection().size() > 0) {
            for (ToolCategory tc : this.getToolCategoryCollection()) {
                if (tc.getGenericEventDefinitionCollection() != null && tc.getGenericEventDefinitionCollection().size() > 0) {
                    generics.addAll(tc.getGenericEventDefinitionCollection());
                }
            }
        }
        return generics;
    }

    /**
     * *
     * Get all the Events of this Tool. Returns a list without elements when
     * there are no events.
     *
     * @return
     */
    @Override
    public Collection<EventDefinition> getEvents() {
        Collection<EventDefinition> join = new ArrayList();
        if (this.getSpecificEventDefinitionCollection() != null && this.getSpecificEventDefinitionCollection().size() > 0) {
            join.addAll(this.getSpecificEventDefinitionCollection());
        }
        join.addAll(this.getGenericEventDefinitionCollection());
        return join;
    }

    /**
     * *
     * Get all the Events of this Tool that have the provided Action and Event.
     * Returns a list without elements when there are no events.
     *
     * @return
     */
    public Set<EventDefinition> getEventDefinitionCollection(Process p, Action a) {
        Set<EventDefinition> join = new TreeSet(new TermLabelComparator());
        for (EventDefinition e : this.getEvents()) {
            if (e instanceof GenericEventDefinition) {
                GenericEventDefinition ge = (GenericEventDefinition) e;
                if (ge.getProcess().equals(p) && ge.getAction().equals(a)) {
                    join.add(ge);
                }

            }
            if (e instanceof SpecificEventDefinition) {
                SpecificEventDefinition se = (SpecificEventDefinition) e;
                if (se.getProcess().equals(p) && se.getAction().equals(a)) {
                    join.add(se);
                }
            }
        }
        return join;
    }

    /**
     * *
     * Get all the Specific Events of this Tool that have the provided Action
     * and Event. Returns a list without elements when there are no events.
     *
     * @return
     */
    public Set<SpecificEventDefinition> getSpecificEventDefinitionCollection(Process p, Action a) {
        Set<SpecificEventDefinition> join = new TreeSet(new TermLabelComparator());
        for (SpecificEventDefinition se : this.getSpecificEventDefinitionCollection()) {
            if (se != null) {
                if (se.getProcess().equals(p) && se.getAction().equals(a)) {
                    join.add(se);
                }
            }
        }
        return join;
    }

    /**
     * *
     * Get all the Specific Events of this Tool that have the provided Action
     * and Event. Returns a list without elements when there are no events.
     *
     * @return
     */
    public Set<GenericEventDefinition> getGenericEventDefinitionCollection(Process p, Action a) {
        Set<GenericEventDefinition> join = new TreeSet(new TermLabelComparator());
        for (GenericEventDefinition ge : this.getGenericEventDefinitionCollection()) {
            if (ge.getProcess().equals(p) && ge.getAction().equals(a)) {
                join.add(ge);
            }
        }
        return join;
    }

    /**
     * Get all the Processes of a tool.
     *
     * @return
     */
    @Override
    public Set<Process> getProcessCollection() {
        Set<Process> join = new TreeSet(new TermLabelComparator());
        for (EventDefinition e : this.getEvents()) {
            if (e instanceof GenericEventDefinition) {
                GenericEventDefinition ge = (GenericEventDefinition) e;
                Process p = ge.getProcess();
                if (p != null && !join.contains(p)) {
                    join.add(p);
                }
            }
            if (e instanceof SpecificEventDefinition) {
                SpecificEventDefinition se = (SpecificEventDefinition) e;
                Process p = se.getProcess();
                if (p != null && !join.contains(p)) {
                    join.add(p);
                }
            }
        }
        // Collections.sort(join, new TermUriComparator());
        return join;
    }

    public Set<Process> getProcessCollection(ToolCategory tc) {
        Set<Process> join = new TreeSet(new TermLabelComparator());
        for (EventDefinition e : this.getEvents()) {
            if (e instanceof GenericEventDefinition) {
                GenericEventDefinition ge = (GenericEventDefinition) e;
                if (ge.getToolCategoryRef().equals(tc)) {
                    Process p = ge.getProcess();

                    if (p != null && !join.contains(p)) {
                        join.add(p);
                    }
                }

            }
            if (e instanceof SpecificEventDefinition) {
                SpecificEventDefinition se = (SpecificEventDefinition) e;
                Process p = se.getProcess();
                if (p != null && !join.contains(p)) {
                    join.add(p);
                }
            }
        }
        // Collections.sort(join, new TermUriComparator());
        return join;
    }

    /**
     * Get all the Actions of a tool.
     *
     * @return
     */
    @Override
    public Set<Action> getActionCollection() {
        Set<Action> join = new TreeSet(new TermLabelComparator());

        for (EventDefinition e : this.getEvents()) {
            if (e instanceof GenericEventDefinition) {
                GenericEventDefinition ge = (GenericEventDefinition) e;
                Action a = ge.getAction();
                if (a != null && !join.contains(a)) {
                    join.add(a);
                }
            }
            if (e instanceof SpecificEventDefinition) {
                SpecificEventDefinition se = (SpecificEventDefinition) e;
                Action a = se.getAction();
                if (a != null && !join.contains(a)) {
                    join.add(a);
                }
            }
        }
        //Collections.sort(join, new TermUriComparator());
        return join;
    }

    /**
     * Get all the Actions of a tool that have the given Process
     *
     * @param p
     * @return
     */
    @Override
    public Set<Action> getActionCollection(Process p) {
        Set<Action> join = new TreeSet(new TermLabelComparator());

        for (EventDefinition e : this.getEvents()) {
            if (e instanceof GenericEventDefinition) {
                GenericEventDefinition ge = (GenericEventDefinition) e;
                if (ge.getAction() != null && ge.getProcess().equals(p)) {
                    join.add(ge.getAction());
                }
            }
            if (e instanceof SpecificEventDefinition) {
                SpecificEventDefinition se = (SpecificEventDefinition) e;
                if (se.getAction() != null && se.getProcess().equals(p)) {
                    join.add(se.getAction());
                }
            }
        }
        //Collections.sort(join, new TermUriComparator());
        return join;
    }

    public boolean hasOneOf(ToolCategory tcCollection) {
        for (ToolCategory tc : toolCategoryCollection) {
            if (tc.equals(tcCollection)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<AsConcept> getChildren(ConceptHierarchy parents) {
        Set<AsConcept> result = new TreeSet(new TermKindComparator());
        result.addAll(getProcessCollection((ToolCategory) parents.getToolCategory()));
        result.addAll(this.hostedCollection);
        return result;
    }

    @Override
    public List<AsConcept> getParents() {
        return new ArrayList(this.toolCategoryCollection);
    }

    /**
     * *
     * Adds a AsConcept childConcept to the children of this, if childConcept is
     * a Process.If childConcept has no Action associated to it via an
     * EventDefinition, a new one is created.
     *
     * @param targetParents The ConceptHierarchy parents of this (so not the
     * original parents of the child).
     * @param childConcept The AsConcept childConcept that needs to be added.
     * @param removePreviousBottomUpAssociations Whether to remove existing
     * relations the AsConcept childConcept has.
     * @param newChildParents The existing parents of the newly added
     * ChildConcept
     */
    @Override
    public void addToChildren(ConceptHierarchy targetParents, AsConcept childConcept, boolean removePreviousBottomUpAssociations, ConceptHierarchy newChildParents, IAsConceptFactory factory) {
        /* if (model == null) {
         throw new IllegalArgumentException("No model given.");
         }*/
        // Set<Process> children = this.getChildren(targetParents);
        //int before = this.getSpecificEventDefinitionCollection().size();
        if (childConcept != null && childConcept instanceof Tool) { //i.e. a tool will be added to myself, I become am a hosting tool.
            Tool childTool = (Tool) childConcept;
            // childTool.setToolCategoryCollection(new ArrayList()); //keep the original categories I was part of
            this.hostedCollection.add(childTool);
            childTool.hostsCollection.add(this);
        }
        if (childConcept != null && childConcept instanceof Process) { //TODO check if not has as child 
            // AsConceptFactory f = new AsConceptFactory();
            Process childProcess = (Process) childConcept;

            Tool originalTool = null;
            if (newChildParents != null) {
                originalTool = (Tool) newChildParents.getTool();
            }
            Collection<Action> childActions = childProcess.getActionCollectionFromSpecificEvents(originalTool);
            if (childActions.isEmpty()) {
                try {
                    //force add process by creating dummy dummyAction
                    AsConcept dummyAction = factory.buildChild(childProcess);
                    targetParents.add(this);
                    childProcess.addToChildren(targetParents, dummyAction, false, newChildParents, factory);
                    dummyAction.getTermRef().setStatusName(OntologyConstants.DEPRECATED); //this ensures this dummy action is never displayed!
                } catch (Exception ex) {
                    throw new RuntimeException("There was a problem with creating an Action for this Tool.", ex);
                }
            } else {
                for (Action childAction : childActions) {
                    if (!childAction.getTermRef().isDeprecated()) { //don't perpetuate deprecated actions
                        Collection<Property> propertyCollection = new THashSet<>();
                        if (originalTool != null) {
                            List<EventDefinition> eventDefinitionCollection = childProcess.getEventDefinitionCollection(originalTool, childAction);
                            if (eventDefinitionCollection.size() > 0) {
                                EventDefinition originalEv = eventDefinitionCollection.get(0);
                                propertyCollection.addAll(originalEv.getPropertyCollection());
                            }
                        }

                        SpecificEventDefinition sev = null;
                        try {
                            sev = factory.build(SpecificEventDefinition.class);
                        } catch (Exception ex) {
                            throw new RuntimeException("There was a problem with creating a Specific Event definition for this Tool.", ex);
                        }
                        sev.setToolRef(this);

                        this.getSpecificEventDefinitionCollection().add(sev);
                        sev.setProcess(childProcess);
                        sev.setAction(childAction);

                        sev.setPropertyCollection(propertyCollection);
                    /*    if (removePreviousBottomUpAssociations) { //erase all previous bottom-up associations of the future child
                            childAction.setEventDefinition(new ArrayList());
                            childProcess.setEventDefinition(new ArrayList());
                            childProcess.setActionCollection(new ArrayList());
                        }*/
                        childAction.getEventDefinition().add(sev);
                        childProcess.getEventDefinition().add(sev);
                        childProcess.getActionCollection().add(childAction);

                    }
                }
            }
        }
    }

    /**
     * *
     * Remove this from all its toolcategories and remove all these
     * toolcategories
     */
    public void removeBottomUpAssociations() {
        /*for (Iterator<ToolCategory> iterator = this.getToolCategoryCollection().iterator(); iterator.hasNext();) {
            ToolCategory originalToolCategory = iterator.next();
            originalToolCategory.getToolCollection().remove(this);

        }*/
        for (ToolCategory originalToolCategory : this.getToolCategoryCollection()) {
            originalToolCategory.getToolCollection().remove(this);
        }
        this.setToolCategoryCollection(new ArrayList());
    }

    public void removeFromHostsCollection(ITool parentTool) {
        parentTool.getHostedCollection().remove(this);
        this.hostsCollection.remove(parentTool);
    }

    /**
     * *
     * Delete this tool from its toolcategorycollection, given its parents.
     *
     * @param parents
     */
    @Override

    public void delete(ConceptHierarchy parents) {

        if (specificEventDefinitionCollection != null && specificEventDefinitionCollection.size() > 0) {
            Iterator<SpecificEventDefinition> iter = this.getSpecificEventDefinitionCollection().iterator();
            while (iter.hasNext()) {
                SpecificEventDefinition sev = iter.next();
                // for (SpecificEventDefinition sev : specificEventDefinitionCollection) {
                sev.setToolRef(null);
                sev.safeDelete(null, this, iter);
            }
            specificEventDefinitionCollection = null;
        }

        ITool parentTool = parents.getTool();
        if (parentTool != null && this.hostsCollection.contains(parentTool)) { //I am a hosted tool because I appear in the hosted tools of my parents
            this.removeFromHostsCollection(parentTool);
        }

        if (toolCategoryCollection != null && toolCategoryCollection.size() > 0) {
            for (ToolCategory tc : toolCategoryCollection) {
                tc.removeTool(this);
            }
            toolCategoryCollection = null;
        }

        /*uri = null;
         hostedCollection = null; //TODO go over all hosteds and see if this is in hosts
         hostsCollection = null;
         termRef = null;*/
    }

    public void safeDelete(ConceptHierarchy parents, AsConcept caller, Iterator iter) {
        if (iter != null) {
            if (!caller.getClass().equals(ToolCategory.class)) {
                if (toolCategoryCollection != null && toolCategoryCollection.size() > 0) {
                    for (ToolCategory tc : toolCategoryCollection) {
                        tc.removeTool(this);
                    }
                    toolCategoryCollection = null;
                }
            }
            if (specificEventDefinitionCollection != null && specificEventDefinitionCollection.size() > 0) {
                Iterator<SpecificEventDefinition> iter2 = this.getSpecificEventDefinitionCollection().iterator();
                while (iter2.hasNext()) {
                    SpecificEventDefinition sev = iter2.next();
                    // for (SpecificEventDefinition sev : specificEventDefinitionCollection) {
                    sev.setToolRef(null);
                    sev.safeDelete(null, this, iter2);
                }
                specificEventDefinitionCollection = null;
            }
        } else {
            throw new IllegalArgumentException("Null iterator provided!");
        }
    }

    /**
     * **
     * For the provided process and/or dummyAction, delete the
     * SpecificEventDefinitions of this.
     *
     * @param process
     * @param action
     */
    void deleteSpecificEventDefinitions(Process process, Action action) {
        if (process == null) {
            throw new IllegalArgumentException("Process must be provided.");
        }
        Iterator<SpecificEventDefinition> iter = this.getSpecificEventDefinitionCollection().iterator();
        while (iter.hasNext()) {
            SpecificEventDefinition sev = iter.next();
            if (sev.getProcess().equals(process) && (action != null ? sev.getAction().equals(action) : true)) {
                iter.remove();
                sev.safeDelete(null, this, iter);
            }
        }
        /*for (int j = 0; j < this.getSpecificEventDefinitionCollection().size(); j++) {
         SpecificEventDefinition sev = (SpecificEventDefinition) new ArrayList(this.getSpecificEventDefinitionCollection()).get(j);
         if (sev.getProcess().equals(process) && (dummyAction != null ? sev.getAction().equals(dummyAction) : true)) {
         sev.delete(null);
         this.specificEventDefinitionCollection.remove(sev);
         j = (j - 1 < 0 ? 0 : j - 1);
         }
         }*/
    }

    public void addToSpecificEventDefinitions(SpecificEventDefinition sev) {
        this.specificEventDefinitionCollection.add(sev);
    }

    public transient static final AsConceptFlavor OWN_DATA_FLAVOR = new AsConceptFlavor(Tool.class, "tool");

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{OWN_DATA_FLAVOR};
        //return null;//new DataFlavor[]{OWN_DATA_FLAVOR};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return false;//flavor == OWN_DATA_FLAVOR;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor == null) {//OWN_DATA_FLAVOR) {
            return this;
        } else if (flavor instanceof AsConceptFlavor) { //a fancy way to get the type of this
            return null;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    @Override
    public Class getParentType() {
        return ToolCategory.class;
    }

    @Override
    public Class getChildType() {
        return Process.class;
    }

    @Override
    public String getKind() {
        return this.getTermRef().getKind();
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public void isolate() {
        if (toolCategoryCollection != null) {
            toolCategoryCollection.clear();
        }
        if (hostedCollection != null) {
            hostedCollection.clear();
        }
        if (hostsCollection != null) {
            hostsCollection.clear();
        }
        if (specificEventDefinitionCollection != null) {
            specificEventDefinitionCollection.clear();
        }
    }

    public boolean isHostingTool() {
        if (this.getHostedCollection().size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHostedTool() {
        if (this.getHostsCollection().size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getDefinitionEn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAltLabelEn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPrefLabelEn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getIdentifierString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getVersionString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date getModifiedDate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date getCreationDate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean isIsDeprecated() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> broadMatch() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
