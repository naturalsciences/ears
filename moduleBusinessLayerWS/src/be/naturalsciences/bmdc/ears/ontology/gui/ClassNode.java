/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.gui;

import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import java.util.Set;
import org.openide.nodes.AbstractNode;

/**
 *
 * @author thomas
 */
public class ClassNode extends AbstractNode {

    private final Class cls;

    public ClassNode(Class cls, Set<AsConcept> ontologyDataObjects) {
        //super(null);
        super(new ClassChildren(/*cls, */ontologyDataObjects));
        this.cls = cls;
    }


    @Override
    public String getName() {
        return cls.getSimpleName();
    }
    

}
