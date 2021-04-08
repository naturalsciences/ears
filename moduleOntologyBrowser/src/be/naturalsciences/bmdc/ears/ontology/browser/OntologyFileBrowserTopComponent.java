/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.browser;

import be.naturalsciences.bmdc.ears.ontology.rdf.DirectoryChangeListener;
import be.naturalsciences.bmdc.ears.properties.Constants;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ears.utils.NotificationThread;
import be.naturalsciences.bmdc.ears.utils.TaskListener;
import be.naturalsciences.bmdc.ontology.writer.ScopeMap;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.windows.TopComponent;

/**
 *
 * @author Thomas Vandenberghe
 */
@TopComponent.Description(preferredID = "OntologyFileBrowserTopComponent", persistenceType = TopComponent.PERSISTENCE_ALWAYS)
//@NavigatorPanel.Registration(mimeType = "application/rdf+xml", displayName = "Browse tree")
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "be.naturalsciences.bmdc.ontology.browser.OntologyFileBrowserTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Window/Trees", position = 1)
})

@TopComponent.OpenActionRegistration(displayName = "#CTL_OntologyFileBrowserAction", preferredID = "OntologyFileBrowser")
@NbBundle.Messages({"CTL_OntologyFileBrowserAction=Browse trees",
    "CTL_OntologyFileBrowserTopComponent=Browse trees",
    "HINT_OntologyFileBrowserTopComponent=Browse the different trees"})
public final class OntologyFileBrowserTopComponent extends TopComponent implements ExplorerManager.Provider, DirectoryChangeListener, TaskListener {

    private final ExplorerManager mgr = new ExplorerManager();

    private static final ScopeNode BASE_NODE = new ScopeNode(new Children.Array(), "Base tree", ScopeMap.BASE_SCOPE.getScopeString(), Constants.BASE_ONTOLOGY_FILE);
    private static final ScopeNode VESSEL_NODE = new ScopeNode(new Children.Array(), "Vessel tree", ScopeMap.VESSEL_SCOPE.getScopeString(), Constants.VESSEL_ONTOLOGY_FILE);
    private static final ScopeNode PROGRAM_NODE = new ScopeNode(new Children.Array(), "Program & test trees", new String[]{ScopeMap.PROGRAM_SCOPE.getScopeString(), ScopeMap.TEST_SCOPE.getScopeString()}, Constants.PROGRAM_ONTOLOGY_DIR);

    public class OntologyRetriever extends NotificationThread {

        ProgressHandle progr;

        ScopeNode node;

        public OntologyRetriever(ProgressHandle progr, ScopeNode node) {
            this.progr = progr;
            this.node = node;
        }

        @Override
        public void doWork() {
            progr.start();
            try {
                progr.progress("Starting to build " + node.getScope() + " nodes.");
                node.populateNode();
            } catch (OutOfMemoryError ex) {
                Messaging.report("OutOfMemoryException while opening the " + node.getScope() + " node(s).", ex, this.getClass(), true);
            } finally {
                progr.finish();
            }

        }
    }

    /**
     * Creates a new OntologyFileBrowser TopComponent
     */
    public OntologyFileBrowserTopComponent() {
        initComponents();
        associateLookup(ExplorerUtils.createLookup(mgr, getActionMap()));
        setLayout(new BorderLayout());
        BeanTreeView treeView = new BeanTreeView();
        treeView.setRootVisible(false);
        treeView.setQuickSearchAllowed(true);
        add(treeView, BorderLayout.CENTER);

        setDisplayName("Browse trees");
        startOntologyRetrieval("Retrieving base tree...", BASE_NODE);
        startOntologyRetrieval("Retrieving vessel tree...", VESSEL_NODE);
        startOntologyRetrieval("Retrieving program & test trees...", PROGRAM_NODE);

        Node[] scopeNodes = {BASE_NODE, VESSEL_NODE, PROGRAM_NODE};
        Children ch = new Children.Array();
        ch.add(scopeNodes);
        Node rootNode = new AbstractNode(ch);

        mgr.setRootContext(rootNode);
    }

    public static ScopeNode getBaseNode() {
        return BASE_NODE;
    }

    public static ScopeNode getVesselNode() {
        return VESSEL_NODE;
    }

    public static ScopeNode getProgramNode() {
        return PROGRAM_NODE;
    }

    RequestProcessor processor = new RequestProcessor("Ontology_retrieval", 3);

    public void startOntologyRetrieval(String progressDescription, ScopeNode node) {
        ProgressHandle progr = ProgressHandleFactory.createSystemHandle(progressDescription);//YS createHandle(progressDescription);
        OntologyFileBrowserTopComponent.OntologyRetriever tsk = new OntologyFileBrowserTopComponent.OntologyRetriever(progr, node);
        tsk.addListener(this);
        processor.post(tsk);
    }

    /**
     * *
     * Assigns a provided String name, a String scope and a File fileOrFolder to
     * a provided node and populates the node with its children. Returns the
     * created ontologyModels. are
     *
     * @param node
     * @param name
     * @param scope
     * @param fileOrFolder
     * @return
     */
    /*public static Set<IOntologyModel> open(ScopeNode node) {
        //node.populateNode();
        return node.getOntologyModels();
    }*/
    @Override
    public void threadComplete(Runnable runner) {
        //progr.finish();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public ExplorerManager getExplorerManager() {
        return mgr;
    }

    @Override
    public void refresh() {
        PROGRAM_NODE.refresh();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        refresh();
    }
}
