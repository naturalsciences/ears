package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ears.comparator.TermLabelUriComparator;
import be.naturalsciences.bmdc.ears.ontology.AsConceptFlavor;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.IToolCategory;
import gnu.trove.map.hash.THashMap;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

/**
 *
 * @author Thomas Vandenberghe
 *
 * A class representing a ToolCategory. It corresponds to a Subject in EARS1 and
 * in the webservices.
 */
@Namespace("http://ontologies.ef-ears.eu/ears2/1#")
@RdfType("ToolCategory")
public class ToolCategory implements IToolCategory<EarsTerm, Tool, Vessel, GenericEventDefinition, Subject>, Transferable, Serializable {

    public static int lastId;

    //private static final long serialVersionUID = 1L;
    //protected Long id;
    @Id
    protected URI uri;

    @RdfProperty(value = "http://ontologies.ef-ears.eu/ears2/1#contains", inverseOf = "http://ontologies.ef-ears.eu/ears2/1#isMemberOf")
    private Collection<Tool> toolCollection;

    //private Collection<Vessel> vesselCollection;
    @RdfProperty(value = "http://ontologies.ef-ears.eu/ears2/1#involvedInEvent", inverseOf = "http://ontologies.ef-ears.eu/ears2/1#withTool")
    private Collection<GenericEventDefinition> genericEventDefinitionCollection;

    @RdfProperty("http://ontologies.ef-ears.eu/ears2/1#asConcept")
    private EarsTerm termRef;

    public ToolCategory() {

    }

    @Override
    public void init() {
        toolCollection = new ArrayList();
        //subjectCollection = new ArrayList();
        //vesselCollection = new ArrayList();
        genericEventDefinitionCollection = new ArrayList();
    }

    /* public ToolCategory(Long id) {
     this.id = id;
     }*/
    @Override
    public Long getId() {
        try {
            return Long.parseLong(uri.getRawFragment().split("_")[1]);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setId(Long id) {
        //this.id = id;
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
    public String getUrn() {
        return this.getTermRef().getPublisherUrn()==null?this.getTermRef().getOrigUrn():this.getTermRef().getPublisherUrn();
    }


    @Override
    public Collection<Tool> getToolCollection() {
        return toolCollection;
    }

    @Override
    public void setToolCollection(Collection<Tool> toolCollection) {
        this.toolCollection = toolCollection;
    }

    /*@Override
    public Collection<Vessel> getVesselCollection() {
        return vesselCollection;
    }

    @Override
    public void setVesselCollection(Collection<Vessel> vesselCollection) {
        this.vesselCollection = vesselCollection;
    }*/
    @Override
    public Collection<GenericEventDefinition> getGenericEventDefinitionCollection() {
        return genericEventDefinitionCollection;
    }

    @Override
    public void setGenericEventDefinitionCollection(Collection<GenericEventDefinition> genericEventDefinitionCollection) {
        this.genericEventDefinitionCollection = genericEventDefinitionCollection;
    }

    @Override
    public EarsTerm getTermRef() {
        return termRef;
    }

    @Override
    public void setTermRef(EarsTerm termRef) {
        this.termRef = termRef;
    }

    @Override
    public int getLastId() {
        return ++lastId;
    }

    public static void setLastId(int lastId) {
        ToolCategory.lastId = lastId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash ^= (getId() != null ? getId().hashCode() : 0);
        hash ^= (getUri() != null ? getUri().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof ToolCategory)) {
            return false;
        }
        ToolCategory other = (ToolCategory) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        if ((this.uri == null && other.uri != null) || (this.uri != null && !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    /*public boolean equalsNbChildren(ToolCategory otherToolCategory) {
        ConceptHierarchy thisParents = new ConceptHierarchy();
        ConceptHierarchy otherParents = new ConceptHierarchy();
        thisParents.add(this);
        otherParents.add(otherToolCategory);
        if (!this.equals(otherToolCategory)) {
            return false;
        }
        boolean allLevelsTrue = true;
        if (this.getChildren(thisParents).size() != otherToolCategory.getChildren(otherParents).size()) {
            return false;
        }
        if (this.getChildren(thisParents).size() > 0) {
            for (Tool tool : this.getChildren(thisParents)) {
                for (Tool otherTool : otherToolCategory.getChildren(otherParents)) {
                    if (tool.equals(otherTool)) {
                        return tool.equalsNbChildren(otherTool, thisParents, otherParents);
                    }
                }

            }
        }
        return true;
    }*/
    @Override
    public String toString() {
        return "id=" + getId() + "; hash=" + System.identityHashCode(this) + "; name=" + ((this.getTermRef() != null) ? this.getTermRef().getName() : "no name");
    }

    @Override
    public ToolCategory clone(IdentityHashMap<Object, Object> clonedObjects) throws CloneNotSupportedException {
        be.naturalsciences.bmdc.ears.utils.Cloner<ToolCategory> cc = new be.naturalsciences.bmdc.ears.utils.Cloner(this, clonedObjects);
        ToolCategory shallowClone = cc.cloneOriginal();

        Map<Collection, Collection> collectionIdentityHashMap = new THashMap<>();
//        collectionIdentityHashMap.put(this.subjectCollection, shallowClone.subjectCollection);
        collectionIdentityHashMap.put(this.toolCollection, shallowClone.toolCollection);
        collectionIdentityHashMap.put(this.genericEventDefinitionCollection, shallowClone.genericEventDefinitionCollection);
        cc.cloneCollection(collectionIdentityHashMap);

        return shallowClone;
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        sb.append("tool category: ");
        sb.append(this.getTermRef().getEarsTermLabel().getPrefLabel());
        sb.append(" (");
        sb.append(this.getTermRef().getUri());
        sb.append(")");
        sb.append(System.getProperty("line.separator"));
        sb.append("Generic EventDefinitions");
        sb.append(System.getProperty("line.separator"));
        for (GenericEventDefinition sev : getGenericEventDefinitionCollection()) {
            sb.append("\t");
            sb.append(sev.print());
            sb.append(System.getProperty("line.separator"));
        }
        sb.append("tools: ");
        sb.append(System.getProperty("line.separator"));
        for (Tool tool : toolCollection) {
            sb.append(tool.print());
            sb.append(System.getProperty("line.separator"));
            sb.append("-----------");
            sb.append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }

    /**
     * Adds the given tool to this collection of tools. Note that this method
     * does NOT add this category to the tool as well!
     *
     */
    @Override
    public void addTool(Tool tool) {
        if (this.toolCollection == null) {
            this.setToolCollection(new ArrayList<Tool>());
        }
        if (!this.toolCollection.contains(tool)) {
            this.toolCollection.add(tool);
        }
    }

    public void removeTool(Tool tool) {
        if (this.toolCollection.contains(tool)) {
            this.toolCollection.remove(tool);
        }
    }

    @Override
    public Set<Tool> getChildren(ConceptHierarchy parents) {
        Set<Tool> l = new TreeSet(new TermLabelUriComparator());

        // List two = new ArrayList(getToolCollection());
        // l.addAll(getToolCollection());
        //getToolCollection()
        //for (Tool tool : getToolCollection()) { //https://bugs.openjdk.java.net/browse/JDK-8150808 and https://stackoverflow.com/questions/26184532/concurrentmodificationexception-add-vs-addall
        //   l.add(tool);
        // }
        Iterator<Tool> iterator = getToolCollection().iterator();
        while (iterator.hasNext()) {
            Tool tool = iterator.next();
            l.add(tool);

        }
        // getToolCollection());
        //Collections.sort(l, new TermUriComparator());
        return l;
    }

    @Override
    public List<AsConcept> getParents() {
        return null;
    }

    /**
     * *
     * Adds a AsConcept childConcept to the children of this, if childConcept is
     * a Tool.
     *
     * @param targetParents The ConceptHierarchy parents of this (so not the
     * original parents of the child).
     * @param childConcept The AsConcept childConcept that needs to be added.
     * @param removePreviousBottomUpAssociations Whether to remove existing
     * relations the AsConcept childConcept has.
     */
    @Override
    public void addToChildren(ConceptHierarchy targetParents, AsConcept childConcept, boolean removePreviousBottomUpAssociations, ConceptHierarchy newChildParents, IAsConceptFactory factory) {
        if (childConcept != null && childConcept instanceof Tool) {
            Tool newChildTool = (Tool) childConcept;
            /*if(this.getToolCollection().contains(newChildTool)){
             this.getToolCollection().remove(newChildTool);
             }*/

            //Set<GenericEventDefinition> newGevs = new THashSet();
            //child.getToolCategoryCollection().removeAll(newChildTool.getToolCategoryCollection()); // remove, as a tool CAN belong to multiple categories
            //child.getToolCategoryCollection().add(this); //already done in newChildTool.addToCategory(this);

            /*for (GenericEventDefinition gev : newChildTool.getGenericEventDefinitionCollection()) {

                GenericEventDefinition gevClone = null;
                try {
                    gevClone = AsConceptFactory.build(GenericEventDefinition.class, gev);
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
                if (gevClone != null) {
                    gevClone.setToolCategoryRef(this);
                    gevClone.getProcess().getEventDefinition().add(gevClone);
                    gevClone.getAction().getEventDefinition().add(gevClone);
                    newGevs.add(gevClone);
                }
            }*/
            if (removePreviousBottomUpAssociations) { //erase all previous bottom-up associations of the future newChildTool
                newChildTool.removeBottomUpAssociations();
            }
            newChildTool.addToCategory(this); //bidirectional
            //this.getGenericEventDefinitionCollection().addAll(newGevs);
        }
    }

    /**
     * *
     * For the provided process and/or action, convert the
     * GenericEventDefinitions of this ToolCategory to SpecificEventDefinitions
     * of its tools and delete the GenericEventDefinition.
     *
     * @param process
     * @param action
     */
    /*  void reduceGevsToSevs(Process process, Action action) throws EarsException {
        if (process == null) {
            throw new IllegalArgumentException("Process must be provided.");
        }
        //GenericEventDefinition gev = null;
        Iterator<GenericEventDefinition> iter = this.getGenericEventDefinitionCollection().iterator();
        while (iter.hasNext()) {
            //for (int j = 0; j < this.getGenericEventDefinitionCollection().size(); j++) {

            GenericEventDefinition gev = iter.next(); //(GenericEventDefinition) new ArrayList(this.getGenericEventDefinitionCollection()).get(j);

            if (gev.getProcess().equals(process) && (action != null ? gev.getAction().equals(action) : true)) {
                Set<SpecificEventDefinition> sevs = gev.convertToSpecificEventDefinition();
                iter.remove();
                gev.delete(null);
                //j = (j - 1 < 0 ? 0 : j - 1);
            }
        }
    }*/
    /**
     * *
     * Convert the GenericEventDefinitions of this ToolCategory to
     * SpecificEventDefinitions of its tools and delete each
     * GenericEventDefinition.
     *
     * @throws be.naturalsciences.bmdc.ontology.EarsException
     */
    public void reduceGevsToSevs(IAsConceptFactory factory) throws EarsException {
     /*   List<SpecificEventDefinition> sevs =new ArrayList(); // new THashSet<>()
        Iterator<GenericEventDefinition> iter = this.getGenericEventDefinitionCollection().iterator();
        while (iter.hasNext()) {
            //for (int j = 0; j < this.getGenericEventDefinitionCollection().size(); j++) {
            if (this.getTermRef().getEarsTermLabelEn().getPrefLabel().equals("benthic lander")) {
                int a = 5;
            }
            EventDefinition ev = iter.next(); //(GenericEventDefinition) new ArrayList(this.getGenericEventDefinitionCollection()).get(j);
            if (ev instanceof GenericEventDefinition) {
                GenericEventDefinition gev = (GenericEventDefinition) ev;
                sevs.addAll(gev.convertToSpecificEventDefinition(factory));
                iter.remove();
                gev.delete(null);
            } else {
                int a = 5;
            }
            //j = (j - 1 < 0 ? 0 : j - 1);
        }
        if (sevs != null) {
            for (Tool tool : toolCollection) {
                for (SpecificEventDefinition sev : sevs) {
                    if (sev.getToolRef().equals(tool)) {
                        tool.addToSpecificEventDefinitions(sev);
                    }
                }
            }
        }
        int a = 5;*/
    }

    public void isolate() {
        if (this.genericEventDefinitionCollection != null) {
            this.genericEventDefinitionCollection.clear();
        }
        if (this.toolCollection != null) {
            this.toolCollection.clear();
        }
    }

    @Override
    public String getKind() {
        return this.getTermRef().getKind();
    }

    @Override
    public Class getParentType() {
        return FakeConcept.class;
    }

    @Override
    public Class getChildType() {
        return Tool.class;
    }

    public transient static final AsConceptFlavor OWN_DATA_FLAVOR = new AsConceptFlavor(ToolCategory.class, "toolcategory");

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{OWN_DATA_FLAVOR};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor == OWN_DATA_FLAVOR;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor == OWN_DATA_FLAVOR) {
            return this;
        } else if (flavor instanceof AsConceptFlavor) { //a fancy way to get the type of this
            return null;
        } else if (flavor == null) {
            return this;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    @Override
    public void delete(ConceptHierarchy parents) {

        /*for (AsConcept oldParentToolCategory : this.getParents()) {
         FakeConcept root = (FakeConcept) oldParentToolCategory;
         root.setChildren(null);
         }*/
        Iterator<Tool> iter = this.getToolCollection().iterator();
        while (iter.hasNext()) {
            // for (int i = 0; i < eventDefinition.size(); i++) {
            //EventDefinition e = (EventDefinition) new ArrayList(eventDefinition).get(i);
            Tool e = iter.next();

            //for (Tool tool : this.toolCollection) {
            //ConceptHierarchy newParents = new ConceptHierarchy(parents);
            //newParents.add(this);
            iter.remove();
            e.safeDelete(null, this, iter);
            //}

        }
        parents.getRoot().getChildren(null).remove(this);
    }

    @Override
    public boolean hasChildren() {
        return toolCollection != null && toolCollection.size() > 0;
    }

    @Override
    public String getDefinitionEn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAltLabelEn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPrefLabelEn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getIdentifierString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getVersionString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date getModifiedDate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date getCreationDate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean isIsDeprecated() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> broadMatch() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
