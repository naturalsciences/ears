/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.gui;

import be.naturalsciences.bmdc.ears.ontology.AsConceptFlavor;
import be.naturalsciences.bmdc.ears.ontology.entities.FakeConcept;
import be.naturalsciences.bmdc.ears.ontology.entities.Tool;
import be.naturalsciences.bmdc.ears.ontology.gui.AsConceptNode.ContextBehaviour;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
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
//does nothing
    }

    /*public AsConceptChildFactory(AsConcept concept) {
     this.concept = concept;
     try {
     ontModel = new OntologyModel<>("earsv2-onto-belgica.rdf", ToolCategory.class);
     } catch (FileNotFoundException ex) {
     Exceptions.printStackTrace(ex);
     } catch (OntologyModelException ex) {
     Exceptions.printStackTrace(ex);
     }
     }*/
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
        /*if (this.concept instanceof FakeConcept) {
         FakeConcept c = (FakeConcept) this.concept;
         return c.isIsRoot(); //root is understood to have children 
         } else*/
 /* if (this.concept != null && this.concept.getTermRef() != null && this.concept.getTermRef().getPublisherUrn() != null && this.concept.getTermRef().getPublisherUrn().equals("SDN:L22::NETT0007")) { //BIOMAPER-II
            int a = 5;//no kids
        }
        if (this.concept != null && this.concept.getTermRef() != null && this.concept.getTermRef().getPublisherUrn() != null && this.concept.getTermRef().getPublisherUrn().equals("SDN:L22::TOOL0434")) { //APPLIED MICROSYSTEMS MICRO CTD
            int a = 5; //kids
        }
        if (this.concept != null && this.concept.getTermRef() != null && this.concept.getTermRef().getPublisherUrn() != null && this.concept.getTermRef().getPublisherUrn().equals("SDN:L05::130")) { //CTD
            int a = 5; //kids
        }*/
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
            /*if (concept instanceof FakeConcept && ((FakeConcept) concept).isIsRoot() && concept.getChildren(parents).isEmpty()) {
             List<ToolCategory> tcList = null;
             try {
             tcList = ontModel.getNodes().getNodes();
             } catch (NullPointerException e) {
             int a = 5;
             }
             for (ToolCategory tc : tcList) {
             if (tc.getId() != null) {

             concept.addToChildren(parents, tc, false);
             }
             }
             }*/
            Set<AsConcept> acList = concept.getChildren(parents);
            toPopulate.addAll(acList);
        } else {
            return false;
        }
        return true;
    }

    public void refresh() {
        super.refresh(true);
    }

    public static boolean isDropPermitted(Transferable dropped, AsConcept destination, AsConcept transferred) {
        if (transferred.equals(destination)) {
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
        if (droppedFlavor != null && destinationFlavor.canHaveAsChild(droppedFlavor)) {
            return true;
        } else {
            return false;
        }
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
        this.refresh(true);
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
        //this.conceptNode = new AsConceptNode(parentNode, concept, new InstanceContent(), ontModel);
        //AsConceptNode childNode = new AsConceptNode(this.conceptNode, key, new InstanceContent(), ontModel) {
        this.conceptNode = new AsConceptNode(this.parentNode, this.concept, this.ontModel, this.behaviour);
        //   this.conceptNode.addNodeListenerUseThis(this.conceptNode);
        /*this.conceptNode.addPropertyChangeListenerUseThis(behaviour.pcListener);
         this.conceptNode.addNodeListenerUseThis(behaviour.nListener);*/

        AsConceptNode childNode = new AsConceptNode(this.conceptNode, key, this.ontModel, this.behaviour);

        childNode.addNodeListenerUseThis(this);
        //  childNode.addNodeListenerUseThis(childNode); //listen to my own changes
        return childNode;
    }

}
