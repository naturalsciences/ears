package be.naturalsciences.bmdc.ears.ontology.gui;

import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.ontology.entities.FakeConcept;
import be.naturalsciences.bmdc.ears.ontology.entities.Tool;
import be.naturalsciences.bmdc.ears.ontology.entities.ToolCategory;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import be.naturalsciences.bmdc.ontology.entities.EarsTermLabel;
import be.naturalsciences.bmdc.ontology.entities.IAction;
import be.naturalsciences.bmdc.ontology.entities.IEarsTerm;
import be.naturalsciences.bmdc.ontology.entities.IProcess;
import be.naturalsciences.bmdc.ontology.entities.IProperty;
import be.naturalsciences.bmdc.ontology.entities.ITool;
import be.naturalsciences.bmdc.ontology.entities.IToolCategory;
import be.naturalsciences.bmdc.ontology.entities.Term;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.net.URI;
import java.util.Date;
import java.util.Objects;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

/**
 * A class denoting a node in the concept list class groupings
 *
 * @author Thomas Vandenberghe
 */
public class SimpleAsConceptNode extends AbstractNode {

    private final AsConcept concept;
    private boolean isRoot = false;

    public SimpleAsConceptNode(AsConcept concept) {
        super(Children.LEAF, Lookups.singleton(concept));
        setName(concept.toString());
        this.concept = concept;
        this.isRoot = false;
    }

    @Override
    public String getHtmlDisplayName() {
        CurrentVessel currentVessel = Utilities.actionsGlobalContext().lookup(CurrentVessel.class);
        String currentVesselCode = null;
        if (currentVessel != null && currentVessel.getConcept() != null) {
            currentVesselCode = currentVessel.getConcept().getCode();
        }

        String r = null;
        if (this.concept instanceof IToolCategory) {
            r = "<font color='#0B486B'>" + getDisplayName() + "</font>";
        } else if (this.concept instanceof ITool) {
            r = "<font color='#02779E'>" + getDisplayName() + "</font>";
        } else if (this.concept instanceof IProcess) {
            r = "<font color='#DC4B40'>" + getDisplayName() + "</font>";
        } else if (this.concept instanceof IAction) {
            r = "<font color='#F59E03'>" + getDisplayName() + "</font>";
        } else if (this.concept instanceof IProperty) {
            r = "<font color='#EB540A'>" + getDisplayName() + "</font>";
        } else {
            r = "<font color='#2C3539'>" + getDisplayName() + "</font>";
        }

        if (!concept.getTermRef().isBodcTerm()) {
            r = "<i>" + r + "</i>";
        }
        return r;
    }

    @Override
    public String getDisplayName() {
        if (isRoot()) {
            return "root";
        } else if (concept != null && concept.getTermRef() != null) {
            return concept.getTermRef().getEarsTermLabel().getPrefLabel();
        } else {
            return "root";
        }
    }

    @Override
    public void setDisplayName(String displayName) {
        concept.getTermRef().getEarsTermLabel().setPrefLabel(displayName);
    }

    @Override
    public String getName() {
        return concept.getTermRef().getOrigUrn();
    }

    @Override
    public final String getShortDescription() {
        if (concept != null && concept.getTermRef() != null) {
            if (concept.getTermRef().getEarsTermLabel().getDefinition() != null) {
                return concept.getKind() + ": " + concept.getTermRef().getEarsTermLabel().getDefinition().replace("><", "> <");
            } else {
                return concept.getKind();
            }
        }
        return "";
    }

    @Override
    public Image getIcon(int type) {
        if (this.concept instanceof IToolCategory) {
            return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/treeviewer/toolcategory.png"); //flaticon
        } else if (this.concept instanceof ITool) {
            return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/treeviewer/tool.png"); //flaticon
        } else if (this.concept instanceof IProcess) {
            return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/treeviewer/process.png"); //flaticon
        } else if (this.concept instanceof IAction) {
            return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/treeviewer/action.png"); //flaticon
        } else if (this.concept instanceof IProperty) {
            return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/treeviewer/property.png"); //flaticon
        } else if (this.isRoot()) {
            return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/treeviewer/root.png"); //flaticon
        } else {
            return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/treeviewer/unknown.png"); //flaticon
        }
    }

    private boolean isRoot() {
        return isRoot;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SimpleAsConceptNode)) {
            return false;
        }
        SimpleAsConceptNode other = (SimpleAsConceptNode) object;
        return other.concept.equals(this.concept);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.concept);
        return hash;
    }

    @Override
    public Transferable drag() {
        if (this.concept instanceof ToolCategory) {
            ToolCategory c = (ToolCategory) this.concept;
            return c;
        } else if (this.concept instanceof Tool) {
            Tool c = (Tool) this.concept;
            return c;
        } else if (this.concept instanceof be.naturalsciences.bmdc.ears.ontology.entities.Process) {
            be.naturalsciences.bmdc.ears.ontology.entities.Process c = (be.naturalsciences.bmdc.ears.ontology.entities.Process) this.concept;
            return c;
        } else if (this.concept instanceof be.naturalsciences.bmdc.ears.ontology.entities.Action) {
            be.naturalsciences.bmdc.ears.ontology.entities.Action c = (be.naturalsciences.bmdc.ears.ontology.entities.Action) this.concept;
            return c;
        } else if (this.concept instanceof be.naturalsciences.bmdc.ears.ontology.entities.Property) {
            be.naturalsciences.bmdc.ears.ontology.entities.Property c = (be.naturalsciences.bmdc.ears.ontology.entities.Property) this.concept;
            return c;
        } else {
            return null;
        }
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        if (!(this.concept instanceof FakeConcept)) {

            Sheet.Set set = Sheet.createPropertiesSet();

            //AsConcept infoConcept = Utilities.actionsGlobalContext().lookup(AsConcept.class);
            //Term term = infoConcept.getTermRef();
            Term term = this.concept.getTermRef();
            URI uri = term.getUri();
            EarsTermLabel label = term.getEarsTermLabel(IEarsTerm.Language.en);

            try {
                Node.Property nameProp = null;
                Node.Property altNameProp = null;
                Node.Property defProp = null;

                // String currentVesselCode = Utilities.actionsGlobalContext().lookup(CurrentVessel.class).getConcept().getCode();
                nameProp = new PropertySupport.Reflection(label, String.class, "getPrefLabel", null);
                altNameProp = new PropertySupport.Reflection(label, String.class, "getAltLabel", null);
                defProp = new PropertySupport.Reflection(label, String.class, "getDefinition", null);

                Node.Property kindProp = new PropertySupport.Reflection(/*infoConcept*/this.concept, String.class, "getKind", null);
                Node.Property uriProp = new PropertySupport.Reflection(uri, String.class, "toASCIIString", null);
                Node.Property urnProp = new PropertySupport.Reflection(term, String.class, "getIdentifierUrn", null);
                Node.Property statusProp = new PropertySupport.Reflection(term, String.class, "getStatusName", null);
                Node.Property creationDateProp = new PropertySupport.Reflection(term, Date.class, "getCreationDate", null);
                // Node.Property toStringProp = new PropertySupport.Reflection(this.concept, String.class,"toString", null);

                nameProp.setName("label");
                altNameProp.setName("alt label");
                defProp.setName("definition");
                kindProp.setName("kind");
                uriProp.setName("uri");
                urnProp.setName("urn");
                statusProp.setName("status");
                creationDateProp.setName("creation date");
                // toStringProp.setName("internal details");

                set.put(nameProp);
                set.put(altNameProp);
                set.put(defProp);
                set.put(kindProp);
                set.put(uriProp);
                set.put(urnProp);
                set.put(statusProp);
                set.put(creationDateProp);
                // set.put(toStringProp);

            } catch (NoSuchMethodException ex) {
                ErrorManager.getDefault();
            }

            sheet.put(set);

        }
        return sheet;
    }
}
