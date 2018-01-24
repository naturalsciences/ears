/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.*;
import com.hp.hpl.jena.reasoner.rulesys.*;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

/**
 * Read RDF XML from standard in; infer and write to standard out.
 */
class SimpleReasonerFactory implements ReasonerFactory {

    /**
     * Single global instance of this factory
     */
    private static ReasonerFactory theInstance = new SimpleReasonerFactory();

    /**
     * Static URI for this reasoner type
     */
    public static final String URI = "";

    /**
     * Cache of the capabilities description
     */
    protected Model capabilities;

    /**
     * Return the single global instance of this factory
     */
    public static ReasonerFactory theInstance() {
        return theInstance;
    }

    public static Reasoner getSimpleJenaReasoner() {
        // Create an empty model.
        //Model model = ModelFactory.createDefaultModel();
        // Read the RDF/XML on standard in.
        //model.read(System.in, null);
        // Create a simple RDFS++ Reasoner.
        StringBuilder sb = new StringBuilder();
        sb.append("[rdfs2:   (?x ?p ?y), (?p rdfs:domain ?c) -> (?x rdf:type ?c)] ");
        sb.append("[rdfs3:   (?x ?p ?y), (?p rdfs:range ?c) -> (?y rdf:type ?c)] ");

        sb.append("[rdfs6:   (?a ?p ?b), (?p rdfs:subPropertyOf ?q) -> (?a ?q ?b)] ");
        sb.append("[rdfs5:   (?x rdfs:subPropertyOf ?y), (?y rdfs:subPropertyOf ?z) -> (?x rdfs:subPropertyOf ?z)] ");

        sb.append("[rdfs9:   (?x rdfs:subClassOf ?y), (?a rdf:type ?x) -> (?a rdf:type ?y)] ");
        sb.append("[rdfs11:  (?x rdfs:subClassOf ?y), (?y rdfs:subClassOf ?z) -> (?x rdfs:subClassOf ?z)] ");

        sb.append("[owlinv:  (?x ?p ?y), (?p owl:inverseOf ?q) -> (?y ?q ?x)] ");
        sb.append("[owlinv2: (?p owl:inverseOf ?q) -> (?q owl:inverseOf ?p)] ");
        //sb.append("[owlinv3: (?p ears2:isMemberOf ?q) -> (?q ears2:contains ?p)] ");
        //sb.append("[owlinv4: (?p ears2:withTool ?q) -> (?q ears2:involvedInEvent ?p)] ");

        sb.append("[owltra:  (?x ?p ?y), (?y ?p ?z), (?p rdf:type owl:TransitiveProperty) -> (?x ?p ?z)] ");

        sb.append("[owlsam:  (?x ?p ?y), (?x owl:sameAs ?z) -> (?z ?p ?y)] ");
        sb.append("[owlsam2: (?x owl:sameAs ?y) -> (?y owl:sameAs ?x)] ");
        /*OntModelSpec spec = OntModelSpec.OWL_DL_MEM_RULE_INF;*/
        return new GenericRuleReasoner(Rule.parseRules(sb.toString())/*, spec.getReasonerFactory()*/);
    }

    public static InfModel createInfModel(Model model) {
        return ModelFactory.createInfModel(getSimpleJenaReasoner(), model);
    }

    @Override
    public Reasoner create(Resource rsrc) {
        return getSimpleJenaReasoner();
    }

    @Override
    public Model getCapabilities() {

        if (capabilities == null) {
            capabilities = ModelFactory.createDefaultModel();
            Resource base = capabilities.createResource(getURI());
            base.addProperty(ReasonerVocabulary.nameP, "OWL BRule Reasoner")
                    .addProperty(ReasonerVocabulary.descriptionP, "Experimental OWL reasoner.\n"
                            + "Can separate tbox and abox data if desired to reuse tbox caching or mix them.")
                    .addProperty(ReasonerVocabulary.supportsP, RDFS.subClassOf)
                    .addProperty(ReasonerVocabulary.supportsP, RDFS.subPropertyOf)
                    .addProperty(ReasonerVocabulary.supportsP, RDFS.member)
                    .addProperty(ReasonerVocabulary.supportsP, RDFS.range)
                    .addProperty(ReasonerVocabulary.supportsP, RDFS.domain)
                    // TODO - add OWL elements supported
                    .addProperty(ReasonerVocabulary.supportsP, ReasonerVocabulary.individualAsThingP)
                    .addProperty(ReasonerVocabulary.supportsP, OWL.ObjectProperty)
                    .addProperty(ReasonerVocabulary.supportsP, OWL.DatatypeProperty)
                    .addProperty(ReasonerVocabulary.supportsP, OWL.FunctionalProperty)
                    .addProperty(ReasonerVocabulary.supportsP, OWL.SymmetricProperty)
                    .addProperty(ReasonerVocabulary.supportsP, OWL.TransitiveProperty)
                    .addProperty(ReasonerVocabulary.supportsP, OWL.InverseFunctionalProperty)
                    .addProperty(ReasonerVocabulary.supportsP, OWL.hasValue)
                    .addProperty(ReasonerVocabulary.supportsP, OWL.intersectionOf)
                    .addProperty(ReasonerVocabulary.supportsP, OWL.unionOf) // Only partial
                    .addProperty(ReasonerVocabulary.supportsP, OWL.minCardinality) // Only partial
                    .addProperty(ReasonerVocabulary.supportsP, OWL.maxCardinality) // Only partial
                    .addProperty(ReasonerVocabulary.supportsP, OWL.cardinality) // Only partial
                    .addProperty(ReasonerVocabulary.supportsP, OWL.someValuesFrom) // Only partial
                    .addProperty(ReasonerVocabulary.supportsP, OWL.allValuesFrom) // Only partial
                    .addProperty(ReasonerVocabulary.supportsP, OWL.sameAs)
                    .addProperty(ReasonerVocabulary.supportsP, OWL.differentFrom)
                    .addProperty(ReasonerVocabulary.supportsP, OWL.disjointWith)
                    .addProperty(ReasonerVocabulary.versionP, "0.1");
        }
        return capabilities;
    }

    @Override
    public String getURI() {
        return URI;
    }
}
