/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.validators;

import java.util.Date;
import org.jdesktop.swingx.JXDatePicker;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;

/**
 *
 * @author Thomas Vandenberghe
 */
public final class EndDateValidator implements Validator<String> {

    private JXDatePicker otherDatepicker;
    private DateValidator validator;

    public EndDateValidator(JXDatePicker otherDatepicker) {
        this.otherDatepicker = otherDatepicker;
        this.validator = new DateValidator();
    }

    @Override
    public void validate(Problems problems, String compName, String endDate) {

        validator.validate(problems, compName, endDate);
        Date dEndDate = validator.result;
        Date otherDate = otherDatepicker.getDate();
        if (dEndDate != null && otherDate != null && dEndDate.before(otherDate)) {
            problems.add("The end date lies before start date.");
        }
    }

    @Override
    public Class<String> modelType() {
        return String.class;
    }

}
