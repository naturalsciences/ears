/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.rest;

import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.Person;
import be.naturalsciences.bmdc.ears.entities.SeaAreaBean;
import be.naturalsciences.bmdc.ears.entities.VesselBean;
import be.naturalsciences.bmdc.ontology.EarsException;
import eu.eurofleets.ears3.dto.CruiseDTO;
import java.net.ConnectException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author thomas
 */
public class RestClientCruiseTest {

    public RestClientCruiseTest() {
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
    public void testGetAllCruises() throws Exception {
        System.out.println("getAllCruises");
        RestClientCruise instance = new RestClientCruise();
        Collection<CruiseBean> expResult = null;
        Collection<CruiseBean> result = instance.getAllCruises();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCruiseByPlatform method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testGetCruiseByPlatform() throws Exception {
        System.out.println("getCruiseByPlatform");
        VesselBean vessel = null;
        RestClientCruise instance = new RestClientCruise();
        Collection<CruiseBean> expResult = null;
        Collection<CruiseBean> result = instance.getCruiseByPlatform(vessel);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of CruiseToDTO method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testCruiseToDTO() throws ConnectException, EarsException {
        System.out.println("CruiseToDTO");
        CruiseBean pCruise = null;
        RestClientCruise instance = new RestClientCruise();
        CruiseDTO expResult = null;
        CruiseDTO result = instance.CruiseToDTO(pCruise);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public static CruiseBean getTestCruise(String identifier) throws ParseException {
        CruiseBean c = new CruiseBean();
        c.setArrivalHarbor("SDN:C38::BSH4510");
        c.setDepartureHarbour("SDN:C38::BSH4510");
        c.setIdentifier(identifier);
        c.setName(identifier);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start = formatter.parse("2020-03-04");
        Date end = formatter.parse("2020-03-08");
        c.setdStartDate(start);
        c.setdEndDate(end);
        c.setObjectives("The objectives of the cruise are twofold: 1) to validate the modeling efforts of the last 2 years using the COHERENS model. Rubber ducks will be released. 2) to do fisheries monitoring");
        c.setCollateCentre("SDN:EDMO::1778");
        c.setPlatform("SDN:C17::11BE");
        //  List<String> p02 = Arrays.asList(new String[]{"SDN:P02::VDFC", "SDN:P02::LGCR", "SDN:P02::ZNTX"});
        List<Person> chiefScientists = Arrays.asList(new Person[]{new Person("Katrijn", "Baetens", "SDN:EDMO::3330", "KBIN-SUMO", "SDN:C32::BE"), new Person("Valérie", "Dulière", "SDN:EDMO::3330", "KBIN-SUMO", "SDN:C32::BE")});
        Set<SeaAreaBean> seaAreas = new HashSet(Arrays.asList(new SeaAreaBean[]{new SeaAreaBean("SDN:C19::1_2", "SDN:C19::1_2", "North Sea")}));

        c.setChiefScientists(chiefScientists);
        c.setSeaAreas(seaAreas);
        return c;
    }

    /**
     * Test of postCruise method, of class RestClientCruise.
     */
    @Test
    public void testPostCruise() throws ConnectException, EarsException, ParseException {
        System.out.println("postCruise");
        CruiseBean c = getTestCruise("BE11_2020-04");

        RestClientCruise instance = new RestClientCruise();
        IResponseMessage expResult = null;
        IResponseMessage result = instance.postCruise(c);
        int a = 5;
        // assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of modifyCruise method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testModifyCruise() throws ConnectException, EarsException {
        System.out.println("modifyCruise");
        CruiseBean pCruise = null;
        RestClientCruise instance = new RestClientCruise();
        IResponseMessage expResult = null;
        IResponseMessage result = instance.modifyCruise(pCruise);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeCruise method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testRemoveCruise_String() throws ConnectException, EarsException {
        System.out.println("removeCruise");
        String realId = "";
        RestClientCruise instance = new RestClientCruise();
        IResponseMessage expResult = null;
        IResponseMessage result = instance.removeCruise(realId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeCruise method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testRemoveCruise_CruiseBean() throws ConnectException, EarsException {
        System.out.println("removeCruise");
        CruiseBean pCruise = null;
        RestClientCruise instance = new RestClientCruise();
        IResponseMessage expResult = null;
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
    public void testRemoveCruise_String_String() throws ConnectException, EarsException {
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
    public void testGetCruiseByDate() throws Exception {
        System.out.println("getCruiseByDate");
        OffsetDateTime timeStamp = null;
        VesselBean vessel = null;
        RestClientCruise instance = new RestClientCruise();
        CruiseBean expResult = null;
        CruiseBean result = instance.getCruiseByDate(timeStamp, vessel);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCruisesBetweenDates method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testGetCruisesBetweenDates() throws Exception {
        System.out.println("getCruisesBetweenDates");
        String platformCode = "";
        OffsetDateTime start = null;
        OffsetDateTime stop = null;
        RestClientCruise instance = new RestClientCruise();
        List<CruiseBean> expResult = null;
        List<CruiseBean> result = instance.getCruisesBetweenDates(platformCode, start, stop);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getConcurrentCruises method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testGetConcurrentCruises() throws Exception {
        System.out.println("getConcurrentCruises");
        CruiseBean test = null;
        RestClientCruise instance = new RestClientCruise();
        List<CruiseBean> expResult = null;
        List<CruiseBean> result = instance.getConcurrentCruises(test);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cruiseIsConcurrent method, of class RestClientCruise.
     */
    @Test
    @Ignore
    public void testCruiseIsConcurrent() throws Exception {
        System.out.println("cruiseIsConcurrent");
        CruiseBean test = null;
        RestClientCruise instance = new RestClientCruise();
        boolean expResult = false;
        boolean result = instance.cruiseIsConcurrent(test);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
