/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.application;

import be.naturalsciences.bmdc.ears.base.StaticMetadataManager;
import be.naturalsciences.bmdc.ears.base.StaticMetadataSearcher;
import be.naturalsciences.bmdc.ears.entities.Actor;
import be.naturalsciences.bmdc.ears.entities.CountryBean;
import be.naturalsciences.bmdc.ears.entities.CurrentURL;
import be.naturalsciences.bmdc.ears.entities.CurrentUser;
import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.entities.HarbourBean;
import be.naturalsciences.bmdc.ears.entities.IVessel;
import be.naturalsciences.bmdc.ears.entities.OrganisationBean;
import be.naturalsciences.bmdc.ears.entities.ProjectBean;
import be.naturalsciences.bmdc.ears.entities.SeaAreaBean;
import be.naturalsciences.bmdc.ears.entities.User;
import be.naturalsciences.bmdc.ears.entities.VesselBean;
import be.naturalsciences.bmdc.ears.netbeans.services.GlobalActionContextProxy;
import be.naturalsciences.bmdc.ears.netbeans.services.SingletonResult;
import be.naturalsciences.bmdc.ears.ontology.OntologySynchronizer;
import be.naturalsciences.bmdc.ears.properties.Configs;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ears.utils.WebserviceUtils;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Collection;
import java.util.Set;
import org.openide.modules.OnStart;
import org.openide.modules.OnStop;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author Yvan Stojanov
 */
@OnStart
public class Startable implements Runnable, LookupListener {

    Graphics2D splashGraphics;
    Rectangle2D.Double splashTextArea;
    SplashScreen mySplash;

    public static StaticMetadataSearcher searcher;

    private CurrentVessel currentVessel;
    private URL currentUrl;

    private SingletonResult<CurrentVessel, IVessel> currentVesselResult;
    private SingletonResult<CurrentURL, URL> currentUrlResult;

    private static StaticMetadataManager<VesselBean> vesselMetadataManager;
    private static StaticMetadataManager<SeaAreaBean> seaAreaMetadataManager;
    private static StaticMetadataManager<HarbourBean> harbourMetadataManager;
    private static StaticMetadataManager<OrganisationBean> organisationMetadataManager;
    private static StaticMetadataManager<ProjectBean> projectMetadataManager;
    private static StaticMetadataManager<CountryBean> countryMetadataManager;

    public static StaticMetadataManager<VesselBean> getVesselMetadataManager() {
        return vesselMetadataManager;
    }

    public static StaticMetadataManager<SeaAreaBean> getSeaAreaMetadataManager() {
        return seaAreaMetadataManager;
    }

    public static StaticMetadataManager<HarbourBean> getHarbourMetadataManager() {
        return harbourMetadataManager;
    }

    public static StaticMetadataManager<OrganisationBean> getOrganisationMetadataManager() {
        return organisationMetadataManager;
    }

    public static StaticMetadataManager<ProjectBean> getProjectMetadataManager() {
        return projectMetadataManager;
    }

    public StaticMetadataManager<CountryBean> getCountryMetadataManager() {
        return countryMetadataManager;
    }

    public void splashText(String str) {
        /* splashGraphics.clearRect(0, 0, (int) splashTextArea.getWidth(), (int) splashTextArea.getHeight());

        if (mySplash != null && mySplash.isVisible()) {   // important to check here so no other methods need to know if there
            splashGraphics.fill(splashTextArea);
            splashGraphics.drawString(str, (int) splashTextArea.getX(), (int) splashTextArea.getY());
          //  mySplash.update();
        }*/
    }

    @Override
    public void run() {

        /*mySplash = SplashScreen.getSplashScreen();

        if (mySplash != null) {   // if there are any problems displaying the splash this will be null
            splashGraphics = mySplash.createGraphics();
            Dimension ssDim = mySplash.getSize();
            int height = ssDim.height;
            int width = ssDim.width;
            splashTextArea = new Rectangle2D.Double(0, 110, width, 20);
            splashGraphics.setFont(new Font("Dialog", Font.PLAIN, 20));
        }*/
        StaticMetadataSearcher.getInstance();
        try {
            URL configUrl = Configs.getRestURL();
            if (configUrl != null) {
                CurrentURL currentUrl2 = CurrentURL.getInstance(configUrl);
                GlobalActionContextProxy.getInstance().add(currentUrl2);
                currentUrl = configUrl;

                if (!WebserviceUtils.testWS("ears2/getCruise")) {
                    Messaging.report("Note that the ears2 webservices are offline. This application won't function properly.", Message.State.BAD, this.getClass(), true);
                }
                if (!WebserviceUtils.testWS("ears2Nav/getLastNavXml")) {
                    Messaging.report("Note that the ears2Nav webservices are offline. This doesn't impact the application.", Message.State.BAD, this.getClass(), true);
                }
                if (!WebserviceUtils.testWS("ears2Ont/authenticate")) { //works even if there is no vessel ontology stored
                    Messaging.report("Note that the ears2Ont webservices are offline. You will not be able to edit any tree and will be using possibly outdated ones.", Message.State.BAD, this.getClass(), true);
                }
            } else {
                Messaging.report("The web service URL is not set in the settings.", Message.State.BAD, Startable.class, true);
            }
        } catch (MalformedURLException e) {
            Messaging.report("The web service URL given in the settings is invalid" + e.getMessage(), e, Startable.class, true);
        }
        currentUrlResult = new SingletonResult<>(CurrentURL.class, this);
        currentVesselResult = new SingletonResult<>(CurrentVessel.class, this);
        User currentUser = Configs.getCurrentUser();
        if (currentUser != null) {
            CurrentUser currentUser2 = CurrentUser.getInstance(currentUser);
            GlobalActionContextProxy.getInstance().add(currentUser2);
        }
        Set<Actor> actors = null;
        try {
            actors = Configs.getAllActors();
        } catch (IOException ex) {
            Messaging.report("The actors file could not be found or read", ex, Startable.class, true);
        }
        if (actors != null) {
            GlobalActionContextProxy.getInstance().addAll(actors);
        }

        initialiseVessel();

        Thread thr1 = new Thread() {
            public void run() {
                try {
                    OntologySynchronizer.synchronizeLatestOntologyAxiom();
                } catch (IOException ex) {
                    Messaging.report("The latest ontology axiom file could not be synchronized.", ex, Startable.class, true);
                }
                OntologySynchronizer.synchronizeBaseOntology(false);
                //OntologyFileBrowser.open(OntologyFileBrowser.getBaseNode()); //ontology object and its individuals are now in global lookup
            }
        };
        thr1.start();

        Thread thr2 = new Thread() {
            public void run() {
                OntologySynchronizer.synchronizeVesselOntology(false);
                //  OntologyFileBrowser.open(OntologyFileBrowser.getVesselNode()); //ontology object and its individuals are now in global lookup
            }
        };
        thr2.start();

        Thread thr3 = new Thread() {
            public void run() {
                if (currentVessel != null) {
                    OntologySynchronizer.synchronizeAllProgramOntologies(currentVessel.getConcept(), false);
                }
            }
        };
        thr3.start();
        vesselMetadataManager = new StaticMetadataManager<>(VesselBean.class);
//                new StaticMetadataManager<>(VesselBean.class);
        seaAreaMetadataManager = new StaticMetadataManager<>(SeaAreaBean.class);
        harbourMetadataManager = new StaticMetadataManager<>(HarbourBean.class);
        organisationMetadataManager = new StaticMetadataManager<>(OrganisationBean.class);
        projectMetadataManager = new StaticMetadataManager<>(ProjectBean.class);
        countryMetadataManager = new StaticMetadataManager<>(CountryBean.class);

        retrieveMetadataFiles();

    }

    private void retrieveMetadataFiles() {
        Thread thr4 = new Thread() {
            public void run() {
                try {
                    Collection<CountryBean> countries = countryMetadataManager.refreshMetadataFile(false);
                    if (countries.size() > 0) { //if the file was actually refreshed
                        splashText("Country metadata updated...");
                        Messaging.report("Country metadata has been updated", Message.State.INFO, Startable.class, false);
                        GlobalActionContextProxy.getInstance().addAll(countries);
                    }
                } catch (EarsException ex) {
                    Messaging.report(ex.getMessage(), ex, this.getClass(), true);
                }
            }
        };
        thr4.start();
        Thread thr5 = new Thread() {
            public void run() {
                try {
                    Collection<VesselBean> vessels = vesselMetadataManager.refreshMetadataFile(false);
                    if (vessels.size() > 0) { //if the file was actually refreshed
                        splashText("Vessel metadata updated...");
                        Messaging.report("Vessel metadata has been updated", Message.State.INFO, Startable.class, false);
                        GlobalActionContextProxy.getInstance().addAll(vessels);
                    }
                } catch (EarsException ex) {
                    Messaging.report(ex.getMessage(), ex, this.getClass(), true);
                }
            }
        };
        thr5.start();
        Thread thr6 = new Thread() {
            public void run() {
                try {
                    Collection<SeaAreaBean> seaAreas = seaAreaMetadataManager.refreshMetadataFile(false);
                    if (seaAreas.size() > 0) {
                        splashText("Sea area metadata updated...");
                        Messaging.report("Sea area metadata has been updated", Message.State.INFO, Startable.class, false);
                        GlobalActionContextProxy.getInstance().addAll(seaAreas);
                    }
                } catch (EarsException ex) {
                    Messaging.report(ex.getMessage(), ex, this.getClass(), true);
                }
            }
        };
        thr6.start();
        Thread thr7 = new Thread() {
            public void run() {
                try {
                    Collection<HarbourBean> harbours = harbourMetadataManager.refreshMetadataFile(false);
                    if (harbours.size() > 0) {
                        splashText("Harbour metadata updated...");
                        Messaging.report("Harbour metadata has been updated", Message.State.INFO, Startable.class, false);
                        GlobalActionContextProxy.getInstance().addAll(harbours);
                    }
                } catch (EarsException ex) {
                    Messaging.report(ex.getMessage(), ex, this.getClass(), true);
                }
            }
        };
        thr7.start();
        Thread thr8 = new Thread() {
            public void run() {
                try {
                    Collection<OrganisationBean> organisations = organisationMetadataManager.refreshMetadataFile(false);
                    if (organisations.size() > 0) {
                        splashText("Organisation metadata updated...");
                        Messaging.report("Organisation metadata has been updated", Message.State.INFO, Startable.class, false);
                        GlobalActionContextProxy.getInstance().addAll(organisations);
                    }
                } catch (EarsException ex) {
                    Messaging.report(ex.getMessage(), ex, this.getClass(), true);
                }
            }
        };
        thr8.start();
        Thread thr9 = new Thread() {
            public void run() {
                try {
                    Collection<ProjectBean> projects = projectMetadataManager.refreshMetadataFile(false);
                    if (projects.size() > 0) {
                        splashText("Project metadata updated...");
                        GlobalActionContextProxy.getInstance().addAll(projects);
                    }
                } catch (EarsException ex) {
                    Messaging.report(ex.getMessage(), ex, this.getClass(), true);
                }
            }
        };
        thr9.start();
    }

    private void initialiseVessel(/*boolean atStartUp*/) {

        try {
            currentVessel = Configs.getCurrentVessel();

        } catch (FileNotFoundException ex) {
            Message msg = new Message("The file config.properties could not be found or read. Please check.", ex, this.getClass(), true, Message.State.EXCEPTION);
            Messaging.report(msg);
        } catch (ParseException ex) {
            Message msg = new Message("The file config.properties could not parsed. Exception massage:" + ex.getMessage(), ex, this.getClass(), true, Message.State.EXCEPTION);
            Messaging.report(msg);;
        } catch (NullPointerException ex) {
            Message msg = new Message("There was a problem with the config.properties file. Exception massage:" + ex.getMessage(), ex, this.getClass(), true, Message.State.EXCEPTION);
            Messaging.report(msg);
        }

        if (currentVessel != null) {
            GlobalActionContextProxy.getInstance().add(currentVessel);
            Message msg = new Message("Vessel: " + currentVessel.getConcept().getVesselName(), null, this.getClass(), false, Message.State.INFO);
            Messaging.report(msg);
        } else {
            Message msg = new Message("Current vessel couldn't be retrieved from config file.", null, this.getClass(), true, Message.State.BAD);
            Messaging.report(msg);
        }
    }

    @Override
    public void resultChanged(LookupEvent le) {
        if (currentUrlResult.matches(le) && (currentUrlResult.getCurrent() == null ? null : currentUrlResult.getCurrent().getConcept()) != currentUrl) {
            currentUrl = currentUrlResult.getCurrent() != null ? currentUrlResult.getCurrent().getConcept() : null;
            OntologySynchronizer.synchronizeVesselOntology(false);
            OntologySynchronizer.synchronizeAllProgramOntologies(currentVessel.getConcept(), false);
        }
        if (currentVesselResult.matches(le)) {
            currentVessel = currentVesselResult.getCurrent();
        }
    }

    @OnStop
    public static final class Down implements Runnable {

        @Override
        public void run() {
        }
    }
}
