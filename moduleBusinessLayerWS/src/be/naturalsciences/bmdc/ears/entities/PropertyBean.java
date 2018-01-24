package be.naturalsciences.bmdc.ears.entities;//ys

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "property")
@XmlAccessorType(XmlAccessType.FIELD)
public class PropertyBean implements Serializable {

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "PropName")
    private String propName;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "PropValue")
    private String propValue;

    /**
     * *
     * Returns the name (=key) of the property, i.e. the url that uniquely
     * definbes this property
     *
     * @return
     */
    public String getName() {
        return propName;
    }

    public void setName(String propName) {
        this.propName = propName;
    }

    /**
     * *
     * Returns the value of the property.
     *
     * @return
     */
    public String getValue() {
        return propValue;
    }

    public void setValue(String value) {
        this.propValue = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Property Name....:").append(this.getName());
        sb.append("\n");
        sb.append("Property Value....:").append(this.getValue());
        sb.append("\n");

        return sb.toString();

    }

}
