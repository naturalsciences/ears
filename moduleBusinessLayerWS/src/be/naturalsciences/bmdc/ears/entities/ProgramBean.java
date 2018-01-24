/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

/**
 *
 * @author yvan
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.SerializationUtils;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "program")
public class ProgramBean implements Serializable, Cloneable, IProgram, Comparable<ProgramBean> {

    public ProgramBean() {

    }

    private static final long serialVersionUID = 1L;

    private String programId;

    private String cruiseId;

    private String originatorCode;

    private String piName;

    private String description;

    private Set<ProjectBean> projects = new HashSet();

    public ProgramBean(String programId, String cruiseId) {
        this.programId = programId;
        this.cruiseId = cruiseId;
    }

    @XmlAttribute(name = "programId")
    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId.trim();
    }

    @XmlAttribute(name = "cruiseId")
    public String getCruiseId() {
        return cruiseId;
    }

    public void setCruiseId(String cruiseId) {
        this.cruiseId = cruiseId;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "originatorCode")
    public String getOriginatorCode() {
        return originatorCode;
    }

    public void setOriginatorCode(String originatorCode) {
        this.originatorCode = originatorCode.trim();
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "PIName")
    public String getPiName() {
        return piName;
    }

    public void setPiName(String piName) {
        this.piName = piName.trim();
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }

    @XmlElementWrapper(namespace = "http://www.eurofleets.eu/", name = "projects")//a l interieur des projects on a des elements name voir doc page 60
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "projects")
    public Set<ProjectBean> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectBean> ssa) {
        this.projects = ssa;
    }

    public String getProjectIds() { //get Id because projectName Error
        //Extract Sea Areas
        StringBuilder projectIds = new StringBuilder("");
        Boolean isFirst = true;
        for (ProjectBean pId : this.getProjects()) {
            if (!isFirst) {
                projectIds.append(",");

            } else {
                isFirst = false;
            }
            projectIds.append(pId.getId());
        }
        return projectIds.toString();
    }

    public String getNameOfProject() {
        //Extract Name Of Project
        StringBuilder names = new StringBuilder("");
        Boolean isFirst = true;
        for (ProjectBean sa : this.getProjects()) {
            if (!isFirst) {
                names.append(",");

            } else {
                isFirst = false;
            }
            names.append(sa.getCode());
        }
        return names.toString();
    }

    @Override
    public String toString() {

        String pi = this.getPiName();
        if (pi == null) {
            pi = "no PI";
        }
        return this.getProgramId() + " (" + this.getPiName() + ")";
    }

    public String getProjectsNameString() {

        StringBuilder sb = new StringBuilder();
        for (ProjectBean sa : this.getProjects()) {
            sb.append("name of project: ").append(sa.getCode());
            sb.append("<br/>");
            System.out.print(sb.toString());
        }
        return sb.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ProgramBean duplicationProgram(ProgramBean programSource) {
        ProgramBean programTarget = null;
        try {
            programTarget = (ProgramBean) SerializationUtils.clone(programSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return programTarget;
    }

    @Override
    public int compareTo(ProgramBean other) {
        return this.getProgramId().compareToIgnoreCase(other.getProgramId());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ProgramBean) {
            ProgramBean other = (ProgramBean) o;
            return this.getProgramId().equals(other.getProgramId()) && this.getCruiseId().equals(other.getCruiseId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.programId);
        hash = 23 * hash + Objects.hashCode(this.cruiseId);
        return hash;
    }

    public boolean isLegal() {
        return getProgramId() != null && !getProgramId().isEmpty() && getCruiseId() != null && !getCruiseId().isEmpty() && getPiName() != null && !getPiName().isEmpty() && getOriginatorCode() != null && !getOriginatorCode().isEmpty() /*&& getProjects() != null && !getProjects().isEmpty()*/;
    }

    public String cleanName() {
        String programName = this.getProgramId();
        List<String> replacements = new ArrayList<>();
        replacements.add(" ");
        replacements.add("_");
        replacements.add("<");
        replacements.add(">");
        replacements.add(":");
        replacements.add("\"");
        replacements.add("/");
        replacements.add("\\");
        replacements.add("|");
        replacements.add("?");
        replacements.add("*");
        for (String replacement : replacements) {
            programName = programName.replace(replacement, "_");
        }
        return programName.toLowerCase();
    }
}
