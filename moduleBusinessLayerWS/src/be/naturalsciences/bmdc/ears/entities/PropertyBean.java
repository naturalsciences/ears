/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import be.naturalsciences.bmdc.ontology.entities.IProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author thomas
 */
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "property")
@XmlAccessorType(XmlAccessType.FIELD)
public class PropertyBean implements Serializable, Comparable<PropertyBean> {

    private String code;
    
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "PropName")
    private String name;
    
    private transient boolean isMandatory;
    private transient boolean isMultiple;
    private transient String valueClass;
    
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "PropValue")
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void setIsMultiple(boolean isMultiple) {
        this.isMultiple = isMultiple;
    }

    public String getValueClass() {
        return valueClass;
    }

    public void setValueClass(String valueClass) {
        this.valueClass = valueClass;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PropertyBean(String code, String name, boolean isMandatory, boolean isMultiple) {
        this.code = code;
        this.name = name;
        this.isMandatory = isMandatory;
        this.isMultiple = isMultiple;
    }

    public PropertyBean clone() {
        PropertyBean prop = new PropertyBean(this.code, this.name, this.isMandatory, this.isMultiple);
        prop.valueClass = valueClass;
        prop.value = value;
        return prop;
    }

    @Override
    public int compareTo(PropertyBean other) {
        int i;
        if (this.value == null && other.value == null) {
            i = 0;
        } else if (this.value != null && other.value != null) {
            i = this.value.compareTo(other.value);
        } else {
            i = -1;
        }
        if (i != 0) {
            return i;
        }
        return this.code.compareTo(other.code);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PropertyBean) {
            PropertyBean other = (PropertyBean) o;
            if (this.code.equals(other.code)) {
                if ((this.value == null && other.value == null) || this.value.equals(other.value)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public boolean equals(IProperty p) {
        return this.code.equals(p.getUri().toString());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.code);
        hash = 97 * hash + Objects.hashCode(this.value);
        return hash;
    }

}
