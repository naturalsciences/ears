/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.conceptlist;

import be.naturalsciences.bmdc.ears.entities.CurrentFiles;
import be.naturalsciences.bmdc.ears.entities.CurrentOntologyModels;
import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.ontology.Individuals;
import be.naturalsciences.bmdc.ears.ontology.OntologyModel;
import be.naturalsciences.bmdc.ears.ontology.entities.Action;
import be.naturalsciences.bmdc.ears.ontology.entities.Process;
import be.naturalsciences.bmdc.ears.ontology.entities.Property;
import be.naturalsciences.bmdc.ears.ontology.entities.Tool;
import be.naturalsciences.bmdc.ears.ontology.entities.ToolCategory;
import be.naturalsciences.bmdc.ears.ontology.entities.Vessel;
import be.naturalsciences.bmdc.ears.ontology.gui.AsConceptNode;
import be.naturalsciences.bmdc.ears.ontology.gui.ClassChildren;
import be.naturalsciences.bmdc.ears.ontology.gui.ClassNode;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//be.naturalsciences.bmdc.ears.ontology.conceptlist//ConceptListTopComponent//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ConceptListTopComponentTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = false)
@ActionID(category = "Window", id = "be.naturalsciences.bmdc.ears.ontology.conceptlist.ConceptListTopComponent")
@ActionReference(path = "Menu/Window/Trees", position = 2)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ConceptListTopComponentAction",
        preferredID = "ConceptListTopComponentTopComponent"
)
@Messages({
    "CTL_ConceptListTopComponentAction=View concept list",
    "CTL_ConceptListTopComponentTopComponent=View concept list",
    "HINT_ConceptListTopComponentTopComponent=View all individuals in the different tree files"
})
public final class ConceptListTopComponent extends TopComponent implements LookupListener, ExplorerManager.Provider, NodeListener/*, AsConceptEventListener*/ {

    private Lookup.Result<Individuals> individuals;
    private Lookup.Result<CurrentVessel> currentVessel;
    private Lookup.Result<CurrentFiles> currentFiles;
    private Lookup.Result<CurrentOntologyModels> currentOntologyModels;

    private final ExplorerManager mgr = new ExplorerManager();
    private Map<Class, ClassNode> nodes;

    private Set<File> ontologyFiles;

    @Override
    public ExplorerManager getExplorerManager() {
        return mgr;
    }

    public ConceptListTopComponent() {
        initComponents();
        setName(Bundle.CTL_ConceptListTopComponentTopComponent());
        setToolTipText(Bundle.HINT_ConceptListTopComponentTopComponent());
        associateLookup(ExplorerUtils.createLookup(mgr, getActionMap()));
        //associateLookup(ExplorerUtils.createLookup(mgr, getActionMap()));
        setLayout(new BorderLayout());

        BeanTreeView treeView = new BeanTreeView();
        treeView.setRootVisible(false);
        treeView.setQuickSearchAllowed(true);
        add(treeView, BorderLayout.CENTER);

        currentOntologyModels = Utilities.actionsGlobalContext().lookupResult(CurrentOntologyModels.class);
        currentOntologyModels.addLookupListener(this);

        currentFiles = Utilities.actionsGlobalContext().lookupResult(CurrentFiles.class);
        currentFiles.addLookupListener(this);

        individuals = Utilities.actionsGlobalContext().lookupResult(Individuals.class);
        individuals.addLookupListener(this);

        currentVessel = Utilities.actionsGlobalContext().lookupResult(CurrentVessel.class);
        currentVessel.addLookupListener(this);

        ontologyFiles = new THashSet<>();
        //this.ontologyResult = new THashSet<>();
        this.nodes = new THashMap<>();
        this.nodes.put(ToolCategory.class, new ClassNode(ToolCategory.class, new THashSet<>()));
        this.nodes.put(Tool.class, new ClassNode(Tool.class, new THashSet<>()));
        this.nodes.put(Process.class, new ClassNode(Process.class, new THashSet<>()));
        this.nodes.put(Action.class, new ClassNode(Action.class, new THashSet<>()));
        this.nodes.put(Property.class, new ClassNode(Property.class, new THashSet<>()));
        this.nodes.put(Vessel.class, new ClassNode(Vessel.class, new THashSet<>()));

        Node[] nodes = {this.nodes.get(ToolCategory.class), this.nodes.get(Tool.class), this.nodes.get(Process.class), this.nodes.get(Action.class), this.nodes.get(Property.class), this.nodes.get(Vessel.class)};

        Children ch = new Children.Array();
        ch.add(nodes);
        Node rootNode = new AbstractNode(ch);

        mgr.setRootContext(rootNode);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
    public void componentOpened() {
        Collection<? extends CurrentOntologyModels> allModels = currentOntologyModels.allInstances();
        Collection<Individuals> allInstances = (Collection<Individuals>) individuals.allInstances();
        for (CurrentOntologyModels result : allModels) {
            if (result != null) {
                for (OntologyModel ontologyModel : result.getConcept()) {
                    ontologyModel.getIndividuals().addNodeListener(this);
                    ontologyModel.getIndividuals().addPropertyChangeListener(this);
                    File file = ontologyModel.getFile();
                    reAdd(allInstances, file);

                }
            }
        }
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");

    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    private void reAdd(Collection<Individuals> ontologyDataObjects, File file) {
        //clearNodes();
        if (!ontologyDataObjects.isEmpty()) {
            for (Individuals individuals : ontologyDataObjects) {
                if (individuals.getModel().getFile().equals(file)) {
                    reAdd(individuals);
                }
            }
        }
    }

    private void reAdd(Individuals individuals) {
        Iterator it2 = this.nodes.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry) it2.next();
            Class cls = (Class) pair.getKey();
            Node[] nodes = ClassChildren.createClassNodes(cls, individuals, ClassChildren.Operations.ADD);
            ((ClassNode) pair.getValue()).getChildren().add(nodes);

            //Logger.getLogger(ConceptListTopComponent.class.getName()).log(Level.INFO, "reAdd: Added " + nodes.length + " nodes in " + cls.getSimpleName());
        }
    }

    private void reAddAsConcept(Collection<AsConcept> individuals) {
        Iterator it2 = this.nodes.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry) it2.next();
            Class cls = (Class) pair.getKey();
            Node[] nodes = ClassChildren.createClassNodes(cls, individuals, ClassChildren.Operations.ADD);
            if (nodes.length > 0) {
                boolean add = ((ClassNode) pair.getValue()).getChildren().add(nodes);
            }
            //Logger.getLogger(ConceptListTopComponent.class.getName()).log(Level.INFO, "reAddAsConcept: Added " + nodes.length + " nodes in " + cls.getSimpleName());
        }
    }

    private void reMove(Collection<Individuals> ontologyDataObjects, File file) {
        //clearNodes();
        if (!ontologyDataObjects.isEmpty()) {
            for (Individuals individuals : ontologyDataObjects) {
                if (individuals.getModel().getFile().equals(file)) {
                    reMove(individuals);
                }
            }
        }
    }

    private void reMove(Individuals individuals) {
        Iterator it2 = this.nodes.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry) it2.next();
            Class cls = (Class) pair.getKey();
            Node[] nodes = ClassChildren.createClassNodes(cls, individuals, ClassChildren.Operations.REMOVE);
            try {
                ((ClassNode) pair.getValue()).getChildren().remove(nodes);
            } catch (IllegalStateException ex) {
                Logger.getLogger(ConceptListTopComponent.class.getName()).log(Level.INFO, "", ex);
            }

            //Logger.getLogger(ConceptListTopComponent.class.getName()).log(Level.INFO, "reMove: Removed " + nodes.length + " nodes in " + cls.getSimpleName());
        }
    }

    private void reMoveAsConcept(Collection<AsConcept> individuals) {
        Iterator it2 = this.nodes.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry) it2.next();
            Class cls = (Class) pair.getKey();
            Node[] nodes = ClassChildren.createClassNodes(cls, individuals, ClassChildren.Operations.REMOVE);
            if (nodes.length > 0) {
                try {
                    ((ClassNode) pair.getValue()).getChildren().remove(nodes);
                } catch (IllegalStateException ex) {
                 //   Logger.getLogger(ConceptListTopComponent.class.getName()).log(Level.INFO, "", ex);
                }
            }
            //Logger.getLogger(ConceptListTopComponent.class.getName()).log(Level.INFO, "reMoveAsConcept: Removed " + nodes.length + " nodes in " + cls.getSimpleName());
        }
    }

    private void reNameAsConcept(AsConcept concept) {
        Iterator it2 = this.nodes.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry) it2.next();
            Node child = ((ClassNode) pair.getValue()).getChildren().findChild(concept.getTermRef().getOrigUrn());
            if (child != null) {
                String newName = concept.getTermRef().getEarsTermLabel().getPrefLabel();

                child.setDisplayName(newName);
                child.setName(newName);
               //Logger.getLogger(ConceptListTopComponent.class.getName()).log(Level.INFO, "reNameAsConcept: Renamed " + child.getDisplayName() + ".");
            }
        }
    }

    private void clearNodes() {
        Iterator it = this.nodes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Class, ClassNode> pair = (Map.Entry) it.next();
            Class cls = pair.getKey();
            ClassNode node = pair.getValue();
            Node[] nodeChildren = node.getChildren().getNodes();
            node.getChildren().remove(nodeChildren);
        }
    }

    private void refresh(Class cls) {
        Collection<Individuals> ontologyDataObjects = (Collection<Individuals>) individuals.allInstances();
        if (!ontologyDataObjects.isEmpty()) {
            for (Individuals individuals : ontologyDataObjects) {
                if (!individuals.get(cls).isEmpty()) {
                    this.nodes.get(cls).getChildren().add(ClassChildren.createClassNodes(cls, individuals, ClassChildren.Operations.ADD));
                }
            }
        }
        /* if (cls.equals(Vessel.class)) {
            // currentVessel.allInstances();
            CurrentVessel cv = getOneResult(currentVessel);
            cv.
            this.nodes.get(Vessel.class).getChildren().add(ClassChildren.createClassNodes(Vessel.class, individuals, ClassChildren.Operations.ADD));
        }*/
    }

    public static <C extends Object> C getOneResult(Lookup.Result<C> results) {
        for (C result : results.allInstances()) {
            return result;
        }
        return null;
    }

    @Override
    public void resultChanged(LookupEvent le) {
        if (le.getSource().equals(currentVessel)) {
            Node[] vesselNodes = this.nodes.get(Vessel.class).getChildren().getNodes();

            this.nodes.get(Vessel.class).getChildren().remove(vesselNodes);
            refresh(Vessel.class);
        }
        if (le.getSource().equals(currentOntologyModels)) {
            CurrentOntologyModels result = getOneResult(currentOntologyModels);
            if (result != null) {
                Set<File> ontologyFilesBefore = new THashSet<>(ontologyFiles);
                ontologyFiles.clear();

                for (OntologyModel ontologyModel : result.getConcept()) {
                    ontologyFiles.add(ontologyModel.getFile());
                }

                Set<File> removed = new THashSet<File>(ontologyFilesBefore);
                removed.removeAll(ontologyFiles);

                Set<File> same = new THashSet<File>(ontologyFilesBefore);
                same.retainAll(ontologyFiles);

                Set<File> added = new THashSet<File>(ontologyFiles);
                added.removeAll(ontologyFilesBefore);
                Collection<Individuals> allInstances = (Collection<Individuals>) individuals.allInstances();

                for (File removedFile : removed) {
                    if (removedFile != null) {
                        Logger.getLogger(ConceptListTopComponent.class
                                .getName()).log(Level.INFO, "Closed " + removedFile.getName());
                        reMove(allInstances, removedFile);
                        for (OntologyModel ontologyModel : result.getConcept()) {
                            if (ontologyModel.getFile().equals(removedFile)) {
                                ontologyModel.getIndividuals().removeNodeListener(this);
                                ontologyModel.getIndividuals().removePropertyChangeListener(this);
                            }
                        }
                    }
                }
                for (File sameFile : same) {
                    if (sameFile != null) {
                        reAdd(allInstances, sameFile);
                        for (OntologyModel ontologyModel : result.getConcept()) {
                            if (ontologyModel.getFile().equals(sameFile)) {
                                ontologyModel.getIndividuals().removeNodeListener(this);
                                ontologyModel.getIndividuals().removePropertyChangeListener(this);

                                ontologyModel.getIndividuals().addNodeListener(this);
                                ontologyModel.getIndividuals().addPropertyChangeListener(this);
                            }
                        }
                    }
                }

                for (File addedFile : added) {
                    if (addedFile != null) {
                        Logger.getLogger(ConceptListTopComponent.class
                                .getName()).log(Level.INFO, "Opened " + addedFile.getName());
                        reAdd(allInstances, addedFile);
                        for (OntologyModel ontologyModel : result.getConcept()) {
                            if (ontologyModel.getFile().equals(addedFile)) {
                                ontologyModel.getIndividuals().addNodeListener(this);
                                ontologyModel.getIndividuals().addPropertyChangeListener(this);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void childrenAdded(NodeMemberEvent nme) {
        Node[] delta = nme.getDelta();
        List<AsConcept> concepts = new ArrayList<>();
        for (Node node : delta) {
            if (node instanceof AsConceptNode) {
                AsConceptNode node2 = (AsConceptNode) node;
                if (!node2.getConcept().getTermRef().isDeprecated()) {
                    concepts.add(node2.getConcept());
                }
            }
        }
        reAddAsConcept(concepts);
    }

    @Override
    public void childrenRemoved(NodeMemberEvent nme) {
        Node[] delta = nme.getDelta();
        List<AsConcept> concepts = new ArrayList<>();
        for (Node node : delta) {
            if (node instanceof AsConceptNode) {
                AsConceptNode node2 = (AsConceptNode) node;
                if (!node2.getConcept().getTermRef().isDeprecated()) {
                    concepts.add(node2.getConcept());
                }
            }
        }
        
        reMoveAsConcept(concepts);
    }

    @Override
    public void childrenReordered(NodeReorderEvent nre) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void nodeDestroyed(NodeEvent ne) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void propertyChange(PropertyChangeEvent ev) {
        if ("prefLabel".equals(ev.getPropertyName())) {
            if (ev.getSource() instanceof AsConceptNode) {
                AsConceptNode node = (AsConceptNode) ev.getSource();
                reNameAsConcept(node.getConcept());
            }
        }

        //reMoveAsConcept(concepts);
        //reAddAsConcept(concepts);
    }

    /*@Override
    public void nodeAdded(AsConceptEvent ace) {
        List<AsConcept> concepts = new ArrayList<>();
        concepts.add(ace.getConceptThatChanged());
        reAddAsConcept(concepts);
    }

    @Override
    public void nodeDestroyed(AsConceptEvent ace) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void nodeRenamed(AsConceptEvent ace) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
}
