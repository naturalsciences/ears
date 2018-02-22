/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.EventTableModel;
import be.naturalsciences.bmdc.ears.entities.Actor;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * country
 *
 * @author Yvan Stojanov
 */
public class ActorCellEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {

    private String actualActor;
    private final Set<Actor> actors;
    private JTable jTableEvent;
    private int numRows;

    public ActorCellEditor(Set<Actor> actors) {
        this.actors = actors;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    @Override
    public Object getCellEditorValue() {
        return this.actualActor;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.jTableEvent = table;
        this.numRows = row;
        if (value instanceof String) {
            this.actualActor = (String) value;
        }

        JComboBox<String> comboActor = new JComboBox<>();
        comboActor.setFont(CreateEventTopComponent.DEFAULT_FONT);
        for (Actor actor : actors) {
            comboActor.addItem(actor.getLastNameFirstName());
        }

        comboActor.setSelectedItem(this.actualActor);
        comboActor.addActionListener(this);

        if (isSelected) {
            comboActor.setBackground(table.getBackground());
        } else {
            comboActor.setBackground(table.getSelectionBackground());
        }

        return comboActor;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JComboBox<String> comboCountry = (JComboBox<String>) event.getSource();
        this.actualActor = (String) comboCountry.getSelectedItem();
        int actorColumn = ((EventTableModel) this.jTableEvent.getModel()).findColumn(EventTableModel.ACTOR);
        this.jTableEvent.setValueAt(this.actualActor, this.numRows, actorColumn);
    }

}
