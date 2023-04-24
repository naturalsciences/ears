/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import javax.swing.JComponent;
import org.netbeans.validation.api.Problem;
import org.netbeans.validation.api.ui.ValidationUI;
import org.netbeans.validation.api.ui.swing.SwingComponentDecorationFactory;

/**
 *
 * @author thomas
 */
public class ComponentDecorationFactory extends SwingComponentDecorationFactory {

    public ValidationUI decorationFor(final JComponent c) {
        return new ValidationUI() {
            private javax.swing.border.Border origBorder = c.getBorder();

            public void showProblem(Problem problem) {
                if (problem == null) {
                    c.setBorder(origBorder);
                } else {
                    c.setBorder(javax.swing.BorderFactory.createLineBorder(problem.severity().color(), 3));
                }
            }

            @Override
            public void clearProblem() {
                c.setBorder(origBorder);
            }
        };
    }
};
