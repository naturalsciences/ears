/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Yvan Stojanov
 */
public class ActorCellRenderer
        extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        if (value == null) {
            setText(null);
        } else if (value instanceof String) {
            setText(value.toString());
        }

        setFont(CreateEventTopComponent.DEFAULT_FONT);

        setBackground(table.getBackground());
        return this;
    }
}
