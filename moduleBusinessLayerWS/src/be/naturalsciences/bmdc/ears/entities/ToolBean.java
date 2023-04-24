package be.naturalsciences.bmdc.ears.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "tool")
public class ToolBean {

    private String name;
    private String identifier;
    private ToolBean parentTool;

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "identifier")
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "parentTool")
    public ToolBean getParentTool() {
        return parentTool;
    }

    public void setParentTool(ToolBean parentTool) {
        this.parentTool = parentTool;
    }

    public ToolBean() {
    }

    public ToolBean(ToolBean tool) {
        this.name = tool.name;
        this.identifier = tool.identifier;
        if (tool.parentTool != null) {
            this.parentTool = new ToolBean(tool.parentTool);
        }
    }

    public ToolBean(String name, String identifier, ToolBean parentTool) {
        this.name = name;
        this.identifier = identifier;
        this.parentTool = parentTool;
    }

    @Override
    public String toString() {
        return "ToolBean{" + "name=" + name + '}';
    }

    public String fullName() {
        String r = this.name;
        if (getParentTool() != null) {
            r = r + " ∈ " + getParentTool().getName();
        }
        return r;
    }

}
