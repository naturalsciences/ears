/*
 * To change this license header, choose License Headers in ProjectBean Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package od.nature.naturalsciences.be.bmdc.services.cruise;

import be.naturalsciences.bmdc.ears.entities.Person;
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
 * @author Yvan Stojanov
 */
public class TestRestClientProgram extends TestCase {

    private ProgramBean program;
    private RestClientProgram restClientProgram;
    private Collection<ProgramBean> collectionsProgram;

    private Collection<ProjectBean> collectionsprojects;

    @Override
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
        ProgramBean programToSend = new ProgramBean();

        programToSend.setProgramId("fin");
        programToSend.setDescription("modifyCruisemodifyCruisemodifyCruisemodifyCruise");
        programToSend.getPrincipalInvestigators().add(new Person("Essai", "Tester", "SDN:EDMO::2287", null, null));
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
