/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents.tablemodel;

import be.naturalsciences.bmdc.ears.entities.EventBean;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.rest.RestClient;
import be.naturalsciences.bmdc.ears.rest.RestClientEvent;
import be.naturalsciences.bmdc.ears.topcomponents.CreateEventTopComponent;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Yvan Stojanov, Thomas Vandenberghe
 * @param <E>
 */
public class EventTableModel extends EntityTableModel<EventBean> {

    private RestClientEvent restClientEvent;

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
    //   public static final String DELETE = "Delete";
    public static final String PROPERTIES = "Properties";

    public static final String[] COLUMN_NAMES = {DATE, TIME, TIMEZONE, TOOL_CATEGORY, TOOL, PROCESS, ACTION, ACTOR, PROGRAM, LABEL,/* DELETE,*/ PROPERTIES};

    public EventTableModel(JTable table, List<EventBean> entities) {
        super(table, entities);
        try {
            restClientEvent = new RestClientEvent();
        } catch (ConnectException ex) {
            Messaging.report("Note that the webservices are offline. The events can't be retrieved, saved or edited.", ex, this.getClass(), true);
        } catch (EarsException ex) {
            Messaging.report(ex.getMessage(), ex, this.getClass(), true);
        }
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
            /* case DELETE:
                return true;*/
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
                        CreateEventTopComponent.getAssociatedAcquisitionInfo(event);
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
                    String valueString = value.toString();
                    Pattern noSeconds = Pattern.compile("^\\d{2}:\\d{2}:?$"); //correct times that have no seconds to 0 seconds. Incorrect seconds remain invalid
                    Matcher matcher = noSeconds.matcher(valueString);
                    if (matcher.matches()) {
                        if (valueString.endsWith(":")) {
                            valueString = valueString + "00";
                        } else {
                            valueString = valueString + ":00";
                        }
                    }
                    try {
                        oT = OffsetDateTime.parse(this.getValueAt(rowIndex, DATE) + "T" + valueString + ".000" + this.getValueAt(rowIndex, TIMEZONE), DateTimeFormatter.ISO_DATE_TIME);
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
    }

    @Override
    public void removeRow(int row) {
        IResponseMessage response = restClientEvent.removeEvent(entities.get(row).getEventId().trim());
        if (!response.isBad()) {
            entities.remove(row);
            fireTableDataChanged();

        } else {
            Messaging.report("Event wasn't deleted from the web services" + response.getSummary(), Message.State.BAD, this.getClass(), true);
        }
    }

    @Override
    public void removeEntity(EventBean event) {
        IResponseMessage response = restClientEvent.removeEvent(event.getEventId().trim());
        if (!response.isBad()) {
            entities.remove(event);
            fireTableDataChanged();

        } else {
            Messaging.report("Event wasn't deleted from the web services" + response.getSummary(), Message.State.BAD, this.getClass(), true);
        }
    }

    @Override
    public void addEntity(EventBean e) {
        e.setEventId(e.buildEventId());
        IResponseMessage response = restClientEvent.postEvent(e);
        if (!response.isBad()) {

            entities.add(e);
            fireTableRowsInserted(entities.size() - 1, entities.size() - 1);
            fireTableDataChanged();
        } else {
            Messaging.report("Event wasn't saved to web services" + response.getSummary(), Message.State.BAD, this.getClass(), true);
        }

    }

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
            case PROPERTIES:
                return "properties";
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
