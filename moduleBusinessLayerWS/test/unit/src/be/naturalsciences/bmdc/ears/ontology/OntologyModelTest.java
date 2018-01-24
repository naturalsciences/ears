/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

import be.naturalsciences.bmdc.ears.ontology.gui.AsConceptNode;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import java.io.File;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.nodes.Node;

/**
 *
 * @author thomas
 */
public class OntologyModelTest {

    public OntologyModelTest() {
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
     * Test of setName method, of class OntologyModel.
     */
    AsConcept thingToBeDropped = null;

    public void testClone(File f) throws IOException, CloneNotSupportedException {

        IOntologyModel ontModel = OntologyFactory.getOntology(f);

        ontModel.open(OntologyNodes.DEFAULT_ORDER, IOntologyModel.Action.BROWSING);
        Set<AsConcept> individuals = ontModel.getNodes().getIndividuals(false);
        IdentityHashMap ihm = new IdentityHashMap();
        for (AsConcept individual : individuals) {
            if (individual.getId() == 10) {
                thingToBeDropped = individual;
            }
            AsConcept clone = individual.clone(ihm);
            assertTrue(clone.equals(individual));

        }

    }

    @Test
    public void testOldVesselOntology() throws IOException, CloneNotSupportedException {
        File f = new File("/home/thomas/NetBeansProjects/PlatformEARS/build/testuserdir/config/onto/earsv2-onto-vessel.rdf");
        testClone(f);
    }

    @Test
    public void testBaseOntology() throws IOException, CloneNotSupportedException {
        File f = new File("/home/thomas/NetBeansProjects/PlatformEARS/build/testuserdir/config/onto/earsv2-onto.rdf");
        testClone(f);
    }

    @Test
    public void testProgramOntology() throws IOException, CloneNotSupportedException {
        File f = new File("/home/thomas/NetBeansProjects/moduleBusinessLayerWS/test/unit/src/resources/test-pr-ontology.rdf");
        testClone(f);
    }

   // @Test
    public void testVisualOntology() throws IOException, CloneNotSupportedException {
        File f = new File("/home/thomas/NetBeansProjects/moduleBusinessLayerWS/test/unit/src/resources/test-pr-ontology.rdf");
        IOntologyModel ontModel = OntologyFactory.getOntology(f);

        ontModel.open(OntologyNodes.DEFAULT_ORDER, IOntologyModel.Action.BROWSING);
        AsConceptNode.ContextBehaviour beh = AsConceptNode.EDIT_BEHAVIOUR;

        beh.factory = new AsConceptFactory(ontModel);

        AsConceptNode rootNode = new AsConceptNode(ontModel, beh);
        Node[] nodes = rootNode.getChildren().getNodes();
        Set<Node> allChildren = AsConceptNode.getAllChildren(rootNode);
        for (Node node : allChildren) {
            AsConceptNode conceptNode = (AsConceptNode) node;
            if (conceptNode.getConcept().getUrn().toString().equals("SDN:L22::NETT0014")) {
                be.naturalsciences.bmdc.ears.ontology.entities.Process processToBeDropped = (be.naturalsciences.bmdc.ears.ontology.entities.Process) thingToBeDropped;
                conceptNode.getDropType(processToBeDropped, 0, 0);
            }

        }
    }

}
