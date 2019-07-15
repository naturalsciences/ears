/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package od.nature.naturalsciences.be.bmdc.services.cruise;

import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.Person;
import be.naturalsciences.bmdc.ears.entities.SeaAreaBean;
import be.naturalsciences.bmdc.ears.rest.RestClientCruise;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.openide.util.Exceptions;

/**
 *
 * @author Yvan Stojanov http://www.slf4j.org/codes.html
 * http://stackoverflow.com/questions/7685510/log4j-warning-while-initializing
 *
 *
 *
 */
public class TestRestClientCruise extends TestCase {
    /*
     Ensemble des objets utilisés dans plusieurs 
     fonctions d une même classe de test
     */

    private CruiseBean cruiseBean;
    private RestClientCruise clientCruise;
    private Collection<CruiseBean> collectionsCruise;

    @Override
    /*
     Chaque objet utilisé est alors déclaré en tant que variuable d instance de la classe de test
     initialisé dans la methode setUp() et eventuellement libere dans la methode tearDown
     */
    protected void setUp() throws Exception { //setUp() method which runs before every test invocation.
        cruiseBean = new CruiseBean();
        clientCruise = new RestClientCruise();
    }

    @Override
    protected void tearDown() throws Exception {
        cruiseBean = null;
        clientCruise = null;
    }

    @Test
    @Ignore
    public void testGetAllCruises() {
        //

        assertEquals(1, 1);

        try {
            //BasicConfigurator.configure();
            collectionsCruise = clientCruise.getAllCruises();
        } catch (ConnectException ex) {
            Exceptions.printStackTrace(ex);
        }
        for (CruiseBean iCruiseBean : collectionsCruise) {
            cruiseBean.setCruiseName(iCruiseBean.getCruiseName());
            assertEquals(iCruiseBean.getCruiseName(), cruiseBean.getCruiseName());
            assert (iCruiseBean.getCruiseName() instanceof String);
            assert (iCruiseBean.getCruiseName().length() > 0);

        }

    }
    //id=2016-12-11_TEST-23&cruiseName=TEST-23&startDate=2016-12-11T00:00:00&endDate=2016-12-12T00:00:00&chiefScientist=%5B%7B%22name%22:%22Ahmed%2BAl%2BVerroes%22%2C%22organisationCode%22:%22SDN:EDMO::1231%22%2C%22organisationName%22:%22Ecole%2BNationale%2BSuperieure%2Bdes%2BSciences%2Bde%2Bla%2BMer%2Bet%2Bde%2Bl%2BAmenagement%2Bdu%2BLittoral%2B-%2BENSSMAL%22%2C%22country%22:%22Algeria%22%7D%5D&platformCode=SDN:C17::742Y&platformClass=SDN:L06::0&objectives=&collateCenter=SDN:EDMO::1822&startingHarbor=SDN:C38::BSH6052&arrivalHarbor=SDN:C38::BSH33&seaAreas=SDN:C19::10_10

    @Test
    public void testPostCruise() throws ParseException,  ConnectException, EarsException, MalformedURLException {
        RestClientCruise client = new RestClientCruise();

        for (int i = 0; i < 200; i++) {
            String cruiseName = "TEST-" + i;
            client.removeCruise(cruiseName);

            Calendar c = Calendar.getInstance();
            c.set(2000 + i % 37, i % 12, i % 26);
            Date sdate = c.getTime();
            c.set(2000 + i % 37, i % 12, i + 1 % 26);
            Date edate = c.getTime();
            CruiseBean cruise = new CruiseBean();
            cruise.setArrivalHarbor("SDN:C38::BSH10");
            //Person p0 = new Person("Jorge-Pilár de la Ternéras", "SDN:EDMO::1822", "Naval Hydrographic Service of Ar", "Argent");
            Person p1 = new Person("Gérard Lachaumet-Desrumaux", "SDN:EDMO::1822", "Centre Nationale de la Récherche", "France");
            //Person p2 = new Person("Gérard Lachaumet-Desrumaux", "SDN:EDMO::1822", "Centre Nationale de la Récherche", "France");
            //Person p2 = new Person("Olivia Mortimer-McIntosh", "SDN:EDMO::5224", "Eodhainn Sibhann Ddu Maris", "'Éire");
            List<Person> ps = new ArrayList();
            ps.add(p1);
           // ps.add(p2);
            cruise.setChiefScientists(ps);
            int length = URLEncoder.encode(cruise.getChiefScientist()).length();
            /*String amount = Integer.toString(i * 10);
            String scientistName = StringUtils.rightPad(amount + ":", (i * 10) + amount.length()+1, 'a');
            cruise.setChiefScientist(scientistName);
            int length2 = URLEncoder.encode(cruise.getChiefScientist()).length();*/
            cruise.setRealId(cruiseName);
            cruise.setCollateCenter("SDN:EDMO::1520");
            cruise.setCruiseName(cruiseName);
            cruise.setdStartDate(sdate);
            cruise.setdEndDate(edate);
            cruise.setPlatformClass("SDN:L06::0");
            cruise.setPlatformCode("SDN:C17::742Y");
            Set seas = new HashSet();
            seas.add(new SeaAreaBean("SDN:C19::10_10", "SDN:C19::10_10", "The sea"));
            cruise.setSeaAreas(seas);
            cruise.setStartingHarbor("SDN:C38::BSH10");
            IResponseMessage msg = client.postCruise(cruise);
            if (msg.isBad()) {
                String summary = msg.getSummary();
            }
            int a = 5;
            //platformCode=SDN:C17::742Y&platformClass=SDN:L06::0
            //collateCenter=SDN:EDMO::1822&startingHarbor=SDN:C38::BSH6052&arrivalHarbor=SDN:C38::BSH33
        }

    }
}
