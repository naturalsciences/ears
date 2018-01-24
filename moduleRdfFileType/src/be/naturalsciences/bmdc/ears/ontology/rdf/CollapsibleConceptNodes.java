/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.rdf;

import be.naturalsciences.bmdc.ears.ontology.gui.Collapsible;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author thomas
 */
public class CollapsibleConceptNodes implements Collapsible {

    private ExplorerManager mgr;

    private BeanTreeView treeView;

    public BeanTreeView getTreeView() {
        return treeView;
    }

    public ExplorerManager getMgr() {
        return mgr;
    }

    public CollapsibleConceptNodes(ExplorerManager mgr, BeanTreeView treeView) {
        this.mgr = mgr;
        this.treeView = treeView;
    }

    @Override
    public void collapse() {
        collapse(treeView, mgr.getSelectedNodes()[0]);
    }

    public void collapse(BeanTreeView treeView, Node currentNode) {
        treeView.collapseNode(currentNode);
        Children c = currentNode.getChildren();
        for (Node next : c.getNodes()) {
            this.collapse(treeView, next);
        }
    }
}
