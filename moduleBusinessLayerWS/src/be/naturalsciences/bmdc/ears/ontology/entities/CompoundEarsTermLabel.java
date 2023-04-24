package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ontology.entities.EarsTermLabel;
import be.naturalsciences.bmdc.ontology.entities.Term;
import java.io.Serializable;
import java.net.URI;

/**
 *
 * @author Thomas Vandenberghe
 */
public class CompoundEarsTermLabel implements Serializable, EarsTermLabel {

    private CompoundEarsTerm compoundEarsTerm;
    private String prefLabel;
    private String altLabel;
    private String definition;
    private Term earsTerm;

    public Term getCompoundEarsTerm() {
        return compoundEarsTerm;
    }

    public void setCompoundEarsTerm(CompoundEarsTerm compoundEarsTerm) {
        this.compoundEarsTerm = compoundEarsTerm;
    }

    @Override
    public String getPrefLabel() {
        return prefLabel;
    }

    @Override
    public void setPrefLabel(String prefLabel) {
        this.prefLabel = prefLabel;
    }

    @Override
    public String getAltLabel() {
        return altLabel;
    }

    @Override
    public void setAltLabel(String altLabel) {
        this.altLabel = altLabel;
    }

    @Override
    public String getDefinition() {
        return definition;
    }

    @Override
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public Term getEarsTerm() {
        return earsTerm;
    }

    @Override
    public void setEarsTerm(Term earsTerm) {
        this.earsTerm = earsTerm;
    }

    @Override
    public URI getUri() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long getId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUri(URI uri) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setId(Long l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
