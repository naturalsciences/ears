/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import be.naturalsciences.bmdc.ears.ontology.BaseOntology;
import be.naturalsciences.bmdc.ears.ontology.OntologyModel;
import be.naturalsciences.bmdc.ontology.writer.ScopeMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author thomas
 */
public class CurrentOntologyModels implements CurrentSingleton<Set> {

    private LinkedHashSet<OntologyModel> models;

    private static final CurrentOntologyModels instance = new CurrentOntologyModels();

    private CurrentOntologyModels() {
    }

    public LinkedHashSet<OntologyModel> getConcept() {
        return models;
    }

    public BaseOntology getBaseOntology() {
        for (OntologyModel model : models) {
            if (model instanceof BaseOntology) {
                BaseOntology baseModel = (BaseOntology) model;
                return baseModel;
            }
        }
        return null;
    }

    public static CurrentOntologyModels getInstance(LinkedHashSet currentModels) {
        if (currentModels == null) {
            throw new IllegalArgumentException("currentModels can't be null.");
        }
        instance.models = currentModels;
        return instance;
    }

    @Override
    public String toString() {
        return models.toString();
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrentFiles)) {
            return false;
        }
        CurrentFiles f = (CurrentFiles) o;
        return this.getFile().equals(f.getFile());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.file);
        return hash;
    }*/
}
