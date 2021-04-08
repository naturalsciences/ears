/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.EventTableModel;
import eu.eurofleets.ears3.dto.PersonDTO;
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

    private PersonDTO actualActor;
    private final Set<PersonDTO> actors;
    private JTable jTableEvent;
    private int numRows;

    public ActorCellEditor(Set<PersonDTO> actors) {
        this.actors = actors;
    }

    public Set<PersonDTO> getActors() {
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
        JComboBox actorCombobox = new JComboBox<>();
        actorCombobox.setFont(CreateEventTopComponent.DEFAULT_FONT);
        for (PersonDTO actor : actors) {
            actorCombobox.addItem(actor);
            if (actor.toString().equals(value)) {
                this.actualActor = actor;
                actorCombobox.setSelectedItem(this.actualActor);
                isSelected = true;
            }
        }

        actorCombobox.addActionListener(this);

        if (isSelected) {
            actorCombobox.setBackground(table.getBackground());
        } else {
            actorCombobox.setBackground(table.getSelectionBackground());
        }

        return actorCombobox;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JComboBox<String> comboActor = (JComboBox<String>) event.getSource();
        this.actualActor = (PersonDTO) comboActor.getSelectedItem();
        int actorColumn = ((EventTableModel) this.jTableEvent.getModel()).findColumn(EventTableModel.ACTOR);
        this.jTableEvent.setValueAt(this.actualActor, this.numRows, actorColumn);
    }

}
