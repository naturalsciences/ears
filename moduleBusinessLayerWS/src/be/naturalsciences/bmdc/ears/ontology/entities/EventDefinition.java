package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ontology.entities.IEventDefinition;
import be.naturalsciences.bmdc.ontology.entities.ISubject;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

@Namespace("http://ontologies.ef-ears.eu/ears2/1#")
@RdfType("EventDefinition")
//public abstract class EventDefinition<E extends IEarsTerm, PR extends IProperty, EV extends IEventDefinition> implements IEventDefinition<E, PR, EV> {public abstract class EventDefinition<E extends IEarsTerm, PR extends IProperty, EV extends IEventDefinition> implements IEventDefinition<E, PR, EV> {
public class EventDefinition implements IEventDefinition<EarsTerm, Property, EventDefinition, Subject>, Serializable {

    //private static final long serialVersionUID = 1L;
    @Id
    protected URI uri;
    protected Long id;
    private Boolean isDataProvider;

    //private Collection<EV> triggeredCollection;
    //private Collection<EV> triggerCollection;
    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#hasProperty")
    private Collection<Property> propertyCollection;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#hasSubject")
    private Collection<Subject> subjectCollection;

    private Collection<EventDefinition> triggeredCollection;
    private Collection<EventDefinition> triggerCollection;

    public EventDefinition() {
        this.propertyCollection = new ArrayList<>();
    }

    public EventDefinition(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        try {
            return Long.parseLong(uri.getRawFragment().split("_")[1]);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setId(Long id) {
        //this.id = id;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public void setUri(URI uri) {
        if (uri.getRawFragment() == null) {
            throw new IllegalArgumentException("The URI should have a fragment part (#).");
        }
        this.uri = uri;
    }

    @Override
    public Boolean getIsDataProvider() {
        return isDataProvider == null ? false : isDataProvider;
    }

    @Override
    public void setIsDataProvider(Boolean isDataProvider) {
        this.isDataProvider = isDataProvider;
    }

    @Override
    //public Collection<PR> getPropertyCollection() {
    public Collection<Property> getPropertyCollection() {
        return propertyCollection;
    }

    @Override
    //public void setPropertyCollection(Collection<PR> propertyCollection) {
    public void setPropertyCollection(Collection<Property> propertyCollection) {
        this.propertyCollection = propertyCollection;
    }

    @Override
    //public Collection<EV> getTriggeredCollection() {
    public Collection<EventDefinition> getTriggeredCollection() {
        return triggeredCollection;
    }

    @Override
    //public void setTriggeredCollection(Collection<EV> triggeredCollection) {
    public void setTriggeredCollection(Collection<EventDefinition> triggeredCollection) {
        this.triggeredCollection = triggeredCollection;
    }

    @Override
    //public Collection<EV> getTriggerCollection() {
    public Collection<EventDefinition> getTriggerCollection() {
        return triggerCollection;
    }

    @Override
    //public void setTriggerCollection(Collection<EV> triggererCollection) {
    public void setTriggerCollection(Collection<EventDefinition> triggererCollection) {
        this.triggerCollection = triggererCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash ^= (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof EventDefinition)) {
            return false;
        }
        EventDefinition other = (EventDefinition) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        if ((this.uri == null && other.uri != null) || (this.uri != null && !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EventDefinition[ id=" + id + " ]";
    }

    @Override
    public String getLabel() {
        return this.toString();
    }

    /**
     * Adds the given property to the EventDefinition.
     *
     *
     */
    @Override
    public void addProperty(Property prop) {//public void addProperty(PR prop) {
        if (this.propertyCollection == null) {
            this.setPropertyCollection(new ArrayList<Property>()); //this.setPropertyCollection(new ArrayList<PR>());
        }
        if (!this.propertyCollection.contains(prop)) {
            this.getPropertyCollection().add(prop);
            prop.addEventDefinition(this);
        }
    }

    @Override
    public void setLabel() {

    }

    public void linkMyProcessAndActionBackToMe() {
    }

    @Override
    public Collection<? extends ISubject> getSubjectCollection() {
        return this.subjectCollection;
    }

    @Override
    public void setSubjectCollection(Collection<Subject> subjectCollection) {
        this.subjectCollection = subjectCollection;
    }

}
