/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.gui;

import be.naturalsciences.bmdc.ears.entities.Actor;
import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.CurrentActor;
import be.naturalsciences.bmdc.ears.entities.CurrentCruise;
import be.naturalsciences.bmdc.ears.entities.CurrentProgram;
import be.naturalsciences.bmdc.ears.entities.EventBean;
import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.netbeans.services.GlobalActionContextProxy;
import be.naturalsciences.bmdc.ears.properties.Configs;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.IProperty;
import be.naturalsciences.bmdc.ontology.entities.ITool;
import gnu.trove.set.hash.THashSet;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.openide.util.Utilities;

/**
 * This interface enables a node with focus and all underlying nodes to be
 * expanded.
 *
 * @author Thomas Vandenberghe
 */
public interface ToEventConvertible {

    public void convertToEvent();

    public AsConcept getConcept();

    public IOntologyModel getOntologyModel();

    public static void convertToEventAndAddToLookup(AsConceptNode node) throws EarsException {
        if (node.getConcept() instanceof be.naturalsciences.bmdc.ears.ontology.entities.Action) {
            be.naturalsciences.bmdc.ears.ontology.entities.Action action = (be.naturalsciences.bmdc.ears.ontology.entities.Action) node.getConcept();
            List<AsConcept> la = new ArrayList<>();
            la.addAll(node.getParentsAsConcept());
            ConceptHierarchy cng = new ConceptHierarchy(la);

            Set<IProperty> properties = new THashSet<>(action.getChildren(cng));
            la.add(node.getConcept());

            cng = new ConceptHierarchy(la);

            ProgramBean currentProgram = null;
            CruiseBean currentCruise = null;
            Actor currentActor = null;
            CurrentProgram cProgram = Utilities.actionsGlobalContext().lookup(CurrentProgram.class);
            CurrentActor cActor = Utilities.actionsGlobalContext().lookup(CurrentActor.class);
            CurrentCruise cCruise = Utilities.actionsGlobalContext().lookup(CurrentCruise.class);
            if (cProgram != null) {
                currentProgram = cProgram.getConcept();
            }
            if (cCruise != null) {
                currentCruise = cCruise.getConcept();
            }
            if (cActor != null) {
                currentActor = cActor.getConcept();
            }
            if (currentProgram != null) {
                boolean overrideAnonymous = Configs.getOverrideEventsAsAnonymous();
                if (currentActor != null || overrideAnonymous) {
                    LinkedHashSet<ITool> tools = new LinkedHashSet<>();
                    tools.add(cng.getTool());
                    if (cng.getHostedTool() != null) {
                        tools.add(cng.getHostedTool());
                    }
                    String actor = null;
                    if (!overrideAnonymous) {
                        actor = currentActor.getLastNameFirstName();
                    }
                    EventBean event = new EventBean(currentProgram, currentCruise, cng.getToolCategory(), tools, cng.getProcess(), cng.getAction(), properties, actor);
                    GlobalActionContextProxy.getInstance().addEnsureOne(event);
                } else {
                    throw new EarsException("There is no current actor selected. Please (create and) select the current actor first.");
                }
            } else {
                throw new EarsException("There is no current program selected. Please (create and) select the current program first.");
            }
        }

    }

}
