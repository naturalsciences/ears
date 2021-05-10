/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.gui;

import be.naturalsciences.bmdc.ears.ontology.gui.AsConceptNode.PrintChoice;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CookieAction;

@ActionID(
        category = "Build",
        id = "be.naturalsciences.bmdc.ears.ontology.gui.CopyNodeAsUriAction")
@ActionRegistration(
        displayName = "#CTL_CopyNodeAsUriAction", lazy = false)

@NbBundle.Messages("CTL_CopyNodeAsUriAction=Copy identifiers")
public class CopyNodeAsUriAction extends CookieAction {

    private final Lookup context;

    public CopyNodeAsUriAction() {
        context = Utilities.actionsGlobalContext();
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        return true;
    }

    @Override
    protected boolean surviveFocusChange() {
        return false;
    }

    @Override
    protected Class[] cookieClasses() {
        return new Class[]{AsConceptNode.class};
    }

    @Override
    protected int mode() {
        return org.openide.util.actions.CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            AsConceptNode node = (AsConceptNode) n;
            StringSelection ss = new StringSelection(node.printTable(PrintChoice.BOTH));
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(ss, null);
        }
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public String
            getName() {
        return NbBundle.getMessage(CreateEventAction.class,
                "CTL_CopyNodeAsUriAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("Halp!");
    }

}
