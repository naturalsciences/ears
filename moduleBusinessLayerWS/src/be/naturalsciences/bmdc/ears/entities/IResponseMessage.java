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
public interface IResponseMessage {
    
    public String getCode();
    
    public String getSummary();
    
    public int getStatus();
    
    public void setStatus(int status);
    
    public boolean isBad();
}
