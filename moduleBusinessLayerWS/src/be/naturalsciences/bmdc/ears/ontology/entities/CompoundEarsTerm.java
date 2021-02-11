package be.naturalsciences.bmdc.ears.ontology.entities;

import be.naturalsciences.bmdc.ontology.entities.EarsTermLabel;
import be.naturalsciences.bmdc.ontology.entities.ICompoundEarsTerm;
import be.naturalsciences.bmdc.ontology.entities.IEarsTerm;
import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import thewebsemantic.Id;

public class CompoundEarsTerm implements ICompoundEarsTerm<EarsTerm, ItemStatus>, Serializable {

    private EarsTermLabel compoundEarsTermLabel;

    @Id
    protected URI uri;
    protected Long id;

    private String kind;

    private ItemStatus status;

    private String statusName;

    private String origUrn;

    private String publisherUrn;

    private Date creationDate;

    private String submitter;

    @Override
    public EarsTermLabel getEarsTermLabel() {
        return compoundEarsTermLabel;
    }

    @Override
    public EarsTermLabel getEarsTermLabel(IEarsTerm.Language lng) {
        return getEarsTermLabel();
    }

    @Override
    public void setTermLabel(EarsTermLabel compoundEarsTermLabel) {
        this.compoundEarsTermLabel = compoundEarsTermLabel;
        if (compoundEarsTermLabel != null) {
            compoundEarsTermLabel.setEarsTerm(this);
        }
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
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public ItemStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    @Override
    public String getStatusName() {
        if (status != null) {
            return this.status.getName();
        } else {
            return null;
        }
    }

    @Override
    public void setStatusName(String status) {
        this.statusName = status;
        if (this.status != null) {
            this.status.setName(status);
        } else {
            setStatus(new ItemStatus());
            this.status.setName(status);
        }
    }

    @Override
    public String getOrigUrn() {
        return origUrn;
    }

    @Override
    public void setOrigUrn(String origUrn) {
        this.origUrn = origUrn;
    }

    @Override
    public String getPublisherUrn() {
        return publisherUrn;
    }

    @Override
    public void setPublisherUrn(String publisherUrn) {
        this.publisherUrn = publisherUrn;
    }

    @Override
    public EarsTerm getSubstituteRef() {
        return null;
    }

    @Override
    public void setSubstituteRef(EarsTerm substitute) {
        //method that does nothing
    }

    @Override
    public IEarsTerm getSubstituteBackRef() {
        return null;
    }

    @Override
    public void setSubstituteBackRef(EarsTerm e) {
        //method that does nothing
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public boolean isLabelEditable() {
        return false;
    }

    @Override
    public boolean isBodcTerm() {
        return false;
    }

    @Override
    public boolean isDeprecated() {
        return false;
    }

    @Override
    public int getLastId() {
        return EarsTerm.lastId;
    }

    public static void setLastId(int lastId) {
        //if (EarsTerm.lastId == 0) {
        //CompoundEarsTerm.lastId = lastId;
        // }
    }

    @Override
    public String getSubmitter() {
        return submitter;
    }

    @Override
    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public void fixSubstitutes() {
        //does nothing
    }

    @Override
    public String getVersionInfo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setVersionInfo(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isOwnTerm(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setModifDate(Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date getModifDate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isPublished() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCreator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCreator(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSameAs() {
        return null;
    }

    @Override
    public void setSameAs(String string) {
        
    }

    @Override
    public String getBroader() {
        return null;
    }

    @Override
    public void setBroader(String string) {

    }

    @Override
    public void setTermLabel(EarsTermLabel arg0, IEarsTerm.Language arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getIsMeta() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setIsMeta(boolean arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getController() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setController(String arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
