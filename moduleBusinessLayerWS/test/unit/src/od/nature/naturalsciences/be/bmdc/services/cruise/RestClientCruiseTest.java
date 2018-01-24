/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package od.nature.naturalsciences.be.bmdc.services.cruise;

import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.MessageBean;
import be.naturalsciences.bmdc.ears.entities.SeaAreaBean;
import be.naturalsciences.bmdc.ears.rest.RestClientCruise;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Thomas Vandenberghe
 */
public class RestClientCruiseTest {

    public RestClientCruiseTest() {
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
     * Test of getAllCruises method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testGetAllCruises() throws ConnectException, EarsException, MalformedURLException {
        System.out.println("getAllCruises");
        RestClientCruise instance = new RestClientCruise();
        Collection<CruiseBean> expResult = null;
        Collection<CruiseBean> result = instance.getAllCruises();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCruise method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testGetCruise() throws ConnectException, EarsException, MalformedURLException {
        System.out.println("getCruise");
        String cruiseId = "";
        RestClientCruise instance = new RestClientCruise();
        CruiseBean expResult = null;
        CruiseBean result = instance.getCruise(cruiseId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCruiseByJson method, of class RestClientCruise.
     */
    /*@Test
     @Ignore
     public void testGetCruiseByJson() {
     System.out.println("getCruiseByJson");
     String cruiseId = "";
     RestClientCruise instance = new RestClientCruise();
     CruiseBean expResult = null;
     CruiseBean result = instance.getCruiseByJson(cruiseId);
     assertEquals(expResult, result);
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }*/
    /**
     * Test of getCruiseByPlatform method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testGetCruiseByPlatform() {

    }

    /**
     * Test of postCruise method, of class RestClientCruise.
     */
    @Test
    public void testPostCruiseByParams() throws ParseException, ConnectException, EarsException, MalformedURLException {
        System.out.println("postCruiseByParams");
        CruiseBean pCruise = new CruiseBean();
        pCruise.setArrivalHarbor("Plymouth");
        // pCruise.setAttributId("");
        pCruise.setChiefScientist("Charles Darwin");
        pCruise.setChiefScientistOrganisation("Royal Society");
        pCruise.setCollateCenter("Royal Society");
        pCruise.setCruiseName("Second voyage of the HMS Beagle");
        pCruise.setEndDate("1836-10-02T18:00:00Z");
        pCruise.setInternalId("Beagle2");
        pCruise.setObjectives("Conduct a hydrographic survey of the coasts of the southern part of South America");
        pCruise.setPlatformClass("Brig-Sloop");
        pCruise.setPlatformCode("HMS Beagle");
        SeaAreaBean sea1 = new SeaAreaBean("S1", "test:S1", "Atlantic Ocean");
        SeaAreaBean sea2 = new SeaAreaBean("S2", "test:S2", "Pacific Ocean");
        SeaAreaBean sea3 = new SeaAreaBean("S3", "test:S3", "Southern Ocean");
        SeaAreaBean sea4 = new SeaAreaBean("S4", "test:S4", "Indian Ocean");
        Set<SeaAreaBean> seas = new HashSet();
        pCruise.setSeaAreas(seas);
        pCruise.setStartDate("1831-12-27T18:00:00Z");
        pCruise.setStartingHarbor("Plymouth");
        //pCruise.setdEndDate(Date);
        //pCruise.setdStartDate(Date);

        RestClientCruise instance = new RestClientCruise();
        MessageBean expResult = null;
        IResponseMessage result = instance.postCruise(pCruise);
        if (result.isBad()) {
            fail("response failed: cruise not created. see log.");
        }
        int a = 5;
    }

    /**
     * Test of postCruiseByParamsVoid method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testPostCruiseByParamsVoid() throws ConnectException, EarsException, MalformedURLException {
        System.out.println("postCruiseByParamsVoid");
        CruiseBean pCruise = null;
        RestClientCruise instance = new RestClientCruise();
        instance.postCruise(pCruise);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeCruise method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testRemoveCruise_CruiseBean() throws ConnectException, EarsException, MalformedURLException {
        System.out.println("removeCruise");
        CruiseBean pCruise = null;
        RestClientCruise instance = new RestClientCruise();
        MessageBean expResult = null;
        IResponseMessage result = instance.removeCruise(pCruise);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeCruise method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testRemoveCruise_String_String() throws ConnectException, EarsException, MalformedURLException {
        System.out.println("removeCruise");
        String startDate = "";
        String endDate = "";
        RestClientCruise instance = new RestClientCruise();
        Integer expResult = null;
        Integer result = instance.removeCruise(startDate, endDate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCruiseByDate method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testGetCruiseByDate() throws ConnectException, EarsException, MalformedURLException {
        System.out.println("getCruiseByDate");
        OffsetDateTime timeStamp = null;
        RestClientCruise instance = new RestClientCruise();
        CruiseBean expResult = null;
        //CruiseBean result = instance.getCruiseByDate(timeStamp);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
