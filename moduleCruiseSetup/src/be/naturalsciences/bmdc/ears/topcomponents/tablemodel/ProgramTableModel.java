/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents.tablemodel;

import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.EntityTableModel;

/**
 *
 * @author Thomas Vandenberghe
 */
public class ProgramTableModel extends EntityTableModel<ProgramBean> {

    public final static String NAME = " Program";
    public final static String PI_NAME = "PI name";

    private static final String[] COLUMN_NAMES = {NAME, PI_NAME};

    public ProgramTableModel() {
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
    public Object getValueAt(ProgramBean entity, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return entity.getName();
            case 1:
                return entity.getNicePIString();
            default:
                return null;
        }
    }
    
    public ProgramBean getEntityWithName(String name) {
        for (ProgramBean e : entities) {
                if(e.getName().equals(name)){return e;}
        }
            return null;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (value instanceof ProgramBean) {
            ProgramBean program2 = (ProgramBean) value;
            setEntityAt(program2, row);
        }
    }

    @Override
    public void addRow() {
        entities.add(new ProgramBean());
        fireTableDataChanged();
    }

}
