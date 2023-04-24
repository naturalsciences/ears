/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.properties;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Yvan Stojanov
 */
public class EventProperties extends PropertyEditorSupport { //not in use now

    @Override
    public String getAsText() {
        Date d = (Date) getValue();
        if (d == null) {
            return "No Date Set";
        }
        return new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(d);
    }

    @Override
    public void setAsText(String s) {
        try {
            setValue(new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse(s));
        } catch (ParseException pe) {
            IllegalArgumentException iae = new IllegalArgumentException("Could not parse date");
            throw iae;
        }
    }
}
