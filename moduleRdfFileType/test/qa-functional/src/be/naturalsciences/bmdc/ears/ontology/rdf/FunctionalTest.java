/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.rdf;

import org.netbeans.junit.*;

/**
 *
 * @author Thomas Vandenberghe
 */
public class FunctionalTest extends NbTestCase {

    public FunctionalTest(java.lang.String testName) {
        super(testName);
    }

    public void testSave() {
//        int i = (int) (Math.random() * 1000);
//        int j = (int) (Math.random() * 1000);
//        try {
//            File f1a = new File("/home/thomas/NetBeansProjects/PlatformEARS/trees/earsv2-onto-belgica-test.rdf");
//            File f3 = new File("/home/thomas/NetBeansProjects/PlatformEARS/trees/earsv2-onto.rdf");
//
//            File f1b = new File("/home/thomas/NetBeansProjects/PlatformEARS/trees/earsv2-onto-belgica-test." + i + ".rdf");
//
//            File f1c = new File("/home/thomas/NetBeansProjects/PlatformEARS/trees/earsv2-onto-belgica-test." + j + ".rdf");
//            OntologyModel ontModel1a = new OntologyModel<>(f1a, ToolCategory.class, false);
//
//            OntologyModel ontModel3 = new OntologyModel<>(f3, ToolCategory.class, false);
//            List<ToolCategory> ltc1a = ontModel1a.getNodes();
//            List<ToolCategory> ltc3 = ontModel3.getNodes();
//            assertTrue(ltc1a.size() < ltc3.size());
//
//            ontModel1a.saveAs(f1c.toPath());
//
//            assertTrue(f1c.length() < f1a.length() + 100 && f1c.length() > f1a.length() - 100);//should result in roughly the same file!
//            OntologyModel ontModel1c = new OntologyModel<>(f1c, ToolCategory.class, false);
//            assertTrue(ontModel1a.getModel().isIsomorphicWith(ontModel1c.getModel()));//should be isomorphic!
//
//            Set<ToolCategory> stc1 = new LinkedHashSet(ontModel1a.getNodes());
//            stc1.addAll(ltc3);
//            List<ToolCategory> tcl = null;
//            Tool t = null;
//            for (ToolCategory tc : stc1) {
//                if (tc.getChildren(null).size() > 0 && t == null) {
//                    t = (Tool) new ArrayList(tc.getChildren(null)).get(0);
//                }
//                if (tc.getTermRef().getPrefLabel().equals("current profilers") || tc.getTermRef().getPrefLabel().equals("transmissometers")) {
//                    tc.addToChildren(null, t, false);//TODO set model
//                    tcl.add(tc);
//                }
//            }
//            assertTrue(tcl.get(0).getChildren(null).contains(t) && tcl.get(1).getChildren(null).contains(t));

            /*try {
             ontModel1a.saveAs(f1b.toPath());
             } catch (IOException ex) {
             Exceptions.printStackTrace(ex);
             }
            
             OntologyModel ontModel1b = new OntologyModel<>(f1b, ToolCategory.class);*/
           // ontModel1b.
            //List<ToolCategory> ltc1b  = ontModel1b.getNodes();
            /*try {
             ontModel1a.saveAs(f1b.toPath());
             } catch (IOException ex) {
             Exceptions.printStackTrace(ex);
             }
             OntologyModel ontModel1b = new OntologyModel<>(f1b, ToolCategory.class);
             List<ToolCategory> ltc1b = ontModel1b.getNodes();*/
            //assertTrue(f1b.length() >= f1a.length());
            //assertTrue(ltc1a.size() < ltc1b.size());
//        } catch (FileNotFoundException ex) {
//            Exceptions.printStackTrace(ex);
//        } catch (OWLOntologyCreationException ex) {
//            Exceptions.printStackTrace(ex);
//        }

    }

}
