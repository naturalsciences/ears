/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author thomas
 */
@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "underway")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnderwayBean {

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "timestamp")
    private String date;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "turbidity_l")
    private Double turbidity_l;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "turbidity_h")
    private Double turbidity_h;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "obs_h")
    private Double obs_h;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "obs_l")
    private Double obs_l;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "salinity")
    private Double salinity;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "chlorophyll")
    private Double chlorophyll;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "blue_algae")
    private Double blue_algae;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "cdom")
    private Double cdom;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "ph")
    private Double ph;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "fluorescence")
    private Double fluorescence;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "pco2")
    private Double pco2;
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "par")
    private Double par;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTurbidity_l() {
        return turbidity_l;
    }

    public void setTurbidity_l(Double turbidity_l) {
        this.turbidity_l = turbidity_l;
    }

    public Double getTurbidity_h() {
        return turbidity_h;
    }

    public void setTurbidity_h(Double turbidity_h) {
        this.turbidity_h = turbidity_h;
    }

    public Double getObs_h() {
        return obs_h;
    }

    public void setObs_h(Double obs_h) {
        this.obs_h = obs_h;
    }

    public Double getObs_l() {
        return obs_l;
    }

    public void setObs_l(Double obs_l) {
        this.obs_l = obs_l;
    }

    public Double getSalinity() {
        return salinity;
    }

    public void setSalinity(Double salinity) {
        this.salinity = salinity;
    }

    public Double getChlorophyll() {
        return chlorophyll;
    }

    public void setChlorophyll(Double chlorophyll) {
        this.chlorophyll = chlorophyll;
    }

    public Double getBlue_algae() {
        return blue_algae;
    }

    public void setBlue_algae(Double blue_algae) {
        this.blue_algae = blue_algae;
    }

    public Double getCdom() {
        return cdom;
    }

    public void setCdom(Double cdom) {
        this.cdom = cdom;
    }

    public Double getPh() {
        return ph;
    }

    public void setPh(Double ph) {
        this.ph = ph;
    }

    public Double getFluorescence() {
        return fluorescence;
    }

    public void setFluorescence(Double fluorescence) {
        this.fluorescence = fluorescence;
    }

    public Double getPco2() {
        return pco2;
    }

    public void setPco2(Double pco2) {
        this.pco2 = pco2;
    }

    public Double getPar() {
        return par;
    }

    public void setPar(Double par) {
        this.par = par;
    }
}
