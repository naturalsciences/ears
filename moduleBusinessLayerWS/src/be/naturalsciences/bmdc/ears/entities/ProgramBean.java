/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

/**
 *
 * @author Yvan Stojanov
 */
import be.naturalsciences.bmdc.ontology.writer.JSONReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang3.SerializationUtils;
import org.openide.util.Exceptions;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "program")
public class ProgramBean implements Serializable, Cloneable, IProgram, Comparable<ProgramBean> {

    private static final long serialVersionUID = 1L;

    private String programId;

    private String principalInvestigator;

    private String description;

    private Set<ProjectBean> projects = new HashSet();
    private List<Person> principalInvestigators;

    public ProgramBean(String programId) {
        this.programId = programId;
    }

    public ProgramBean() {

    }

    @XmlAttribute(name = "programId")
    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId.trim();
    }

    /**
     * *
     * Returns the list of scientists and organisations as one string
     *
     * @return
     */
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "principalInvestigator")
    public String getPrincipalInvestigator() {
        /*if (principalInvestigator != null) {
         return principalInvestigator;
         } else {*/
        return stringifyScientistList();
        // }
    }

    public void setPrincipalInvestigator(String principalInvestigator) {
        this.principalInvestigator = principalInvestigator;
        this.principalInvestigators = scientistStringToList(principalInvestigator);
    }

    /**
     * *
     * Return a list of singleton maps of String, String representing all chief
     * scientists and their institutions.
     *
     * @return
     */
    @XmlTransient
    public List<Person> getPrincipalInvestigators() {
        return principalInvestigators;
    }

    /**
     * *
     * Set the list of singleton maps of String, String representing all chief
     * scientists and their institutions to the given argument.
     *
     * @return
     */
    public void setPrincipalInvestigators(List<Person> principalInvestigators) {
        this.principalInvestigator = stringifyScientistList();
        this.principalInvestigators = principalInvestigators;
    }

    @XmlTransient
    public String getNicePIString() {
        StringJoiner sb = new StringJoiner(", ");
        if (principalInvestigators != null) {
            int ii = principalInvestigators.size();
            if (ii == 0) {
                return "No Principal investigator specified";
            } else {
                for (Person p : principalInvestigators) {
                    sb.add(p.firstName + " " + p.lastName);
                }
            }
            return sb.toString();
        } else {
            return "No Principal investigator specified";
        }
    }

    /**
     * *
     * Convert a list of scientists and their organisations represented in json
     * to a list of Scientist
     *
     * @param principalInvestigator
     * @return
     */
    private List<Person> scientistStringToList(String principalInvestigator) {
        if (principalInvestigator == null) {
            return new ArrayList();
        }
        List<Person> r;
        if (JSONReader.isValidJSON(principalInvestigator)) {
            try {
                principalInvestigator = URLDecoder.decode(principalInvestigator, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Exceptions.printStackTrace(ex);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<Person>>() {
            }.getType();

            r = new Gson().fromJson(principalInvestigator, type);
            return r;
        } else {
            r = new ArrayList();
            r.add(new Person(principalInvestigator, principalInvestigator, null, null, null));
            return r;
        }
    }

    /**
     * *
     * Output a JSON representation of
     *
     * @return
     */
    private String stringifyScientistList() {
        String json = new Gson().toJson(getPrincipalInvestigators());
        return json;
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
        return this.getProgramId() + " (" + getNicePIString() + ")";
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
            return this.getProgramId().equals(other.getProgramId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.programId);
        return hash;
    }

    public boolean isLegal() {
        return getProgramId() != null && !getProgramId().isEmpty() && !getPrincipalInvestigator().isEmpty() /*!= null && !getPiFirstName().isEmpty() && getPiLastName() != null && !getPiLastName().isEmpty() && getPiOrganisationCode() != null && !getPiOrganisationCode().isEmpty() && getProjects() != null && !getProjects().isEmpty()*/;
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

    @Override
    public String getName() {
        return getProgramId();
    }
}
