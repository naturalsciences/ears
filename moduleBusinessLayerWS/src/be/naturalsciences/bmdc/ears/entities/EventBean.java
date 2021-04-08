package be.naturalsciences.bmdc.ears.entities;

import be.naturalsciences.bmdc.ears.comparator.EventPropertyComparator;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.IAction;
import be.naturalsciences.bmdc.ontology.entities.IProcess;
import be.naturalsciences.bmdc.ontology.entities.IProperty;
import be.naturalsciences.bmdc.ontology.entities.IToolCategory;
import be.naturalsciences.bmdc.ontology.writer.JSONReader;
import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import be.naturalsciences.bmdc.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gnu.trove.set.hash.THashSet;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.collections4.map.SingletonMap;
import org.openide.util.Exceptions;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "event")
public class EventBean implements Serializable, EARSConcept {

    public static final String REGEX_KEYVAL_DELIM = "\\|";
    public static final String REGEX_PROP_DELIM = "\\$";
    public static final String KEYVAL_DELIM = "|";
    public static final String PROP_DELIM = "$";

    public static final String PROP_URI = "";

    public static final String TOOL_DELIM = ",";

    public static final Map<Enum, String> PROPERTY_URLS;
    private ToolBean tool;

    public enum Prop {
        DEPTH_M, HASDATA, LABEL, LENGTH_M, PARAMETER, PROGRAM, RECIPIENT_VOLUME_L, RELATED_EVENT, SAMPLEID, SAMPLED_VOLUME_L, SENSORHEIGHT_M, SUBJECT, SUBSAMPLEID, SWATH_MODE, VOLUMECENTRIFUGE_READING, VOLUMECENTRIFUGED_L, VOLUMEFILTERED_L, VOLUMEFILTERED_READING, WIDTH_SWATH
    };

    static {
        PROPERTY_URLS = new HashMap<Enum, String>();
        PROPERTY_URLS.put(Prop.DEPTH_M, "http://ontologies.ef-ears.eu/ears2/1#pry_19");
        PROPERTY_URLS.put(Prop.HASDATA, "http://ontologies.ef-ears.eu/ears2/1#pry_15");
        PROPERTY_URLS.put(Prop.LABEL, "http://ontologies.ef-ears.eu/ears2/1#pry_4");
        PROPERTY_URLS.put(Prop.LENGTH_M, "http://ontologies.ef-ears.eu/ears2/1#pry_18");
        PROPERTY_URLS.put(Prop.PARAMETER, "http://ontologies.ef-ears.eu/ears2/1#pry_13");
        PROPERTY_URLS.put(Prop.PROGRAM, "http://ontologies.ef-ears.eu/ears2/1#pry_22");
        PROPERTY_URLS.put(Prop.RECIPIENT_VOLUME_L, "http://ontologies.ef-ears.eu/ears2/1#pry_20");
        PROPERTY_URLS.put(Prop.RELATED_EVENT, "http://ontologies.ef-ears.eu/ears2/1#pry_14");
        PROPERTY_URLS.put(Prop.SAMPLEID, "http://ontologies.ef-ears.eu/ears2/1#pry_16");
        PROPERTY_URLS.put(Prop.SAMPLED_VOLUME_L, "http://ontologies.ef-ears.eu/ears2/1#pry_21");
        PROPERTY_URLS.put(Prop.SENSORHEIGHT_M, "http://ontologies.ef-ears.eu/ears2/1#pry_1");
        PROPERTY_URLS.put(Prop.SUBJECT, "http://ontologies.ef-ears.eu/ears2/1#pry_12");
        PROPERTY_URLS.put(Prop.SUBSAMPLEID, "http://ontologies.ef-ears.eu/ears2/1#pry_17");
        PROPERTY_URLS.put(Prop.SWATH_MODE, "http://ontologies.ef-ears.eu/ears2/1#pry_6");
        PROPERTY_URLS.put(Prop.VOLUMECENTRIFUGE_READING, "http://ontologies.ef-ears.eu/ears2/1#pry_2");
        PROPERTY_URLS.put(Prop.VOLUMECENTRIFUGED_L, "http://ontologies.ef-ears.eu/ears2/1#pry_3");
        PROPERTY_URLS.put(Prop.VOLUMEFILTERED_L, "http://ontologies.ef-ears.eu/ears2/1#pry_8");
        PROPERTY_URLS.put(Prop.VOLUMEFILTERED_READING, "http://ontologies.ef-ears.eu/ears2/1#pry_9");
        PROPERTY_URLS.put(Prop.WIDTH_SWATH, "http://ontologies.ef-ears.eu/ears2/1#pry_5");

    }

    /*------------------------------*/
    //URIs
    /**
     * The Category (=Process) URI String.
     */
    private SingletonMap<String, String> processUri; //process

    /**
     * The Subject (=Tool Category) URI String.
     */
    private SingletonMap<String, String> toolCategoryUri; //toolCategory

    /**
     * The Action URI String.
     */
    private SingletonMap<String, String> actionUri; //action

    private SingletonMap<String, String> platformUri; //platform
    /**
     * A map of URI Strings of the properties. A property is a key, value pair.
     * As the same property can occur N times, this is implemented as a
     * Map<String, List<String>>
     */
    //private Map<SingletonMap<String, String>, Set<String>> propertyMap = new THashMap<>(); //property
    private Set<PropertyBean> properties = new TreeSet<>(new EventPropertyComparator());
    ;
 
    /**
     * *
     * A map of Tool URI and name Strings. An event is constituted of 1 Tool or
     * 1 nested tool and its parent.
     */
    //private Map<String, String> toolUris;
    /*------------------------------*/
 /*base stuff*/
    private String id;

    private OffsetDateTime timeStampDt;

    private Double latDec;

    private Double lonDec;

    private Actor actor;

    private VesselBean platform;

    /**
     * *
     * An ordered set of tools with the parent tools first, followed by the
     * nested tools.
     */
    //  private Set<String> toolSet; //tool
    private Set<EventBean> relatedEvents;

    private ProgramBean program;

    public static int counter;

    /*------------------------------*/
 /*entities from the ontology*/
 /*private IToolCategory toolCategory; //toolCategory

    private Set<ITool> tools; //tool

    private IProcess process; //process

    private IAction action; //action

    private Set<IProperty> properties; //property*/
 /*------------------------------*/
    private static final long serialVersionUID = 1L;

    private String eventId;

    private String eventDefinitionId;

    public EventBean() {
        super();
    }

    /**
     * *
     * Constructor for an event. Do not use when creating event from
     * webservices, only for event creation module. Implementation based on URI.
     *
     * @param eventDefinitionId
     * @param program Must be provided
     * @param cruise Must be provided
     * @param toolCategory Must be provided
     * @param tools
     * @param process Must be provided
     * @param action Must be provided
     * @param properties Optional. Only property keys, not their values. If left
     * empty, property key and a value can be provided later with
     * attachProperty()
     * @param actor Optional
     */
    public EventBean(String eventDefinitionId, VesselBean vessel, ProgramBean program, IToolCategory toolCategory, ToolBean tool, IProcess process, IAction action, Set<IProperty> properties, Actor actor) throws IllegalArgumentException {
        super();
        if (toolCategory == null) {
            throw new IllegalArgumentException("Null tool category provided");
        } else if (tool == null) {
            throw new IllegalArgumentException("Null tool provided");
        } else if (process == null) {
            throw new IllegalArgumentException("Null process provided");
        } else if (action == null) {
            throw new IllegalArgumentException("Null action provided");
        } else if (program == null) {
            throw new IllegalArgumentException("No program provided.");
        }

        this.actor = actor;
        this.tool = tool;
        this.processUri = new SingletonMap(AsConcept.getConceptUriString(process), AsConcept.getConceptName(process));//Collections.singletonMap(AsConcept.getConceptUriString(process), AsConcept.getConceptName(process));
        this.toolCategoryUri = new SingletonMap(AsConcept.getConceptUriString(toolCategory), AsConcept.getConceptName(toolCategory));//Collections.singletonMap(AsConcept.getConceptUriString(toolCategory), AsConcept.getConceptName(toolCategory));
        this.actionUri = new SingletonMap(AsConcept.getConceptUriString(action), AsConcept.getConceptName(action));//Collections.singletonMap(AsConcept.getConceptUriString(action), AsConcept.getConceptName(action));

        PropertyBean programProperty = new PropertyBean(PROPERTY_URLS.get(Prop.PROGRAM), "program", true, true);
        programProperty.setValue(program.getProgramId());
        programProperty.setValueClass("Program");
        this.properties.add(programProperty);

        if (properties != null) {
            for (IProperty earsProperty : properties) {
                PropertyBean prop = new PropertyBean(AsConcept.getConceptUri(earsProperty).toASCIIString(), AsConcept.getConceptName(earsProperty), earsProperty.isMandatory(), earsProperty.isMultiple());
                prop.setIsMandatory(earsProperty.isMandatory());
                prop.setIsMultiple(earsProperty.isMultiple());
                prop.setValueClass(earsProperty.getValueClass());

                this.properties.add(prop);
            }
        }

        this.timeStampDt = OffsetDateTime.now();
        this.platform = vessel;
        this.program = program;
        //this.eventId = buildEventId(); //the server takes care of creating the eventId
        this.eventDefinitionId = eventDefinitionId;
        counter++;
    }

    public String buildEventId() {
        return UUID.randomUUID().toString();
    }

    public String getEventDefinitionId() {
        return eventDefinitionId;
    }

    public void setEventDefinitionId(String eventDefinitionId) {
        this.eventDefinitionId = eventDefinitionId;
    }

    @XmlAttribute(name = "eventId")
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * *
     * Return the string representation of the timeStamp. This String datetime
     * must always be formatted in UTC as database stores it in UTC.
     *
     * @return
     */
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "timeStamp")
    public String getTimeStamp() {
        if (timeStampDt != null) {
            LocalDateTime toLocalDateTime = timeStampDt.atZoneSameInstant(ZoneId.of("Z")).toLocalDateTime();
            return toLocalDateTime.format(StringUtils.DTF_ISO_DATETIME);
            // return timeStampDt.toInstant().toString();
        } else {
            return null;
        }
    }

    /**
     * *
     * Set the string representation of the timeStamp. The given datetime String
     * must be in UTC.
     *
     * @return
     */
    public void setTimeStamp(String timeStamp) {
        if (timeStamp != null) {
            if (!timeStamp.endsWith("Z")) {
                timeStamp = timeStamp + "Z";
            }
            //this.timeStampDt = OffsetDateTime.ofInstant(Instant.parse(timeStamp), ZoneId.of("UTC"));
            this.timeStampDt = OffsetDateTime.parse(timeStamp).withOffsetSameInstant(ZoneOffset.ofHours(0));
            int a = 5;
        } else {
            this.timeStampDt = null;
        }
    }

    /**
     * *
     * Return the actual datetime of the event. This OffsetDateTime datetime
     * always has an offset showing the difference to UTC.
     *
     * @return
     */
    public OffsetDateTime getTimeStampDt() {
        return timeStampDt;
    }

    /**
     * *
     * Set the actual datetime of the event. The given OffsetDateTime datetime
     * always has an offset showing the difference to UTC.
     *
     * @return
     */
    public void setTimeStampDt(OffsetDateTime timeStampDt) {
        this.timeStampDt = timeStampDt;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "actor")
    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "platform")
    public VesselBean getPlatform() {
        return this.platform;
    }

    public void setPlatform(VesselBean platform) {
        this.platform = platform;
    }

    /*public String getPlatformJson() {
        if (platformUri != null && platformUri.keySet() != null) {
            return JsonUtils.serializeConcept(platformUri);
        } else {
            return null;
        }
    }

    public void setPlatformJson(String platformJson) {
        this.platformUri = JsonUtils.deserializeConcept(platformJson);
    }

    public String getPlatformUri() {
        if (platformUri != null && toolCategoryUri.keySet() != null) {
            return platformUri.firstKey();
        } else {
            return null;
        }
    }

    public String getPlatform() {
        if (toolCategoryUri == null) {
            return null;
        }
        if (!hasPlatform()) {//there is only one map element.
            return (String) toolCategoryUri.keySet().toArray()[0]; //if there is no name return the key 
        } else {
            return (String) toolCategoryUri.values().toArray()[0];
        }
    }

    public boolean hasPlatform() {
        if (toolCategoryUri != null) {
            return toolCategoryUri.values().toArray()[0] != null && !toolCategoryUri.values().toArray()[0].equals("");
        }
        return false;
    }*/
    /**
     * *
     * Return a JSON string representation of the properties. Removes properties
     * with an empty or null value.
     *
     * @return
     */
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "property")
    public String getProperty() {
        Set<PropertyBean> propResults = new TreeSet<>(new EventPropertyComparator());
        for (PropertyBean property : getProperties()) {
            if (property.getValue() != null && !property.getValue().equals("")) {
                /*  if ("label".equals(property.name)) {
                       System.out.println("YS4" + property.value );
                    setLabel(property.value);
                }
                 */
                propResults.add(property);
            }

        }
        return serializeProperties(propResults);
    }

    public void setProperty(String propertyName) {
        this.properties.addAll(deserializeProperties(propertyName));
    }

    @XmlTransient
    public Set<PropertyBean> getProperties() {
        return properties;
    }

    public void setProperties(Set<PropertyBean> properties) {
        this.properties = properties;
    }

    public String getLabel() {
        if (hasProperties()) {
            Set<String> values = getPropertyValues(Prop.LABEL);
            return values.toArray(new String[1])[0];
        } else {
            return "";

        }
    }

    public String getProgramProperty() {
        Set<String> values = getPropertyValues(Prop.PROGRAM);
        return StringUtils.join(values, ",");
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "tool")
    public ToolBean getTool() {
        return this.tool;
    }

    public void setTool(ToolBean tool) {
        this.tool = tool;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "toolCategory")
    public String getToolCategoryJson() {
        if (toolCategoryUri != null && toolCategoryUri.keySet() != null) {
            return JsonUtils.serializeConcept(toolCategoryUri);
        } else {
            return null;
        }
    }

    public void setToolCategoryJson(String toolcategoryJson) {
        this.toolCategoryUri = JsonUtils.deserializeConcept(toolcategoryJson);
    }

    public String getToolCategoryUri() {
        if (toolCategoryUri != null && toolCategoryUri.keySet() != null) {
            return toolCategoryUri.firstKey();
        } else {
            return null;
        }
    }

    public String getToolCategoryName() {
        if (toolCategoryUri == null) {
            return null;
        }
        if (!hasToolCategoryName()) {//there is only one map element.
            return (String) toolCategoryUri.keySet().toArray()[0]; //if there is no name return the key 
        } else {
            return (String) toolCategoryUri.values().toArray()[0];
        }
    }

    public boolean hasToolCategoryName() {
        if (toolCategoryUri != null) {
            return toolCategoryUri.values().toArray()[0] != null && !toolCategoryUri.values().toArray()[0].equals("");
        }
        return false;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "process")
    public String getProcessJson() {
        if (processUri != null && processUri.keySet() != null) {
            return JsonUtils.serializeConcept(processUri);
        } else {
            return null;
        }
    }

    public void setProcessJson(String processJson) {
        this.processUri = JsonUtils.deserializeConcept(processJson);
    }

    public String getProcessUri() {
        if (processUri != null && processUri.keySet() != null) {
            return processUri.firstKey();
        } else {
            return null;
        }
    }

    public boolean hasProcessName() {
        if (processUri != null) {
            return processUri.values().toArray()[0] != null && !processUri.values().toArray()[0].equals("");
        }
        return false;
    }

    public String getProcessName() {
        if (processUri == null) {
            return null;
        }
        if (!hasProcessName()) {//there is only one map element.
            return (String) processUri.keySet().toArray()[0]; //if there is no name return the key 
        } else {
            return (String) processUri.values().toArray()[0];
        }
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "action")
    public String getActionJson() {
        if (actionUri != null && actionUri.keySet() != null) {
            return JsonUtils.serializeConcept(actionUri);
        } else {
            return null;
        }
    }

    public void setActionJson(String actionJson) {
        this.actionUri = JsonUtils.deserializeConcept(actionJson);
    }

    public String getActionUri() {
        if (actionUri != null && actionUri.keySet() != null) {
            return actionUri.firstKey();
        } else {
            return null;
        }
    }

    public boolean hasActionName() {
        if (actionUri != null) {
            return actionUri.values().toArray()[0] != null && !actionUri.values().toArray()[0].equals("");
        }
        return false;
    }

    public String getActionName() {
        if (actionUri == null) {
            return null;
        }
        if (!hasActionName()) {//there is only one map element.
            return (String) actionUri.keySet().toArray()[0]; //if there is no name return the key 
        } else {
            return (String) actionUri.values().toArray()[0];
        }
    }

    public Double getLatDec() {
        return latDec;
    }

    public void setLatDec(Double latDec) {
        this.latDec = latDec;
    }

    public Double getLonDec() {
        return lonDec;
    }

    public void setLonDec(Double lonDec) {
        this.lonDec = lonDec;
    }

    public Set<EventBean> getRelatedEvents() {
        return relatedEvents;
    }

    public ProgramBean getProgram() {
        return program;
    }

    public void setProgram(ProgramBean program) {
        this.program = program;
    }

    /**
     * *
     * Attach a related event to this event and vice versa. Related events can
     * occur before or after this event. Related events are stored as a
     * property, as there is no other way to persist them via the webservices.
     *
     * @param relatedEvent
     */
    public void attachRelatedEvent(EventBean relatedEvent) {
        /*this.relatedEvents.add(relatedEvent);
         relatedEvent.relatedEvents.add(this);

         this.attachProperty(Prop.RELATED_EVENT, relatedEvent.getEventId());
         relatedEvent.attachProperty(Prop.RELATED_EVENT, this.getEventId());*/
        throw new UnsupportedOperationException("Method not implemented yet.");
    }

    /**
     * *
     * Attach a property to the event by name and the value (url functions as
     * the key), as long as it has not yet been attached (otherwise already
     * completed values would be overwritten).
     *
     * @param property
     * @param value
     */
    public void attachProperty(IProperty property, String value) {
        for (PropertyBean ownProperty : getProperties()) {
            if (ownProperty.equals(property)) {
                return;
            }
        }

        PropertyBean propertyCopy = new PropertyBean(property.getUri().toASCIIString(), property.getTermRef().getName(), property.isMandatory(), property.isMultiple());
        propertyCopy.setValue(value);
        this.getProperties().add(propertyCopy);
    }

    public void attachProperty(PropertyBean property) {
        this.getProperties().add(property);
    }

    /**
     * *
     * Test whether the specified property has the specified value
     *
     * @param prop
     * @param value
     * @return
     */
    public boolean hasPropertyValue(Enum prop, String value) {
        for (PropertyBean property : getProperties()) {
            if (property.getCode().equals(PROPERTY_URLS.get(prop)) && property.getValue().equals(value)) {
                return true;
            }

        }
        return false;
    }

    /**
     * *
     * Test whether the property has only non-null values
     *
     * @param prop
     * @param value
     * @return
     */
    public boolean hasPropertyValue(Enum prop) {
        for (String s : getPropertyValues(prop)) {
            if (s == null) {
                return false;
            }
        }
        return true;
    }

    public boolean hasProperty(Enum prop) {
        return getPropertyValues(prop) != null;
    }

    public boolean hasProperties() {
        return getProperties().size() > 0;
    }

    public Set<String> getPropertyValues(Enum prop) {
        String url = PROPERTY_URLS.get(prop);
        Set<String> r = new THashSet<>();
        for (PropertyBean property : getProperties()) {
            if (property.getCode().equals(url)) {
                r.add(property.getValue());
            }
        }
        return r;
    }

    public Set<String> getPropertyValues(String propertyUrl) {
        Set<String> r = new THashSet<>();
        for (PropertyBean property : getProperties()) {

            if (property.getCode().equals(propertyUrl)) {
                r.add(property.getValue());
            }

        }
        return r;
    }

    /**
     * *
     * Get the eventIds of this event's related events.
     *
     * @return
     */
    public Set<String> getRelatedEventId() {
        if (hasProperty(Prop.RELATED_EVENT)) {
            return getPropertyValues(Prop.RELATED_EVENT);
        } else {
            return null;
        }
    }

    /**
     * *
     * Get the eventIds of this event's data-ending events. Returns null if
     * property absent.
     *
     * @return
     */
    /* public Set<String> getDataEndEvent() {
        if (hasProperty(Prop.DATA_END_EVENT)) {
            return getPropertyValues(Prop.DATA_END_EVENT); //if multiple annotations
        } else {
            return null;
        }
    }*/
    /**
     * *
     * Whether this event has potentially data attached. Independent of whether
     * the scientist makes this data available or not.
     *
     * @return
     */
    public boolean hasDataAttached() {
        Set<String> values = getPropertyValues(Prop.HASDATA);
        return values != null && values.size() == 1 && new ArrayList(values).get(0).equals("true");//having data is a boolean and may not occur mor ethan onec
    }

    /**
     * *
     * Verify if an existing event is legal, ie. constitutes a time, tool,
     * toolcat, process, action together and has an id
     *
     * @return
     */
    public boolean isLegal() {
        if (getTimeStampDt() == null || getId() == null || getActionUri() == null || getToolCategoryUri() == null || getProcessUri() == null || getTool() == null) {
            return false;
        }
        return true;
    }

    public static Gson gson = new GsonBuilder().disableHtmlEscaping().enableComplexMapKeySerialization().create();

    /**
     * *
     * Output a JSON representation of
     *
     * @return
     */
    private static String serializeProperties(Set<PropertyBean> properties) {
        Set<PropertyBean> result = new TreeSet<>(new EventPropertyComparator());
        for (PropertyBean property : properties) {
            if (property.getValue() != null && !property.getValue().equals("")) {
                result.add(property);
            }
        }
        String json = gson.toJson(properties);
        return json;
    }

    /**
     * *
     * Convert a String of multiple properties to the propertyMap
     *
     * @param propString
     * @return
     */
    private static Set<PropertyBean> deserializeProperties(String propString) {
        if (propString == null) {
            return new THashSet<>();
        }

        //  Set<Property> r = new TreeSet<>();
        if (JSONReader.isValidJSON(propString)) {
            try {
                propString = URLDecoder.decode(propString, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Exceptions.printStackTrace(ex);
            }
            Type type = new TypeToken<Set<PropertyBean>>() {
            }.getType();

            return gson.fromJson(propString, type);
        } else {
            return new THashSet<>();
        }
    }

    /**
     * ***
     * Clone an EventBean, including the id and eventId
     *
     * @return
     */
    @Override
    public EventBean clone() {
        EventBean clone = new EventBean();

        clone.setProgram(this.getProgram());

        clone.setToolCategoryJson(this.getToolCategoryJson());
        // clone.setToolSet(this.getToolSet());
        clone.setProcessJson(this.getProcessJson());
        clone.setActionJson(this.getActionJson());
        Set<PropertyBean> properties = this.getProperties();
        Set<PropertyBean> clonedProperties = new THashSet<>();
        for (PropertyBean property : properties) {
            clonedProperties.add(property.clone());
        }
        clone.setProperties(clonedProperties);

        clone.setActor(this.actor);
        clone.setTimeStampDt(this.timeStampDt);

        clone.setEventId(this.eventId);
        clone.setId(this.id);
        return clone;
    }

    @Override
    public String toString() {
        return "EventBean{" + "tool=" + tool + ", timeStampDt=" + timeStampDt + ", program=" + program + ", eventId=" + eventId + ", eventDefinitionId=" + eventDefinitionId + '}';
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getTool());
        sb.append(": ");
        sb.append(this.getProcessName());
        sb.append("/");
        sb.append(this.getActionName());
        sb.append(": ");
        sb.append(this.getTimeStamp());
        return sb.toString();
    }

    @Override
    public String getName() {
        return getEventId();
    }
}
