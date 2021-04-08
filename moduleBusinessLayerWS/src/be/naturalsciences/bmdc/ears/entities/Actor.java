/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import eu.eurofleets.ears3.dto.PersonDTO;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yvan Stojanov
 */
@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "actor")
public class Actor implements IActor, Comparable<Actor> {

    private int id;
    private String firstName;
    private String lastName;
    private String email;

    public Actor(PersonDTO p) {
        this.firstName = p.firstName;
        this.lastName = p.lastName;
        this.email = p.email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastNameFirstName() {
        return this.lastName + ((lastName != null && firstName != null) ? " " : "") + this.firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Actor() {
    }

    public Actor(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Actor(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public boolean isComplete() {
        return !this.email.isEmpty() && !this.firstName.isEmpty() && !this.lastName.isEmpty();
    }

    @Override
    public int compareTo(Actor a) {
        return this.getLastNameFirstName().compareToIgnoreCase(a.getLastNameFirstName());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Actor) {
            Actor other = (Actor) o;
            return Objects.equals(this.getEmail(), other.getEmail());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.getEmail());
        return hash;
    }

    @Override
    public String toString() {
        return getLastNameFirstName();
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
