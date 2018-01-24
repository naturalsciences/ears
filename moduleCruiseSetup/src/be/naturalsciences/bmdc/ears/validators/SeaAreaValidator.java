/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.validators;

import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;

/**
 * A validator for a Sea Area
 *
 * @author Thomas Vandenberghe
 */
public final class SeaAreaValidator implements Validator<String> {

    @Override
    public void validate(Problems problems, String compName, String q) {
        if (q.isEmpty()) {
            problems.add("Choose a sea area");
        }

    }

    @Override
    public Class<String> modelType() {
        return String.class;
    }

}
