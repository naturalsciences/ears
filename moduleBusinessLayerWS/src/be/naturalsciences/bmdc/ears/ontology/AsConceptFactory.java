/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.ontology.entities.EarsTerm;
import be.naturalsciences.bmdc.ears.ontology.entities.EarsTermLabelEn;
import be.naturalsciences.bmdc.ears.ontology.entities.GenericEventDefinition;
import be.naturalsciences.bmdc.ears.properties.Constants;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.IIndividuals;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.OntologyConstants;
import be.naturalsciences.bmdc.ontology.OntologyConstants.ConceptMD;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import gnu.trove.set.hash.THashSet;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.http.client.utils.URIBuilder;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

/**
 *
 * @author Thomas Vandenberghe
 */
public class AsConceptFactory implements IAsConceptFactory {

    //public static ConceptMD md;
    //IOntologyModel model;

    /* public AsConceptFactory(IOntologyModel model) {
     this.model = model;
     }*/
    private IOntologyModel model;

    public AsConceptFactory(IOntologyModel model) {
        this.model = model;
    }

    public IOntologyModel getModel() {
        return model;
    }

    public IIndividuals getIndividuals() {
        return model.getIndividuals();
    }

    /**
     * *
     * Build a copy the provided AsConcept from.
     *
     * @param <C>
     * @param cls
     * @param from
     * @return
     * @throws EarsException
     */
    /*  public static <C extends AsConcept> C build(Class<C> cls, C from) throws EarsException {
        Cloner cloner = new Cloner();
        C to = cloner.shallowClone(from);
        createUriAttachTerm(cls, to);
        return to;
    }*/
    /**
     * *
     * Given a parent AsConcept parent, build a child for it.
     *
     * @param <C>
     * @param parent
     * @return
     * @throws EarsException
     */
    public <C extends AsConcept> AsConcept buildChild(C parent) throws EarsException {
        AsConcept c = null;
        try {
            c = (AsConcept) parent.getChildType().newInstance();

        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
        //try {
        createUriAttachTerm(parent.getChildType(), c);
        /*} catch (EarsException ex) {
            Exceptions.printStackTrace(ex);
        }*/
        c.init();
        return c;
    }

    private static Set<String> names = new THashSet<>();

    public void testNames(List<AsConcept> concepts) {
        for (AsConcept concept : concepts) {
            if (concept.getTermRef().getEarsTermLabel().getPrefLabel().equals("New Action 29")) {
                int a = 5;
            }
            if (names.add(concept.getTermRef().getEarsTermLabel().getPrefLabel())) {
            } else {
                int a = 5;
            }
        }
    }

    // static Set<Long> allTermIds;
    /**
     * **
     * Attach a new term without a default name to the given Concept c of class
     * cls. In the case of a SpecificEventDefinition, generic text is added.
     *
     * @param <C>
     * @param cls
     * @param c
     * @throws FileNotFoundException
     */
    private <C extends AsConcept> void createUriAttachTerm(Class<C> cls, C c) throws EarsException {
        ConceptMD md = Constants.ALL_CLASSNAMES.get(cls);
        //long conceptId = getIndividuals().getGlobalHighestIdOfClass(cls) + 1;
        //long termId = getIndividuals().getGlobalHighestEarsTermId() + 1;
        /*  if (Individuals.allTermIds.contains(termId)) {
            int a = 5;
        }
        Individuals.allTermIds.add(termId);*/

        if (md != null) {

            CurrentVessel currentVessel = Utilities.actionsGlobalContext().lookup(CurrentVessel.class);
            if (currentVessel == null || currentVessel.getConcept() == null || currentVessel.getConcept().getCode() == null || currentVessel.getConcept().getCode().isEmpty()) {
                throw new EarsException("The current vessel is null or empty. Can't create an URI for a new term.");
            }
            String vesselCode = currentVessel.getConcept().getCode();
            String id = getUUID();
            String newName = "New " + cls.getSimpleName() + " " + id;
            c.setUri(getURI(cls, vesselCode.replaceAll("SDN:C17::", ""), id));
            EarsTerm term = null;
            if (cls.equals(GenericEventDefinition.class)) {
                //term = new CompoundEarsTerm();
            } else {
                URI termURI = getTermURI(vesselCode.replaceAll("SDN:C17::", ""), id);
                term = new EarsTerm();
                term.setCreationDate(new Date());
                term.setOrigUrn("ears:" + md.abbreviation.toLowerCase() + "::" + id);

                String statusName = (String) OntologyConstants.STATUSES.get(OntologyConstants.WAITFORAPPROVAL);
                term.setStatusName(statusName);

                term.setVersionInfo("1");

                term.setSubmitter(vesselCode);
                term.setUri(termURI);
                c.setTermRef(term);

                EarsTermLabelEn label = new EarsTermLabelEn();
                label.setPrefLabel(newName);
                term.setEarsTermLabelEn(label);
            }
        } else {
            throw new IllegalArgumentException("Method called for a class cls for which no EARS URN can be generated.");
        }

        List list = new ArrayList<>();
        list.add(c);
        testNames(list);

        //getIndividuals().add(c, conceptId, termId);
    }

    public <C extends AsConcept> C build(Class<C> cls) throws EarsException {

        C o = null;

        try {
            o = cls.newInstance();
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }

        createUriAttachTerm(cls, o);
        o.init();
        return o;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * *
     * Builds a new uri for any concept of Class C. Makes use of urls.
     *
     * @param urlModifier
     * @param termId
     * @return
     */
    private static <C extends AsConcept> URI getURI(Class<C> cls, String urlModifier, String id) {
        ConceptMD md = Constants.ALL_CLASSNAMES.get(cls);
        if (md != null) {
            URI uri = null;

            C o = null;

            try {
                o = cls.newInstance();
            } catch (InstantiationException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            }

            // int id = o.getLastId();//model.getLastId(cls);//OntologyServices.getLastConceptIdOfType(cls, model) + 1;//increase with 1
            String path = OntologyConstants.EARS2_PATH;
            try {

                uri = new URIBuilder().setScheme("http").setHost(OntologyConstants.EARS2_HOST).setPath(path).setFragment(urlModifier + "_" + md.abbreviation.toLowerCase() + "_" + id).build();
            } catch (URISyntaxException ex) {
                Exceptions.printStackTrace(ex);
            }
            return uri;
        }
        throw new IllegalArgumentException("Method called for a class cls for which no EARS URI can be generated.");
    }

    /**
     * *
     * Builds a new uri for any term. Makes use of urls.
     *
     * @param urlModifier
     * @param termId
     * @return
     */
    private URI getTermURI(String urlModifier, String termId) {
        URI uri = null;
        String path = OntologyConstants.EARS2_PATH;
        try {
            uri = new URIBuilder().setScheme("http").setHost(OntologyConstants.EARS2_HOST).setPath(path).setFragment(urlModifier + "_concept_" + termId).build();
        } catch (URISyntaxException ex) {
            Exceptions.printStackTrace(ex);
        }
        return uri;
    }

}
