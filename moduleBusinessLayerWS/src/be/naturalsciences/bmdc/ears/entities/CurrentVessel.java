/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

/**
 *
 * @author Thomas Vandenberghe
 */
public class CurrentVessel implements IVessel, CurrentSingleton<IVessel> {

    private VesselBean currentVessel;

    private static final CurrentVessel instance = new CurrentVessel();

    private CurrentVessel() {
    }

    public VesselBean getConcept() {
        return currentVessel;
    }

    public static CurrentVessel getInstance(VesselBean currentVessel) {
        if (currentVessel == null) {
            throw new IllegalArgumentException("Vessel can't be null.");
        }
        instance.currentVessel = currentVessel;
        return instance;
    }

    @Override
    public String toString() {
        return currentVessel.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CurrentVessel) {
            CurrentVessel other = (CurrentVessel) o;
            return other.getConcept().equals(this.getConcept());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return currentVessel.hashCode();
    }

    @Override
    public boolean isLegal() {
        return true;
    }

    @Override
    public String getCode() {
        return getConcept().getCode();
    }

    @Override
    public void setCode(String code) {
        throw new UnsupportedOperationException("Cannot perform operation"); //To change body of generated methods, choose Tools | Templates.
    }
}
