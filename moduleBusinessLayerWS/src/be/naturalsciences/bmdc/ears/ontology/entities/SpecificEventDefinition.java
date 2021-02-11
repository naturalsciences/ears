package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ears.entities.EventBean;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.ISpecificEventDefinition;
import gnu.trove.map.hash.THashMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

@Namespace("http://ontologies.ef-ears.eu/ears2/1#")
@RdfType("SpecificEventDefinition")
public class SpecificEventDefinition extends EventDefinition implements ISpecificEventDefinition<EarsTerm, Tool, ProcessAction, EventDefinition, GenericEventDefinition, Property>, Serializable {

    public static int lastId;
    //public class SpecificEventDefinition extends EventDefinition<EarsTerm, Property, EventDefinition> implements ISpecificEventDefinition<EarsTerm, Tool, ProcessAction, EventDefinition, GenericEventDefinition, Property> {

    //private static final long serialVersionUID = 1L;
    private Long eventRef;

    @RdfProperty(value = "http://ontologies.ef-ears.eu/ears2/1#withTool", inverseOf = "http://ontologies.ef-ears.eu/ears2/1#involvedInEvent")
    private Tool toolRef;

    private transient ProcessAction processAction;

    @RdfProperty(value = "http://ontologies.ef-ears.eu/ears2/1#hasAction", inverseOf = "http://ontologies.ef-ears.eu/ears2/1#actionPartOfEvent")
    private Action action;

    @RdfProperty(value = "http://ontologies.ef-ears.eu/ears2/1#hasProcess", inverseOf = "http://ontologies.ef-ears.eu/ears2/1#processPartOfEvent")
    private Process process;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#asConcept")
    private EarsTerm termRef;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#realizes")
    private GenericEventDefinition realizesRef;

    @Override
    public GenericEventDefinition getRealizesRef() {
        return realizesRef;
    }

    @Override
    public String getUrn() {
        return this.getTermRef().getPublisherUrn();
    }

    @Override
    public void setRealizesRef(GenericEventDefinition realizesRef) {
        this.realizesRef = realizesRef;
    }

    public SpecificEventDefinition() {
        super();
    }

    public SpecificEventDefinition(Long eventRef) {
        this();
        this.eventRef = eventRef;
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
    public Tool getToolRef() {
        return toolRef;
    }

    @Override
    public void setToolRef(Tool toolRef) {
        this.toolRef = toolRef;
    }

    @Override
    public ProcessAction getProcessAction() {
        if (processAction == null && process != null && action != null) {
            processAction = new ProcessAction(process, action);
            return processAction;
        } else if (processAction == null && process == null && action == null) {
            return null;
        } else {
            return processAction;
        }
    }

    @Override
    public void setProcessAction(ProcessAction processAction) {
        this.processAction = processAction;
        this.action = processAction.getAction();
        this.process = processAction.getProcess();
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
    public int getLastId() {
        return ++lastId;
    }

    public static void setLastId(int lastId) {
        SpecificEventDefinition.lastId = lastId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        /*hash ^= (getId() != null ? getId().hashCode() : 0);
        hash ^= (getUri() != null ? getUri().hashCode() : 0);*/
        hash ^= (getToolRef() != null ? getToolRef().hashCode() : 0);
        hash ^= (getProcess() != null ? getProcess().hashCode() : 0);
        hash ^= (getAction() != null ? getAction().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof SpecificEventDefinition)) {
            return false;
        }
        SpecificEventDefinition other = (SpecificEventDefinition) object;
        /*if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        if ((this.uri == null && other.uri != null) || (this.uri != null && !this.uri.equals(other.uri))) {
            return false;
        }
        return true;*/
        boolean toolsEqual = false;
        boolean processEqual = false;
        boolean actionEqual = false;
        if ((this.getToolRef() == null ^ other.getToolRef() == null)) {
            return false;
        }
        if ((this.getProcess() == null ^ other.getProcess() == null)) {
            return false;
        }
        if ((this.getAction() == null ^ other.getAction() == null)) {
            return false;
        }
        if ((this.getToolRef() == null && other.getToolRef() == null) || this.getToolRef().equals(other.getToolRef())) {
            toolsEqual = true;
        }
        if ((this.getProcess() == null && other.getProcess() == null) || this.getProcess().equals(other.getProcess())) {
            processEqual = true;
        }
        if ((this.getAction() == null && other.getAction() == null) || this.getAction().equals(other.getAction())) {
            actionEqual = true;
        }
        return toolsEqual && processEqual && actionEqual;
    }

    public boolean equals(EventBean event) {
        if (event == null) {
            return false;
        }
       
        return (this.getToolRef().getUri().toString().equals(event.getTool().getIdentifier()))
                && this.getProcess().getUri().toString().equals(event.getProcessUri())
                && this.getAction().getUri().toString().equals(event.getActionUri());
    }

    @Override
    public String toString() {
        return "eventRef=" + eventRef + ";TOOL=" + getToolRef().toString() + ";PROC=" + getProcess().toString() + ";ACT=" + getAction().toString();
    }

    @Override
    public SpecificEventDefinition clone(IdentityHashMap<Object, Object> clonedObjects) throws CloneNotSupportedException {
        if (clonedObjects.get(this) == null) {

            be.naturalsciences.bmdc.ears.utils.Cloner<SpecificEventDefinition> cc = new be.naturalsciences.bmdc.ears.utils.Cloner(this, clonedObjects);
            SpecificEventDefinition shallowClone = cc.cloneOriginal();

            /*shallowClone.process = this.getProcessJson().clone(clonedObjects);
        shallowClone.action = this.getActionJson().clone(clonedObjects);
        return shallowClone;*/
            be.naturalsciences.bmdc.ears.utils.Cloner<Process> ccProcess = new be.naturalsciences.bmdc.ears.utils.Cloner(this.getProcess(), clonedObjects);
            Process shallowProcess = ccProcess.cloneOriginal();

            be.naturalsciences.bmdc.ears.utils.Cloner<Action> ccAction = new be.naturalsciences.bmdc.ears.utils.Cloner(this.getAction(), clonedObjects);
            Action shallowAction = ccAction.cloneOriginal();

            shallowClone.process = shallowProcess;
            shallowClone.action = shallowAction;

            Map<Collection, Collection> collectionIdentityHashMap = new THashMap<>();
            collectionIdentityHashMap.put(this.process.getEventDefinition(), shallowProcess.getEventDefinition());
            collectionIdentityHashMap.put(this.process.getActionCollection(), shallowProcess.getActionCollection());
            collectionIdentityHashMap.put(this.process.getProcessCollection(), shallowProcess.getProcessCollection());
            collectionIdentityHashMap.put(this.process.getSubjectCollection(), shallowProcess.getSubjectCollection());
            ccProcess.cloneCollection(collectionIdentityHashMap);

            Map<Collection, Collection> collectionIdentityHashMap2 = new THashMap<>();
            //collectionIdentityHashMap2.put(this.action.getEventDefinition(), shallowAction.getEventDefinition());
            collectionIdentityHashMap2.put(this.action.getProcessActionCollection(), shallowAction.getProcessActionCollection());
            ccAction.cloneCollection(collectionIdentityHashMap2);

            Map<Collection, Collection> collectionIdentityHashMap3 = new THashMap<>();
            collectionIdentityHashMap3.put(this.getPropertyCollection(), shallowClone.getPropertyCollection());
            cc.cloneCollection(collectionIdentityHashMap3);

            return shallowClone;
        }
        return (SpecificEventDefinition) clonedObjects.get(this);
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        sb.append(toolRef.getTermRef().getEarsTermLabel().getPrefLabel());
        sb.append(": ");
        sb.append(process.getTermRef().getEarsTermLabel().getPrefLabel());
        sb.append("-");
        sb.append(action.getTermRef().getEarsTermLabel().getPrefLabel());
        sb.append(" (");
        sb.append(this.getTermRef().getUri());
        sb.append("--");
        sb.append(this.getUri());
        sb.append(")");
        return sb.toString();

    }

    /*@Override
     public void setLabel() {
     ProcessAction ac = this.getProcessAction();
     termRef.getEarsTermLabel().setPrefLabel(this.getTermRef().getEarsTermLabelEn().getPrefLabel());
     }*/

 /*@Override
     public String getLabel() {
     return termRef.getEarsTermLabel().getPrefLabel();
     }*/
    @Override
    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    @Override
    public Process getProcess() {
        return process;
    }

    @Override
    public Set<AsConcept> getChildren(ConceptHierarchy parents) {
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
        return this.getTermRef().getKind();
    }

    @Override
    public Class getParentType() {
        return null;
    }

    @Override
    public Class getChildType() {
        return null;
    }

    /**
     * *
     * Delete all references to this. Parents can be null. Do not call this
     * method when either the tool reference, process or action is deleted
     * inside an iterator.
     *
     *
     * @param parents
     */
    @Override
    public void delete(ConceptHierarchy parents) {
        toolRef.getSpecificEventDefinitionCollection().remove(this);
        if (processAction != null && processAction.getSpecificEventDefinitionCollection() != null) {
            processAction.getSpecificEventDefinitionCollection().remove(this);
        }
        process.getEventDefinition().remove(this);
        action.getEventDefinition().remove(this);
        Iterator<Property> iter = this.getPropertyCollection().iterator();
        while (iter.hasNext()) {
            //for (Property prop : this.getPropertyCollection()) {
            Property prop = iter.next();
            prop.getEventDefinitionCollection().remove(this);
        }
        GenericEventDefinition gev = (GenericEventDefinition) realizesRef;
        if (gev != null && gev.getSpecificEventDefinitionCollection() != null) {
            gev.getSpecificEventDefinitionCollection().remove(this);
        }
    }

    public void safeDelete(ConceptHierarchy parents, AsConcept caller, Iterator iter) {
        if (iter != null) {
            if (!caller.getClass().equals(Tool.class)) {
                toolRef.getSpecificEventDefinitionCollection().remove(this);
            }
            if (processAction != null && processAction.getSpecificEventDefinitionCollection() != null) {
                processAction.getSpecificEventDefinitionCollection().remove(this);
            }
            if (!caller.getClass().equals(Process.class)) {
                process.getEventDefinition().remove(this);
            }
            if (!caller.getClass().equals(Action.class)) {
                action.getEventDefinition().remove(this);
            }
            //Iterator<Property> iter2 = this.getPropertyCollection().iterator();
            //while (iter2.hasNext()) {
            for (Property prop : this.getPropertyCollection()) {
                //Property prop = iter2.next();
                prop.getEventDefinitionCollection().remove(this);
            }
            GenericEventDefinition gev = (GenericEventDefinition) realizesRef;
            if (gev != null && gev.getSpecificEventDefinitionCollection() != null) {
                gev.getSpecificEventDefinitionCollection().remove(this);
            }
        } else {
            throw new IllegalArgumentException("Null iterator provided!");
        }
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    /**
     * *
     * Ensure that the process of this refers back to this and likewise for the
     * action. Result: this now has a bidirectional relationship with its
     * process and its action.
     */
    @Override
    public void linkMyProcessAndActionBackToMe() {
        this.getAction().addToEventDefinition(this);
        this.getProcess().addToEventDefinition(this);
    }

    /**
     * *
     * Ensure that this SpecificEventDefinition its tool is set to the provided
     * tool, its process to the provided process and its action to the provided
     * action. Result:
     *
     * @param tool
     * @param process
     * @param action
     */
    public void linkToolProcessAndActionToMe(Tool tool, Process process, Action action) {
        this.setToolRef(tool);
        tool.getSpecificEventDefinitionCollection().add(this);
        this.setProcess(process);
        this.setAction(action);

        linkMyProcessAndActionBackToMe();
        //action.getEventDefinition().add(this);
        //process.getEventDefinition().add(this);
        process.getActionCollection().add(action);
    }

    public void addLabel(String desc) {

        EarsTermLabelEn label = new EarsTermLabelEn();

        label.setPrefLabel(desc);
        label.setAltLabel(desc);
        label.setDefinition(desc);

        this.getTermRef().setTermLabel(label);
    }

    public String generateSevLabel() {
        if (getToolRef() != null && getProcess() != null && getAction() != null) {
            return getToolRef().getTermRef().getEarsTermLabelEn().getPrefLabel() + ": " + getProcess().getTermRef().getEarsTermLabelEn().getPrefLabel() + " "
                    + getAction().getTermRef().getEarsTermLabelEn().getPrefLabel();
        } else {
            throw new IllegalArgumentException("Can't create a label for a SpecificEventDefinition without a tool, process, or action");
        }
    }

    @Override
    public void isolate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
