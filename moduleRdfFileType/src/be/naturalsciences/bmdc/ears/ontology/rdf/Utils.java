/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.rdf;

import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Thomas Vandenberghe
 */
public class Utils {

    public static TopComponent findTopComponent(String name) {
        for (Mode m : WindowManager.getDefault().getModes()) {
            for (TopComponent tp : WindowManager.getDefault().getOpenedTopComponents(m)) {
                if (tp.getName() != null && tp.getName().equals(name)) {
                    return tp;
                }
            }
        }
        return null;
    }

}
