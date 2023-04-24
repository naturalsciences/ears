/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import javax.swing.JTextField;

/**
 *
 * @author Thomas Vandenberghe
 */
public class SearchTextField extends JTextField {

    public void SearchTextField() {
    }

    public void accept(SearchTextVisitor visitor) {
        visitor.embed();
    }

}
