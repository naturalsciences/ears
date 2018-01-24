/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.validators;

import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import java.text.ParseException;
import java.util.Date;
import org.jdesktop.swingx.JXDatePicker;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;

/**
 *
 * @author Thomas Vandenberghe
 */
public final class StartDateValidator implements Validator<String> {

    private JXDatePicker otherDatepicker;
    private DateValidator validator;

    public StartDateValidator(JXDatePicker otherDatepicker) {
        this.otherDatepicker = otherDatepicker;
        validator = new DateValidator();
    }

    @Override
    public void validate(Problems problems, String compName, String startDate) {
        validator.validate(problems, compName, startDate);
        Date dStartDate = validator.result;

        //Date otherDate = otherDatepicker.getDate(); //doesn't work!! returns the previous value, the non-edited date.
        String otherDateS = otherDatepicker.getEditor().getText(); //provides the edited date before it is set as the date for the editor

        Date otherDate = null;
        if (otherDateS != null && !otherDateS.equals("")) {
            try {
                otherDate = StringUtils.ISO_DATETIME_FORMAT.parse(otherDateS);

            } catch (ParseException ex) {
                problems.add("Cannot parse the date " + otherDateS);
            }
        }

        if (dStartDate != null && otherDate != null && dStartDate.after(otherDate)) {
            problems.add("The start date lies after the end date.");
        }
    }

    @Override
    public Class<String> modelType() {
        return String.class;
    }

}
