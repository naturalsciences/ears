/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Thomas Vandenberghe
 */
public interface ICruise extends EARSConcept {

    public String getStartDate();

    public void setStartDate(String startDate) throws ParseException;

    public String getEndDate();

    public void setEndDate(String endDate) throws ParseException;

    public Date getdStartDate();

    public void setdStartDate(Date dStartDate) throws ParseException;

    public Date getdEndDate();

    public void setdEndDate(Date dEndDate) throws ParseException;

    public String getChiefScientist();

    public void setChiefScientist(String chiefScientist);

    public String getChiefScientistOrganisation();

    public void setChiefScientistOrganisation(String chiefScientistOrganisation);

    public List<Person> getChiefScientists();

    public void setChiefScientists(List<Person> chiefScientists);

    public String getPlatformCode();

    public void setPlatformCode(String platformCode);

    public String getPlatformClass();

    public void setPlatformClass(String platformClass);

    public String getObjectives();

    public void setObjectives(String objectives);

    public String getCollateCenter();

    public void setCollateCenter(String collateCenter);

    public String getStartingHarbor();

    public void setStartingHarbor(String startingHarbor);

    public String getArrivalHarbor();

    public void setArrivalHarbor(String arrivalHarbor);

    public Set<SeaAreaBean> getSeaAreas();

    public void setSeaAreas(Set<SeaAreaBean> ssa);
}
