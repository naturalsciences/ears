/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.FilterableTableModel;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Thomas Vandenberghe
 */
public abstract class SearchTextVisitor<M extends TableModel> {

    protected JTextField field;
    protected FilterableTableModel model;
    protected JTable table;
    protected TableRowSorter<M> sorter;
    protected String column;

    public SearchTextVisitor(SearchTextField field, FilterableTableModel model, JTable table, TableRowSorter<M> sorter) {
        this.field = field;
        this.model = model;
        this.table = table;
        this.sorter = sorter;
    }

    protected void filter(String text, int column) {
        RowFilter<M, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(text, column);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    public void embed() {
        field.setEnabled(true);
        field.setEditable(true);

        field.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                /* if (field.getText().equals(org.openide.util.NbBundle.getMessage(UpdateCruiseTopComponent.class, "UpdateCruiseTopComponent.cruiseNameTextField.text"))) {
                 field.setText("");
                 }*/
            }

            @Override
            public void focusLost(FocusEvent e) {
                //Your code here
            }
        });

        field.getDocument().addDocumentListener(new DocumentListener() {

            boolean selectionBySearch = false;

            @Override
            public void removeUpdate(DocumentEvent e) {
                retainRows(e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                retainRows(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                retainRows(e);
            }

            private void retainRows(DocumentEvent e) {

                try {
                    ListSelectionModel selectionModel = table.getSelectionModel();
                    String text = e.getDocument().getText(0, e.getDocument().getLength());
                    if (text.length() > 3) {
                        filter(text, model.findColumn(column));
                    } else {
                        filter("", model.findColumn(column));
                    }
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }

            private void selectRows(DocumentEvent e) {
                /*ArrayList<Integer> allRows = new ArrayList(model.getRowCount());
                 for (int j = 0; j < model.getRowCount(); j++) {
                 allRows.set(j, new Integer(j));
                 }*/
                try {
                    ListSelectionModel selectionModel = table.getSelectionModel();
                    String text = e.getDocument().getText(0, e.getDocument().getLength());
                    if (text.length() > 3) {

                        List<Integer> results = model.getRowsByQueryAndColumn(text, column);

                        if (results.size() > 0) {
                            selectionBySearch = true;
                            for (Integer i : results) {
                                selectionModel.setSelectionInterval(i, i);

                            }
                        } else {
                            selectionModel.clearSelection();
                            selectionBySearch = false;
                        }
                    } else {
                        if (selectionBySearch) {
                            selectionModel.clearSelection();
                            selectionBySearch = false;
                        }
                    }
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

}
