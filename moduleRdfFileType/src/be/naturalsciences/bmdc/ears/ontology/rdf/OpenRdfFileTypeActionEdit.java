/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.rdf;

import be.naturalsciences.bmdc.ears.entities.CurrentUser;
import be.naturalsciences.bmdc.ears.rest.RestClientOnt;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(
        category = "File",
        id = "be.naturalsciences.bmdc.ears.ontology.rdf.OpenRdfFileTypeActionEdit"
)
@ActionRegistration(
        iconBase = "be/naturalsciences/bmdc/ears/ontology/rdf/icon.png",
        displayName = "#CTL_OpenRdfFileTypeActionEdit"
)
@ActionReferences({
    @ActionReference(path = "Loaders/application/rdf+xml/Actions", position = 1)
})
@Messages("CTL_OpenRdfFileTypeActionEdit=Edit...")
public final class OpenRdfFileTypeActionEdit implements ActionListener {

    private final RdfFileTypeDataObject context;
    private final boolean bypass = true; //bypasses any credential check so a file can be opened anyway

    public OpenRdfFileTypeActionEdit(RdfFileTypeDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String failMsg = null;
        CurrentUser currentUser = Utilities.actionsGlobalContext().lookup(CurrentUser.class);
        if (context.getOntModel().isPasswordProtected()) {
            if (!bypass && currentUser != null && currentUser.getConcept() != null) {
                RestClientOnt client = null;

                try {
                    client = new RestClientOnt();
                } catch (ConnectException ex) {
                    failMsg = "This tree is write-protected and cannot be edited when the EARS web server is unreachable.";
                } catch (EarsException ex) {
                    failMsg = "This tree is write-protected and cannot be edited when the url for the EARS web server is empty or invalid.";
                }
                if (failMsg == null) {
                    boolean authenticated = false;
                    try {
                        if (client != null) {
                            authenticated = client.authenticate(currentUser.getConcept());
                        }
                    } catch (ConnectException ex) {
                        failMsg = "This tree is write-protected and cannot be edited when the EARS web server is unreachable.";
                    }
                    if (!authenticated) {
                        failMsg = "This tree is write-protected. You did not provide the correct credentials in the Settings to edit it.";
                    }
                }

            } else if (!bypass) {
                failMsg = "This tree is write-protected. No credentials are registered in the Settings.";
            }
        }
        if (failMsg == null && RdfFileTypeDataObject.nbFilesOpen > 0) {
            failMsg = "Only one tree file can be edited at a time.";
        }
        if (failMsg == null) {
            context.open();
        } else {
            NotifyDescriptor nd = new NotifyDescriptor.Message(failMsg, NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(nd);
        }
    }
}
