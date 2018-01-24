/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.base.StaticMetadataSearcher;
import be.naturalsciences.bmdc.ears.entities.CountryBean;
import be.naturalsciences.bmdc.ears.entities.IOrganisation;
import be.naturalsciences.bmdc.ears.entities.OrganisationBean;
import be.naturalsciences.bmdc.ears.entities.Person;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Thomas Vandenberghe
 */
public class ChiefScientistTableModel extends AbstractTableModel {

    private List<Map> data;
    private JTable table;
    //private InputOutput io;

    public static final String BASE_ACTION = "Choose organisation";
    public static final String COUNTRY = "Country";
    public static final String ORG = "Organisation";
    public static final String ORG_CODE = "Code";
    public static final String NAME = "Name";
    //private SaveButtonDisablerOnValidationFailure parent;
    public static final String[] COLUMN_NAMES = {COUNTRY, ORG, ORG_CODE, NAME};

    //public static JComboBox chiefScientistOrganisationList;
    //public static JComboBox chiefScientistCountryList;
    public ChiefScientistTableModel() {
        //this.parent = parent;
        data = new ArrayList();
    }

    /*public SaveButtonDisablerOnValidationFailure getParent() {
        return parent;
    }

    public void setParent(SaveButtonDisablerOnValidationFailure parent) {
        this.parent = parent;
    }*/
    

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch (column) {
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                return false;
            case 3:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }

    @Override
    public int findColumn(String name) {
        for (int i = 0; i < COLUMN_NAMES.length; i++) {
            if (COLUMN_NAMES[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (row > -1 && column > -1) {

            Map<String, Object> map = data.get(row);
            map.put(getColumnName(column), value);
            fireTableCellUpdated(row, column);
            fireTableDataChanged();
        }
    }

    private void setValueAtNoFire(Object value, int row, int column) {
        if (row > -1 && column > -1) {
            Map<String, Object> map = data.get(row);
            map.put(getColumnName(column), value);
        }
    }

    public void addRow() {
        String[] rowData = {"", "", "", ""};
        addRow(rowData);
    }

    private void addRow(String[] rowData) {
        //chiefScientistCountryList = new JComboBox();
        //chiefScientistOrganisationList = new JComboBox();

        Map<String, Object> map = new HashMap();
        for (int i = 0; i < rowData.length; i++) {
            if (rowData[i] != null) {
                map.put(getColumnName(i), rowData[i]);
            }
        }
        data.add(map);
        fireTableDataChanged();
        int lastRow = this.getRowCount() - 1;
    }

    void removeRow(int selectedRow) {
        data.remove(selectedRow);
        fireTableDataChanged();
    }

    private <C> C getCellEditorComponent(int row, String columnName, Class<C> cls) {
        if (table.getColumnModel().getColumn(findColumn(columnName)).getCellEditor() != null) {
            return (C) table.getColumnModel().getColumn(findColumn(columnName)).getCellEditor().getTableCellEditorComponent(table, null, true, row, findColumn(columnName));
        } else {
            return null;
        }
    }

    public List<Person> getPersons() {
        List<Person> r = new ArrayList(data.size());
        for (Map<String, Object> map : data) {
            if (map.get(ORG) instanceof OrganisationBean) {
                r.add(new Person((String) map.get(NAME), (OrganisationBean) map.get(ORG)));
            } else if (map.get(ORG) instanceof String) {
                r.add(new Person((String) map.get(NAME), (String) map.get(ORG_CODE), (String) map.get(ORG), (String) map.get(COUNTRY)));
            }

        }
        return r;
    }

    public void addPersons(List<Person> persons) {
        for (Person person : persons) {
            String[] rowData = {person.country, person.organisationName, person.organisationCode, person.name};
            addRow(rowData);
        }

    }

    @Override
    public void fireTableCellUpdated(int row, int column) {
        JComboBox organisationList = getCellEditorComponent(row, ORG, JComboBox.class);
        if (organisationList != null) {
            switch (column) {
                case 0:

                    organisationList.removeAllItems();
                    organisationList.addItem(BASE_ACTION);
                    setValueAtNoFire("", row, findColumn(ORG));
                    setValueAtNoFire("", row, findColumn(ORG_CODE));
                    Object o = getValueAt(row, findColumn(COUNTRY));
                    if (o != null && o instanceof CountryBean) {
                        CountryBean selectedCountry = (CountryBean) o;
                        for (IOrganisation org : StaticMetadataSearcher.getInstance().getOrganisations(true)) {
                            if (org.getCountryObject().equals(selectedCountry)) {
                                organisationList.addItem(org);
                            }
                        }
                    }
                    break;

                case 1://organisation changed
                    Object o2 = getValueAt(row, findColumn(ORG));
                    if (o2 != null && o2 instanceof OrganisationBean) {
                        OrganisationBean selectedOrg = (OrganisationBean) o2;
                        setValueAt(selectedOrg.getCode(), row, findColumn(ORG_CODE));
                        //fireTableRowsUpdated(row, findColumn(ORG_CODE));
                    }
                    break;
                default:
            }
            fireTableDataChanged();
        }

    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int i, int j) {
        return data.get(i).get(getColumnName(j));
    }

    public boolean performValidation() {

        for (Map<String, Object> map : data) {
            if (map.get(COUNTRY).equals("") || map.get(ORG).equals(BASE_ACTION) || map.get(ORG_CODE).equals("") || map.get(COUNTRY) == null || map.get(ORG) == null || map.get(ORG_CODE) == null || map.get(NAME) == null) {
                return false;
            }
        }
        return true;
    }

}
