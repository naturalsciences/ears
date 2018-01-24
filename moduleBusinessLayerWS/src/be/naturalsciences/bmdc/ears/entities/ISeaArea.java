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
public interface ISeaArea extends EARSConcept {

    public String getName();

    public void setName(String name);

    public String getCode();

    public void setCode(String code);

    public String getSillyCode();

    public void setSillyCode(String code);
}
