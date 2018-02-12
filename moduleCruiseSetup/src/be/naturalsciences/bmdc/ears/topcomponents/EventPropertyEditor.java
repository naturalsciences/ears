package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.entities.EventBean;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author thomas
 */
class EventPropertyEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    
    private JButton button;
    private JDialog dialog;
    protected static final String EDIT = "edit";
    private final JTable table;
    private EventBean currentEvent;
    
    public EventPropertyEditor(JTable table) {
        this.table = table;
    }

    /**
     * Handles events from the editor button and from the dialog's OK button.
     */
    public void actionPerformed(ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {
            //The user has clicked the cell, so
            //bring up the dialog.
            //button.setBackground(currentColor);

            dialog.setVisible(true);

            //Make the renderer reappear.
            fireEditingStopped();
            
        } else { //User pressed dialog's "OK" button.
            //  currentColor = colorChooser.getColor();
        }
    }

    //Implement the one CellEditor method that AbstractCellEditor doesn't.
    @Override
    public Object getCellEditorValue() {
        return this.currentEvent;
    }

    //Implement the one method defined by TableCellEditor.
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.currentEvent = ((EventTableModel) table.getModel()).getEntities().get(row);
        if (this.currentEvent.hasProperties()) {
            button = new JButton();
            button.setText("Properties");
            button.setActionCommand(EDIT);
            button.addActionListener(this);
            button.setBorderPainted(false);
            JFrame owner = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this.table);
            
            dialog = new EventPropertyDialog(owner, true, button, null, null, this.currentEvent);
            return button;
        } else {
            return null;
        }
    }
    
}
