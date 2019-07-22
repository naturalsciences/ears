/*//GEN-LINE:variables
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.treeviewer;

import be.naturalsciences.bmdc.ears.entities.Actor;
import be.naturalsciences.bmdc.ears.ontology.OntologyModel;
import be.naturalsciences.bmdc.ears.ontology.OntologyNodes;
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
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
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
//@ActionID(category = "Window", id = "be.naturalsciences.bmdc.ears.ontology.treeviewer.OntologyTreeViewer")
//@ActionReference(path = "Menu/Window/Trees")
//@TopComponent.OpenActionRegistration(displayName = "#CTL_OntologyTreeViewerAction")
@NbBundle.Messages({"CTL_OntologyTreeViewerAction=Browse tree",
    "CTL_OntologyTreeViewer=Browse individuals",
    "HINT_OntologyTreeViewer=Open and browse a tree"})
public class OntologyTreeViewer extends TopComponent implements LookupListener, ExplorerManager.Provider, ChangeListener, NodeDonor {

    private Lookup.Result<Actor> resultActor = null;
    private Lookup globalLookup;

    /*private String ActorFormLookup = "NoActor";

     public void setActorFormLookup(String ActorFormLookup) {
     this.ActorFormLookup = ActorFormLookup;
     }

     public String getActorFormLookup() {
     return ActorFormLookup;
     }*/
    //private Lookup lookup;
    //private MultiDataObject obj;
    private IOntologyModel ontModel;

    private BeanTreeView treeView;

    private final InstanceContent lookupContent = new InstanceContent();
    private final ExplorerManager mgr = new ExplorerManager();

    private Lookup lookup;

    @Override
    public ExplorerManager getExplorerManager() {
        return mgr;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    public IOntologyModel getOntModel() {
        return ontModel;
    }

    public BeanTreeView getTreeView() {
        return treeView;
    }

    /**
     * Creates new form OntologyEditor
     */
    public OntologyTreeViewer(RdfFileTypeDataObject dataObject) throws FileNotFoundException, IOException {
        if (dataObject != null) {
            String fileName = dataObject.getPrimaryFile().getName();
            initComponents();

            setName(Bundle.CTL_OntologyTreeViewer() + " of " + fileName);
            setToolTipText(Bundle.HINT_OntologyTreeViewer());

            treeView = new BeanTreeView();
            treeView.setRootVisible(false);
            treeView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            lookup = new ProxyLookup(new AbstractLookup(lookupContent), Lookups.fixed(new ToEventConvertibleNode(), new CollapsibleConceptNodes(mgr, treeView), new ExpandableConceptNodes(mgr, treeView)), ExplorerUtils.createLookup(mgr, getActionMap()));
            associateLookup(lookup);

            this.ontModel = dataObject.getOntModel();//dataObject.getLookup().lookup(OntologyModel.class);
            this.ontModel.open(OntologyNodes.DEFAULT_ORDER, OntologyModel.Action.BROWSING);

            mgr.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(mgr.PROP_SELECTED_NODES)) {
                        //System.out.println("propertyChange== " + mgr.getSelectedNodes().toString() + getActorFormLookup());
                        resultChanged(null);
                    }
                }
            });

            setLayout(new BorderLayout());
            add(treeView, BorderLayout.CENTER);
            this.lookupContent.add(treeView);
            this.lookupContent.add(this.ontModel); //necessary for concept list
            mgr.setRootContext(new AsConceptNode(ontModel, AsConceptNode.BROWSE_BEHAVIOUR));

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

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(OntologyTreeViewer.class, "OntologyTreeViewer.jLabel1.text")); // NOI18N

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
            return OntologyTreeViewer.this.getOntModel();
        }
    }

    @Override
    public void componentClosed() {
        this.ontModel.close(OntologyModel.Action.BROWSING);
    }
}
