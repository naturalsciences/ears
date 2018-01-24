/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

/**
 *
 * @author Thomas Vandenberghe
 */
import be.naturalsciences.bmdc.ears.utils.DateUtilities;
import be.naturalsciences.bmdc.ontology.writer.JSONReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.openide.util.Exceptions;

//import org.apache.commons.lang3.SerializationUtils;
@XmlRootElement(namespace = "http://www.eurofleets.eu/", name = "cruise")
public class CruiseBean implements Serializable, Cloneable, Comparable<CruiseBean>, ICruise {

    private static final long serialVersionUID = 1L;

    private String realId;//<ewsl:cruise internalId="Final02"> attribut

    private String internalId; //<ewsl:internalId>384</ewsl:internalId> element ok
    private String cruiseName;
    private String startDate;
    private String endDate;
    private Date dStartDate;
    private Date dEndDate;
    private String chiefScientist;
    private String chiefScientistOrganisation;
    private String platformCode;
    private String platformClass;
    private String objectives;
    private String collateCenter;
    private String startingHarbor;
    private String arrivalHarbor;

    @XmlTransient
    private List<Person> chiefScientists = new ArrayList();

    private Set<SeaAreaBean> seaAreas;
    // private String seaAreasIds;

    public final static DateFormat DAY_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    public CruiseBean() {

    }

    @XmlAttribute(name = "id")
    public String getRealId() {
        return realId;
    }

    public void setRealId(String realId) {
        this.realId = realId;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "id")
    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "cruiseName")
    public String getCruiseName() {
        return cruiseName;
    }

    public void setCruiseName(String cruiseName) {
        this.cruiseName = cruiseName.trim();
    }

    /*---*/
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "startDate")
    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) throws ParseException {
        this.dStartDate = be.naturalsciences.bmdc.ears.utils.DateUtilities.parseDate(startDate);
        this.startDate = startDate;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "endDate")
    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) throws ParseException {
        this.dEndDate = be.naturalsciences.bmdc.ears.utils.DateUtilities.parseDate(endDate);
        this.endDate = endDate;
    }

    /*---*/
    public Date getdStartDate() {
        return this.dStartDate;
    }

    public void setdStartDate(Date dStartDate) throws ParseException {
        this.setStartDate(be.naturalsciences.bmdc.ears.utils.DateUtilities.formatDateTime(dStartDate));
        this.dStartDate = dStartDate;
    }

    public Date getdEndDate() {
        return this.dEndDate;
    }

    public void setdEndDate(Date dEndDate) throws ParseException {
        this.setEndDate(be.naturalsciences.bmdc.ears.utils.DateUtilities.formatDateTime(dEndDate));
        this.dEndDate = dEndDate;
    }

    /**
     * *
     * Returns the list of scientists and organisations as one string
     *
     * @return
     */
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "chiefScientist")
    public String getChiefScientist() {
        /*if (chiefScientist != null) {
         return chiefScientist;
         } else {*/
        return stringifyScientistList();
        // }
    }

    public void setChiefScientist(String chiefScientist) {
        this.chiefScientist = chiefScientist;
        this.chiefScientists = scientistStringToList(chiefScientist);
    }

    /**
     * *
     * Obsolete method to read the organisation.
     *
     * @return
     */
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "chiefScientistOrganisation")
    public String getChiefScientistOrganisation() {
        return chiefScientistOrganisation;
    }

    /**
     * *
     * Obsolete method to set the organisation.
     *
     * @return
     */
    public void setChiefScientistOrganisation(String chiefScientistOrganisation) {
        this.chiefScientistOrganisation = chiefScientistOrganisation;
    }

    /**
     * *
     * Return a list of singleton maps of String, String representing all chief
     * scientists and their institutions.
     *
     * @return
     */
    @XmlTransient
    public List<Person> getChiefScientists() {
        return chiefScientists;
    }

    /**
     * *
     * Set the list of singleton maps of String, String representing all chief
     * scientists and their institutions to the given argument.
     *
     * @return
     */
    public void setChiefScientists(List<Person> chiefScientists) {
        this.chiefScientist = stringifyScientistList();
        this.chiefScientists = chiefScientists;
    }

    @XmlTransient
    public String getNiceChiefScientistString() {
        StringBuilder sb = new StringBuilder();
        int ii = chiefScientists.size();
        if (ii == 0) {
            sb.append("none specified");
        } else {
            for (int i = 0; i < ii; i++) {
                Person p = chiefScientists.get(i);
                sb.append(p.name);
                if (i < ii - 1) {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "platformCode")
    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "platformClass")
    public String getPlatformClass() {
        return platformClass;
    }

    public void setPlatformClass(String platformClass) {
        this.platformClass = platformClass;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "objectives")
    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "collateCenter")
    public String getCollateCenter() {
        return collateCenter;
    }

    public void setCollateCenter(String collateCenter) {
        this.collateCenter = collateCenter;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "startingHarbor")
    public String getStartingHarbor() {
        return startingHarbor;
    }

    public void setStartingHarbor(String startingHarbor) {
        this.startingHarbor = startingHarbor;
    }

    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "arrivalHarbor")
    public String getArrivalHarbor() {
        return arrivalHarbor;
    }

    public void setArrivalHarbor(String arrivalHarbor) {
        this.arrivalHarbor = arrivalHarbor;
    }

    @XmlElementWrapper(namespace = "http://www.eurofleets.eu/", name = "seaAreas")
    @XmlElement(namespace = "http://www.eurofleets.eu/", name = "seaArea")
    public Set<SeaAreaBean> getSeaAreas() {
        return seaAreas;
    }

    public void setSeaAreas(Set<SeaAreaBean> ssa) {
        this.seaAreas = ssa;
    }

    public String getSeaAreasIds() {
        //Extract Sea Areas
        StringBuilder seaAreasIds = new StringBuilder("");
        Boolean isFirst = true;
        for (SeaAreaBean sa : this.getSeaAreas()) {
            if (!isFirst) {
                seaAreasIds.append(",");

            } else {
                isFirst = false;
            }
            seaAreasIds.append(sa.getCode());
        }
        return seaAreasIds.toString();
    }

    @Override
    public String toString() {
        return getCruiseName() + " (" + DAY_FORMAT.format(getdStartDate()) + "-" + DAY_FORMAT.format(getdEndDate()) + ")";
    }

    @Override
    public int compareTo(CruiseBean other) {
        return this.getRealId().compareTo(other.getRealId());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CruiseBean) {
            CruiseBean other = (CruiseBean) o;
            return this.getRealId().equals(other.getRealId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.getRealId());
        return hash;
    }

    public boolean canHaveAsName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public boolean canHaveAsStartDate(String startDate) {
        try {
            return canHaveAsStartDate(DateUtilities.parseDate(startDate));
        } catch (ParseException ex) {
            return false;
        }
    }

    public boolean canHaveAsEndDate(String endDate) {
        try {
            return canHaveAsEndDate(DateUtilities.parseDate(endDate));
        } catch (ParseException ex) {
            return false;
        }
    }

    public boolean canHaveAsStartDate(Date dStartDate) {
        if (dStartDate == null) {
            return false;
        } else {
            if (this.getdEndDate() != null && this.getdEndDate().before(dStartDate)) {
                return false;
            }
            return true;
        }
    }

    public boolean canHaveAsEndDate(Date dEndDate) {
        if (dEndDate == null) {
            return false;
        } else {
            if (this.getdStartDate() != null && this.getdStartDate().after(dEndDate)) {
                return false;
            }
            return true;
        }
    }

    public boolean isLegal() {
        return canHaveAsName(this.getCruiseName()) && canHaveAsStartDate(this.getdStartDate()) && canHaveAsEndDate(this.getdEndDate()) && /*getSeaAreas() != null && !getSeaAreas().isEmpty() &&*/ getStartingHarbor() != null && !getStartingHarbor().isEmpty() && getArrivalHarbor() != null && !getArrivalHarbor().isEmpty();
    }

    /**
     * *
     * Convert a list of scientists and their organisations represented in json
     * to a list of Scientist
     *
     * @param chiefScientist
     * @return
     */
    private List<Person> scientistStringToList(String chiefScientist) {
        if (chiefScientist == null) {
            return new ArrayList();
        }
        List<Person> r;
        if (JSONReader.isValidJSON(chiefScientist)) {
            try {
                chiefScientist = URLDecoder.decode(chiefScientist, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Exceptions.printStackTrace(ex);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<Person>>() {
            }.getType();

            r = new Gson().fromJson(chiefScientist, type);
            return r;
        } else {
            r = new ArrayList();
            r.add(new Person(chiefScientist, null, null, null));
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
        String json = new Gson().toJson(getChiefScientists());
        return json;
    }

    public void assignRealId() {
        this.setRealId(DateUtilities.formatDate(getdStartDate()) + "_" + cruiseName);
    }

    public static ICruise getCruiseByDate(Collection<? extends ICruise> cruises, OffsetDateTime timeStamp) {
        for (ICruise cruise : cruises) {

            OffsetDateTime startDate = OffsetDateTime.parse(cruise.getStartDate());
            OffsetDateTime endDate = OffsetDateTime.parse(cruise.getEndDate());
            if ((timeStamp.equals(startDate) || timeStamp.equals(endDate) || (timeStamp.isAfter(startDate) && timeStamp.isBefore(endDate)))) {
                return cruise;
            }
        }
        return null;
    }
}
