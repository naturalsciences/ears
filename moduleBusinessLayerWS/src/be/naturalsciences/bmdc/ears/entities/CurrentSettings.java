/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import java.util.Map;

/**
 *
 * @author thomas
 */
public class CurrentSettings implements CurrentSingleton<Map> {

    private Map<String, Boolean> settings;

    private static final CurrentSettings instance = new CurrentSettings();

    private CurrentSettings() {
    }

    public Map<String, Boolean> getConcept() {
        return settings;
    }

    public static CurrentSettings getInstance(Map currentSettings) {
        if (currentSettings == null) {
            throw new IllegalArgumentException("Vessel can't be null.");
        }
        instance.settings = currentSettings;
        return instance;
    }

    @Override
    public String toString() {
        return settings.toString();
    }
    
}
