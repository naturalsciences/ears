/*
 * To change this license header, choose License Headers in ProjectBean Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package od.nature.naturalsciences.be.bmdc.services.cruise;

import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.entities.ProjectBean;
import be.naturalsciences.bmdc.ears.rest.RestClientProgram;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author yvan http://www.slf4j.org/codes.html
 * http://stackoverflow.com/questions/7685510/log4j-warning-while-initializing
 *
 *
 *
 */
public class TestRestClientProgram extends TestCase {
    /*
     Ensemble des objets utilisés dans plusieurs 
     fonctions d une même classe de test
     */

    //private Program program;
    private ProgramBean program;
    private RestClientProgram restClientProgram;
    private Collection<ProgramBean> collectionsProgram;

    private Collection<ProjectBean> collectionsprojects;

    @Override
    /*
     Chaque objet utilisé est alors déclaré en tant que variuable d instance de la classe de test
     initialisé dans la methode setUp() et eventuellement libere dans la methode tearDown
     */
    protected void setUp() throws Exception { //setUp() method which runs before every test invocation.
        program = new ProgramBean();
        restClientProgram = new RestClientProgram();
    }

    @Override
    protected void tearDown() throws Exception {
        program = null;
        restClientProgram = null;
    }

    @Test
    public void testPostProgramByParamsBean() {

        //BasicConfigurator.configure();
        ProgramBean programToSend = new ProgramBean();
  //           ProgramBean programToSendBe = new ProgramBean();

        programToSend.setCruiseId("fin");
        programToSend.setProgramId("fin");
        programToSend.setOriginatorCode("SDN:EDMO::2287");
        programToSend.setDescription("modifyCruisemodifyCruisemodifyCruisemodifyCruise");
        programToSend.setPiName("Principal Investigator");

        Set<ProjectBean> collectionProjects = new HashSet();

        ProjectBean projectsAssociatedToProgram = new ProjectBean();
        projectsAssociatedToProgram.setId("1");
        projectsAssociatedToProgram.setCode("yvan");

        ProjectBean projectsAssociatedToProgram1 = new ProjectBean();
        projectsAssociatedToProgram1.setId("2");
        projectsAssociatedToProgram1.setCode("yvan2");

        collectionProjects.add(projectsAssociatedToProgram);
        collectionProjects.add(projectsAssociatedToProgram1);

        programToSend.setProjects(collectionProjects);

//          programToSendBe.setCruiseId("yvan");
        //   programToSendBe.setProgramId("yoyo");
        restClientProgram.postProgram(programToSend);
   //    restClientProgram.postProgramByParamsVoid(programToSendBe);

    }

}
