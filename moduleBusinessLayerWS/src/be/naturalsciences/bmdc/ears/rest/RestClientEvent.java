package be.naturalsciences.bmdc.ears.rest;//ys

import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.EventBean;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.MessageBean;
import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import static be.naturalsciences.bmdc.ears.rest.RestClient.createAllTrustingClient;
import static be.naturalsciences.bmdc.ears.rest.RestClient.printResponse;
import be.naturalsciences.bmdc.ontology.EarsException;
import eu.eurofleets.ears3.dto.EventDTO;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.openide.util.Exceptions;

public class RestClientEvent extends RestClient {

    protected ResteasyClient client;// = new ResteasyClientBuilder().build();
    protected ResteasyWebTarget getEventTarget;
    protected ResteasyWebTarget getEventsTarget;
    protected ResteasyWebTarget postEventTarget;
    protected ResteasyWebTarget modifyEventTarget;
    protected ResteasyWebTarget removeEventTarget;

    public RestClientEvent() throws ConnectException, EarsException {
        super(false);
        if (isHttps) {
            ApacheHttpClient4Engine engine = null;
            try {
                engine = new ApacheHttpClient4Engine(createAllTrustingClient());
            } catch (GeneralSecurityException ex) {
                Exceptions.printStackTrace(ex);
            }
            client = new ResteasyClientBuilder().httpEngine(engine).build();
        } else {
            client = new ResteasyClientBuilder().build();
        }
        URI uri = null;
        try {
            uri = baseURL.toURI();
        } catch (URISyntaxException ex) {
            throw new EarsException("The base url for the web services is invalid. The events won't work correctly.", ex);
        }
        if (uri != null) {
            getEventTarget = client.target(uri.resolve("ears3/dto/event"));
            getEventsTarget = client.target(uri.resolve("ears3/dto/events"));
            postEventTarget = client.target(uri.resolve("ears3/event"));
            modifyEventTarget = client.target(uri.resolve("ears3/event"));
            removeEventTarget = client.target(uri.resolve("ears3/event"));
        }
    }

    /**
     * *
     * Retrieve all events from the web services; append their names, as they
     * only contain uris
     *
     * @return
     */
    public Collection<EventDTO> getAllEvents() throws ConnectException {

        Collection<EventDTO> events = new ArrayList();
        Response response = getEventsTarget.request(MediaType.APPLICATION_XML).get();
        try {
            if (response.getStatus() != 200) {
                MessageBean message = response.readEntity(MessageBean.class);
                throw new ConnectException(response.getStatus() + "(" + response.getStatusInfo().getReasonPhrase() + ") -" + message.getMessage());
            }
            events = (Collection<EventDTO>) response.readEntity(new GenericType<Collection<EventDTO>>() {
            });
        } finally {
            response.close();
        }
        return events;
    }

    private EventDTO getEvent(String eventIdentifier) throws ConnectException {
        EventDTO event = null;
        Response response = null;
        try {
            response = getEventTarget.queryParam("identifier", eventIdentifier).request().get();
            if (response.getStatus() != 200) {
                MessageBean message = response.readEntity(MessageBean.class);
                throw new ConnectException(response.getStatus() + "(" + response.getStatusInfo().getReasonPhrase() + ") -" + message.getMessage());
            }
            event = response.readEntity(EventDTO.class);
        } finally {
            response.close();
        }
        return event;
    }

    /**
     * *
     * A web method to retrieve the events of one cruise. Is not implemented at
     * the level of the web services but indirectly via the date range of the
     * cruise.
     *
     * @param cruiseId
     * @return
     */
    public Collection<EventDTO> getEventsByCruise(CruiseBean cruise) throws ConnectException {
        Collection<EventDTO> events = new ArrayList();
        Response response = getEventsTarget.queryParam("cruiseIdentifier", cruise.getIdentifier()).request(MediaType.APPLICATION_XML).get();
        try {
            if (response.getStatus() != 200) {
                MessageBean message = response.readEntity(MessageBean.class);
                throw new ConnectException(response.getStatus() + "(" + response.getStatusInfo().getReasonPhrase() + ") -" + message.getMessage());
            }
            events = (Collection<EventDTO>) response.readEntity(new GenericType<Collection<EventDTO>>() {
            });
        } finally {
            response.close();
        }
        return events;

    }

    public IResponseMessage<EventDTO> postEvent(EventDTO event) {
        return performPost(postEventTarget, EventDTO.class, event);
    }

    public IResponseMessage removeEvent(String eventIdentifier) {
        ResteasyWebTarget target = removeEventTarget.queryParam("identifier", eventIdentifier);
        Response response = target.request().delete();
        MessageBean res = new MessageBean("Event removed " + response.getStatus(), response.getStatus(), eventIdentifier, null, null);
        response.close();
        return res;
    }

    public IResponseMessage<EventDTO> modifyEvent(EventDTO event) {
        //EventDTO eventDTO = new EventDTO(event);
        return performPost(postEventTarget, EventDTO.class, event);
    }
}
