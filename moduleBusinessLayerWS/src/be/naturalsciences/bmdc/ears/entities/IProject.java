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
public interface IProject extends EARSConcept {

    public ICountry getCountryObject();

    public IOrganisation getOrganisationObject();
    
    public String getOrganisation();

    public String getCode();

    public void setCode(String code);

}
