/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.rdf;

import be.naturalsciences.bmdc.ears.ontology.AsConceptFactory;
import be.naturalsciences.bmdc.ears.ontology.BaseOntology;
import be.naturalsciences.bmdc.ears.ontology.OntologyModel;
import be.naturalsciences.bmdc.ears.ontology.OntologyNodes;
import be.naturalsciences.bmdc.ears.ontology.entities.EventDefinition;
import be.naturalsciences.bmdc.ears.ontology.entities.GenericEventDefinition;
import be.naturalsciences.bmdc.ears.ontology.entities.Property;
import be.naturalsciences.bmdc.ears.ontology.entities.ToolCategory;
import be.naturalsciences.bmdc.ears.ontology.gui.AsConceptNode;
import be.naturalsciences.bmdc.ears.ontology.gui.ToEventConvertible;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.writer.ScopeMap;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Thomas Vandenberghe
 *
 * The panel that is used to edit an ontology tree
 */
public class EditRdfFilePanel extends JPanel implements ExplorerManager.Provider, Lookup.Provider, ChangeListener {

    private BeanTreeView treeView;
    private final InstanceContent lookupContent = new InstanceContent();
    private final ExplorerManager mgr = new ExplorerManager();
    private Lookup lookup;
    private RdfFileTypeDataObject dataObject;
    private AsConceptNode.ContextBehaviour beh;

    @Override
    public ExplorerManager getExplorerManager() {
        return mgr;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    public BeanTreeView getTreeView() {
        return treeView;
    }

    /**
     *
     * @param dobj
     * @param beh
     */
    public EditRdfFilePanel(final RdfFileTypeDataObject dataObject, AsConceptNode.ContextBehaviour beh) throws IOException {
        this.beh = beh;
        this.beh.factory = new AsConceptFactory(dataObject.getOntModel());
        initTree(dataObject);

        this.lookup = new ProxyLookup(Lookups.fixed(/*new AbstractLookup(lookupContent), new ToEventConvertibleNode(),*/new CollapsibleConceptNodes(mgr, treeView), new ExpandableConceptNodes(mgr, treeView)), ExplorerUtils.createLookup(mgr, this.getActionMap()));
    }

    private void initTree(final RdfFileTypeDataObject dataObject) throws IOException {
        this.dataObject = dataObject;
        this.treeView = new BeanTreeView();
        this.treeView.setRootVisible(true);
        setLayout(new BorderLayout());
        add(this.treeView, BorderLayout.CENTER);
        this.treeView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.lookupContent.add(this.dataObject.getOntModel());

        buildTree();
    }

    private void buildTree() throws IOException {
        this.dataObject.getOntModel().open(OntologyNodes.DEFAULT_ORDER, OntologyModel.ActionEnum.EDITING);
        AsConceptNode rootNode = new AsConceptNode(this.dataObject.getOntModel(), beh);
        mgr.setRootContext(rootNode);
/*
        if (!this.dataObject.getOntModel().getScope().equals(ScopeMap.Scope.BASE.toString())) { //all GEVs are derived from the base ontology
            Set<ToolCategory> theseTCs = this.dataObject.getOntModel().getNodes().getIndividuals(ToolCategory.class, false);
            BaseOntology baseOntology = Utilities.actionsGlobalContext().lookup(BaseOntology.class);
            Set<ToolCategory> baseTCs = baseOntology.getAllIndividuals(ToolCategory.class, false);
            for (ToolCategory thisTC : theseTCs) {
                for (ToolCategory baseTC : baseTCs) {
                    if (baseTC.equals(thisTC)) {
                        for (EventDefinition eventDefinition : baseTC.getGenericEventDefinitionCollection()) {
                            if (eventDefinition instanceof GenericEventDefinition) {
                                GenericEventDefinition genericEventDefinition = (GenericEventDefinition) eventDefinition;
                                thisTC.getGenericEventDefinitionCollection().add(genericEventDefinition);
                            }
                        }
                        for (GenericEventDefinition gev : thisTC.getGenericEventDefinitionCollection()) {
                            gev.setToolCategoryRef(baseTC);
                            gev.getAction().getEventDefinition().add(gev);
                            gev.getProcess().getEventDefinition().add(gev);
                            for (Property prop : gev.getPropertyCollection()) {
                                prop.getEventDefinitionCollection().add(gev);
                            }
                        }
                    }
                }
            }
        }*/
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        try {
            //reload the nodes.
            buildTree();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        AsConceptNode rootNode = new AsConceptNode(this.dataObject.getOntModel(), beh);
        mgr.setRootContext(rootNode);
    }
}
