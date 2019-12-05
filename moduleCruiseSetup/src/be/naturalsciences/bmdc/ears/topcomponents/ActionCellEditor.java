/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.EventTableModel;
import be.naturalsciences.bmdc.ears.entities.Actor;
import be.naturalsciences.bmdc.ears.ontology.entities.Action;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *  A class that cereates a dropdown menu to change the Action of an event in the event table.
 *
 * @author Thomas Vandenberghe
 */
public class ActionCellEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {

    private String actualAction;
    private JTable jTableEvent;
    private int numRows;

    @Override
    public Object getCellEditorValue() {
        return this.actualAction;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.jTableEvent = table;
        this.numRows = row;
        if (value instanceof String) {
            this.actualAction = (String) value;
        }

        JComboBox<String> comboAction = new JComboBox<>();
        comboAction.setFont(CreateEventTopComponent.DEFAULT_FONT);
        
       /* table.getModel().getEntityAt(table.convertRowIndexToModel(row));
        for (Action action : actions) {
            comboAction.addItem(action.getTermRef().getPrefLabel());
        }*/

        comboAction.setSelectedItem(this.actualAction);
        comboAction.addActionListener(this);

        if (isSelected) {
            comboAction.setBackground(table.getBackground());
        } else {
            comboAction.setBackground(table.getSelectionBackground());
        }

        return comboAction;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JComboBox<String> comboAction = (JComboBox<String>) event.getSource();
        this.actualAction = (String) comboAction.getSelectedItem();
        int actionColumn = ((EventTableModel) this.jTableEvent.getModel()).findColumn(EventTableModel.ACTION);
        this.jTableEvent.setValueAt(this.actualAction, this.numRows, actionColumn);
    }

}
