/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.entities.EARSConcept;
import be.naturalsciences.bmdc.ears.utils.SwingUtils;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import org.netbeans.validation.api.builtin.stringvalidation.StringValidators;
import org.netbeans.validation.api.ui.ValidationUI;

/**
 *
 * @author Thomas Vandenberghe
 */
/**
 * *
 * An editor for comboboxes capable of managing an arbitrary number of different
 * comboboxes in the same column of a table
 *
 * @param <E>
 */
public class ComboBoxColumnEditor<E extends EARSConcept> extends AbstractCellEditor implements TableCellEditor, ItemListener {

    Collection<E> set;
    JComboBox currentComboBox;
    //E currentValue;
    List<JComboBox<E>> lists;
    JTable table;
    int column;
    SaveButtonDisablerOnValidationFailure parent;

    public ComboBoxColumnEditor(Collection<E> set, JTable table, int column, String tooltip, SaveButtonDisablerOnValidationFailure parent) {
        lists = new ArrayList<>();

        this.set = set;
        this.table = table;
        this.column = column;
        this.parent = parent;

        TableColumn col = table.getColumnModel().getColumn(column);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        col.setResizable(true);
        col.setCellRenderer(renderer);
        col.setCellEditor(this);
    }

    public JComboBox addComboBox(String name) {
        JComboBox list = new JComboBox();

        if (set != null) {
            for (E e : set) {
                list.addItem(e);
            }
        }

        list.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    int row = ComboBoxColumnEditor.this.table.getEditingRow();
                    if (row > -1) {
                        AbstractTableModel model = (AbstractTableModel) ComboBoxColumnEditor.this.table.getModel();
                        int column = ComboBoxColumnEditor.this.column;
                        Object value = list.getSelectedItem();
                        Object originalValue = model.getValueAt(row, column);
                        if (originalValue == null || !originalValue.equals(value)) {
                            model.setValueAt(value, row, column);
                            model.fireTableCellUpdated(row, column);
                            model.fireTableDataChanged();
                        }
                    }
                }
            }
        });
        list.setMaximumRowCount(SwingUtils.COMBOBOX_MAX_ROW_COUNT);

        list.setName(name);

        list.addItemListener(this);
        lists.add(list);

        parent.getValidationGroup().add(list, StringValidators.REQUIRE_NON_EMPTY_STRING);
        parent.enableThatButtonGreysOutOnValidationFailure((JTextField) list.getEditor().getEditorComponent(), parent.getValidationGroup());

        return list;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (lists.size() > row) {
            return lists.get(row);
        } else {
            return null;
        }
    }

    @Override
    protected void fireEditingStopped() {
        currentComboBox = lists.get(table.getEditingRow());
        super.fireEditingStopped();
    }

    @Override
    public Object getCellEditorValue() {
        return currentComboBox.getSelectedItem();
    }

    @Override
    public void itemStateChanged(ItemEvent ie) {
        if (ie.getStateChange() == ItemEvent.SELECTED) {
            if (!(ie.getItem() instanceof String)) {
                currentComboBox = (JComboBox) ie.getSource();
            }
        }
    }

    public void removeComboBox(int selectedRow) {
        if (selectedRow < lists.size()) {
            JComboBox box = lists.get(selectedRow);
            ComponentDecorationFactory f = new ComponentDecorationFactory();
            ValidationUI vui = f.decorationFor(box);
            parent.getValidationGroup().removeUI(vui);
            parent.disableThatButtonGreysOutOnValidationFailure((JTextField) box.getEditor().getEditorComponent());
            box.removeItemListener(this);
            lists.remove(box);
        }

    }
}
