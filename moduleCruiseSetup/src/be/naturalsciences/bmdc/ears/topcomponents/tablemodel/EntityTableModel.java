/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents.tablemodel;

import be.naturalsciences.bmdc.ears.entities.EARSConcept;
import static be.naturalsciences.bmdc.ears.topcomponents.tablemodel.ProjectTableModel.COLUMN_NAMES;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Yvan Stojanov
 * @param <E>
 */
public abstract class EntityTableModel<E extends EARSConcept> extends AbstractTableModel {

    protected JTable table;
    protected TableRowSorter<EntityTableModel<E>> rowSorter;
    protected List<E> entities;

    //protected void initRenders() {}
    public EntityTableModel(JTable table, List<E> entities) {

        if (entities != null) {
            this.entities = entities;
        } else {
            this.entities = new ArrayList<>();
        }
        this.table = table;
        if (this.table != null) {
            this.table.setModel(this);
        }

        rowSorter = new TableRowSorter<>(this);
        //initRenders();
    }

    public E remove(int index) {
        return entities.remove(index);
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public TableRowSorter<EntityTableModel<E>> getRowSorter() {
        return rowSorter;
    }

    public List<E> getEntities() {
        return this.entities;
    }

    public Set<E> getEntitiesSet() {
        //this.entities.removeAll(Collections.singleton(null));
        Iterator<E> iter = getEntities().iterator();
        while (iter.hasNext()) {
            E e = iter.next();
            if (e == null || !e.isLegal()) {
                iter.remove();
            }
        }
        return new TreeSet<>(getEntities());//ordered and unique
    }

    abstract public String[] getColumns();

    @Override
    public int getRowCount() {
        return (getEntities() != null) ? getEntities().size() : 0;
    }

    @Override
    public int getColumnCount() {
        return getColumns().length;
    }

    @Override
    public String getColumnName(int col) {
        return getColumns()[col];
    }

    @Override
    public int findColumn(String name) {
        for (int i = 0; i < getColumns().length; i++) {
            if (getColumns()[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public static int findColumnStatic(String name) {
        for (int i = 0; i < COLUMN_NAMES.length; i++) {
            if (COLUMN_NAMES[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public abstract void addRow();

    public abstract E getEntityWithName(String name);

    public void addEntity(E entity) {
        getEntities().add(entity);
        fireTableDataChanged();
    }

    public void addEntities(Collection<E> entities) {
        for (E e : entities) {
            addEntity(e);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (!getEntities().isEmpty()) {
            return getValueAt(getEntities().get(rowIndex), columnIndex);
        } else {
            return null;
        }

    }

    abstract public Object getValueAt(E entity, int columnIndex);

    public Object getValueAt(int rowIndex, String columnName) {
        return getValueAt(rowIndex, findColumn(columnName));
    }

    public E getEntityAt(int row) {
        return getEntities().get(row);
    }

    public void setEntityAt(E entity, int row) {
        getEntities().remove(row);
        getEntities().add(row, entity);
        fireTableDataChanged();
    }

    public void removeRow(int selectedRow) {
        getEntities().remove(selectedRow);
        fireTableDataChanged();
    }

    public void removeEntity(E entity) {
        getEntities().remove(entity);
        fireTableDataChanged();
    }

    public void removeAllEntities() {
        getEntities().retainAll(new ArrayList<>());
        fireTableDataChanged();
    }

    public void refresh(Collection<E> entities) {
        this.removeAllEntities();
        this.addEntities(entities);
        this.fireTableDataChanged();
    }

    /**
     * *
     *
     * @return
     */
    public E getSelectedEntity() {
        if (table.getSelectedRow() != -1) {
            int selectedRow = table.getSelectedRow();
            int selectedRowModel = table.convertRowIndexToModel(selectedRow);
            return getEntities().get(selectedRowModel);
        } else {
            return null;
        }
    }

    public List<E> getSelectedEntities() {

        if (table.getSelectedRowCount() > 0) {

            List<E> selecteds = new ArrayList<E>();

            int[] selectedRows = table.getSelectedRows();

            for (int selectedRow : selectedRows) {
                int selectedRowModel = table.convertRowIndexToModel(selectedRow);
                selecteds.add(getEntities().get(selectedRowModel));
            }

            return selecteds;
        } else {
            return new ArrayList<E>();
        }
    }

}
