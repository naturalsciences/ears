/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.eurofleets.ears3.dto;

import be.naturalsciences.bmdc.ears.entities.Actor;
import be.naturalsciences.bmdc.ears.entities.Person;
import javax.persistence.Transient;

/**
 *
 * @author thomas
 */
public class PersonDTO {

    public String firstName;
    public String lastName;
    public String organisation; //an identifier in an external vocabulary, e.g. EDMO (can be urn or url)
    public String phoneNumber;
    public String faxNumber;
    public String email;

    public PersonDTO() {

    }

    public PersonDTO(String firstName, String lastName, String organisation, String phoneNumber, String faxNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisation = organisation;
        this.phoneNumber = phoneNumber;
        this.faxNumber = faxNumber;
        this.email = email;
    }

    public PersonDTO(Person pib) {
        this.firstName = pib.firstName;
        this.lastName = pib.lastName;
        this.organisation = pib.organisationCode;
    }

    public PersonDTO(Actor pib) {
        this.firstName = pib.getFirstName();
        this.lastName = pib.getLastName();
        this.email = pib.getEmail();
    }

    @Transient
    public String getLastNameFirstName() {
        return this.lastName + ((lastName != null && firstName != null) ? " " : "") + this.firstName;
    }
}
