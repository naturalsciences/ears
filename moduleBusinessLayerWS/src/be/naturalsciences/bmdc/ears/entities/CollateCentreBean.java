package be.naturalsciences.bmdc.ears.entities;//ys

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "collateCentre")
public class CollateCentreBean implements Serializable{
	
	
	
	public CollateCentreBean() {
		super();
	}

	public CollateCentreBean(String pCode, String pURN) {
		super();
		this.code = pCode;
		this.URN = pURN;
	}

	private static final long serialVersionUID = 1L;
	
	@XmlAttribute
	private String code;
	
	private String URN;
	
	@XmlAttribute(namespace = "http://www.eurofleets.eu/", name = "collateCode")
	public String getCollateCode() {
		return code;
	}

	public void setCollateCode(String code) {
		this.code = code;
	}

	@XmlElement(namespace = "http://www.eurofleets.eu/", name = "URN")
	public String getURN() {
		return URN;
	}

	public void setURN(String urn) {
		this.URN = urn;
	}

	@Override
	public String toString() {
		return "[Code: " + this.getCollateCode() + ", Class: " + this.getURN() + "]";
	}
	
	

}
