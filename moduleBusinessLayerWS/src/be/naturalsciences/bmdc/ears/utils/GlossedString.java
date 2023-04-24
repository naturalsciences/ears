/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.utils;

import be.naturalsciences.bmdc.ontology.entities.IEarsTerm.Language;

/**
 *
 * @author Thomas Vandenberghe
 */
public class GlossedString {

    private final String underlyingString;
    private final Language language;

    public GlossedString(String underlyingString, Language language) {
        this.underlyingString = underlyingString;
        this.language = language;
    }

    public String getUnderlyingString() {
        return underlyingString;
    }

    public Language getLanguage() {
        return language;
    }

    public String getLanguageString() {
        return language.name();
    }

}
