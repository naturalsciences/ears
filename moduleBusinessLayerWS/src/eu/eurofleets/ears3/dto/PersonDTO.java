/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.eurofleets.ears3.dto;

import be.naturalsciences.bmdc.ears.entities.Actor;
import be.naturalsciences.bmdc.ears.entities.IActor;
import be.naturalsciences.bmdc.ears.entities.Person;
import java.util.Objects;
import javax.persistence.Transient;

/**
 *
 * @author thomas
 */
public class PersonDTO implements IActor {

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

    @Override
    public String toString() {
        return getLastNameFirstName();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.firstName);
        hash = 19 * hash + Objects.hashCode(this.lastName);
        hash = 19 * hash + Objects.hashCode(this.email);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersonDTO other = (PersonDTO) obj;
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isLegal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return getLastNameFirstName();
    }

}
