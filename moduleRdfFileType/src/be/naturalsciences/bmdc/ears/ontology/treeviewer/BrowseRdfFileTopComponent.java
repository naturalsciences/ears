/*//GEN-LINE:variables
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.treeviewer;

import be.naturalsciences.bmdc.ears.entities.Actor;
import be.naturalsciences.bmdc.ears.entities.CurrentOntologyModels;
import be.naturalsciences.bmdc.ears.ontology.BaseOntology;
import be.naturalsciences.bmdc.ears.ontology.OntologyModel;
import be.naturalsciences.bmdc.ears.ontology.OntologyNodes;
import be.naturalsciences.bmdc.ears.ontology.entities.EventDefinition;
import be.naturalsciences.bmdc.ears.ontology.entities.GenericEventDefinition;
import be.naturalsciences.bmdc.ears.ontology.entities.Property;
import be.naturalsciences.bmdc.ears.ontology.entities.ToolCategory;
import be.naturalsciences.bmdc.ears.ontology.gui.AsConceptNode;
import be.naturalsciences.bmdc.ears.ontology.gui.NodeDonor;
import be.naturalsciences.bmdc.ears.ontology.gui.ToEventConvertible;
import be.naturalsciences.bmdc.ears.ontology.rdf.CollapsibleConceptNodes;
import be.naturalsciences.bmdc.ears.ontology.rdf.ExpandableConceptNodes;
import be.naturalsciences.bmdc.ears.ontology.rdf.RdfFileTypeDataObject;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.writer.ScopeMap;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;

/**
 *
 * @author Thomas Vandenberghe
 */
@TopComponent.Description(preferredID = "OntologyTreeViewer", persistenceType = TopComponent.PERSISTENCE_ALWAYS)
//@NavigatorPanel.Registration(mimeType = "application/rdf+xml", displayName = "Browse tree")
@TopComponent.Registration(mode = "explorer", openAtStartup = false)
//@ActionID(category = "Window", id = "be.naturalsciences.bmdc.ears.ontology.treeviewer.BrowseRdfFileTopComponent")
//@ActionReference(path = "Menu/Window/Trees")
//@TopComponent.OpenActionRegistration(displayName = "#CTL_OntologyTreeViewerAction")
@NbBundle.Messages({"CTL_OntologyTreeViewerAction=Browse tree",
    "CTL_OntologyTreeViewer=Browse terms",
    "HINT_OntologyTreeViewer=Open and browse a tree"})
public class BrowseRdfFileTopComponent extends TopComponent implements LookupListener, ExplorerManager.Provider, ChangeListener, NodeDonor {

    private Lookup.Result<Actor> resultActor = null;
    private Lookup globalLookup;

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
     * Creates new form OntologyEditor
     */
    public BrowseRdfFileTopComponent(final RdfFileTypeDataObject dataObject) throws FileNotFoundException, IOException {
        if (dataObject != null) {
            String fileName = dataObject.getPrimaryFile().getName();
            initComponents();

            setName(Bundle.CTL_OntologyTreeViewer() + " of " + fileName);
            setToolTipText(Bundle.HINT_OntologyTreeViewer());

            initTree(dataObject);

            this.lookup = new ProxyLookup(new AbstractLookup(lookupContent), Lookups.fixed(new ToEventConvertibleNode(), new CollapsibleConceptNodes(mgr, treeView), new ExpandableConceptNodes(mgr, treeView)), ExplorerUtils.createLookup(mgr, getActionMap()));
            this.lookupContent.add(treeView); //needed to add node in the properties pane
            associateLookup(lookup); //needed to add node in the properties pane

            mgr.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(mgr.PROP_SELECTED_NODES)) {
                        //System.out.println("propertyChange== " + mgr.getSelectedNodes().toString() + getActorFormLookup());
                        resultChanged(null);
                    }
                }
            });
        }
    }

    private void initTree(final RdfFileTypeDataObject dataObject) throws IOException {

        this.dataObject = dataObject;
        this.treeView = new BeanTreeView();
        this.treeView.setRootVisible(false);
        setLayout(new BorderLayout());
        add(this.treeView, BorderLayout.CENTER);
        this.treeView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.lookupContent.add(this.dataObject.getOntModel());

        this.dataObject.getOntModel().open(OntologyNodes.DEFAULT_ORDER, OntologyModel.ActionEnum.BROWSING);
        AsConceptNode rootNode = new AsConceptNode(this.dataObject.getOntModel(), AsConceptNode.BROWSE_BEHAVIOUR);
        mgr.setRootContext(rootNode);

        if (!this.dataObject.getOntModel().getScope().equals(ScopeMap.Scope.BASE.toString())) { //all GEVs are derived from the base ontology
            Set<ToolCategory> theseTCs = this.dataObject.getOntModel().getNodes().getIndividuals(ToolCategory.class, false);
            BaseOntology baseOntology = Utilities.actionsGlobalContext().lookup(BaseOntology.class);
            // CurrentOntologyModels currentModels = Utilities.actionsGlobalContext().lookup(CurrentOntologyModels.class);
            //Set<ToolCategory> baseTCs = currentModels.getBaseOntology().getAllIndividuals(ToolCategory.class, false);
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
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(BrowseRdfFileTopComponent.class, "OntologyTreeViewer.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabel1)
                                .addContainerGap(702, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addContainerGap(270, Short.MAX_VALUE))
        );
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    private javax.swing.JLabel jLabel1;
    // End of variables declaration                   

    @Override
    public void resultChanged(LookupEvent ev) {
        Collection<? extends Actor> allActors = resultActor.allInstances();
        if (!allActors.isEmpty()) {
            Actor actor = allActors.iterator().next();
            //setActorFormLookup(actor.getNameOfActor());
        }
    }

    @Override
    protected void componentOpened() {
        globalLookup = Utilities.actionsGlobalContext();
        resultActor = globalLookup.lookupResult(Actor.class);
        resultActor.addLookupListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class ToEventConvertibleNode implements ToEventConvertible {

        @Override
        public void convertToEvent() {
            AsConceptNode node = (AsConceptNode) mgr.getSelectedNodes()[0];
            try {
                ToEventConvertible.convertToEventAndAddToLookup(node);
            } catch (EarsException ex) {
                Messaging.report(ex.getMessage(), ex, this.getClass(), true);
            }
        }

        @Override
        public AsConcept getConcept() {
            AsConceptNode node = (AsConceptNode) mgr.getSelectedNodes()[0];
            return node.getConcept();
        }

        public IOntologyModel getOntologyModel() {
            return dataObject.getOntModel();
        }
    }

    @Override
    public void componentClosed() {
        this.dataObject.getOntModel().close(OntologyModel.ActionEnum.BROWSING);
    }
}
