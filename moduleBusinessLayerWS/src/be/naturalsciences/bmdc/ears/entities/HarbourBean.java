package be.naturalsciences.bmdc.ears.entities;//ys

import be.naturalsciences.bmdc.ears.utils.SetterField;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "harbor")
public class HarbourBean implements Serializable, IHarbour, Comparable<HarbourBean> {

    public HarbourBean() {
        super();
    }

    public HarbourBean(String pCode, String pName, String pCountry) {
        super();
        this.code = pCode;
        this.name = pName;
        this.country = pCountry;
    }

    public static HarbourBean fromMap(Map<String, String> map) {
        HarbourBean r = new HarbourBean();
        for (String key : map.keySet()) {
            if (key.equals("key")) {
                r.setCode(map.get(key));
            } else if (key.equals("label")) {
                r.setName(map.get(key));
            } else if (key.equals("group")) {
                r.setCountry(map.get(key));
            }
        }
        if (r.isLegal()) {
            return r;
        } else {
            return null;
        }
    }

    private static final long serialVersionUID = 1L;

    @XmlAttribute
    private String code;

    private String name;

    private String country;

    @XmlAttribute(namespace = "http://www.eurofleets.eu/", name = "harborCode")
    public String getCode() {
        return code;
    }

    @SetterField(name = "code")
    public void setCode(String code) {
        this.code = code;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "name")
    public String getName() {
        return name;
    }

    @SetterField(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "country")
    public String getCountry() {
        return country;
    }

    @SetterField(name = "country")
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(HarbourBean other) {
        return this.getName().compareTo(other.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof HarbourBean) {
            HarbourBean other = (HarbourBean) o;
            return this.getCode().equals(other.getCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.code);
        return hash;
    }

    public boolean isLegal() {
        return this.code != null && this.name != null && this.country != null;
    }

    @Override
    public CountryBean getCountryObject() {
        return new CountryBean(getCountry());
    }

}
