package be.naturalsciences.bmdc.ears.ontology.gui;

import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.ontology.Individuals;
import be.naturalsciences.bmdc.ears.ontology.entities.FakeConcept;
import be.naturalsciences.bmdc.ears.ontology.entities.Tool;
import be.naturalsciences.bmdc.ears.ontology.entities.ToolCategory;
import be.naturalsciences.bmdc.ears.ontology.entities.Vessel;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.AsConceptEvent;
import be.naturalsciences.bmdc.ontology.AsConceptEventListener;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.IIndividuals;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.EarsTermLabel;
import be.naturalsciences.bmdc.ontology.entities.IAction;
import be.naturalsciences.bmdc.ontology.entities.IEarsTerm;
import be.naturalsciences.bmdc.ontology.entities.IProcess;
import be.naturalsciences.bmdc.ontology.entities.IProperty;
import be.naturalsciences.bmdc.ontology.entities.ITool;
import be.naturalsciences.bmdc.ontology.entities.IToolCategory;
import be.naturalsciences.bmdc.ontology.entities.Term;
import gnu.trove.set.hash.THashSet;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import javax.swing.Action;
import org.netbeans.core.multiview.MultiViewTopComponent;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;

/**
 *
 * @author Thomas Vandenberghe
 */
public class AsConceptNode extends AbstractNode implements NodeListener, AsConceptEventListener {

    public static String CHILD_ADDED = "CHILD_ADDED";

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        /* if ("prefLabel".equals(evt.getPropertyName())) {
            this.fireDisplayNameChange(null, getDisplayName());
            //this.getConcept().getTermRef().getEarsTermLabel().setPrefLabel((String) evt.getNewValue());
            PropertyChangeEvent evt2 = new PropertyChangeEvent(this, "prefLabel", evt.getOldValue(), evt.getNewValue());
            IIndividuals individuals = childFactory.getOntModel().getIndividuals();
            individuals.change(evt2);
            individuals.refresh();
        }*/
    }

    @Override
    public void childrenAdded(NodeMemberEvent nme) {
        IIndividuals individuals = childFactory.getOntModel().getIndividuals();
        if (individuals != null) {
            individuals.add(nme);
            individuals.refresh();
        }
    }

    @Override
    public void childrenRemoved(NodeMemberEvent nme) {
        IIndividuals individuals = childFactory.getOntModel().getIndividuals();
        if (individuals != null) {
            individuals.remove(nme);
            individuals.refresh();
        }
    }

    @Override
    public void childrenReordered(NodeReorderEvent nre) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void nodeDestroyed(NodeEvent ne) {
        IIndividuals individuals = childFactory.getOntModel().getIndividuals();
        individuals.remove(ne);
        individuals.refresh();
    }

    @Override
    public void nodeRenamed(AsConceptEvent ace) {
        IIndividuals individuals = childFactory.getOntModel().getIndividuals();
        individuals.change(ace);
        individuals.refresh();
    }

    @Override
    public void nodeAdded(AsConceptEvent ace) {
        IIndividuals individuals = childFactory.getOntModel().getIndividuals();
        individuals.add(ace);
        individuals.refresh();
    }

    @Override
    public void nodeDestroyed(AsConceptEvent ace) {
        IIndividuals individuals = childFactory.getOntModel().getIndividuals();
        individuals.remove(ace);
        individuals.refresh();
    }

    public static class ContextBehaviour {

        public boolean moveFromWithin;
        public boolean moveToOther;
        public boolean moveFromOther;
        public IAsConceptFactory factory;

        public PropertyChangeListener pcListener;
        public NodeListener nListener;

        private ContextBehaviour(boolean moveFromWithin, boolean moveToOther, boolean moveFromOther, PropertyChangeListener pcListener, NodeListener nListener) {
            this.moveFromWithin = moveFromWithin;
            this.moveToOther = moveToOther;
            this.moveFromOther = moveFromOther;
            this.nListener = nListener;
            this.pcListener = pcListener;
        }

        private ContextBehaviour(boolean moveFromWithin, boolean moveToOther, boolean moveFromOther) {
            this(moveFromWithin, moveToOther, moveFromOther, null, null);
        }
    }

    public List<PropertyChangeListener> pcListenerList;
    public List<NodeListener> nodeListenerList;

    public static final ContextBehaviour BROWSE_BEHAVIOUR = new ContextBehaviour(false, true, false, null, null);
    public static final ContextBehaviour EDIT_BEHAVIOUR = new ContextBehaviour(true, false, true);

    private AsConcept concept;
    public AsConceptNode parentNode;

    private ContextBehaviour behaviour;

    private AsConceptChildFactory childFactory;
    ConceptHierarchy ConceptHierarchy;

    public AsConceptChildFactory getChildFactory() {
        return childFactory;
    }

    public AsConcept getConcept() {
        return concept;
    }

    public void setConcept(AsConcept concept) {
        this.concept = concept;
    }

    public ContextBehaviour getBehaviour() {
        return behaviour;
    }

    public void addPropertyChangeListenerUseThis(PropertyChangeListener pcl) {
        if (this.pcListenerList == null) {
            this.pcListenerList = new ArrayList();
        }
        pcListenerList.add(pcl);
        this.addPropertyChangeListener(pcl);
    }

    public void addNodeListenerUseThis(NodeListener nl) {
        if (this.nodeListenerList == null) {
            this.nodeListenerList = new ArrayList();
        }
        nodeListenerList.add(nl);
        this.addNodeListener(nl);
    }

    public void removeNodeListeners() {
        this.nodeListenerList.clear();
    }

    protected AsConceptNode(AsConceptNode parent, AsConcept obj, IOntologyModel ontModel, ContextBehaviour behaviour) { //InstanceContent ic, 
        super(Children.create(new AsConceptChildFactory(), true), Lookups.singleton(obj));

        this.parentNode = parent;
        this.concept = obj;
        this.behaviour = behaviour;

        this.ConceptHierarchy = new ConceptHierarchy(this.getParentsAsConcept());
        this.childFactory = new AsConceptChildFactory(parent, obj, this.ConceptHierarchy, ontModel, behaviour);

        if (childFactory.hasChildren()) {
            this.setChildren(Children.create(childFactory, true));
        } else {
            this.setChildren(Children.LEAF);
        }
        this.setValue("nodeDescription", getShortDescription());
        this.setShortDescription(getShortDescription());

        /* if (obj.getTermRef() != null) {
            EarsTermLabel label = obj.getTermRef().getEarsTermLabel(IEarsTerm.Language.en);
            label.addPropertyChangeListener(WeakListeners.propertyChange(this, label));
            label.addPropertyChangeListener(WeakListeners.propertyChange(behaviour.pcListener, label));
        }*/
        this.addPropertyChangeListenerUseThis(this.behaviour.pcListener);

        this.addNodeListenerUseThis(this); //listen to my own changes
    }

    /**
     * *
     * Constructor for a root node
     *
     * @param ontModel
     * @param behaviour
     */
    public AsConceptNode(IOntologyModel ontModel, ContextBehaviour behaviour) {
        super(Children.create(new AsConceptChildFactory(), true));
        this.parentNode = null;
        this.concept = new FakeConcept("Root", "Root", "Nothing to see here", true, ontModel.getNodes());

        this.behaviour = behaviour;
        setName("root");
        this.ConceptHierarchy = new ConceptHierarchy();
        this.childFactory = new AsConceptChildFactory(null, this.concept, this.ConceptHierarchy, ontModel, behaviour);
        this.setChildren(Children.create(childFactory, true));

        this.addNodeListenerUseThis(this); //listen to my own changes
    }

    @Override
    public String toString() {
        return this.getDisplayName();
    }

    public Collection<AsConceptNode> getParents() {
        Collection<AsConceptNode> ca = this.getParents(new HashSet());
        ca.remove(this);
        return ca;
    }

    public final Collection<AsConcept> getParentsAsConcept() {
        Collection<AsConcept> sa = new THashSet();
        Collection<AsConceptNode> ca = this.getParents();
        for (AsConceptNode conceptNode : ca) {
            sa.add(conceptNode.getConcept());
        }
        return sa;
    }

    private Collection<AsConceptNode> getParents(Collection l) {
        if (this.parentNode == null) { //if (this.getParentNode() == null) {
            l.add(this);
        } else {
            l.add(this);
            l.addAll(((AsConceptNode) this.parentNode).getParents(l)); //l.addAll(((AsConceptNode) this.getParentNode()).getParents(l));
        }
        return l;
    }

    public static Set<Node> getAllChildren(Node thisNode) {
        Set<Node> nodes = new THashSet();
        // ignore root -- root acts as a container
        Node node;
        if (thisNode.getChildren().getNodes().length > 0) {
            node = thisNode.getChildren().getNodes()[0];
        } else {
            return nodes;
        }

        while (node != null && node.getParentNode() != null) {
            // print node information
            //System.out.println(node. + "=" + node.getNodeValue());
            nodes.add(node);
            if (node.getChildren().getNodesCount() > 0) {//node.hasChildren() //branch
                node = node.getChildren().getNodes()[0]; //node = node.getFirstChild();
            } else {    // leaf
                // find the parent level
                Node nodeParent = node.getParentNode();
                int siblings = 0;
                try {
                    siblings = nodeParent.getChildren().getNodesCount() - 1;
                } catch (Exception e) {
                    int a = 5;
                }
                int nodeIndex = getNodeIndex(nodeParent, node);
                int remainingSiblings = siblings - nodeIndex;
                //for (int i = 1; i < siblings; i++) {
                //Node nextSibling = 
                //Arrays.asList(nodeParent.getChildren().getNodes()).remove().iterator().next();
                //}
                while (remainingSiblings == 0 && node != thisNode) //while (node.getNextSibling() == null && node != rootNode) // use child-parent link to get to the parent level
                {
                    node = node.getParentNode();
                }
                if (nodeIndex < siblings) {
                    try {
                        node = nodeParent.getChildren().getNodes()[nodeIndex + 1]; //node = node.getNextSibling();
                    } catch (Exception e) {
                        int a = 5;
                    }
                } else {
                    node = null;
                }
            }
        }
        return nodes;
    }

    private static int getNodeIndex(Node parentNode, Node ofNode) {
        int c = 0;
        try {
            c = parentNode.getChildren().getNodesCount();
        } catch (Exception e) {
            int a = 5;
        }
        for (int i = 0; i < c; i++) {
            if (parentNode.getChildren().getNodes()[i].equals(ofNode)) {
                return i;
            }

        }
        return -1;
    }

    @Override
    public String getHtmlDisplayName() {

        if (this.concept instanceof IToolCategory) {
            return "<font color='#0B486B'>" + getDisplayName() + "</font>";
        } else if (this.concept instanceof ITool) {
            return "<font color='#02779E'>" + getDisplayName() + "</font>";
        } else if (this.concept instanceof IProcess) {
            return "<font color='#DC4B40'>" + getDisplayName() + "</font>";
        } else if (this.concept instanceof IAction) {
            return "<font color='#F59E03'>" + getDisplayName() + "</font>";
        } else if (this.concept instanceof IProperty) {
            return "<font color='#EB540A'>" + getDisplayName() + "</font>";
        } else {
            return "<font color='#2C3539'>" + getDisplayName() + "</font>";
        }
    }

    @Override
    public String getDisplayName() {
        if (isRoot()) {
            return "root";
        } else if (concept != null && concept.getTermRef() != null) {
            return concept.getTermRef().getEarsTermLabel().getPrefLabel();
        } else {
            return "root";
        }
    }

    public boolean isRoot() {
        if (this.concept instanceof FakeConcept) {
            FakeConcept c = (FakeConcept) this.concept;
            return c.isIsRoot();
        } else {
            return false;
        }
    }

    @Override
    public final String getShortDescription() {
        if (concept != null && concept.getTermRef() != null) {
            if (concept.getTermRef().getEarsTermLabel().getDefinition() != null) {
                return concept.getKind() + ": " + concept.getTermRef().getEarsTermLabel().getDefinition().replace("><", "> <");
            } else {
                return concept.getKind();
            }
        }
        return "";
    }

    @Override
    public Image getIcon(int type) {
        if (this.concept instanceof IToolCategory) {
            return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/treeviewer/toolcategory.png"); //flaticon
        } else if (this.concept instanceof ITool) {
            return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/treeviewer/tool.png"); //flaticon
        } else if (this.concept instanceof IProcess) {
            return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/treeviewer/process.png"); //flaticon
        } else if (this.concept instanceof IAction) {
            return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/treeviewer/action.png"); //flaticon
        } else if (this.concept instanceof IProperty) {
            return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/treeviewer/property.png"); //flaticon
        } else if (this.isRoot()) {
            return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/treeviewer/root.png"); //flaticon
        } else {
            return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/treeviewer/unknown.png"); //flaticon
        }
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public Action[] getActions(boolean context) {
      //  Action a = SystemAction.get(ExpandNodeAction.class);
        if (this.behaviour == EDIT_BEHAVIOUR) {
            return new Action[]{
                SystemAction.get(DeleteNodeAction.class),
                SystemAction.get(ExpandNodeAction.class),
                //SystemAction.get(CollapseNodeAction.class),
                SystemAction.get(CreateChildNodeAction.class), //After discussion during ODIP 2 in October 2017 in Galway it was decided to only allow creating new tools or properties.
                SystemAction.get(CreateEventAction.class)};
        } else {
            return new Action[]{
                SystemAction.get(ExpandNodeAction.class),
                //SystemAction.get(CollapseNodeAction.class), 
                SystemAction.get(CreateEventAction.class)};
        }
    }

    @Override
    public PasteType getDropType(Transferable t, int arg1, int arg2) {
        if (behaviour == AsConceptNode.EDIT_BEHAVIOUR && t instanceof AsConcept) {
            AsConcept transferred = AsConceptChildFactory.getTransferData(t);
            boolean dropPermission = AsConceptChildFactory.isDropPermitted(t, concept, transferred);
            if (dropPermission) {
                return new PasteType() {
                    @Override
                    public Transferable paste() throws IOException {

                        AsConcept transferredCopy = null;
                        boolean removePreviousBottomUpAssociations = true;
                        TopComponent originalTopcomponent = TopComponent.getRegistry().getActivated();
                        AsConceptNode originalNode = originalTopcomponent.getLookup().lookup(AsConceptNode.class);
                        try {
                            transferredCopy = transferred.clone(new IdentityHashMap());
                        } catch (CloneNotSupportedException ex) {
                            Messaging.report("Could not clone the dragged object " + transferred.getUri(), ex, this.getClass(), true);
                        }
                        if (originalTopcomponent instanceof MultiViewTopComponent) { //ugly hack
                            removePreviousBottomUpAssociations = false;
                        }
                        if (transferredCopy != null) {
                            if (transferredCopy instanceof ToolCategory) {
                                ToolCategory child = (ToolCategory) transferredCopy;
                                try {
                                    child.reduceGevsToSevs(AsConceptNode.this.behaviour.factory);
                                } catch (EarsException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                            if (transferredCopy instanceof Tool) {
                                Tool child = (Tool) transferredCopy;
                                child.setToolIdentifier(null);
                                child.setSerialNumber(null);
                            }
                            addAsChild(transferredCopy, removePreviousBottomUpAssociations, originalNode); //:false should be dependent on whether the donor is the same instance as the reciever.
                        }
                        return null; //We put nothing in the clipboard
                    }
                };
            } else {
                return null;
            }
        } else { //open the node
            return null;
        }
    }

    private void addAsChild(AsConcept newChild, boolean removePreviousBottomUpAssociations, AsConceptNode originalNode) {
        if (originalNode != null) {
            concept.addToChildren(ConceptHierarchy, newChild, removePreviousBottomUpAssociations, originalNode.ConceptHierarchy, this.behaviour.factory);
        } else {
            concept.addToChildren(ConceptHierarchy, newChild, removePreviousBottomUpAssociations, null, this.behaviour.factory);
        }
        if (concept instanceof FakeConcept && newChild instanceof ToolCategory) {
            childFactory.getOntModel().getNodes().getNodes().add(newChild);
        }
        if (isLeaf()) {
            setChildren(Children.create(childFactory, true));
        }
        addNodeListenerUseThis(behaviour.nListener);
        childFactory.refresh();
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public Transferable drag() {
        if (this.concept instanceof ToolCategory) {
            ToolCategory c = (ToolCategory) this.concept;
            return c;
        } else if (this.concept instanceof Tool) {
            Tool c = (Tool) this.concept;
            return c;
        } else if (this.concept instanceof Vessel) {
            Vessel c = (Vessel) this.concept;
            return c;
        } else if (this.concept instanceof be.naturalsciences.bmdc.ears.ontology.entities.Process) {
            be.naturalsciences.bmdc.ears.ontology.entities.Process c = (be.naturalsciences.bmdc.ears.ontology.entities.Process) this.concept;
            return c;
        } else if (this.concept instanceof be.naturalsciences.bmdc.ears.ontology.entities.Action) {
            be.naturalsciences.bmdc.ears.ontology.entities.Action c = (be.naturalsciences.bmdc.ears.ontology.entities.Action) this.concept;
            return c;
        } else if (this.concept instanceof be.naturalsciences.bmdc.ears.ontology.entities.Property) {
            be.naturalsciences.bmdc.ears.ontology.entities.Property c = (be.naturalsciences.bmdc.ears.ontology.entities.Property) this.concept;
            return c;
        } else {
            return null;
        }
    }

    public void delete() {
        concept.delete(this.ConceptHierarchy);
        addNodeListenerUseThis(behaviour.nListener);
        fireNodeDestroyed();

        /* IIndividuals individuals = childFactory.getOntModel().getIndividuals();
        individuals.remove(concept);
        individuals.refresh();*/
    }

    public void createNewChild() {
        AsConcept newChild = null;
        try {
            newChild = this.behaviour.factory.buildChild(concept);

            if (newChild != null) {
                /*IIndividuals individuals = childFactory.getOntModel().getIndividuals(); //TODO replaced by event
                individuals.add(newChild);
                individuals.refresh();*/
                addAsChild(newChild, false, null);
            }
        } catch (EarsException ex) {
            Messaging.report("Could not create a child node", ex, this.getClass(), true);
        }
    }

    public class EarsTermRenamer {

        private EarsTermLabel termLabel;

        public EarsTermLabel getTermLabel() {
            return termLabel;
        }

        public void setTermLabel(EarsTermLabel termLabel) {
            this.termLabel = termLabel;
        }

        public EarsTermRenamer(EarsTermLabel termLabel) {
            this.termLabel = termLabel;
        }

        public String getPrefLabel() {
            return termLabel.getPrefLabel();
        }

        public void setPrefLabel(String label) {
            String nameExists = Individuals.nameExists(label);
            if (nameExists != null) {
                Messaging.report(nameExists, Message.State.BAD, ClassChildren.class, true);
            } else {
                //IIndividuals individuals = childFactory.getOntModel().getIndividuals();
                //individuals.remove(concept);
                String oldPrefLabel = this.termLabel.getPrefLabel();
                this.termLabel.setPrefLabel(label);

                PropertyChangeEvent evt2 = new PropertyChangeEvent(AsConceptNode.this, "prefLabel", oldPrefLabel, label);

                AsConceptNode.this.fireDisplayNameChange(null, getDisplayName());
                for (PropertyChangeListener pcl : AsConceptNode.this.pcListenerList) {
                    pcl.propertyChange(evt2);
                }
                IIndividuals individuals = childFactory.getOntModel().getIndividuals();
                individuals.change(evt2);
                individuals.refresh();
//                individuals.change(concept);
                //individuals.refresh();
            }
        }

        public String getDefinition() {
            return termLabel.getDefinition();
        }

        public void setDefinition(String definition) {
            this.termLabel.setDefinition(definition);
            IIndividuals individuals = childFactory.getOntModel().getIndividuals();
            individuals.refresh();
        }
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        if (!(this.concept instanceof FakeConcept)) {
            Sheet.Set set = Sheet.createPropertiesSet();

            if (this.concept != null) {
                IOntologyModel currentModel = childFactory.getOntModel();

                Term term = this.concept.getTermRef();
                if (term != null) {
                    URI uri = term.getUri();
                    be.naturalsciences.bmdc.ears.ontology.entities.Property infoProperty = null;
                    Tool infoTool = null;
                    EarsTermLabel label = term.getEarsTermLabel(IEarsTerm.Language.en);
                    EarsTermRenamer termRenamer = new EarsTermRenamer(label);
                    if (this.concept instanceof be.naturalsciences.bmdc.ears.ontology.entities.Property) {
                        infoProperty = (be.naturalsciences.bmdc.ears.ontology.entities.Property) this.concept;
                    }
                    if (this.concept instanceof Tool) {
                        infoTool = (Tool) this.concept;
                    }
                    try {
                        Property nameProp = null;
                        Property altNameProp = null;
                        Property defProp = null;
                        Property mandatoryPropertyProp = null;
                        Property multiplePropertyProp = null;
                        Property serialNumberProp = null;
                        Property toolIdentifierProp = null;
                        CurrentVessel currentVessel = Utilities.actionsGlobalContext().lookup(CurrentVessel.class);
                        String currentVesselCode = null;
                        if (currentVessel != null && currentVessel.getConcept() != null) {
                            currentVesselCode = currentVessel.getConcept().getCode();
                        }

                        if (currentVesselCode != null && this.behaviour == EDIT_BEHAVIOUR && currentModel.isEditable() && concept.getTermRef().isOwnTerm(currentVesselCode)) {
                            nameProp = new PropertySupport.Reflection(termRenamer, String.class, "getPrefLabel", "setPrefLabel");
                            altNameProp = new PropertySupport.Reflection(label, String.class, "altLabel");
                            defProp = new PropertySupport.Reflection(termRenamer, String.class, "getDefinition", "setDefinition");

                            if (infoProperty != null) {
                                mandatoryPropertyProp = new PropertySupport.Reflection(infoProperty, Boolean.class, "isMultiple", "setMultiple");
                                multiplePropertyProp = new PropertySupport.Reflection(infoProperty, Boolean.class, "isMandatory", "setMandatory");

                                mandatoryPropertyProp.setName("is mandatory");
                                multiplePropertyProp.setName("can occur multiple times");

                                set.put(mandatoryPropertyProp);
                                set.put(multiplePropertyProp);
                            }

                        } else {
                            nameProp = new PropertySupport.Reflection(label, String.class, "getPrefLabel", null);
                            altNameProp
                                    = new PropertySupport.Reflection(label, String.class, "getAltLabel", null);
                            defProp
                                    = new PropertySupport.Reflection(label, String.class, "getDefinition", null);

                            if (infoProperty != null) {
                                mandatoryPropertyProp = new PropertySupport.Reflection(infoProperty, Boolean.class, "isMultiple", null);
                                multiplePropertyProp = new PropertySupport.Reflection(infoProperty, Boolean.class, "isMandatory", null);

                                mandatoryPropertyProp.setName("is mandatory");
                                multiplePropertyProp.setName("can occur multiple times");

                                set.put(mandatoryPropertyProp);
                                set.put(multiplePropertyProp);

                            }
                        }

                        /*if (currentVesselCode != null && this.behaviour == editBehaviour && currentModel.isEditable() && infoTool != null) {
                            serialNumberProp = new PropertySupport.Reflection(infoTool, String.class, "getSerialNumber", "setSerialNumber");
                            serialNumberProp.setName("tool serial number");
                            
                            toolIdentifierProp = new PropertySupport.Reflection(infoTool, String.class, "getToolIdentifier", "setToolIdentifier");
                            toolIdentifierProp.setName("tool identifier");
                            
                        }*/
                        Property kindProp = new PropertySupport.Reflection(this.concept, String.class, "getKind", null);
                        Property uriProp = new PropertySupport.Reflection(uri, String.class, "toASCIIString", null);
                        Property urnProp = new PropertySupport.Reflection(term, String.class, "getIdentifierUrn", null);
                        Property statusProp = new PropertySupport.Reflection(term, String.class, "getStatusName", null);
                        Property creationDateProp = new PropertySupport.Reflection(term, Date.class, "getCreationDate", null);
                        Property toStringProp = new PropertySupport.Reflection(this.concept, String.class, "toString", null);
                        //Property printProp = new PropertySupport.Reflection(this.concept, String.class, "print", null);
                        nameProp.setName("label");
                        altNameProp.setName("alt label");
                        defProp.setName("definition");
                        kindProp.setName("kind");
                        uriProp.setName("uri");
                        urnProp.setName("urn");
                        statusProp.setName("status");
                        creationDateProp.setName("creation date");
                        toStringProp.setName("internal details");
                        //printProp.setName("relations");

                        set.put(nameProp);
                        set.put(altNameProp);
                        set.put(defProp);
                        set.put(kindProp);
                        /*if (serialNumberProp != null) {
                            set.put(serialNumberProp);
                        }
                        if (toolIdentifierProp != null) {
                            set.put(toolIdentifierProp);
                        }*/
                        set.put(uriProp);
                        set.put(urnProp);
                        set.put(statusProp);
                        set.put(creationDateProp);
                        set.put(toStringProp);
                        //set.put(printProp);

                    } catch (NoSuchMethodException ex) {
                        ErrorManager.getDefault();
                    }

                    sheet.put(set);
                }
            }
        }
        return sheet;
    }
}
