/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.gui;

import be.naturalsciences.bmdc.ears.utils.Messaging;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CookieAction;

/**
 *
 * @author Thomas Vandenberghe
 */
@ActionID(
        category = "Build",
        id = "be.naturalsciences.bmdc.ears.ontology.gui.DeleteNodeAction")
public class DeleteNodeAction extends CookieAction {

    private final Lookup context;

    public DeleteNodeAction() {
        context = Utilities.actionsGlobalContext();
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        AsConceptNode node = context.lookup(AsConceptNode.class);
        if (node != null) {
            return !node.isRoot();
        } else {
            return false;
        }
        //boolean isRoot = false;
        //  try {
        //   isRoot = node.isRoot();
        //  } catch (NullPointerException e) {
        //      Messaging.report("Method DeleteNodeAction.enable not applicable as node no longer exists.", e, this.getClass(), false);
        //      return false;
        //  }
        //return !isRoot;
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
        return org.openide.util.actions.CookieAction.MODE_ALL;
    }

    @Override
    protected void performAction(Node[] activatedNodes) {

        NotifyDescriptor.Confirmation confirm = new NotifyDescriptor.Confirmation(
                NbBundle.getMessage(AsConceptNode.class, "Action.removeCategory.areyousure"),
                NbBundle.getMessage(AsConceptNode.class, "Action.removeCategory.title"),
                NotifyDescriptor.YES_NO_OPTION);

        Object result = DialogDisplayer.getDefault().notify(confirm);
        if (result != NotifyDescriptor.YES_OPTION) {
            return;
        }
        for (AsConceptNode n : context.lookupAll(AsConceptNode.class)) {
            n.delete();
        }
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("Halp!");
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(AsConceptNode.class, "Action.removeCategory.label");
    }
}
