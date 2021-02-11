/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.base;

import be.naturalsciences.bmdc.ears.entities.CountryBean;
import be.naturalsciences.bmdc.ears.entities.EARSConcept;
import be.naturalsciences.bmdc.ears.entities.HarbourBean;
import be.naturalsciences.bmdc.ears.entities.ICountry;
import be.naturalsciences.bmdc.ears.entities.IHarbour;
import be.naturalsciences.bmdc.ears.entities.IOrganisation;
import be.naturalsciences.bmdc.ears.entities.IPlatformClass;
import be.naturalsciences.bmdc.ears.entities.IProject;
import be.naturalsciences.bmdc.ears.entities.ISeaArea;
import be.naturalsciences.bmdc.ears.entities.IVessel;
import be.naturalsciences.bmdc.ears.entities.OrganisationBean;
import be.naturalsciences.bmdc.ears.entities.ProjectBean;
import be.naturalsciences.bmdc.ears.entities.SeaAreaBean;
import be.naturalsciences.bmdc.ears.entities.VesselBean;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author Thomas Vandenberghe
 */
public class StaticMetadataSearcher implements LookupListener {

    private static final StaticMetadataSearcher INSTANCE = new StaticMetadataSearcher();

    private static Map<String, Set<? extends EARSConcept>> results = new THashMap<>();

    private Lookup.Result<HarbourBean> harbourResult;
    private Lookup.Result<OrganisationBean> organisationResult;
    private Lookup.Result<ProjectBean> projectResult;

    private static final Set<Class> recalculateList = new THashSet<>();

    public static StaticMetadataSearcher getInstance() {
        return INSTANCE;
    }

    private StaticMetadataSearcher() {

        harbourResult = Utilities.actionsGlobalContext().lookupResult(HarbourBean.class);
        harbourResult.addLookupListener(this);

        organisationResult = Utilities.actionsGlobalContext().lookupResult(OrganisationBean.class);
        organisationResult.addLookupListener(this);

        projectResult = Utilities.actionsGlobalContext().lookupResult(ProjectBean.class);
        projectResult.addLookupListener(this);
    }

    /*static{
        results = new THashMap<>();
    }*/
    private static Set<? extends ISeaArea> seaAreasS;

    private static Set<? extends IProject> projectsS;

    private static Set<? extends IHarbour> harboursS;

    private static Set<? extends IVessel> vesselsS;

    private static Set<? extends ICountry> countriesS;

    private static Set<? extends IPlatformClass> platformClassesS;

    private static Set<? extends IOrganisation> organisationsS;

    private static Set<? extends ISeaArea> seaAreasU;

    private static Set<? extends IProject> projectsU;

    private static Set<? extends IHarbour> harboursU;

    private static Set<? extends IVessel> vesselsU;

    private static Set<? extends ICountry> countriesU;

    private static Set<? extends IPlatformClass> platformClassesU;

    private static Set<? extends IOrganisation> organisationsU;

    private static <C extends EARSConcept> Collection<? extends EARSConcept> getConcepts(Class<C> cls, boolean sorted) {

        String sort = (sorted) ? "S" : "U";
        String key = cls.getName().concat(sort);
        Set<? extends EARSConcept> concepts = results.get(key);

        if (recalculateList.contains(cls) || concepts == null || concepts.size() == 0) { //if they were not yet added to this class' class reference
            recalculateList.remove(cls);
            Collection<? extends C> lookupAll = Utilities.actionsGlobalContext().lookupAll(cls);
            if (lookupAll.size() > 0) {//If another component of EARS did add the metadata for this class to the lookup.
                if (!sorted) {
                    concepts = new THashSet<>(lookupAll);
                } else {
                    concepts = new TreeSet<>(lookupAll);
                }
                results.put(key, concepts);
            } else {
                StaticMetadataManager<C> organisationMetadataManager = new StaticMetadataManager<>(cls);

                try {
                    if (!sorted) {
                        concepts = new THashSet<>(organisationMetadataManager.readMetadataFromFile());
                    } else {
                        concepts = new TreeSet<>(organisationMetadataManager.readMetadataFromFile());
                    }

                } catch (FileNotFoundException ex) {
                    Messaging.report("The static metadata of type '" + cls.getSimpleName() + "' can't be found.", ex, StaticMetadataSearcher.class, true);
                    return new THashSet<>();
                }
            }
        }
        return concepts;
    }

    
    public Collection<SeaAreaBean> getSeaAreas(boolean sorted) {
        Collection<SeaAreaBean> seas = (Collection<SeaAreaBean>) getConcepts(SeaAreaBean.class, sorted);
        seas.removeIf(s -> s.getCode().contains("C16"));
        return seas;
    }

    public Collection<ProjectBean> getProjects(boolean sorted) {
        return (Collection<ProjectBean>) getConcepts(ProjectBean.class, sorted);
    }

    public Collection<HarbourBean> getHarbours(boolean sorted) {
        return (Collection<HarbourBean>) getConcepts(HarbourBean.class, sorted);
    }

    public Collection<VesselBean> getVessels(boolean sorted) {
        return (Collection<VesselBean>) getConcepts(VesselBean.class, sorted);
    }

    public Collection<? extends ICountry> getCountries(boolean sorted) {
        return (Collection<CountryBean>) getConcepts(CountryBean.class, sorted);
    }

    // public static Collection<PlatformClassBean> getCategories(boolean sorted) {
    //     return (Collection<PlatformClassBean>) getConcepts(PlatformClassBean.class, sorted);
    /*if (!sorted) {
            if (platformClassesU == null) {
                platformClassesU = new THashSet(Utilities.actionsGlobalContext().lookupAll(PlatformClassBean.class
                ));
            }
            return platformClassesU;

        } else {
            if (platformClassesS == null) {
                platformClassesS = new TreeSet(Utilities.actionsGlobalContext().lookupAll(PlatformClassBean.class
                ));
            }
            return platformClassesS;
        }*/
    //}
    public Collection<OrganisationBean> getOrganisations(boolean sorted) {
        return (Collection<OrganisationBean>) getConcepts(OrganisationBean.class, sorted);
    }

    public SeaAreaBean getSeaArea(String sdnId) {
        List<SeaAreaBean> l = ((Stream<SeaAreaBean>) getConcepts(SeaAreaBean.class, false).stream()).filter(c -> c.getCode().equals(sdnId)).collect(Collectors.toList());
        if (!l.isEmpty()) {
            return l.get(0);
        } else {
            return null;
        }
    }

    public ProjectBean getProject(String sdnId) {
        List<ProjectBean> l = ((Stream<ProjectBean>) getConcepts(ProjectBean.class, false).stream()).filter(c -> c.getCode().equals(sdnId)).collect(Collectors.toList());
        if (!l.isEmpty()) {
            return l.get(0);
        } else {
            return null;
        }
    }

    public HarbourBean getHarbour(String sdnId) {
        List<HarbourBean> l = ((Stream<HarbourBean>) getConcepts(HarbourBean.class, false).stream()).filter(c -> c.getCode().equals(sdnId)).collect(Collectors.toList());
        if (!l.isEmpty()) {
            return l.get(0);
        } else {
            return null;
        }
    }

    public VesselBean getVessel(String sdnId) {
        List<VesselBean> l = ((Stream<VesselBean>) getConcepts(VesselBean.class, false).stream()).filter(c -> c.getCode().equals(sdnId)).collect(Collectors.toList());
        if (!l.isEmpty()) {
            return l.get(0);
        } else {
            return null;
        }
    }

    public CountryBean getCountry(String name) {
        List<CountryBean> l = ((Stream<CountryBean>) getConcepts(CountryBean.class, false).stream()).filter(c -> c.getName().equals(name)).collect(Collectors.toList());
        if (!l.isEmpty()) {
            return l.get(0);
        } else {
            return null;
        }
    }

    /* public static IPlatformClassBean getCategory(String sdnId) {
        List<IPlatformClass> l = ((Stream<IPlatformClass>) getConcepts(IPlatformClass.class, false).stream()).filter(c -> c.getCode().equals(sdnId)).collect(Collectors.toList());
        if (!l.isEmpty()) {
            return l.get(0);
        } else {
            return null;
        }
    }*/
    public OrganisationBean getOrganisation(String sdnId) {
        List<OrganisationBean> l = ((Stream<OrganisationBean>) getConcepts(OrganisationBean.class, false).stream()).filter(c -> c.getCode().equals(sdnId)).collect(Collectors.toList());
        if (!l.isEmpty()) {
            return l.get(0);
        } else {
            return null;
        }
    }

    public OrganisationBean getOrganisationByName(String name) {
        List<OrganisationBean> l = ((Stream<OrganisationBean>) getConcepts(OrganisationBean.class, false).stream()).filter(c -> c.getName().equals(name)).collect(Collectors.toList());
        if (!l.isEmpty()) {
            return l.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void resultChanged(LookupEvent le) {
        if (le.getSource().equals(harbourResult)) {
            recalculateList.add(HarbourBean.class);
        } else if (le.getSource().equals(organisationResult)) {
            recalculateList.add(OrganisationBean.class);
        } else if (le.getSource().equals(projectResult)) {
            recalculateList.add(ProjectBean.class);
        }
    }

}
