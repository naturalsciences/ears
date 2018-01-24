package be.naturalsciences.bmdc.ears.entities;//ys

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "categoryName")
public class CategoryBean implements Serializable, IVesselCategory, Comparable<CategoryBean> {

    private String pCode;
    private String name;

    public CategoryBean() {
        super();
    }

    public CategoryBean(String pName, String pCode) {
        super();
        this.name = pName;
        this.pCode = pCode;
    }

    private static final long serialVersionUID = 1L;

    public String getCode() {
        return pCode;
    }

    public void setCode(String pCode) {
        this.pCode = pCode;
    }

    @XmlValue
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
    public int compareTo(CategoryBean other) {
        return this.getName().compareTo(other.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CategoryBean) {
            CategoryBean other = (CategoryBean) o;
            return this.getCode().equals(other.getCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.pCode);
        return hash;
    }

    public boolean isLegal() {
        return this.name != null && this.pCode != null;
    }

}
