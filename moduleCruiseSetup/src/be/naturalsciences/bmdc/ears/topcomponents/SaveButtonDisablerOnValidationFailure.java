/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import java.util.EventListener;
import javax.swing.text.JTextComponent;
import org.netbeans.validation.api.ui.ValidationGroup;

/**
 *
 * @author thomas
 */
interface SaveButtonDisablerOnValidationFailure extends EventListener {

    void enableThatButtonGreysOutOnValidationFailure(JTextComponent c, ValidationGroup group);
    
    void disableThatButtonGreysOutOnValidationFailure(JTextComponent c);

    void checkValidation();

    public ValidationGroup getValidationGroup();
}
