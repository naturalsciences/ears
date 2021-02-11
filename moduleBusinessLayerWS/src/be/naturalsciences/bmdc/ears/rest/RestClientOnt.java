/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.rest;

import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.MessageBean;
import be.naturalsciences.bmdc.ears.entities.User;
import static be.naturalsciences.bmdc.ears.rest.RestClient.createAllTrustingClient;
import be.naturalsciences.bmdc.ears.utils.WebserviceUtils;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.OntologyConstants;
import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.openide.util.Exceptions;

/**
 *
 * @author thomas
 */
public class RestClientOnt extends RestClient {

    protected ResteasyClient client;// = new ResteasyClientBuilder().build();
    protected ResteasyWebTarget getVesselOntologyTarget;
    protected ResteasyWebTarget getProgramOntologyTarget;
    protected ResteasyWebTarget getVesselOntologyDateTarget;
    protected ResteasyWebTarget getProgramOntologyDateTarget;
    protected ResteasyWebTarget uploadVesselOntologyTarget;
    protected ResteasyWebTarget uploadProgramOntologyTarget;
    protected ResteasyWebTarget authenticateTarget;

    private void init() throws EarsException, ConnectException {
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
            throw new EarsException("The base url for the web services is invalid. Managing the vessel tree won't work correctly.", ex);
        }
        if (uri != null) {
            getVesselOntologyTarget = client.target(uri.resolve("ears3/ontology/vessel"));
            getProgramOntologyTarget = client.target(uri.resolve("ears3/ontology/program"));
            getVesselOntologyDateTarget = client.target(uri.resolve("ears3/ontology/vessel/date"));
            getProgramOntologyDateTarget = client.target(uri.resolve("ears3/ontology/program/date"));
            uploadVesselOntologyTarget = client.target(uri.resolve("ears3/uploadVesselOntology"));
            uploadProgramOntologyTarget = client.target(uri.resolve("ears3/uploadProgramOntology"));
            authenticateTarget = client.target(uri.resolve("ears3/ontology/authenticate"));
        }
    }

    public RestClientOnt() throws ConnectException, EarsException {
        super(false);
        init();
    }

    public String getVesselOntology() {
        Response response = getVesselOntologyTarget.request("application/rdf+xml; charset=utf-8").get();
        String entity = response.readEntity(String.class);
        //InputStream entity = response.readEntity(InputStream.class);
        if (response.getStatus() != 200 || entity.length() < 20) {
            return null;
        }
        response.close();
        return entity;

    }

    public String getProgramOntology(String name) {

        if (name.endsWith(".rdf")) {
            name = name.replaceAll(".rdf", "");
        }
        Response response = getProgramOntologyTarget.queryParam("name", name).request("application/rdf+xml; charset=utf-8").get();
        String entity = response.readEntity(String.class);
        //InputStream entity = response.readEntity(InputStream.class);
        if (response.getStatus() != 200 || entity.length() < 20) {
            return null;
        }
        response.close();
        return entity;

    }

    public String getOntology(String fileName) {
        if (fileName == null || fileName.equals(OntologyConstants.VESSEL_ONTOLOGY_FILENAME)) {
            return getVesselOntology();
        } else {
            return getProgramOntology(fileName);
        }
    }

    public IResponseMessage uploadVesselOntology(Path path, String username, String password) {
        try ( FileInputStream fis = new FileInputStream(path.toFile())) {
            return uploadVesselOntology(fis, path.getFileName().toString(), username, password);
        } catch (FileNotFoundException ex) {
            return new MessageBean(ex);
        } catch (IOException ex) {
            return new MessageBean(ex);
        }
    }

    public IResponseMessage uploadVesselOntology(File file, String username, String password) {
        try ( FileInputStream fis = new FileInputStream(file)) {
            return uploadVesselOntology(fis, file.getName(), username, password);
        } catch (FileNotFoundException ex) {
            return new MessageBean(ex);
        } catch (IOException ex) {
            return new MessageBean(ex);
        }
    }

    public IResponseMessage uploadVesselOntology(FileInputStream stream, String fileName, String username, String password) {

        MultipartFormDataOutput mdo = new MultipartFormDataOutput();
        mdo.addFormData("file", stream, MediaType.APPLICATION_OCTET_STREAM_TYPE, fileName);
        mdo.setBoundary("BOUNDARY");
        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(mdo) {
        };
        uploadVesselOntologyTarget.register(new BasicAuthentication(username, password));
        Response response = uploadVesselOntologyTarget.request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));

        IResponseMessage responseMessage = response.readEntity(MessageBean.class);
        responseMessage.setCode(response.getStatus());
        printResponse(uploadVesselOntologyTarget, response.getStatus(), this.getClass(), responseMessage);
        return responseMessage;

    }

    public IResponseMessage uploadProgramOntology(Path path) {
        try ( FileInputStream fis = new FileInputStream(path.toFile())) {
            return uploadProgramOntology(fis, path.getFileName().toString());
        } catch (FileNotFoundException ex) {
            return new MessageBean(ex);
        } catch (IOException ex) {
            return new MessageBean(ex);
        }
    }

    public IResponseMessage uploadProgramOntology(FileInputStream stream, String fileName) {

        MultipartFormDataOutput mdo = new MultipartFormDataOutput();
        mdo.addFormData("file", stream, MediaType.APPLICATION_OCTET_STREAM_TYPE, fileName);
        mdo.setBoundary("BOUNDARY");
        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(mdo) {
        };

        Response response = uploadProgramOntologyTarget.request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));

        IResponseMessage responseMessage = response.readEntity(MessageBean.class);
        responseMessage.setCode(response.getStatus());
        printResponse(uploadProgramOntologyTarget, response.getStatus(), this.getClass(), responseMessage);
        return responseMessage;

    }

    public boolean authenticate(User user) throws ConnectException {

        authenticateTarget.register(new BasicAuthentication(user.getUsername(), user.getPassword()));
        Response response = authenticateTarget.request(MediaType.TEXT_PLAIN).get();

        if (response.getStatus() != 200) {
            throw new ConnectException(response.getStatus() + "(" + response.getStatusInfo().getReasonPhrase() + ") - " + authenticateTarget.getUri().toString() + ")");
        }
        String result = response.readEntity(String.class);
        response.close();
        return Boolean.valueOf(result);

    }

    public Date getVesselOntologyDate() {

        Response response = getVesselOntologyDateTarget.request(MediaType.TEXT_PLAIN).accept(MediaType.TEXT_PLAIN).get();

        if (response.getStatus() != 200) {
            return null;
        }
        String result = response.readEntity(String.class);
        response.close();
        Date parse = StringUtils.parse(result, new SimpleDateFormat[]{StringUtils.SDF_FULL_ISO_DATETIME, StringUtils.SDF_SIMPLE_DATE});
        return parse;

    }

    public Date getProgramOntologyDate(String fileName) {

        Response response = getProgramOntologyDateTarget.queryParam("name", fileName).request(MediaType.TEXT_PLAIN).accept(MediaType.TEXT_PLAIN).get();
        if (response.getStatus() != 200) {
            return null;
        }
        String result = response.readEntity(String.class);
        response.close();
        Date parse = StringUtils.parse(result, new SimpleDateFormat[]{StringUtils.SDF_FULL_ISO_DATETIME, StringUtils.SDF_SIMPLE_DATE});
        return parse;

    }

    public Date getOntologyDate(String fileName) {
        if (fileName == null || fileName.equals(OntologyConstants.VESSEL_ONTOLOGY_FILENAME) || fileName.equals(OntologyConstants.VESSEL_ONTOLOGY_FILENAME_NO_EXT)) {
            return getVesselOntologyDate();
        } else {
            return getProgramOntologyDate(fileName);
        }
    }

}
