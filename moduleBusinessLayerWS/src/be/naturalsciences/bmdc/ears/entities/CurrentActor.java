/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import eu.eurofleets.ears3.dto.PersonDTO;

/**
 *
 * @author Thomas Vandenberghe
 */
public class CurrentActor implements IActor, CurrentSingleton<IActor> {

    private PersonDTO currentActor;

    private static final CurrentActor instance = new CurrentActor();

    private CurrentActor() {
    }

    public PersonDTO getConcept() {
        return currentActor;
    }

    public static CurrentActor getInstance(PersonDTO currentActor) {
        instance.currentActor = currentActor;
        return instance;
    }

    @Override
    public boolean isLegal() {
        return true;
    }

    @Override
    public String getName() {
        return currentActor.getLastNameFirstName();
    }
}
