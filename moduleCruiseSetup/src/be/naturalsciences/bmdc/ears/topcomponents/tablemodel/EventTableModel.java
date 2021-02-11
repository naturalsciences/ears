/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents.tablemodel;

import be.naturalsciences.bmdc.ears.entities.Actor;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.SeaAreaBean;
import be.naturalsciences.bmdc.ears.rest.RestClient;
import be.naturalsciences.bmdc.ears.rest.RestClientEvent;
import be.naturalsciences.bmdc.ears.topcomponents.CreateEventTopComponent;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import eu.eurofleets.ears3.dto.EventDTO;
import eu.eurofleets.ears3.dto.PersonDTO;
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
public class EventTableModel extends EntityTableModel<EventDTO> {

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

    public EventTableModel(JTable table, List<EventDTO> entities) {
        super(table, entities);
        try {
            restClientEvent = new RestClientEvent();
        } catch (ConnectException ex) {
            Messaging.report("The webservices are offline. The events can't be retrieved, saved or edited.", ex, this.getClass(), true);
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
        EventDTO event = getEntities().get(rowIndex);
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
                        event.setTimeStamp(oD);
                        IResponseMessage response = restClientEvent.modifyEvent(event);
                       // CreateEventTopComponent.getAssociatedAcquisitionInfo(event);
                        if (response.isBad()) {
                            Messaging.report("Event "+response.getIdentifier()+" wasn't modified because of http error " + response.getMessage(), Message.State.BAD, this.getClass(), true);
                            event.setTimeStamp(originalDate);
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
                        event.setTimeStamp(oT);
                        IResponseMessage response = restClientEvent.modifyEvent(event);
                        if (response.isBad()) {
                            Messaging.report("Event "+response.getIdentifier()+" wasn't modified because of http error " + response.getMessage(), Message.State.BAD, this.getClass(), true);
                            event.setTimeStamp(originalDate);
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
                        event.setTimeStamp(oZ);
                        IResponseMessage response = restClientEvent.modifyEvent(event);
                        if (response.isBad()) {
                            Messaging.report("Event "+response.getIdentifier()+" wasn't modified because of http error " + response.getMessage(), Message.State.BAD, this.getClass(), true);
                            event.setTimeStamp(originalDate);
                        }
                    }
                }
                break;
            case ACTOR:
                if (value != null && !value.equals(originalValue)) {
                    PersonDTO actor = new PersonDTO((Actor)value);
                    event.setActor(actor);
                    IResponseMessage response = restClientEvent.modifyEvent(event);
                    if (response.isBad()) {
                        Messaging.report("Event wasn't modified" + response.getMessage(), Message.State.BAD, this.getClass(), true);
                        PersonDTO originalActor = new PersonDTO((Actor)originalValue);
                        event.setActor(originalActor);
                    }
                }
                break;
        }
    }

    @Override
    public void removeRow(int row) {
        EventDTO event = (EventDTO)entities.get(row);
        removeEntity(event);
    }

    @Override
    public void removeEntity(EventDTO event) {
        IResponseMessage response = restClientEvent.removeEvent(event.getIdentifier());
        if (!response.isBad()) {
            entities.remove(event);
            fireTableDataChanged();
        } else {
            Messaging.report("Event wasn't deleted from the web services" + response.getMessage(), Message.State.BAD, this.getClass(), true);
        }
    }

    @Override
    public void addEntity(EventDTO e) {
        IResponseMessage<EventDTO> response = restClientEvent.postEvent(e);
        if (!response.isBad()) {
            String identifier = response.getIdentifier();
            e.setIdentifier(identifier);
            OffsetDateTime timeStamp = response.getEntity().getTimeStamp();
            e.setTimeStamp(timeStamp);
            entities.add(e);
            fireTableRowsInserted(entities.size() - 1, entities.size() - 1);
            fireTableDataChanged();
        } else {
            Messaging.report("Event wasn't saved to web services: " + response.getMessage(), Message.State.BAD, this.getClass(), true);
        }

    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        EventDTO event = (EventDTO)entities.get(rowIndex);

        String colName = getColumnName(columnIndex);
        switch (colName) {
            case DATE:
                return event.getTimeStamp().format(DateTimeFormatter.ISO_LOCAL_DATE);
            case TIME:
                return event.getTimeStamp().format(StringUtils.DTF_TIME_FORMAT_HOURS_MINS_SECS);
            case TIMEZONE:
                return event.getTimeStamp().getOffset().toString();
            case TOOL_CATEGORY:
                return event.getToolCategory().name;
            case TOOL:
                return event.getTool().fullName();
            case PROCESS:
                return event.getProcess().name;
            case ACTION:
                return event.getAction().name;
            case ACTOR:
                return event.getActor().getLastNameFirstName();
            case PROGRAM:
                return event.getProgram();
            case LABEL:
                return event.getLabel();
            case PROPERTIES:
                return "properties";
            default:
                return null;
        }
    }
    
        public EventDTO getEntityWithName(String name) {
        for (EventDTO e : entities) {
                if(e.getIdentifier().equals(name)){return e;}
        }
            return null;
    }

    @Override
    public void addRow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getValueAt(EventDTO entity, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
