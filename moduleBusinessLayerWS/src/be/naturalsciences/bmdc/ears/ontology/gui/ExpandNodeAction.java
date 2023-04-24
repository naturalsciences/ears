/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.gui;

import static be.naturalsciences.bmdc.ears.ontology.gui.Bundle.CTL_ExpandNodeAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.actions.CookieAction;

@ActionID(
        category = "Build",
        id = "be.naturalsciences.bmdc.ears.ontology.gui.ExpandNodeAction")
@ActionRegistration(
        displayName = "#CTL_ExpandNodeAction",lazy = false)//YS

@Messages("CTL_ExpandNodeAction=Expand all")
public class ExpandNodeAction extends CookieAction {

    private final Lookup context;
    private Expandable expandable;

    public ExpandNodeAction() {
        context = Utilities.actionsGlobalContext();
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        if (context.lookup(Expandable.class) != null
                && context.lookup(Node.class) != null
                && !context.lookup(Node.class).isLeaf()) {
            this.expandable = context.lookup(Expandable.class);
            return true;
        }
        return false;
    }

    @Override
    protected boolean surviveFocusChange() {
        return false;
    }

    @Override
    protected Class[] cookieClasses() {
        return new Class[]{Node.class, Expandable.class};
    }

    @Override
    protected int mode() {
        return org.openide.util.actions.CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (expandable != null) {
            expandable.expand();
        }
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public String getName() {
        return CTL_ExpandNodeAction();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("Halp!");
    }
}
