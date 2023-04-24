/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.rdf;

import be.naturalsciences.bmdc.ears.ontology.TestOntology;
import be.naturalsciences.bmdc.ears.properties.Constants;
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
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 *
 * @author Thomas Vandenberghe
 */
@ActionID(id = "be.naturalsciences.bmdc.ears.ontology.rdf.NewTestRdfFileTypeAction", category = "File")
@ActionRegistration(displayName = "#CTL_NewTestRdfFileTypeAction")
@ActionReference(path = "Menu/File", position = 15)
@Messages("CTL_NewTestRdfFileTypeAction=New test tree")
public class NewTestRdfFileTypeAction implements ActionListener {

    private RdfFileTypeDataObject context;

    @Override
    public void actionPerformed(ActionEvent e) {
        ScopeMap scope = null;
        String filename = null;

        scope = ScopeMap.TEST_SCOPE;
        scope.put(ScopeMap.Scope.TEST, "");
        filename = TestOntology.getPreferredName() + ".rdf";

        if (scope != null) {
            EARSOntologyCreator creator = new EARSOntologyCreator(scope, "Test tree of " + filename);
            Constants.PROGRAM_ONTOLOGY_DIR.mkdirs();
            File tempFile = new File(Constants.PROGRAM_ONTOLOGY_DIR, filename);
            try {
                creator.createOntoFile(LoadOnto.PASTE, new File(Constants.ACTUAL_LOCAL_ONTOLOGY_AXIOM_LOCATION), 0, tempFile.toPath(), null, null, null,true);
            } catch (OWLOntologyCreationException ex) {
                Messaging.report("Could not create test ontology file.", ex, this.getClass(), true);
            }
            try {
                FileObject templateFo = FileUtil.createData(tempFile);
                context = (RdfFileTypeDataObject) RdfFileTypeDataObject.find(templateFo);
            } catch (IOException ex) {
                Messaging.report("Could not create test ontology file.", ex, this.getClass(), true);
            }
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
