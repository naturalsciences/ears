package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ears.ontology.AsConceptFlavor;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.IProperty;
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
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

@Namespace("http://ontologies.ef-ears.eu/ears2/1#")
@RdfType("EventProperty")
public class Property implements IProperty<EarsTerm, EventDefinition, ObjectValue>, Transferable, Serializable {

    public static int lastId;

    //private static final long serialVersionUID = 1L;
    @Id
    protected URI uri;
    protected Long id;

    private String dimension;

    private String unit;

    private String formatPattern;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#multiple")
    private boolean multiple;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#mandatory")
    private boolean mandatory;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#isPropertyOf")
    private Collection<EventDefinition> eventDefinitionCollection;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#asConcept")
    private EarsTerm termRef;
    
    private String valueClass;
    

    private Collection<ObjectValue> objectValueCollection;

    public Property() {
    }

    public Property(Long id) {
        this.id = id;
    }

    @Override
    public void init() {
        eventDefinitionCollection = new ArrayList();
        objectValueCollection = new ArrayList();
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
    public String getDimension() {
        return dimension;
    }

    @Override
    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String getFormatPattern() {
        return formatPattern;
    }

    @Override
    public void setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
    }

    @Override
    public Collection<EventDefinition> getEventDefinitionCollection() {
        return eventDefinitionCollection;
    }

    @Override
    public void setEventDefinitionCollection(Collection<EventDefinition> eventDefinitionCollection) {
        this.eventDefinitionCollection = eventDefinitionCollection;
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
    public Collection<ObjectValue> getObjectValueCollection() {
        return objectValueCollection;
    }

    @Override
    public void setObjectValueCollection(Collection<ObjectValue> objectValueCollection) {
        this.objectValueCollection = objectValueCollection;
    }

    @Override
    public boolean isMultiple() {
        return multiple;
    }

    @Override
    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }


    public String getValueClass() {
        return valueClass;
    }

    public void setValueClass(String valueClass) {
        this.valueClass = valueClass;
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
    }

    @Override
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Override
    public int getLastId() {
        return ++lastId;
    }

    public static void setLastId(int lastId) {
        Property.lastId = lastId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash ^= (id != null ? id.hashCode() : 0);
        hash ^= (uri != null ? uri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof Property)) {
            return false;
        }
        Property other = (Property) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        if ((this.uri == null && other.uri != null) || (this.uri != null && !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    boolean equalsNbChildren(Property otherAction, ConceptHierarchy thisParents, ConceptHierarchy otherParents) {
        thisParents.add(this);
        otherParents.add(otherAction);
        if (!this.equals(otherAction)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id=" + getId() + "; hash=" + System.identityHashCode(this) + "; name=" + ((this.getTermRef() != null) ? this.getTermRef().getName() : "no name");
    }

    @Override
    public Property clone(IdentityHashMap<Object, Object> clonedObjects) throws CloneNotSupportedException {
        be.naturalsciences.bmdc.ears.utils.Cloner<Property> cc = new be.naturalsciences.bmdc.ears.utils.Cloner(this, clonedObjects);
        Property shallowClone = cc.cloneOriginal();

        Map<Collection, Collection> collectionIdentityHashMap = new THashMap<>();
        //collectionIdentityHashMap.put(this.eventDefinitionCollection, shallowClone.eventDefinitionCollection);
        collectionIdentityHashMap.put(this.objectValueCollection, shallowClone.objectValueCollection);
        cc.cloneCollection(collectionIdentityHashMap);
        return shallowClone;
    }

    /**
     * Adds the given event definition to this property. Note that this method
     * does not add this property to the event definition as well!
     *
     */
    @Override
    public void addEventDefinition(EventDefinition ev) {
        if (this.eventDefinitionCollection == null) {
            this.setEventDefinitionCollection(new ArrayList<EventDefinition>());
        }
        if (!this.eventDefinitionCollection.contains(ev)) {
            this.eventDefinitionCollection.add(ev);
        }
    }

    /**
     * Adds the given object value to this property. Note that this method does
     * not add this property to the object value as well!
     *
     */
    @Override
    public void addObjectValue(ObjectValue ov) {
        if (this.objectValueCollection == null) {
            this.setObjectValueCollection(new ArrayList<ObjectValue>());
        }
        if (!this.objectValueCollection.contains(ov)) {
            this.objectValueCollection.add(ov);
        }
    }

    @Override
    public Set<AsConcept> getChildren(ConceptHierarchy parents) {
        return new HashSet();
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
        return Action.class;
    }

    @Override
    public Class getChildType() {
        return null;
    }

    public transient static final AsConceptFlavor OWN_DATA_FLAVOR = new AsConceptFlavor(Property.class, "property");

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
        Process process = (Process) parents.getProcess();
        Action action = (Action) parents.getAction();

        Iterator<EventDefinition> iter = this.getEventDefinitionCollection().iterator();
        while (iter.hasNext()) {
            //for (Property prop : this.getPropertyCollection()) {
            EventDefinition ev = iter.next();

            /*  if (ev instanceof GenericEventDefinition) {

                GenericEventDefinition gev = (GenericEventDefinition) ev;
                if (gev.getAction().equals(action) && gev.getProcess().equals(process) && gev.getToolCategoryRef().equals(toolCat)) {
                    try {
                        gev.convertToSpecificEventDefinition();
                    } catch (EarsException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    iter.remove();
                }
            }*/
        }
        iter = this.getEventDefinitionCollection().iterator();
        while (iter.hasNext()) {
            //for (Property prop : this.getPropertyCollection()) {
            EventDefinition ev = iter.next();

            if (ev instanceof SpecificEventDefinition) {

                SpecificEventDefinition sev = (SpecificEventDefinition) ev;
                if (sev.getAction().equals(action) && sev.getProcess().equals(process) && sev.getToolRef().equals(tool)) {
                    sev.getPropertyCollection().remove(this);
                }
            }
        }
        /*for (EventDefinition ev : this.getEventDefinitionCollection()) {
         if (ev instanceof GenericEventDefinition) {
         GenericEventDefinition gev = (GenericEventDefinition) ev;
         gev.convertToSpecificEventDefinition();
         }
         }*/

 /*for (GenericEventDefinition gev : toolCat.getGenericEventDefinitionCollection()) {
         gev.getPropertyCollection().remove(this);
         }
         for (SpecificEventDefinition sev : tool.getSpecificEventDefinitionCollection()) {
         sev.getPropertyCollection().remove(this);
         }
         for (EventDefinition ev : process.getEventDefinitionCollection(tool, action)) {
         ev.getPropertyCollection().remove(this);
         }
         for (EventDefinition ev : action.getEventDefinition()) {
         ev.getPropertyCollection().remove(this);
         }*/
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public void isolate() {
        if (eventDefinitionCollection != null) {
            eventDefinitionCollection.clear();
        }
        if (objectValueCollection != null) {
            objectValueCollection.clear();
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
