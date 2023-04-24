/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

import be.naturalsciences.bmdc.ears.comparator.TermLabelComparator;
import be.naturalsciences.bmdc.ears.comparator.TermLabelUriComparator;
import be.naturalsciences.bmdc.ears.entities.CurrentOntologyModels;
import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.netbeans.services.GlobalActionContextProxy;
import be.naturalsciences.bmdc.ears.ontology.entities.Tool;
import be.naturalsciences.bmdc.ears.ontology.entities.ToolCategory;
import be.naturalsciences.bmdc.ears.ontology.entities.Vessel;
import be.naturalsciences.bmdc.ears.properties.Constants;
import be.naturalsciences.bmdc.ears.rest.RestClientOnt;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.OntologyConstants;
import be.naturalsciences.bmdc.ontology.OntologyConstants.ConceptMD;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.EARSThing;
import be.naturalsciences.bmdc.ontology.writer.FileUtils;
import be.naturalsciences.bmdc.ontology.writer.ScopeMap;
import be.naturalsciences.bmdc.ontology.writer.ScopeMap.Scope;
import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.ProfileRegistry;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.OWL;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import thewebsemantic.RDF2Bean;
import thewebsemantic.Sparql;

/**
 *
 * @author Thomas Vandenberghe
 *
 * The underlying ontology jenaModel.
 */
public abstract class OntologyModel implements IOntologyModel, Lookup.Provider {

    private Lookup lookup;
    private final File ontologyFile;
    //private String name;
    private OntModel jenaModel;

    private OntologyNodes<AsConcept> nodes;
    private Individuals individuals;

    private Date versionInfo;
    private int version;

    private boolean killModelAfterNodeCalculation;

    protected ScopeMap scopeMap;

    OntModelSpec spec2 = new OntModelSpec(ModelFactory.createMemModelMaker(), null, SimpleReasonerFactory.theInstance(), ProfileRegistry.OWL_LANG);
    private Set<ActionEnum> currentActions = new THashSet<>();
    private ArrayDeque<Class<? extends AsConcept>> classesOrder;

    public static final String BROWSE_ONTOLOGY = "be.naturalsciences.bmdc.ears.ontology.rdf.OpenRdfFileTypeActionBrowse";
    public static final String EDIT_ONTOLOGY = "be.naturalsciences.bmdc.ears.ontology.rdf.OpenRdfFileTypeActionEdit";

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    String sparqlQuery = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX skos:<http://www.w3.org/2004/02/skos/core#>\n"
            + "PREFIX ears2:<http://ontologies.ef-ears.eu/ears2/1#>\n"
            + "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n"
            + "\n"
            + "SELECT ?s\n"
            + "WHERE {\n"
            + "	{%s .\n"
            + "          ?s ears2:asConcept ?Concept .\n"
            + "		?Concept skos:prefLabel ?prefLabel .\n"
            + "		OPTIONAL{?Concept skos:definition ?Definition}\n"
            + "		OPTIONAL{?Concept ears2:status ?status FILTER (str(?status) !=\"WaitForApproval\" && str(?status) !=\"Rejected\")}\n"
            + "		OPTIONAL{?Concept owl:deprecated ?depr FILTER (xsd:boolean(?depr)=false)}\n"
            + "		MINUS {?Concept ears2:supersededBy ?supersededBy}\n"
            + "\n"
            + "		OPTIONAL{?Concept skos:prefLabel ?prefLabelLan . FILTER(lang(?prefLabelLan)=\"fr\")} .\n"
            + "		OPTIONAL{?Concept skos:definition ?DefinitionLan . FILTER(lang(?DefinitionLan)=\"fr\")} #.\n"
            + "		FILTER(lang(?prefLabel) = \"en\" && lang(?Definition) = \"en\") .\n"
            + "		%s"
            + "    }\n"
            + "}";

    public File getFile() {
        return ontologyFile;
    }

    /**
     * Get the versioninfo (date) of this ontology jenaModel.
     *
     * @return Date
     */
    public Date getVersionInfo() {
        return versionInfo;
    }

    public int getVersion() {
        return version;
    }

    /**
     * *
     * Change whether this model can be edited. Models can only be made
     * uneditable.
     *
     * @param editable
     */
    /*void setEditable(boolean editable) {
        if (!editable) {
            this.editable = editable;
        }
    }*/
    @Override
    public OntologyNodes<AsConcept> getNodes() {
        return nodes;
    }

    @Override
    public Individuals getIndividuals() {
        return individuals;
    }

    public ScopeMap getScopeMap() {
        return scopeMap;
    }

    /**
     * Get the scope of this jenaModel. The scope is the level to which this
     * ontology can be applied to, eg. whether it is applicable to a campaign,
     * the whole vessel or the whole ontology.
     *
     * @return
     */
    public String getScope() {
        if (scopeMap.getScope() != null) {
            return scopeMap.getScope().name();
        }
        return null;
    }

    /**
     * Get the actual scope of this jenaModel. The actual scope is the actual
     * value of the scope, ie the vessel code if the scope is VESSEL or the
     * program code if the scope is PROGRAM scope is CRUISE
     *
     * @return
     */
    public String getScopedTo() {
        return scopeMap.getScopedTo();
    }

    /**
     * Get the Jena Model behind this OntologyModel.
     *
     * @return
     */
    public OntModel getJenaModel() {
        return jenaModel;
    }

    /**
     * Create a OntologyModel with a file and without a type. Only a Jena
     * jenaModel is created from the file. Nodes from this jenaModel are not
     * calculated.
     *
     * @param localFile
     * @param killModelAfterNodeCalculation
     * @throws FileNotFoundException
     * @throws OWLOntologyCreationException
     */
    public OntologyModel(File localFile, boolean killModelAfterNodeCalculation, boolean reasoning) throws FileNotFoundException, IOException {
        this.ontologyFile = localFile;
        this.killModelAfterNodeCalculation = killModelAfterNodeCalculation;
        Map<String, String> staticStuff = IOntologyModel.getStaticStuff(localFile);
        if (staticStuff == null) {
            throw new IOException("Scope or versionInfo can't be found in the RDF file. The file could not be opened.");
        }
        this.scopeMap = new ScopeMap(Scope.valueOf(staticStuff.get(IOntologyModel.SCOPE)), staticStuff.get(IOntologyModel.SCOPEDTO));
        //verifyEditable(scopeMap);
        createModel(reasoning);
    }

    /**
     * **
     * Empty constructor to create a start object, useful to download the actual
     * models to later.
     *
     * @param localFile
     */
    public OntologyModel(File localFile) {
        this.ontologyFile = localFile;
    }

    public static Date getMostSpecificDate(Map<String, String> staticStuff) {
        String dateModified = staticStuff.get(IOntologyModel.DATEMODIFIED);
        String dateVersionInfo = staticStuff.get(IOntologyModel.VERSIONINFO);
        Map<SimpleDateFormat, String> dateMap = new HashMap<>();
        dateMap.put(StringUtils.SDF_SIMPLE_DATE, dateVersionInfo);
        dateMap.put(StringUtils.SDF_FULL_ISO_DATETIME, dateModified);
        return StringUtils.returnMostSpecificDate(dateMap);
    }

    /**
     * Create a OntologyModel with a filename for a file stored in the default
     * trees folder and type. A Jena jenaModel is created from the file and from
     * this jenaModel, nodes are calculated.
     *
     * @param localFile
     * @param fast
     * @throws FileNotFoundException
     * @throws OWLOntologyCreationException
     */
    public OntologyModel(String localFile, boolean killModelAfterNodeCalculation, boolean reasoning) throws FileNotFoundException, IOException {
        this(new File(Constants.TREES_FOLDER_PATH + Constants.SEPARATOR + localFile), killModelAfterNodeCalculation, reasoning);
    }

    private OntologyModel(OntModel model, boolean killModelAfterNodeCalculation) {
        this.ontologyFile = null;
        this.killModelAfterNodeCalculation = killModelAfterNodeCalculation;
        this.jenaModel = model;
        //this.editable = false;
    }

    private void createModel(boolean reasoning) throws FileNotFoundException, IOException {
        Path ontologyPath = ontologyFile.toPath();
        if (reasoning) {
            this.jenaModel = ModelFactory.createOntologyModel(spec2);
        } else {
            this.jenaModel = ModelFactory.createOntologyModel();
        }
        Resource ontology = this.jenaModel.getResource(OntologyConstants.EARS2_NS);

        InputStream in = FileManager.get().open(ontologyPath.toString());
        if (in == null) {
            throw new FileNotFoundException("The provided file (" + ontologyPath.getFileName().toString() + ") was not found in " + ontologyPath.getParent().toString() + ".");
        }

        try {
            this.jenaModel.read(in, OntologyConstants.EARS2_NS);
        } catch (Exception e) {
            throw new IOException("There was an error with creating the jena model from the rdf file.", e);
        }

        com.hp.hpl.jena.rdf.model.Property modifiedProp = this.jenaModel.getProperty("http://purl.org/dc/elements/1.1/modified"); //Ignore trailing slash!!!!
        com.hp.hpl.jena.rdf.model.Property scopeProp = this.jenaModel.getProperty(OntologyConstants.EARS2_NS + "scope"); //Ignore trailing slash!!!!
        com.hp.hpl.jena.rdf.model.Property scopedToProp = this.jenaModel.getProperty(OntologyConstants.EARS2_NS + "scopedTo"); //Ignore trailing slash!!!!
        //TODO try to read an ontology from a different vessel=>exception
        Statement modifiedProperty = this.jenaModel.getProperty(ontology, modifiedProp);
        Statement versionInfoProperty = this.jenaModel.getProperty(ontology, OWL.versionInfo);
        if (modifiedProperty != null) {
            Date resultDate = ((XSDDateTime) modifiedProperty.getLiteral().getValue()).asCalendar().getTime();
            versionInfo = resultDate;
        }

        if (versionInfoProperty != null) {
            String versionS = versionInfoProperty.getString();
            version = Integer.parseInt(versionS);
        }

        if (this.jenaModel.getProperty(ontology, scopeProp) != null) {
            String scope = this.jenaModel.getProperty(ontology, scopeProp).getString();
            String scopedTo = null;
            if (this.jenaModel.getProperty(ontology, scopedToProp) != null) {
                scopedTo = this.jenaModel.getProperty(ontology, scopedToProp).getString();
            }
            this.scopeMap = new ScopeMap(ScopeMap.Scope.valueOf(scope), scopedTo);
            //this.scopeMap.put(ScopeMap.Scope.valueOf(scope), scopedTo);
        } else {
            throw new IOException("The rdf tree file has no property that denotes its scope.");
        }
        buildIndividuals();
        GlobalActionContextProxy.getInstance().add(this.getIndividuals()); //are added to the list and last forever, opening and reopening the ontology doesn't change this.
    }

    @Override
    public Set<ActionEnum> getCurrentActions() {
        return this.currentActions;
    }

    /**
     * *
     * Calculates the nodes of this ontologymodel based on the provided classes
     * provided in the right order in classOrder and sets the operation.
     *
     * @param classesOrder
     * @param operation
     * @throws java.io.FileNotFoundException
     */
    public void open(ArrayDeque<Class<? extends AsConcept>> classesOrder, ActionEnum operation) throws FileNotFoundException, IOException {
        this.currentActions.add(operation);
        this.classesOrder = classesOrder;
        if (this.jenaModel == null) {
            createModel(true);
        }
        this.nodes = new OntologyNodes<AsConcept>(this, this.classesOrder, this.killModelAfterNodeCalculation);

        register();
        //GlobalActionContextProxy.getInstance().add(this.getIndividuals());
    }

    /**
     * Register the model in some central system. IN this case the NB Lookup system.
     */
    public void register() {
        //this block makes the ontologies available for the concept list, overview of all entities
        CurrentOntologyModels currentModels = Utilities.actionsGlobalContext().lookup(CurrentOntologyModels.class);
        if (currentModels == null) {
            LinkedHashSet models = new LinkedHashSet();
            models.add(this);
            GlobalActionContextProxy.getInstance().add(CurrentOntologyModels.getInstance(models));
        } else {
            currentModels.getConcept().add(this);
            GlobalActionContextProxy.getInstance().add(currentModels);
        }
    }

    /***
     * Close this model. Remove it from the ontology model registry.
     * @param operation 
     */
    public void close(ActionEnum operation) {
        currentActions.remove(operation);
        CurrentOntologyModels currentFiles = Utilities.actionsGlobalContext().lookup(CurrentOntologyModels.class);
        if (currentFiles != null) {
            currentFiles.getConcept().remove(this);
            GlobalActionContextProxy.getInstance().add(currentFiles);
        }
        this.nodes = null;

    }

    private void buildIndividuals() {
        if (!this.getScopeMap().sameScope(ScopeMap.STATIC_SCOPE)) {
            Set<Class<? extends EARSThing>> classes = Constants.getEARSAsConceptClasses();
            classes.add(Vessel.class);
            this.individuals = this.getAllIndividuals(classes, false);
        }
    }

    /**
     * ***
     * Get all the individuals in this jenaModel, even if they are not related
     * via parent-child relationships. Based on a SPARQL vesselQuery that
     * happens on the underlying OntModel, so unsaved changes are not taken into
     * account.
     *
     * @param classes
     * @param sorted
     * @param forceCheck
     * @return
     */
    public Individuals getAllIndividuals(Set<Class<? extends EARSThing>> classes, boolean sorted/*, boolean forceCheck*/) {
        RDF2Bean reader = new RDF2Bean(getJenaModel());
        Package pack = Package.getPackage("be.naturalsciences.bmdc.ears.ontology.entities");
        reader.bind(pack);
        Map<Class, Set<AsConcept>> results = new THashMap<>();
        for (Iterator it = Constants.ALL_CLASSNAMES.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Class, ConceptMD> entry = (Map.Entry<Class, ConceptMD>) it.next();
            Class cls = entry.getKey();
            if (classes.contains(cls)/* && cls.isInstance(AsConcept.class)*/) {
                if (entry != null && entry.getValue() != null && !entry.getValue().sparqlWhere.isEmpty()) {
                    String query = String.format(sparqlQuery, entry.getValue().sparqlWhere, "");
                    //results.put(cls, findIndividuals(query, cls, sorted)); //fails on terms from the ears2/vesselCode namespace!
                    results.put(cls, OntologyModel.this.getAllIndividuals(reader, cls, sorted));
                }
            }
        }
        return new Individuals(results, this);
    }

    private <C extends AsConcept> Set<C> getAllIndividuals(RDF2Bean reader, Class<C> cls, boolean sorted) {
        Set<C> classResults;
        if (sorted) {
            classResults = new TreeSet<>(new TermLabelUriComparator());
        } else {
            classResults = new THashSet<>();
        }

        Collection<C> intRes = reader.load(cls);
        classResults.addAll(intRes);

        return classResults;
    }

    private <C extends AsConcept> Set<C> getAllIndividuals(String query, Class<C> cls, boolean sorted) {
        Set<C> classResults;
        if (sorted) {
            classResults = new TreeSet<>(new TermLabelComparator());
        } else {
            classResults = new THashSet<>();
        }
        Collection<C> intRes = Sparql.exec(jenaModel, cls, query);
        if (cls.equals(Tool.class)) {
            int a = 5;
        }
        classResults.addAll(intRes);

        return classResults;
    }

    /**
     * ***
     * Get all the individuals of class C in this ontology jenaModel, even if
     * they are not related via parent-child relationships. Based on a SPARQL
     * vesselQuery that happens on the underlying OntModel, so unsaved changes
     * are not taken into account.
     *
     * @param <C>
     * @param cls
     * @param sorted
     * @return
     */
    public <C extends AsConcept> Set<C> getAllIndividuals(Class<C> cls, boolean sorted) {
        RDF2Bean reader = new RDF2Bean(getJenaModel());
        Package pack = Package.getPackage("be.naturalsciences.bmdc.ears.ontology.entities");
        reader.bind(pack);
        Set<C> concepts = OntologyModel.this.getAllIndividuals(reader, cls, sorted);
        if (concepts != null) {
            return concepts;
        } else {
            return new THashSet();
        }

    }

    @Override
    public ResultSet query(String q) throws Exception {
        return query(q, false);
    }

    public ResultSet query(String q, boolean inferred) throws Exception {
        if (this.getJenaModel() == null) {
            throw new Exception("The underlying JENA model is null. Cannot launch a query.");
        }
        /*if (inferred) {
         return queryInferred(q);
         } else {*/
        Query query = QueryFactory.create(q);
        QueryExecution qe = QueryExecutionFactory.create(query, this.getJenaModel());
        return qe.execSelect();
        //}
    }

    public boolean queryHasResult(String q, boolean inferred) throws Exception {
        ResultSet rs = query(q, inferred);
        return rs.hasNext();
    }

    public int queryNbResult(String q, boolean inferred) throws Exception {
        ResultSet rs = query(q, inferred);
        int i = 0;
        while (rs.hasNext()) {
            i++;
            rs.next();
        }
        return i;
    }

    /*private void verifyEditable(ScopeMap scopeMap) throws FileNotFoundException {
        if (scopeMap == null) {
            //    this.editable = false;
        } else if (scopeMap.sameScope(ScopeMap.BASE_SCOPE) || scopeMap.sameScope(ScopeMap.STATIC_SCOPE)) {
            //    this.editable = false;
        } else if (!ontologyScopedToAndCurrentVesselMatch(scopeMap)) {
            //    this.editable = false;
        } else {
            //   this.editable = true;
        }
    }*/
    /**
     * *
     * Verify whether the provided ontologyFile can be used. If a newer version
     * is present, it is downloaded.
     */
    /* private void verifyVersion(ScopeMap scopeMap, Date date) {
        try {
            OntologySynchronizer.synchronizeOntology(this);
        } catch (ConnectException ex) {
            Messaging.report("The local " + scopeMap.getScopeString() + " tree could not be synced as the remote or local ontology service couldn't be reached.", ex, this.getClass(), false);
        } catch (EarsException ex) {
            Messaging.report("The local " + scopeMap.getScopeString() + " tree could not be synced as the EARS web services url is not properly set.", ex, this.getClass(), false);
        }
    }*/
    protected static File downloadLatestVersion(Path folder, String name) {
        return downloadLatestVersion(folder.toFile(), name);
    }

    protected static File downloadLatestVersion(File folder, String name) {
        try {
            RestClientOnt client = new RestClientOnt();

            String result = client.getOntology(name);

            if (result != null) {
                String fileName = new String(name);
                if (!name.endsWith(".rdf")) {
                    fileName = name.concat(".rdf");
                }
                File file = new File(folder, fileName);
                FileUtils.writeStringToFile(file, result, StandardCharsets.UTF_8);
                return file;
            } else {
                return null;
            }
        } catch (ConnectException ex) {
            Messaging.report("Could not connect to the EARS web services to download the latest version of the tree (" + name + ").", ex, ProgramOntology.class, true);
        } catch (EarsException ex) {
            Messaging.report("The EARS web services url is wrongly configured. Could not download the latest version of the tree (" + name + ").", ex, ProgramOntology.class, true);
        } catch (IOException ex) {
            Messaging.report("There was a problem with the downloaded tree (" + name + "). It wasn't found on the server or couldn't be written to disk.", ex, ProgramOntology.class, true);
        }
        return null;
    }

    public boolean ontologyScopedToAndCurrentVesselMatch(ScopeMap scopeMap) throws FileNotFoundException {
        if (scopeMap.sameScope(ScopeMap.Scope.VESSEL)) {
            return Utilities.actionsGlobalContext().lookup(CurrentVessel.class).getConcept().getCode().equals(scopeMap.getScopedTo());
        }
        return true;
    }

    public void print() {
        if (this.jenaModel != null) {
            jenaModel.write(System.out, "RDF/XML");
        } else {
            System.out.println("null model");
        }
    }

    public String printTree() {
        Set<ToolCategory> tcs = this.getAllIndividuals(ToolCategory.class, true);
        for (ToolCategory tc : tcs) {

        }
        return "";
    }

    public void printTriples() {
        System.out.println("Printing triples:");
        StmtIterator iter = jenaModel.listStatements();
// print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();  // get next statement
            Resource subject = stmt.getSubject();     // get the subject
            com.hp.hpl.jena.rdf.model.Property predicate = stmt.getPredicate();   // get the predicate
            RDFNode object = stmt.getObject();      // get the object

            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            if (object instanceof Resource) {
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.print(" \"" + object.toString() + "\"");
            }
            System.out.println(" .");
        }
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof OntologyModel)) {
            return false;
        }
        OntologyModel other = (OntologyModel) object;
        return this.getFile().equals(other.getFile());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.getFile());
        return hash;
    }

}
