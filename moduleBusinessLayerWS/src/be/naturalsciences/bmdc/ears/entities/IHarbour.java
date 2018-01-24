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
public interface IHarbour extends EARSConcept {

    public ICountry getCountryObject();

    public String getCode();

    public void setCode(String code);

    public String getName();

    public void setName(String name);

    public String getCountry();

    public void setCountry(String country);
}
