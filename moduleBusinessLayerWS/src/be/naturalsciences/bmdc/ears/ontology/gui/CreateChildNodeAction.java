/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.gui;

import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
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
        id = "be.naturalsciences.bmdc.ears.ontology.gui.CreateChildNodeAction")
@ActionRegistration(
        displayName = "#CTL_CreateChildNodeAction",lazy = false)//YS

@NbBundle.Messages("CTL_CreateChildNodeAction=Create child...")
public class CreateChildNodeAction extends CookieAction {

    private final Lookup context;

    public CreateChildNodeAction() {
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
        return org.openide.util.actions.CookieAction.MODE_ONE;
    }

    @Override
    protected void performAction(Node[] activatedNodes) {

        for (Node n : activatedNodes) {
            AsConceptNode node = (AsConceptNode) n;
            node.createNewChild();
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
        return NbBundle.getMessage(CreateChildNodeAction.class, "CTL_CreateChildNodeAction");
    }
}
