package be.naturalsciences.bmdc.ears.entities;//ys

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "subject")
public class SubjectBean implements Serializable, ISubject, Comparable<SubjectBean> {

    public SubjectBean() {
        super();
    }

    public SubjectBean(String pName) {
        super();
        this.name = pName;
    }

    private static final long serialVersionUID = 1L;

    @XmlAttribute
    private String code;

    private String name;

    @XmlAttribute(namespace = "http://www.eurofleets.eu/", name = "subjectCode")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(SubjectBean other) {
        return this.getCode().compareTo(other.getCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SubjectBean) {
            SubjectBean other = (SubjectBean) o;
            return this.getCode().equals(other.getCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.code);
        return hash;
    }

    @Override
    public boolean isLegal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
