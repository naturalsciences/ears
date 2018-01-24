/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.rdf;

import be.naturalsciences.bmdc.ears.ontology.treeviewer.OntologyTreeViewer;
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
        id = "be.naturalsciences.bmdc.ears.ontology.rdf.OpenAction"
)
@ActionRegistration(
        displayName = "#CTL_OpenAction"
)
@ActionReference(path = "Loaders/application/rdf+xml/Actions", position = 0)
@Messages("CTL_OpenAction=Browse...")
public final class OpenRdfFileTypeActionBrowse implements ActionListener {

    private final RdfFileTypeDataObject context;

    public OpenRdfFileTypeActionBrowse(RdfFileTypeDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        TopComponent tc;
        try {
            tc = new OntologyTreeViewer(context);
            Mode editor = WindowManager.getDefault().findMode("explorer");
            editor.dockInto(tc);
            tc.open();
            tc.requestActive();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            throw new RuntimeException();
        }

    }
}
