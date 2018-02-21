/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents.tablemodel;

import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.EntityTableModel;
import be.naturalsciences.bmdc.ears.entities.SeaAreaBean;

/**
 *
 * @author Thomas Vandenberghe
 */
public class SeaAreaTableModel extends EntityTableModel<SeaAreaBean> {

    public final static String NAME = "Sea Area";
    public final static String CODE = "ID";

    private static final String[] COLUMN_NAMES = {NAME, CODE};

    public SeaAreaTableModel() {
        super(null, null);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch (column) {
            case 0:
                return true;
            case 1:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String[] getColumns() {
        return COLUMN_NAMES;
    }

    @Override
    public Object getValueAt(SeaAreaBean entity, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return entity.getName();
            case 1:
                return entity.getCode();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (value instanceof SeaAreaBean) {
            SeaAreaBean sea2 = (SeaAreaBean) value;
            setEntityAt(sea2, row);
        }
    }

    @Override
    public void addRow() {
        entities.add(new SeaAreaBean());
        fireTableDataChanged();
    }

}
