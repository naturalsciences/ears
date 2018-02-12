package be.naturalsciences.bmdc.ears.entities;

import be.naturalsciences.bmdc.ears.comparator.EventPropertyComparator;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.IAction;
import be.naturalsciences.bmdc.ontology.entities.IProcess;
import be.naturalsciences.bmdc.ontology.entities.IProperty;
import be.naturalsciences.bmdc.ontology.entities.ITool;
import be.naturalsciences.bmdc.ontology.entities.IToolCategory;
import be.naturalsciences.bmdc.ontology.writer.JSONReader;
import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gnu.trove.set.hash.THashSet;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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

    public static final String TOOL_DELIM = ", ";

    public static final Map<Enum, String> PROPERTY_URLS;

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

    /**
     * A map of URI Strings of the properties. A property is a key, value pair.
     * As the same property can occur N times, this is implemented as a
     * Map<String, List<String>>
     */
    //private Map<SingletonMap<String, String>, Set<String>> propertyMap = new THashMap<>(); //property
    private Set<Property> properties = new TreeSet<>(new EventPropertyComparator());
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

    private String actor;

    private Set<String> toolSet; //tool

    private Set<EventBean> relatedEvents;

    private ProgramBean program;

    private CruiseBean cruise;

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

    public class Property implements Comparable<Property> {

        public String code;
        public String name;
        public transient boolean isMandatory;
        public transient boolean isMultiple;
        public transient String valueClass;
        public String value;

        public Property(String code, String name, boolean isMandatory, boolean isMultiple) {
            this.code = code;
            this.name = name;
            this.isMandatory = isMandatory;
            this.isMultiple = isMultiple;
        }

        public Property clone() {
            Property prop = new Property(this.code, this.name, this.isMandatory, this.isMultiple);
            prop.valueClass = valueClass;
            prop.value = value;
            return prop;
        }

        @Override
        public int compareTo(Property other) {
            int i;
            if (this.value == null && other.value == null) {
                i = 0;
            } else if (this.value != null && other.value != null) {
                i = this.value.compareTo(other.value);
            } else {
                i = -1;
            }
            if (i != 0) {
                return i;
            }
            return this.code.compareTo(other.code);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Property) {
                Property other = (Property) o;
                if (this.code.equals(other.code)) {
                    if ((this.value == null && other.value == null) || this.value.equals(other.value)) {
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
        }

        public boolean equals(IProperty p) {

            return this.code.equals(p.getUri().toString());
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.code);
            hash = 97 * hash + Objects.hashCode(this.value);
            return hash;
        }
    }

    public EventBean() {
        super();
    }

    /**
     * *
     * Constructor for an event. Do not use when creating event from
     * webservices, only for event creation module. Implementation based on URI.
     *
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
    public EventBean(ProgramBean program, CruiseBean cruise, IToolCategory toolCategory, Set<ITool> tools, IProcess process, IAction action, Set<IProperty> properties, String actor) throws IllegalArgumentException {
        super();
        if (toolCategory == null) {
            throw new IllegalArgumentException("Null tool category provided");
        } else if (tools == null) {
            throw new IllegalArgumentException("Null tool provided");
        } else if (tools.isEmpty()) {
            throw new IllegalArgumentException("Tools list empty");
        } else if (process == null) {
            throw new IllegalArgumentException("Null process provided");
        } else if (action == null) {
            throw new IllegalArgumentException("Null action provided");
        } else if (cruise == null) {
            throw new IllegalArgumentException("No cruise provided.");
        } else if (program == null) {
            throw new IllegalArgumentException("No program provided.");
        }

        //this.toolCategory = toolCategory;
        //this.tools = new HashSet();
        //this.tools.addAll(tools);
        //this.process = process;
        //this.action = action;
        //this.properties = properties;
        this.actor = actor;

        // this.toolUris = new HashMap();
        this.toolSet = new LinkedHashSet();

        for (ITool tool : tools) {
            this.addTool(tool);
        }
        this.processUri = new SingletonMap(AsConcept.getConceptUriString(process), AsConcept.getConceptName(process));//Collections.singletonMap(AsConcept.getConceptUriString(process), AsConcept.getConceptName(process));
        this.toolCategoryUri = new SingletonMap(AsConcept.getConceptUriString(toolCategory), AsConcept.getConceptName(toolCategory));//Collections.singletonMap(AsConcept.getConceptUriString(toolCategory), AsConcept.getConceptName(toolCategory));
        this.actionUri = new SingletonMap(AsConcept.getConceptUriString(action), AsConcept.getConceptName(action));//Collections.singletonMap(AsConcept.getConceptUriString(action), AsConcept.getConceptName(action));

        Property programProperty = new Property(PROPERTY_URLS.get(Prop.PROGRAM), "program", true, true);
        programProperty.value = program.getProgramId();
        programProperty.valueClass = "Program";
        this.properties.add(programProperty);

        if (properties != null) {
            for (IProperty earsProperty : properties) {
                Property prop = new Property(AsConcept.getConceptUri(earsProperty).toASCIIString(), AsConcept.getConceptName(earsProperty), earsProperty.isMandatory(), earsProperty.isMultiple());
                prop.isMandatory = earsProperty.isMandatory();
                prop.isMultiple = earsProperty.isMultiple();
                prop.valueClass = earsProperty.getValueClass();

                this.properties.add(prop);
            }
        }

        this.timeStampDt = OffsetDateTime.now();
        this.cruise = cruise;
        this.program = program;
        this.eventId = buildEventId();
        counter++;
    }

        /**
     * *
     * Constructor for an event. Do not use when creating event from
     * webservices, only for event creation module. Implementation based on URI.
     *
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
    public EventBean(ProgramBean program, CruiseBean cruise, IToolCategory toolCategory, Set<ITool> tools, IProcess process, IAction action, Set<IProperty> properties, String actor, OffsetDateTime timeStamp) throws IllegalArgumentException {
        this(program, cruise, toolCategory, tools, process, action, properties, actor);
        this.timeStampDt = timeStamp;
        this.eventId = buildEventId();
    }

    private String buildEventId() {
        return this.timeStampDt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + cruise.getCruiseName() + "-" + counter;
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

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "timeStamp")
    public String getTimeStamp() {
        if (timeStampDt != null) {
            return timeStampDt.format(StringUtils.FULL_ISO_DATETIME);
            // return timeStampDt.toInstant().toString();
        } else {
            return null;
        }
    }

    public void setTimeStamp(String timeStamp) {
        if (timeStamp != null) {

            //this.timeStampDt = OffsetDateTime.ofInstant(Instant.parse(timeStamp), ZoneId.of("UTC"));
            this.timeStampDt = OffsetDateTime.parse(timeStamp);
        } else {
            this.timeStampDt = null;
        }
    }

    public OffsetDateTime getTimeStampDt() {
        return timeStampDt;
    }

    public void setTimeStampDt(OffsetDateTime timeStampDt) {
        this.timeStampDt = timeStampDt;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "actor")
    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    /**
     * *
     * Return a JSON string representation of the properties. Removes properties
     * with an empty or null value.
     *
     * @return
     */
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "property")
    public String getProperty() {
        Set<Property> propResults = new TreeSet<>(new EventPropertyComparator());
        for (Property property : getProperties()) {
            if (property.value != null && !property.value.equals("")) {
                propResults.add(property);
            }

        }
        return serializeProperties(propResults);
    }

    public void setProperty(String propertyName) {
        this.properties.addAll(deserializeProperties(propertyName));
    }

    @XmlTransient
    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }

    public String getLabel() {
        Set<String> values = getPropertyValues(Prop.LABEL);
        return values.toArray(new String[1])[0];
    }

    public Object getProgramProperty() {
        Set<String> values = getPropertyValues(Prop.PROGRAM);
        return values.toArray(new String[1])[0];
    }

    @XmlElementWrapper(namespace = "http://www.eurofleets.eu/", name = "tool")
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "name")
    public Set<String> getToolSet() {
        return this.toolSet;
    }

    public void setToolSet(Set<String> toolSet) {
        this.toolSet = toolSet;
    }

    public Map<String, String> getToolUris() {
        return deserializeTools(toolSet);
    }

    public String getTools() {
        return StringUtils.concatString(toolSet, TOOL_DELIM);//serializeTools(toolUris);
    }

    public static String serializeTools(Map<String, String> tools) {
        Set<String> result = new THashSet<>();
        for (Map.Entry<String, String> entry : tools.entrySet()) {
            SingletonMap toolMap = new SingletonMap(entry);
            result.add(serializeConcept(toolMap));
        }
        return StringUtils.concatString(result, TOOL_DELIM);
    }

    /**
     * *
     * Convert a provided tool uri and tool name to a json tool representation
     *
     * @param uri
     * @param name
     * @return
     */
    public static String serializeTool(String uri, String name) {
        Set<String> result = new THashSet<>();
        SingletonMap toolMap = new SingletonMap(uri, name);
        result.add(serializeConcept(toolMap));

        return StringUtils.concatString(result, TOOL_DELIM);
    }

    /**
     * *
     * Convert a collection of json tool representations to a Map that maps the
     * tool uri to the tool name
     *
     * @param toolJson
     * @return
     */
    public static Map<String, String> deserializeTools(Collection<String> toolJson) {
        Map<String, String> result = new LinkedHashMap<>();

        for (String string : toolJson) {
            String[] split = string.split(",\\+");
            for (String string1 : split) {
                SingletonMap<String, String> deserializeConcept = deserializeConcept(string1);
                String toolUri = deserializeConcept.firstKey();
                String toolName = deserializeConcept.get(deserializeConcept.firstKey());
                result.put(toolUri, toolName);
            }

        }
        return result;
    }

    /**
     * *
     * Return the names of the attached tools, separated by a comma.
     *
     * @return
     */
    public String getToolNames() {
        return StringUtils.concatString(getToolUris().values(), ", ");
    }

    /*public static boolean isUrl(String url) {
        try {
            URL u = new URL(url);
        } catch (MalformedURLException ex) {
            return false;
        }
        return true;
    }*/
    public void addTool(ITool tool) {
        this.toolSet.add(serializeTool(AsConcept.getConceptUriString(tool), AsConcept.getConceptName(tool)));
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "subjectName")
    public String getToolCategory() {
        if (toolCategoryUri != null && toolCategoryUri.keySet() != null) {
            return serializeConcept(toolCategoryUri);
        } else {
            return null;
        }
    }

    public void setToolCategory(String subjectUri) {
        this.toolCategoryUri = deserializeConcept(subjectUri);
    }

    public String getToolCategoryUri() {
        if (toolCategoryUri != null && toolCategoryUri.keySet() != null) {
            return toolCategoryUri.firstKey();
        } else {
            return null;
        }
    }

    public boolean hasToolCategoryName() {
        if (toolCategoryUri != null) {
            return toolCategoryUri.values().toArray()[0] != null && !toolCategoryUri.values().toArray()[0].equals("");
        }
        return false;
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

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "categoryName")
    public String getProcess() {
        if (processUri != null && processUri.keySet() != null) {
            return serializeConcept(processUri);
        } else {
            return null;
        }
    }

    public void setProcess(String categoryUri) {
        this.processUri = deserializeConcept(categoryUri);
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

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "name")
    public String getAction() {
        if (actionUri != null && actionUri.keySet() != null) {
            return serializeConcept(actionUri);
        } else {
            return null;
        }
    }

    public void setAction(String actionUri) {
        this.actionUri = deserializeConcept(actionUri);
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

    public CruiseBean getCruise() {
        return cruise;
    }

    public void setCruise(CruiseBean cruise) {
        this.cruise = cruise;
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
        for (Property ownProperty : getProperties()) {
            if (ownProperty.equals(property)) {
                return;
            }
        }

        Property propertyCopy = new Property(property.getUri().toASCIIString(), property.getTermRef().getName(), property.isMandatory(), property.isMultiple());
        propertyCopy.value = value;
        this.getProperties().add(propertyCopy);
        /*String propertyUrl = PROPERTY_URLS.get(prop);
         Set propValues = getPropertyValues(prop);
         if (propValues == null) { //ie if key doesn't exist, if this property wasn't before registered.
         propValues = new THashSet<>();
         }
         propValues.add(value);

         propertyMap.put(propertyUrl, propValues);*/
    }

    /**
     * *
     * Test whether the property has the specified value
     *
     * @param prop
     * @param value
     * @return
     */
    public boolean hasPropertyValue(Enum prop, String value) {
        for (Property property : getProperties()) {
            if (property.code.equals(PROPERTY_URLS.get(prop)) && property.value.equals(value)) {
                return true;
            }

        }
        /*for (String s : getPropertyMap().get(EventBean.PROPERTY_URLS.get(prop))) {
         return s.equals(value);
         }*/
        //getPropertyMap().get(EventBean.PROPERTY_URLS.get(prop)).get(0).equals("true");
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
        for (Property property : getProperties()) {

            if (property.code.equals(url)) {
                r.add(property.value);
            }

        }
        return r;
    }

    public Set<String> getPropertyValues(String propertyUrl) {
        Set<String> r = new THashSet<>();
        for (Property property : getProperties()) {

            if (property.code.equals(propertyUrl)) {
                r.add(property.value);
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
        if (getTimeStampDt() == null || getId() == null || getActionUri() == null || getToolCategoryUri() == null || getProcessUri() == null || getToolSet() == null || getToolSet().isEmpty() || new ArrayList(getToolSet()).get(0) == null) {
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
    private static String serializeProperties(Set<Property> properties) {
        Set<Property> result = new TreeSet<>(new EventPropertyComparator());
        for (Property property : properties) {
            if (property.value != null && !property.value.equals("")) {
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
    private static Set<Property> deserializeProperties(String propString) {
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
            Type type = new TypeToken<Set<Property>>() {
            }.getType();

            return gson.fromJson(propString, type);
        } else {
            return new THashSet<>();
        }
    }

    private static String serializeConcept(SingletonMap<String, String> concept) {
        String json = gson.toJson(concept);
        return json;
    }

    private static SingletonMap<String, String> deserializeConcept(String jsonConcept) {
        Type type = new TypeToken<Map>() {
        }.getType();
        try {
            Map fromJson = gson.fromJson(jsonConcept, type);
            SingletonMap<String, String> r = new SingletonMap(fromJson);
            String label = r.get(r.firstKey());
            label = label.replaceAll("\\+", " ");
            r.put(r.firstKey(), label);
            return r;
        } catch (Exception e) {
            SingletonMap<String, String> r = new SingletonMap("pseudokey", jsonConcept);
            String label = r.get(r.firstKey());
            label = label.replaceAll("\\+", " ");
            r.put(r.firstKey(), label);
            return r;
        }

    }

    @Override
    public String toString() {

        return "Event [eventId=" + eventId + ", timeStamp=" + getTimeStampDt().toString() + ", actor="
                + actor + ", subject=" + getToolCategoryName() + ", actionName=" + getActionName()
                + ", actionProperty=" + serializeProperties(getProperties()) + ", categoryName="
                + getProcessName() + "tools= " + getToolNames() + "] " + "\n";
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("T: ");
        sb.append(this.getToolNames());
        sb.append(" P: ");
        sb.append(this.getToolCategoryName());
        sb.append(" A: ");
        sb.append(this.getActionName());
        sb.append(" TIME: ");
        sb.append(this.getTimeStamp());
        return sb.toString();
    }
}
