/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ears.ontology.AsConceptFlavor;
import be.naturalsciences.bmdc.ontology.entities.IVessel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import thewebsemantic.Namespace;
import thewebsemantic.RdfType;

@Namespace("http://ontologies.ef-ears.eu/ears2/1#")
@RdfType("Vessel")
public class Vessel extends Tool implements Transferable, IVessel<EarsTerm, ToolCategory, Tool, Process, SpecificEventDefinition, GenericEventDefinition>, Serializable {

    //private static final long serialVersionUID = 1L;
    /*@Id
     protected URI uri;
    
     private Integer codeRef;

     private ToolCategory toolCategoryRef;*/
 /*@Override
     public ToolCategory getToolCategoryRef() {
     return toolCategoryRef;
     }

     @Override
     public void setToolCategoryRef(ToolCategory toolCategoryRef) {
     this.toolCategoryRef = toolCategoryRef;
     }*/

 /*@Override
     public int hashCode() {
     int hash = 0;
     hash ^= (codeRef != null ? codeRef.hashCode() : 0);
     return hash;
     }

     @Override
     public boolean equals(Object object) {
     // TODO: Warning - this method won't work in the case the id fields are not set
     if (this == object) {
     return true;
     }
     if (!(object instanceof Vessel)) {
     return false;
     }
     Vessel other = (Vessel) object;
     if ((this.codeRef == null && other.codeRef != null) || (this.codeRef != null && !this.codeRef.equals(other.codeRef))) {
     return false;
     }
     return true;
     }*/
    @Override
    public String toString() {
        return "Vessel[ codeRef=" + getId() + " ]";
    }

    public transient static final AsConceptFlavor OWN_DATA_FLAVOR = new AsConceptFlavor(Tool.class, "tool");

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{OWN_DATA_FLAVOR};
        //return null;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return false;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor == null) {//OWN_DATA_FLAVOR) {
            return this;
        } else if (flavor instanceof AsConceptFlavor) { //a fancy way to get the type of this
            return null;
        } else if (flavor == null) {
            return this;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

}
