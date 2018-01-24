/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import java.net.URL;

/**
 *
 * @author Thomas Vandenberghe
 */
public class CurrentURL implements IProgram, CurrentSingleton<URL> {

    private URL currentUrl;

    private static final CurrentURL instance = new CurrentURL();

    private CurrentURL() {
    }

    public URL getConcept() {
        return currentUrl;
    }

    public static CurrentURL getInstance(URL currentUrl) {
        if (currentUrl == null) {
            throw new IllegalArgumentException("URL can't be null.");
        }
        instance.currentUrl = currentUrl;
        return instance;
    }

    @Override
    public boolean isLegal() {
        return true;
    }
}
