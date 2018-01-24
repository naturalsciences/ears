/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package od.nature.naturalsciences.be.bmdc.services.event;

import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.EventBean;
import be.naturalsciences.bmdc.ears.entities.MessageBean;
import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.rest.RestClientCruise;
import be.naturalsciences.bmdc.ears.rest.RestClientEvent;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.io.File;
import java.net.ConnectException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
public class RestClientEventTest {

    private ClassLoader classLoader;

    //private OntologyModel baseTestModel;
    //private OntologyModel belgicaTestModel;
    // Collection<ToolCategory> toolCatColl;
    public RestClientEventTest() {
    }

    @Before
    public void setUp() throws Exception {
        classLoader = getClass().getClassLoader();
        File f1 = new File(classLoader.getResource("resources/earsv2-onto-test.rdf").getFile());
        File f2 = new File(classLoader.getResource("resources/earsv2-onto-belgica-test.rdf").getFile());

        // baseTestModel = new OntologyModel<ToolCategory>(f1, ToolCategory.class, false);
        // belgicaTestModel = new OntologyModel<ToolCategory>(f2, ToolCategory.class, false);
        // toolCatColl = baseTestModel.getNodes();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getAllEvents method, of class RestClientEvent.
     */
    @Test
    public void testGetAllEvents() throws ConnectException, EarsException {
        System.out.println("getAllEvents");
        RestClientEvent instance = new RestClientEvent();
        Collection<EventBean> expResult = null;
        Collection<EventBean> result = instance.getAllEvents();
        validEvents(result);
    }

    private void validEvents(Collection<EventBean> events) {
        int fail = 0;
        int fail2 = 0;
        int fail3 = 0;
        for (EventBean e : events) {

            if (e.getTimeStamp() == null || e.getTimeStampDt() == null) {
                fail++;
            }
            if (e.getToolCategoryName() == null) {
                fail2++;
            }
            if (e.getToolSet() == null || e.getToolSet().size() < 1 || new ArrayList(e.getToolSet()).get(0) == null) {
                fail3++;
            }
        }
        assertTrue("timestamp more than 6 times empty!", fail <= 6);
        assertTrue("subjectName empty for some events!", fail2 == 0);
        assertTrue("some events with faulty/absent tools!!", fail3 == 0);
    }

    /**
     * Test of getEvent method, of class RestClientEvent.
     */
    @Test
    public void testGetEvent() throws ConnectException, EarsException {
        System.out.println("getEvent");
        RestClientEvent instance = new RestClientEvent();
        EventBean expResult = null;
        EventBean result = instance.getEvent("EV00005");
        List l = new ArrayList();
        l.add(result);
        validEvents(l);
    }

    /**
     * Test of getEvent method, of class RestClientEvent.
     */
    /*@Test
     public void testGetEventWithProperty() throws ConnectException {
     System.out.println("getEventWithProperty");
     RestClientEvent instance = new RestClientEvent();
     EventBean expResult = null;
     EventBean result = instance.getEvent("A941");
     List l = new ArrayList();
     l.add(result);
     validEvents(l);
     boolean hasEvents = new ArrayList(result.getPropertyMap().get(EventBean.PROPERTY_URLS.get(EventBean.Prop.HAS_DATA))).get(0).equals("true");
     boolean hasAmon = new ArrayList(result.getPropertyMap().get(EventBean.PROPERTY_URLS.get(EventBean.Prop.PARAMETER))).get(0).equals("AMON");

     assertTrue(hasEvents && hasAmon);
     }*/
    /**
     * Test of getEventByProgram method, of class RestClientEvent.
     */
    @Test
    @Ignore
    public void testGetEventByProgram() throws ConnectException, EarsException {
        System.out.println("getEventByProgram");
        RestClientEvent instance = new RestClientEvent();
        Collection<EventBean> expResult = null;
        Collection<EventBean> result = instance.getEventByProgram();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEventByCruise method, of class RestClientEvent.
     */
    @Test
    @Ignore
    public void testGetEventByCruise() throws ConnectException, EarsException {
        System.out.println("getEventByCruise");
        CruiseBean cruise = new CruiseBean();
        RestClientEvent instance = new RestClientEvent();
        Collection<EventBean> expResult = null;
        Collection<EventBean> result = instance.getEventByCruise(cruise);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    /**
     * Test of getEventByDate method, of class RestClientEvent.
     */
    @Test
    @Ignore
    public void testGetEventByDate() throws ConnectException, EarsException {
        System.out.println("getEventByDate");
        String date = "";
        RestClientEvent instance = new RestClientEvent();
        EventBean expResult = null;
        EventBean result = instance.getEventByDate(date);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEventByDates method, of class RestClientEvent.
     */
    @Test
    @Ignore
    public void testGetEventByDates() throws ConnectException, EarsException {
        System.out.println("getEventByDates");
        OffsetDateTime fromDate = null;
        OffsetDateTime toDate = null;
        RestClientEvent instance = new RestClientEvent();
        Collection<EventBean> expResult = null;
        Collection<EventBean> result = instance.getEventByDates(fromDate, toDate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of postEvent method, of class RestClientEvent.
     */
    @Test
    public void testPostEventByParams() throws ConnectException, EarsException {
        System.out.println("postEventByParams");
        RestClientCruise cruiseClient = new RestClientCruise();
        // CruiseBean cruise = cruiseClient.getCruiseByDate(OffsetDateTime.parse("2016-04-07T00:00:00+02:00"));
       /* if (cruise == null) {
         fail("cruise not found within date 2016-04-07T00:00:00+02:00");
         }*/

        ProgramBean currentProgram = new ProgramBean();
//        ToolCategory toolCat = (ToolCategory) baseTestModel.findIndividualConceptFromNodes("http://ontologies.ef-ears.eu/ears2/1#ctg_1");
        //    Tool tool = (Tool) baseTestModel.findIndividualFromNodes(Tool.class, "http://ontologies.ef-ears.eu/ears2/1#dev_1");
        Set tools = new HashSet();
        //    tools.add(tool);
        //    Process process = (Process) new ArrayList(baseTestModel.getIndividuals(Process.class, false)).get(0);
        //    Action action = (Action) new ArrayList(baseTestModel.getIndividuals(Action.class, false)).get(1);
        //   EventBean event = new EventBean(currentProgram, cruise, toolCat, tools, process, action, null, "Oscar Vandewiele");
        //    event.attachProperty(EventBean.Prop.HAS_DATA, "true");
        //    event.attachProperty(EventBean.Prop.PARAMETER, "AMON");
        //EventBean event = new EventBean(toolCat, tool, process, action, null, "Oscar Vandewiele");
        RestClientEvent instance = new RestClientEvent();
        MessageBean expResult = null;
        //  IResponseMessage result = instance.postEvent(event);
        //    if (result.isBad()) {
        fail("response failed: event not created. see log.");
        //   }
        int a = 5;
    }

}
