package be.naturalsciences.bmdc.ears.entities;//ys

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "scientistOrg")
public class ScientistOrgBean implements Serializable{
	
	
	
	public ScientistOrgBean() {
		super();
	}

	public ScientistOrgBean(String pCode, String pName, String pCountry) {
		super();
		this.code = pCode;
		this.name = pName;
		this.country = pCountry;
	}

	private static final long serialVersionUID = 1L;
	
	@XmlAttribute
	private String code;
	
	private String name;
	
	private String country;
	
	@XmlAttribute(namespace = "http://www.eurofleets.eu/", name = "code")
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
	
	@XmlElement(namespace = "http://www.eurofleets.eu/", name = "country")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "[Code: " + this.getCode() + ", Name: " + this.getName() + ", Country: " + this.getCountry() + "]";
	}
	
}
