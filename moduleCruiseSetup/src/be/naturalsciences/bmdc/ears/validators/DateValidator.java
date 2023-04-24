/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.validators;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;

/**
 * A validator for a JXDatePicker date.
 *
 * @author Thomas Vandenberghe
 */
public final class DateValidator implements Validator<String> {

    static SimpleDateFormat jXDatePickerFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Date result;

    @Override
    public void validate(Problems problems, String compName, String date) {
        if (date == null) {
            problems.add("The date in " + compName + " is null.");
        }
        if (date.isEmpty()) {
            problems.add("The date in " + compName + " is empty.");
        }
        try {
            result = jXDatePickerFm.parse(date);
        } catch (Exception e) {
            problems.add("The date in " + compName + " is not a date in yyyy-MM-dd HH:mm:ss.");
        }

    }

    @Override
    public Class<String> modelType() {
        return String.class;
    }

}
