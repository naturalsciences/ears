/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ontology.writer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author thomas
 */
public class StringUtilsTest {

    public StringUtilsTest() {
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
     * Test of concatString method, of class StringUtils.
     */
    @Test
    public void testConcatString() {

    }

    /**
     * Test of countOccurences method, of class StringUtils.
     */
    @Test
    public void testCountOccurences() {

    }

    /**
     * Test of parseUrn method, of class StringUtils.
     */
    @Test
    public void testParseUrn() {

    }

    /**
     * Test of getLastUrnPart method, of class StringUtils.
     */
    @Test
    public void testGetLastUrnPart() {

    }

    /**
     * Test of mappifyPropertyString method, of class StringUtils.
     */
    @Test
    public void testMappifyPropertyString() {

    }

    /**
     * Test of returnMostSpecificDate method, of class StringUtils.
     */
    @Test
    public void testReturnMostSpecificDate() {
        System.out.println("returnMostSpecificDate");
        Map<SimpleDateFormat, String> dates = new HashMap();

        dates.put(StringUtils.SDF_SIMPLE_DATE, "20170530");//yyyyMMdd
        dates.put(StringUtils.SDF_FULL_ISO_DATETIME, "2017-05-30T13:54:02.866Z");//yyyy-MM-dd'T'HH:mm:ss.SSSX

        //  Date expResult = date2;
        Date result = StringUtils.returnMostSpecificDate(dates);
        Date expResult = null;
        try {
            expResult = StringUtils.SDF_FULL_ISO_DATETIME.parse("2017-05-30T13:54:02.866Z");
        } catch (ParseException ex) {
            fail("Couldn't parse");
        }
        assertEquals(expResult, result);

        dates.put(StringUtils.SDF_SIMPLE_DATE, "20170530");//yyyyMMdd
        dates.put(StringUtils.SDF_FULL_ISO_DATETIME, null);//yyyy-MM-dd'T'HH:mm:ss.SSSX

        result = StringUtils.returnMostSpecificDate(dates);
        try {
            expResult = StringUtils.SDF_SIMPLE_DATE.parse("20170530");
        } catch (ParseException ex) {
            fail("Couldn't parse");
        }
        assertEquals(expResult, result);

        dates.put(StringUtils.SDF_SIMPLE_DATE, null);//yyyyMMdd
        dates.put(StringUtils.SDF_FULL_ISO_DATETIME, "2017-05-30T13:54:02.866Z");//yyyy-MM-dd'T'HH:mm:ss.SSSX

        result = StringUtils.returnMostSpecificDate(dates);
        try {
            expResult = StringUtils.SDF_FULL_ISO_DATETIME.parse("2017-05-30T13:54:02.866Z");
        } catch (ParseException ex) {
            fail("Couldn't parse");
        }
        assertEquals(expResult, result);

        dates.put(StringUtils.SDF_SIMPLE_DATE, null);//yyyyMMdd
        dates.put(StringUtils.SDF_FULL_ISO_DATETIME, null);//yyyy-MM-dd'T'HH:mm:ss.SSSX

        //  Date expResult = date2;
        result = StringUtils.returnMostSpecificDate(dates);

        assertEquals(null, result);

    }

    /**
     * Test of parse method, of class StringUtils.
     */
    @Test
    public void testParse() {

    }

}
