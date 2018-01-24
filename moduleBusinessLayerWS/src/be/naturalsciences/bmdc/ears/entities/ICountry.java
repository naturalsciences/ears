/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

/**
 *
 * @author Thomas Vandenberghe
 */
public interface ICountry extends EARSConcept {

    public String getName();

    public void setName(String name);

    public boolean hasHarbour();

    public void setHasHarbours(boolean hasHarbours);

    public boolean hasOrganisations();

    public void setHasOrganisations(boolean hasOrganisations);
}
