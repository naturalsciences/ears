/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.gui;

import be.naturalsciences.bmdc.ears.entities.CurrentActor;
import be.naturalsciences.bmdc.ears.entities.CurrentEvent;
import be.naturalsciences.bmdc.ears.entities.CurrentProgram;
import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.entities.VesselBean;
import be.naturalsciences.bmdc.ears.netbeans.services.GlobalActionContextProxy;
import be.naturalsciences.bmdc.ears.ontology.entities.Tool;
import be.naturalsciences.bmdc.ears.ontology.entities.ToolCategory;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.IEventDefinition;
import be.naturalsciences.bmdc.ontology.entities.IProperty;
import eu.eurofleets.ears3.dto.EventDTO;
import eu.eurofleets.ears3.dto.LinkedDataTermDTO;
import eu.eurofleets.ears3.dto.PersonDTO;
import eu.eurofleets.ears3.dto.PropertyDTO;
import eu.eurofleets.ears3.dto.ToolDTO;
import gnu.trove.set.hash.THashSet;
import java.util.ArrayList;
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
            la.add(node.getConcept());
            ConceptHierarchy cng = new ConceptHierarchy(la);
            Set<IProperty> properties = new THashSet<>(action.getChildren(cng));
            Set<PropertyDTO> propertyDTOs = new THashSet<>();
            for (IProperty property : properties) {
                PropertyDTO propertyDTO = new PropertyDTO(new LinkedDataTermDTO(property), null, property.getUnit());
                propertyDTOs.add(propertyDTO);
            }
            IEventDefinition eventDefinition = cng.getEvent();

            cng = new ConceptHierarchy(la);

            ProgramBean currentProgram = null;
            VesselBean currentVessel = null;
            PersonDTO currentActor = null;
            CurrentProgram cProgram = Utilities.actionsGlobalContext().lookup(CurrentProgram.class);
            CurrentActor cActor = Utilities.actionsGlobalContext().lookup(CurrentActor.class);
            CurrentVessel cVessel = Utilities.actionsGlobalContext().lookup(CurrentVessel.class);
            if (cProgram != null) {
                currentProgram = cProgram.getConcept();
            }
            if (cVessel != null) {
                currentVessel = cVessel.getConcept();
            }
            if (cActor != null) {
                currentActor = cActor.getConcept();
            }
            if (currentProgram != null) {
                // boolean overrideAnonymous = Configs.getOverrideEventsAsAnonymous();
                if (currentActor != null/* || overrideAnonymous*/) { //no more anonymous events in ears3
                    LinkedDataTermDTO parentTool = null;
                    ToolDTO tool = null;
                    LinkedDataTermDTO toolCategory = null;

                    if (cng.getHostedTool() != null) { //I am in a hierarchy of a host tool.
                        //tools.add(cng.getHostedTool());
                        parentTool = new LinkedDataTermDTO(cng.getTool());
                        tool = new ToolDTO(new LinkedDataTermDTO(cng.getHostedTool()), parentTool);
                        toolCategory = new LinkedDataTermDTO((ToolCategory) cng.getHostedTool().getToolCategoryCollection().toArray()[0]); //TODO when creating nested tools, pick ONE tool catageory instedad of all.
                    } else { // I am not in a hierarchy of a host tool, but I may appear in another place in one 
                        parentTool = cng.getTool().getHostsCollection().isEmpty() ? null : new LinkedDataTermDTO((Tool) cng.getTool().getHostsCollection().toArray()[0]);
                        tool = new ToolDTO(new LinkedDataTermDTO(cng.getTool()), parentTool);
                        toolCategory = new LinkedDataTermDTO(cng.getToolCategory());
                    }

                    // EventDTO event = new EventBean(eventDefinition.getUri().toString(), currentVessel, currentProgram, cng.getToolCategory(), tool, cng.getProcess(), cng.getAction(), properties, actor);
                    LinkedDataTermDTO subject = new LinkedDataTermDTO("http://vocab.nerc.ac.uk/collection/C77/current/M05", null, "Occasional standard measurements"); //TODO change this.

                    EventDTO event = new EventDTO(null, eventDefinition.getUri().toString(), null, currentActor, subject, toolCategory, tool, new LinkedDataTermDTO(cng.getProcess()), new LinkedDataTermDTO(cng.getAction()), propertyDTOs, currentProgram.getName(), currentVessel.getCode());
                    //  GlobalActionContextProxy.getInstance().addEnsureOne(event);
                    GlobalActionContextProxy.getInstance().add(CurrentEvent.getInstance(event));
                } else {
                    throw new EarsException("There is no current actor selected. Please (create and) select the current actor first.");
                }
            } else {
                throw new EarsException("There is no current program selected. Please (create and) select the current program first.");
            }
        }

    }

}
