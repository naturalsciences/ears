package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.EarsTermLabel;
import be.naturalsciences.bmdc.ontology.entities.IObjectValue;
import be.naturalsciences.bmdc.ontology.entities.Term;
import java.io.Serializable;
import java.net.URI;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.RdfType;

@Namespace("http://ontologies.ef-ears.eu/ears2/1#")
@RdfType("ObjectValue")
public class ObjectValue implements IObjectValue<EarsTerm, CompoundEarsTerm, Property>, Serializable {

    private static int lastId;

    //private static final long serialVersionUID = 1L;
    @Id
    protected URI uri;
    protected Long id;

    private EarsTerm valuedTermRef;

    private Property property;

    private CompoundEarsTerm compoundEarsTerm;

    public ObjectValue() {
    }

    public ObjectValue(Long id) {
        this.id = id;
    }

    @Override
    public void init() {
    }

    @Override
    public Property getProperty() {
        return property;
    }

    @Override
    public void setProperty(Property property) {
        this.property = property;
    }

    @Override
    public int getLastId() {
        return ++lastId;
    }

    public static void setLastId(int lastId) {
        ObjectValue.lastId = lastId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash ^= (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof ObjectValue)) {
            return false;
        }
        ObjectValue other = (ObjectValue) object;
        if (this.property == null || this.valuedTermRef == null || other.property == null || other.valuedTermRef == null) {
            return false;
        }
        return (this.property.getId() == other.property.getId() && this.valuedTermRef.getId() == other.valuedTermRef.getId());
    }

    @Override
    public String toString() {
        return "ObjectValue[ id=" + id + " ]";
    }

    @Override
    public ObjectValue clone(IdentityHashMap<Object, Object> clonedObjects) throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLabel() {
        String s = this.property.getTermRef().getEarsTermLabelEn().getPrefLabel() + ": " + this.valuedTermRef.getEarsTermLabel().getPrefLabel();
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
    public void setTermRef(CompoundEarsTerm termRef) {
        compoundEarsTerm = termRef;
    }

    @Override
    public Term getValuedTermRef() {
        return valuedTermRef;
    }

    @Override
    public void setValuedTermRef(EarsTerm valuedTermRef) {
        this.valuedTermRef = valuedTermRef;
    }

    @Override
    public void addCompoundEarsTerm() {
        CompoundEarsTerm cef = new CompoundEarsTerm();
        CompoundEarsTermLabel cefl = new CompoundEarsTermLabel();
        this.setTermRef(cef);
        cef.setTermLabel(cefl);
        cef.setId(this.getId());
        cef.setKind("OBV");
        cef.setStatus(null);
        cef.setOrigUrn(null);
        this.setLabel();
    }

    @Override
    public Long getId() {
        return this.id;
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
        return "OBV";
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
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public void setId(Long l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
