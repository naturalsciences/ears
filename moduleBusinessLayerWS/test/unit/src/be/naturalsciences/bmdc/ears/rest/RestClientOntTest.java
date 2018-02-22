/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.rest;

import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.User;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.util.Exceptions;

/**
 *
 * @author thomas
 */
public class RestClientOntTest {

    private static URL baseUrl;

    public RestClientOntTest() {
        try {
            baseUrl = new URL("https://ears.bmdc.be/");
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getVesselOntology method, of class RestClientOnt.
     */
    @Test
    public void testGetVesselOntology() throws ConnectException, EarsException, IOException {
        System.out.println("getVesselOntology");
        RestClientOnt instance = new RestClientOnt();
        String result = instance.getVesselOntology();
        assertTrue(result.length() > 20);

    }

    /**
     * Test of uploadVesselOntology method, of class RestClientOnt.
     */
    @Test
    public void testUploadVesselOntology_3args_1() throws ConnectException, EarsException {
        System.out.println("uploadVesselOntology");
        Path path = null;
        String username = "";
        String password = "";
        RestClientOnt instance = new RestClientOnt();
        IResponseMessage expResult = null;
        IResponseMessage result = instance.uploadVesselOntology(path, username, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of uploadVesselOntology method, of class RestClientOnt.
     */
    @Test
    public void testUploadVesselOntology_3args_2() throws ConnectException, EarsException {
        System.out.println("uploadVesselOntology");
        File file = null;
        String username = "";
        String password = "";
        RestClientOnt instance = new RestClientOnt();
        IResponseMessage expResult = null;
        IResponseMessage result = instance.uploadVesselOntology(file, username, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    /**
     * Test of authenticate method, of class RestClientOnt.
     */
    @Test
    public void testAuthenticate() throws ConnectException, EarsException {
        System.out.println("authenticate");
        User user = null;
        RestClientOnt instance = new RestClientOnt();
        boolean expResult = false;
        boolean result = instance.authenticate(user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVesselOntologyDate method, of class RestClientOnt.
     */
    @Test
    public void testGetVesselOntologyDate() throws ConnectException, EarsException {
        System.out.println("getVesselOntologyDate");
        RestClientOnt instance = new RestClientOnt();
        Date expResult = null;
        Date result = instance.getVesselOntologyDate();
        assertNotEquals(null, result);
    }

}
