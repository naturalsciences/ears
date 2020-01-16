/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.rdf;

import be.naturalsciences.bmdc.ears.entities.CurrentProgram;
import be.naturalsciences.bmdc.ears.entities.CurrentSettings;
import be.naturalsciences.bmdc.ears.ontology.TestOntology;
import be.naturalsciences.bmdc.ears.properties.Constants;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.writer.EARSOntologyCreator;
import be.naturalsciences.bmdc.ontology.writer.EARSOntologyCreator.LoadOnto;
import be.naturalsciences.bmdc.ontology.writer.ScopeMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import org.netbeans.core.api.multiview.MultiViews;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 *
 * @author Thomas Vandenberghe
 */
@ActionID(id = "be.naturalsciences.bmdc.ears.ontology.rdf.NewProgramRdfFileTypeAction", category = "File")
@ActionRegistration(displayName = "#CTL_NewProgramRdfFileTypeAction")
@ActionReference(path = "Menu/File", position = 10)
@Messages("CTL_NewProgramRdfFileTypeAction=New program tree")
public class NewProgramRdfFileTypeAction implements ActionListener {

    private RdfFileTypeDataObject context;

    //private final ScopeMap scope = ScopeMap.PROGRAM_SCOPE;

    /*public NewProgramRdfFileTypeAction() {

     }*/
    @Override
    public void actionPerformed(ActionEvent e) {
        CurrentProgram currentProgram = Utilities.actionsGlobalContext().lookup(CurrentProgram.class);
        CurrentSettings settings = Utilities.actionsGlobalContext().lookup(CurrentSettings.class);
       /* Boolean allowTestOnto = null;
        if (settings != null) {
            allowTestOnto = settings.getConcept().get("test_onto");
        }*/
        ScopeMap scope = null;
        String filename = null;
        String programName = null;
        if (currentProgram != null) {
            programName = currentProgram.getConcept().cleanName();
            scope = ScopeMap.PROGRAM_SCOPE;
            scope.put(ScopeMap.Scope.PROGRAM, programName);
            filename = programName + ".rdf";
        } /*else if (allowTestOnto != null && allowTestOnto.booleanValue()) {
            scope = ScopeMap.TEST_SCOPE;
            scope.put(ScopeMap.Scope.TEST, "");
            filename = TestOntology.getPreferredName();
        }*/
        if (scope != null) {
            EARSOntologyCreator creator = new EARSOntologyCreator(scope, "Program tree of " + programName);
//File tempFile = new File(System.getProperty("java.io.tmpdir"), "untitled.rdf");

            Constants.PROGRAM_ONTOLOGY_DIR.mkdirs();

            File tempFile = new File(Constants.PROGRAM_ONTOLOGY_DIR, filename);
            try {
                //FileUtils.createFile(tempFile.toPath(), "rdf", null, null, 8 * 1024);

                creator.createOntoFile(LoadOnto.PASTE, new File(Constants.ACTUAL_LOCAL_ONTOLOGY_AXIOM_LOCATION), 0, tempFile.toPath(), null, null, null);
            } catch (OWLOntologyCreationException ex) {
                Exceptions.printStackTrace(ex);
            }
            try {
                FileObject templateFo = FileUtil.createData(tempFile);
                context = (RdfFileTypeDataObject) RdfFileTypeDataObject.find(templateFo);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            Messaging.report("There is no current program selected. Please (create and) select the current program first.", Message.State.BAD, this.getClass(), true);
        }
        if (context != null) {
            if (RdfFileTypeDataObject.nbFilesOpen == 0) {
                TopComponent tc = MultiViews.createMultiView("application/rdf+xml", context);

                tc.setDisplayName(context.getName());
                tc.open();
                tc.requestActive();
                RdfFileTypeDataObject.nbFilesOpen++;
            } else {
                String msg = "Only one tree file can be edited at a time.";
                NotifyDescriptor nd = new NotifyDescriptor.Message(msg, NotifyDescriptor.INFORMATION_MESSAGE);
                DialogDisplayer.getDefault().notify(nd);
            }
        }
    }

}
