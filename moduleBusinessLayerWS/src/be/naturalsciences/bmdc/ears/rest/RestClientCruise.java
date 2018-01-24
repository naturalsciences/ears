/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.rest;

import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.ExceptionMessage;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.MessageBean;
import be.naturalsciences.bmdc.ears.entities.VesselBean;
import static be.naturalsciences.bmdc.ears.rest.RestClient.printResponse;
import be.naturalsciences.bmdc.ears.utils.DateUtilities;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
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
    protected ResteasyWebTarget getTarget;
    protected ResteasyWebTarget postTarget;
    protected ResteasyWebTarget modifyTarget;
    protected ResteasyWebTarget removeTarget;

    /*static ResteasyClient client = new ResteasyClientBuilder().build();
    static ResteasyWebTarget getTarget;
    static ResteasyWebTarget postTarget;
    static ResteasyWebTarget modifyTarget;
    static ResteasyWebTarget removeTarget;

    static {
        getTarget = client.target(getBaseURI().resolve("ears2/getCruise"));
        postTarget = client.target(getBaseURI().resolve("ears2/insertCruise"));
        modifyTarget = client.target(getBaseURI().resolve("ears2/modifyCruise"));
        removeTarget = client.target(getBaseURI().resolve("ears2/removeCruise"));
    }*/
    public RestClientCruise() throws ConnectException, EarsException {
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
            throw new EarsException("The base url for the web services is invalid. The cruises won't work correctly.", ex);
        }
        if (uri != null) {
            getTarget = client.target(uri.resolve("ears2/getCruise"));
            postTarget = client.target(uri.resolve("ears2/insertCruise"));
            modifyTarget = client.target(uri.resolve("ears2/modifyCruise"));
            removeTarget = client.target(uri.resolve("ears2/removeCruise"));
        }/* else {
            throw new EarsException("The base url for the web services has not been set correctly. The cruises .");
        }*/
    }

    public Collection<CruiseBean> getAllCruises() {
        return getCruises(getTarget);
    }

    private CruiseBean getCruise(ResteasyWebTarget target) {
        CruiseBean cruise = null;
        if (online) {
            try {
                Response response = target.request().get();
                if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
                }
                cruise = response.readEntity(CruiseBean.class);
                response.close();
            } catch (ProcessingException e) {
                Response response = target.request(MediaType.APPLICATION_XML).get();
                IResponseMessage responseMessage = null;
                try {
                    responseMessage = response.readEntity(ExceptionMessage.class);
                } catch (ProcessingException e2) {
                    responseMessage = new ExceptionMessage(new Date().toString(), e2);
                }
                printResponse(target, response, this.getClass(), responseMessage);
            } catch (RuntimeException e) {
                Messaging.report("There was a problem connecting to the web services" + e.getMessage(), e, this.getClass(), true);
            }
        }
        return cruise;
    }

    private Collection<CruiseBean> getCruises(ResteasyWebTarget target) {
        Collection<CruiseBean> cruises = new ArrayList();

        if (online) {
            try {
                Response response = target.request().get();
                if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
                }
                cruises = (Collection<CruiseBean>) response.readEntity(new GenericType<Collection<CruiseBean>>() {
                });
                response.close();
            } catch (ProcessingException e) {
                try {
                    Response response = target.request().get();
                    //   Response response = target.request(MediaType.APPLICATION_XML).get();
                    IResponseMessage responseMessage = null;
                    try {
                        responseMessage = response.readEntity(ExceptionMessage.class);
                    } catch (ProcessingException e2) {
                        responseMessage = new ExceptionMessage(new Date().toString(), e2);
                    }
                    printResponse(target, response, this.getClass(), responseMessage);
                } catch (RuntimeException e2) {
                    Messaging.report("There was a problem connecting to the web services" + e.getMessage(), e, this.getClass(), true);
                }
            } catch (RuntimeException e) {
                Messaging.report("There was a problem connecting to the web services" + e.getMessage(), e, this.getClass(), true);
            }
        }
        return cruises;
    }

    public CruiseBean getCruise(String cruiseId, String platformCode) {
        ResteasyWebTarget target = getTarget.queryParam("id", cruiseId);
        target = target.queryParam("platformCode", platformCode);
        CruiseBean cruise = getCruise(target);
        if (cruise.getPlatformCode().equals(platformCode)) {
            return cruise;
        } else {
            return null;
        }
    }

    public CruiseBean getCruise(String cruiseId) {
        ResteasyWebTarget target = getTarget.queryParam("id", cruiseId);
        return getCruise(target);
    }

    public Collection<CruiseBean> getCruiseByPlatformCode(String platformCode) {
        ResteasyWebTarget target = getTarget.queryParam("platformCode", platformCode);
        List<CruiseBean> cruises = new ArrayList(getCruises(target));
        for (int i = 0; i < cruises.size(); i++) {
            if (!cruises.get(i).getPlatformCode().equals(platformCode)) {
                cruises.remove(i);
            }
        }
        return cruises;
    }

    public Collection<CruiseBean> getCruiseByPlatform(VesselBean vessel) {
        if (vessel != null) {
            return getCruiseByPlatformCode(vessel.getCode());
        } else {
            return new ArrayList();
        }
    }

    public IResponseMessage postCruise(CruiseBean pCruise) {
        if (online) {
            if (pCruise.isLegal()) {
                ResteasyClient client = new ResteasyClientBuilder().build();
                ResteasyWebTarget target = null;// = postTarget;//client.target(getBaseURL().resolve("insertCruise"));

                try {
                    target = postTarget.queryParam("id", pCruise.getRealId())
                            .queryParam("cruiseName", pCruise.getCruiseName())
                            .queryParam("startDate", pCruise.getStartDate())
                            .queryParam("endDate", pCruise.getEndDate())
                            .queryParam("chiefScientist", URLEncoder.encode(pCruise.getChiefScientist(), "ISO-8859-1"))
                            .queryParam("csorg", pCruise.getChiefScientistOrganisation())
                            .queryParam("platformCode", pCruise.getPlatformCode())
                            .queryParam("platformClass", "SDN:L06::31")
                            .queryParam("objectives", pCruise.getObjectives())
                            .queryParam("collateCenter", pCruise.getCollateCenter())
                            .queryParam("startingHarbor", pCruise.getStartingHarbor())
                            .queryParam("arrivalHarbor", pCruise.getArrivalHarbor());
                    if (!pCruise.getSeaAreasIds().isEmpty()) {
                        target = target.queryParam("seaAreas", pCruise.getSeaAreasIds());
                    }
                } catch (UnsupportedEncodingException ex) {
                    Exceptions.printStackTrace(ex);
                }
                //.queryParam("seaAreas", "1,2")
                try {
                    if (getCruise(pCruise.getRealId()) == null) { //if this cruise has not been created before
                        List concurrentCruises = getConcurrentCruises(pCruise);
                        if (concurrentCruises.isEmpty()) { //if this cruise doesn't overlap with other cruises on this vessel
                            return performGetWhichIsActuallyAPost(target, CruiseBean.class);
                        } else {
                            return new ExceptionMessage(new Date().toString(), "Could not create this cruise because it overlaps with the following cruises of this vessel: " + StringUtils.join(concurrentCruises, ","));
                        }
                    } else {
                        return new ExceptionMessage(new Date().toString(), "Could not create this cruise because a cruise with the same cruiseId already exists.");
                    }
                } catch (Exception e) {
                    return new ExceptionMessage(new Date().toString(), "Could not create this cruise because an exception occured: " + e.getLocalizedMessage());
                }
            } else {
                return new ExceptionMessage(new Date().toString(), "Could not create this cruise because it is illegal");
            }
        }
        return new ExceptionMessage(new Date().toString(), "Could not create this cruise because the web service is not available.");
    }

    public IResponseMessage modifyCruise(CruiseBean pCruise) {
        if (online) {
            if (pCruise.isLegal()) {
                ResteasyClient client = new ResteasyClientBuilder().build();
                ResteasyWebTarget target = null;// = modifyTarget;//client.target(getBaseURL().resolve("modifyCruise"));

                try {
                    target = modifyTarget.queryParam("id", pCruise.getRealId())
                            .queryParam("cruiseName", pCruise.getCruiseName())
                            .queryParam("startDate", pCruise.getStartDate())
                            .queryParam("endDate", pCruise.getEndDate())
                            .queryParam("chiefScientist", URLEncoder.encode(pCruise.getChiefScientist(), "ISO-8859-1"))
                            .queryParam("csorg", pCruise.getChiefScientistOrganisation())
                            .queryParam("platformCode", pCruise.getPlatformCode())
                            .queryParam("platformClass", pCruise.getPlatformClass())
                            .queryParam("objectives", pCruise.getObjectives())
                            .queryParam("collateCenter", pCruise.getCollateCenter())
                            .queryParam("startingHarbor", pCruise.getStartingHarbor())
                            .queryParam("arrivalHarbor", pCruise.getArrivalHarbor())
                            .queryParam("seaAreas", pCruise.getSeaAreasIds());
                } catch (UnsupportedEncodingException ex) {
                    Exceptions.printStackTrace(ex);
                }
                try {
                    return performGetWhichIsActuallyAPost(target, CruiseBean.class);
                } catch (Exception e) {
                    return new ExceptionMessage(new Date().toString(), "Could not create this cruise because an exception occured: " + e.getLocalizedMessage());
                }

                // Read output in string format
                //MessageBean res = response.readEntity(MessageBean.class);
                //MessageBean res = new MessageBean(pCruise.getInternalId(), "Cruise inserted");
                //System.out.println("Response getStatus code from Server when Mofify Cruise" + response.getStatus());
                //
                //response.close();
            } else {
                return new ExceptionMessage(new Date().toString(), "Could not create this cruise because it is illegal");
            }
        }
        return new ExceptionMessage(new Date().toString(), "Could not modify this cruise because the web service is not available.");
    }

    /**
     * *
     * Removes a cruise via the API by true cruise id, not the internal id.
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public IResponseMessage removeCruise(String realId) {
        if (online) {
            ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target;//= removeTarget;//client.target(getBaseURL().resolve("removeCruise"));
            Response response = removeTarget.queryParam("id", realId)
                    //.queryParam("SeaAreas", pCruise.getSeaAreas().toString())
                    .request().get();

            // Read output in string format
            MessageBean res = new MessageBean(realId, response.getStatus(), "Cruise Removed");

            //response.close();
            return res;
        }
        return new ExceptionMessage(new Date().toString(), "Could not remove this cruise because the web service is not available.");
    }

    /**
     * *
     * Removes a cruise via the API by true cruise id, not the internal id.
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public IResponseMessage removeCruise(CruiseBean pCruise) {
        if (online) {
            ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target = removeTarget;//client.target(getBaseURL().resolve("removeCruise"));
            Response response = target.queryParam("id", pCruise.getRealId())
                    //.queryParam("SeaAreas", pCruise.getSeaAreas().toString())
                    .request().get();

            // Read output in string format
            MessageBean res = new MessageBean(pCruise.getInternalId(), response.getStatus(), "Cruise Removed");

            //response.close();
            return res;
        }
        return new ExceptionMessage(new Date().toString(), "Could not remove this cruise because the web service is not available.");
    }

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

    public CruiseBean getCruiseByDate(OffsetDateTime timeStamp, VesselBean vessel) {
        if (online) {
            Collection< CruiseBean> cruises = getAllCruises();
            if (cruises != null) {
                for (CruiseBean c : cruises) {
                    OffsetDateTime startDate = OffsetDateTime.parse(c.getStartDate());
                    OffsetDateTime endDate = OffsetDateTime.parse(c.getEndDate());
                    if (vessel.getCode().equals(c.getPlatformCode()) && (timeStamp.equals(startDate) || timeStamp.equals(endDate) || (timeStamp.isAfter(startDate) && timeStamp.isBefore(endDate)))) {
                        return c;
                    }
                }
            }
        }
        return null;
    }

    public List<CruiseBean> getCruisesBetweenDates(String platformCode, OffsetDateTime start, OffsetDateTime stop) {
        List< CruiseBean> r = new ArrayList();
        if (online) {
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
        }
        return r;
    }

    public List<CruiseBean> getConcurrentCruises(CruiseBean test) {
        List<CruiseBean> result = getCruisesBetweenDates(test.getPlatformCode(), DateUtilities.getOffsetDateTime(test.getdStartDate()), DateUtilities.getOffsetDateTime(test.getdEndDate()));
        List r = new ArrayList();
        for (CruiseBean cruise : result) {
            if (cruise.getPlatformCode().equals(test.getPlatformCode())) {
                r.add(cruise);
            }
        }
        return r;
    }

    public boolean cruiseIsConcurrent(CruiseBean test) {
        return !getConcurrentCruises(test).isEmpty();
    }

}
