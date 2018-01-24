/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ears.comparator.TermLabelComparator;
import be.naturalsciences.bmdc.ears.ontology.AsConceptFlavor;
import be.naturalsciences.bmdc.ontology.ConceptHierarchy;
import be.naturalsciences.bmdc.ontology.IAsConceptFactory;
import be.naturalsciences.bmdc.ontology.IOntologyNodes;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.IFakeConcept;
import be.naturalsciences.bmdc.ontology.entities.Term;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Thomas Vandenberghe
 */
public class FakeConcept implements IFakeConcept, Transferable, Serializable {

    Set<AsConcept> children;

    private String prefLabel;
    private String altLabel;
    private String definition;
    private boolean isRoot;

    public FakeConcept() {
        this.isRoot = true;
        children = new TreeSet(new TermLabelComparator());
    }

    public FakeConcept(boolean isRoot, IOntologyNodes nodes) {
        this.isRoot = isRoot;
        children = new TreeSet(new TermLabelComparator());
        this.addToChildren(nodes.getNodes());
    }

    /**
     * *
     *
     * @param prefLabel
     * @param altLabel
     * @param definition
     * @param isRoot
     * @param nodes an OntologyNodes object that is a wrapper for node children.
     */
    public FakeConcept(String prefLabel, String altLabel, String definition, boolean isRoot, IOntologyNodes nodes) {
        this.isRoot = isRoot;
        children = new TreeSet(new TermLabelComparator());
        this.prefLabel = prefLabel;
        this.altLabel = altLabel;
        this.definition = definition;
        if (nodes.getNodes() != null) {
            this.addToChildren(nodes.getNodes());
        }
        nodes.setRoot(this);
    }

    @Override
    public void init() {
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public Term getTermRef() {
        return null;
    }

    @Override
    public void setTermRef(Term termRef) {
    }

    @Override
    public Set<? extends AsConcept> getChildren(ConceptHierarchy parents) {
        return children;
    }

    public void setChildren(Set children) {
        this.children = children;
    }

    @Override
    public List<AsConcept> getParents() {
        return null;
    }

    public String getPrefLabel() {
        return prefLabel;
    }

    public void setPrefLabel(String prefLabel) {
        this.prefLabel = prefLabel;
    }

    public String getAltLabel() {
        return altLabel;
    }

    public void setAltLabel(String altLabel) {
        this.altLabel = altLabel;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public boolean isIsRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    /**
     * Adds a AsConcept childConcept to the children of this, if childConcept is
     * a ToolCategory.
     *
     * @param parents The ConceptHierarchy parents of this (so not the original
     * parents of the child).
     * @param childConcept The AsConcept childConcept that needs to be added.
     * @param removePreviousBottomUpAssociations Whether to remove existing
     * relations the AsConcept childConcept has.
     */
    @Override
    public void addToChildren(ConceptHierarchy parents, AsConcept childConcept, boolean removePreviousBottomUpAssociations, ConceptHierarchy newChildParents, IAsConceptFactory factory) {
        if (childConcept != null && childConcept instanceof ToolCategory) {

            if (!this.children.contains(childConcept)) {
                this.children.add(childConcept);
            }
            ToolCategory child = (ToolCategory) childConcept;
            for (Tool tool : child.getToolCollection()) {
                if (tool.getToolCategoryCollection() != null) {
                    if (Collections.disjoint(children, tool.getToolCategoryCollection())) { //if the other toolcats of the tools inside the added ToolCategory do not already occur in this children.
                        tool.getToolCategoryCollection().clear();
                        tool.getToolCategoryCollection().add(child);
                    } else {
                        tool.getToolCategoryCollection().retainAll(this.children);
                    }
                }
            }
            /*for (Iterator<Tool> iterator = child.getToolCollection().iterator(); iterator.hasNext();) {
                Tool next = iterator.next();
                next.removeBottomUpAssociations(); //verify all the ToolCategories of the tools inside the ToolCategory to be added. Remove membership of the tools and clear all menmbers from the toolcategory.
                next.addToCategory(child);//then re-add the ToolCategory to be added to the tools
            }*/
 /* for (Tool tool : child.getToolCollection()) {
                tool.removeBottomUpAssociations(); //verify all the ToolCategories of the tools inside the ToolCategory to be added. Remove membership of the tools and clear all menmbers from the toolcategory.
                tool.addToCategory(child);//then re-add the ToolCategory to be added to the tools
            }*/
        }
    }

    public void addToChildren(Set<ToolCategory> toolCategories) {
        for (ToolCategory tc : toolCategories) {
            addToChildren(null, tc, false, null, null);
        }
    }

    @Override
    public String getKind() {
        return "";
    }

    public transient static final AsConceptFlavor OWN_DATA_FLAVOR = new AsConceptFlavor(FakeConcept.class, "root");

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
            return null;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    @Override
    public Class getParentType() {
        return null;
    }

    @Override
    public Class getChildType() {
        return ToolCategory.class;
    }

    @Override
    public void delete(ConceptHierarchy parents) {
    }

    @Override
    public boolean hasChildren() {
        return !getChildren(null).isEmpty();
    }

    @Override
    public void setUri(URI uri) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getLastId() {
        return 0;
    }

    @Override
    public void setId(Long l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getUrn() {
        return this.getTermRef().getPublisherUrn();
    }

    @Override
    public AsConcept clone(IdentityHashMap ihm) throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void isolate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
