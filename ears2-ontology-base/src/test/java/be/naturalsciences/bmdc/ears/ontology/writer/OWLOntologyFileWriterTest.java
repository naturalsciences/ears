/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.writer;

import be.naturalsciences.bmdc.ontology.writer.OntologyFileFormat;
import be.naturalsciences.bmdc.ontology.writer.OntologyFileWriter;
import java.io.BufferedOutputStream;
import java.nio.file.Path;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 *
 * @author Thomas Vandenberghe
 */
public class OWLOntologyFileWriterTest {

    public OWLOntologyFileWriterTest() {
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
     * Test of createOntoFile method, of class OntologyFileWriter.
     */
    @Test
    @Ignore
    public void testCreateOntoFile() throws Exception {
        System.out.println("createOntoFile");
        OWLOntology onto = null;
        OntologyFileFormat outputFormat = null;
        Path fullPath = null;
        String perm = "";
        String group = "";
        String owner = "";
        OntologyFileWriter instance = new OntologyFileWriter();
        BufferedOutputStream expResult = null;
        BufferedOutputStream result = instance.createOntoFile(onto, OntologyFileFormat.RDF_FORMAT, fullPath, owner, perm, group);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createOntologyAxiomFile method, of class OntologyFileWriter.
     */
   // @Test
   // @Ignore
   /* public void testCreateOntologyAxiomFile() throws OWLOntologyCreationException {
        System.out.println("createOntologyAxiomFile");
        try {
            EARSOntologyCreator creator = new EARSOntologyCreator(ScopeMap.PROGRAM_SCOPE, "Tryout ontology");
            fail("My method didn't throw when I expected it to");
        } catch (IllegalArgumentException e) {

        }
        ScopeMap scope = ScopeMap.PROGRAM_SCOPE;
        scope.put(ScopeMap.Scope.PROGRAM, "ProgramID6546");
        EARSOntologyCreator creator2 = new EARSOntologyCreator(scope, "tryout ProgramID6546 ontology");
        OntologyFileWriter writer = new OntologyFileWriter(creator2.getManager());
        OWLOntology earsOnto = creator2.createOntology(EARSOntologyCreator.LoadOnto.IMPORT);
        BufferedOutputStream createOntoFile = writer.createOntoFile(earsOnto, OntologyFileFormat.RDF_FORMAT, Paths.get("/home/thomas/Desktop", "test_rdf.rdf"), null, null, null);
        assertNotEquals(createOntoFile, null);

    }*/

}
