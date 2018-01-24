/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.validators;

import be.naturalsciences.bmdc.ears.base.StaticMetadataSearcher;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;

/**
 * A validator for an organisation JCombobox.
 *
 * @author Thomas Vandenberghe
 */
public final class OrganisationValidator implements Validator<String> {

    @Override
    public void validate(Problems problems, String compName, String q) {
        if (StaticMetadataSearcher.getInstance().getOrganisationByName(q) == null) {
            problems.add("Choose a principal investigator organisation.");
        }

    }

    @Override
    public Class<String> modelType() {
        return String.class;
    }

}
