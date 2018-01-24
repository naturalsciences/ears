/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ontology.OntologyConstants;
import be.naturalsciences.bmdc.ontology.entities.IItemStatus;
import java.io.Serializable;
import java.util.Collection;

public class ItemStatus implements IItemStatus<EarsTerm>, Serializable {

    //private static final long serialVersionUID = 1L;
    private String shortName;

    private String name;

    private Collection<EarsTerm> earsTermCollection;

    //private Collection<EarsCode> earsCodeCollection;
    public ItemStatus() {
    }

    /*public ItemStatus(String shortName) {
        this.shortName = shortName;
    }*/
    public ItemStatus(String shortName, String name) {
        if (OntologyConstants.STATUSES.containsKey(shortName) && OntologyConstants.STATUSES.containsValue(name)) {
            this.shortName = shortName;
            this.name = name;
        } else {
            throw new IllegalArgumentException(String.format("Arguments %s & %s are wrong short names and names for the Item status.", shortName, name));
        }
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Collection<EarsTerm> getEarsTermCollection() {
        return earsTermCollection;
    }

    @Override
    public void setEarsTermCollection(Collection<EarsTerm> earsTermCollection) {
        this.earsTermCollection = earsTermCollection;
    }

    /*public Collection<EarsCode> getEarsCodeCollection() {
     return earsCodeCollection;
     }

     public void setEarsCodeCollection(Collection<EarsCode> earsCodeCollection) {
     this.earsCodeCollection = earsCodeCollection;
     }+*/
    @Override
    public int hashCode() {
        int hash = 0;
        hash ^= (shortName != null ? shortName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof ItemStatus)) {
            return false;
        }
        ItemStatus other = (ItemStatus) object;
        if ((this.shortName == null && other.shortName != null) || (this.shortName != null && !this.shortName.equals(other.shortName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ItemStatus[ shortName=" + shortName + " ]";
    }

}
