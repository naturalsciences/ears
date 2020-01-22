package be.naturalsciences.bmdc.ears.entities;//ys

import be.naturalsciences.bmdc.utils.StringUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "last_thermosalinometer_data")
@XmlAccessorType(XmlAccessType.FIELD)
public class ThermosalBean implements Serializable {

    public ThermosalBean() {
        super();
    }

    private static final long serialVersionUID = 1L;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "date_time")
    private String date;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "salinity")
    private Double salinity;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "surface_water_temperature")
    private Double surfaceWaterTemperature;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "raw_fluorometry")
    private Double fluor;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "conductivity")
    private Double conductivity;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "sigmaT")
    private Double sigmat;

    private Date instrumentTime;

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getSalinity() {
        return this.salinity;
    }

    public void setSalinity(Double salinity) {
        this.salinity = salinity;
    }

    public Double getSurfaceWaterTemperature() {
        return this.surfaceWaterTemperature;
    }

    public void setSurfaceWaterTemperature(Double surfaceWaterTemperature) {
        this.surfaceWaterTemperature = surfaceWaterTemperature;
    }

    public Double getFluor() {
        return this.fluor;
    }

    public void setFluor(Double fluor) {
        this.fluor = fluor;
    }

    public Double getConductivity() {
        return this.conductivity;
    }

    public void setConductivity(Double conductivity) {
        this.conductivity = conductivity;
    }

    public Double getSigmat() {
        return this.sigmat;
    }

    public void setSigmat(Double sigmat) {
        this.sigmat = sigmat;
    }

    public String getInstrumentTime() {
        return this.instrumentTime.toString();
    }

    public void setInstrumentTime(Date instrumentTime) {
        this.instrumentTime = instrumentTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Thermosalinity data for: ");
        sb.append(getDate());
        return sb.toString();
    }

    public OffsetDateTime getOffsetDateTime() {
        if (getInstrumentTime() != null && !getInstrumentTime().equals("")) {
            LocalDateTime ld = LocalDateTime.parse(getInstrumentTime(), StringUtils.DTF_ISO_DATETIME_ZONE);
            return ld.atOffset(ZoneOffset.UTC);
        } else {
            return null;
        }
    }

}
