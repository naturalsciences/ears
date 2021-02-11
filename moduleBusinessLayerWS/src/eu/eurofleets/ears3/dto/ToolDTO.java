/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.eurofleets.ears3.dto;

import be.naturalsciences.bmdc.ears.entities.ToolBean;
import javax.persistence.Transient;

/**
 *
 * @author thomas
 */
public class ToolDTO {

    public LinkedDataTermDTO tool;
    public LinkedDataTermDTO parentTool;

    public ToolDTO() {
    }

    public ToolDTO(LinkedDataTermDTO tool, LinkedDataTermDTO parentTool) {
        this.tool = tool;
        this.parentTool = parentTool;
    }

    public ToolDTO(ToolBean tool) {
        this.tool = new LinkedDataTermDTO(tool.getIdentifier(), null, tool.getName());
        if (tool.getParentTool() != null) {
            this.parentTool = new LinkedDataTermDTO(tool.getParentTool().getIdentifier(), null, tool.getParentTool().getName());;
        }
    }

    @Transient
    public String fullName() {
        String r = this.tool.name;
        if (this.parentTool != null) {
            r = r + " ∈ " + this.parentTool.name;
        }
        return r;
    }
}
