package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ears.entities.EventBean;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.EarsTermLabel;
import be.naturalsciences.bmdc.ontology.entities.IGenericEventDefinition;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

@Namespace("http://ontologies.ef-ears.eu/ears2/1#")
@RdfType("GenericEventDefinition")
public class GenericEventDefinition extends EventDefinition implements IGenericEventDefinition<CompoundEarsTerm, EarsTerm, ToolCategory, ProcessAction, EventDefinition, SpecificEventDefinition, Property>, Serializable {

    public static int lastId;

    //private static final long serialVersionUID = 1L;
    private Long eventRef;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#realizedBy")
    private Collection<SpecificEventDefinition> specificEventDefinitionCollection;

    @RdfProperty(value = "http://ontologies.ef-ears.eu/ears2/1#withTool", inverseOf = "http://ontologies.ef-ears.eu/ears2/1#involvedInEvent")
    private ToolCategory toolCategoryRef;

    private transient ProcessAction processAction;

    @RdfProperty(value = "http://ontologies.ef-ears.eu/ears2/1#hasAction", inverseOf = "http://ontologies.ef-ears.eu/ears2/1#actionPartOfEvent")
    private Action action;

    @RdfProperty(value = "http://ontologies.ef-ears.eu/ears2/1#hasProcess", inverseOf = "http://ontologies.ef-ears.eu/ears2/1#processPartOfEvent")
    private Process process;

    private CompoundEarsTerm compoundEarsTerm;

    public GenericEventDefinition() {
        super();
    }

    public GenericEventDefinition(Long ev) {
        this();
        this.eventRef = ev;
    }

    @Override
    public void init() {
    }

    @Override
    public Long getEventRef() {
        return eventRef;
    }

    @Override
    public void setEventRef(Long eventRef) {
        this.eventRef = eventRef;
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
    public ToolCategory getToolCategoryRef() {
        return toolCategoryRef;
    }

    @Override
    public void setToolCategoryRef(ToolCategory tc) {
        this.toolCategoryRef = tc;
    }

    @Override
    public ProcessAction getProcessAction() {
        if (processAction == null && process != null && action != null) {
            processAction = new ProcessAction(process, action);
        }
        return processAction;
    }

    @Override
    public void setProcessAction(ProcessAction pa) {
        this.processAction = pa;
    }

    @Override
    public int getLastId() {
        return ++lastId;
    }

    public static void setLastId(int lastId) {
        GenericEventDefinition.lastId = lastId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash ^= (getId() != null ? getId().hashCode() : 0);
        hash ^= (getUri() != null ? getUri().hashCode() : 0);
        /*hash ^= (getToolCategoryRef() != null ? getToolCategoryRef().hashCode() : 0);
         hash ^= (getProcessJson() != null ? getProcessJson().hashCode() : 0);
         hash ^= (getActionJson() != null ? getActionJson().hashCode() : 0);*/
        return hash;
    }

    @Override
    public GenericEventDefinition clone(IdentityHashMap<Object, Object> clonedObjects) throws CloneNotSupportedException {
        be.naturalsciences.bmdc.ears.utils.Cloner<GenericEventDefinition> cc = new be.naturalsciences.bmdc.ears.utils.Cloner(this, clonedObjects);
        GenericEventDefinition shallowClone = cc.cloneOriginal();
        shallowClone.process = this.getProcess().clone(clonedObjects);
        shallowClone.action = this.getAction().clone(clonedObjects);
        Map<Collection, Collection> collectionIdentityHashMap = new THashMap<>();
        collectionIdentityHashMap.put(this.specificEventDefinitionCollection, shallowClone.specificEventDefinitionCollection);
        cc.cloneCollection(collectionIdentityHashMap);

        return shallowClone;

        /*        be.naturalsciences.bmdc.ears.utils.Cloner<Process> ccProcess = new be.naturalsciences.bmdc.ears.utils.Cloner(this.getProcessJson(), clonedObjects);
        Process shallowProcess = ccProcess.cloneOriginal();
        be.naturalsciences.bmdc.ears.utils.Cloner<Action> ccAction = new be.naturalsciences.bmdc.ears.utils.Cloner(this.getActionJson(), clonedObjects);
        Action shallowAction = ccAction.cloneOriginal();
        shallowClone.process = shallowProcess;
         shallowClone.action = shallowAction;
         
        Map<Collection, Collection> collectionIdentityHashMap = new THashMap<>();
        collectionIdentityHashMap.put(this.specificEventDefinitionCollection, shallowClone.specificEventDefinitionCollection);
        ccThis.cloneCollection(collectionIdentityHashMap);

        return shallowClone;*/
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id/uri fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof GenericEventDefinition)) {
            return false;
        }
        GenericEventDefinition other = (GenericEventDefinition) object;
        /* if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        if ((this.uri == null && other.uri != null) || (this.uri != null && !this.uri.equals(other.uri))) {
            return false;
        }
        return true;*/
        if ((this.getToolCategoryRef() == null ^ other.getToolCategoryRef() == null)) {
            return false;
        }
        if ((this.getProcess() == null ^ other.getProcess() == null)) {
            return false;
        }
        if ((this.getAction() == null ^ other.getAction() == null)) {
            return false;
        }
        return this.getToolCategoryRef().equals(other.getToolCategoryRef()) && this.getProcess().equals(other.getProcess()) && this.getAction().equals(other.getAction());
    }

    /**
     * *
     * Test if this GenericEventDefinition equals the provided
     * SpecificEventDefinition. Equals means the process and action are the same
     * and the tool of the SpecificEventDefinition belongs to the ToolCategory
     * of this.
     *
     * @param object
     * @return
     */
    public boolean equalsSpecificEventDefinition(SpecificEventDefinition object) {
        SpecificEventDefinition other = (SpecificEventDefinition) object;
        return this.action.equals(other.getAction()) && this.process.equals(other.getProcess()) && other.getToolRef().hasOneOf(toolCategoryRef);
    }

    /**
     * *
     * Test if this GenericEventDefinition is found in the provided list of
     * SpecificEventDefinitions, limited to the given tool. Return the
     * SpecificEventDefinition if found, null if not found
     *
     * @param list
     * @return
     */
    public SpecificEventDefinition equalsOneOfSpecificEventDefinition(Set<SpecificEventDefinition> list, Tool tool) {
        for (SpecificEventDefinition sev : list) {
            if (sev.getToolRef().equals(tool) && this.equalsSpecificEventDefinition(sev)) {
                return sev;
            }
        }
        return null;
    }

    public boolean equals(EventBean event) {

        if (event == null) {
            return false;
        }
        return this.getToolCategoryRef().getUri().toString().equals(event.getToolCategoryUri())
                && this.getProcess().getUri().toString().equals(event.getProcessUri())
                && this.getAction().getUri().toString().equals(event.getActionUri());
    }

    @Override
    public String toString() {
        return "eventRef=" + eventRef;
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        sb.append(toolCategoryRef.getTermRef().getEarsTermLabel().getPrefLabel());
        sb.append(": ");
        sb.append(process.getTermRef().getEarsTermLabel().getPrefLabel());
        sb.append("-");
        sb.append(action.getTermRef().getEarsTermLabel().getPrefLabel());
        sb.append(" (");
        sb.append(this.getUri());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public void setLabel() {
        ProcessAction ac = this.getProcessAction();
        String s = this.getToolCategoryRef().getTermRef().getEarsTermLabelEn().getPrefLabel() + ": "
                + ac.getProcess().getTermRef().getEarsTermLabelEn().getPrefLabel() + "-" + ac.getAction().getTermRef().getEarsTermLabelEn().getPrefLabel();
        EarsTermLabel etl = compoundEarsTerm.getEarsTermLabel();
        etl.setPrefLabel(s);
    }

    @Override
    public String getLabel() {
        String s;
        if (compoundEarsTerm == null) {
            addCompoundEarsTerm();
            s = compoundEarsTerm.getEarsTermLabel().getPrefLabel();
        } else {
            s = compoundEarsTerm.getEarsTermLabel().getPrefLabel();
        }
        return s;
    }

    @Override
    public String getPrefLabel() {
        return getLabel();
    }

    @Override
    public CompoundEarsTerm getTermRef() {
        return compoundEarsTerm;
    }

    @Override
    public void setTermRef(CompoundEarsTerm termRef
    ) {
        compoundEarsTerm = termRef;
    }

    @Override
    public void addCompoundEarsTerm() {
        CompoundEarsTerm cef = new CompoundEarsTerm();
        CompoundEarsTermLabel cefl = new CompoundEarsTermLabel();
        this.setTermRef(cef);
        cef.setTermLabel(cefl);
        cef.setId(this.getId());
        cef.setKind("GEV");
        cef.setStatus(null);
        cef.setOrigUrn(null);
        this.setLabel();
    }

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public Process getProcess() {
        return process;
    }

    @Override
    public Set<AsConcept> getChildren(ConceptHierarchy parents
    ) {
        return null;
    }

    @Override
    public List<AsConcept> getParents() {
        return null;
    }

    @Override
    public void addToChildren(ConceptHierarchy parents, AsConcept childConcept, boolean removePreviousBottomUpAssociations, ConceptHierarchy newChildParents, IAsConceptFactory factory) {

    }

    @Override
    public String getKind() {
        return "GEV";
    }

    @Override
    public Class getParentType() {
        return null;
    }

    @Override
    public Class getChildType() {
        return null;
    }

    @Override
    public void delete(ConceptHierarchy parents) {
        toolCategoryRef.getGenericEventDefinitionCollection().remove(this);
        if (processAction != null && processAction.getGenericEventDefinitionCollection() != null) {
            processAction.getGenericEventDefinitionCollection().remove(this);
        }
        process.getEventDefinition().remove(this);
        action.getEventDefinition().remove(this);

        Iterator<Property> iter = this.getPropertyCollection().iterator();
        while (iter.hasNext()) {
            //for (Property prop : this.getPropertyCollection()) {
            Property prop = iter.next();
            prop.getEventDefinitionCollection().remove(this);
        }
        for (SpecificEventDefinition sev : getSpecificEventDefinitionCollection()) {
            sev.setRealizesRef(null);
        }
        this.setTermRef(null);
    }

    public void safeDelete(ConceptHierarchy parents, AsConcept caller, Iterator iter) {
        if (iter != null) {
            if (!caller.getClass().equals(ToolCategory.class)) {
                toolCategoryRef.getGenericEventDefinitionCollection().remove(this);
            }
            if (processAction != null && processAction.getGenericEventDefinitionCollection() != null) {
                processAction.getGenericEventDefinitionCollection().remove(this);
            }
            //Iterator<Property> iter2 = this.getPropertyCollection().iterator();
            //while (iter2.hasNext()) {
            for (Property prop : this.getPropertyCollection()) {
                //Property prop = iter2.next();
                prop.getEventDefinitionCollection().remove(this);
            }
            for (SpecificEventDefinition sev : getSpecificEventDefinitionCollection()) {
                sev.setRealizesRef(null);
            }
        } else {
            throw new IllegalArgumentException("Null iterator provided!");
        }
    }

    /**
     * *
     * Convert this generic event definition to specific event definitions of
     * all the tools of its toolCategory and set all the necessary
     * relationships. Returns the set of the created SpecificEventDefinitions
     */
    Set<SpecificEventDefinition> convertToSpecificEventDefinition(IAsConceptFactory factory) throws EarsException {
        Set<SpecificEventDefinition> result = new THashSet();
        for (Tool tool : this.toolCategoryRef.getToolCollection()) {
            SpecificEventDefinition sev = null;

            sev = factory.build(SpecificEventDefinition.class);

            for (Property property : this.getPropertyCollection()) {
                sev.addProperty(property);
            }

            sev.linkToolProcessAndActionToMe(tool, this.process, this.action);
            sev.addLabel(sev.generateSevLabel());
            /*sev.setToolRef(tool);
             tool.getSpecificEventDefinitionCollection().add(sev);
             //this.getSpecificEventDefinitionCollection().add(sev);
             sev.setProcessJson(this.process);
             sev.setActionJson(this.action);

            

             EarsTermLabelEn label = new EarsTermLabelEn();
             String desc = sev.generateSevLabel();
             label.setPrefLabel(desc);
             label.setAltLabel(desc);
             label.setDefinition(desc);
                
             sev.getTermRef().*/

            result.add(sev);
        }
        return result;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public void linkMyProcessAndActionBackToMe() {
        this.getAction().addToEventDefinition(this);
        this.getProcess().addToEventDefinition(this);
    }

    @Override
    public String getUrn() {
        return this.getTermRef().getPublisherUrn();
    }

    @Override
    public void isolate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
