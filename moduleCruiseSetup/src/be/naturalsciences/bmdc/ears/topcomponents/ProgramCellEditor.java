/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.EventTableModel;
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
 * @author Thomas Vandenberghe
 */
public class ProgramCellEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {

    private String actualProgram;
    private final Set<ProgramBean> programs;
    private JTable jTableEvent;
    private int numRows;

    public ProgramCellEditor(Set<ProgramBean> programs) {
        this.programs = programs;
    }

    public Set<ProgramBean> getPrograms() {
        return programs;
    }

    @Override
    public Object getCellEditorValue() {
        return this.actualProgram;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.jTableEvent = table;
        this.numRows = row;

        JComboBox programCombobox = new JComboBox<>();
        programCombobox.setFont(CreateEventTopComponent.DEFAULT_FONT);
        for (ProgramBean program : programs) {
            programCombobox.addItem(program.getName());
            if (program.getName().equals(value)) {
                this.actualProgram = program.getName();
                programCombobox.setSelectedItem(this.actualProgram);
                isSelected = true;
            }
        }

        programCombobox.addActionListener(this);

        if (isSelected) {
            programCombobox.setBackground(table.getBackground());
        } else {
            programCombobox.setBackground(table.getSelectionBackground());
        }

        return programCombobox;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JComboBox<String> comboProgram = (JComboBox<String>) event.getSource();
        this.actualProgram = (String) comboProgram.getSelectedItem();
        int programColumn = ((EventTableModel) this.jTableEvent.getModel()).findColumn(EventTableModel.PROGRAM);
        this.jTableEvent.setValueAt(this.actualProgram, this.numRows, programColumn);
    }

}
