package be.naturalsciences.bmdc.ears.entities;//ys

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "navigation")
@XmlAccessorType(XmlAccessType.FIELD)
public class NavBean implements Serializable {

    public NavBean() {
        super();
    }

    private static final long serialVersionUID = 1L;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "timestamp")
    private String timestamp;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "lon")
    private String lon;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "lat")
    private String lat;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "heading")
    private String heading;
    private String veloc;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "depth")
    private String depth;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "cog")
    private String cog;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "sog")
    private String sog;

    private String timeStampUDPTelegram;

    public String getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(String pTimeStamp) {
        this.timestamp = pTimeStamp;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String pHeading) {
        this.heading = pHeading;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String pLon) {
        this.lon = pLon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String pLat) {
        this.lat = pLat;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String pDepth) {
        this.depth = pDepth;
    }

    public String getCog() {
        return cog;
    }

    public void setCog(String pCog) {
        this.cog = pCog;
    }

    public String getSog() {
        return sog;
    }

    public void setSog(String pSog) {
        this.sog = pSog;
    }

    public String getTimeStampUDPTelegram() {
        return timeStampUDPTelegram;
    }

    public void setFecha_telegrama(String pTimeStampUDPTelegram) {
        this.timeStampUDPTelegram = pTimeStampUDPTelegram;
    }

    @Override
    public String toString() {
        return "Nav [timeStamp=" + timestamp + ", heading="
                + heading + ", lon=" + lon + ", lat=" + lat
                + ", depth=" + depth + ", cog="
                + cog + "sog= " + sog + "]";
    }

}
