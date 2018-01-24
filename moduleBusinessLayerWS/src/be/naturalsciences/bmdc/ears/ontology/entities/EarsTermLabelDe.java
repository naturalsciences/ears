package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ontology.entities.IEarsTermLabelDe;
import be.naturalsciences.bmdc.ontology.entities.Term;
import java.io.Serializable;
import java.net.URI;
import thewebsemantic.Id;

public class EarsTermLabelDe implements IEarsTermLabelDe, Serializable {

    //private static final long serialVersionUID = 1L;
    @Id
    protected URI uri;
    protected Long id;

    private String prefLabel;

    private String altLabel;

    private String definition;

    private EarsTerm earsTerm;

    public EarsTermLabelDe() {
    }

    @Override
    public String getPrefLabel() {
        return prefLabel;
    }

    @Override
    public void setPrefLabel(String prefLabel) {
        String old = this.prefLabel;
        this.prefLabel = prefLabel;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public void setUri(URI uri) {
        if (uri.getRawFragment() == null) {
            throw new IllegalArgumentException("The URI should have a fragment part (#).");
        }
        this.uri = uri;
    }

    @Override
    public String getAltLabel() {
        return altLabel;
    }

    @Override
    public void setAltLabel(String altLabel) {
        if ("<null value>".equals(altLabel)) {
            altLabel = null;
        }
        String old = this.altLabel;
        this.altLabel = altLabel;
    }

    @Override
    public String getDefinition() {
        return definition;
    }

    @Override
    public void setDefinition(String definition) {
       if ("<null value>".equals(definition)) {
            definition = null;
        }
        String old = this.definition;
        this.definition = definition;
    }

    @Override
    public Term getEarsTerm() {
        return (Term) earsTerm;
    }

    @Override
    public void setEarsTerm(Term earsTerm) {
        this.earsTerm = (EarsTerm) earsTerm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash ^= (id != null ? id.hashCode() : 0);
        hash ^= (uri != null ? uri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof EarsTermLabelDe)) {
            return false;
        }
        EarsTermLabelDe other = (EarsTermLabelDe) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        if ((this.uri == null && other.uri != null) || (this.uri != null && !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EarsTermLabelDe[  id=" + id + " ]";
    }
}
