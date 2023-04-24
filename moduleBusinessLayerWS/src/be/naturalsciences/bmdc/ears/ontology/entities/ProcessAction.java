/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ontology.entities.IProcessAction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProcessAction implements IProcessAction<EarsTerm, Process, Action, ProcessAction, ProcessActionPK, SpecificEventDefinition, GenericEventDefinition>, Serializable {

    //private static final long serialVersionUID = 1L;
    protected ProcessActionPK processActionPK;

    private Boolean isDataProvider;

    private Collection<SpecificEventDefinition> specificEventDefinitionCollection;

    private Collection<ProcessAction> processActionCollection;

    private ProcessAction processAction;

    private Process process;

    private Action action;

    private Collection<GenericEventDefinition> genericEventDefinitionCollection;

    public ProcessAction() {
    }

    public ProcessAction(ProcessActionPK processActionPK) {
        this.processActionPK = processActionPK;
    }

    public ProcessAction(long processRef, long actionRef) {
        this.processActionPK = new ProcessActionPK(processRef, actionRef);
    }

    public ProcessAction(Process process, Action action) {
        this.process = process;
        this.action = action;
        this.genericEventDefinitionCollection = new ArrayList<>();
        this.specificEventDefinitionCollection = new ArrayList<>();
        if (process.getId() != null & action.getId() != null) {
            this.processActionPK = new ProcessActionPK(process.getId().longValue(), action.getId().longValue());
        }
    }

    @Override
    public ProcessActionPK getProcessActionPK() {
        return processActionPK;
    }

    @Override
    public void setProcessActionPK(ProcessActionPK processActionPK) {
        this.processActionPK = processActionPK;
    }

    @Override
    public Boolean getIsDataProvider() {
        return isDataProvider;
    }

    @Override
    public void setIsDataProvider(Boolean isDataProvider) {
        this.isDataProvider = isDataProvider;
    }

    @Override
    public Collection<SpecificEventDefinition> getSpecificEventDefinitionCollection() {
        return specificEventDefinitionCollection;
    }

    @Override
    public void setSpecificEventDefinitionCollection(Collection<SpecificEventDefinition> specificEventDefinitionCollection) {
        this.specificEventDefinitionCollection = specificEventDefinitionCollection;
    }

    @Override
    public Collection<ProcessAction> getProcessActionCollection() {
        return processActionCollection;
    }

    @Override
    public void setProcessActionCollection(Collection<ProcessAction> processActionCollection) {
        this.processActionCollection = processActionCollection;
    }

    @Override
    public ProcessAction getProcessAction() {
        return processAction;
    }

    @Override
    public void setProcessAction(ProcessAction processAction) {
        this.processAction = processAction;
    }

    @Override
    public Process getProcess() {
        return process;
    }

    @Override
    public void setProcess(Process process) {
        this.process = process;
    }

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public Collection<GenericEventDefinition> getGenericEventDefinitionCollection() {
        return genericEventDefinitionCollection;
    }

    @Override
    public void setGenericEventDefinitionCollection(Collection<GenericEventDefinition> genericEventDefinitionCollection) {
        this.genericEventDefinitionCollection = genericEventDefinitionCollection;
    }

    @Override
    public Collection<EventDefinition> getEvents() {
        Collection<EventDefinition> cev = new ArrayList<>();
        cev.addAll(getSpecificEventDefinitionCollection());
        cev.addAll(getGenericEventDefinitionCollection());
        return cev;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash ^= (processActionPK != null ? processActionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof ProcessAction)) {
            return false;
        }
        ProcessAction other = (ProcessAction) object;
        if ((this.processActionPK == null && other.processActionPK != null) || (this.processActionPK != null && !this.processActionPK.equals(other.processActionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProcessAction[ processActionPK=" + processActionPK + " ]";
    }

    @Override
    public List<Tool> getTools() {
        List<Tool> join = new ArrayList<>();
        for (EventDefinition e : this.getEvents()) {
            if (e instanceof GenericEventDefinition) {
                GenericEventDefinition ge = (GenericEventDefinition) e;
                Collection<Tool> ct = ge.getToolCategoryRef().getToolCollection();
                if (ct != null && !ct.isEmpty()) {
                    for (Tool t : ct) {
                        if (t != null && !join.contains(t)) {
                            join.add(t);
                        }
                    }

                }
            }
            if (e instanceof SpecificEventDefinition) {
                SpecificEventDefinition se = (SpecificEventDefinition) e;
                Tool t = se.getToolRef();
                if (t != null && !join.contains(t)) {
                    join.add(t);
                }
            }
        }
        return join;
    }

}
