/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.entities;

import thewebsemantic.Namespace;
import thewebsemantic.RdfType;

/**
 *
 * @author Thomas Vandenberghe
 */
@Namespace("http://ontologies.ef-ears.eu/ears2/1#")
@RdfType("Incident")
public class Incident extends Action {

    private Incident() {
    }

    /* public Incident(Long id) {
     super(id);
     }*/
}
