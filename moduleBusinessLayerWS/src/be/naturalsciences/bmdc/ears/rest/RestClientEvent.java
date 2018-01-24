package be.naturalsciences.bmdc.ears.rest;//ys

import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.EventBean;
import be.naturalsciences.bmdc.ears.entities.ExceptionMessage;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import static be.naturalsciences.bmdc.ears.rest.RestClient.createAllTrustingClient;
import static be.naturalsciences.bmdc.ears.rest.RestClient.printResponse;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
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
    protected ResteasyWebTarget getTarget;
    protected ResteasyWebTarget postTarget;
    protected ResteasyWebTarget modifyTarget;
    protected ResteasyWebTarget removeTarget;

    public RestClientEvent() throws ConnectException, EarsException {
        super();
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
            getTarget = client.target(uri.resolve("ears2/getEvent"));
            postTarget = client.target(uri.resolve("ears2/insertEvent"));
            modifyTarget = client.target(uri.resolve("ears2/modifyEvent"));
            removeTarget = client.target(uri.resolve("ears2/removeEvent"));
        }
    }

    /**
     * *
     * Retrieve all events from the web services; append their names, as they
     * only contain uris
     *
     * @return
     */
    public Collection<EventBean> getAllEvents() {

        Collection<EventBean> events = new ArrayList();
        if (online) {
            ResteasyWebTarget target = null;
            Response response;
            try {
                response = getTarget.request(MediaType.APPLICATION_XML).get();
                if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
                }
                events = (Collection<EventBean>) response.readEntity(new GenericType<Collection<EventBean>>() {
                });
                response.close();
            } catch (Exception e) {
                e.getCause().getStackTrace();
                response = target.request(MediaType.APPLICATION_XML).get();
                IResponseMessage responseMessage;
                try {
                    responseMessage = response.readEntity(ExceptionMessage.class);
                } catch (ProcessingException e2) {
                    responseMessage = new ExceptionMessage(new Date().toString(), e2);
                }
                printResponse(target, response, this.getClass(), responseMessage);
            }
        }
        return events;
    }

    public EventBean getEvent(String eventId) {
        EventBean event = null;
        if (online) {
            try {
                Response response = getTarget.queryParam("id", eventId).request().get();
                if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
                }
                event = response.readEntity(EventBean.class);

                response.close();
            } catch (ProcessingException e) {
                Response response = getTarget.request(MediaType.APPLICATION_XML).get();
                IResponseMessage responseMessage = null;
                try {
                    responseMessage = response.readEntity(ExceptionMessage.class);
                } catch (ProcessingException e2) {
                    responseMessage = new ExceptionMessage(new Date().toString(), e2);
                }
                printResponse(getTarget, response, this.getClass(), responseMessage);

            }
        }
        return event;
    }

    public EventBean getLatestEventOf(List<String> eventIds) {
        EventBean latest = null;
        if (online) {
            for (String eid : eventIds) {
                EventBean event = getEvent(eid);
                if (latest == null || event.getTimeStampDt().isAfter(latest.getTimeStampDt())) {
                    latest = event;
                }
            }
        }
        return latest;
    }

    public EventBean getEarliestEventOf(List<String> eventIds) {
        EventBean latest = null;
        if (online) {
            for (String eid : eventIds) {
                EventBean event = getEvent(eid);
                if (latest == null || event.getTimeStampDt().isBefore(latest.getTimeStampDt())) {
                    latest = event;
                }
            }
        }
        return latest;
    }

    // Not implemented in ears2 WS - Why?
    public Collection<EventBean> getEventByProgram() {

        return null;
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
    public Collection<EventBean> getEventByCruise(CruiseBean cruise) {

        OffsetDateTime startDate = OffsetDateTime.ofInstant(cruise.getdStartDate().toInstant(), ZoneId.of("UTC"));
        OffsetDateTime endDate = OffsetDateTime.ofInstant(cruise.getdEndDate().toInstant(), ZoneId.of("UTC"));

        return getEventByDates(startDate, endDate);
    }

    public EventBean getEventByDate(String date) {
        EventBean event = null;
        if (online) {
            try {
                //ResteasyWebTarget target = client.target(getBaseURL().resolve("getEvent"));
                //prepare Date time for parameter
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date dateParsed = sdf.parse(date);
                Response response = getTarget.queryParam("date", sdf.format(dateParsed)).request().get();
                // Check Status
                if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + response.getStatus());
                }
                event = response.readEntity(EventBean.class);
                //System.out.println(event);
                response.close();
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
            }
        }
        return event;
    }

    /**
     * *
     * A web method to retrieve the events between two dates.
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public Collection<EventBean> getEventByDates(OffsetDateTime fromDate, OffsetDateTime toDate) {
        Collection< EventBean> r = new ArrayList();
        if (online) {
            Collection< EventBean> events = getAllEvents();

            for (EventBean e : events) {
                if (e.isLegal()) {
                    OffsetDateTime timeStamp = e.getTimeStampDt();
                    if (timeStamp.isAfter(fromDate) && timeStamp.isBefore(toDate)) {
                        r.add(e);
                    }
                }
            }
        }
        return r;
    }

    public IResponseMessage postEvent(EventBean event) {
        if (online) {
            if (getEvent(event.getEventId()) == null) { //if this event has not been created before!
                ResteasyWebTarget target = postTarget.queryParam("id", event.getEventId())
                        .queryParam("date", event.getTimeStamp())
                        .queryParam("actor", event.getActor())
                        .queryParam("subject", encodeUrl(event.getToolCategory()))
                        .queryParam("tool", encodeUrl(event.getTools()))
                        .queryParam("categoryName", encodeUrl(event.getProcess()))
                        .queryParam("actionName", encodeUrl(event.getAction()))
                        .queryParam("actionProperty", encodeUrl(event.getProperty()));

                return performGetWhichIsActuallyAPost(target, EventBean.class);
            } else {
                return new ExceptionMessage(new Date().toString(), "Could not create this event because an event with the same eventId already exists");
            }
        }
        return new ExceptionMessage(new Date().toString(), "Could not create this event because the web service is not available.");
    }

    public IResponseMessage removeEvent(String EventId) {
        if (online) {
            ResteasyWebTarget target = removeTarget.queryParam("id", EventId);
            return performGetWhichIsActuallyAPost(target, EventBean.class);
        }
        return new ExceptionMessage(new Date().toString(), "Could not delete this event because the web service is not available.");
    }

    public IResponseMessage modifyEvent(EventBean event) {
        if (online) {
            ResteasyWebTarget target = modifyTarget
                    .queryParam("eventId", event.getEventId())
                    .queryParam("timeStamp", event.getTimeStamp())
                    .queryParam("actor", event.getActor() == null ? "" : event.getActor())
                    .queryParam("subject", encodeUrl(event.getToolCategory()))
                    .queryParam("tool", encodeUrl(event.getTools()))
                    .queryParam("categoryName", encodeUrl(event.getProcess()))
                    .queryParam("actionName", encodeUrl(event.getAction()))
                    .queryParam("actionProperty", encodeUrl(event.getProperty()));
            return performGetWhichIsActuallyAPost(target, EventBean.class);
        }
        return new ExceptionMessage(new Date().toString(), "Could not modify this event because the web service is not available.");
    }

}
