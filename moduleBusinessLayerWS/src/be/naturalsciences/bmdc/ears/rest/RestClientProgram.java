/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.rest;

import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.RestMessage;
import be.naturalsciences.bmdc.ears.entities.Person;
import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.entities.ProjectBean;
import be.naturalsciences.bmdc.ears.entities.VesselBean;
import static be.naturalsciences.bmdc.ears.rest.RestClient.createAllTrustingClient;
import be.naturalsciences.bmdc.ontology.EarsException;
import eu.eurofleets.ears3.dto.PersonDTO;
import eu.eurofleets.ears3.dto.ProgramDTO;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
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
    protected ResteasyWebTarget getProgramsTarget;
    protected ResteasyWebTarget getProgramTarget;
    protected ResteasyWebTarget postTarget;
    protected ResteasyWebTarget removeTarget;
    
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
            getProgramsTarget = client.target(uri.resolve("ears3/ears2/programs"));
            getProgramTarget = client.target(uri.resolve("ears3/ears2/program"));
            postTarget = client.target(uri.resolve("ears3/program"));;
            removeTarget = client.target(uri.resolve("ears3/program"));
        }
    }
    
    public Collection<ProgramBean> getAllPrograms() throws ConnectException {
        Collection<ProgramBean> programs = new ArrayList();
        
        Response response = getProgramsTarget.request(MediaType.APPLICATION_XML).get();
        if (response.getStatus() != 200) {
            throw new ConnectException(response.getStatus() + "(" + response.getStatusInfo().getReasonPhrase() + ") - " + getProgramsTarget.getUri().toString() + ")");
        }
        programs = (Collection<ProgramBean>) response.readEntity(new GenericType<Collection<ProgramBean>>() {
        });
        response.close();
        
        return programs;
    }
    
    private ProgramBean getProgram(ResteasyWebTarget target) throws ConnectException {
        ProgramBean program = null;//new ProgramBean();
        Response response = null;
        
        try {
            response = target.request().get();
            if (response.getStatus() == 404) {
                return null;
            } else if (response.getStatus() != 200) {
                RestMessage message = response.readEntity(RestMessage.class);
                throw new ConnectException(response.getStatus() + "(" + response.getStatusInfo().getReasonPhrase() + ") -" + message.getMessage());
            } else {
                program = response.readEntity(ProgramBean.class);
                
            }
        } finally {
            response.close();
        }
        
        return program;
    }
    
    private ProgramBean getProgramByIdentifier(String programIdentifier) throws ConnectException {
        ResteasyWebTarget target = getProgramTarget.queryParam("identifier", programIdentifier);
        return getProgram(target);
    }
    
    public Collection<ProgramBean> getProgramByCruise(CruiseBean cruise) throws ConnectException {
        if (cruise != null) {
            return getProgramByCruiseId(cruise.getIdentifier());
        } else {
            return new ArrayList();
        }
    }
    
    public Collection<ProgramBean> getProgramByCruise(Collection<CruiseBean> cruises) throws ConnectException {
        Collection<ProgramBean> result = new ArrayList();
        
        for (CruiseBean cruise : cruises) {
            result.addAll(getProgramByCruise(cruise));
        }
        
        return result;
    }
    
    private Collection<ProgramBean> getProgramByCruiseId(String cruiseId) throws ConnectException {
        Collection<ProgramBean> programs = new ArrayList();
        Response response = getProgramsTarget.queryParam("cruiseIdentifier", cruiseId).request().get();
        try {
            if (response.getStatus() != 200) {
                throw new ConnectException(response.getStatus() + "(" + response.getStatusInfo().getReasonPhrase() + ") - " + getProgramsTarget.getUri().toString() + ")");
            }
            programs = (Collection<ProgramBean>) response.readEntity(new GenericType<Collection<ProgramBean>>() {
            });
        } finally {
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
    
    private ProgramDTO ProgramToDTO(ProgramBean pb) {
        ProgramDTO p = new ProgramDTO();
        p.description = pb.getDescription();
        p.identifier = pb.getProgramId();
        p.name = pb.getProgramId();
        p.principalInvestigators = new ArrayList<>();
        for (Person pib : pb.getPrincipalInvestigators()) {
            PersonDTO pi = new PersonDTO(pib);
            p.principalInvestigators.add(pi);
        }
        
        p.projects = new ArrayList<>();
        for (ProjectBean pr : pb.getProjects()) {
            p.projects.add(pr.getCode());
        }
        return p;
    }
    
    public IResponseMessage<ProgramDTO> postProgram(ProgramBean program) {
        if (program.isLegal()) {
            ProgramDTO programDTO = ProgramToDTO(program);
            
            try {
                if (getProgramByIdentifier(program.getProgramId()) == null) { //if this program has not been created before!
                    return performPost(postTarget, ProgramDTO.class, programDTO);
                } else {
                    return new RestMessage(10, "Could not create this program because a program with the same programId already exists!");
                }
            } catch (Exception e) {
                return new RestMessage(10, "Could not create this program because an exception occured: " + e.getLocalizedMessage());
            }
        } else {
            return new RestMessage(10, "Could not create this program because it is illegal");
        }
        
    }
    
    public IResponseMessage<ProgramDTO> modifyProgram(ProgramBean program) {
        
        if (program.isLegal()) {
            ProgramDTO programDTO = ProgramToDTO(program);
            try {
                return performPost(postTarget, ProgramDTO.class, programDTO);
            } catch (Exception e) {
                return new RestMessage("Could not modify this program because a " + e.getClass().getSimpleName() + " occured: " + e.getLocalizedMessage(), e);
            }
        } else {
            return new RestMessage("Could not modify this program because it is illegal");
        }
    }
    
    public IResponseMessage removeProgram(ProgramBean program) {
        return removeProgram(program.getName());
    }

    /**
     * *
     * Removes a cruise via the API by true cruise id, not the internal id.
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public IResponseMessage removeProgram(String programIdentifier) {
        ResteasyWebTarget target = removeTarget.queryParam("identifier", programIdentifier);
        Response response = target.request().delete();
        RestMessage res = null;
        if (response.getStatus() <= 400) {
            res = new RestMessage("Program " + programIdentifier + " removed ", response.getStatus(), programIdentifier, null, null);
        } else {
            res = new RestMessage("Program " + programIdentifier + " not removed, because there are events associated with it: " + response.readEntity(RestMessage.class).message, response.getStatus(), programIdentifier, null, null);
        }
        response.close();
        return res;
    }
    
}
