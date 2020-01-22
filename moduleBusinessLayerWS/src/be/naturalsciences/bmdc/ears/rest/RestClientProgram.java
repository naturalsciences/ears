/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.rest;

import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.ExceptionMessage;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.entities.VesselBean;
import static be.naturalsciences.bmdc.ears.rest.RestClient.createAllTrustingClient;
import static be.naturalsciences.bmdc.ears.rest.RestClient.printResponse;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.openide.util.Exceptions;

public class RestClientProgram extends RestClient {

    protected ResteasyClient client;// = new ResteasyClientBuilder().build();
    protected ResteasyWebTarget getTarget;
    protected ResteasyWebTarget postTarget;

    public RestClientProgram() throws ConnectException, EarsException {
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
            //uri = getBaseURL().toURI();
        } catch (URISyntaxException ex) {
            throw new EarsException("The base url for the web services is invalid. The programs won't work correctly.", ex);
        }
        if (uri != null) {
            getTarget = client.target(uri.resolve("ears2/getProgram"));
            postTarget = client.target(uri.resolve("ears2/insertProgram"));;
        }
    }

    public Collection<ProgramBean> getAllPrograms() throws ConnectException {
        Collection<ProgramBean> programs = new ArrayList();
        if (online) {

            Response response = getTarget.request(MediaType.APPLICATION_XML).get();
            if (response.getStatus() != 200) {
                throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getTarget.getUri().toString() + ")");
            }
            programs = (Collection<ProgramBean>) response.readEntity(new GenericType<Collection<ProgramBean>>() {
            });
            response.close();

        }
        return programs;
    }

    private ProgramBean getProgram(ResteasyWebTarget target) throws ConnectException {
        ProgramBean program = null;//new ProgramBean();
        Response response = null;
        if (online) {
            try {
                response = target.request().get();
                if (response.getStatus() != 200) {
                    throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getTarget.getUri().toString() + ")");
                }
                program = response.readEntity(ProgramBean.class);
                response.close();
            } catch (ConnectException e) {
                throw e;
            } catch (ProcessingException e) {
                if (response != null && response.getLength() == -1) { //empty content because web service is not propertly set up
                    return null;
                }
                response = target.request(MediaType.APPLICATION_XML).get();
                IResponseMessage responseMessage = null;
                try {
                    responseMessage = response.readEntity(ExceptionMessage.class);
                } catch (ProcessingException e2) {
                    responseMessage = new ExceptionMessage(new Date().toString(), e2);
                }
                printResponse(target, response, this.getClass(), responseMessage);
            }
        }
        return program;
    }

    public ProgramBean getProgram(String programId) throws ConnectException {
        ResteasyWebTarget target = getTarget.queryParam("id", programId);
        return getProgram(target);
    }

    public ProgramBean getProgram(String programId, String cruiseId) throws ConnectException {
        ResteasyWebTarget target = getTarget.queryParam("id", programId);
        target = target.queryParam("cruiseId", cruiseId);
        ProgramBean result = getProgram(target);
        if (result != null && result.getCruiseId().equals(cruiseId)) {
            return result;
        } else {
            return null;
        }
    }

    public Collection<ProgramBean> getProgramByCruise(CruiseBean cruise) throws ConnectException {
        if (cruise != null) {
            return getProgramByCruiseId(cruise.getRealId());
        } else {
            return new ArrayList();
        }
    }

    public Collection<ProgramBean> getProgramByCruise(Collection<CruiseBean> cruises) throws ConnectException {
        Collection<ProgramBean> result = new ArrayList();
        if (online) {
            for (CruiseBean cruise : cruises) {
                result.addAll(getProgramByCruise(cruise));
            }
        }
        return result;
    }

    public Collection<ProgramBean> getProgramByCruiseId(String cruiseId) throws ConnectException {

        Collection<ProgramBean> programs = new ArrayList();
        if (online) {

            ResteasyClient client = new ResteasyClientBuilder().build();

            //ResteasyWebTarget target = client.target(getBaseURL().resolve("getProgram"));
            Response response = getTarget.queryParam("cruiseId", cruiseId).request().get();
            //Check Status
            if (response.getStatus() != 200) {
                throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getTarget.getUri().toString() + ")");
            }
            // Read output in string format
            programs = (Collection<ProgramBean>) response.readEntity(new GenericType<Collection<ProgramBean>>() {

            });

            response.close();

        }
        return programs;
    }

    public Collection<ProgramBean> getProgramByVessel(VesselBean vessel) throws EarsException, ConnectException {
        if (vessel != null) {
            RestClientCruise cruiseClient = null;

            cruiseClient = new RestClientCruise();

            Collection<CruiseBean> cruises;
            if (cruiseClient != null) {
                cruises = cruiseClient.getCruiseByPlatform(vessel);
            } else {
                return new ArrayList();
            }
            return getProgramByCruise(cruises);
        } else {
            return new ArrayList();
        }
    }

    public IResponseMessage postProgram(ProgramBean pProgram) {
        if (online) {
            if (pProgram.isLegal()) {
                ResteasyClient client = new ResteasyClientBuilder().build();
                //ResteasyWebTarget target = client.target(getBaseURL().resolve("insertProgram"));

                ResteasyWebTarget target = postTarget
                        .queryParam("cruiseId", pProgram.getCruiseId())
                        .queryParam("description", pProgram.getDescription())
                        .queryParam("originatorCode", pProgram.getOriginatorCode())
                        .queryParam("PIName", pProgram.getPiName())
                        .queryParam("id", pProgram.getProgramId())
                        .queryParam("projects", pProgram.getProjectIds()
                        );

                try {
                    if (getProgram(pProgram.getProgramId(), pProgram.getCruiseId()) == null) { //if this program has not been created before for this cruise!
                        return performGetWhichIsActuallyAPost(target, CruiseBean.class
                        );
                    } else {
                        return new ExceptionMessage(new Date().toString(), "Could not create this program because a program with the same programId already exists for this cruise");
                    }
                } catch (Exception e) {
                    return new ExceptionMessage(new Date().toString(), "Could not create this program because an exception occured: " + e.getLocalizedMessage());
                }
            } else {
                return new ExceptionMessage(new Date().toString(), "Could not create this program because it is illegal");
            }
        }
        return new ExceptionMessage(new Date().toString(), "Could not create this program because the web service is not available.");
    }

}
