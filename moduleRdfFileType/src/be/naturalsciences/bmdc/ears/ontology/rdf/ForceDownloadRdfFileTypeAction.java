/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.rdf;

import be.naturalsciences.bmdc.ears.ontology.OntologySynchronizer;
import be.naturalsciences.bmdc.ears.ontology.treeviewer.OntologyTreeViewer;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "File",
        id = "be.naturalsciences.bmdc.ears.ontology.rdf.ForceDownloadAction"
)
@ActionRegistration(
        displayName = "#CTL_ForceDownloadAction"
)
@ActionReference(path = "Loaders/application/rdf+xml/Actions", position = 2)
@Messages("CTL_ForceDownloadAction=Force download (loses unsaved changes!)...")
public final class ForceDownloadRdfFileTypeAction implements ActionListener {

    private final RdfFileTypeDataObject context;

    public ForceDownloadRdfFileTypeAction(RdfFileTypeDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        try {
            OntologySynchronizer.synchronizeOntology(context.getFile(), true);
            context.refresh(false);
        } catch (IOException | EarsException ex) {
            Messaging.report("There was a problem force downloading this tree.", ex, ForceDownloadRdfFileTypeAction.class, true);
        }
    }
}
