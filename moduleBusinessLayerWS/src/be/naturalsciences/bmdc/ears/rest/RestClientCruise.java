/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.rest;

import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.MessageBean;
import be.naturalsciences.bmdc.ears.entities.Person;
import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.entities.SeaAreaBean;
import be.naturalsciences.bmdc.ears.entities.VesselBean;
import static be.naturalsciences.bmdc.ears.rest.RestClient.printResponse;
import be.naturalsciences.bmdc.ears.utils.DateUtilities;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import eu.eurofleets.ears3.dto.CruiseDTO;
import eu.eurofleets.ears3.dto.LinkedDataTermDTO;
import eu.eurofleets.ears3.dto.PersonDTO;
import eu.eurofleets.ears3.dto.ProgramDTO;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.openide.util.Exceptions;

public class RestClientCruise extends RestClient {

    protected ResteasyClient client;//= new ResteasyClientBuilder().build();
    protected ResteasyWebTarget getCruisesTarget;
    protected ResteasyWebTarget getCruiseTarget;
    protected ResteasyWebTarget postTarget;
    protected ResteasyWebTarget modifyTarget;
    protected ResteasyWebTarget removeTarget;

    public RestClientCruise() throws ConnectException, EarsException {
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
            throw new EarsException("The base url for the web services is invalid. The cruises won't work correctly.", ex);
        }
        if (uri != null) {
            getCruisesTarget = client.target(uri.resolve("ears3/ears2/cruises"));
            getCruiseTarget = client.target(uri.resolve("ears3/ears2/cruise"));
            postTarget = client.target(uri.resolve("ears3/cruise"));
            modifyTarget = client.target(uri.resolve("ears3/cruise"));
            removeTarget = client.target(uri.resolve("ears3/cruise"));
        }/* else {
            throw new EarsException("The base url for the web services has not been set correctly. The cruises .");
        }*/
    }

    public Collection<CruiseBean> getAllCruises() throws ConnectException {
        return getAllCruises(getCruisesTarget);
    }

    public Collection<CruiseBean> getCruiseByPlatform(VesselBean vessel) throws ConnectException {
        if (vessel != null) {
            return getCruiseByPlatformCode(vessel.getCode());
        } else {
            return new ArrayList();
        }
    }

    private CruiseBean getCruise(ResteasyWebTarget target) throws ConnectException {
        CruiseBean cruise = null;
        Response response = null;
        response = target.request().get();
        try {
            if (response.getStatus() == 404) {
                return null;
            } else if (response.getStatus() != 200) {
                MessageBean message = response.readEntity(MessageBean.class);
                throw new ConnectException(response.getStatus() + "(" + response.getStatusInfo().getReasonPhrase() + ") -" + message.getMessage());
            } else {
                cruise = response.readEntity(CruiseBean.class);
            }
        } finally {
            response.close();
        }

        return cruise;
    }

    private Collection<CruiseBean> getAllCruises(ResteasyWebTarget target) throws ConnectException {
        Collection<CruiseBean> cruises = new ArrayList();

        Response response = target.request().get();
        try {
            if (response.getStatus() != 200) {
                throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getCruisesTarget.getUri().toString() + ")");
            }
            cruises = (Collection<CruiseBean>) response.readEntity(new GenericType<Collection<CruiseBean>>() {
            });

        } finally {
            response.close();
        }

        return cruises;
    }

    private CruiseBean getCruise(String cruiseId, String platformCode) throws ConnectException {
        ResteasyWebTarget target = getCruiseTarget.queryParam("id", cruiseId);
        target = target.queryParam("platformCode", platformCode);
        CruiseBean cruise = getCruise(target);
        if (cruise.getPlatform().equals(platformCode)) {
            return cruise;
        } else {
            return null;
        }
    }

    private CruiseBean getCruiseByIdentifier(String cruiseIdentifier) throws ConnectException {
        ResteasyWebTarget target = getCruiseTarget.queryParam("identifier", cruiseIdentifier);
        return getCruise(target);
    }

    private Collection<CruiseBean> getCruiseByPlatformCode(String platformCode) throws ConnectException {
        ResteasyWebTarget target = getCruisesTarget.queryParam("platformCode", platformCode);
        List<CruiseBean> cruises = new ArrayList(getAllCruises(target));
        return cruises;
    }

    public CruiseDTO CruiseToDTO(CruiseBean pCruise) {
        CruiseDTO cruise = new CruiseDTO();
        cruise.identifier = pCruise.getIdentifier();
        cruise.name = pCruise.getName();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        cruise.startDate = pCruise.getdStartDate().toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
        cruise.endDate = pCruise.getdEndDate().toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
        List<PersonDTO> persons = new ArrayList<>();
        for (Person cs : pCruise.getChiefScientists()) {
            PersonDTO pi = new PersonDTO();
            pi.firstName = cs.firstName;
            pi.lastName = cs.lastName;
            pi.organisation = cs.organisationCode;
            persons.add(pi);
        }
        cruise.seaAreas = new ArrayList<>();
        for (SeaAreaBean sa : pCruise.getSeaAreas()) {
            cruise.seaAreas.add(sa.getCode());
        }
        cruise.programs = new ArrayList<>();
        for (ProgramBean program : pCruise.getPrograms()) {
            /*ProgramDTO pr = new ProgramDTO();
            pr.identifier = program.getName();
            pr.description = program.getDescription();
            for (Person pi : program.getPrincipalInvestigators()) {
                pr.principalInvestigators.add(new PersonDTO(pi));
            }*/
            cruise.programs.add(program.getName());
        }

        cruise.chiefScientists = persons;
        cruise.platform = pCruise.getPlatform();
        cruise.objectives = pCruise.getObjectives();
        cruise.collateCentre = pCruise.getCollateCentre();
        cruise.departureHarbour = pCruise.getDepartureHarbour();
        cruise.arrivalHarbour = pCruise.getArrivalHarbor();
        return cruise;
    }

    public IResponseMessage<CruiseDTO>  postCruise(CruiseBean pCruise) {
        if (pCruise.isLegal()) {
            CruiseDTO cruiseDTO = CruiseToDTO(pCruise);
            try {
                if (getCruiseByIdentifier(pCruise.getIdentifier()) == null) { //if this cruise has not been created before
                    List concurrentCruises = getConcurrentCruises(pCruise);

                    if (concurrentCruises.isEmpty()) { //if this cruise doesn't overlap with other cruises on this vessel
                        return performPost(postTarget, CruiseDTO.class, cruiseDTO);
                    } else {
                        return new MessageBean("Could not create this cruise because it overlaps with the following cruises of this vessel: " + StringUtils.join(concurrentCruises, ","));
                    }
                } else {
                    return new MessageBean("Could not create this cruise because a cruise with the same cruiseId already exists.");
                }
            } catch (Exception e) {
                return new MessageBean("Could not create this cruise because a " + e.getClass().getSimpleName() + " occured: " + e.getLocalizedMessage(), e);
            }
        } else {
            return new MessageBean("Could not create this cruise because it is illegal");
        }
    }

    public IResponseMessage<CruiseDTO> modifyCruise(CruiseBean pCruise) {
        if (pCruise.isLegal()) {
            CruiseDTO cruiseDTO = CruiseToDTO(pCruise);
            try {
                return performPost(postTarget, CruiseDTO.class,
                        cruiseDTO);
            } catch (Exception e) {
                return new MessageBean("Could not modify this cruise because a " + e.getClass().getSimpleName() + " occured: " + e.getLocalizedMessage(), e);
            }
        } else {
            return new MessageBean("Could not modify this cruise because it is illegal");
        }

    }

    public IResponseMessage removeCruise(CruiseBean cruise) {
        return removeCruise(cruise.getIdentifier());
    }

    /**
     * *
     * Removes a cruise via the API by true cruise id, not the internal id.
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public IResponseMessage removeCruise(String cruiseIdentifier) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = removeTarget.queryParam("identifier", cruiseIdentifier);
        Response response = target.request().delete();
        MessageBean res = new MessageBean("Cruise " + cruiseIdentifier + " removed ", response.getStatus(), cruiseIdentifier, null, null);
        response.close();
        return res;
    }

    /**
     * *
     * Removes a cruise via the API by true cruise id, not the internal id.
     *
     * @param startDate
     * @param endDate
     * @return
     */
    /*public IResponseMessage removeCruise(CruiseBean pCruise) {
        if (isOnline()) {
            ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target = removeTarget;//client.target(getBaseURL().resolve("removeCruise"));
            Response response = target.queryParam("id", pCruise.getIdentifier())
                    //.queryParam("SeaAreas", pCruise.getSeaAreas().toString())
                    .request().get();

            // Read output in string format
            MessageBean res = response.readEntity(MessageBean.class);

            response.close();
            return res;
        }
        return new MessageBean(10, "Could not remove this cruise because the web service is not available.");
    }*/
    /**
     * *
     * Removes a cruise via the API by startDate.
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public Integer removeCruise(String startDate, String endDate) {

        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = removeTarget;//client.target(getBaseURL().resolve("removeCruise"));
        Response response = target.queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .request().get();

        // Read output in string format
        System.out.println(response.getStatus());
        response.close();

        return response.getStatus();

    }

    public CruiseBean getCruiseByDate(OffsetDateTime timeStamp, VesselBean vessel) throws ConnectException {

        Collection< CruiseBean> cruises = getAllCruises();
        if (cruises != null) {
            for (CruiseBean c : cruises) {
                OffsetDateTime startDate = OffsetDateTime.parse(c.getStartDate());
                OffsetDateTime endDate = OffsetDateTime.parse(c.getEndDate());
                if (vessel.getCode().equals(c.getPlatform()) && (timeStamp.equals(startDate) || timeStamp.equals(endDate) || (timeStamp.isAfter(startDate) && timeStamp.isBefore(endDate)))) {
                    return c;
                }
            }
        }
        return null;
    }

    public List<CruiseBean> getCruisesBetweenDates(String platformCode, OffsetDateTime start, OffsetDateTime stop) throws ConnectException {
        List< CruiseBean> r = new ArrayList();

        Collection< CruiseBean> cruises = getCruiseByPlatformCode(platformCode);
        if (cruises != null) {

            for (CruiseBean c : cruises) {
                OffsetDateTime startDate = OffsetDateTime.parse(c.getStartDate());
                OffsetDateTime endDate = OffsetDateTime.parse(c.getEndDate());
                if (DateUtilities.isBetween(startDate, start, stop) || DateUtilities.isBetween(endDate, start, stop)) {
                    r.add(c);
                }
            }
        }

        return r;
    }

    public List<CruiseBean> getConcurrentCruises(CruiseBean test) throws ConnectException {
        List<CruiseBean> result = getCruisesBetweenDates(test.getPlatform(), DateUtilities.getOffsetDateTime(test.getdStartDate()), DateUtilities.getOffsetDateTime(test.getdEndDate()));
        List r = new ArrayList();
        for (CruiseBean cruise : result) {
            if (cruise.getPlatform().equals(test.getPlatform())) {
                r.add(cruise);
            }
        }
        return r;
    }

    public boolean cruiseIsConcurrent(CruiseBean test) throws ConnectException {
        return !getConcurrentCruises(test).isEmpty();
    }

}
