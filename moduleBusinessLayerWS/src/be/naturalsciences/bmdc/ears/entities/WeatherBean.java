package be.naturalsciences.bmdc.ears.entities;//ys

import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "met")
@XmlAccessorType(XmlAccessType.FIELD)
public class WeatherBean implements Serializable {

    public WeatherBean() {
        super();
    }

    private static final long serialVersionUID = 1L;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "timestamp")
    private String date;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "wind_speed")
    private Double windSpeed;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "wind_gust")
    private Double windGust;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "wind_direction")
    private Double windDirection;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "air_temperature")
    private Double airTemperature;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "humidity")
    private Double humidity;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "solar_radiation")
    private Double solarRadiation;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "air_pressure")
    private Double airPressure;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "surface_water_temperature")
    private Double surfaceWaterTemperature;

    private Date instrumentTime;

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getWindSpeed() {
        return this.windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getWindGust() {
        return this.windGust;
    }

    public void setWindGust(Double windGust) {
        this.windGust = windGust;
    }

    public Double getWindDirection() {
        return this.windDirection;
    }

    public void setWindDirection(Double windDirection) {
        this.windDirection = windDirection;
    }

    public Double getAirTemperature() {
        return this.airTemperature;
    }

    public void setAirTemperature(Double airTemperature) {
        this.airTemperature = airTemperature;
    }

    public Double getHumidity() {
        return this.humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getSolarRadiation() {
        return this.solarRadiation;
    }

    public void setSolarRadiation(Double solarRadiation) {
        this.solarRadiation = solarRadiation;
    }

    public Double getAirPressure() {
        return this.airPressure;
    }

    public void setAirPressure(Double airPressure) {
        this.airPressure = airPressure;
    }

    public Double getSurfaceWaterTemperature() {
        return this.surfaceWaterTemperature;
    }

    public void setSurfaceWaterTemperature(Double surfaceWaterTemperature) {
        this.surfaceWaterTemperature = surfaceWaterTemperature;
    }

    public String getInstrumentTime() {
        return this.instrumentTime.toString();
    }

    public void setInstrumentTime(Date date) {
        this.instrumentTime = date;
    }

    /**
     * Parse string dates of the form 2020-01-21T18:22:42+00 to an
     * OffsetDateTime
     *
     * @return
     */
    public OffsetDateTime getOffsetDateTime() {
        if (getInstrumentTime() != null && !getInstrumentTime().equals("")) {
            LocalDateTime ld = LocalDateTime.parse(getInstrumentTime(), StringUtils.DTF_ISO_DATETIME_FLEX);
            return ld.atOffset(ZoneOffset.UTC);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Weather data for: ");
        sb.append(getDate());
        return sb.toString();
    }

}
