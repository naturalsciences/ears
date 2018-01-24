package be.naturalsciences.bmdc.ears.entities;

import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "tool")
@XmlAccessorType(XmlAccessType.FIELD)
public class ToolBean {

    public ToolBean() {
        super();
    }

    @XmlElementWrapper(namespace = "http://www.eurofleets.eu/", name = "tools")
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "tool")
    private Set<ToolBean> tools = new HashSet<ToolBean>();

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "name")
    private String name;

    private static StringBuilder dir = new StringBuilder("+");
    private static StringBuilder space = new StringBuilder(" ");

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ToolBean> getTools() {
        return tools;
    }

    public void setTools(Set<ToolBean> st) {
        this.tools = st;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.dir).append("Tool Name....:").append(this.getName());
        sb.append("\n");

        if (!this.tools.isEmpty()) {
            sb.append(this.space).append("Number of Tools....").append(this.getTools().size());
            sb.append("\n");
            sb.append(this.space).append("**** Tools ******\n");

            for (ToolBean tool : this.getTools()) {
                sb.append(this.space).append(tool.toString());
            }
            sb.append(this.space).append("**** End Tools******\n");
            sb.append("\n");
        }

        return sb.toString();
    }
}
