/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.entities.CurrentCruise;
import be.naturalsciences.bmdc.ears.entities.CurrentProgram;
import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.entities.EventBean;
import java.util.Collection;
import java.util.EventObject;

/**
 *
 * @author thomas
 */
public class ExportEventAction extends EventObject {//ActionEvent {

    private Collection<EventBean> events;
    private CurrentVessel vessel;
    private CurrentCruise cruise;
    private CurrentProgram program;

    public Collection<EventBean> getEvents() {
        return events;
    }

    public CurrentVessel getVessel() {
        return vessel;
    }

    public void setVessel(CurrentVessel vessel) {
        this.vessel = vessel;
    }

    public CurrentCruise getCruise() {
        return cruise;
    }

    public void setCruise(CurrentCruise cruise) {
        this.cruise = cruise;
    }

    public CurrentProgram getProgram() {
        return program;
    }

    public void setProgram(CurrentProgram program) {
        this.program = program;
    }

    public void setEvents(Collection<EventBean> events) {
        this.events = events;
    }

    public ExportEventAction(Object source, Collection<EventBean> events, CurrentVessel vessel, CurrentCruise cruise, CurrentProgram program) {
        super(source);
        this.events = events;
        this.vessel = vessel;
        this.cruise = cruise;
        this.program = program;
    }

}
