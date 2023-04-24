package be.naturalsciences.bmdc.ears.entities;//ys

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "platfrom")
public class PlatformClassBean implements Serializable, IPlatformClass, Comparable<PlatformClassBean> {

    public PlatformClassBean() {
        super();
    }

    public PlatformClassBean(String code, String pClass) {
        super();
        this.code = code;
        this.name = pClass;
    }

    public static PlatformClassBean fromMap(Map<String, String> map) {
        PlatformClassBean r = new PlatformClassBean();
        for (String key : map.keySet()) {
            if (key.equals("key")) {
                r.setCode(map.get(key));
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

    @XmlAttribute
    private String code;

    private String name;

    @XmlAttribute(namespace = "http://www.eurofleets.eu/", name = "platformCode")
    public String getCode() {
        return code;
    }

    public void setCode(String platformCode) {
        this.code = platformCode;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "class")
    public String getName() {
        return name;
    }

    public void setName(String platformClass) {
        this.name = platformClass;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(PlatformClassBean other) {
        return this.getName().compareTo(other.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PlatformClassBean) {
            PlatformClassBean other = (PlatformClassBean) o;
            return this.getCode().equals(other.getCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.code);
        return hash;
    }

    public boolean isLegal() {
        return this.code != null && this.name != null;
    }

}
