/*
 * To change field license header, choose License Headers in Project Properties.
 * To change field template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.topcomponents.UpdateCruiseTopComponent.CruiseTableModel;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;


/**
 *
 * @author Thomas Vandenberghe
 */
public class SearchCruiseByName extends SearchTextVisitor {

    public SearchCruiseByName(SearchTextField field, FilterableTableModel model, JTable table, TableRowSorter<CruiseTableModel> sorter) {
        super(field, model, table, sorter);
        this.column = UpdateCruiseTopComponent.CruiseTableModel.NAME;
    }

}
