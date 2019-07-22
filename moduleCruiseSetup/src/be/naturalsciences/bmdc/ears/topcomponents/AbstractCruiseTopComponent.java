/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.SeaAreaTableModel;
import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.ChiefScientistTableModel;
import be.naturalsciences.bmdc.ears.base.StaticMetadataSearcher;
import be.naturalsciences.bmdc.ears.comparator.CountryComparator;
import be.naturalsciences.bmdc.ears.entities.CountryBean;
import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.entities.HarbourBean;
import be.naturalsciences.bmdc.ears.entities.ICountry;
import be.naturalsciences.bmdc.ears.entities.IHarbour;
import be.naturalsciences.bmdc.ears.entities.IOrganisation;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.IVessel;
import be.naturalsciences.bmdc.ears.entities.OrganisationBean;
import be.naturalsciences.bmdc.ears.entities.SeaAreaBean;
import be.naturalsciences.bmdc.ears.entities.VesselBean;
import be.naturalsciences.bmdc.ears.netbeans.services.GlobalActionContextProxy;
import be.naturalsciences.bmdc.ears.netbeans.services.SingletonResult;
import be.naturalsciences.bmdc.ears.rest.RestClientCruise;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ears.utils.NotificationThread;
import be.naturalsciences.bmdc.ears.utils.SwingUtils;
import be.naturalsciences.bmdc.ears.validators.EndDateValidator;
import be.naturalsciences.bmdc.ears.validators.HarbourValidator;
import be.naturalsciences.bmdc.ears.validators.OrganisationValidator;
import be.naturalsciences.bmdc.ears.validators.SeaAreaValidator;
import be.naturalsciences.bmdc.ears.validators.StartDateValidator;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import gnu.trove.map.hash.THashMap;
import java.net.ConnectException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.JTextComponent;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.validation.api.Problem;
import org.netbeans.validation.api.builtin.stringvalidation.StringValidators;
import org.netbeans.validation.api.ui.ValidationGroup;
import org.openide.util.*;
import org.openide.windows.TopComponent;

public abstract class AbstractCruiseTopComponent extends TopComponent implements LookupListener, TableModelListener, SaveButtonDisablerOnValidationFailure {

    //protected JComboBox chiefScientistCountryList;
    //protected JComboBox list;
    //protected static JComboBox seaAreaList;
    protected CruiseBean actualCruise;

    protected final SingletonResult<CurrentVessel, IVessel> currentVesselResult;

    protected SeaAreaTableModel seaAreaModel;
    protected ChiefScientistTableModel chiefScientistModel;

    ComboBoxColumnEditor<CountryBean> countryList;
    ComboBoxColumnEditor<OrganisationBean> organisationList;
    ComboBoxColumnEditor<SeaAreaBean> seaAreaList;
    //protected List<JComboBox> chiefScientistOrganisationEditors;
    //static List<JComboBox<Country>> countryLists;
    //static List<JComboBox<OrganizationBean>> organisationLists;

    @Override
    public ValidationGroup getValidationGroup() {
        return group;
    }

    public AbstractCruiseTopComponent() {
        __initComponents();
        currentVesselResult = new SingletonResult<>(CurrentVessel.class, this);
        //countryLists = new ArrayList();
        //organisationLists = new ArrayList();
    }

    protected abstract void __initComponents();

    protected void cruiseNameValueFocusLostP(java.awt.event.FocusEvent evt) {
    }

    protected void platformCodeResultActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    protected void collateCentreListPrincipalActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        collateCentreListSecondary.removeAllItems();
        collateCentreListSecondary.addItem("Choose organisation");
        Object o = collateCentreListPrincipal.getSelectedItem();
        if (o != null && o instanceof CountryBean) {
            CountryBean selectedCountry = (CountryBean) o;
            StaticMetadataSearcher.getInstance().getOrganisations(true).stream().filter((org) -> (org.getCountryObject().equals(selectedCountry))).forEachOrdered((org) -> {
                collateCentreListSecondary.addItem(org);
            });
        } else if (o instanceof String) {
            StaticMetadataSearcher.getInstance().getOrganisations(true).forEach((org) -> {
                collateCentreListSecondary.addItem(org);
            });
        }
    }

    protected void collateCentreListSecondaryActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        Object o = collateCentreListSecondary.getSelectedItem();
        if (o != null && o instanceof OrganisationBean) {
            OrganisationBean selectedOrganisation = (OrganisationBean) o;
            collateCentreResult.setText(selectedOrganisation.getCode());
        } else if (o instanceof String) {
            collateCentreResult.setText("");
        }
    }

    protected void startingHarborListPrincipalActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        startingHarborListSecondary.removeAllItems();
        startingHarborListSecondary.addItem("Choose harbour");
        Object o = startingHarborListPrincipal.getSelectedItem();
        if (o != null && o instanceof CountryBean) {
            CountryBean selectedCountry = (CountryBean) o;
            StaticMetadataSearcher.getInstance().getHarbours(true).stream().filter((harbour) -> (harbour.getCountryObject().equals(selectedCountry))).forEachOrdered((harbour) -> {
                startingHarborListSecondary.addItem(harbour);
            });
        } else if (o instanceof String) {
            StaticMetadataSearcher.getInstance().getHarbours(true).forEach((harbour) -> {
                startingHarborListSecondary.addItem(harbour);
            });
        }
    }

    protected void startingHarborListSecondaryActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        Object o = startingHarborListSecondary.getSelectedItem();
        if (o != null && o instanceof HarbourBean) {
            HarbourBean selectedHarbor = (HarbourBean) o;
            startingHarborResult.setText(selectedHarbor.getCode());
        } else if (o instanceof String) {
            startingHarborResult.setText("");
        }
    }

    protected void arrivalHarborListPrincipalActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        arrivalHarborListSecondary.removeAllItems();
        arrivalHarborListSecondary.addItem("Choose harbour");
        Object o = arrivalHarborListPrincipal.getSelectedItem();
        if (o != null && o instanceof CountryBean) {
            CountryBean selectedCountry = (CountryBean) o;
            StaticMetadataSearcher.getInstance().getHarbours(true).stream().filter((harbour) -> (harbour.getCountryObject().equals(selectedCountry))).forEachOrdered((harbour) -> {
                arrivalHarborListSecondary.addItem(harbour);
            });
        } else if (o instanceof String) {
            StaticMetadataSearcher.getInstance().getHarbours(true).forEach((harbour) -> {
                arrivalHarborListSecondary.addItem(harbour);
            });
        }
    }

    protected void arrivalHarborListSecondaryActionPerformedP(java.awt.event.ActionEvent evt) {

        // TODO add your handling code here:
        Object o = arrivalHarborListSecondary.getSelectedItem();
        if (o != null && o instanceof HarbourBean) {
            HarbourBean selectedHarbour = (HarbourBean) o;
            arrivalHarborResult.setText(selectedHarbour.getCode());
        } else if (o instanceof String) {
            arrivalHarborResult.setText("");
        }
    }

    protected void platformCodeListPrincipalActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

        Object o = platformCodeListPrincipal.getSelectedItem();
        if (o != null && o instanceof VesselBean) {
            VesselBean selectedVessel = (VesselBean) o;
            platformCodeResult.setText(selectedVessel.getCode());
        }
    }

    protected void addSeaAreaActionPerformedP(java.awt.event.ActionEvent evt) {
        seaAreaModel.addRow();
        seaAreaTable.setEditingRow(seaAreaModel.getRowCount() - 1);

        JComboBox box = seaAreaList.addComboBox("Sea area"); //add a combobox with a validator
        group.add(box, new SeaAreaValidator());
        enableThatButtonGreysOutOnValidationFailure((JTextComponent) box.getEditor().getEditorComponent(), group);

    }

    protected void removeSeaAreaActionPerformedP(java.awt.event.ActionEvent evt) {

        if (seaAreaTable.getSelectedRow() > -1 && seaAreaModel.getRowCount() > 0) {
            int selectedRow = seaAreaTable.getSelectedRow();
            seaAreaModel.removeRow(selectedRow);
            seaAreaList.removeComboBox(selectedRow);
        }
    }

    protected void addChiefScientistActionPerformedP(java.awt.event.ActionEvent evt) {
        chiefScientistModel.addRow();
        chiefScientistTable.setEditingRow(chiefScientistModel.getRowCount() - 1);

        organisationList.addComboBox("Chief scientist organisation name");
        countryList.addComboBox("Chief scientist country name");

    }

    protected void removeChiefScientistActionPerformedP(java.awt.event.ActionEvent evt) {
        if (chiefScientistTable.getSelectedRow() > -1) {
            int selectedRow = chiefScientistTable.getSelectedRow();
            chiefScientistModel.removeRow(selectedRow);
            countryList.removeComboBox(selectedRow);
            organisationList.removeComboBox(selectedRow);
        }
    }

    protected void inputEndDateActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    protected void inputStartDateActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

    }

    protected void cruiseIdentifierValueActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

    }

    protected void collateCentreResultActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    protected void cruiseNameValueActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

    }

    protected void cruiseNameValueKeyTypedP(java.awt.event.KeyEvent evt) {
        // TODO add your handling code here:
        //   System.out.println(cruiseNameValue.getText());
    }

    protected void cruiseNameValueKeyReleasedP(java.awt.event.KeyEvent evt) {
        // TODO add your handling code here:
        JFormattedTextField getDateTimeStartDate = inputStartDate.getEditor();

        if (inputStartDate.getDate() != null) {

            cruiseIdentifierValue.setText(cruiseNameValue.getText());
            //  cruiseIdentifierValue.setText(getDateTimeStartDate.getText() + "_" + cruiseNameValue.getText());
        } else {
            cruiseIdentifierValue.setText(cruiseNameValue.getText() + " Please fill the start date");
        }

    }

    protected void inputStartDateKeyPressedP(java.awt.event.KeyEvent evt) {
        // TODO add your handling code here:

    }

    protected void inputStartDateKeyTypedP(java.awt.event.KeyEvent evt) {
        // TODO add your handling code here:

    }

    protected void inputStartDateFocusLostP(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:

    }

    protected void inputStartDatePropertyChangeP(java.beans.PropertyChangeEvent evt) {
        // TODO add your handling code here:

    }

    protected void inputStartDateFocusGainedP(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:

    }

    protected void inputStartDateInputMethodTextChangedP(java.awt.event.InputMethodEvent evt) {
        // TODO add your handling code here:

    }

    protected void chiefScientistTableFocusLostP(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:

    }

    protected void inputEndDatePropertyChangeP(java.beans.PropertyChangeEvent evt) {
        // TODO add your handling code here:
    }

    protected void theContainerComponentShownP(java.awt.event.ComponentEvent evt) {
        // TODO add your handling code here:

    }

    protected void jPanelPlatformComponentShownP(java.awt.event.ComponentEvent evt) {
        // TODO add your handling code here:

    }

    protected void platformCodeListPrincipalItemStateChangedP(java.awt.event.ItemEvent evt) {
        // TODO add your handling code here:

    }

    protected javax.swing.JButton addChiefScientist;
    protected javax.swing.JButton addSeaArea;
    protected javax.swing.JButton removeChiefScientist;
    protected javax.swing.JButton removeSeaArea;
    protected javax.swing.JButton saveCruiseButton;
    protected javax.swing.JComboBox arrivalHarborListPrincipal;
    protected javax.swing.JComboBox arrivalHarborListSecondary;
    protected javax.swing.JComboBox collateCentreListPrincipal;
    protected javax.swing.JComboBox collateCentreListSecondary;
    protected javax.swing.JComboBox platformCodeListPrincipal;
    protected javax.swing.JComboBox startingHarborListPrincipal;
    protected javax.swing.JComboBox startingHarborListSecondary;
    protected javax.swing.JLabel jLabel10;
    protected javax.swing.JLabel jLabel2;
    protected javax.swing.JLabel jLabel7;
    protected javax.swing.JLabel jLabel8;
    protected javax.swing.JLabel jLabel9;
    protected javax.swing.JPanel cruiseIdentifier;
    protected javax.swing.JPanel jPanel;
    protected javax.swing.JPanel jPanel1;
    protected javax.swing.JPanel jPanel5;
    protected javax.swing.JPanel jPanelArrivalHarbor;
    protected javax.swing.JPanel jPanelCollateCentre;
    protected javax.swing.JPanel jPanelDate;
    protected javax.swing.JPanel jPanelInformation;
    protected javax.swing.JPanel jPanelObjectives;
    protected javax.swing.JPanel jPanelPlatform;
    protected javax.swing.JPanel jPanelStartingHarbor;
    protected javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JScrollPane jScrollPane4;
    protected javax.swing.JScrollPane jScrollPane6;
    protected javax.swing.JScrollPane jScrollPane7;
    protected javax.swing.JTable chiefScientistTable;
    protected javax.swing.JTable seaAreaTable;
    protected javax.swing.JTextArea o_objectiveValue;
    protected javax.swing.JTextField arrivalHarborResult;
    protected javax.swing.JTextField collateCentreResult;
    protected javax.swing.JTextField cruiseIdentifierValue;
    protected javax.swing.JTextField cruiseNameValue;
    protected javax.swing.JTextField platformCodeResult;
    protected javax.swing.JTextField startingHarborResult;
    protected org.jdesktop.swingx.JXDatePicker inputEndDate;
    protected org.jdesktop.swingx.JXDatePicker inputStartDate;

    protected org.netbeans.validation.api.ui.swing.ValidationPanel validationPanel1;

    protected ValidationGroup group = null;// = validationPanel1.getValidationGroup();

    public class CruisePoster extends NotificationThread {

        ProgressHandle progr;
        IResponseMessage response;

        public CruisePoster(ProgressHandle progr, IResponseMessage response) {
            this.progr = progr;
            this.response = response;
        }

        @Override
        public void doWork() {
            progr.start();
            RestClientCruise client = null;
            try {
                client = new RestClientCruise();
            } catch (ConnectException ex) {
                Messaging.report("Posting the new cruise to the webservice failed because it is offline or its url is incorrect.", ex, this.getClass(), true);
            } catch (EarsException ex) {
                Messaging.report(ex.getMessage(), ex, this.getClass(), true);
            }
            if (client != null) {
                response = client.postCruise(actualCruise);
                progr.progress("Cruise info sent");

                if (response.isBad()) {
                    Messaging.report("Posting the new cruise to the webservice failed: " + response.getSummary(), Message.State.BAD, this.getClass(), true);
                } else {
                    Messaging.report(response.getSummary(), Message.State.GOOD, this.getClass(), true);
                }
            }
        }
    }

    public class CruiseModifier extends NotificationThread {

        ProgressHandle progr;
        IResponseMessage response;

        public CruiseModifier(ProgressHandle progr, IResponseMessage response) {
            this.progr = progr;
            this.response = response;
        }

        @Override
        public void doWork() {
            progr.start();
            RestClientCruise client = null;
            try {
                client = new RestClientCruise();
            } catch (ConnectException ex) {
                Messaging.report("Posting the modified cruise to the webservice failed because it is offline or its url is incorrect.", ex, this.getClass(), true);
            } catch (EarsException ex) {
                Messaging.report(ex.getMessage(), ex, this.getClass(), true);
            }
            if (client != null) {
                response = client.modifyCruise(actualCruise);
                progr.progress("Cruise info sent");

                if (response.isBad()) {
                    Messaging.report("Posting the changed cruise to the webservice failed: " + response.getSummary(), Message.State.BAD, this.getClass(), true);
                } else {
                    Messaging.report(response.getSummary(), Message.State.GOOD, this.getClass(), true);
                    GlobalActionContextProxy.getInstance().add(currentVesselResult.getCurrent()); //causes the vessel to be changed to itself, causing vessel listeners to update their cruise list
                    progr.finish();
                }
            }
        }
    }

    //  private boolean simpleValid = false; //remove to make sea areas no longer mandatory.
    private boolean simpleValid = true;

    public boolean formValidates() {
        boolean chiefScientistModelOk = chiefScientistModel.performValidation();
        Problem validateAll = group.performValidation();

        return chiefScientistModelOk && simpleValid && !(validateAll != null && validateAll.isFatal());
    }

    @Override
    protected void componentShowing() {
    }

    @Override
    public void componentOpened() {
        collateCentreListPrincipal.addItem("Choose country to limit organisation");
        startingHarborListPrincipal.addItem("Choose country to limit harbours");
        arrivalHarborListPrincipal.addItem("Choose country to limit harbours");

        StaticMetadataSearcher.getInstance().getVessels(true).forEach((vessel) -> {
            platformCodeListPrincipal.addItem(vessel);
        });
        if (currentVesselResult.getCurrent() != null && currentVesselResult.getCurrent().getConcept() != null) {
            VesselBean currentVessel = currentVesselResult.getCurrent().getConcept();
            platformCodeResult.setText(currentVessel.getCode());
            platformCodeListPrincipal.setSelectedItem(currentVessel);
        }
        DefaultComboBoxModel collateCentreListPrincipalModel = new DefaultComboBoxModel();
        collateCentreListPrincipal.setModel(collateCentreListPrincipalModel);
        StaticMetadataSearcher.getInstance().getOrganisations(true).stream().map((organisation) -> {
            collateCentreListSecondary.addItem(organisation);
            return organisation;
        }).map((organisation) -> organisation.getCountryObject()).forEachOrdered((c) -> {
            SwingUtils.addToComboBox(collateCentreListPrincipalModel, c);
        });

        DefaultComboBoxModel startingHarborListPrincipalModel = new DefaultComboBoxModel();
        startingHarborListPrincipal.setModel(startingHarborListPrincipalModel);

        DefaultComboBoxModel arrivalHarborListPrincipalModel = new DefaultComboBoxModel();
        arrivalHarborListPrincipal.setModel(arrivalHarborListPrincipalModel);

        StaticMetadataSearcher.getInstance().getHarbours(true).stream().map((harbour) -> {
            startingHarborListSecondary.addItem(harbour);
            return harbour;
        }).map((harbour) -> {
            arrivalHarborListSecondary.addItem(harbour);
            return harbour;
        }).map((harbour) -> harbour.getCountryObject()).map((c) -> {
            SwingUtils.addToComboBox(startingHarborListPrincipalModel, c);
            return c;
        }).forEachOrdered(new Consumer<CountryBean>() {
            @Override
            public void accept(CountryBean c) {
                SwingUtils.addToComboBox(arrivalHarborListPrincipalModel, c);
            }
        });

        chiefScientistModel = (ChiefScientistTableModel) chiefScientistTable.getModel();
        chiefScientistModel.setTable(chiefScientistTable);
        setUpChiefScientistColumn();

        seaAreaModel = (SeaAreaTableModel) seaAreaTable.getModel();
        seaAreaModel.setTable(seaAreaTable);
        setUpSeaAreaColumn();

        cruiseNameValue.setName("Cruise name (identifier)");
        inputStartDate.getEditor().setName("Start date of cruise");
        inputEndDate.getEditor().setName("End date of cruise");
        startingHarborResult.setName("Start harbour code");
        arrivalHarborResult.setName("Arrival harbour code");
        platformCodeResult.setName("Platform code");

        o_objectiveValue.setEditable(true);
        arrivalHarborListPrincipal.setEditable(true);
        arrivalHarborListSecondary.setEditable(true);
        collateCentreListPrincipal.setEditable(true);
        collateCentreListSecondary.setEditable(true);
        platformCodeListPrincipal.setEditable(true);
        startingHarborListPrincipal.setEditable(true);
        startingHarborListSecondary.setEditable(true);

        AutoCompleteDecorator.decorate(startingHarborListSecondary);
        AutoCompleteDecorator.decorate(arrivalHarborListSecondary);
        AutoCompleteDecorator.decorate(collateCentreListSecondary);

        //removeSeaArea.setEnabled(false);
        seaAreaModel.addTableModelListener(this);

        group.add(cruiseNameValue, StringValidators.REQUIRE_NON_EMPTY_STRING);
        group.add(inputStartDate.getEditor(), new StartDateValidator(inputEndDate));
        group.add(inputEndDate.getEditor(), new EndDateValidator(inputStartDate));
        group.add(startingHarborListSecondary, new HarbourValidator());
        group.add(arrivalHarborListSecondary, new HarbourValidator());
        group.add(collateCentreListSecondary, new OrganisationValidator());
        group.add(platformCodeListPrincipal, StringValidators.REQUIRE_NON_EMPTY_STRING);
        tableChanged(null);

        enableThatButtonGreysOutOnValidationFailure(cruiseNameValue, group);
        enableThatButtonGreysOutOnValidationFailure(inputStartDate.getEditor(), group);
        enableThatButtonGreysOutOnValidationFailure(inputEndDate.getEditor(), group);

        enableThatButtonGreysOutOnValidationFailure(startingHarborResult, group);
        enableThatButtonGreysOutOnValidationFailure(arrivalHarborResult, group);
        enableThatButtonGreysOutOnValidationFailure((JTextField) collateCentreListSecondary.getEditor().getEditorComponent(), group);
        enableThatButtonGreysOutOnValidationFailure(platformCodeResult, group);
        enableThatButtonGreysOutOnValidationFailure((JTextField) platformCodeListPrincipal.getEditor().getEditorComponent(), group);

        String scrollSpeedString = org.openide.util.NbBundle.getMessage(AbstractCruiseTopComponent.class, "AbstractCruiseTopComponent.ScrollSpeed");
        int scrollSpeed = Integer.parseInt(scrollSpeedString);
        SwingUtils.setScrollSpeed(jScrollPane1, scrollSpeed);
        SwingUtils.setScrollSpeed(jScrollPane4, scrollSpeed);
        SwingUtils.setScrollSpeed(jScrollPane6, scrollSpeed);
        SwingUtils.setScrollSpeed(jScrollPane7, scrollSpeed);

        collateCentreListSecondary.setMaximumRowCount(SwingUtils.COMBOBOX_MAX_ROW_COUNT);
        startingHarborListSecondary.setMaximumRowCount(SwingUtils.COMBOBOX_MAX_ROW_COUNT);
        arrivalHarborListSecondary.setMaximumRowCount(SwingUtils.COMBOBOX_MAX_ROW_COUNT);

    }

    private Map<JTextComponent, DocumentListener> componentsWithDocumentListener = new THashMap<>();

    @Override
    public void enableThatButtonGreysOutOnValidationFailure(JTextComponent c, ValidationGroup group) {
        DocumentListener dc = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkValidation();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkValidation();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkValidation();
            }

        };
        c.getDocument().addDocumentListener(dc);
        componentsWithDocumentListener.put(c, dc);
    }

    @Override
    public void disableThatButtonGreysOutOnValidationFailure(JTextComponent c) {
        c.getDocument().removeDocumentListener(componentsWithDocumentListener.get(c));
    }

    //public abstract void checkValidation();
    @Override
    public void componentClosed() {
    }

    @Override
    public void resultChanged(LookupEvent le) {
        if (currentVesselResult.getCurrent() != null) {
            platformCodeListPrincipal.setSelectedItem(currentVesselResult.getCurrent().getConcept());
        }
    }

    protected void setUpSeaAreaColumn() {
        seaAreaList = new ComboBoxColumnEditor(StaticMetadataSearcher.getInstance().getSeaAreas(true), seaAreaTable, 0, "Choose a sea", this);

        //addSeaAreaActionPerformedP(null); //add a first sea area.
    }

    protected void setUpChiefScientistColumn() {
        Set<ICountry> countries = new TreeSet<>(new CountryComparator());
        StaticMetadataSearcher.getInstance().getOrganisations(true).forEach((org) -> {
            countries.add(org.getCountryObject());
        });
        countryList = new ComboBoxColumnEditor(countries, chiefScientistTable, 0, "You can choose a country to narrow down organisations.", this);
        organisationList = new ComboBoxColumnEditor(null, chiefScientistTable, 1, "Choose an organisation.", this);
    }

    /**
     * *
     * Create a new cruise from input fields
     *
     * @return
     */
    protected CruiseBean createCruiseFromInput() {
        CruiseBean newCruise = new CruiseBean();
        newCruise.setCruiseName(cruiseNameValue.getText());

        try {
            newCruise.setdStartDate(inputStartDate.getDate());
            newCruise.setdEndDate(inputEndDate.getDate());
        } catch (ParseException ex) {
            //cannot occur
        }

        if (newCruise.getRealId() == null) { //if it already has a real identifier (ie. if we're editing), don't assign a new one.
            newCruise.assignRealId();
        }
        return createCruiseFromInput(newCruise);

    }

    protected CruiseBean createCruiseFromInput(CruiseBean cruise) {
        if (cruise.getRealId() == null) {
            throw new IllegalArgumentException("The provided cruise has no identifier, and so cannot be edited");
        } else {
            cruise.setCruiseName(cruiseNameValue.getText());

            try {
                //Date start = inputStartDate.getDate(); //doesn't provide the correct date!!
                //Date end = inputEndDate.getDate(); //doesn't provide the correct date!!
                Date start = StringUtils.SDF_ISO_DATETIME.parse(inputStartDate.getEditor().getText());
                Date end = StringUtils.SDF_ISO_DATETIME.parse(inputEndDate.getEditor().getText());

                if (start.equals(end)) { //00.00.00

                    LocalDateTime endl = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault());
                    endl.plusHours(23).plusMinutes(59);
                    end = Date.from(endl.atZone(ZoneId.systemDefault()).toInstant());
                }
                //ZonedDateTime d = ZonedDateTime.ofInstant(otherDate.toInstant(),ZoneId.of("Europe/Paris"));    

                cruise.setdStartDate(start);
                cruise.setdEndDate(end);
            } catch (ParseException ex) {
                //cannot occur
            }

            cruise.setChiefScientists(chiefScientistModel.getPersons());
            cruise.setPlatformCode(platformCodeResult.getText());
            cruise.setObjectives(o_objectiveValue.getText());
            cruise.setCollateCenter(collateCentreResult.getText());
            cruise.setStartingHarbor(startingHarborResult.getText());
            cruise.setArrivalHarbor(arrivalHarborResult.getText());

            Set<SeaAreaBean> seas = seaAreaModel.getEntitiesSet();

            cruise.setSeaAreas(seas);

            if (cruise.isLegal()) {
                return cruise;
            } else {
                return null;
            }
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        /*  JLabel label = ValidatorUtils.getValidatorLabel(validationPanel1);
         if (seaAreaModel.getRowCount() == 1) {
         removeSeaArea.setEnabled(false);
         } else {
         removeSeaArea.setEnabled(true);
         }
         boolean valid = true;
         for (SeaAreaBean seaArea : seaAreaModel.getEntities()) {
         if (!seaArea.isLegal()) {
         valid = false;
         }
         }
         if (seaAreaModel.getEntities().isEmpty()) {
         valid = false;
         }
         simpleValid = valid;
         if (!simpleValid) {
         //group.add(platformCodeListPrincipal, StringValidators.REQUIRE_NON_EMPTY_STRING)
         ValidatorUtils.addError(validationPanel1, "Choose a Sea Area.");
         checkValidation();
         } else {
         ValidatorUtils.removeError(validationPanel1);
         checkValidation();
         }*/
    }
}
