package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ears.comparator.TermLabelComparator;
import be.naturalsciences.bmdc.ears.ontology.AsConceptFlavor;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.IAction;
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
 * A class representing an Action. It corresponds to an Action in EARS1 and in
 * the webservices.
 */
@Namespace("http://ontologies.ef-ears.eu/ears2/1#")
@RdfType("Action")
public class Action implements IAction<EarsTerm, ProcessAction>, Transferable, Serializable {

    public static int lastId = 0;

    @Id
    protected URI uri;

    private Boolean isIncident;
    private transient Collection<ProcessAction> processActionCollection;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#asConcept")
    private EarsTerm termRef;

    @RdfProperty(inverseOf = "http://ontologies.ef-ears.eu/ears2/1#hasAction")
    private Collection<EventDefinition> eventDefinition;

    public Action() {

    }

    @Override
    public void init() {
        eventDefinition = new ArrayList<>();
        processActionCollection = new ArrayList<>();
    }

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
    public Boolean getIsIncident() {
        return isIncident;
    }

    @Override
    public void setIsIncident(Boolean isIncident) {
        this.isIncident = isIncident;
    }

    @Override
    public Collection<ProcessAction> getProcessActionCollection() {
        return processActionCollection;
    }

    @Override
    public void setProcessActionCollection(Collection<ProcessAction> processActionCollection) {
        this.processActionCollection = processActionCollection;
    }

    @Override
    public EarsTerm getTermRef() {
        return termRef;
    }

    @Override
    public void setTermRef(EarsTerm termRef) {
        this.termRef = termRef;
    }

    public Collection<EventDefinition> getEventDefinition() {
        return eventDefinition;
    }

    public void setEventDefinition(Collection<EventDefinition> eventDefinition) {
        this.eventDefinition = eventDefinition;
    }

    @Override
    public int getLastId() {
        return ++lastId;
    }

    public static void setLastId(int lastId) {
        //if (Action.lastId == 0) {
        Action.lastId = lastId;
        //}
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
        if (!(object instanceof Action)) {
            return false;
        }
        Action other = (Action) object;
        if (!(this.getId() == null && other.getId() == null) && ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId())))) {
            return false;
        }
        if ((this.uri == null && other.uri != null) || (this.uri != null && !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    /*boolean equalsNbChildren(Action otherAction, ConceptHierarchy thisParents, ConceptHierarchy otherParents) {
        thisParents.add(this);
        otherParents.add(otherAction);
        if (!this.equals(otherAction)) {
            return false;
        }
        boolean allLevelsTrue = true;
        if (this.getChildren(thisParents).size() != otherAction.getChildren(otherParents).size()) {
            return false;
        }
        if (this.getChildren(thisParents).size() > 0) {
            for (Property property : this.getChildren(thisParents)) {
                for (Property otherProperty : otherAction.getChildren(otherParents)) {
                    if (property.equals(otherProperty)) {
                        return property.equalsNbChildren(otherProperty, thisParents, otherParents);
                    }
                }

            }
        }
        return true;
    }*/
    @Override
    public Action clone(IdentityHashMap<Object, Object> clonedObjects) throws CloneNotSupportedException {
        be.naturalsciences.bmdc.ears.utils.Cloner<Action> cc = new be.naturalsciences.bmdc.ears.utils.Cloner(this, clonedObjects);
        Action shallowClone = cc.cloneOriginal();

        Map<Collection, Collection> collectionIdentityHashMap = new THashMap<>();
        collectionIdentityHashMap.put(this.eventDefinition, shallowClone.eventDefinition);
        collectionIdentityHashMap.put(this.processActionCollection, shallowClone.processActionCollection);
        cc.cloneCollection(collectionIdentityHashMap);
        return shallowClone;
    }

    @Override
    public String toString() {
        return "id=" + getId() + "; hash=" + System.identityHashCode(this) + "; name=" + ((this.getTermRef() != null) ? this.getTermRef().getName() : "no name");
    }

    @Override
    public Set<Property> getChildren(ConceptHierarchy parents) {
        if (this.getTermRef().getPrefLabel().equals("Setup")) {
            int a = 5;
        }
        Set<Property> propertyList = new TreeSet<>(new TermLabelComparator());
        if (parents != null) {
            ITool tool = parents.getLowestToolInHierarchy();
            IProcess process = parents.getProcess();
            List<EventDefinition> eventList = process.getEventDefinitionCollection(tool, this);

            for (EventDefinition e : eventList) {
                if (e instanceof SpecificEventDefinition) {
                    SpecificEventDefinition se = (SpecificEventDefinition) e;
                    if (!se.getPropertyCollection().isEmpty()) {
                        propertyList.addAll(se.getPropertyCollection());
                    }
                } else if (e instanceof GenericEventDefinition) {
                    GenericEventDefinition ge = (GenericEventDefinition) e;
                    if (ge.getPropertyCollection() != null && !ge.getPropertyCollection().isEmpty()) {
                        propertyList.addAll(ge.getPropertyCollection());
                    }
                }
            }
            //Collections.sort(propertyList, new TermUriComparator());
        }
        return propertyList;
    }

    @Override
    public List<AsConcept> getParents() {
        /*
         Collection<ProcessAction> lpa = getProcessActionCollection();
         List<AsConcept> lp = new ArrayList();
         for (ProcessAction pa : lpa) {
         lp.add(pa.getProcess());
         }
         return lp;*/
        Collection<EventDefinition> eventList = getEventDefinition();
        List<AsConcept> lp = new ArrayList<>();

        for (EventDefinition e : eventList) {
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

    /**
     * Adds a property to this action given its parent tool and process. If the
     * tool, process and action are combined within a GEV, a SEV is created
     * based on the GEV, without any properties already present. The property is
     * added to the new SEV If the tool, process and action are combined within
     * a SEV, the property is added to the SEV.
     *
     * @param targetParents
     * @param childConcept
     * @param model Irrelevant, can be null
     */
    @Override
    public void addToChildren(ConceptHierarchy targetParents, AsConcept childConcept, boolean removePreviousBottomUpAssociations, ConceptHierarchy newChildParents, IAsConceptFactory factory) {
        Tool tool = (Tool) targetParents.getLowestToolInHierarchy();
        Process process = (Process) targetParents.getProcess();
        if (process != null && tool != null && childConcept != null && childConcept instanceof Property) {
            Property property = (Property) childConcept;

            for (GenericEventDefinition e : tool.getGenericEventDefinitionCollection(process, this)) { //can contain at most 1 element (unless tool belongs to multiple toolcats)
                SpecificEventDefinition sev = null;
                tool.getSpecificEventDefinitionCollection().add(sev);
                try {
                    sev = factory.build(SpecificEventDefinition.class);
                } catch (Exception ex) {
                    throw new RuntimeException("There was a problem with creating the Specific Event definition.", ex);
                }
                sev.addProperty(property);

                sev.setToolRef(tool);
                tool.getSpecificEventDefinitionCollection().add(sev);

                sev.setProcess(process);
                sev.setAction(this);
                this.getEventDefinition().add(sev);
                process.getEventDefinition().add(sev);
                process.getActionCollection().add(this);//if not yet the case.
            }
            for (SpecificEventDefinition sev : tool.getSpecificEventDefinitionCollection(process, this)) { //can contain at most 1 element (unless tool belongs to multiple toolcats)
                sev.addProperty(property);
            }
        }

    }

    @Override
    public String getKind() {
        return this.getTermRef().getKind();
    }

    @Override
    public Class getParentType() {
        return Process.class;
    }

    @Override
    public Class getChildType() {
        return Property.class;
    }

    public transient static final AsConceptFlavor OWN_DATA_FLAVOR = new AsConceptFlavor(Action.class, "action");

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

    public static Action createDummyAction() {
        Action a = new Action();
        a.uri = null;
        a.setUri(null);
        a.setTermRef(new EarsTerm());
        return a;
    }

    @Override
    public void delete(ConceptHierarchy parents) {
        Tool tool = (Tool) parents.getLowestToolInHierarchy();
        ToolCategory toolCat = (ToolCategory) parents.getToolCategory();
        Process process = (Process) parents.getProcess();

        //toolCat.reduceGevsToSevs(process, this);
        tool.deleteSpecificEventDefinitions(process, this);
        //process.reduceGevsToSevs(toolCat, tool, this);
        process.deleteSpecificEventDefinitions(tool, this);
        //this.reduceGevsToSevs(toolCat, tool, process);
        this.deleteSpecificEventDefinitions(tool, process);
        /*for (int i = 0; i < eventDefinition.size(); i++) {
         EventDefinition e = (EventDefinition) new ArrayList(eventDefinition).get(i);

         if (e instanceof SpecificEventDefinition) {
         SpecificEventDefinition se = (SpecificEventDefinition) e;
         if (se.getToolRef().equals(tool) && se.getProcess().equals(process)) {
         se.delete(null);
         }
         } else if (e instanceof GenericEventDefinition) {
         GenericEventDefinition ge = (GenericEventDefinition) e;
         if (ge.getToolCategoryRef().equals(toolCat) && ge.getProcess().equals(process)) {
         Set<SpecificEventDefinition> sevs = ge.convertToSpecificEventDefinition(); //change the gev all into sevs.
                    
         ge.delete(null); //delete this' genericeventdefinition
         i = (i - 1 < 0 ? 0 : i - 1);
         //after converting, resetting i and continuing the loop; the first if will match the sevs.
         }

         }
         }*/
    }

    void deleteSpecificEventDefinitions(Tool tool, Process process) {
        if (tool == null) {
            throw new IllegalArgumentException("Tool must be provided.");
        }
        Iterator<EventDefinition> iter = this.getEventDefinition().iterator();
        while (iter.hasNext()) {
            // for (int i = 0; i < eventDefinition.size(); i++) {
            //EventDefinition e = (EventDefinition) new ArrayList(eventDefinition).get(i);
            EventDefinition e = iter.next();
            if (e instanceof SpecificEventDefinition) {
                SpecificEventDefinition se = (SpecificEventDefinition) e;
                if (se.getToolRef().equals(tool) && (process != null ? se.getProcess().equals(process) : true)) {
                    iter.remove();
                    se.safeDelete(null, this, iter);
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
    /*  void reduceGevsToSevs(ToolCategory toolCat, Tool tool, Process process) throws EarsException {
        if (process == null) {
            throw new IllegalArgumentException("Process must be provided.");
        }
        Iterator<EventDefinition> iter = this.getEventDefinition().iterator();
        while (iter.hasNext()) {
            EventDefinition e = iter.next();
            if (e instanceof GenericEventDefinition) {
                GenericEventDefinition ge = (GenericEventDefinition) e;
                if (ge.getToolCategoryRef().equals(toolCat) && (process != null ? ge.getProcess().equals(process) : true)) {
                    Set<SpecificEventDefinition> sevs = ge.convertToSpecificEventDefinition(); //change the gev all into sevs.
                    iter.remove();
                    ge.safeDelete(null, this, iter);
                }
            }
        }
    }*/
    @Override
    public boolean hasChildren() {
        return false;
    }

    void addToEventDefinition(EventDefinition ev) {
        this.getEventDefinition().add(ev);
    }

    /* void removeEventDefinition(SpecificEventDefinition sev) {
     this.getEventDefinition().remove(sev);
     }

     void removeEventDefinition(Tool tool, Process process) {
     this.getEventDefinition().remove(se);
     }*/
    @Override
    public void isolate() {
        if (processActionCollection != null) {
            processActionCollection.clear();
        }
        if (eventDefinition != null) {
            eventDefinition.clear();
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
