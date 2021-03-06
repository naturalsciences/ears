package be.naturalsciences.bmdc.ears.entities;//ys

import be.naturalsciences.bmdc.utils.StringUtils;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    /**
     * Parse string dates of the form 2020-01-21T18:22:42+00 to an
     * OffsetDateTime
     *
     * @return
     */
    public OffsetDateTime getOffsetDateTime() {
        if (getTimeStamp() != null && !getTimeStamp().equals("")) {
            LocalDateTime ld = LocalDateTime.parse(getTimeStamp(), StringUtils.DTF_ISO_DATETIME_ZONE);
            return ld.atOffset(ZoneOffset.UTC);
        } else {
            return null;
        }
    }

    public String getLatitudeDMS(boolean useSeconds) {
        double latitude = Double.valueOf(getLat());
        return toDMS(latitude, LatOrLon.LAT, useSeconds);

    }

    public enum LatOrLon {
        LAT, LON
    }

    private String toDMS(double number, LatOrLon latOrLon, boolean useSeconds) {
        String suffix = null;
        if (latOrLon == LatOrLon.LAT) {
            if (number < 0) {
                suffix = "S";
            } else {
                suffix = "N";
            }
        } else if (latOrLon == LatOrLon.LON) {
            if (number < 0) {
                suffix = "W";
            } else {
                suffix = "E";
            }
        }
        number = Math.abs(number);
        long degrees = (long) Math.floor(number);
        double minutesAndSeconds = (number - degrees) * 60;
        long minutes = (long) Math.floor(minutesAndSeconds);
        double seconds = (minutesAndSeconds - minutes) * 60;
        DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.CEILING);
        df.setMinimumFractionDigits(5);
        if (useSeconds) {
            return Long.toString(degrees) + "°" + suffix + " " + Long.toString(minutes) + "' " + df.format(seconds) + "''";
        } else {
            return Long.toString(degrees) + "°" + suffix + " " + df.format(minutesAndSeconds) + "'";
        }
    }

    public String getLongitudeDMS(boolean useSeconds) {
        double longitude = Double.valueOf(getLon());
        return toDMS(longitude, LatOrLon.LON, useSeconds);
    }

    @Override
    public String toString() {
        return "Nav [timeStamp=" + timestamp + ", heading="
                + heading + ", lon=" + lon + ", lat=" + lat
                + ", depth=" + depth + ", cog="
                + cog + "sog= " + sog + "]";
    }

}
