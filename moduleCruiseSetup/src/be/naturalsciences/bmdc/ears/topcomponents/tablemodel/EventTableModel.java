/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents.tablemodel;

import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.EntityTableModel;
import be.naturalsciences.bmdc.ears.entities.EventBean;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.rest.RestClientEvent;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author yvan
 * @param <E>
 */
public class EventTableModel extends EntityTableModel<EventBean> {

    private List<EventBean> entities = new ArrayList<>();
    private RestClientEvent restClientEvent;

    public List<EventBean> getEntities() {
        return entities;
    }
    JOptionPane optionPane = new JOptionPane();

    public static final String DATE = "Date";
    public static final String TIME = "Time";
    public static final String TIMEZONE = "Timezone";
    public static final String TOOL_CATEGORY = "Tool category";
    public static final String TOOL = "Tool";
    public static final String PROCESS = "Process";
    public static final String ACTION = "Action";
    public static final String ACTOR = "Actor";
    public static final String PROGRAM = "Program";
    public static final String LABEL = "Label";
    public static final String DELETE = "Delete";
    public static final String PROPERTIES = "Properties";

    public static final String[] COLUMN_NAMES = {DATE, TIME, TIMEZONE, TOOL_CATEGORY, TOOL, PROCESS, ACTION, ACTOR, PROGRAM, LABEL, DELETE, PROPERTIES};
    //private final String[] columnNames = {"Date", "Time", "TZ", "tool category", "tool", "process", "action", "actor", "Delete", "Properties"};//7 colonnes

    public EventTableModel(JTable table, List<EventBean> entities) {
        super(table, entities);
        try {
            restClientEvent = new RestClientEvent();
        } catch (ConnectException ex) {
            Messaging.report("Note that the webservices are offline. The events can't be retrieved, saved or edited.", ex, this.getClass(), true);
        } catch (EarsException ex) {
            Messaging.report(ex.getMessage(), ex, this.getClass(), true);
        }
        this.entities.addAll(entities);
    }

    public Set<String> getAllDates() {
        Set<String> result = new TreeSet<>();

        for (int i = 0; i < this.getRowCount(); i++) {
            result.add((String) this.getValueAt(i, this.findColumn(DATE)));
        }

        return result;
    }

    @Override
    public String[] getColumns() {
        return COLUMN_NAMES;
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        if (entities.isEmpty() || this.getValueAt(0, columnIndex) == null) { //avoid error in setAutoCreateRowSorter
            return Object.class;
        }
        return this.getValueAt(0, columnIndex).getClass();

    }

    /**
     * Determines if any particular cell is editable.
     *
     * @return
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        String colName = getColumnName(columnIndex);
        switch (colName) {
            case DATE:
                return true;
            case TIME:
                return true;
            case TIMEZONE:
                return true;
            case TOOL_CATEGORY:
                return false;
            case TOOL:
                return false;
            case PROCESS:
                return false;
            case ACTION:
                return false;
            case ACTOR:
                return true;
            case PROGRAM:
                return false;
            case LABEL:
                return false;
            case DELETE:
                return true;
            case PROPERTIES:
                return true;
            default:
                return false;
        }
    }

    /**
     * Set the value of the specified cell.
     *
     * @param value
     */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Object originalValue = getValueAt(rowIndex, columnIndex);
        EventBean event = getEntities().get(rowIndex);
        String colName = getColumnName(columnIndex);
        switch (colName) {
            case DATE:
                if (value != null && !value.equals(originalValue)) {
                    OffsetDateTime originalDate = OffsetDateTime.parse(originalValue.toString() + "T" + this.getValueAt(rowIndex, TIME) + this.getValueAt(rowIndex, TIMEZONE), DateTimeFormatter.ISO_DATE_TIME);
                    OffsetDateTime oD = null;
                    try {
                        oD = OffsetDateTime.parse(value.toString() + "T" + this.getValueAt(rowIndex, TIME) + this.getValueAt(rowIndex, TIMEZONE), DateTimeFormatter.ISO_DATE_TIME);
                    } catch (java.time.format.DateTimeParseException ex) {
                        Messaging.report("Date format incorrect", ex, this.getClass(), true);
                    }
                    if (oD != null) {
                        event.setTimeStampDt(oD);
                        IResponseMessage response = restClientEvent.modifyEvent(event);
                        if (response.isBad()) {
                            Messaging.report("Event wasn't modified" + response.getSummary(), Message.State.BAD, this.getClass(), true);
                            event.setTimeStampDt(originalDate);
                        }
                    }
                }
                break;
            case TIME:
                if (value != null && !value.equals(originalValue)) {
                    OffsetDateTime originalDate = OffsetDateTime.parse(this.getValueAt(rowIndex, DATE) + "T" + originalValue.toString() + this.getValueAt(rowIndex, TIMEZONE), DateTimeFormatter.ISO_DATE_TIME);
                    OffsetDateTime oT = null;
                    try {
                        oT = OffsetDateTime.parse(this.getValueAt(rowIndex, DATE) + "T" + value.toString() + ".000" + this.getValueAt(rowIndex, TIMEZONE), DateTimeFormatter.ISO_DATE_TIME);
                    } catch (java.time.format.DateTimeParseException ex) {
                        Messaging.report("Date format incorrect", ex, this.getClass(), true);
                    }
                    if (oT != null) {
                        event.setTimeStampDt(oT);
                        IResponseMessage response = restClientEvent.modifyEvent(event);
                        if (response.isBad()) {
                            Messaging.report("Event wasn't modified" + response.getSummary(), Message.State.BAD, this.getClass(), true);
                            event.setTimeStampDt(originalDate);
                        }
                    }

                }
                break;
            case TIMEZONE:
                if (value != null && !value.equals(originalValue)) {
                    OffsetDateTime originalDate = OffsetDateTime.parse(this.getValueAt(rowIndex, 0) + "T" + this.getValueAt(rowIndex, 1) + originalValue.toString(), DateTimeFormatter.ISO_DATE_TIME);
                    OffsetDateTime oZ = null;
                    try {
                        oZ = OffsetDateTime.parse(this.getValueAt(rowIndex, 0) + "T" + this.getValueAt(rowIndex, 1) + value.toString(), DateTimeFormatter.ISO_DATE_TIME);
                    } catch (java.time.format.DateTimeParseException ex) {
                        Messaging.report("Date format incorrect", ex, this.getClass(), true);
                    }
                    if (oZ != null) {
                        event.setTimeStampDt(oZ);
                        IResponseMessage response = restClientEvent.modifyEvent(event);
                        if (response.isBad()) {
                            Messaging.report("Event wasn't modified" + response.getSummary(), Message.State.BAD, this.getClass(), true);
                            event.setTimeStampDt(originalDate);
                        }
                    }
                }
                break;
            case ACTOR:
                if (value != null && !value.equals(originalValue)) {
                    event.setActor((String) value);
                    IResponseMessage response = restClientEvent.modifyEvent(event);
                    if (response.isBad()) {
                        Messaging.report("Event wasn't modified" + response.getSummary(), Message.State.BAD, this.getClass(), true);
                        event.setActor((String) originalValue);
                    }
                }
                break;
        }

        //  set(event, rowIndex);
        //fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void removeRow(int rowsToDelete) {
        restClientEvent.removeEvent(entities.get(rowsToDelete).getEventId().trim());
        entities.remove(rowsToDelete);
        fireTableDataChanged();

    }

    @Override
    public void addEntity(EventBean e) {
        e.setEventId(LocalDateTime.ofInstant(e.getTimeStampDt().toInstant(), e.getTimeStampDt().getOffset()).toLocalTime().toString().replace(".", "").replace(":", "").subSequence(0, 9).toString());
        IResponseMessage response = restClientEvent.postEvent(e);
        if (!response.isBad()) {
            entities.add(e);
            table.scrollRectToVisible(table.getCellRect(entities.size() - 1, 0, true));
        } else {
            Messaging.report("Event wasn't saved to web services" + response.getSummary(), Message.State.BAD, this.getClass(), true);
        }

        fireTableDataChanged();

    }

    /*
                case DATE:
                return event.getTimeStampDt().atZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE);
            case TIME:
                return event.getTimeStampDt().atZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_TIME);
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        EventBean event = entities.get(rowIndex);

        String colName = getColumnName(columnIndex);
        switch (colName) {
            case DATE:
                return event.getTimeStampDt().format(DateTimeFormatter.ISO_LOCAL_DATE);
            case TIME:
                return event.getTimeStampDt().format(StringUtils.DTF_TIME_FORMAT_HOURS_MINS_SECS);
            case TIMEZONE:
                return event.getTimeStampDt().getOffset().toString();
            case TOOL_CATEGORY:
                return event.getToolCategoryName();
            case TOOL:
                return event.getToolNames();
            case PROCESS:
                return event.getProcessName();
            case ACTION:
                return event.getActionName();
            case ACTOR:
                return event.getActor();
            case PROGRAM:
                return event.getProgramProperty();
            case LABEL:
                return event.getLabel();
            case DELETE:
                java.net.URL imageURL = EventTableModel.class.getResource("/images/deleteImg.png");
                Icon deleteIcon = new ImageIcon(imageURL, "Delete event " + event.getId());
                if (imageURL != null) {
                    // ImageIcon icon = new ImageIcon(favicon);
                    return deleteIcon;
                }

                return "";
            case PROPERTIES:
                if (event.hasProperties()) {
                    return event.getProperties();
                    // return StringUtils.concatString(event.getPropertyMap().toString(), ";");
                    //return event.getPropertyMap();
                } else {
                    return "";
                }
            default:
                return null;
        }
    }

    @Override
    public void addRow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getValueAt(EventBean entity, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
