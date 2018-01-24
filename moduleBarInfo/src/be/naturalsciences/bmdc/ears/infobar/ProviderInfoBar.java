/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.infobar;

import java.awt.Component;
import org.openide.awt.StatusLineElementProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author yvan
 */
//the implementation must be published to be found by the status bar
@ServiceProvider(service = StatusLineElementProvider.class)
public class ProviderInfoBar  implements StatusLineElementProvider {
    

    private Component component = InfoBar.getInstance().getComponent();
    
    /**
     *
     * @return
     */
    @Override
    public Component getStatusLineElement() {
        return component;
    }
        
}