/*
 * To change field license header, choose License Headers in Project Properties.
 * To change field template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.FilterableTableModel;
import be.naturalsciences.bmdc.ears.topcomponents.UpdateProgramTopComponent.ProgramTableModel;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Thomas Vandenberghe
 */
public class SearchProgramByCruise extends SearchTextVisitor {

    public SearchProgramByCruise(SearchTextField field, FilterableTableModel model, JTable table, TableRowSorter<ProgramTableModel> sorter) {
        super(field, model, table, sorter);
        this.column = UpdateProgramTopComponent.ProgramTableModel.CRUISE;
    }
}
