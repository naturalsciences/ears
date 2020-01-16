/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.rdf;

import be.naturalsciences.bmdc.ears.ontology.AsConceptFactory;
import be.naturalsciences.bmdc.ears.ontology.OntologyModel;
import be.naturalsciences.bmdc.ears.ontology.OntologyNodes;
import be.naturalsciences.bmdc.ears.ontology.gui.AsConceptNode;
import be.naturalsciences.bmdc.ears.ontology.gui.ToEventConvertible;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Thomas Vandenberghe
 */
public class RdfVisualElementPanel extends JPanel implements ExplorerManager.Provider, Lookup.Provider, ChangeListener {

    private Lookup lookup;

    private final ExplorerManager mgr = new ExplorerManager();

    private InstanceContent lookupContent = new InstanceContent();
    private RdfFileTypeDataObject dobj;
    private BeanTreeView treeView;

    private javax.swing.JLabel jLabel1;

    private AsConceptNode.ContextBehaviour beh;

    @Override
    public ExplorerManager getExplorerManager() {
        return mgr;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    /*public InstanceContent getIc() {
     return lookupContent;
     }*/
    public BeanTreeView getTreeView() {
        return treeView;
    }

    /**
     *
     * @param dobj
     * @param beh
     */
    public RdfVisualElementPanel(final RdfFileTypeDataObject dobj, AsConceptNode.ContextBehaviour beh) throws IOException {
        jLabel1 = new javax.swing.JLabel();
        this.dobj = dobj;
        this.beh = beh;
        this.beh.factory = new AsConceptFactory(this.dobj.getOntModel());

        treeView = new BeanTreeView();
        treeView.setRootVisible(true);
        setLayout(new BorderLayout());
        add(treeView, BorderLayout.CENTER);
        lookup = new ProxyLookup(Lookups.fixed(/*new AbstractLookup(lookupContent), */new ToEventConvertibleNode(), new CollapsibleConceptNodes(mgr, treeView), new ExpandableConceptNodes(mgr, treeView)), ExplorerUtils.createLookup(mgr, this.getActionMap()));

        lookupContent.add(this.dobj.getOntModel());
        initTree();
    }

    private void initTree() throws IOException {
        this.dobj.getOntModel().open(OntologyNodes.DEFAULT_ORDER, OntologyModel.ActionEnum.EDITING);
        AsConceptNode rootNode = new AsConceptNode(this.dobj.getOntModel(), this.beh);

        mgr.setRootContext(rootNode);
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        try {
            //reload the nodes.
            initTree();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        AsConceptNode rootNode = new AsConceptNode(this.dobj.getOntModel(), this.beh);
        mgr.setRootContext(rootNode);
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

        @Override
        public IOntologyModel getOntologyModel() {
            return RdfVisualElementPanel.this.dobj.getOntModel();
        }
    }

}
