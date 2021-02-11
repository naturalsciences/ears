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
import be.naturalsciences.bmdc.ears.utils.SetterField;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "seaArea")
public class SeaAreaBean implements Serializable, ISeaArea, Comparable<SeaAreaBean> {

    private String name;
    private String code;
    private String sillyCode;

    public SeaAreaBean() {
        super();
    }

    public SeaAreaBean(String id, String urn, String name) {
        super();
        this.code = id;
        this.sillyCode = urn;
        this.name = name;
    }

    public static SeaAreaBean fromMap(Map<String, String> map) {
        SeaAreaBean r = new SeaAreaBean();
        for (String key : map.keySet()) {
            if (key.equals("key")) {
                r.setCode(map.get(key));
                r.setSillyCode(map.get(key));
            } else if (key.equals("label")) {
                r.setName(map.get(key));
            }
        }
        if (r.isLegal()) {
            return r;
        } else {
            return null;
        }
    }

    private static final long serialVersionUID = 1L;

    @Override
    public String getName() {
        return name;
    }

    @SetterField(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "id")
    public String getCode() {
        return code;
    }

    @SetterField(name = "code")
    public void setCode(String code) {
        this.code = code;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "URN")
    @Override
    public String getSillyCode() {
        return sillyCode;
    }

    public void setSillyCode(String code) {
        this.sillyCode = code;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(SeaAreaBean other) {
        return this.getName().compareTo(other.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SeaAreaBean) {
            SeaAreaBean other = (SeaAreaBean) o;
            return this.getCode().equals(other.getCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.getCode());
        return hash;
    }

    public boolean isLegal() {
        return code != null /*&& sillyCode != null */ && name != null;
    }
}
