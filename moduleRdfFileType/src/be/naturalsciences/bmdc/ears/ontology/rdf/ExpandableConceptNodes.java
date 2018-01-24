/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.rdf;

import be.naturalsciences.bmdc.ears.ontology.gui.Expandable;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author thomas
 */
public class ExpandableConceptNodes implements Expandable {

    private ExplorerManager mgr;

    private BeanTreeView treeView;

    public BeanTreeView getTreeView() {
        return treeView;
    }

    public ExplorerManager getMgr() {
        return mgr;
    }

    public ExpandableConceptNodes(ExplorerManager mgr, BeanTreeView treeView) {
        this.mgr = mgr;
        this.treeView = treeView;
    }

    @Override
    public void expand() {
        expand(treeView, mgr.getSelectedNodes()[0]);
    }

    public void expand(BeanTreeView treeView, Node currentNode) {
        treeView.expandNode(currentNode);
        Children c = currentNode.getChildren();
        for (Node next : c.getNodes()) {
            this.expand(treeView, next);
        }
    }

}
