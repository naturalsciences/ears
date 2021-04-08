package eu.eurofleets.ears3.dto;

import be.naturalsciences.bmdc.ontology.entities.AsConcept;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author thomas
 */
@XmlAccessorType(XmlAccessType.FIELD) //ignore all the getters
public class LinkedDataTermDTO {

    public String identifier;  //an identifier in an external vocabulary, i.e. the BODC Tool list L22 (can only be url)
    public String transitiveIdentifier;  //an identifier in a transitive vocanbulary, i.e. the EARS vocabulary
    public String name;

    public LinkedDataTermDTO() {
    }

    public LinkedDataTermDTO(String identifier, String transitiveIdentifier, String name) {
        this.identifier = identifier;
        this.transitiveIdentifier = transitiveIdentifier;
        this.name = name;
    }

    public LinkedDataTermDTO(AsConcept concept) {
        this.identifier = concept.getTermRef().getUri().toString(); //reads the BODC identifier
        this.name = concept.getTermRef().getEarsTermLabel().getPrefLabel();
        this.transitiveIdentifier = concept.getUri().toString(); //reads the EARS identifier
    }

}
