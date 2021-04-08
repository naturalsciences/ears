/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.toolbar;

import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.CurrentCruise;
import be.naturalsciences.bmdc.ears.entities.CurrentProgram;
import be.naturalsciences.bmdc.ears.entities.CurrentURL;
import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.entities.EventBean;
import be.naturalsciences.bmdc.ears.entities.ICruise;
import be.naturalsciences.bmdc.ears.entities.IProgram;
import be.naturalsciences.bmdc.ears.entities.IVessel;
import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.netbeans.services.GlobalActionContextProxy;
import be.naturalsciences.bmdc.ears.netbeans.services.SingletonResult;
import be.naturalsciences.bmdc.ears.ontology.browser.OntologyFileBrowserTopComponent;
import be.naturalsciences.bmdc.ears.ontology.conceptlist.ConceptListTopComponent;
import be.naturalsciences.bmdc.ears.rest.RestClientCruise;
import be.naturalsciences.bmdc.ears.rest.RestClientEvent;
import be.naturalsciences.bmdc.ears.rest.RestClientProgram;
import be.naturalsciences.bmdc.ears.topcomponents.CreateCruiseTopComponent;
import be.naturalsciences.bmdc.ears.topcomponents.CreateEventTopComponent;
import be.naturalsciences.bmdc.ears.topcomponents.CreateProgramTopComponent;
import be.naturalsciences.bmdc.ears.topcomponents.UpdateCruiseTopComponent;
import be.naturalsciences.bmdc.ears.topcomponents.UpdateProgramTopComponent;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ears.utils.SwingUtils;
import be.naturalsciences.bmdc.ears.utils.WebserviceUtils;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.ConnectException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;
import org.openide.windows.Mode;
import org.openide.windows.WindowManager;

@ActionID(
        category = "View",
        id = "be.naturalsciences.bmdc.ears.toolbar.GeneralToolBarAction"
)
@ActionRegistration(
        displayName = "#CTL_GeneralToolBarAction",
        lazy = false
)
@ActionReference(path = "Toolbars/Operations")
@Messages("CTL_GeneralToolBarAction=GeneralToolBarAction")
public final class GeneralToolBarAction extends AbstractAction implements Presenter.Toolbar, LookupListener {

    JComboBox<ProgramBean> programComboBox;
    JComboBox<String> cruiseComboBox;

    RestClientCruise cruiseClient;
    RestClientProgram programClient;

    RestClientEvent eventClient;

    private SingletonResult<CurrentVessel, IVessel> currentVesselResult;
    private SingletonResult<CurrentCruise, ICruise> currentCruiseResult;
    private SingletonResult<CurrentProgram, IProgram> currentProgramResult;

    private SingletonResult<CurrentURL, URL> currentUrlResult;

    URL currentUrl = null;

    CurrentVessel currentVessel;
    CurrentCruise currentCruise;
    CurrentProgram currentProgram;

    Set<CruiseBean> cruises = new TreeSet<>();
    Collection<? extends IProgram> possibleCurrentPrograms;

    Collection<EventBean> events;

    public static String NO_CRUISE_SELECTED = "Choose a Cruise";

    ItemListener cruiseIl = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent ie) {

            if (ie.getStateChange() == ItemEvent.SELECTED) {
                JComboBox<ItemEvent> cb = (JComboBox<ItemEvent>) ie.getSource();

                Object o = cb.getSelectedItem();
                if (o != null && o instanceof CruiseBean) {
                    CruiseBean cr = (CruiseBean) o;
                    Messaging.report("Current cruise: " + cr.toString(), Message.State.INFO, this.getClass(), false);
                    GlobalActionContextProxy.getInstance().add(CurrentCruise.getInstance(cr));
                } else if (o.equals(NO_CRUISE_SELECTED)) { //we have picked the "Choose the cruise" element
                    Messaging.report("Current cruise: none", Message.State.INFO, this.getClass(), false);
                    GlobalActionContextProxy.getInstance().add(CurrentCruise.getInstance(null));
                }
            }
        }
    };

    ItemListener programIl = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent ie) {

            if (ie.getStateChange() == ItemEvent.SELECTED) {
                JComboBox<ItemEvent> cb = (JComboBox<ItemEvent>) ie.getSource();
                Object o = cb.getSelectedItem();
                if (o != null && o instanceof ProgramBean) {
                    ProgramBean pr = (ProgramBean) o;
                    Messaging.report("Current program: " + pr.toString(), Message.State.INFO, this.getClass(), false);
                    GlobalActionContextProxy.getInstance().add(CurrentProgram.getInstance(pr));
                }
            }
        }
    };

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public GeneralToolBarAction() {
        currentUrlResult = new SingletonResult<>(CurrentURL.class, this);
        currentVesselResult = new SingletonResult<>(CurrentVessel.class, this);
        currentCruiseResult = new SingletonResult<>(CurrentCruise.class, this);
        currentProgramResult = new SingletonResult<>(CurrentProgram.class, this);
        currentVessel = getCurrentVessel();
        currentCruise = getCurrentCruise();
        currentProgram = getCurrentProgram();
        //initialiseStaticMetadata(true);

        if (WebserviceUtils.testWS("ears3/alive")) {
            try {
                cruiseClient = new RestClientCruise();
                programClient = new RestClientProgram();
                eventClient = new RestClientEvent();
            } catch (ConnectException ex) {
                //can't happen
                Messaging.report("The webservices are offline. The tool won't function properly.", Message.State.BAD, this.getClass(), true);
            } catch (EarsException ex) {
                Messaging.report(ex.getMessage(), ex, this.getClass(), true);
            }
            if (programClient != null && cruiseClient != null && currentVessel != null) {
                populateCruiseList();

                currentCruise = CurrentCruise.getInstance(CruiseBean.getCruiseByDate(cruises, OffsetDateTime.now()));
                if (currentCruise != null && currentCruise.getConcept() == null) {
                    if (cruises.size() > 0) {
                        currentCruise = CurrentCruise.getInstance((CruiseBean) cruises.toArray()[0]);
                    }
                }
                if (currentCruise != null && currentCruise.getConcept() != null) {
                    GlobalActionContextProxy.getInstance().add(currentCruise);
                    Messaging.report("Current cruise: " + currentCruise.getConcept().toString(), Message.State.INFO, this.getClass(), false);
                } else {
                    Messaging.report("There is no actual cruise ongoing.", Message.State.BAD, this.getClass(), true);
                }

                populatePossibleCurrentProgramList();
                populateProgramCombobox();
                /* if (currentProgram == null || currentProgram.getConcept() == null) {
                    if (possibleCurrentPrograms.size() > 0) {
                        currentProgram = CurrentProgram.getInstance((ProgramBean) possibleCurrentPrograms.toArray()[0]);
                    }
                }
                if (currentProgram != null && currentProgram.getConcept() != null) {
                    GlobalActionContextProxy.getInstance().add(currentProgram);
                } else {
                    Messaging.report("There is no current program selected.", Message.State.BAD, this.getClass(), true);
                }*/
            } else {
                cruises = new TreeSet<>();
                possibleCurrentPrograms = new TreeSet<>();
            }

        } else {
            cruises = new TreeSet<>();
            possibleCurrentPrograms = new TreeSet<>();
        }
    }

    private void populateCruiseList() {
        if (cruiseClient != null && currentVessel != null && WebserviceUtils.testWS("ears3/alive")) {
            try {
                //  if (cruises == null) {
                cruises = new TreeSet<>(cruiseClient.getCruiseByPlatform(currentVessel.getConcept()));
                //  } else {
                //      cruises.addAll(cruiseClient.getCruiseByPlatform(currentVessel.getConcept()));
                //  }
            } catch (ConnectException ex) {
                Messaging.report("The webservices are offline. The list of cruises can't be updated.", ex, this.getClass(), true);
            }
        } else {
            cruises = new TreeSet<>();
        }
    }

    private void populatePossibleCurrentProgramList() {
        if (programClient != null && WebserviceUtils.testWS("ears3/alive")) {
            currentCruise = currentCruiseResult.getCurrent();
            if (currentCruise != null && currentCruise.getConcept() != null) {
                try {
                    possibleCurrentPrograms = programClient.getProgramByCruise(currentCruise.getConcept()); //get the programs of the current cruise
                } catch (ConnectException ex) {
                    Messaging.report("The webservices are offline. The list of programs can't be updated.", ex, this.getClass(), true);
                }
            } else {
                try {
                //    if (!cruises.isEmpty()) {
                //        List<CruiseBean> list = new ArrayList(cruises);
                //        possibleCurrentPrograms = programClient.getProgramByCruise(list.get(0)); //get the programs of the first cruise in the list
                //    } else {
                        possibleCurrentPrograms = programClient.getAllPrograms();
                //    }
                } catch (ConnectException ex) {
                    Messaging.report("The webservices are offline. The list of programs can't be updated.", ex, this.getClass(), true);
                }
            }
        } else {
            possibleCurrentPrograms = new TreeSet<>();
        }
    }

    @Override
    public void resultChanged(LookupEvent ev) { //part after && because of weird behaviour where currentUrlResult does match even when the cruise changes
        if ((currentVesselResult.matches(ev) || currentUrlResult.matches(ev)) || (currentUrlResult.getCurrent() == null ? null : currentUrlResult.getCurrent().getConcept()) != currentUrl) {
            if (currentUrlResult.getCurrent() == null) {
                currentUrl = null;
            } else {
                currentUrl = currentUrlResult.getCurrent().getConcept();
            }
            currentVessel = currentVesselResult.getCurrent();
            populateCruiseList();
            populatePossibleCurrentProgramList();
            GlobalActionContextProxy.getInstance().removeAll(IProgram.class);
            GlobalActionContextProxy.getInstance().addAll(possibleCurrentPrograms);
            populateComboboxes();
        }

        if (currentCruiseResult.matches(ev)) {
            currentCruise = currentCruiseResult.getCurrent();
            populatePossibleCurrentProgramList();
            GlobalActionContextProxy.getInstance().removeAll(IProgram.class);
            GlobalActionContextProxy.getInstance().addAll(possibleCurrentPrograms);
            populateProgramCombobox();
        }
    }

    private CurrentCruise getCurrentCruise() {
        return currentCruiseResult.getCurrent();
    }

    private CurrentVessel getCurrentVessel() {
        return currentVesselResult.getCurrent();
    }

    private CurrentProgram getCurrentProgram() {
        return currentProgramResult.getCurrent();
    }

    private void populateComboboxes() {
        populateCruiseCombobox();
        populateProgramCombobox();
    }

    /**
     * *
     * Instantiate the cruisecombobox if needed and populate or repopulate it.
     */
    private void populateCruiseCombobox() {

        if (cruiseComboBox == null) {
            cruiseComboBox = new JComboBox<>();
        }
        cruiseComboBox.removeItemListener(cruiseIl);
        if (cruises != null) {
            if (cruiseComboBox.getItemCount() > 0) {
                cruiseComboBox.removeAllItems();
            }
            cruiseComboBox.addItem(NO_CRUISE_SELECTED);
            if (cruises.size() > 0) {
                for (ICruise cruise : cruises) {
                    SwingUtils.addToComboBox(cruiseComboBox, cruise);
                }
            } else {
                cruiseComboBox.addItem("No cruises for selected vessel");
            }
        }
        if (currentCruise != null && currentCruise.getConcept() != null) {
            cruiseComboBox.setSelectedItem(currentCruise.getConcept());

        }
        cruiseComboBox.addItemListener(cruiseIl);

    }

    /**
     * *
     * Instantiate the programcombobox if needed and populate or repopulate it.
     */
    private void populateProgramCombobox() {

        if (programComboBox == null) {
            programComboBox = new JComboBox<>();
        }
        programComboBox.removeItemListener(programIl);
        if (possibleCurrentPrograms != null) {
            if (programComboBox.getItemCount() > 0) {
                programComboBox.removeAllItems();
            }
            if (possibleCurrentPrograms.size() > 0 /*&& cruiseComboBox.getSelectedIndex() != 0*/) {

                for (IProgram program : possibleCurrentPrograms) {
                    SwingUtils.addToComboBox(programComboBox, program);
                }
                if (/*currentCruise != null && currentCruise.getConcept() != null && */(currentProgram == null || currentProgram.getConcept() == null)) {
                    ProgramBean currentProgramBean = programComboBox.getItemAt(0);
                    programComboBox.setSelectedItem(currentProgramBean);
                    currentProgram = CurrentProgram.getInstance(currentProgramBean);
                    GlobalActionContextProxy.getInstance().add(CurrentProgram.getInstance(currentProgramBean));

                    Messaging.report("Current program: " + currentProgram.getConcept().toString(), Message.State.INFO, this.getClass(), false);
                }
            } else {
                currentProgram = CurrentProgram.getInstance(null);
            }
        }
        if (currentProgram != null && currentProgram.getConcept() != null) {
            if (SwingUtils.modelContains(programComboBox.getModel(), currentProgram.getConcept())) {
                programComboBox.setSelectedItem(currentProgram.getConcept());
            }
        }
        programComboBox.addItemListener(programIl);

    }

    @Override
    public Component getToolbarPresenter() {
        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton createCruise = new JButton(NbBundle.getMessage(GeneralToolBarAction.class,
                "CREATE_CRUISE"));
        createCruise.addActionListener(new CreateNewCruiseActionListener());

        JButton createProgram = new JButton(NbBundle.getMessage(GeneralToolBarAction.class, "CREATE_PROGRAM"));

        createProgram.addActionListener(new CreateNewProgramActionListener());

        JButton editCruise = new JButton(NbBundle.getMessage(GeneralToolBarAction.class, "UPDATE_CRUISE"));

        editCruise.addActionListener(new EditCruiseActionListener());

        JButton editProgram = new JButton(NbBundle.getMessage(GeneralToolBarAction.class, "UPDATE_PROGRAM"));

        editProgram.addActionListener(new EditProgramActionListener());

        JButton createEvent = new JButton(NbBundle.getMessage(GeneralToolBarAction.class, "CREATE_EVENT"));

        createEvent.addActionListener(new CreateEventActionListener());

        /*     JButton manageTrees = new JButton(NbBundle.getMessage(GeneralToolBarAction.class, "MANAGE_TREES"));
        manageTrees.addActionListener(new ManageTreesActionListener());

        JButton browseTerms = new JButton(NbBundle.getMessage(GeneralToolBarAction.class, "BROWSE_TERMS"));
        browseTerms.addActionListener(new BrowseTermsActionListener());*/
        if (cruiseComboBox == null) {
            cruiseComboBox = new JComboBox<>();
        }

        if (programComboBox == null) {
            programComboBox = new JComboBox<>();

        }

        jPanel.add(createCruise);
        jPanel.add(createProgram);
        jPanel.add(editCruise);
        jPanel.add(editProgram);
        jPanel.add(createEvent);
        // jPanel.add(manageTrees);
        // jPanel.add(browseTerms);

        jPanel.add(
                new JLabel("cruise:"));
        jPanel.add(cruiseComboBox);

        jPanel.add(
                new JLabel("program:"));
        jPanel.add(programComboBox);
        return jPanel;
    }

    private class EditProgramActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (UpdateProgramTopComponent.getInstance() instanceof UpdateProgramTopComponent) {
                UpdateProgramTopComponent currentCruiseTopComponent = UpdateProgramTopComponent.getInstance();
                currentCruiseTopComponent.close();
            }
            Mode toProperties = WindowManager.getDefault().findMode("properties");
            UpdateProgramTopComponent uptc = new UpdateProgramTopComponent();
            toProperties.dockInto(uptc);
            uptc.open();
            uptc.requestActive();

        }
    }

    private class CreateEventActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            CreateEventTopComponent cnptc = new CreateEventTopComponent();
            cnptc.open();
            cnptc.requestActive();
        }
    }

    private class EditCruiseActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (UpdateCruiseTopComponent.getInstance() instanceof UpdateCruiseTopComponent) {//ys01
                UpdateCruiseTopComponent currentCruiseTopComponent = UpdateCruiseTopComponent.getInstance();//ys01
                currentCruiseTopComponent.close();//ys01
            }
            Mode toProperties = WindowManager.getDefault().findMode("properties");
            UpdateCruiseTopComponent uctc = new UpdateCruiseTopComponent();
            toProperties.dockInto(uctc);
            uctc.open();
            uctc.requestActive();
        }
    }

    private class CreateNewCruiseActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            CreateCruiseTopComponent cnc = new CreateCruiseTopComponent();
            cnc.open();
            cnc.requestActive();
        }
    }

    private class CreateNewProgramActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            CreateProgramTopComponent cps = new CreateProgramTopComponent();
            cps.open();
            cps.requestActive();
        }
    }

    private class ManageTreesActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            OntologyFileBrowserTopComponent ofb = new OntologyFileBrowserTopComponent(); // mode = "explorer"            

            ofb.open();
            ofb.requestActive();
        }
    }

    private class BrowseTermsActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ConceptListTopComponent clt = new ConceptListTopComponent();

            // mode = "explorer"
            clt.open();
            clt.requestActive();
        }
    }

}
