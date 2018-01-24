package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ears.properties.Constants;
import be.naturalsciences.bmdc.ears.utils.GlossedString;
import be.naturalsciences.bmdc.ontology.OntologyConstants;
import be.naturalsciences.bmdc.ontology.OntologyConstants.ConceptMD;
import be.naturalsciences.bmdc.ontology.entities.EarsTermLabel;
import be.naturalsciences.bmdc.ontology.entities.IEarsTerm;
import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import thewebsemantic.Id;
import thewebsemantic.LocalizedString;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

@Namespace("http://www.w3.org/2004/02/skos/core#")
@RdfType("Concept")
public class EarsTerm implements IEarsTerm<Action, EarsTerm, ItemStatus, EarsTermLabelDe, EarsTermLabelEn, EarsTermLabelEs, EarsTermLabelFr, EarsTermLabelIt, EarsTermLabelNl, SpecificEventDefinition, MetaTerm, Process, Parameter, Property, Subject, Tool, ToolCategory>, Serializable {

    public static int lastId;

    //private static final long serialVersionUID = 1L;
    private transient Map<URI, Long> uriMap = new HashMap();

    @Id
    protected URI uri;
    protected Long id;

    private String kind;
    private boolean isMeta;

    @RdfProperty("http://purl.org/dc/elements/1.1/identifier")
    private String identifierUrn;

    private String origUrn;
    private String publisherUrn;

    @RdfProperty("http://www.w3.org/2002/07/owl#versionInfo")
    private String versionInfo;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#submitter")
    private String submitter;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#controller")
    private String controller;

    @RdfProperty("http://purl.org/dc/elements/1.1/created")
    private Date creationDate;

    @RdfProperty("http://purl.org/dc/elements/1.1/creator")
    private String creator;

    @RdfProperty("http://purl.org/dc/elements/1.1/publisher")
    private String publisher;

    @RdfProperty("http://purl.org/dc/elements/1.1/modified")
    private Date modifDate;

    @RdfProperty("http://www.w3.org/2002/07/owl#sameAs")
    private String sameAs;

    @RdfProperty("http://www.w3.org/2004/02/skos/core#broader")
    private String broader;

    private String prefLabel;

    private String altLabel;

    private String definition;

    private Collection<LocalizedString> localizedPrefLabels;
    private Collection<LocalizedString> localizedAltLabels;
    private Collection<LocalizedString> localizedDefinitions;

    /*private String prefLabelDe;
     private String definitionDe;
     private String altLabelDe;
    
     private String definitionFr;
     private String altLabelFr;
     private String prefLabelFr;
    
     private String definitionEs;
     private String altLabelEs;
     private String prefLabelEs;
    
     private String definitionIt;
     private String altLabelIt;
     private String prefLabelIt;*/
    private EarsTermLabelEs earsTermLabelEs;
    private EarsTermLabelIt earsTermLabelIt;
    private EarsTermLabelEn earsTermLabelEn;
    private EarsTermLabelDe earsTermLabelDe;
    private EarsTermLabelNl earsTermLabelNl;
    private EarsTermLabelFr earsTermLabelFr;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#status")
    private String statusName;
    private ItemStatus status;

    /**
     * *
     * The term that precedes this term. Since precededBy and supersededBy are
     * each other's inverse, the term that is pointed to by precededBy must have
     * as substituteRef this.
     */
    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#precededBy")
    private EarsTerm substituteBackRef;

    /**
     * *
     * The term that substitutes (=supersedes) this term.
     */
    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#supersededBy")
    private EarsTerm substituteRef;

    private EarsTerm synonymBackRef;
    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#isSynonymOf")
    private EarsTerm synonymRef;
    private Property property;
    //private MetaTerm metaTerm;

    /*@RdfProperty("http://ontologies.ef-ears.eu/ears2/1#defines")
     private SpecificEventDefinition eventDefinition;
     @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#defines")
     private Action action;
     @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#defines")
     private Process process;
     @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#defines")
     private ToolCategory toolCategory;
     @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#defines")
     private Tool tool;
     @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#defines")
     private Subject subject;
     @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#defines")
     private Parameter parameter;

     @Override
     public AsConcept getConcept() {
     if (property != null) {
     return property;
     } else if (metaTerm != null) {
     return metaTerm;
     } else if (eventDefinition != null) {
     return eventDefinition;
     } else if (action != null) {
     return action;
     } else if (process != null) {
     return process;
     } else if (toolCategory != null) {
     return toolCategory;
     } else if (tool != null) {
     return tool;
     } else if (subject != null) {
     return subject;
     } else if (parameter != null) {
     return parameter;
     }
     return null;
     }*/
    public EarsTerm() {
    }

    /*public EarsTerm(Long id) {
     this.id = id;
     }*/

 /*public EarsTerm(Long id, String kind, boolean isMeta, String origUrn, String versionInfo, String submitter, Date creationDate) {
     this.id = id;
     this.kind = kind;
     this.isMeta = isMeta;
     this.origUrn = origUrn;
     this.versionInfo = versionInfo;
     this.submitter = submitter;
     this.creationDate = creationDate;
     }*/
    /**
     * *
     * Returns the default Ears term. Set to the English version.
     */
    @Override
    public EarsTermLabel getEarsTermLabel() {
        if (getEarsTermLabelEn() == null) {
            setEarsTermLabelEn(new EarsTermLabelEn());
            getEarsTermLabelEn().setPrefLabel(prefLabel);
            getEarsTermLabelEn().setAltLabel(altLabel);
            getEarsTermLabelEn().setDefinition(definition);
        }
        return getEarsTermLabelEn();
    }

    public void setPrefLabel(GlossedString prefLabel) {
        String oldLabel = getEarsTermLabel(prefLabel.getLanguage()).getPrefLabel();
        getEarsTermLabel(prefLabel.getLanguage()).setPrefLabel(prefLabel.getUnderlyingString());
    }

    public void setAltLabel(GlossedString altLabel) {
        String oldLabel = getEarsTermLabel(altLabel.getLanguage()).getAltLabel();
        getEarsTermLabel(altLabel.getLanguage()).setAltLabel(altLabel.getUnderlyingString());
    }

    public void setDefinition(GlossedString definition) {
        String oldLabel = getEarsTermLabel(definition.getLanguage()).getDefinition();
        getEarsTermLabel(definition.getLanguage()).setDefinition(definition.getUnderlyingString());
    }

    @Override
    public EarsTermLabel getEarsTermLabel(Language language) {

        switch (language) {
            case de:
                return this.getEarsTermLabelDe();
            case en:
                return this.getEarsTermLabelEn();
            case es:
                return this.getEarsTermLabelEs();
            case fr:
                return this.getEarsTermLabelFr();
            case it:
                return this.getEarsTermLabelIt();
            case nl:
                return this.getEarsTermLabelNl();
            default:
                return this.getEarsTermLabelEn();
        }
    }

    @Override
    public void setTermLabel(EarsTermLabel label) {
        earsTermLabelEn = (EarsTermLabelEn) label;
    }

    @Override
    public Long getId() {
        if (id != null) {
            return id;
        } else {
            try {
                return Long.parseLong(uri.getRawFragment().split("_")[1]);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public void setUri(URI uri) {
        if (uri.getRawFragment() == null) {
            throw new IllegalArgumentException("The URI should have a fragment part (#).");
        }
        this.uri = uri;
    }

    @Override
    public String getKind() {
        if (this.kind != null) {
            return this.kind;
        } else if (this.getIdentifierUrn() != null) {
            for (Iterator it = Constants.ALL_CLASSNAMES.entrySet().iterator(); it.hasNext();) {
                Map.Entry<Class, ConceptMD> entry = (Map.Entry<Class, ConceptMD>) it.next();
                if (entry != null && entry.getValue() != null) {
                    if (entry.getValue().urnMatches(this.getIdentifierUrn())) {
                        this.kind = entry.getValue().abbreviation;
                        break;
                    }/* else if (entry.getValue().sdnMatches(this.getIdentifierUrn())) {
                     this.kind = entry.getValue().abbreviation;
                     break;
                     }*/

                }
            }
        }
        return this.kind;
    }

    public boolean getIsMeta() {
        return isMeta;
    }

    public void setIsMeta(boolean isMeta
    ) {
        this.isMeta = isMeta;
    }

    public String getIdentifierUrn() {
        return this.identifierUrn;
    }

    public void setIdentifierUrn(String identifierUrn) {
        this.identifierUrn = identifierUrn;
        //return identifierUrn;
    }

    @Override
    public String getPublisherUrn() {
        if (this.publisherUrn == null && identifierUrn.contains("SDN")) {
            this.publisherUrn = identifierUrn;
            return identifierUrn;
        } else {
            return this.publisherUrn;
        }
    }

    @Override
    public void setPublisherUrn(String publisherUrn) {
        this.publisherUrn = publisherUrn;
        this.identifierUrn = publisherUrn;
    }

    @Override
    public String getOrigUrn() {
        if (this.origUrn == null) {
            this.origUrn = identifierUrn;
            return identifierUrn;
        } else {
            return this.origUrn;
        }
    }

    @Override
    public void setOrigUrn(String origUrn) {
        this.origUrn = origUrn;
        this.identifierUrn = origUrn;
    }

    @Override
    public String getCreator() {
        return creator;
    }

    @Override
    public void setCreator(String creator
    ) {
        this.creator = creator;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher
    ) {
        this.publisher = publisher;
    }

    @Override
    public String getVersionInfo() {
        return versionInfo;
    }

    @Override
    public void setVersionInfo(String versionInfo
    ) {
        this.versionInfo = versionInfo;
    }

    @Override
    public String getSubmitter() {
        return submitter;
    }

    @Override
    public void setSubmitter(String submitter
    ) {
        this.submitter = submitter;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller
    ) {
        this.controller = controller;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate
    ) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getModifDate() {
        return modifDate;
    }

    @Override
    public void setModifDate(Date modifDate
    ) {
        this.modifDate = modifDate;
    }

    public EarsTermLabelEs getEarsTermLabelEs() {
        return earsTermLabelEs;
    }

    public void setEarsTermLabelEs(EarsTermLabelEs earsTermLabelEs
    ) {
        this.earsTermLabelEs = earsTermLabelEs;
    }

    public EarsTermLabelIt getEarsTermLabelIt() {
        return earsTermLabelIt;
    }

    public void setEarsTermLabelIt(EarsTermLabelIt earsTermLabelIt
    ) {
        this.earsTermLabelIt = earsTermLabelIt;
    }

    /*@Override
     public Tool getTool() {
     return tool;
     }

     @Override
     public void setTool(Tool tool) {
     this.tool = tool;
     }*/
    public EarsTermLabelEn getEarsTermLabelEn() {
        if (this.earsTermLabelEn == null) {
            EarsTermLabelEn termLabel = new EarsTermLabelEn();
            termLabel.prefLabel = prefLabel;
            termLabel.altLabel = altLabel;
            termLabel.definition = definition;
            setEarsTermLabelEn(termLabel);
        }

        return this.earsTermLabelEn;
    }

    public void setEarsTermLabelEn(EarsTermLabelEn earsTermLabelEn
    ) {
        this.earsTermLabelEn = earsTermLabelEn;
    }

    public EarsTermLabelDe getEarsTermLabelDe() {
        return earsTermLabelDe;
    }

    public void setEarsTermLabelDe(EarsTermLabelDe earsTermLabelDe
    ) {
        this.earsTermLabelDe = earsTermLabelDe;
    }

    /*@Override
     public Action getAction() {
     return action;
     }

     @Override
     public void setAction(Action action) {
     this.action = action;
     }

     @Override
     public Parameter getParameter() {
     return parameter;
     }

     @Override
     public void setParameter(Parameter parameter) {
     this.parameter = parameter;
     }*/
    @Override
    public ItemStatus getStatus() {
        if (status != null) {
            return status;
        } else if (getStatusName() == null || !OntologyConstants.STATUSES.containsValue(getStatusName())) {
            return null;
        } else {
            return new ItemStatus((String) OntologyConstants.STATUSES.getKey(getStatusName()), getStatusName());
        }

    }

    @Override
    public void setStatus(ItemStatus status) {
        this.status = status;
        this.statusName = status.getName();
    }

    @Override
    /**
     * *
     * Provide the long version of the status, eg. WaitForApproval etc. See
     * Constants.Status.
     */
    public String getStatusName() {
        return statusName;
    }

    @Override
    public void setStatusName(String status) {
        if (OntologyConstants.STATUSES.containsKey(status)) { //argument is the short variant
            this.statusName = (String) OntologyConstants.STATUSES.get(status);//set the long variant
            this.status = new ItemStatus(status, this.statusName);
        } else if (OntologyConstants.STATUSES.containsValue(status)) {//argument is the long variant
            this.statusName = status;//set the long variant
            this.status = new ItemStatus((String) OntologyConstants.STATUSES.getKey(status), status);
        } else {
            throw new IllegalArgumentException(String.format("The given string '%s' can't be used as a status (neither short nor long).", status));
        }
    }

    @Override
    public EarsTerm getSubstituteBackRef() {
        return substituteBackRef;
    }

    @Override
    public void setSubstituteBackRef(EarsTerm substituteBackRef
    ) {
        this.substituteBackRef = substituteBackRef;
    }

    @Override
    public EarsTerm getSubstituteRef() {
        return substituteRef;
    }

    @Override
    public void setSubstituteRef(EarsTerm substituteRef
    ) {
        this.substituteRef = substituteRef;
    }

    public EarsTerm getSynonymBackRef() {
        return synonymBackRef;
    }

    public void setSynonymBackRef(EarsTerm synonymBackRef
    ) {
        this.synonymBackRef = synonymBackRef;
    }

    public EarsTerm getSynonymRef() {
        return synonymRef;
    }

    public void setSynonymRef(EarsTerm synonymRef
    ) {
        this.synonymRef = synonymRef;
    }

    /*@Override
     public Property getProperty() {
     return property;
     }

     @Override
     public void setProperty(Property property) {
     this.property = property;
     }*/
    public EarsTermLabelFr getEarsTermLabelFr() {
        return earsTermLabelFr;
    }

    public void setEarsTermLabelFr(EarsTermLabelFr earsTermLabelFr
    ) {
        this.earsTermLabelFr = earsTermLabelFr;
    }

    /*@Override
     public MetaTerm getMetaTerm() {
     return metaTerm;
     }

     @Override
     public void setMetaTerm(MetaTerm metaTerm) {
     this.metaTerm = metaTerm;
     }*/

 /*@Override
     public SpecificEventDefinition getEventDefinition() {
     return eventDefinition;
     }

     @Override
     public void setEventDefinition(SpecificEventDefinition eventDefinition) {
     this.eventDefinition = eventDefinition;
     }*/
    public EarsTermLabelNl getEarsTermLabelNl() {
        return earsTermLabelNl;
    }

    public void setEarsTermLabelNl(EarsTermLabelNl earsTermLabelNl
    ) {
        this.earsTermLabelNl = earsTermLabelNl;
    }

    /*@Override
     public Process getProcess() {
     return process;
     }

     @Override
     public void setProcess(Process process) {
     this.process = process;
     }

     @Override
     public ToolCategory getToolCategory() {
     return toolCategory;
     }

     @Override
     public void setToolCategory(ToolCategory toolCategory) {
     this.toolCategory = toolCategory;
     }*/
    @RdfProperty("http://www.w3.org/2004/02/skos/core#prefLabel")
    public String getPrefLabel() {
        if (getEarsTermLabelEn() == null) {
            setEarsTermLabelEn(new EarsTermLabelEn());
        }
        this.earsTermLabelEn.setPrefLabel(prefLabel);
        return prefLabel;
    }

    public void setPrefLabel(String prefLabel
    ) {
        this.prefLabel = prefLabel;
        if (getEarsTermLabelEn() == null) {
            setEarsTermLabelEn(new EarsTermLabelEn());
        }
        this.earsTermLabelEn.setPrefLabel(prefLabel);
    }

    @RdfProperty("http://www.w3.org/2004/02/skos/core#altLabel")
    public String getAltLabel() {
        if (getEarsTermLabelEn() == null) {
            setEarsTermLabelEn(new EarsTermLabelEn());
        }
        this.earsTermLabelEn.setAltLabel(altLabel);
        return altLabel;
    }

    public void setAltLabel(String altLabel
    ) {
        this.altLabel = altLabel;
        if (getEarsTermLabelEn() == null) {
            setEarsTermLabelEn(new EarsTermLabelEn());
        }
        this.earsTermLabelEn.setAltLabel(altLabel);
    }

    @RdfProperty("http://www.w3.org/2004/02/skos/core#definition")
    public String getDefinition() {
        if (getEarsTermLabelEn() == null) {
            setEarsTermLabelEn(new EarsTermLabelEn());
        }
        this.earsTermLabelEn.setDefinition(definition);
        return definition;
    }

    public void setDefinition(String definition
    ) {
        this.definition = definition;
        if (getEarsTermLabelEn() == null) {
            setEarsTermLabelEn(new EarsTermLabelEn());
        }
        this.earsTermLabelEn.setDefinition(definition);
    }

    public Collection<LocalizedString> getLocalizedPrefLabels() {
        return localizedPrefLabels;
    }

    public void setLocalizedPrefLabels(Collection<LocalizedString> localizedPrefLabels) {
        this.localizedPrefLabels = localizedPrefLabels;
    }

    public Collection<LocalizedString> getLocalizedAltLabels() {
        return localizedAltLabels;
    }

    public void setLocalizedAltLabels(Collection<LocalizedString> localizedAltLabels) {
        this.localizedAltLabels = localizedAltLabels;
    }

    public Collection<LocalizedString> getLocalizedDefinitions() {
        return localizedDefinitions;
    }

    public void setLocalizedDefinitions(Collection<LocalizedString> localizedDefinitions) {
        this.localizedDefinitions = localizedDefinitions;
    }

    @Override
    public int getLastId() {
        return ++lastId;
    }

    public static void setLastId(int lastId) {
        EarsTerm.lastId = lastId;
    }

    @Override
    public String getSameAs() {
        return sameAs;
    }

    @Override
    public void setSameAs(String sameAs) {
        this.sameAs = sameAs;
    }

    @Override
    public String getBroader() {
        return broader;
    }

    @Override
    public void setBroader(String broader) {
        this.broader = broader;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash ^= (getId() != null ? getId().hashCode() : 0);
        hash ^= (uri != null ? uri.hashCode() : 0);
        return hash;
    }

    /*@Override
     public Subject getSubject() {
     return subject;
     }

     @Override
     public void setSubject(Subject subject) {
     this.subject = subject;
     }*/
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof EarsTerm)) {
            return false;
        }
        EarsTerm other = (EarsTerm) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        if ((this.uri == null && other.uri != null) || (this.uri != null && !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EarsTerm[ id=" + getId() + " ]";
    }

    /**
     * *
     * Retrieve the name (ie. the prefLabel) of the connected EarsTermLabel in
     * English.
     *
     * @return
     */
    @Override
    public String getName() {
        if (earsTermLabelEn != null) {
            return earsTermLabelEn.getPrefLabel() + " - " + identifierUrn;
        } else {
            return prefLabel;
        }
    }

    @Override
    public boolean isBodcTerm() {
        /*if (this.controller != null && this.controller.equalsIgnoreCase("bodc")) {
         return true;
         }*/
        if (this.getOrigUrn() != null && this.getOrigUrn().contains("SDN")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isOwnTerm(String sdnVesselId) {
        return this.getUri().toASCIIString().contains(StringUtils.getLastUrnPart(sdnVesselId));
    }

    @Override
    public boolean isPublished() {
        return this.getPublisherUrn() != null && !this.getPublisherUrn().trim().isEmpty();
    }

    @Override
    public boolean isLabelEditable() {
        return !isBodcTerm();
    }

    public boolean isDeprecated() {
        return OntologyConstants.STATUSES.get(this.getStatus().getShortName()).equals(OntologyConstants.DEPRECATED) || this.getStatusName().equals(OntologyConstants.STATUSES.get(OntologyConstants.DEPRECATED)) || this.getStatusName().equals(OntologyConstants.DEPRECATED);
    }

    @Override
    public final void fixSubstitutes() {
        this.substituteBackRef = null;
        if (this.getSubstituteRef() != null && this.getSubstituteRef() == this) {
            this.setSubstituteRef(null);
        }
        if (this.getSubstituteRef() != null && this.getKind() != null && this.getSubstituteRef().getKind() != null) {

            if (this.getSubstituteRef().getKind().equals(this.getKind())) {
                EarsTerm other = this.getSubstituteRef();

                if (other.substituteRef != null && other.substituteRef.getKind() != null && other.substituteRef.getKind().equals(other.getKind())) {
                    other.fixSubstitutes();
                } else {
                    other.substituteRef = null;
                }
                this.altLabel = other.altLabel;
                this.controller = other.controller;
                this.creationDate = other.creationDate;
                this.creator = other.creator;
                this.definition = other.definition;
                this.earsTermLabelDe = other.earsTermLabelDe;
                this.earsTermLabelEn = other.earsTermLabelEn;
                this.earsTermLabelEs = other.earsTermLabelEs;
                this.earsTermLabelFr = other.earsTermLabelFr;
                this.earsTermLabelIt = other.earsTermLabelIt;
                this.earsTermLabelNl = other.earsTermLabelNl;
                this.id = other.id;
                this.isMeta = other.isMeta;

                this.localizedAltLabels = other.localizedAltLabels;
                this.localizedDefinitions = other.localizedDefinitions;
                this.localizedPrefLabels = other.localizedPrefLabels;
                this.modifDate = other.modifDate;
                this.setOrigUrn(other.origUrn);
                // this.origUrn = other.origUrn;
                this.prefLabel = other.prefLabel;
                this.property = other.property;
                this.publisher = other.publisher;
                this.setIdentifierUrn(other.identifierUrn);
                //this.identifierUrn = other.identifierUrn;
                this.status = other.status;
                this.statusName = other.statusName;
                this.submitter = other.submitter;
                //this.substituteBackRef = clone;
                this.synonymRef = other.synonymRef;
                this.uri = other.uri;
                this.uriMap = other.uriMap;
                this.versionInfo = other.versionInfo;
            } else {
                this.substituteRef = null;
            }
        }
    }
}
