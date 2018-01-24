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
public class MessageBean implements Serializable, IResponseMessage {

    /**
     * The id of the entity that was just created.
     */
    private String code;

    /**
     * The http response code.
     */
    private int status;

    private String description;

    public MessageBean() {
    }

    public MessageBean(String code, int status, String description) {
        this.code = code;
        this.description = description;
        this.status = status;
    }

    /**
     * The id of the entity that was just created.
     *
     * @return
     */
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "code")
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * A description of the web service action that was just performed.
     *
     * @return
     */
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getSummary() {
        return getDescription() + ": identifier " + getCode();
    }

    @Override
    public boolean isBad() {
        int code;
        if (this.code == null) {
            return this.status < 200 || this.status >= 300;
        } else {
            /*try {
                code = Integer.parseInt(this.code);
            } catch (NumberFormatException ex) {
                return true;
            }*/
            return false;
        }
    }
}
