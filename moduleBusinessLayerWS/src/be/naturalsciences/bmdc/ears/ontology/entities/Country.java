package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ears.entities.CountryBean;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.ICountry;
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
@RdfType("Country")
public class Country implements ICountry<EarsTerm>, Serializable/*, od.nature.naturalsciences.be.bmdc.entities.ICountry*/ {

    private static int lastId;

    @Id
    protected URI uri;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#asConcept")
    private EarsTerm termRef;
    private boolean hasOrganisations = false;
    private boolean hasHarbours = false;

    public Country() {
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
        return this.getTermRef().getPublisherUrn()==null?this.getTermRef().getOrigUrn():this.getTermRef().getPublisherUrn();
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

        Country.lastId = lastId;
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
        if (this == object) {
            return true;
        }
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Harbour)) {
            return false;
        }
        Country other = (Country) object;
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
        return "Country[ id=" + getId() + " ]";
    }

    @Override
    public Country clone(IdentityHashMap<Object, Object> clonedObjects) throws CloneNotSupportedException {
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
    public boolean hasHarbour() {
        return hasHarbours;
    }

    public void setHasHarbours(boolean hasHarbours) {
        this.hasHarbours = hasHarbours;
    }

    @Override
    public boolean hasOrganisations() {
        return hasOrganisations;
    }

    public void setHasOrganisations(boolean hasOrganisations) {
        this.hasOrganisations = hasOrganisations;
    }

    /*@Override
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
    public CountryBean toBean() {
        return new CountryBean(termRef.getEarsTermLabel().getPrefLabel());
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
