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
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "message")
public class ExceptionMessage implements Serializable, IResponseMessage {

    private String timestamp;
    private int status;
    private String error;
    private String exception;
    private String message;

    public ExceptionMessage() {
    }

    /**
     * *
     * Constructor for a generic exceptionmessage without a real exception.
     *
     * @param timeStamp
     * @param message
     */
    public ExceptionMessage(String timeStamp, String message) {
        this.timestamp = timeStamp;
        this.message = message;
        this.exception = "No exception occured";
        this.error = "No http error code applicable";
    }

    /**
     * *
     * Constructor for an exceptionmessage with a real exception.
     *
     * @param timeStamp
     * @param message
     */
    public ExceptionMessage(String timeStamp, Throwable exception) {
        this.timestamp = timeStamp;
        this.message = message + "(" + exception.getMessage() + ")";
        this.exception = exception.toString();
        this.error = "No http error code applicable";
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "status")
    public String getStatusString() {
        return Integer.toString(status);
    }

    public void setStatusString(String status) {
        this.status = Integer.parseInt(status);
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getCode() {
        return null;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "error")
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "exception")
    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getSummary() {
        return getError() + "|" + getMessage() + "|(" + getException() + ")";
    }

    @Override
    public boolean isBad() {
        return true;
    }

}
