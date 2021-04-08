/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.ProjectTableModel;
import be.naturalsciences.bmdc.ears.base.StaticMetadataSearcher;
import be.naturalsciences.bmdc.ears.comparator.CountryComparator;
import be.naturalsciences.bmdc.ears.entities.CountryBean;
import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.entities.ICountry;
import be.naturalsciences.bmdc.ears.entities.IOrganisation;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.IVessel;
import be.naturalsciences.bmdc.ears.entities.OrganisationBean;
import be.naturalsciences.bmdc.ears.entities.Person;
import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.netbeans.services.GlobalActionContextProxy;
import be.naturalsciences.bmdc.ears.netbeans.services.SingletonResult;
import be.naturalsciences.bmdc.ears.rest.RestClientProgram;
import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.ChiefScientistTableModel;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ears.utils.NotificationThread;
import be.naturalsciences.bmdc.ears.utils.SwingUtils;
import be.naturalsciences.bmdc.ears.validators.OrganisationValidator;
import be.naturalsciences.bmdc.ontology.EarsException;
import gnu.trove.map.hash.THashMap;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.validation.api.Problem;
import org.netbeans.validation.api.builtin.stringvalidation.StringValidators;
import org.netbeans.validation.api.ui.ValidationGroup;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;

/**
 *
 * @author Thomas Vandenberghe
 */
public abstract class AbstractProgramTopComponent extends TopComponent implements SaveButtonDisablerOnValidationFailure, LookupListener {

    protected ProgramBean actualProgram;

    protected final SingletonResult<CurrentVessel, IVessel> currentVesselResult;

    ComboBoxColumnEditor projectCountryList;
    ComboBoxColumnEditor projectOrganisationList;
    ComboBoxColumnEditor projectList;

    ProjectTableModel projectTableModel;

    public ValidationGroup getValidationGroup() {
        return validationGroup;
    }

    protected abstract void __initComponents();

    public AbstractProgramTopComponent() {
        __initComponents();
        currentVesselResult = new SingletonResult<>(CurrentVessel.class, this);
    }

    protected void projectEdmerpTableFocusLostP(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:

    }

    protected void addProjectActionPerformedP(java.awt.event.ActionEvent evt) {
        projectTableModel.addRow();
        projectEdmerpTable.setEditingRow(projectTableModel.getRowCount() - 1);

        projectList.addComboBox("Project name");
        projectOrganisationList.addComboBox("Project organisation name");
        projectCountryList.addComboBox("Project country name");

    }

    protected void removeProjectActionPerformedP(java.awt.event.ActionEvent evt) {
        if (projectEdmerpTable.getSelectedRow() > -1) {
            int selectedRow = projectEdmerpTable.getSelectedRow();
            projectTableModel.removeRow(selectedRow);
            projectCountryList.removeComboBox(selectedRow);
            projectOrganisationList.removeComboBox(selectedRow);
            projectList.removeComboBox(selectedRow);
        }
    }

    protected void collateCentreListPrincipalActionPerformedP(java.awt.event.ActionEvent evt) {
        piOrganisationSecondary.removeAllItems();
        piOrganisationSecondary.addItem(ChiefScientistTableModel.BASE_ACTION);
        Object o = piOrganisationCountry.getSelectedItem();
        if (o != null && o instanceof CountryBean) {
            CountryBean selectedCountry = (CountryBean) o;
            for (IOrganisation org : StaticMetadataSearcher.getInstance().getOrganisations(true)) {
                if (org.getCountryObject().equals(selectedCountry)) {
                    piOrganisationSecondary.addItem(org);
                }
            }
        }
    }

    protected void collateCentreListSecondaryActionPerformedP(java.awt.event.ActionEvent evt) {
        Object o = piOrganisationSecondary.getSelectedItem();
        if (o != null && o instanceof OrganisationBean) {
            OrganisationBean selectedOrganisation = (OrganisationBean) o;
            piOrgGreyTextField.setText(selectedOrganisation.getCode());
        } else if (o instanceof String) {
            piOrgGreyTextField.setText("");
        }
    }

    protected void o_collateCentreResultActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    protected void o_piNameActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    protected javax.swing.JButton addProject;
    protected javax.swing.JComboBox piOrganisationCountry;
    protected javax.swing.JComboBox piOrganisationSecondary;
    protected javax.swing.JPanel jPanel1;
    protected javax.swing.JPanel jPanel2;
    protected javax.swing.JPanel jPanelCollateCentre;
    protected javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JScrollPane jScrollPane2;
    protected javax.swing.JScrollPane jScrollPane3;
    protected javax.swing.JTextField piOrgGreyTextField;
    protected javax.swing.JTextArea descriptionTextField;
    protected javax.swing.JTextField piLastNameTextField;
    protected javax.swing.JTextField piFirstNameTextField;
    protected javax.swing.JTextField programIdTextField;
    protected javax.swing.JPanel programIdentifier;
    protected javax.swing.JPanel programIdentifier1;
    protected javax.swing.JPanel programIdentifier3;
    protected javax.swing.JPanel programIdentifier4;
    protected javax.swing.JTable projectEdmerpTable;
    protected javax.swing.JButton removeProject;

    protected org.netbeans.validation.api.ui.swing.ValidationPanel validationPanel1;

    protected ValidationGroup validationGroup = null;

    // End of variables declaration                   
    @Override
    public void componentOpened() {
        piOrganisationCountry.addItem("Choose country to limit organisation");

        DefaultComboBoxModel piOrganisationCountryModel = new DefaultComboBoxModel();
        piOrganisationCountry.setModel(piOrganisationCountryModel);

        for (OrganisationBean organisation : StaticMetadataSearcher.getInstance().getOrganisations(true)) {
            piOrganisationSecondary.addItem(organisation);
            SwingUtils.addToComboBox(piOrganisationCountryModel, organisation.getCountryObject());
        }

        projectTableModel = (ProjectTableModel) projectEdmerpTable.getModel();
        projectTableModel.setTable(projectEdmerpTable);
        setUpProjectColumn();

        programIdTextField.setName("Program name (identifier)");
        piFirstNameTextField.setName("Principal investigator first name");
        piLastNameTextField.setName("Principal investigator last name");
        piOrganisationSecondary.setName("Collate centre");
        descriptionTextField.setName("Description");

        programIdTextField.setEditable(true);
        piFirstNameTextField.setEditable(true);
        piLastNameTextField.setEditable(true);
        descriptionTextField.setEditable(true);
        piOrganisationCountry.setEditable(true);
        piOrganisationSecondary.setEditable(true);

        AutoCompleteDecorator.decorate(piOrganisationSecondary);

        validationGroup.add(programIdTextField, StringValidators.REQUIRE_NON_EMPTY_STRING);
        validationGroup.add(piFirstNameTextField, StringValidators.REQUIRE_NON_EMPTY_STRING);
        validationGroup.add(piLastNameTextField, StringValidators.REQUIRE_NON_EMPTY_STRING);
        validationGroup.add(piOrgGreyTextField, new OrganisationValidator());
        //associated project

        enableThatButtonGreysOutOnValidationFailure(programIdTextField, validationGroup);
        enableThatButtonGreysOutOnValidationFailure(piFirstNameTextField, validationGroup);
        enableThatButtonGreysOutOnValidationFailure(piLastNameTextField, validationGroup);
        enableThatButtonGreysOutOnValidationFailure(piOrgGreyTextField, validationGroup);

        String scrollSpeedString = org.openide.util.NbBundle.getMessage(AbstractCruiseTopComponent.class, "AbstractCruiseTopComponent.ScrollSpeed");
        int scrollSpeed = Integer.parseInt(scrollSpeedString);
        SwingUtils.setScrollSpeed(jScrollPane1, scrollSpeed);
        SwingUtils.setScrollSpeed(jScrollPane2, scrollSpeed);
        SwingUtils.setScrollSpeed(jScrollPane3, scrollSpeed);

        piOrganisationSecondary.setMaximumRowCount(SwingUtils.COMBOBOX_MAX_ROW_COUNT);
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

    //abstract void checkValidation();
    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    protected void setUpProjectColumn() {
        Set<ICountry> countries = new TreeSet<>(new CountryComparator());
        for (IOrganisation org : StaticMetadataSearcher.getInstance().getOrganisations(true)) {
            countries.add(org.getCountryObject());
        }
        projectCountryList = new ComboBoxColumnEditor("Country", countries, projectEdmerpTable, ProjectTableModel.findColumnStatic(ProjectTableModel.COUNTRY), "You can choose a country to narrow down organisations.", this);
        projectOrganisationList = new ComboBoxColumnEditor("Project", null, projectEdmerpTable, ProjectTableModel.findColumnStatic(ProjectTableModel.ORG), "Choose an organisation.", this);
        projectList = new ComboBoxColumnEditor("Project", null, projectEdmerpTable, ProjectTableModel.findColumnStatic(ProjectTableModel.NAME), "Choose a project.", this);
    }

    public class ProgramPoster extends NotificationThread {

        ProgressHandle progr;
        IResponseMessage response;

        public ProgramPoster(ProgressHandle progr, IResponseMessage response) {
            this.progr = progr;
            this.response = response;
        }

        @Override
        public void doWork() {
            progr.start();
            RestClientProgram client = null;
            try {
                client = new RestClientProgram();
            } catch (ConnectException ex) {
                Messaging.report("There is a problem reaching the webservices.", ex, this.getClass(), true);
            } catch (EarsException ex) {
                Messaging.report("There is a problem reaching the webservices.", ex, this.getClass(), true);
            }
            if (client != null) {
                response = client.postProgram(actualProgram);
                progr.progress("Program info sent");

                if (response.isBad()) {
                    Messaging.report("Posting the new program to the webservice failed: " + response.getMessage(), Message.State.BAD, this.getClass(), true);
                } else {
                    Messaging.report(response.getMessage(), Message.State.GOOD, this.getClass(), true);
                    GlobalActionContextProxy.getInstance().add(currentVesselResult.getCurrent()); //causes the vessel to be changed to itself, causing vessel listeners to update their cruise list

                }
            }
        }
    }

    public class ProgramModifier extends NotificationThread {

        ProgressHandle progr;
        IResponseMessage response;

        public ProgramModifier(ProgressHandle progr, IResponseMessage response) {
            this.progr = progr;
            this.response = response;
        }

        @Override
        public void doWork() {
            progr.start();
            RestClientProgram client = null;
            try {
                client = new RestClientProgram();
            } catch (ConnectException ex) {
                Messaging.report("There is a problem reaching the webservices.", ex, this.getClass(), true);
            } catch (EarsException ex) {
                Messaging.report("There is a problem reaching the webservices.", ex, this.getClass(), true);
            }
            if (client != null) {
                response = client.modifyProgram(actualProgram);
                progr.progress("Program info sent");

                if (response.isBad()) {
                    Messaging.report("Posting the modified program to the webservice failed: " + response.getMessage(), Message.State.BAD, this.getClass(), true);
                } else {
                    Messaging.report(response.getMessage(), Message.State.GOOD, this.getClass(), true);
                    GlobalActionContextProxy.getInstance().add(currentVesselResult.getCurrent()); //causes the vessel to be changed to itself, causing vessel listeners to update their cruise list
                }
            }
        }
    }

    protected boolean formValidates() {
        Problem validateAll = validationGroup.performValidation();
        return !(validateAll != null && validateAll.isFatal());
    }

    public ProgramBean createProgramFromInput() {
        ProgramBean program = new ProgramBean();

        //   program.setCruiseId(((CruiseBean) cruiseComboBox.getSelectedItem()).getIdentifier()); //YS error if no cruise predefini
        program.setProgramId(programIdTextField.getText());
        Person pi = new Person(piFirstNameTextField.getText(), piLastNameTextField.getText(), piOrgGreyTextField.getText(), null, null);
        program.setPrincipalInvestigators(new ArrayList<>());
        program.getPrincipalInvestigators().add(pi);
        program.setDescription(descriptionTextField.getText());
        program.setProjects(projectTableModel.getEntitiesSet());
        return program;
    }

}
