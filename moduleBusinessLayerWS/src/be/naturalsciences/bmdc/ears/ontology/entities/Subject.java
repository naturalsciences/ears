package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.ISubject;
import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

@Namespace("http://ontologies.ef-ears.eu/ears2/1#")
@RdfType("Subject")
public class Subject implements ISubject<EarsTerm, ToolCategory, Process>, Serializable {

    private static int lastId;

    //private static final long serialVersionUID = 1L;
    @Id
    protected URI uri;
    protected Long id;

    private Collection<Process> processCollection;

    private Collection<ToolCategory> toolCategCollection;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#asConcept")
    private EarsTerm termRef;

    public Subject() {
    }

    public Subject(Long id) {
        this.id = id;
    }

    @Override
    public void init() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
    public Collection<Process> getProcessCollection() {
        return processCollection;
    }

    @Override
    public void setProcessCollection(Collection<Process> processCollection) {
        this.processCollection = processCollection;
    }

    @Override
    public Collection<ToolCategory> getToolCategCollection() {
        return toolCategCollection;
    }

    @Override
    public void setToolCategCollection(Collection<ToolCategory> toolCategCollection) {
        this.toolCategCollection = toolCategCollection;
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
        Subject.lastId = lastId;
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
        if (!(object instanceof Subject)) {
            return false;
        }
        Subject other = (Subject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        if ((this.uri == null && other.uri != null) || (this.uri != null && !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Subject[ id=" + id + " ]";
    }

    @Override
    public Subject clone(IdentityHashMap<Object, Object> clonedObjects) throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    @Override
    public void delete(ConceptHierarchy parents) {
    }

    @Override
    public boolean hasChildren() {
        return false;
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
