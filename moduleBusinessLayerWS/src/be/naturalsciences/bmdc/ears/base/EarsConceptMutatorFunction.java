/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.base;

import be.naturalsciences.bmdc.ears.entities.EARSConcept;
import java.util.Map;

/**
 *
 * @author Thomas Vandenberghe
 */
public interface EarsConceptMutatorFunction<E extends EARSConcept> {
    public E execute(Map map);
}
