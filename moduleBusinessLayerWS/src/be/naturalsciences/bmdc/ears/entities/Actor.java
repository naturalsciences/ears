/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import java.util.Objects;

/**
 *
 * @author yvan
 */
public class Actor implements IActor, Comparable<Actor> {

    private String id;
    private String firstNameOfActor;
    private String lastNameOfActor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstNameOfActor() {
        return firstNameOfActor;
    }

    public void setFirstNameOfActor(String firstNameOfActor) {
        this.firstNameOfActor = firstNameOfActor;
    }

    public String getLastNameOfActor() {
        return lastNameOfActor;
    }

    public void setLastNameOfActor(String lastNameOfActor) {
        this.lastNameOfActor = lastNameOfActor;
    }

    public String getLastNameFirstName() {
        return this.lastNameOfActor + ((lastNameOfActor != null && firstNameOfActor != null) ? " " : "") + this.firstNameOfActor;
    }

    public Actor(String id, String nameOfActor, String surnameOfActor) {
        this.id = id;
        this.firstNameOfActor = nameOfActor;
        this.lastNameOfActor = surnameOfActor;
    }

    public boolean isComplete() {
        return !this.id.isEmpty() && !this.firstNameOfActor.isEmpty() && !this.lastNameOfActor.isEmpty();
    }

    @Override
    public int compareTo(Actor a) {
        return this.getLastNameFirstName().compareToIgnoreCase(a.getLastNameFirstName());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Actor) {
            Actor other = (Actor) o;
            return this.getId().equals(other.getId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
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

}
