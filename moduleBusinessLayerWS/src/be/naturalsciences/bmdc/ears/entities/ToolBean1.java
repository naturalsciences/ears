package be.naturalsciences.bmdc.ears.entities;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "tool")

public class ToolBean1 implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;

    public ToolBean1() {
        super();
    }

    public ToolBean1(String name) {
        super();
        this.name = name;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName()).append("\n");
        //ys sb.append("name: ").append(this.getName()).append("\n");
        System.out.print(sb.toString());
        return sb.toString();
    }

}
/*
 <ewsl:event eventId="Event0002">
 <ewsl:id>2</ewsl:id>
 <ewsl:timeStamp>2014-04-15T12:00:00+02:00</ewsl:timeStamp>
 <ewsl:actor>oscar</ewsl:actor>
 <ewsl:subjectName>OffShore Data Center</ewsl:subjectName>
 <ewsl:name>OtherEvent</ewsl:name>
 <ewsl:property>Some Other Event</ewsl:property>
 <ewsl:categoryName>Some Other Kind of Event</ewsl:categoryName>

 <ewsl:tool>
 <ewsl:name>Some Tool</ewsl:name>
 </ewsl:tool>

 </ewsl:event>

 */
