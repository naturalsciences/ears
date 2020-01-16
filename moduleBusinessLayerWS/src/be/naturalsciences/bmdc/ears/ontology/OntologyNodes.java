/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

import be.naturalsciences.bmdc.ears.comparator.TermIdentityHashCodeComparator;
import be.naturalsciences.bmdc.ears.comparator.TermKindComparator;
import be.naturalsciences.bmdc.ears.entities.CurrentUser;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.User;
import be.naturalsciences.bmdc.ears.ontology.entities.Action;
import be.naturalsciences.bmdc.ears.ontology.entities.EarsTerm;
import be.naturalsciences.bmdc.ears.ontology.entities.EventDefinition;
import be.naturalsciences.bmdc.ears.ontology.entities.GenericEventDefinition;
import be.naturalsciences.bmdc.ears.ontology.entities.Process;
import be.naturalsciences.bmdc.ears.ontology.entities.ProcessAction;
import be.naturalsciences.bmdc.ears.ontology.entities.Property;
import be.naturalsciences.bmdc.ears.ontology.entities.SpecificEventDefinition;
import be.naturalsciences.bmdc.ears.ontology.entities.Tool;
import be.naturalsciences.bmdc.ears.ontology.entities.ToolCategory;
import be.naturalsciences.bmdc.ears.properties.Constants;
import be.naturalsciences.bmdc.ears.rest.RestClientOnt;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IOntologyNodes;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.IEventDefinition;
import be.naturalsciences.bmdc.ontology.entities.IFakeConcept;
import be.naturalsciences.bmdc.ontology.writer.EARSOntologyCreator;
import be.naturalsciences.bmdc.ontology.writer.ScopeMap;
import gnu.trove.set.hash.THashSet;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import thewebsemantic.binding.Jenabean;

/**
 *
 * @author thomas
 */
public class OntologyNodes<T extends AsConcept> implements IOntologyNodes<T> {

    private IFakeConcept root;
    private Set<T> nodes;
    private OntologyModel model;
    private final ArrayDeque<Class<? extends AsConcept>> classes;
    //private TObjectIntHashMap<Class<? extends AsConcept>> lastIds;
    private boolean killModel;
    private boolean saved;

    private static Comparator DEFAULT_COMPARATOR = new TermIdentityHashCodeComparator();

    public static final ArrayDeque<Class<? extends AsConcept>> DEFAULT_ORDER = new ArrayDeque();

    static {
        DEFAULT_ORDER.add(ToolCategory.class);
        DEFAULT_ORDER.add(Tool.class);
        DEFAULT_ORDER.add(be.naturalsciences.bmdc.ears.ontology.entities.Process.class);
        DEFAULT_ORDER.add(Action.class);
        DEFAULT_ORDER.add(be.naturalsciences.bmdc.ears.ontology.entities.Property.class);
    }

    public IFakeConcept getRoot() {
        return root;
    }

    public void setRoot(IFakeConcept root) {
        this.root = root;
    }

    public Set<T> getNodes() {
        return nodes;
    }

    public OntologyModel getModel() {
        return model;
    }

    public boolean killModel() {
        return killModel;
    }

    public boolean isSaved() {
        return saved;
    }

    public OntologyNodes(OntologyModel model, ArrayDeque<Class<? extends AsConcept>> classes, boolean killModel) {
        if (model == null) {
            throw new IllegalArgumentException("A model must be provided.");
        }
        if (classes == null) {
            throw new IllegalArgumentException("A deque of classes must be provided to build the nodes.");
        }
        this.model = model;
        this.classes = classes;
        this.killModel = killModel;
        //this.lastIds = new TObjectIntHashMap<>();
        try {
            buildNodes();
        } catch (EarsException ex) {
            Messaging.report("Could not load these tree nodes.", ex, this.getClass(), true);
            this.nodes = null;
        }
    }

    public Class<? extends AsConcept> getType() {
        return classes.getFirst();
    }

    private void buildNodes() throws EarsException {
        if ((this.nodes == null && !model.isEditable()) || model.isEditable()) { //if not editable only do once, if editable nodes can be recreated any time.
            Jenabean b = Jenabean.instance();
            b.bind(this.model.getJenaModel());
            //b.reader().bind(AsConcept.class);

            b.reader().bind(EarsTerm.class);
            b.reader().bind(GenericEventDefinition.class);
            b.reader().bind(SpecificEventDefinition.class);
            for (Class<? extends AsConcept> cls : this.classes) {
                b.reader().bind(cls);
            }
            long t1 = System.currentTimeMillis();

            Jenabean.include("alternateNames");

            this.nodes = new THashSet(b.reader().load(getType()));

            for (AsConcept node : nodes) {
                ToolCategory tc = (ToolCategory) node;
                try {
                    node.getTermRef().fixSubstitutes();
                } catch (Exception e) {
                    Messaging.report("An exception occured when fixing the substitutes of " + tc.getUri() + " or one of its descendants", e, this.getClass(), false);
                }
                Set<AsConcept> l = getAllChildren(node, false);
                try {
                    tc.reduceGevsToSevs(new AsConceptFactory(model));
                } catch (EarsException ex) {
                    Messaging.report("An exception occured when reducing the GenericEventDefinitions to Specific ones.", ex, this.getClass(), false);
                }

                for (AsConcept node2 : l) {
                    if (node2 instanceof Tool) {

                        Tool tool = (Tool) node2;
                        for (EventDefinition e : tool.getEventDefinitionCollection()) {
                            e.linkMyProcessAndActionBackToMe();
                        }

                    }
                    node2.getTermRef().fixSubstitutes();
                }
            }
            long t2 = System.currentTimeMillis();

            System.out.println("time taken to load all nodes: " + (t2 - t1));
            if (this.killModel) {
                this.model = null;
            }
        }
    }

    private static ConceptHierarchy previousNodesInPath = new ConceptHierarchy();

    /**
     * *
     * Recursively gets all distinct children, sorted or unsorted, of AsConcept
     * c and return them in a linear Set<AsConcept>. Based on parent-child
     * relationships.
     *
     * @param c
     * @return
     */
    public static Set<AsConcept> getAllChildren(AsConcept c, boolean sorted) {
        Set<AsConcept> r;
        if (sorted) {
            r = getAllChildren(c, new TreeSet<>(new TermKindComparator()));
        } else {
            r = getAllChildren(c, new THashSet<>());

        }
        previousNodesInPath.removeAll();
        return r;
    }

    /**
     * *
     * Recursively gets all distinct children of Class cls, sorted or unsorted,
     * of AsConcept c and return them in a linear Set<AsConcept>. Based on
     * parent-child relationships.
     *
     * @param <C>
     * @param c
     * @param sorted
     * @param cls
     * @return
     */
    public static <C extends AsConcept> Set<C> getAllChildren(C c, boolean sorted, Class<C> cls) {
        Set<C> r;
        if (sorted) {
            r = getAllChildren(c, new TreeSet<>(new TermKindComparator()), cls);
        } else {
            r = getAllChildren(c, new THashSet<>(), cls);
        }
        previousNodesInPath.removeAll();
        return r;
    }

    /**
     * *
     * Recursively gets all distinct children of AsConcept c and return them in
     * a linear Set<AsConcept>. Based on parent-child relationships.
     *
     * @param c
     * @return
     */
    static Set<AsConcept> getAllChildren(AsConcept c, Set<AsConcept> r) {
        Set<AsConcept> ch = c.getChildren(previousNodesInPath);
        if (ch != null && ch.size() > 0 && r != null) { //if there are any children
            previousNodesInPath.removeOfType(c);
            previousNodesInPath.add(c);
            for (Object child : ch) {
                AsConcept c2 = (AsConcept) child;
                if (c2.getTermRef() != null) {
                    r.add(c2);
                }
                getAllChildren(c2, r);
            }
        }
        return r; //return the result
    }

    /**
     * *
     * Recursively gets all distinct children of AsConcept c of Class cls and
     * return them in a linear Set<C extends AsConcept>. Based on parent-child
     * relationships.
     *
     * @param c
     * @return
     */
    static <C extends AsConcept> Set<C> getAllChildren(AsConcept c, Set<C> r, Class<C> cls) {
        if (c != null && c.getTermRef() != null && c.getTermRef().getEarsTermLabel() != null && c.getTermRef().getEarsTermLabel().getPrefLabel() != null) {

            if (c.getTermRef().getEarsTermLabel().getPrefLabel().equals("Rosette")) {
                int a = 5;
            }
            if (c.getTermRef().getEarsTermLabel().getPrefLabel().equals("Profile")) {
                int a = 5;
            }
            if (c.getTermRef().getEarsTermLabel().getPrefLabel().equals("Cruise")) {
                int a = 5;
            }
        }
        Tool hostedToolNode = (Tool) previousNodesInPath.getHostedTool();
        Process processNode = (Process) previousNodesInPath.getProcess();
        Action actionNode = (Action) previousNodesInPath.getAction();
        boolean killHostedToolNode = true;
        if (hostedToolNode != null && processNode != null) {
            for (SpecificEventDefinition sev : hostedToolNode.getSpecificEventDefinitionCollection()) {
                if (sev.getProcess() == c || sev.getAction() == c || (sev.getProcess() == processNode && sev.getAction() == actionNode)) {
                    killHostedToolNode = false;
                }
            }
            if (c instanceof Process) {
                Process process = (Process) c;
                for (IEventDefinition ev : process.getEventDefinition()) {
                    if (ev instanceof SpecificEventDefinition) {
                        SpecificEventDefinition se = (SpecificEventDefinition) ev;
                        if (se.getToolRef() == hostedToolNode) {
                            killHostedToolNode = false;
                        }
                    }/* else if (ev instanceof GenericEventDefinition) {
                        GenericEventDefinition ge = (GenericEventDefinition) ev;
                        if (ge.getToolRef() == hostedToolNode) {
                            killHostedToolNode = false;
                        }
                    }*/
                }
            }
        }
        if (killHostedToolNode) {
            previousNodesInPath.setHostedTool(null);
        }
        Set<C> ch = c.getChildren(previousNodesInPath);

        //make sure that first the Processes of the own tool are handled and later the processes of the nested tool!!
        //otherwise the nestedTools are already stored as a child and the subpath is calculated from the tool's processes but with the child tool as purported tool.
        if (ch != null && ch.size() > 0 && r != null) { //if there are any children
            // boolean isHostedTool = false;
            // if (c instanceof Tool) {
            //     Tool tool = (Tool) c;
            //     isHostedTool = tool.isHostedTool();
            // }
            // if (!isHostedTool) {
            previousNodesInPath.removeOfType(c); //attention: this removes the parent tool in case there is a nested tool.
            previousNodesInPath.add(c);
            //  }
            for (Object child : ch) {
                AsConcept c2 = (AsConcept) child;
                if (cls.isInstance(child)) {
                    C c3 = (C) child;
                    if (c3.getTermRef() != null) {
                        r.add(c3);
                    }
                }
                getAllChildren(c2, r, cls);
            }
        }
        return r;

    }

    /**
     * ***
     * Recursively gets all distinct nodes,ordered by type and name in this
     * jenaModel and return them in a linear Set<AsConcept>. Based on
     * parent-child relationships.
     *
     * @param c
     * @return
     */
    public Set<AsConcept> getIndividuals(boolean sorted) {//TODO see if changes to the entries in nodes need to be alerted so that individuals is to be recomputed
        Set<AsConcept> r = null;
        if (sorted) {
            r = new TreeSet<>(new TermKindComparator());
        } else {
            r = new TreeSet(DEFAULT_COMPARATOR);
        }
        for (Object o : getNodes()) {
            AsConcept c = (AsConcept) o;
            r.add(c);
            r.addAll(getAllChildren(c, r));
        }
        previousNodesInPath.removeAll();

        return r;
    }

    /**
     * ***
     * Recursively gets all distinct nodes,ordered by type and name in this
     * jenaModel and return them in a linear Set<AsConcept>. Based on
     * parent-child relationships.
     *
     * @param c
     * @return
     */
    public <C extends AsConcept> Set< C> getIndividuals(Class< C> cls, Comparator comp) {

        Set<C> r = null;
        if (comp != null) {
            r = new TreeSet<>(comp);
        } else {
            r = new TreeSet(DEFAULT_COMPARATOR);
        }

        for (Object o : getNodes()) {
            C c = (C) o;
            if (cls.isInstance(c)) {
                C c3 = (C) c;
                if (c3.getTermRef() != null) {
                    r.add(c3);
                }
            }
            r.addAll(getAllChildren(c, r, cls));
        }
        previousNodesInPath.removeAll();

        return r;
    }

    /**
     * ***
     * Recursively gets all distinct nodes,ordered by type and name in this
     * jenaModel and return them in a linear Set<AsConcept>. Based on
     * parent-child relationships.
     *
     * @param c
     * @return
     */
    public <C extends AsConcept> Set< C> getIndividuals(Class< C> cls, boolean sorted
    ) {
        if (sorted) {
            return getIndividuals(cls, new TermKindComparator());
        } else {
            return getIndividuals(cls, null);

        }

    }

    public <C extends AsConcept> C findIndividual(Class<C> cls, String uri) {
        Set<C> sc = OntologyNodes.this.getIndividuals(cls, false);
        for (C c : sc) {
            if (c.getUri().toString().equals(uri)) {
                return c;
            }
        }
        return null;
    }

    public AsConcept findIndividualConcept(String uri) {
        Set<AsConcept> sc = getIndividuals(false);
        for (AsConcept c : sc) {
            if (c.getUri().toString().equals(uri)) {
                return c;
            }
        }
        return null;
    }

    Set<String> names = new THashSet<>();

    public void testNames(List<AsConcept> concepts) {
        for (AsConcept concept : concepts) {
            if (names.add(concept.getTermRef().getEarsTermLabel().getPrefLabel())) {

            } else {
                int a = 5;
            }
        }
    }

    /**
     * ***
     * Serializes the jenaModel to an rdf file at the given path. Ignores
     * countries, organisations, harbours,
     *
     * @throws IOException
     */
    private void save(Path destPath) throws OWLOntologyCreationException, EarsException {
        CurrentUser currentUser = Utilities.actionsGlobalContext().lookup(CurrentUser.class);
        User user = null;
        if (currentUser != null && currentUser.getConcept() != null) {
            user = currentUser.getConcept();
        }

        RestClientOnt client = null;
        if (this.model.getScope().equals(ScopeMap.Scope.VESSEL.name())) {
            if (user == null) {
                throw new IllegalStateException("The vessel tree is write-protected. No credentials are registered in the Settings.");
            }
            try {
                client = new RestClientOnt();
            } catch (ConnectException ex) {
                throw new EarsException("The vessel tree cannot be edited when the EARS web server is unreachable.");
            } catch (EarsException ex) {
                throw new IllegalStateException("The vessel tree cannot be edited when the url for the EARS web server is empty or invalid.");
            }
            boolean authenticated;
            try {
                authenticated = client.authenticate(user);
            } catch (ConnectException ex) {
                throw new EarsException("The vessel tree cannot be edited when the EARS web server is unreachable.");
            }
            if (!authenticated) {
                throw new IllegalStateException("The vessel tree is write-protected. You did not provide the correct credentials in the Settings to edit it.");
            }
        } else if (this.model.getScope().equals(ScopeMap.Scope.PROGRAM.name())) {
            try {
                client = new RestClientOnt();
            } catch (ConnectException ex) {
                throw new EarsException("The program tree cannot be edited when the EARS web server is unreachable.");
            } catch (EarsException ex) {
                throw new IllegalStateException("The vessel tree cannot be edited when the url for the EARS web server is empty or invalid.");
            }
        }

        if (getRoot() == null) {
            throw new IllegalStateException("This set of OntologyNodes doesn't have a root. "
                    + "Unrooted OntologyNodes can't be saved. Adding a root to an OntologyModel's nodes is done by passing the OntologyModel to a Root FakeConcept constructor.");
        } else {
            this.nodes = (Set<T>) getRoot().getChildren(null);
        }
        EARSOntologyCreator owlCreator;

        Set<ToolCategory> toolCategories;
        Set<Tool> tools;
        Set<be.naturalsciences.bmdc.ears.ontology.entities.Process> processes;
        Set<Action> actions;
        Set<Property> properties;
        Set<ProcessAction> processActions = new THashSet();//new TreeSet(new ProcessActionComparator()); //sorting is irrelevant

        Set<SpecificEventDefinition> specificEventDefinitions = new THashSet(); //sorting is irrelevant
        Set<GenericEventDefinition> genericEventDefinitions = new THashSet(); //sorting is irrelevant

        try {
            owlCreator = new EARSOntologyCreator(this.getModel().getScopeMap(), this.getModel().getName());
            //     TermLabelIdentityHashCodeComparator comp = new TermLabelIdentityHashCodeComparator();
            toolCategories = this.getIndividuals(ToolCategory.class, null); //sorting is irrelevant and must include individuals even having the same uri.
            tools = this.getIndividuals(Tool.class, null); //sorting is irrelevant and must include individuals even having the same uri.
            processes = this.getIndividuals(be.naturalsciences.bmdc.ears.ontology.entities.Process.class, null); //sorting is irrelevant and must include individuals even having the same uri.
            actions = this.getIndividuals(Action.class, null); //sorting is irrelevant and must include individuals even having the same uri.
            properties = this.getIndividuals(Property.class, null); //sorting is irrelevant and must include individuals even having the same uri.
            for (Tool tool : tools) {
                specificEventDefinitions.addAll(tool.getSpecificEventDefinitionCollection().stream().filter(c -> c != null).collect(Collectors.toList()));
                try {
                    genericEventDefinitions.addAll(tool.getGenericEventDefinitionCollection().stream().filter(c -> c != null).collect(Collectors.toList()));
                } catch (Exception e) {
                    Messaging.report("A problem occured during saving the tree at " + destPath + " for the tool" + tool.getUri(), e, this.getClass(), false);
                }
                if (tool.isHostingTool()) {
                    for (SpecificEventDefinition sev : tool.getSpecificEventDefinitionCollection()) {
                        specificEventDefinitions.add(sev);
                        processes.add(sev.getProcess());
                        actions.add(sev.getAction());
                        processActions.add(sev.getProcessAction());
                        properties.addAll(sev.getPropertyCollection());
                    }
                    for (GenericEventDefinition gev : tool.getGenericEventDefinitionCollection()) {
                        genericEventDefinitions.add(gev);
                        processes.add(gev.getProcess());
                        actions.add(gev.getAction());
                        processActions.add(gev.getProcessAction());
                        properties.addAll(gev.getPropertyCollection());
                    }
                    for (Tool nestedTool : tool.getHostedCollection()) {
                        nestedTool.getToolCategoryCollection().retainAll(toolCategories); // remove any previous categories the nested tool belongs to, unless this category is included in the current ontology itself. Otherwise the category is referenced to but the category entity itself of the previous ontology the tool belonged to does not exist.
                        for (SpecificEventDefinition sev : nestedTool.getSpecificEventDefinitionCollection()) {
                            specificEventDefinitions.add(sev);
                            processes.add(sev.getProcess());
                            actions.add(sev.getAction());
                            processActions.add(sev.getProcessAction());
                            properties.addAll(sev.getPropertyCollection());
                        }
                        for (GenericEventDefinition gev : nestedTool.getGenericEventDefinitionCollection()) {
                            genericEventDefinitions.add(gev);
                            processes.add(gev.getProcess());
                            actions.add(gev.getAction());
                            processActions.add(gev.getProcessAction());
                            properties.addAll(gev.getPropertyCollection());
                        }
                    }
                }
            }
            for (SpecificEventDefinition sev : specificEventDefinitions) {
                try {
                    processActions.add(sev.getProcessAction());
                    processes.add(sev.getProcess());
                    actions.add(sev.getAction());
                } catch (NullPointerException e) {
                    Messaging.report("A problem occured during saving the tree at " + destPath + " for the specificevent about " + sev.getProcess().getUri() + " & " + sev.getAction().getUri() + " & " + sev.getToolRef().getUri(), e, this.getClass(), false);
                    processActions.add(sev.getProcessAction());
                }
            }
            for (GenericEventDefinition gev : genericEventDefinitions) {
                processActions.add(gev.getProcessAction());
            }

            /* testNames(new ArrayList<AsConcept>(toolCategories));
            testNames(new ArrayList<AsConcept>(tools));
            testNames(new ArrayList<AsConcept>(processes));
            testNames(new ArrayList<AsConcept>(actions));
            testNames(new ArrayList<AsConcept>(properties));
            testNames(new ArrayList<AsConcept>(specificEventDefinitions));
            testNames(new ArrayList<AsConcept>(genericEventDefinitions));*/
            owlCreator.setToolCategoryCollection(toolCategories);
            owlCreator.setToolCollection(tools);
            owlCreator.setProcessCollection(processes);
            owlCreator.setActionCollection(actions);
            owlCreator.setPropertyCollection(properties);
            owlCreator.setSevCollection(specificEventDefinitions);
            owlCreator.setGevCollection(genericEventDefinitions);
            owlCreator.setProcessActionCollection(processActions);

            int version = this.model.getVersion();

            owlCreator.createOntoFile(EARSOntologyCreator.LoadOnto.PASTE, new File(Constants.ACTUAL_LOCAL_ONTOLOGY_AXIOM_LOCATION), version + 1, destPath, null, null, null, true);

            if (this.model.getScope().equals(ScopeMap.Scope.VESSEL.name()) && user != null) {
                IResponseMessage response = client.uploadVesselOntology(destPath, user.getUsername(), user.getPassword());
                if (response.isBad()) {
                    throw new EarsException("Vessel tree wasn't saved to EARS web services. File was only saved locally and not on the server." + response.getSummary());
                }
            }
            if (this.model.getScope().equals(ScopeMap.Scope.PROGRAM.name())) {
                IResponseMessage response = client.uploadProgramOntology(destPath);
                if (response.isBad()) {
                    throw new EarsException("Program tree wasn't saved to EARS web services. File was only saved locally and not on the server." + response.getSummary());
                }
            }
            saved = true;
        } catch (OWLOntologyCreationException | NullPointerException ex) {
            saved = false;
            throw ex;
        } finally {
            owlCreator = null;
            toolCategories = null;
            tools = null;
            processes = null;
            actions = null;
            properties = null;
            genericEventDefinitions = null;
            specificEventDefinitions = null;
            processActions = null;
        }
    }

    /**
     * *
     * Saves the ontology to its path and returns true if the operation
     * succeeds, false if otherwise. After saving, the reference from the model
     * to this is destroyed.
     *
     * @param destPath
     * @return
     */
    @Override
    public boolean save() {

        Path path = this.getModel().getFile().toPath();
        try {
            save(path);
        } catch (OWLOntologyCreationIOException ex) {
            Messaging.report("The ontology server is unreachable", ex, this.getClass(), false);
            return false;
        } catch (OWLOntologyCreationException ex) {
            Messaging.report("The tree couldn't be serialized to rdf", ex, this.getClass(), false);
            return false;
        } catch (EarsException ex) {
            Messaging.report("The tree couldn't be saved", ex, this.getClass(), false);
            return false;
        }
        try {
            // this.getModel().reopen();
        } catch (Exception ex) {
            Messaging.report("The tree file could not be re-read after saving", ex, this.getClass(), false);
            return false;

        } finally {
            path = null;
        }
        return true;
    }

    /**
     * *
     * Saves the ontology to the given path and returns true if the operation
     * succeeds, false if otherwise.
     *
     * @param destPath
     * @return
     */
    @Override
    public boolean saveAs(Path destPath
    ) {
        try {
            save(destPath);
        } catch (OWLOntologyCreationIOException ex) {
            Messaging.report("The ontology server is unreachable.", ex, this.getClass(), true);
            return false;
        } catch (OWLOntologyCreationException ex) {
            Messaging.report("The ontology couldn't be serialized to rdf.", ex, this.getClass(), true);
            return false;
        } catch (EarsException ex) {
            Messaging.report("The ontology couldn't be saved.", ex, this.getClass(), false);
            return false;
        }
        return true;
    }

}
