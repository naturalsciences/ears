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
import be.naturalsciences.bmdc.ears.rest.RestClientCruise;
import be.naturalsciences.bmdc.ears.rest.RestClientEvent;
import be.naturalsciences.bmdc.ears.rest.RestClientProgram;
import be.naturalsciences.bmdc.ears.topcomponents.CreateCruiseSetupTopComponent;
import be.naturalsciences.bmdc.ears.topcomponents.CreateEventTopComponent;
import be.naturalsciences.bmdc.ears.topcomponents.CreateProgramSetupTopComponent;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    JComboBox programComboBox;
    JComboBox cruiseComboBox;

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

    Set<? extends ICruise> cruises;
    Collection<? extends IProgram> currentPrograms;

    Collection<EventBean> events;

    ItemListener cruiseIl = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent ie) {
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                JComboBox cb = (JComboBox) ie.getSource();
                Object o2 = cb.getSelectedItem();
                if (o2 != null && o2 instanceof CruiseBean) {
                    CruiseBean cr = (CruiseBean) o2;
                    Messaging.report("Current cruise: " + cr.toString(), Message.State.INFO, this.getClass(), false);
                    GlobalActionContextProxy.getInstance().add(CurrentCruise.getInstance(cr));
                }
            }
        }
    };

    ItemListener programIl = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent ie) {
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                JComboBox cb = (JComboBox) ie.getSource();
                Object o2 = cb.getSelectedItem();
                if (o2 != null && o2 instanceof ProgramBean) {
                    ProgramBean pr = (ProgramBean) o2;
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

        if (WebserviceUtils.testWS("ears2/getCruise")) {
            try {
                cruiseClient = new RestClientCruise();
                programClient = new RestClientProgram();
                eventClient = new RestClientEvent();
            } catch (ConnectException ex) {
                //can't happen
                Messaging.report("Note that the webservices are offline. The tool won't function properly.", Message.State.BAD, this.getClass(), true);
            } catch (EarsException ex) {
                Messaging.report(ex.getMessage(), ex, this.getClass(), true);
            }
            if (currentVessel != null) {
                cruises = new TreeSet<>(cruiseClient.getCruiseByPlatform(currentVessel.getConcept()));
                currentCruise = CurrentCruise.getInstance(CruiseBean.getCruiseByDate(cruises, OffsetDateTime.now()));
                // currentCruise = CurrentCruise.getInstance(cruiseClient.getCruiseByDate(OffsetDateTime.now(), currentVessel.getConcept()));

                if (currentCruise != null && currentCruise.getConcept() == null) {
                    currentCruise = CurrentCruise.getInstance((CruiseBean) cruises.toArray()[0]);
                }
                if (currentCruise != null && currentCruise.getConcept() != null) {
                    GlobalActionContextProxy.getInstance().add(currentCruise);
                    Messaging.report("Current cruise: " + currentCruise.getConcept().toString(), Message.State.INFO, this.getClass(), false);
                } else {
                    Messaging.report("There is no actual cruise ongoing.", Message.State.BAD, this.getClass(), true);
                }

                currentPrograms = programClient.getProgramByCruise(currentCruise.getConcept());

                if (currentProgram == null || currentProgram.getConcept() == null) {
                    currentProgram = CurrentProgram.getInstance((ProgramBean) currentPrograms.toArray()[0]);
                }
                if (currentProgram != null && currentProgram.getConcept() != null) {
                    GlobalActionContextProxy.getInstance().add(currentProgram);
                    //Messaging.report("Current program: " + currentProgram.getConcept().toString(), Message.State.INFO, this.getClass(), false);
                } else {
                    Messaging.report("There is no current program selected.", Message.State.BAD, this.getClass(), true);
                }
            } else {
                cruises = new TreeSet<>();
                currentPrograms = new TreeSet<>();
            }

        } else {
            cruises = new TreeSet<>();
            currentPrograms = new TreeSet<>();
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
            cruiseComboBox = new JComboBox();
        }
        cruiseComboBox.removeItemListener(cruiseIl);
        if (cruises != null) {
            if (cruiseComboBox.getItemCount() > 0) {
                cruiseComboBox.removeAllItems();
            }
            if (cruises.size() > 0) {
                for (ICruise cruise : cruises) {
                    SwingUtils.addToComboBox(cruiseComboBox, cruise);
                }
                //cruiseComboBox.setSelectedItem(cruiseComboBox.getItemAt(0));
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
            programComboBox = new JComboBox();
        }
        programComboBox.removeItemListener(programIl);
        if (currentPrograms != null) {
            if (programComboBox.getItemCount() > 0) {
                programComboBox.removeAllItems();
            }
            if (currentPrograms.size() > 0) {
                for (IProgram program : currentPrograms) {
                    SwingUtils.addToComboBox(programComboBox, program);
                }
                if (currentCruise != null && currentCruise.getConcept() != null && (currentProgram == null || currentProgram.getConcept() == null)) {
                    ProgramBean currentProgramBean = (ProgramBean) programComboBox.getItemAt(0);
                    programComboBox.setSelectedItem(currentProgramBean);
                    currentProgram = CurrentProgram.getInstance(currentProgramBean);
                    Messaging.report("Current program: " + currentProgram.getConcept().toString(), Message.State.INFO, this.getClass(), false);
                }

            } else {
                currentProgram = CurrentProgram.getInstance(null);
                /* programComboBox.addItem("No programs for selected cruise");
                currentProgram = CurrentProgram.getInstance(null);
                Messaging.report("There is no current program selected.", Message.State.BAD, this.getClass(), true); */
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

        JButton o_createCruise = new JButton(NbBundle.getMessage(GeneralToolBarAction.class, "CREATE_CRUISE"));
        o_createCruise.addActionListener(new CreateNewCruiseActionListener());

        JButton o_createProgram = new JButton(NbBundle.getMessage(GeneralToolBarAction.class, "CREATE_PROGRAM"));
        o_createProgram.addActionListener(new CreateNewProgramActionListener());

        JButton o_editCruise = new JButton(NbBundle.getMessage(GeneralToolBarAction.class, "UPDATE_CRUISE"));
        o_editCruise.addActionListener(new EditCruiseActionListener());

        JButton o_editProgram = new JButton(NbBundle.getMessage(GeneralToolBarAction.class, "UPDATE_PROGRAM"));
        o_editProgram.addActionListener(new EditProgramActionListener());

        JButton o_createEvent = new JButton(NbBundle.getMessage(GeneralToolBarAction.class, "CREATE_EVENT"));
        o_createEvent.addActionListener(new CreateEventActionListener());

        if (cruiseComboBox == null) {
            cruiseComboBox = new JComboBox();
        }

        if (programComboBox == null) {
            programComboBox = new JComboBox();
        }
        jPanel.add(o_createCruise);
        jPanel.add(o_createProgram);
        jPanel.add(o_editCruise);
        jPanel.add(o_editProgram);
        jPanel.add(o_createEvent);
        jPanel.add(new JLabel("Set current program by selecting a cruise:"));
        jPanel.add(cruiseComboBox);
        jPanel.add(programComboBox);
        return jPanel;
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
            if (cruiseClient == null) {
                try {
                    cruiseClient = new RestClientCruise();
                } catch (ConnectException ex) {
                    Messaging.report("The webservices are offline.", Message.State.BAD, this.getClass(), false);
                } catch (EarsException ex) {
                    Messaging.report(ex.getMessage(), ex, this.getClass(), false);
                }
            }
            if (cruiseClient != null && currentVessel != null && WebserviceUtils.testWS("ears2/getCruise")) {
                cruises = new TreeSet<>(cruiseClient.getCruiseByPlatform(currentVessel.getConcept()));
            } else {
                cruises = new TreeSet<>();
            }
            if (!cruises.isEmpty()) {
                if (programClient == null) {
                    try {
                        programClient = new RestClientProgram();
                    } catch (ConnectException ex) {
                        Messaging.report("The webservices are offline.", Message.State.BAD, this.getClass(), false);
                    } catch (EarsException ex) {
                        Messaging.report(ex.getMessage(), ex, this.getClass(), false);
                    }
                }
                List<CruiseBean> list = new ArrayList(cruises);
                if (programClient != null && WebserviceUtils.testWS("ears2/getProgram")) {
                    currentCruise = currentCruiseResult.getCurrent();
                    if (currentCruise != null) {
                        currentPrograms = programClient.getProgramByCruise(currentCruise.getConcept()); //get the programs of the current cruise
                    } else {
                        currentPrograms = programClient.getProgramByCruise(list.get(0)); //get the programs of the first cruise in the list

                    }

                } else {
                    currentPrograms = new TreeSet<>();
                }
            } else {
                currentPrograms = new TreeSet<>();
            }
            GlobalActionContextProxy.getInstance().removeAll(IProgram.class);
            GlobalActionContextProxy.getInstance().addAll(currentPrograms);

            populateComboboxes();
        }

        if (currentCruiseResult.matches(ev)) {
            currentCruise = currentCruiseResult.getCurrent();
            if (programClient != null && currentCruise != null && WebserviceUtils.testWS("ears2/getProgram")) {
                currentPrograms = new TreeSet<>(programClient.getProgramByCruise(currentCruise.getConcept()));
            } else {
                currentPrograms = new TreeSet<>();
            }
            GlobalActionContextProxy.getInstance().removeAll(IProgram.class);
            GlobalActionContextProxy.getInstance().addAll(currentPrograms);
            populateProgramCombobox();
        }
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
            CreateCruiseSetupTopComponent cnc = new CreateCruiseSetupTopComponent();
            cnc.open();
            cnc.requestActive();
        }
    }

    private class CreateNewProgramActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            CreateProgramSetupTopComponent cps = new CreateProgramSetupTopComponent();
            cps.open();
            cps.requestActive();
        }
    }
}
