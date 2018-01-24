/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

/**
 * An interface for a class of which only one instance may be present.
 *
 * @author Thomas Vandenberghe
 */
public interface CurrentSingleton<E extends Object> {

    public E getConcept();

    public static boolean conceptExists(CurrentSingleton singleton) {
        return singleton != null && singleton.getConcept() != null;
    }
}
