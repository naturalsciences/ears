/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import java.util.List;

/**
 *
 * @author Thomas Vandenberghe
 */
public interface IResponseMessage<E> {

    public String getIdentifier();

    public void setIdentifier(String identifier);

    public int getCode();

    public void setCode(int code);

    public String getExceptionType();

    public void setExceptionType(String exceptionType);

    public String getMessage();

    public void setMessage(String message);

    public E getObject();

    public void setObject(E entity);
    
    public List<String> getMessages();

    public boolean isBad();

    public boolean isOk();
}
