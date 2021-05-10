/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.browser;

import be.naturalsciences.bmdc.ears.ontology.rdf.DirectoryChangeListener;
import be.naturalsciences.bmdc.ears.ontology.rdf.OntologyNode;
import be.naturalsciences.bmdc.ears.ontology.rdf.OntologyNodeComparator;
import be.naturalsciences.bmdc.ears.ontology.rdf.RdfFileTypeDataObject;
import be.naturalsciences.bmdc.ears.ontology.rdf.RdfFileTypeLoader;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.writer.ScopeMap.Scope;
import gnu.trove.set.hash.THashSet;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.Action;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author Thomas Vandenberghe
 */
public class ScopeNode extends AbstractNode implements DirectoryChangeListener {

    private Map<String, Set<OntologyNode>> fileMap = new HashMap();

    private Set<OntologyNode> goodList = new THashSet();
    private Set<OntologyNode> badList = new THashSet();

    public final static RdfFileTypeLoader LOADER = new RdfFileTypeLoader();

    private List<String> scopes;
    private File fileOrFolder;

    public ScopeNode(Children children, String name, String scope, File fileOrFolder) {
        super(children);
        this.scopes = new ArrayList();
        setDisplayName(name);
        this.scopes.add(scope);
        this.fileOrFolder = fileOrFolder;
    }

    public ScopeNode(Children children, String name, String[] scopes, File fileOrFolder) {
        super(children);
        this.scopes = new ArrayList<>(Arrays.asList(scopes));
        setDisplayName(name);
        this.fileOrFolder = fileOrFolder;
    }

    public List<String> getScope() {
        return scopes;
    }

    public void setScope(List<String> scopes) {
        this.scopes = scopes;
    }

    public File getFileOrFolder() {
        return fileOrFolder;
    }

    public void setFileOrFolder(File fileOrFolder) {
        this.fileOrFolder = fileOrFolder;
    }

    @Override
    public javax.swing.Action[] getActions(boolean context) {
        return new Action[]{SystemAction.get(RefreshNodeAction.class)};
    }

    private List<File> getFilesInFileOrFileIfDir() {
        List<File> fList = new ArrayList();

        if (this.fileOrFolder.isDirectory()) {
            fList.addAll(Arrays.asList(fileOrFolder.listFiles()));
        } else {
            fList.add(this.fileOrFolder);
        }
        return fList;
    }

    public Set<IOntologyModel> getOntologyModels() {
        Set<IOntologyModel> models = new THashSet<>();
        for (Node oNode : this.getChildren().getNodes()) {
            OntologyNode ontologyNode = (OntologyNode) oNode;
            IOntologyModel ontModel = ontologyNode.getOntologyDataObject().getOntModel();
            models.add(ontModel);
        }
        return models;
    }

    /**
     * Populate a given node with a File or the contents of a folder if the file
     * is a folder. Ie. set the Children of a Node node to the file itself or
     * the contents of File fileOrFolder.
     *
     */
    public void populateNode() {

        for (File file : getFilesInFileOrFileIfDir()) {
            if (!file.isDirectory()) {
                buildDataObject(file);
            }
        }
        Set<OntologyNode> nodeList = new TreeSet(new OntologyNodeComparator());
        OntologyNode[] nodes = {};
        nodeList.addAll(this.goodList);
        nodeList.addAll(this.badList);
        this.getChildren().add(nodeList.toArray(nodes));
    }

    private static boolean listContains(Collection<OntologyNode> list, File file) {
        for (OntologyNode node : list) {
            if (node.getFileName().equals(file.getAbsolutePath())) {
                return true;
            }
        }
        return false;
    }

    private void buildDataObject(File file) {

        if (!listContains(goodList, file) && !listContains(badList, file)) {
            FileObject fO = FileUtil.toFileObject(file);
            MultiDataObject mdo = null;
            try {
                mdo = LOADER.createMultiObject(fO);
            } catch (Exception ex) {
                Messaging.report("The tree of the file " + file.getAbsolutePath() + " is corrupt and wasn't put in the list", ex, this.getClass(), true);
                mdo = null;
            }
            if (mdo != null && mdo instanceof RdfFileTypeDataObject) {
                RdfFileTypeDataObject rdfO = (RdfFileTypeDataObject) mdo;
                rdfO.addDirectoryChangeListener(this);
                OntologyNode childNode = new OntologyNode(rdfO);
                Scope fileScope = rdfO.getScope().getScope();
                if (fileScope != null && this.getScope().contains(fileScope.toString())) {
                    goodList.add(childNode);
                } else {
                    Messaging.report("The tree of the file " + file.getAbsolutePath() + " is corrupt and wasn't put in the list", Message.State.BAD, this.getClass(), true);
                }
            }
        }
    }

    @Override
    public void refresh() {
        populateNode();
        for (int i = 0; i < goodList.size(); i++) {
            OntologyNode node = (OntologyNode) goodList.toArray()[i];
            boolean foundInDir = false;
            for (File file : getFilesInFileOrFileIfDir()) {
                if (node.getFileName().equals(file.getAbsolutePath())) {
                    foundInDir = true;
                }
            }
            if (!foundInDir) {
                goodList.remove(node);
                this.getChildren().remove(new OntologyNode[]{node});
            }
        }
        for (int i = 0; i < badList.size(); i++) {
            OntologyNode node = (OntologyNode) badList.toArray()[i];
            boolean foundInDir = false;
            for (File file : getFilesInFileOrFileIfDir()) {
                if (node.getFileName().equals(file.getAbsolutePath())) {
                    foundInDir = true;
                }
            }
            if (!foundInDir) {
                badList.remove(node);
                this.getChildren().remove(new OntologyNode[]{node});
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        refresh();
    }

}
