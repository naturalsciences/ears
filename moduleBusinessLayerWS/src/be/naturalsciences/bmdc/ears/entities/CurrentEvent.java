/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import eu.eurofleets.ears3.dto.EventDTO;

/**
 *
 * @author thomas
 */
public class CurrentEvent implements CurrentSingleton<EventDTO> {

    private EventDTO currentEvent;

    private static final CurrentEvent instance = new CurrentEvent();

    private CurrentEvent() {
    }

    public EventDTO getConcept() {
        return currentEvent;
    }

    public static CurrentEvent getInstance(EventDTO currentEvent) {
        instance.currentEvent = currentEvent;
        return instance;
    }
}
