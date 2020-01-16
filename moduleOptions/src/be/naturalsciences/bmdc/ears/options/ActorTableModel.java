/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.options;

import be.naturalsciences.bmdc.ears.properties.Configs;
import be.naturalsciences.bmdc.ears.entities.Actor;
import be.naturalsciences.bmdc.ears.netbeans.services.GlobalActionContextProxy;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author thomas
 */
public class ActorTableModel extends AbstractTableModel implements TableModelListener {

    public static final int ERROR = -42;

    private List<Actor> datalist = new ArrayList();

    private static final String INDEX = "Index";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";

    private static final String[] COLUMN_NAMES = {INDEX, FIRSTNAME, LASTNAME};

    public Map<String, String> errors;

    public ActorTableModel() {
        errors = new HashMap<>();
        Set<Actor> actors = null;
        try {
            actors = Configs.getAllActors();
        } catch (IOException ex) {
            Messaging.report("There was a problem with the actors file.", ex, ActorTableModel.class, false);
        }
        addActorList(actors);
        this.addTableModelListener(this);
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public int getRowCount() {
        return datalist.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    public int getMaxId() {
        int result = 0;
        int id;
        for (Actor a : datalist) {
            try {
                id = Integer.parseInt(a.getId());
            } catch (NumberFormatException ex) {
                break;
            }
            if (id > result) {
                result = id;
            }
        }
        return result;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch (getColumnName(column)) {
            case FIRSTNAME:
                return true;
            case LASTNAME:
                return true;
            case INDEX:
                return false;
            default:
                return false;
        }
    }

    private void clearErrors(Actor a, String type) {
        errors.remove(a.getId() + type);
        //errors.remove(a.getId() + LASTNAME);
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        Actor actor = datalist.get(row);
        String string = (String) value;
        if (string != null) {
            switch (getColumnName(column)) {
                case FIRSTNAME:
                    if (!string.isEmpty()) {
                        if (!string.equals(actor.getFirstNameOfActor())) {
                            actor.setFirstNameOfActor(string);
                            if (!actor.getLastNameOfActor().isEmpty()) {
                                clearErrors(actor, FIRSTNAME);
                                clearErrors(actor, LASTNAME);
                                fireTableChanged(new TableModelEvent(this, row, row, column));
                            } else {
                                errors.put(actor.getId() + LASTNAME, actor.toString() + ": last name can't be empty.");
                                fireTableChanged(new TableModelEvent(this, ERROR));
                                //clearErrors(actor);
                            }
                        }
                    } else {
                        errors.put(actor.getId() + FIRSTNAME, actor.toString() + ": first name can't be empty.");
                        fireTableChanged(new TableModelEvent(this, ERROR));
                        //clearErrors(actor);
                    }
                    break;
                case LASTNAME:
                    if (!string.isEmpty()) {
                        if (!string.equals(actor.getLastNameOfActor())) {
                            actor.setLastNameOfActor(string);
                            if (!actor.getFirstNameOfActor().isEmpty()) {
                                clearErrors(actor, FIRSTNAME);
                                clearErrors(actor, LASTNAME);
                                fireTableChanged(new TableModelEvent(this, row, row, column));
                            } else {
                                errors.put(actor.getId() + FIRSTNAME, actor.toString() + ": first name can't be empty.");
                                fireTableChanged(new TableModelEvent(this, ERROR));
                                //clearErrors(actor);
                            }
                        }
                    } else {
                        errors.put(actor.getId() + LASTNAME, actor.toString() + ": last name can't be empty.");
                        fireTableChanged(new TableModelEvent(this, ERROR));
                        //clearErrors(actor);
                    }
                    break;
                case INDEX:
                /*if (!string.isEmpty()) {
                     actor.setId(string);
                     fireTableChanged(new TableModelEvent(this, row, row, column));
                     } else {
                     errors.put(actor.toString() + ID, actor.toString() + ": id can't be empty.");
                     }
                     break;*/
                default:
            }

        }
    }

    public void addActor(Actor a) {
        datalist.add(a);
        fireTableChanged(new TableModelEvent(this, getRowCount(), getRowCount(), TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    public final void addActorList(Collection<Actor> l) {
        for (Actor actor : l) {
            addActor(actor);
        }
    }

    public void removeRow(int row) {
        fireTableChanged(new TableModelEvent(this, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
        datalist.remove(row);
    }

    public Actor getActorAt(int row) {
        if (datalist.isEmpty()) {
            return null;
        } else {
            return (Actor) datalist.get(row);
        }
    }

    public void addEmptyRow() {
        if (errors.isEmpty() || datalist.isEmpty()) {
            addActor(new Actor(String.valueOf(getMaxId() + 1), "", ""));
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        Actor actor = datalist.get(row);
        switch ((getColumnName(column))) {
            case FIRSTNAME:
                return actor.getFirstNameOfActor();
            case LASTNAME:
                return actor.getLastNameOfActor();
            case INDEX:
                return actor.getId();
            default:
                return null;
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int firstRow = e.getFirstRow();
        int lastRow = e.getLastRow();
        int index = e.getColumn();
        Actor a = null;
        switch (e.getType()) {
            case TableModelEvent.INSERT:
                /*Actor a = getActorAt(firstRow);
                     GlobalActionContextProxy.getInstance().add(a);
                     Configs.persistActor(a);*/
                break;
            case TableModelEvent.UPDATE:
                if (index > -1 && firstRow > -1) {
                    a = getActorAt(firstRow);
                    GlobalActionContextProxy.getInstance().replace(a);
                    Configs.persistActor(a);
                }
                break;
            case TableModelEvent.DELETE:
                a = getActorAt(firstRow);
                GlobalActionContextProxy.getInstance().remove(a);
                Configs.removeActor(a);
                break;
        }
    }

    public void fireLookupListeners() {
        Actor a = getActorAt(0);
        if (a != null) {
            GlobalActionContextProxy.getInstance().replace(a);
        }
    }

}
