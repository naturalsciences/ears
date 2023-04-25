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
import eu.eurofleets.ears3.dto.CruiseDTO;
import eu.eurofleets.ears3.dto.EventDTO;
import eu.eurofleets.ears3.dto.ProgramDTO;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name = "message")
@XmlSeeAlso({EventDTO.class, CruiseDTO.class, ProgramDTO.class})
@XmlAccessorType(XmlAccessType.FIELD) //ignore all the getters
public class RestMessage<E> implements Serializable, IResponseMessage<E> {

    /**
     * The error message.
     */
    public String message;

    /**
     * The http status code.
     */
    public int code;

    /**
     * The identifier of the created entity
     */
    public String identifier;

    /**
     * The error exceptionType.
     */
    public String exceptionType;

    @XmlAnyElement(lax = true)
    public E object;
    private List<String> messages;

    public RestMessage() {
    }

    public RestMessage(String message) {
        this.message = message;
    }

    public RestMessage(Exception e) {
        this.message = e.getMessage();
        this.exceptionType = e.getClass().getSimpleName();
    }

    public RestMessage(String message, Exception e) {
        this.message = message;
        this.exceptionType = e.getClass().getSimpleName();
    }

    public RestMessage(String message, int code, String identifier, String exceptionType, E object) {
        this.message = message;
        this.code = code;
        this.identifier = identifier;
        this.exceptionType = exceptionType;
        this.object = object;
    }

    public RestMessage(int code, String message) {
        this.code = code;
        this.message = message;
        this.exceptionType = null;
    }

    /**
     * The id of the entity that was just created.
     *
     * @return
     */
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    /**
     * A message of the web service action that was just performed.
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean isBad() {
        return this.code < 200 || this.code >= 300;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public E getObject() {
        return object;
    }

    @Override
    public void setObject(E object) {
        this.object = object;
    }

    @Override
    public boolean isOk() {
        return this.code == 200 || this.code == 201 || this.code == 202;
    }

    @Override
    public List<String> getMessages() {
        return messages;
    }
}