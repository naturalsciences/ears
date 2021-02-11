package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ears.comparator.TermLabelComparator;
import be.naturalsciences.bmdc.ears.ontology.AsConceptFlavor;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.IEventDefinition;
import be.naturalsciences.bmdc.ontology.entities.IProcess;
import be.naturalsciences.bmdc.ontology.entities.ITool;
import gnu.trove.map.hash.THashMap;
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
 * A class representing a Process. It corresponds to a Category in EARS1 and in
 * the webservices.
 */
@Namespace("http://ontologies.ef-ears.eu/ears2/1#")
@RdfType("Process")
public class Process implements IProcess<EarsTerm, Tool, Process, Action, ProcessAction, EventDefinition, Subject>, Transferable, Serializable {

    public static int lastId;

    //private static final long serialVersionUID = 1L;
    @Id
    protected URI uri;
    //protected Long id;

    private Collection<Subject> subjectCollection;

    private transient Collection<ProcessAction> processActionCollection;
    private Collection<Process> processCollection;

    @RdfProperty(inverseOf = "http://ontologies.ef-ears.eu/ears2/1#hasProcess")
    private Collection<IEventDefinition> eventDefinition;

    /*@RdfProperty(value="http://ontologies.ef-ears.eu/ears2/1#processPartOfEvent")
     private Collection<SpecificEventDefinition> eventDefinition1;
    
     @RdfProperty(value="http://ontologies.ef-ears.eu/ears2/1#processPartOfEvent")
     private Collection<GenericEventDefinition> eventDefinition2;*/
    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#involvesStep")
    private Collection<Action> actionCollection;

    private Process nextProcessRef;

    private Process process;

    private Process parentRef;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#asConcept")
    private EarsTerm termRef;

    public Process() {

    }

    @Override
    public void init() {
        eventDefinition = new ArrayList();
        actionCollection = new ArrayList();
        processCollection = new ArrayList();
        subjectCollection = new ArrayList();
    }

    /*public Process(Long id) {
     this.id = id;
     }*/
    @Override
    public Long getId() {
        try {
            return Long.parseLong(uri.getRawFragment().split("_")[1]);
        } catch (Exception e) {
            return null;
        }
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
        return this.getTermRef().getPublisherUrn();
    }

    @Override
    public Collection<Subject> getSubjectCollection() {
        return subjectCollection;
    }

    @Override
    public void setSubjectCollection(Collection<Subject> subjectCollection) {
        this.subjectCollection = subjectCollection;
    }

    /**
     * *
     * Returns the actions this process is involved in via its events.
     *
     * @return
     */
    @Override
    public Collection<Action> getActionCollectionFromEvent() {
        Set<Action> join = new TreeSet(new TermLabelComparator());
        /*Collection<EventDefinition> events = new ArrayList();
         while (true) {
         try {
         for (EventDefinition e : this.getEventDefinition()) {
         if (e != null) {
         events.add(e);
         }
         }
         break;
         } catch (Exception e) {
         continue;
         }
         }*/

        for (IEventDefinition e : this.getEventDefinition()) {
            if (e != null) {
                if (e instanceof GenericEventDefinition) {
                    GenericEventDefinition ge = (GenericEventDefinition) e;
                    if (ge.getAction() != null && ge.getProcess().equals(this)) {
                        join.add(ge.getAction());
                    }
                }
                if (e instanceof SpecificEventDefinition) {
                    SpecificEventDefinition se = (SpecificEventDefinition) e;
                    if (se.getAction() != null && se.getProcess().equals(this)) {
                        join.add(se.getAction());
                    }
                }
            }
        }
        return join;
        /*if (processActionCollection == null) {
         processActionCollection = new HashSet();
         }
         for (Action a : actionCollection) {
         ProcessAction pa = new ProcessAction(this, a);
         if (!processActionCollection.contains(pa)) {
         processActionCollection.add(pa);
         }
         }
         return processActionCollection;*/
    }

    @Override
    public void setActionCollection(Collection<Action> actionCollection) {
        this.actionCollection = actionCollection;
    }

    /**
     * *
     * Get all the Actions of this Process
     *
     * @return
     */
    @Override
    public Collection<Action> getActionCollection() {
        return actionCollection;
    }

    @Override
    public void setProcessActionCollection(Collection<ProcessAction> processActionCollection) {
        this.processActionCollection = processActionCollection;
    }

    @Override
    public Collection<Process> getProcessCollection() {
        return processCollection;
    }

    @Override
    public void setProcessCollection(Collection<Process> processCollection) {
        this.processCollection = processCollection;
    }

    @Override
    public Process getNextProcessRef() {
        return nextProcessRef;
    }

    @Override
    public void setNextProcessRef(Process nextProcessRef) {
        this.nextProcessRef = nextProcessRef;
    }

    @Override
    public Process getProcess() {
        return process;
    }

    @Override
    public void setProcess(Process process) {
        this.process = process;
    }

    @Override
    public Process getParentRef() {
        return parentRef;
    }

    @Override
    public void setParentRef(Process parentRef) {
        this.parentRef = parentRef;
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
    public Collection<IEventDefinition> getEventDefinition() {
        return eventDefinition;
    }

    @Override
    public void setEventDefinition(Collection<IEventDefinition> eventDefinition) {
        this.eventDefinition = eventDefinition;
    }

    @Override
    public int getLastId() {
        return ++lastId;
    }

    public static void setLastId(int lastId) {
        Process.lastId = lastId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash ^= (getId() != null ? getId().hashCode() : 0);
        hash ^= (getUri() != null ? getUri().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof Process)) {
            return false;
        }
        Process other = (Process) object;
        if (!(this.getId() == null && other.getId() == null) && ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId())))) {
            return false;
        }
        if ((this.uri == null && other.uri != null) || (this.uri != null && !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    /*boolean equalsNbChildren(Process otherProcess, ConceptHierarchy thisParents, ConceptHierarchy otherParents) {
        thisParents.add(this);
        otherParents.add(otherProcess);
        if (!this.equals(otherProcess)) {
            return false;
        }
        boolean allLevelsTrue = true;
        if (this.getChildren(thisParents).size() != otherProcess.getChildren(otherParents).size()) {
            return false;
        }
        if (this.getChildren(thisParents).size() > 0) {
            for (Action action : this.getChildren(thisParents)) {
                for (Action otherAction : otherProcess.getChildren(otherParents)) {
                    if (action.equals(otherAction)) {
                        return action.equalsNbChildren(otherAction, thisParents, otherParents);
                    }
                }

            }
        }
        return true;
    }*/
    @Override
    public String toString() {
        return "id=" + getId() + "; hash=" + System.identityHashCode(this) + "; name=" + ((this.getTermRef() != null) ? this.getTermRef().getName() : "no name");
    }

    @Override
    public Process clone(IdentityHashMap<Object, Object> clonedObjects) throws CloneNotSupportedException {
        be.naturalsciences.bmdc.ears.utils.Cloner<Process> cc = new be.naturalsciences.bmdc.ears.utils.Cloner(this, clonedObjects);
        Process shallowClone = cc.cloneOriginal();

        Map<Collection, Collection> collectionIdentityHashMap = new THashMap<>();
        collectionIdentityHashMap.put(this.eventDefinition, shallowClone.eventDefinition);
        collectionIdentityHashMap.put(this.actionCollection, shallowClone.actionCollection);
        collectionIdentityHashMap.put(this.processCollection, shallowClone.processCollection);
        collectionIdentityHashMap.put(this.subjectCollection, shallowClone.subjectCollection);
        cc.cloneCollection(collectionIdentityHashMap);

        return shallowClone;
    }

    @Override
    public Set<Action> getChildren(ConceptHierarchy parents) {//AsConcept concept
        ITool concept = null;
        if (parents != null) {
            concept = parents.getLowestToolInHierarchy();
        }
        if (concept != null && concept instanceof Tool) {
            Tool tool = (Tool) concept;
            return getActionCollection(tool);
        } else {
            return getActionCollection(null);
        }
    }

    /**
     * *
     * Get all the actions of this Process and limit them by tool. If tool is
     * null, all actions are returned.
     *
     * @param tool
     * @return
     */
    @Override
    public Set<Action> getActionCollection(Tool tool) {
        Set<Action> result = new TreeSet(new TermLabelComparator());
        for (IEventDefinition e : eventDefinition) {
            if (e instanceof SpecificEventDefinition) {
                SpecificEventDefinition se = (SpecificEventDefinition) e;
                if (tool != null && tool.equals(se.getToolRef())) {
                    result.add(se.getAction());
                } else if (tool == null) {
                    result.add(se.getAction());
                }
            } else if (e instanceof GenericEventDefinition) {
                GenericEventDefinition ge = (GenericEventDefinition) e;
                if (tool != null && tool.hasOneOf(ge.getToolCategoryRef())) {
                    result.add(ge.getAction());
                } else if (tool == null) {
                    result.add(ge.getAction());
                }
            }
        }

        return result;
    }

    /**
     * *
     * Get all the EventDefinition of this Process and limit them by tool and
     * action
     *
     * @param tool
     * @param action
     * @return
     */
    @Override
    public List<EventDefinition> getEventDefinitionCollection(Tool tool, Action action) {
        Collection<IEventDefinition> eventList = this.getEventDefinition();
        //List<EventDefinition> eventList = new ArrayList(tool.getEvents());
        List<EventDefinition> eventListResult = new ArrayList();
        for (IEventDefinition e : eventList) {
            if (e instanceof SpecificEventDefinition) {
                SpecificEventDefinition se = (SpecificEventDefinition) e;
                try {
                    if (se.getToolRef().equals(tool) && se.getAction().equals(action) && se.getProcess().equals(this)) {
                        eventListResult.add(se);
                    }
                } catch (java.lang.NullPointerException ex) {
                    int a = 5;
                }
            } else if (e instanceof GenericEventDefinition) {
                GenericEventDefinition ge = (GenericEventDefinition) e;
                if (tool.getToolCategoryCollection().contains(ge.getToolCategoryRef()) && ge.getAction().equals(action) && ge.getProcess().equals(this)) {
                    eventListResult.add(ge);
                }
            }
        }
        return eventListResult;
    }

    public List<EventDefinition> getEventDefinitionCollection(Action action) {
        Collection<IEventDefinition> eventList = this.getEventDefinition();
        //List<EventDefinition> eventList = new ArrayList(tool.getEvents());
        List<EventDefinition> eventListResult = new ArrayList();
        for (IEventDefinition e : eventList) {
            if (e instanceof SpecificEventDefinition) {
                SpecificEventDefinition se = (SpecificEventDefinition) e;
                if (se.getAction().equals(action) && se.getProcess().equals(this)) {
                    eventListResult.add(se);
                }
            } else if (e instanceof GenericEventDefinition) {
                GenericEventDefinition ge = (GenericEventDefinition) e;
                if (ge.getAction().equals(action) && ge.getProcess().equals(this)) {
                    eventListResult.add(ge);
                }
            }
        }
        return eventListResult;
    }

    @Override
    public List<AsConcept> getParents() {
        Collection<IEventDefinition> eventList = getEventDefinition();
        List<AsConcept> lp = new ArrayList();

        for (IEventDefinition e : eventList) {
            if (e instanceof SpecificEventDefinition) {
                SpecificEventDefinition se = (SpecificEventDefinition) e;

                lp.add(se.getToolRef());

            } else if (e instanceof GenericEventDefinition) {
                GenericEventDefinition ge = (GenericEventDefinition) e;
                lp.add(ge.getToolCategoryRef());
            }
        }
        return lp;
    }

    @Override
    public void addToChildren(ConceptHierarchy targetParents, AsConcept childConcept, boolean removePreviousBottomUpAssociations, ConceptHierarchy newChildParents, IAsConceptFactory factory) {
        ITool itool = targetParents.getLowestToolInHierarchy();
        Tool tool = (Tool) itool;
        if (tool != null && childConcept != null && childConcept instanceof Action) {
            Action action = (Action) childConcept;

            SpecificEventDefinition sev = null;
            try {
                sev = factory.build(SpecificEventDefinition.class);
            } catch (Exception ex) {
                throw new RuntimeException("There was a problem with creating a new Specific Event definition.", ex);
            }

            sev.setToolRef(tool);
            tool.getSpecificEventDefinitionCollection().add(sev);

            sev.setProcess(this);
            sev.setAction(action);

            if (removePreviousBottomUpAssociations) {//erase all previous bottom-up associations of the future child
                action.setEventDefinition(new ArrayList());
                //this.setEventDefinition(new ArrayList());
                //this.setActionCollection(new ArrayList());
            }
            action.getEventDefinition().add(sev);
            this.getEventDefinition().add(sev);
            this.getActionCollection().add(action);
        }
    }

    @Override
    public String getKind() {
        return this.getTermRef().getKind();
    }

    @Override
    public Class getParentType() {
        return Tool.class;
    }

    @Override
    public Class getChildType() {
        return Action.class;
    }

    public transient static final AsConceptFlavor OWN_DATA_FLAVOR = new AsConceptFlavor(Process.class, "process");

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{OWN_DATA_FLAVOR};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor == OWN_DATA_FLAVOR;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor == OWN_DATA_FLAVOR) {
            return this;
        } else if (flavor instanceof AsConceptFlavor) { //a fancy way to get the type of this
            return null;
        } else if (flavor == null) {
            return this;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    @Override
    public void delete(ConceptHierarchy parents) {
        Tool tool = (Tool) parents.getLowestToolInHierarchy();
        ToolCategory toolCat = (ToolCategory) parents.getToolCategory();

        //toolCat.reduceGevsToSevs(this, null);
        tool.deleteSpecificEventDefinitions(this, null);
        //this.reduceGevsToSevs(toolCat, tool, null);

        Iterator<Action> iter = this.getActionCollectionFromEvent().iterator();
        while (iter.hasNext()) {
            Action e = iter.next();
            this.deleteSpecificEventDefinitions(tool, e);
            e.delete(parents.add(this));
        }
        if (parentRef != null && parentRef.process != null) {
            parentRef.process = null;
        }
    }

    void deleteSpecificEventDefinitions(Tool tool, Action action) {
        if (tool == null) {
            throw new IllegalArgumentException("Tool must be provided.");
        }
        Iterator<IEventDefinition> iter = this.getEventDefinition().iterator();
        while (iter.hasNext()) {
            // for (int i = 0; i < eventDefinition.size(); i++) {
            //EventDefinition e = (EventDefinition) new ArrayList(eventDefinition).get(i);
            IEventDefinition e = iter.next();
            if (e instanceof SpecificEventDefinition) {
                SpecificEventDefinition se = (SpecificEventDefinition) e;
                try {
                    if (se.getToolRef().equals(tool) && se.getAction().equals(action)) {
                        se.safeDelete(null, this, iter);
                        iter.remove();
                    }
                } catch (Exception ex) {
                    int a = 5;
                }
            }

        }
    }

    /**
     * *
     * For the provided ToolCategory, reduce this GenericEventDefinitions to
     * SpecificEventDefinitions of its tools and delete them. Then, delete all
     * sevs of this corresponsing to the provided Tool. Do the same for the
     * Actions connected to this.
     *
     * @param toolCat
     * @param tool
     */
    /* void reduceGevsToSevs(ToolCategory toolCat, Tool tool, Action action) throws EarsException {
        if (toolCat == null) {
            throw new IllegalArgumentException("Tool category must be provided.");
        }
        Iterator<IEventDefinition> iter = this.getEventDefinition().iterator();
        while (iter.hasNext()) {
            // for (int i = 0; i < eventDefinition.size(); i++) {
            //EventDefinition e = (EventDefinition) new ArrayList(eventDefinition).get(i);
            IEventDefinition e = iter.next();
            /*if (e instanceof SpecificEventDefinition) {
             SpecificEventDefinition se = (SpecificEventDefinition) e;
             if (se.getToolRef().equals(tool) && (action != null ? se.getAction().equals(action) : true)) {
             se.delete(null);

             }

             } else */
 /*   if (e instanceof GenericEventDefinition) {
                GenericEventDefinition ge = (GenericEventDefinition) e;
                if (ge.getToolCategoryRef().equals(toolCat) && (action != null ? ge.getAction().equals(action) : true)) {
                    Set<SpecificEventDefinition> sevs = ge.convertToSpecificEventDefinition(); //change the gev all into sevs.
                    iter.remove();
                    ge.safeDelete(null, this, iter); //delete this' genericeventdefinition
                    //i = (i - 1 < 0 ? 0 : i - 1);
                    //after converting, resetting i and continuing the loop; the first if will match the sevs.
                }
            }
        }*/
    // }*/
    @Override
    public boolean hasChildren() {
        return false;
    }

    void addToEventDefinition(EventDefinition ev) {
        this.getEventDefinition().add(ev);
    }

    @Override
    public void isolate() {
        if (subjectCollection != null) {
            subjectCollection.clear();
        }

        if (processActionCollection != null) {
            processActionCollection.clear();
        }

        if (processCollection != null) {
            processCollection.clear();
        }
        if (eventDefinition != null) {
            eventDefinition.clear();
        }
        if (actionCollection != null) {
            actionCollection.clear();
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
