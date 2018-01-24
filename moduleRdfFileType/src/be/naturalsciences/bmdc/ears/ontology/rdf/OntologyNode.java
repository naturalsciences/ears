package be.naturalsciences.bmdc.ears.ontology.rdf;

import be.naturalsciences.bmdc.ontology.IOntologyModel;
import java.awt.Image;
import java.util.List;
import java.util.Objects;
import javax.swing.Action;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Thomas Vandenberghe
 */
public class OntologyNode extends DataNode implements Comparable<OntologyNode> {

    private RdfFileTypeDataObject ontologyDataObject;

    public OntologyNode(RdfFileTypeDataObject ontologyDataObject/*, InstanceContent ic*/) {
        super(ontologyDataObject, Children.LEAF, Lookups.singleton(ontologyDataObject)/*new ProxyLookup(Lookups.singleton(ontologyDataObject), new AbstractLookup(ic))*/);
        //this.ic = ic;
        this.ontologyDataObject = ontologyDataObject;
        this.ontologyDataObject.setNode(this);

        if (this.ontologyDataObject != null && !this.ontologyDataObject.getName().isEmpty()) {
            //this.ontologyFile = new File(this.ontologyDataObject.getPrimaryFile().getPath());
            this.setChildren(Children.LEAF);
        }
    }

    public RdfFileTypeDataObject getOntologyDataObject() {
        return ontologyDataObject;
    }

    /**
     * *
     * Returns the absolute path (path, filename) of the underlying data object.
     *
     * @return
     */
    public String getFileName() {
        return ontologyDataObject.getFile().getAbsolutePath();
    }

    @Override
    public String getHtmlDisplayName() {
        if (this.ontologyDataObject.isCorrect()) {
            return getDisplayName();
        } else {
            return "<font color='#b86919'>" + getDisplayName() + "</font>";
        }
    }

    /**
     * *
     * Returns the name of the underlying data object
     *
     * @return
     */
    @Override
    public String getName() {
        if (this.ontologyDataObject != null) {
            return this.ontologyDataObject.getNiceName();
        }
        return null;
    }

    @Override
    public String getDisplayName() {
        if (this.ontologyDataObject != null) {
            return this.ontologyDataObject.getNiceName();
        }
        return null;
    }

    @Override
    public String getShortDescription() {
        if (!this.ontologyDataObject.isCorrect()) {
            return "Incorrect tree. Can't be edited.";
        } else {
            return this.ontologyDataObject.getOntModel().getScope() + (this.ontologyDataObject.getOntModel().getScopedTo() == null ? " scope" : " scope: " + this.ontologyDataObject.getOntModel().getScopedTo());
        }
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("be/naturalsciences/bmdc/ears/ontology/rdf/rdf-16.png");
    }

    public static final String BROWSE_ONTOLOGY = "be.naturalsciences.bmdc.ears.ontology.rdf.OpenRdfFileTypeActionBrowse";
    public static final String EDIT_ONTOLOGY = "be.naturalsciences.bmdc.ears.ontology.rdf.OpenRdfFileTypeAction";

    @Override
    public Action[] getActions(boolean context) {
        IOntologyModel model = this.ontologyDataObject.getOntModel();
        Action[] actions = {};
        List<?> nodeActions = Utilities.actionsForPath("Loaders/application/rdf+xml/Actions");
        for (int i = 0; i < nodeActions.size(); i++) {
            Action a = (Action) nodeActions.get(i);
            if (a != null) {
                String type = (String) a.getValue("injectable");
                if (model == null) { //this.this.ontologyDataObject.isCorrect();
                    nodeActions.remove(i);
                } else if (!model.actionIsAllowed(type)) {
                    nodeActions.remove(i);
                }
            } else {
                nodeActions.remove(i);
            }

        }

        return nodeActions.toArray(actions);
    }

    @Override
    public Image getOpenedIcon(int i
    ) {
        return getIcon(i);
    }

    @Override
    public int compareTo(OntologyNode other) {
        String name = this.ontologyDataObject.getPrimaryFile().getPath();
        String otherName = other.ontologyDataObject.getPrimaryFile().getPath();
        return name.compareTo(otherName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof OntologyNode)) {
            return false;
        }
        OntologyNode otherOntologyNode = (OntologyNode) other;

        return this.ontologyDataObject.equals(otherOntologyNode.getDataObject());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.ontologyDataObject);
        return hash;
    }

}
