package eu.eurofleets.ears3.dto;

import be.naturalsciences.bmdc.ears.entities.EARSConcept;
import be.naturalsciences.bmdc.ears.entities.EventBean;
import static be.naturalsciences.bmdc.ears.entities.EventBean.PROPERTY_URLS;
import be.naturalsciences.bmdc.ontology.entities.IProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import gnu.trove.set.hash.THashSet;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author thomas
 */
@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD) //ignore all the getters
public class EventDTO implements EARSConcept {

    public String identifier;
    public String eventDefinitionId;
    public String label;
    public String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    @XmlJavaTypeAdapter(value = OffsetDateTimeAdapter.class)
    public OffsetDateTime timeStamp;
    public PersonDTO actor;
    public LinkedDataTermDTO subject;
    public ToolDTO tool;
    public LinkedDataTermDTO toolCategory;
    public LinkedDataTermDTO process;
    public LinkedDataTermDTO action;
    public Collection<PropertyDTO> properties;
    public String program;
    public String platform;
    public Collection<NavigationDTO> navigation;
    public Collection<ThermosalDTO> thermosal;
    public Collection<WeatherDTO> weather;

    public EventDTO() {
    }

    public EventDTO(String identifier, String eventDefinitionId, OffsetDateTime timeStamp, PersonDTO actor, LinkedDataTermDTO subject, LinkedDataTermDTO toolCategory, ToolDTO tool, LinkedDataTermDTO process, LinkedDataTermDTO action, Set<PropertyDTO> properties, String program, String platform) {
        this.identifier = identifier;
        this.eventDefinitionId = eventDefinitionId;
        this.timeStamp = timeStamp;
        this.actor = actor;
        this.subject = subject;
        this.tool = tool;
        this.toolCategory = toolCategory;
        this.process = process;
        this.action = action;
        this.properties = properties;
        this.program = program;
        this.platform = platform;
    }

    /* public EventDTO(EventBean event) {

        this.identifier = event.getEventId(); //should be null when creating a new event
        this.eventDefinitionId = event.getEventDefinitionId();
        this.timeStamp = event.getTimeStampDt();
        if (event.getActor() != null) {
            this.actor = new PersonDTO(event.getActor());
        }
        this.subject = new LinkedDataTermDTO("http://vocab.nerc.ac.uk/collection/C77/current/M05", null, "Occasional standard measurements"); //TODO change this.
        this.tool = new ToolDTO(event.getTool());
        this.toolCategory = new LinkedDataTermDTO(event.getToolCategoryUri(), null, event.getToolCategoryName());
        this.process = process = new LinkedDataTermDTO(event.getProcessUri(), null, event.getProcessName());
        this.action = new LinkedDataTermDTO(event.getActionUri(), null, event.getActionName());
        this.properties = properties;
        this.program = event.getProgram().getName();
        this.platform = event.getPlatform().getCode();
    }*/
    public String getIdentifier() {
        return identifier;
    }

    public String getEventDefinitionId() {
        return eventDefinitionId;
    }

    public OffsetDateTime getTimeStamp() {
        return timeStamp;
    }

    public PersonDTO getActor() {
        return actor;
    }

    public LinkedDataTermDTO getSubject() {
        return subject;
    }

    public ToolDTO getTool() {
        return tool;
    }

    public LinkedDataTermDTO getToolCategory() {
        return toolCategory;
    }

    public LinkedDataTermDTO getProcess() {
        return process;
    }

    public LinkedDataTermDTO getAction() {
        return action;
    }

    public Collection<PropertyDTO> getProperties() {
        return properties;
    }

    public String getProgram() {
        return program;
    }

    public String getPlatform() {
        return platform;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setEventDefinitionId(String eventDefinitionId) {
        this.eventDefinitionId = eventDefinitionId;
    }

    public void setTimeStamp(OffsetDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setActor(PersonDTO actor) {
        this.actor = actor;
    }

    public void setSubject(LinkedDataTermDTO subject) {
        this.subject = subject;
    }

    public void setTool(ToolDTO tool) {
        this.tool = tool;
    }

    public void setToolCategory(LinkedDataTermDTO toolCategory) {
        this.toolCategory = toolCategory;
    }

    public void setProcess(LinkedDataTermDTO process) {
        this.process = process;
    }

    public void setAction(LinkedDataTermDTO action) {
        this.action = action;
    }

    public void setProperties(Set<PropertyDTO> properties) {
        this.properties = properties;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*public String getLabel() {
        if (this.properties != null && !this.properties.isEmpty()) {
            Set<String> values = getPropertyValues(EventBean.Prop.LABEL);
            return values.toArray(new String[1])[0];
        } else {
            return "";

        }
    }*/

    public Set<String> getPropertyValues(Enum prop) {
        String url = (String) PROPERTY_URLS.get(prop);
        Set<String> r = new THashSet<>();
        for (PropertyDTO property : getProperties()) {
            if (url.equals(property.key.transitiveIdentifier) || url.equals(property.key.identifier)) {
                r.add(property.value);
            }
        }
        return r;
    }

    @Override
    public boolean isLegal() {
        return true;
    }

    @Override
    public String getName() {
        return tool.fullName() + ": " + process.name + "-" + action.name + " at " + timeStamp.format(DateTimeFormatter.ISO_DATE);
    }

    public void attachProperty(PropertyDTO property, String value) {
        for (PropertyDTO ownProperty : getProperties()) {
            if (ownProperty.equals(property)) {
                return;
            }
        }
        PropertyDTO propertyCopy = property.clone();
        this.getProperties().add(propertyCopy);
    }

    public void attachProperty(IProperty property, String value) {
        for (PropertyDTO ownProperty : getProperties()) {
            if (ownProperty.equals(property)) {
                return;
            }
        }
        PropertyDTO propertyCopy = new PropertyDTO(new LinkedDataTermDTO(property.getUri().toASCIIString(), null, property.getTermRef().getName()), null, property.getUnit());
        propertyCopy.mandatory = property.isMandatory();
        propertyCopy.multiple = property.isMultiple();
        propertyCopy.valueClass = property.getValueClass();
        this.getProperties().add(propertyCopy);
    }

    @Override
    public EventDTO clone() {

        EventDTO clone = new EventDTO(this.identifier,
                this.eventDefinitionId,
                this.timeStamp,
                this.actor,
                this.subject,
                this.toolCategory,
                this.tool,
                this.process,
                this.action,
                this.properties != null ? new HashSet<>(this.properties) : null,
                this.program,
                this.platform);
        return clone;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.identifier);
        hash = 17 * hash + Objects.hashCode(this.eventDefinitionId);
        hash = 17 * hash + Objects.hashCode(this.timeStamp);
        hash = 17 * hash + Objects.hashCode(this.actor);
        hash = 17 * hash + Objects.hashCode(this.subject);
        hash = 17 * hash + Objects.hashCode(this.tool);
        hash = 17 * hash + Objects.hashCode(this.toolCategory);
        hash = 17 * hash + Objects.hashCode(this.process);
        hash = 17 * hash + Objects.hashCode(this.action);
        hash = 17 * hash + Objects.hashCode(this.properties);
        hash = 17 * hash + Objects.hashCode(this.program);
        hash = 17 * hash + Objects.hashCode(this.platform);
        hash = 17 * hash + Objects.hashCode(this.navigation);
        hash = 17 * hash + Objects.hashCode(this.thermosal);
        hash = 17 * hash + Objects.hashCode(this.weather);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventDTO other = (EventDTO) obj;
        if (!Objects.equals(this.identifier, other.identifier)) {
            return false;
        }
        if (!Objects.equals(this.eventDefinitionId, other.eventDefinitionId)) {
            return false;
        }
        if (!Objects.equals(this.program, other.program)) {
            return false;
        }
        if (!Objects.equals(this.platform, other.platform)) {
            return false;
        }
        if (!Objects.equals(this.timeStamp, other.timeStamp)) {
            return false;
        }
        if (!Objects.equals(this.actor, other.actor)) {
            return false;
        }
        if (!Objects.equals(this.subject, other.subject)) {
            return false;
        }
        if (!Objects.equals(this.tool, other.tool)) {
            return false;
        }
        if (!Objects.equals(this.toolCategory, other.toolCategory)) {
            return false;
        }
        if (!Objects.equals(this.process, other.process)) {
            return false;
        }
        if (!Objects.equals(this.action, other.action)) {
            return false;
        }
        if (!Objects.equals(this.properties, other.properties)) {
            return false;
        }
        if (!Objects.equals(this.navigation, other.navigation)) {
            return false;
        }
        if (!Objects.equals(this.thermosal, other.thermosal)) {
            return false;
        }
        if (!Objects.equals(this.weather, other.weather)) {
            return false;
        }
        return true;
    }

}
