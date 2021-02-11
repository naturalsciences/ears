/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents.tablemodel;

import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.topcomponents.UpdateCruiseTopComponent;
import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.FilterableTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author thomas
 */
public class CruiseTableModel extends AbstractTableModel implements FilterableTableModel {
    
    public static final String NAME = "Cruise name (code)";
    public static final String START_DATE = "Start date";
    public static final String END_DATE = "End date";
    public static final String CHIEF_SCIENTIST = "Chief scientist";
    private final String[] columnNames = {NAME, START_DATE, END_DATE, CHIEF_SCIENTIST};
    private List<CruiseBean> data;
    private final UpdateCruiseTopComponent outer;

    public CruiseTableModel(Collection<CruiseBean> cruises, final UpdateCruiseTopComponent outer) {
        this.outer = outer;
        data = new ArrayList<>();
        addCruiseList(cruises);
        
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public int findColumn(String name) {
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public List<Integer> getRowsByQueryAndColumn(String searchBy, String column) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            String name = (String) getValueAt(i, findColumn(column));
            if (name.contains(searchBy)) {
                result.add(i);
            }
        }
        return result;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(!data.isEmpty()){
            CruiseBean cruise = data.get(row);
            switch (getColumnName(column)) {
                case NAME:
                    return cruise.getName();
                case START_DATE:
                    return CruiseBean.DAY_FORMAT.format(cruise.getdStartDate());
                case END_DATE:
                    return CruiseBean.DAY_FORMAT.format(cruise.getdEndDate());
                case CHIEF_SCIENTIST:
                    return cruise.getNiceChiefScientistString();
                default:
                    return null;
            }
        }else{
        return null;
        }
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    @Override
    public Class getColumnClass(int c) {
        try {
           return getValueAt(0, c).getClass();
        } catch (Exception e) {
            int a = 5;
        }
        return null;
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        //dummy method. Nothing will be added to this table anyway
    }

    public CruiseBean getCruise(int row) {
        return data.get(row);
    }

    public void addCruise(CruiseBean cruise) {
        data.add(cruise);
    }

    public void addCruiseList(Collection<CruiseBean> cruises) {
        for (CruiseBean cruise : cruises) {
            addCruise(cruise);
        }
    }

    public void removeCruiseFromVisualRepresentation(int row) {
        data.remove(row);
    }

    public void clearAllCruises() {
        data.retainAll(new ArrayList());
    }

    public void refreshModel(Collection<CruiseBean> cruises) {
        this.clearAllCruises();
        this.addCruiseList(cruises);
        fireTableDataChanged();
    }
    
}
