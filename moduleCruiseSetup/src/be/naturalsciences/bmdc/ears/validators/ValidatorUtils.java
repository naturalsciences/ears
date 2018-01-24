/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.validators;

import javax.swing.JLabel;
import org.netbeans.validation.api.ui.swing.ValidationPanel;

/**
 *
 * @author Thomas Vandenberghe
 */
public class ValidatorUtils {

    public static JLabel getValidatorLabel(ValidationPanel validationPanel) {
        return (JLabel) validationPanel.getComponents()[0];
        /*List<Object> list = new ArrayList(Arrays.asList(validationPanel.getComponents()));
         for (Object o : list) {
         if (o instanceof JLabel) {
         return 
         }
         }*/
    }

    public static void addError(ValidationPanel validationPanel, String msg) {
        JLabel label = getValidatorLabel(validationPanel);
        label.setText(msg);
        label.setIcon(org.netbeans.validation.api.Severity.FATAL.icon());
    }

    public static void removeError(ValidationPanel validationPanel) {
        JLabel label = getValidatorLabel(validationPanel);
        label.setText("");
        label.setIcon(null);
    }
}
