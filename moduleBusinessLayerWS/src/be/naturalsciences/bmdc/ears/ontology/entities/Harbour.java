package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ears.entities.HarbourBean;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.IHarbour;
import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

/**
 *
 * @author Thomas Vandenberghe
 */
@Namespace("http://ontologies.ef-ears.eu/ears2/1#")
@RdfType("Harbour")
public class Harbour implements IHarbour<EarsTerm>, Serializable/*, od.nature.naturalsciences.be.bmdc.entities.IHarbour*/ {

    private static int lastId;

    @Id
    protected URI uri;

    @RdfProperty("http://www.geonames.org/ontology#parentCountry")
    private Country country;

    @RdfProperty("http://www.w3.org/2003/01/geo/wgs84_pos#lon")
    private Double lonDec;

    @RdfProperty("http://www.w3.org/2003/01/geo/wgs84_pos#lat")
    private Double latDec;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#asConcept")
    private EarsTerm termRef;

    public Harbour() {
    }

    @Override
    public void init() {

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
        return this.getTermRef().getPublisherUrn();
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
    public String getCountry() {
        return country.getTermRef().getPrefLabel();
    }

    @Override
    public void setCountry(String country) {
        //this.country = country;
    }

    public Country getCountryObject() {
        return country;
    }

    public void setCountryObject(Country country) {
        this.country = country;
    }

    @Override
    public Double getLonDec() {
        return lonDec;
    }

    @Override
    public void setLonDec(Double lonDec) {
        this.lonDec = lonDec;
    }

    @Override
    public Double getLatDec() {
        return latDec;
    }

    @Override
    public void setLatDec(Double latDec) {
        this.latDec = latDec;
    }

    @Override
    public int getLastId() {
        return ++lastId;
    }

    public static void setLastId(int lastId) {
        Harbour.lastId = lastId;
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
        if (!(object instanceof Harbour)) {
            return false;
        }
        Harbour other = (Harbour) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        if ((this.uri == null && other.uri != null) || (this.uri != null && !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Harbour[ id=" + getId() + " ]";
    }

    @Override
    public Harbour clone(IdentityHashMap<Object, Object> clonedObjects) throws CloneNotSupportedException {
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

    /*
    @Override
    public String getCode() {
        return getUrn();
    }

    @Override
    public void setCode(String code) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return termRef.getEarsTermLabel().getPrefLabel();
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isLegal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
    public HarbourBean toBean() {
        return new HarbourBean(getUrn(), termRef.getEarsTermLabel().getPrefLabel(), getCountry());
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
