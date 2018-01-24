package be.naturalsciences.bmdc.ears.entities;//ys

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "action")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActionBean implements Serializable{
	
	public ActionBean() {
		super();
	}

	public ActionBean(String pName, Collection<PropertyBean> pProperty) {
		super();
		this.name = pName;
		this.properties = pProperty;
	}

	private static final long serialVersionUID = 1L;
	@XmlElement(namespace = "http://www.eurofleets.eu/", name = "name")
	private String name;
	@XmlElementWrapper(namespace = "http://www.eurofleets.eu/", name="propertyList")
	@XmlElement(namespace = "http://www.eurofleets.eu/", name = "property")
	private Collection<PropertyBean> properties = new HashSet<PropertyBean>();
	
	private static StringBuilder dir = new StringBuilder("+");
	private static StringBuilder space = new StringBuilder(" ");

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Collection<PropertyBean> getProperties() {
		return properties;
	}

	public void setProperties(Collection<PropertyBean> properties) {
		this.properties = properties;
	}
	
	@Override
	public String toString() {
	        StringBuilder sb = new StringBuilder();
	        sb.append(this.dir).append("Action....:").append(this.getName());
	        sb.append("\n");
	        
	        if (!this.properties.isEmpty()) {
	        	sb.append(this.space).append("Number of Properties....").append(this.getProperties().size());
		        sb.append("\n");
		        sb.append(this.space).append("**** Properties ******\n");
		        
		        for (PropertyBean property : this.getProperties()) {
		            sb.append(property.toString());
		            sb.append("\n");
		        }
		        
		        sb.append(this.space).append("**** End Properties******\n");
		        sb.append("\n");
	        }
	        
	        return sb.toString();
	}	
	
}
