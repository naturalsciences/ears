/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ontology.entities.IProcessActionPK;
import java.io.Serializable;

public class ProcessActionPK implements IProcessActionPK, Serializable {

    private Long processRef;
    private Long actionRef;

    public ProcessActionPK() {
    }

    public ProcessActionPK(Long processRef, Long actionRef) {
        this.processRef = processRef;
        this.actionRef = actionRef;
    }

    @Override
    public Long getProcessRef() {
        return processRef;
    }

    @Override
    public void setProcessRef(Long processRef) {
        this.processRef = processRef;
    }

    @Override
    public Long getActionRef() {
        return actionRef;
    }

    @Override
    public void setActionRef(Long actionRef) {
        this.actionRef = actionRef;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash ^= (long) processRef;
        hash ^= (long) actionRef;
        return (int) hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof ProcessActionPK)) {
            return false;
        }
        ProcessActionPK other = (ProcessActionPK) object;
        if (this.processRef != other.processRef) {
            return false;
        }
        if (this.actionRef != other.actionRef) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProcessActionPK[ processRef=" + processRef + ", actionRef=" + actionRef + " ]";
    }

}
