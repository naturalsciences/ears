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
public class CurrentActor implements IActor, CurrentSingleton<IActor> {

    private Actor currentActor;

    private static final CurrentActor instance = new CurrentActor();

    private CurrentActor() {
    }

    public Actor getConcept() {
        return currentActor;
    }

    public static CurrentActor getInstance(Actor currentActor) {
        instance.currentActor = currentActor;
        return instance;
    }

    @Override
    public boolean isLegal() {
        return true;
    }
}
