/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.gui;

import be.naturalsciences.bmdc.ears.ontology.AsConceptFlavor;
import be.naturalsciences.bmdc.ears.ontology.entities.Action;
import be.naturalsciences.bmdc.ears.ontology.entities.FakeConcept;
import be.naturalsciences.bmdc.ears.ontology.entities.Tool;
import be.naturalsciences.bmdc.ears.ontology.gui.AsConceptNode.ContextBehaviour;
import static be.naturalsciences.bmdc.ears.ontology.gui.AsConceptNode.EDIT_BEHAVIOUR;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.OntologyConstants;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.IAction;
import be.naturalsciences.bmdc.ontology.entities.IProcess;
import be.naturalsciences.bmdc.ontology.entities.IToolCategory;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Set;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;

/**
 *
 * @author Thomas Vandenberghe
 */
class AsConceptChildFactory extends ChildFactory<AsConcept> implements NodeListener {

    private IOntologyModel ontModel;

    private AsConcept concept;

    private ConceptHierarchy parents;

    private AsConceptNode parentNode;

    private AsConceptNode conceptNode;

    private ContextBehaviour behaviour;

    public IOntologyModel getOntModel() {
        return ontModel;
    }

    public AsConcept getConcept() {
        return concept;
    }

    /**
     * *
     * An empty constructor, used to set the Children after construction
     */
    public AsConceptChildFactory() {
    }

    public AsConceptChildFactory(AsConceptNode parentNode, AsConcept concept, ConceptHierarchy parents, IOntologyModel ontModel, ContextBehaviour behaviour/*, File ontologyFile*/) {
        if (parentNode == null && !(concept instanceof FakeConcept)) {
            throw new IllegalArgumentException("ParentNode is null.");
        }
        this.concept = concept;
        this.parents = parents;
        this.parentNode = parentNode;
        this.ontModel = ontModel;
        this.behaviour = behaviour;
    }

    public boolean hasChildren() {
        if (parents != null) {
            Set<AsConcept> children = concept.getChildren(parents);
            if (children != null) {
                return !children.isEmpty();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * Build the children of this AsConcept.
     *
     * @param toPopulate
     * @return
     */
    @Override
    protected boolean createKeys(List<AsConcept> toPopulate) {
        if (parents != null) {
            Set<AsConcept> children = null;
            if (this.behaviour == EDIT_BEHAVIOUR && this.concept instanceof IToolCategory) { //if we are editing only show toolcats and tools, don't show the processes and actions
                children = concept.getChildren(parents);
            } else {
                children = concept.getChildren(parents);
            }

            children.removeIf(c -> c.getTermRef() == null || (c.getTermRef().getStatusName() != null && c.getTermRef().getStatusName().equals(OntologyConstants.STATUSES.get(OntologyConstants.DEPRECATED))));
            toPopulate.addAll(children);
        } else {
            return false;
        }
        return true;
    }

    public void refresh() {
        super.refresh(true);
    }

    public static boolean isDropPermitted(Transferable dropped, AsConceptNode destinationNode, AsConcept transferred) {
        AsConcept destination = destinationNode.getConcept();
        if (transferred.equals(destination)) { //if something is dropped unto itself
            return false;
        }
        if (!(transferred instanceof Tool)) { //tools can be added to their own parent multiple times, everything else not
            for (Node chN : destinationNode.getChildren().getNodes()) {
                AsConceptNode ch = (AsConceptNode) chN;
                if (ch.getConcept().equals(transferred)) { //if the destination already contains the transferred.
                    return false;
                }
            }
        }
        if (destinationNode.getConcept() instanceof Action && destinationNode.getConcept() instanceof Process && destinationNode.conceptHierarchy.isGeneric()) { //we can't add new actions to existing generic processes and we can't add new properties to existing generic actions
            return false;
        }

        if (transferred instanceof Tool && destination instanceof Tool) {
            Tool transferredTool = (Tool) transferred;
            Tool destinationTool = (Tool) destination;
            if (destinationTool.isHostedTool()) {// nested tools can only go 2 levels deep
                return false;
            }
        }
        AsConceptFlavor droppedFlavor = null;
        if (dropped.getTransferDataFlavors().length > 0) {
            droppedFlavor = (AsConceptFlavor) dropped.getTransferDataFlavors()[0];
        }
        AsConceptFlavor destinationFlavor = new AsConceptFlavor(destination.getClass(), destination.getClass().getSimpleName());
        return destinationFlavor.canHaveAsChild(droppedFlavor);
    }

    @Override
    public void childrenAdded(NodeMemberEvent nme) {

    }

    @Override
    public void childrenRemoved(NodeMemberEvent nme) {

    }

    @Override
    public void childrenReordered(NodeReorderEvent nre) {

    }

    @Override
    public void nodeDestroyed(NodeEvent ne) {
        if (!(this.concept instanceof FakeConcept)) {
            this.parentNode.getChildFactory().refresh();
        }
        this.refresh(false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public enum DropPermitted {
        TRUE,
        FALSE,
        ONPARENT //the drop of dropped should happen on the parent of destination because the classes of destination and dropped are the same.
    };

    public static AsConcept getTransferData(Transferable t) {
        try {
            if (t.getTransferData(AsConceptFlavor.TOOLCATEGORY_FLAVOR) != null) {
                return (AsConcept) t.getTransferData(AsConceptFlavor.TOOLCATEGORY_FLAVOR);
            } else if (t.getTransferData(AsConceptFlavor.TOOL_FLAVOR) != null) {
                return (AsConcept) t.getTransferData(AsConceptFlavor.TOOL_FLAVOR);
            } else if (t.getTransferData(AsConceptFlavor.PROCESS_FLAVOR) != null) {
                return (AsConcept) t.getTransferData(AsConceptFlavor.PROCESS_FLAVOR);
            } else if (t.getTransferData(AsConceptFlavor.ACTION_FLAVOR) != null) {
                return (AsConcept) t.getTransferData(AsConceptFlavor.ACTION_FLAVOR);
            } else if (t.getTransferData(AsConceptFlavor.PROPERTY_FLAVOR) != null) {
                return (AsConcept) t.getTransferData(AsConceptFlavor.PROPERTY_FLAVOR);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected Node createNodeForKey(AsConcept key) {
        this.conceptNode = new AsConceptNode(this.parentNode, this.concept, this.ontModel, this.behaviour);
        AsConceptNode childNode = new AsConceptNode(this.conceptNode, key, this.ontModel, this.behaviour);
        childNode.addNodeListenerUseThis(this);
        return childNode;
    }
}
