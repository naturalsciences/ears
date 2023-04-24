package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ears.entities.OrganisationBean;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.IOrganisation;
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
@RdfType("Organisation")
public class Organisation implements IOrganisation<EarsTerm, Organisation>, Serializable/*, od.nature.naturalsciences.be.bmdc.entities.IOrganisation*/ {

    private static int lastId;

    @Id
    protected URI uri;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#asConcept")
    private EarsTerm termRef;

    private Double lonDec;
    private Double latDec;

    @RdfProperty("http://www.w3.org/2006/vcard/ns#street-address")
    private String deliveryPoint;

    @RdfProperty("http://www.w3.org/2006/vcard/ns#postal-code")
    private String postalCode;

    @RdfProperty("http://www.w3.org/2006/vcard/ns#postal-code")
    private String city;

    @RdfProperty("http://www.w3.org/2006/vcard/ns#locality")
    private String administrativeArea;

    @RdfProperty("http://www.w3.org/2006/vcard/ns#country-name")
    private String country;

    @RdfProperty("http://www.geonames.org/ontology#parentCountry")
    private Country countryObject;

    @RdfProperty("http://www.w3.org/2006/vcard/ns#hasEmail")
    private String electronicMailAddress;

    private String voice;
    private String facsimile;
    private String onlineResource;

    private Organisation collateCentre;

    public Organisation() {
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
    public String getDeliveryPoint() {
        return deliveryPoint;
    }

    @Override
    public void setDeliveryPoint(String deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getAdministrativeArea() {
        return administrativeArea;
    }

    @Override
    public void setAdministrativeArea(String administrativeArea) {
        this.administrativeArea = administrativeArea;
    }

    @Override
    public String getElectronicMailAddress() {
        return electronicMailAddress;
    }

    @Override
    public void setElectronicMailAddress(String electronicMailAddress) {
        this.electronicMailAddress = electronicMailAddress;
    }

    @Override
    public String getVoice() {
        return voice;
    }

    @Override
    public void setVoice(String voice) {
        this.voice = voice;
    }

    @Override
    public String getFacsimile() {
        return facsimile;
    }

    @Override
    public void setFacsimile(String facsimile) {
        this.facsimile = facsimile;
    }

    @Override
    public String getOnlineResource() {
        return onlineResource;
    }

    @Override
    public void setOnlineResource(String onlineResource) {
        this.onlineResource = onlineResource;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    public Country getCountryObject() {
        return countryObject;
    }

    public void setCountryObject(Country countryObject) {
        this.countryObject = countryObject;
    }

    @Override
    public Organisation getCollateCentre() {
        return collateCentre;
    }

    @Override
    public void setCollateCentre(Organisation collateCentre) {
        this.collateCentre = collateCentre;
    }

    @Override
    public int getLastId() {
        return ++lastId;
    }

    public static void setLastId(int lastId) {
        Organisation.lastId = lastId;
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
        if (!(object instanceof Organisation)) {
            return false;
        }
        Organisation other = (Organisation) object;
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
        return "Organisation[ id=" + getId() + " ]";
    }

    @Override
    public Organisation clone(IdentityHashMap<Object, Object> clonedObjects) throws CloneNotSupportedException {
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
    }

    @Override
    public String getCode() {
        return getUrn();
    }

    @Override
    public void setCode(String code) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
    public OrganisationBean toBean() {
        return new OrganisationBean(getUrn(), termRef.getEarsTermLabel().getPrefLabel(), getCountry());
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
