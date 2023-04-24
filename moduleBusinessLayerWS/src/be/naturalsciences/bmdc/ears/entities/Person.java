/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Thomas Vandenberghe
 */
@XmlTransient
public class Person {

    public String firstName;
    public String lastName;
    public String organisationCode;
    public String organisationName;
    public String country;

    public Person(String firstName, String lastName, String organisationCode, String organisationName, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisationCode = organisationCode;
        this.organisationName = organisationName;
        this.country = country;
    }

    public Person(String firstName, String lastName, OrganisationBean organisation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisationCode = organisation.getCode();
        this.organisationName = organisation.getName();
        this.country = organisation.getCountry();
    }

    public OrganisationBean getOrganisation() {
        return new OrganisationBean(this.organisationCode, this.organisationName, this.country);
    }
}
