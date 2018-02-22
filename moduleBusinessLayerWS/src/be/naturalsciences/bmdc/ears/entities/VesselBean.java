/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import be.naturalsciences.bmdc.ears.utils.SetterField;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Yvan Stojanov
 */
public class VesselBean implements IVessel, Comparable<VesselBean> {

    private String vesselName;
    private String code;

    /*public VesselBean(String vesselName, String vesselSDNCode) {
        this.vesselName = vesselName;
        this.code = vesselSDNCode;
    }*/

    public VesselBean() {
    }

    public static VesselBean fromMap(Map<String, String> map) {
        VesselBean r = new VesselBean();
        for (String key : map.keySet()) {
            if (key.equals("key")) {
                r.setCode(map.get(key));
            } else if (key.equals("label")) {
                r.setVesselName(map.get(key));
            }
        }
        if (r.isLegal()) {
            return r;
        } else {
            return null;
        }
    }

    public boolean isLegal() {
        return vesselName != null && code != null;
    }

    public String getVesselName() {
        return vesselName;
    }

    @SetterField(name = "vesselName")
    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getCode() {
        return code;
    }

    @SetterField(name = "code")
    public void setCode(String code) {
        this.code = code;
    }

    public boolean equalsConcept(AsConcept concept) {
        return this.getCode().equals(concept.getTermRef().getOrigUrn());
    }

    @Override
    public String toString() {
        return getVesselName() + " (" + getCode() + ")";
    }

    @Override
    public int compareTo(VesselBean other) {
        int i = vesselName.compareTo(other.vesselName);
        if (i != 0) {
            return i;
        }
        return code.compareTo(other.code);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof VesselBean) {
            VesselBean other = (VesselBean) o;
            return this.vesselName.equals(other.vesselName) && this.code.equals(other.code);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.vesselName);
        hash = 29 * hash + Objects.hashCode(this.code);
        return hash;
    }

}
