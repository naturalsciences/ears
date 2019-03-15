/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.gui;

import be.naturalsciences.bmdc.ears.comparator.SimpleAsConceptNodeComparator;
import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.ontology.Individuals;
import be.naturalsciences.bmdc.ears.ontology.entities.Tool;
import be.naturalsciences.bmdc.ears.ontology.entities.Vessel;
import be.naturalsciences.bmdc.ears.utils.Cloner;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Utilities;

/**
 *
 * @author thomas
 */
public class ClassChildren extends Children.Keys {

    // public static final Class[] classes = new Class[]{ToolCategory.class, Tool.class, Process.class, Action.class, Property.class};
    //private final Collection<AsConcept> ontologyDataObjects;
    //private final Class cls;
    public ClassChildren(/*Class cls, */Collection<AsConcept> ontologyDataObjects) {
        //this.ontologyDataObjects = ontologyDataObjects;
        //this.cls = cls;
        setKeys(ontologyDataObjects);

    }

    public static enum Operations {
        ADD, REMOVE
    }

    @Override
    protected Collection<Node> initCollection() {
        return new TreeSet(new SimpleAsConceptNodeComparator());
    }

    public static Node[] createClassNodes(Class cls, Individuals individuals, Operations op) {
        Set<AsConcept> concepts = null;
        if (individuals == null) {
            throw new IllegalArgumentException("Individuals canot be null.");
        }
        concepts = individuals.get(cls);
        return createClassNodes(cls, concepts, op);
    }

    public static Node[] createClassNodes(Class cls, Collection<AsConcept> concepts, Operations op) {
        if (concepts == null) {
            throw new IllegalArgumentException("Concepts canot be null.");
        }

        boolean doingTools = false;
        if (cls.equals(Tool.class)) {
            doingTools = true;
        }
        boolean doingVessels = false;
        if (cls.equals(Vessel.class)) {
            doingVessels = true;
        }
        ArrayList<Node> childNodes = new ArrayList(concepts.size());

        for (AsConcept originalConcept : concepts) {
            AsConcept copiedConcept = null;
            //System.out.println("about to perform isolate(). original: " + originalConcept.toString() + " has " + originalConcept.getChildren(null).size() + "children");
            if (op.equals(Operations.ADD)) {

                copiedConcept = Cloner.clone(originalConcept);
                //copiedConcept = originalConcept.clone(new IdentityHashMap());

                copiedConcept.isolate();//make sure it has no children etc. 
                // System.out.println("after isolate(). original: " + originalConcept.toString() + " has " + originalConcept.getChildren(null).size() + "children");
                //System.out.println("after isolate(). clone: " + copiedConcept.toString() + " has " + copiedConcept.getChildren(null).size() + "children");
            } else {
                copiedConcept = originalConcept;
            }
            if (copiedConcept != null && copiedConcept.getClass().equals(cls)) {
                if (doingTools) {
                    if (copiedConcept.getKind().equals("VES")) {
                        continue;
                    }
                }
                if (doingVessels) {
                    CurrentVessel currentVessel = Utilities.actionsGlobalContext().lookup(CurrentVessel.class);
                    if (currentVessel != null && currentVessel.getConcept() != null && !currentVessel.getConcept().equalsConcept(copiedConcept)) {
                        continue;
                    }
                }
                if (copiedConcept.getTermRef() != null && copiedConcept.getTermRef().isDeprecated()) {
                    continue;
                }
                SimpleAsConceptNode childNode = new SimpleAsConceptNode(copiedConcept);
                String uniqueName = null;
                if (copiedConcept.getTermRef() != null) {
                    uniqueName = copiedConcept.getTermRef().getOrigUrn();
                } else {
                    uniqueName = "PROBLEM-CASE";
                    Messaging.report("There was a problem with concept " + copiedConcept.getUri().toASCIIString() + " of one of the trees: it has no or multiple names. If it was the base tree, please contact the EARS governance team.", Message.State.BAD, ClassChildren.class, true);
                }

                childNode.setName(uniqueName);
                childNodes.add(childNode);

            }
        }
        childNodes.trimToSize();
        return childNodes.toArray(new Node[0]);
    }

    /*@Override
     public boolean add(Node[] arr) {
     List<Node> toAdd = new ArrayList(Arrays.asList(arr));
     List<Node> ownNodes = Arrays.asList(this.getNodes());
     Iterator<Node> iter = toAdd.iterator();

     while (iter.hasNext()) {
     Node node = iter.next();
     if (ownNodes.get(node)) {
     iter.remove();
     }
     }
     return super.add(toAdd.toArray(arr));
     }*/
    @Override
    protected Node[] createNodes(Object t) {
        AsConcept a = (AsConcept) t;
        SimpleAsConceptNode[] arr = {new SimpleAsConceptNode(a)};
        return arr;
        /*SimpleAsConceptNode[] childNodes = new SimpleAsConceptNode[ontologyDataObjects.size()];
         int i = 0;
         for (AsConcept concept : ontologyDataObjects) {
         childNodes[i] = new SimpleAsConceptNode(concept);
         i++;
         }
         return childNodes;*/
    }

    @Override
    protected void addNotify() {
        super.addNotify(); //To change body of generated methods, choose Tools | Templates.
    }

}
