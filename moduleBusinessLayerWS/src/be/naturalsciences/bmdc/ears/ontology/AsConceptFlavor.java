/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

import be.naturalsciences.bmdc.ears.ontology.entities.Action;
import be.naturalsciences.bmdc.ears.ontology.entities.FakeConcept;
import be.naturalsciences.bmdc.ears.ontology.entities.Process;
import be.naturalsciences.bmdc.ears.ontology.entities.Property;
import be.naturalsciences.bmdc.ears.ontology.entities.Tool;
import be.naturalsciences.bmdc.ears.ontology.entities.ToolCategory;
import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import java.awt.datatransfer.DataFlavor;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 *
 * @author Thomas Vandenberghe
 */
public class AsConceptFlavor extends DataFlavor {

    public static final AsConceptFlavor CONCEPT_FLAVOR = new AsConceptFlavor();

    public static final AsConceptFlavor ROOT_FLAVOR = FakeConcept.OWN_DATA_FLAVOR;
    public static final AsConceptFlavor TOOLCATEGORY_FLAVOR = ToolCategory.OWN_DATA_FLAVOR;
    public static final AsConceptFlavor TOOL_FLAVOR = Tool.OWN_DATA_FLAVOR;
    public static final AsConceptFlavor PROCESS_FLAVOR = Process.OWN_DATA_FLAVOR;
    public static final AsConceptFlavor ACTION_FLAVOR = Action.OWN_DATA_FLAVOR;
    public static final AsConceptFlavor PROPERTY_FLAVOR = Property.OWN_DATA_FLAVOR;

    public static final String NAME = "Concept";

    private Class<? extends AsConcept> repClass;

    public AsConceptFlavor() {
        super(AsConcept.class, "Concept");
        this.repClass = AsConcept.class;
    }

    public AsConceptFlavor(Class<? extends AsConcept> cls, String humanPresentableName) {
        super(cls, humanPresentableName);
        this.repClass = cls;
    }

    public Class<? extends AsConcept> getRepClass() {
        return repClass;
    }

    /**
     * *
     * Return whether the current flavor (i.e. Class) can accept another flavor.
     * I.e. if objects of class this can have other as children
     *
     * @return
     */
    public boolean canHaveAsChild(AsConceptFlavor other) {
        if (other == null) {
            return false;
        }
        Class<? extends AsConcept> thisClass = this.repClass;
        Class<? extends AsConcept> otherClass = other.repClass;

        if (other.getHumanPresentableName().equals(NAME) || this.getHumanPresentableName().equals(NAME)) {
            return false;

            /*} else if (thisClass.equals(Tool.class) && otherClass.equals(FakeConcept.class)) {
             return true;*/
        } else {
            try {
                Method thisGetChildrenClassMethod = thisClass.getMethod("getChildType");
                AsConcept c = thisClass.newInstance();
                //Method otherGetParentClassMethod = otherClass.getMethod("getParentClass");
                if (thisClass.equals(Tool.class) && thisClass.equals(otherClass)) { //only for tools
                    return true;
                }
                return thisGetChildrenClassMethod.invoke(c) == otherClass;
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * *
     * Return whether the current flavor (i.e. Class) can accept another flavor.
     * I.e. if objects of class this can have other as children
     *
     * @return
     */
    public boolean canHaveAsGrandChild(AsConceptFlavor other) {
        Class<? extends AsConcept> grandParentClass = this.repClass;
        Class<? extends AsConcept> grandChildClass = other.repClass;

        if (other.getHumanPresentableName().equals(NAME) || this.getHumanPresentableName().equals(NAME)) {
            return false;
        } else {
            try {
                Method thisGetChildrenClassMethod = grandParentClass.getMethod("getChildType");
                AsConcept c = grandParentClass.newInstance();
                Class<? extends AsConcept> parentClass = (Class) thisGetChildrenClassMethod.invoke(c);
                c = parentClass.newInstance();
                thisGetChildrenClassMethod = parentClass.getMethod("getChildType");
                return thisGetChildrenClassMethod.invoke(c) == grandChildClass;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        } else {
            AsConceptFlavor other = (AsConceptFlavor) o;
            return this.repClass.equals(other.repClass) && this.getHumanPresentableName().equals(other.getHumanPresentableName());
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.repClass);
        return hash;
    }

}
