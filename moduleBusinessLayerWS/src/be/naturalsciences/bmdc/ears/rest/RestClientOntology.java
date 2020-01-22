package be.naturalsciences.bmdc.ears.rest;//ys

import be.naturalsciences.bmdc.ears.entities.ActionBean;
import be.naturalsciences.bmdc.ears.entities.CategoryBean;
import be.naturalsciences.bmdc.ears.entities.CollateCentreBean;
import be.naturalsciences.bmdc.ears.entities.HarbourBean;
import be.naturalsciences.bmdc.ears.entities.OrganisationBean;
import be.naturalsciences.bmdc.ears.entities.PlatformClassBean;
import be.naturalsciences.bmdc.ears.entities.PropertyBean;
import be.naturalsciences.bmdc.ears.entities.ToolBean;
import static be.naturalsciences.bmdc.ears.rest.RestClient.createAllTrustingClient;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.openide.util.Exceptions;

/**
 * A class that will probably not be further developed. The ontology rest client is RestClientOntology.
 * @author thomas
 */
public class RestClientOntology extends RestClient {

    protected ResteasyClient client;// = new ResteasyClientBuilder().build();

    protected ResteasyWebTarget getHarborsTarget;
    protected ResteasyWebTarget getCollateCentresTarget;
    protected ResteasyWebTarget getChiefOrganisationsTarget;
    protected ResteasyWebTarget getPlatformsTarget;
    protected ResteasyWebTarget getToolsTarget;
    protected ResteasyWebTarget getActionsTarget;
    protected ResteasyWebTarget getPropertyTarget;
    protected ResteasyWebTarget getCategoryListTarget;

    public RestClientOntology() throws ConnectException, EarsException {
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
            throw new EarsException("The base url for the web services is invalid. The ontology web services won't work correctly.", ex);
        }
        if (uri != null) {
            getHarborsTarget = client.target(uri.resolve("ears2Ont/getHarbors"));
            getCollateCentresTarget = client.target(uri.resolve("ears2Ont/getCollateCentres"));
            getChiefOrganisationsTarget = client.target(uri.resolve("ears2Ont/getChiefOrganisations"));
            getPlatformsTarget = client.target(uri.resolve("ears2Ont/getPlatforms"));
            getToolsTarget = client.target(uri.resolve("ears2Ont/getTools"));
            getActionsTarget = client.target(uri.resolve("ears2Ont/getActions"));
            getPropertyTarget = client.target(uri.resolve("ears2Ont/getProperty"));
            getCategoryListTarget = client.target(uri.resolve("ears2Ont/getCategoryList"));
        }
        /*else {
            throw new EarsException("The base url for the web services has not been set correctly; the application won't work properly.");
        }*/
    }

    /*public Collection<SeaAreaBean> getAllSeaAreas() {

        // Client Using RESTEasy API
        Collection<SeaAreaBean> seaAreas = null;
        try {
            ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target = client.target(this.getOntologyURI().resolve("getSeaAreas"));
            Response response = target.request(MediaType.APPLICATION_XML).get();
            //Check Status
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }
            // Read output in string format
            seaAreas = (Collection<SeaAreaBean>) response.readEntity(new GenericType<Collection<SeaAreaBean>>() {
            });
            //System.out.println(seaAreas);
            response.close();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }

        return seaAreas;
    }*/
    public Collection<HarbourBean> getAllHarbors() throws ConnectException {

        // Client Using RESTEasy API
        Collection<HarbourBean> harbors = null;
   
            //ResteasyClient client = new ResteasyClientBuilder().build();
            //ResteasyWebTarget target = client.target(getBaseURL().resolve("getHarbors"));
            Response response = getHarborsTarget.request(MediaType.APPLICATION_XML).get();
            //Check Status
            if (response.getStatus() != 200) {
                throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getHarborsTarget.getUri().toString() + ")");
            }
            // Read output in string format
            // System.out.println(response.readEntity(new GenericType<String>(){}) );
            harbors = (Collection<HarbourBean>) response.readEntity(new GenericType<Collection<HarbourBean>>() {
            });
            response.close();
      

        return harbors;
    }

    public Collection<CollateCentreBean> getAllCollateCentres() throws ConnectException {

        // Client Using RESTEasy API
        Collection<CollateCentreBean> collateCentres = null;
       
            //ResteasyClient client = new ResteasyClientBuilder().build();
            //ResteasyWebTarget target = client.target(getBaseURL().resolve("getCollateCentres"));
            Response response = getCollateCentresTarget.request(MediaType.APPLICATION_XML).get();
            //Check Status
            if (response.getStatus() != 200) {
               throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getCollateCentresTarget.getUri().toString() + ")");
            }
            // Read output in string format
            collateCentres = (Collection<CollateCentreBean>) response.readEntity(new GenericType<Collection<CollateCentreBean>>() {
            });
            response.close();
       

        return collateCentres;
    }

    public Collection<OrganisationBean> getAllOrganizations() throws ConnectException {

        // Client Using RESTEasy API
        Collection<OrganisationBean> organizations = null;
       
            //ResteasyClient client = new ResteasyClientBuilder().build();
            //ResteasyWebTarget target = client.target(getBaseURL().resolve("getChiefOrganisations"));
            Response response = getChiefOrganisationsTarget.request(MediaType.APPLICATION_XML).get();
            //Check Status
            if (response.getStatus() != 200) {
                throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getChiefOrganisationsTarget.getUri().toString() + ")");
            }
            // Read output in string format
            organizations = (Collection<OrganisationBean>) response.readEntity(new GenericType<Collection<OrganisationBean>>() {
            });
            response.close();
    

        return organizations;
    }

    public Collection<PlatformClassBean> getAllPlatforms() throws ConnectException {

        // Client Using RESTEasy API
        Collection<PlatformClassBean> platforms = null;
        
            // ResteasyClient client = new ResteasyClientBuilder().build();
            //ResteasyWebTarget target = client.target(getBaseURL().resolve("getPlatforms"));;
            Response response = getPlatformsTarget.request(MediaType.APPLICATION_XML).get();
            //Check Status
            if (response.getStatus() != 200) {
                throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getPlatformsTarget.getUri().toString() + ")");
            }
            // Read output in string format
            platforms = (Collection<PlatformClassBean>) response.readEntity(new GenericType<Collection<PlatformClassBean>>() {
            });
            response.close();
       

        return platforms;
    }

    public Collection<ToolBean> getAllTools() throws ConnectException {

        // Client Using RESTEasy API
        Collection<ToolBean> tools = null;
       
            // ResteasyClient client = new ResteasyClientBuilder().build();
            // ResteasyWebTarget target = client.target(getBaseURL().resolve("getTools"));
            Response response = getToolsTarget.request(MediaType.APPLICATION_XML).get();

            //Check Status
            if (response.getStatus() != 200) {
                throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getToolsTarget.getUri().toString() + ")");
            }
            // Read output in string format
            //System.out.println(response.readEntity(new GenericType<String>(){}) );
            tools = (Collection<ToolBean>) response.readEntity(new GenericType<Collection<ToolBean>>() {
            });
            StringBuilder sb = new StringBuilder();
            for (ToolBean tool : tools) {
                sb.append(tool.toString());
            }
            System.out.println(sb.toString());
            // ---- Pruebas unmarshallling -----
            /*try {
             File file = new File("/Users/oscar/Documents/workspace/ears2BLML/src/main/resources/tools.xml");
             JAXBContext jaxbContext = JAXBContext.newInstance(ToolBeans.class);

             Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
             ToolBeans toolss = (ToolBeans) jaxbUnmarshaller.unmarshal(file);
             StringBuilder sb = new StringBuilder();
             for (ToolBean tool : toolss.getTools()) {
             sb.append("**********\n");
             sb.append(tool.toString());
             sb.append("**********\n");
             }
             System.out.println(sb.toString());

             } catch (JAXBException e) {
             e.printStackTrace();
             }
             */

            // ---------------------------------
            response.close();
       

        return tools;
    }

    public Collection<ActionBean> getAllActions() throws ConnectException {

        // Client Using RESTEasy API
        Collection<ActionBean> actions = null;
       
            //ResteasyClient client = new ResteasyClientBuilder().build();
            // ResteasyWebTarget target = client.target(getBaseURL().resolve("getActions"));
            Response response = getActionsTarget.request(MediaType.APPLICATION_XML).get();
            //Check Status
            if (response.getStatus() != 200) {
                throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getActionsTarget.getUri().toString() + ")");
            }
            // Read output in string format
            actions = (Collection<ActionBean>) response.readEntity(new GenericType<Collection<ActionBean>>() {
            });
            response.close();
      

        return actions;

        /*
         Collection<ActionBean> actions = Collections.emptyList();
         actions.add(new ActionBean("action1", "property01"));
         actions.add(new ActionBean("action2", "property01"));
         actions.add(new ActionBean("action3", "property02"));
         actions.add(new ActionBean("action4", "property03"));
         actions.add(new ActionBean("action5", "property05"));
	
         return actions;
         */
    }

    public Collection<PropertyBean> getPropertiesByAction(String action) throws ConnectException {

        // Event Using RESTEasy API
        Collection<PropertyBean> properties = null;
        
            // ResteasyClient client = new ResteasyClientBuilder().build();
            // ResteasyWebTarget target = client.target(getBaseURL().resolve("getProperty"));

            Response response = getPropertyTarget.queryParam("action", action).request().get();
            // Check Status
            if (response.getStatus() != 200) {
                throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getPropertyTarget.getUri().toString() + ")");
            }
            properties = response.readEntity(new GenericType<Collection<PropertyBean>>() {
            });
            // System.out.println(event);
            response.close();
     
        return properties;
    }

    public Collection<CategoryBean> getAllCategories() throws ConnectException {

        // Client Using RESTEasy API
        Collection<CategoryBean> categories = null;
      
            //    ResteasyClient client = new ResteasyClientBuilder().build();
            //    ResteasyWebTarget target = client.target(getBaseURL().resolve("getCategoryList"));
            Response response = getCategoryListTarget.request(MediaType.APPLICATION_XML).get();
            //Check Status
            if (response.getStatus() != 200) {
                throw new ConnectException("Failed (http code : " + response.getStatus() + "; url " + getCategoryListTarget.getUri().toString() + ")");
            }
            // Read output in string format
            categories = (Collection<CategoryBean>) response.readEntity(new GenericType<Collection<CategoryBean>>() {
            });
            response.close();
    

        return categories;

        /*
         Collection<ActionBean> actions = Collections.emptyList();
         actions.add(new ActionBean("action1", "property01"));
         actions.add(new ActionBean("action2", "property01"));
         actions.add(new ActionBean("action3", "property02"));
         actions.add(new ActionBean("action4", "property03"));
         actions.add(new ActionBean("action5", "property05"));
	
         return actions;
         */
    }

}
