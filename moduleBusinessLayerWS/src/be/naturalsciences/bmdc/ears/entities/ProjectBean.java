/*
 * To change this license header, choose License Headers in ProjectBean Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import be.naturalsciences.bmdc.ears.utils.SetterField;
import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yvan Stojanov
 */
@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "projects")
public class ProjectBean implements Serializable, IProject, Comparable<ProjectBean> {

    /**
     * *
     * The name (=edmerp code) of the project
     */
    private String code;
    /**
     * *
     * The id (=edmerp code) of the project
     */
    // private String id;//<ewsl:projects id="A"> un String

    /**
     * *
     * The full written name of the project
     */
    private String fullName;
    private String acronym;
    private String organisation;
    private OrganisationBean organisationObject;
    private String country;
    private CountryBean countryObject;

    public ProjectBean() {
        super();
    }

    /*public ProjectBean(String id) {
     super();
     this.id = id;
     }*/
    public ProjectBean(String projectName, String id, String fullName, String acronym, String organisation, OrganisationBean organisationObject, String country, CountryBean countryObject) {
        super();
        this.code = projectName;
        //this.id = id;
        this.fullName = fullName;
        this.acronym = acronym;
        this.organisation = organisation;
        this.organisationObject = organisationObject;
        this.country = country;
        this.countryObject = countryObject;
    }

    /*public ProjectBean(String projectName, String id) {
        this.code = projectName;
        this.id = id;
    }*/
    @XmlAttribute(name = "id")
    public String getId() {
        return code;
    }

    public void setId(String id) {
        this.code = id;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "projectName")
    public String getCode() {
        return code;
    }

    @SetterField(name = "code")
    public void setCode(String code) {
        this.code = code;
        //this.id = code;
    }

    public String getAcronym() {
        return acronym;
    }

    @SetterField(name = "acronym")
    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getOrganisation() {
        return organisation;
    }

    @SetterField(name = "organisation")
    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getCountry() {
        return country;
    }

    @SetterField(name = "country")
    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullName() {
        return fullName;
    }

    @SetterField(name = "name")
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /*public static ProjectBean fromMap(Map<String, String> map) {
     ProjectBean r = new ProjectBean();
     for (String key : map.keySet()) {
     if (key.equals("SDNIdent")) {
     r.setId(map.get(key));
     r.setCode(map.get(key));
     } else if (key.equals("acronym")) {
     r.setAcronym(map.get(key));
     } else if (key.equals("organisation")) {
     r.setOrganisation(map.get(key));
     } else if (key.equals("country")) {
     r.setCountry(map.get(key));
     } else if (key.equals("name")) {
     r.setFullName(map.get(key));
     }
     }
     if (r.isLegal()) {
     return r;
     } else {
     return null;
     }
     }*/
    public boolean isLegal() {
        return /*this.id != null &&*/ this.code != null;
    }

    @Override
    public String toString() {
        if (getFullName() != null && getId() != null && !getFullName().isEmpty() && !getId().isEmpty()) {
            return getFullName() + " - (" + getId() + ")";
        } else {
            return null;
        }
    }

    @Override
    public int compareTo(ProjectBean other) {
        if (this.getId() == null) {
            return 0;
        } else {
            return this.getId().compareTo(other.getId());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof ProjectBean) {
            ProjectBean other = (ProjectBean) o;
            if (this.getId() != null) {
                return this.getId().equals(other.getId());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.code);
        return hash;
    }

    @Override
    public CountryBean getCountryObject() {
        return countryObject;
    }

    public void setCountryObject(CountryBean countryObject) {
        this.countryObject = countryObject;
        this.country = countryObject.getName();
    }

    @Override
    public OrganisationBean getOrganisationObject() {
        return organisationObject;
    }

    public void setOrganisationObject(OrganisationBean organisationObject) {
        this.organisationObject = organisationObject;
        if (organisationObject != null) {
            this.organisation = organisationObject.getCode();
        } else {
            this.organisation = null;
        }
    }

}
