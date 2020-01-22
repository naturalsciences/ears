/**
 *
 * @author Thomas Vandenberghe
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents.tablemodel;

import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.EntityTableModel;
import be.naturalsciences.bmdc.ears.base.StaticMetadataSearcher;
import be.naturalsciences.bmdc.ears.entities.CountryBean;
import be.naturalsciences.bmdc.ears.entities.IOrganisation;
import be.naturalsciences.bmdc.ears.entities.IProject;
import be.naturalsciences.bmdc.ears.entities.OrganisationBean;
import be.naturalsciences.bmdc.ears.entities.ProjectBean;
import javax.swing.JComboBox;
import javax.swing.JTable;

/**
 *
 * @author Thomas Vandenberghe
 */
public class ProjectTableModel extends EntityTableModel<ProjectBean> {

    private JTable table;

    //  "CountryBean", "Organisation", "Name", "EDMERP Code"
    public final static String COUNTRY = "Country";
    public final static String ORG = "Organisation";
    public final static String ORG_CODE = "Organisation code";
    public final static String NAME = "Name";
    public final static String PROJ_CODE = "EDMERP Code";

    protected static final String[] COLUMN_NAMES = {COUNTRY, ORG, NAME, PROJ_CODE};

    public ProjectTableModel() {
        super(null, null);
    }

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
                return true;
            case 3:
                return false;
            default:
                return false;
        }
    }

    private <C> C getCellEditorComponent(int row, String columnName, Class<C> cls) {
        if (table.getColumnModel().getColumn(findColumn(columnName)).getCellEditor() != null) {
            return (C) table.getColumnModel().getColumn(findColumn(columnName)).getCellEditor().getTableCellEditorComponent(table, null, true, row, findColumn(columnName));
        } else {
            return null;
        }
    }

    @Override
    public void fireTableCellUpdated(int updatedRow, int updatedColumn) {
        JComboBox organisationList = getCellEditorComponent(updatedRow, ORG, JComboBox.class);
        JComboBox projectNameList = getCellEditorComponent(updatedRow, NAME, JComboBox.class);
        if (organisationList != null && projectNameList != null) {
            switch (getColumnName(updatedColumn)) {

                case COUNTRY:
                    organisationList.removeAllItems();
                    organisationList.addItem("Choose organisation");
                    Object c = getValueAt(updatedRow, findColumn(COUNTRY));

                    if (c != null && c instanceof CountryBean) {
                        CountryBean selectedCountry = (CountryBean) c;
                        //Projects newProject = new ProjectBean(null, null, null, null, null, new Organisation(), selectedCountry.getName(), selectedCountry);
                        //setValueAtNoFire(newProject, row, findColumn(NAME));
                        for (IOrganisation org : StaticMetadataSearcher.getInstance().getOrganisations(true)) {
                            if (org.getCountryObject().equals(selectedCountry)) {
                                organisationList.addItem(org);
                            }
                        }
                    } else {
                        //setValueAtNoFire(new Organisation(), row, findColumn(ORG));
                        setValueAtNoFire(new ProjectBean(), updatedRow, findColumn(NAME));
                    }
                    break;
                case ORG:
                    projectNameList.removeAllItems();
                    projectNameList.addItem("Choose project");
                    Object o = getValueAt(updatedRow, findColumn(ORG));

                    if (o != null && o instanceof OrganisationBean) {
                        OrganisationBean selectedOrganisation = (OrganisationBean) o;
                        //Projects newProject = new ProjectBean(null, null, null, null, null, selectedOrganisation, selectedOrganisation.getCountry(), selectedOrganisation.getCountryObject());
                        //setValueAtNoFire(newProject, row, findColumn(NAME));
                        for (IProject project : StaticMetadataSearcher.getInstance().getProjects(true)) {
                            if (project.getOrganisation() != null && project.getOrganisation().equals(selectedOrganisation.getName()) /*project.getOrganisationObject() != null && project.getOrganisationObject().equals(selectedOrganisation)*/) {
                                projectNameList.addItem(project);
                            }
                        }
                    }
                    break;
                case NAME:
                    Object p = getValueAt(updatedRow, findColumn(NAME));
                    //Object o2 = organisationList.getSelectedItem();
                    if (p != null && p instanceof ProjectBean) {
                        ProjectBean selectedProj = (ProjectBean) p;
                        setValueAtBase(selectedProj, updatedRow, updatedColumn);
                        //setValueAt(selectedProj.getId(), row, findColumn(PROJ_CODE));
                        //fireTableRowsUpdated(row, findColumn(ORG_CODE));
                    }
                    break;
                case PROJ_CODE:

                default:
            }
        }
        //fireTableDataChanged();
    }

    @Override
    public String[] getColumns() {
        return COLUMN_NAMES;
    }

    @Override
    public Object getValueAt(ProjectBean entity, int columnIndex) {

        switch (getColumnName(columnIndex)) {
            case COUNTRY:
                return entity.getCountryObject();
            case ORG:
                return entity.getOrganisationObject();
            case NAME:
                return entity;
            case PROJ_CODE:
                return entity.getCode();
            default:
                return null;
        }
    }

    @Override
    public void addRow() {
        entities.add(new ProjectBean());
        fireTableDataChanged();
    }

    private void setValueAtNoFire(Object value, int row, int column) {
        setValueAtBase(value, row, column);
    }

    private void setValueAtBase(Object value, int row, int column) {
        ProjectBean proj = getEntityAt(row);

        if (value != null) {
            switch (getColumnName(column)) {
                case COUNTRY:
                    if (value instanceof String) {
                        proj.setCountryObject(new CountryBean());
                    } else {
                        CountryBean country = (CountryBean) value;
                        proj.setCountryObject(country);
                        proj.setCountry(country.getName());
                        proj.setOrganisationObject(null);
                        proj.setOrganisation(null);
                        proj.setCode(null);
                        proj.setAcronym(null);
                        proj.setFullName(null);
                        proj.setId(null);

                    }
                    break;
                case ORG:
                    if (value instanceof String) {
                        proj.setOrganisationObject(null);
                    } else {
                        OrganisationBean organisation = (OrganisationBean) value;
                        proj.setCountryObject(organisation.getCountryObject());
                        proj.setCountry(organisation.getCountryObject().getName());
                        proj.setOrganisationObject(organisation);
                        proj.setOrganisation(organisation.getName());
                        proj.setCode(null);
                        proj.setAcronym(null);
                        proj.setFullName(null);
                        proj.setId(null);
                    }
                    break;
                case NAME:
                    if (value instanceof String) {
                        proj.setFullName(null);
                        proj.setId(null);
                        proj.setCode(null);
                    } else {
                        ProjectBean selectedProj = (ProjectBean) value;
                        proj.setCode(selectedProj.getCode());
                        proj.setAcronym(selectedProj.getAcronym());
                        proj.setFullName(selectedProj.getFullName());
                        proj.setId(selectedProj.getId());
                    }
                    break;
                case PROJ_CODE:
                    proj.setId((String) value);
                    proj.setCode((String) value);
                    break;
                default:
            }
        }
        /*if (value instanceof ProjectBean) {
         ProjectBean project2 = (ProjectBean) value;
         setEntityAt(project2, row);;}*/
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        setValueAtBase(value, row, column);
        //fireTableCellUpdated(row, column);
        fireTableDataChanged();
    }

}
