/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import be.naturalsciences.bmdc.ears.utils.SetterField;
import java.util.Objects;

/**
 *
 * @author Thomas Vandenberghe
 */
public class CountryBean implements ICountry, Comparable<CountryBean> {

    private String name;

    private boolean hasHarbours = false;
    private boolean hasOrganisations = false;

    public CountryBean() {

    }

    public CountryBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @SetterField(name = "country")
    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(CountryBean other) {
        return this.getName().compareTo(other.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof CountryBean) {
            CountryBean other = (CountryBean) o;
            return this.getName().equals(other.getName());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean isLegal() {
        return getName() != null;
    }

}
