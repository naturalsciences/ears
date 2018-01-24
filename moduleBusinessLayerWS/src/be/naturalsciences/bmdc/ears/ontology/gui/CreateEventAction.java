/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.gui;

import be.naturalsciences.bmdc.ears.ontology.ProgramOntology;
import be.naturalsciences.bmdc.ears.ontology.VesselOntology;
import be.naturalsciences.bmdc.ears.ontology.entities.Action;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.actions.CookieAction;

@ActionID(
        category = "Build",
        id = "be.naturalsciences.bmdc.ears.ontology.gui.CreateEventAction")
/*@ActionReferences({
 @ActionReference(path = "Menu/File", position = 0),
 @ActionReference(path = "Loaders/Languages/Actions", position = 0),
 @ActionReference(path = "Projects/Actions")
 })*/
@ActionRegistration(
        displayName = "#CTL_CreateEventAction")

@Messages("CTL_CreateEventAction=Create event")
public class CreateEventAction extends CookieAction {

    private final Lookup context;
    private ToEventConvertible node;
    //private EventBean event;

    public CreateEventAction() {
        context = Utilities.actionsGlobalContext();

    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        if (context.lookup(ToEventConvertible.class) != null && context.lookup(Node.class) != null) {
            this.node = context.lookup(ToEventConvertible.class);
            if ((this.node.getOntologyModel() instanceof VesselOntology || this.node.getOntologyModel() instanceof ProgramOntology) && this.node.getConcept() instanceof Action) {
                return true;
            }
        }
        return false;
        /*
         for (Node node : activatedNodes) {
         if (node instanceof ToEventConvertible) {
         ToEventConvertible n = (ToEventConvertible) node;
         if (n.getConcept() instanceof Action) {
         return true;
         }
         }
         }*/
    }

    @Override
    protected boolean surviveFocusChange() {
        return false;
    }

    @Override
    protected Class[] cookieClasses() {
        return new Class[]{Node.class, ToEventConvertible.class};
    }

    @Override
    protected int mode() {
        return org.openide.util.actions.CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (node != null) {
            node.convertToEvent();
            surviveFocusChange();
        }
    }

    @Override
    protected boolean asynchronous() {
        return true;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(CreateEventAction.class, "CTL_CreateEventAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("Help!");
    }
}
