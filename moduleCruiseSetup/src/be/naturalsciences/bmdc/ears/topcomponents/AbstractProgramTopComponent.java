/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.base.StaticMetadataSearcher;
import be.naturalsciences.bmdc.ears.comparator.CountryComparator;
import be.naturalsciences.bmdc.ears.entities.CountryBean;
import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.CurrentCruise;
import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.entities.ICountry;
import be.naturalsciences.bmdc.ears.entities.ICruise;
import be.naturalsciences.bmdc.ears.entities.IOrganisation;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.IVessel;
import be.naturalsciences.bmdc.ears.entities.OrganisationBean;
import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.netbeans.services.SingletonResult;
import be.naturalsciences.bmdc.ears.rest.RestClientCruise;
import be.naturalsciences.bmdc.ears.rest.RestClientProgram;
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
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.validation.api.Problem;
import org.netbeans.validation.api.builtin.stringvalidation.StringValidators;
import org.netbeans.validation.api.ui.ValidationGroup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;

/**
 *
 * @author Thomas Vandenberghe
 */
public abstract class AbstractProgramTopComponent extends TopComponent implements LookupListener, SaveButtonDisablerOnValidationFailure {

    //protected InputOutput io;
    protected ProgramBean actualProgram;

    RestClientCruise cruiseClient;
    //RestClientProgram programClient;

    Set<? extends ICruise> cruises;

    protected SingletonResult<CurrentVessel, IVessel> currentVesselResult;
    protected SingletonResult<CurrentCruise, ICruise> currentCruiseResult;

    ComboBoxColumnEditor projectCountryList;
    ComboBoxColumnEditor projectOrganisationList;
    ComboBoxColumnEditor projectList;

    ProjectTableModel projectTableModel;

    public ValidationGroup getValidationGroup() {
        return group;
    }

    protected abstract void __initComponents();

    public AbstractProgramTopComponent() {
        __initComponents();

        try {
            cruiseClient = new RestClientCruise();
            // programClient = new RestClientProgram();
        } catch (ConnectException ex) {
            Messaging.report("Note that the webservices are offline. The program can't be saved or edited.", ex, this.getClass(), true);
        } catch (EarsException ex) {
            Messaging.report(ex.getMessage(), ex, this.getClass(), true);
        }

        currentVesselResult = new SingletonResult(CurrentVessel.class, this);
        currentCruiseResult = new SingletonResult(CurrentCruise.class, this);

        if (getCurrentVessel() != null && cruiseClient != null) {
            cruises = new TreeSet(cruiseClient.getCruiseByPlatform(getCurrentVessel().getConcept()));
        }

    }

    /*protected CurrentCruise getCurrentCruise() {
     if (currentCruiseResult.allInstances().size() > 0) {
     return ((CurrentCruise) new ArrayList(currentCruiseResult.allInstances()).get(0));
     }
     return null;
     }*/
    protected CurrentVessel getCurrentVessel() {
        if (currentVesselResult.allInstances().size() > 0) {
            return ((CurrentVessel) new ArrayList(currentVesselResult.allInstances()).get(0));
        }
        return null;
    }

    protected void cruiseComboBoxActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

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
        collateCentreListSecondary.removeAllItems();
        collateCentreListSecondary.addItem("Choose organisation");
        Object o = collateCentreListPrincipal.getSelectedItem();
        if (o != null && o instanceof CountryBean) {
            CountryBean selectedCountry = (CountryBean) o;
            for (IOrganisation org : StaticMetadataSearcher.getInstance().getOrganisations(true)) {
                if (org.getCountryObject().equals(selectedCountry)) {
                    collateCentreListSecondary.addItem(org);
                }
            }
        }
    }

    protected void collateCentreListSecondaryActionPerformedP(java.awt.event.ActionEvent evt) {
        Object o = collateCentreListSecondary.getSelectedItem();
        if (o != null && o instanceof OrganisationBean) {
            OrganisationBean selectedOrganisation = (OrganisationBean) o;
            o_collateCentreResult.setText(selectedOrganisation.getCode());
        } else if (o instanceof String) {
            o_collateCentreResult.setText("");
        }
    }

    protected void o_collateCentreResultActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    protected void o_piNameActionPerformedP(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    protected javax.swing.JButton addProject;
    protected javax.swing.JComboBox collateCentreListPrincipal;
    protected javax.swing.JComboBox collateCentreListSecondary;
    protected javax.swing.JComboBox cruiseComboBox;
    protected javax.swing.JPanel jPanel1;
    protected javax.swing.JPanel jPanel2;
    protected javax.swing.JPanel jPanelCollateCentre;
    protected javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JScrollPane jScrollPane2;
    protected javax.swing.JScrollPane jScrollPane3;
    protected javax.swing.JTextField o_collateCentreResult;
    protected javax.swing.JTextArea o_description;
    protected javax.swing.JTextField o_piName;
    protected javax.swing.JTextField o_programId_Attribut;
    protected javax.swing.JPanel programIdentifier;
    protected javax.swing.JPanel programIdentifier1;
    protected javax.swing.JPanel programIdentifier3;
    protected javax.swing.JPanel programIdentifier4;
    protected javax.swing.JTable projectEdmerpTable;
    protected javax.swing.JButton removeProject;

    protected org.netbeans.validation.api.ui.swing.ValidationPanel validationPanel1;

    protected ValidationGroup group = null;

    // End of variables declaration                   
    @Override
    public void componentOpened() {
        collateCentreListPrincipal.addItem("Choose country to limit organisation");

        DefaultComboBoxModel collateCentreListPrincipalModel = new DefaultComboBoxModel();
        collateCentreListPrincipal.setModel(collateCentreListPrincipalModel);

        for (OrganisationBean organisation : StaticMetadataSearcher.getInstance().getOrganisations(true)) {
            collateCentreListSecondary.addItem(organisation);
            SwingUtils.addToComboBox(collateCentreListPrincipalModel, organisation.getCountryObject());
        }

        if (getCurrentVessel() != null && cruiseClient != null) {
            cruises = new TreeSet(cruiseClient.getCruiseByPlatform(getCurrentVessel().getConcept()));
        }

        /* if (cruises.size() == 1) {
         cruiseComboBox.setSelectedItem();
         }*/
        projectTableModel = (ProjectTableModel) projectEdmerpTable.getModel();
        projectTableModel.setTable(projectEdmerpTable);
        setUpProjectColumn();

        cruiseComboBox.setName("Cruise name");
        o_programId_Attribut.setName("Program name (identifier)");
        o_piName.setName("Principal investigator name");
        collateCentreListSecondary.setName("Collate centre");
        o_description.setName("Description");

        cruiseComboBox.setEditable(true);
        o_programId_Attribut.setEditable(true);
        o_piName.setEditable(true);
        o_description.setEditable(true);
        collateCentreListPrincipal.setEditable(true);
        collateCentreListSecondary.setEditable(true);

        AutoCompleteDecorator.decorate(cruiseComboBox);
        AutoCompleteDecorator.decorate(collateCentreListSecondary);

        group.add(cruiseComboBox, StringValidators.REQUIRE_NON_EMPTY_STRING);
        group.add(o_programId_Attribut, StringValidators.REQUIRE_NON_EMPTY_STRING);
        group.add(o_piName, StringValidators.REQUIRE_NON_EMPTY_STRING);
        group.add(collateCentreListSecondary, new OrganisationValidator());
        //associated project

        enableThatButtonGreysOutOnValidationFailure((JTextField) cruiseComboBox.getEditor().getEditorComponent(), group);
        enableThatButtonGreysOutOnValidationFailure(o_programId_Attribut, group);
        enableThatButtonGreysOutOnValidationFailure(o_piName, group);
        enableThatButtonGreysOutOnValidationFailure((JTextField) collateCentreListSecondary.getEditor().getEditorComponent(), group);

        String scrollSpeedString = org.openide.util.NbBundle.getMessage(AbstractCruiseTopComponent.class, "AbstractCruiseTopComponent.ScrollSpeed");
        int scrollSpeed = Integer.parseInt(scrollSpeedString);
        SwingUtils.setScrollSpeed(jScrollPane1, scrollSpeed);
        SwingUtils.setScrollSpeed(jScrollPane2, scrollSpeed);
        SwingUtils.setScrollSpeed(jScrollPane3, scrollSpeed);

        cruiseComboBox.setMaximumRowCount(SwingUtils.COMBOBOX_MAX_ROW_COUNT);
        collateCentreListSecondary.setMaximumRowCount(SwingUtils.COMBOBOX_MAX_ROW_COUNT);
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
        projectCountryList = new ComboBoxColumnEditor(countries, projectEdmerpTable, ProjectTableModel.findCol(ProjectTableModel.COUNTRY), "You can choose a country to narrow down organisations.", this);
        projectOrganisationList = new ComboBoxColumnEditor(null, projectEdmerpTable, ProjectTableModel.findCol(ProjectTableModel.ORG), "Choose an organisation.", this);
        projectList = new ComboBoxColumnEditor(null, projectEdmerpTable, ProjectTableModel.findCol(ProjectTableModel.NAME), "Choose a project.", this);
    }

    @Override
    public void resultChanged(LookupEvent le) {
        if (currentVesselResult.matches(le)) {
            if (getCurrentVessel() != null && cruiseClient != null) {
                cruises = new TreeSet(cruiseClient.getCruiseByPlatform(getCurrentVessel().getConcept()));
            }
            populateCruiseCombobox();
        }
    }

    /**
     * *
     * Instantiate the cruisecombobox if needed and populate or repopulate it.
     */
    protected void populateCruiseCombobox() {
        if (cruiseComboBox == null) {
            cruiseComboBox = new JComboBox();
        }
        if (cruiseComboBox.getItemCount() > 0) {
            cruiseComboBox.removeAllItems();
        }
        if (cruises != null) {
            if (cruises.size() > 0) {
                for (ICruise cruise : cruises) {
                    cruiseComboBox.addItem(cruise);
                }
            } else {
                cruiseComboBox.addItem("No cruises for selected vessel");
            }
        }

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
                Messaging.report("Posting the new cruise to the webservice failed because it is offline or its url is incorrect.", ex, this.getClass(), true);
            } catch (EarsException ex) {
                Messaging.report(ex.getMessage(), ex, this.getClass(), true);
            }
            if (client != null) {
                response = client.postProgram(actualProgram);
                progr.progress("Program info sent");

                if (response.isBad()) {
                    Messaging.report("Posting the new program to the webservice failed: " + response.getSummary(), Message.State.BAD, this.getClass(), true);
                } else {
                    Messaging.report(response.getSummary(), Message.State.GOOD, this.getClass(), true);
                }
            }
        }
    }

    protected boolean formValidates() {
        Problem validateAll = group.performValidation();
        return !(validateAll != null && validateAll.isFatal());
    }

    public ProgramBean createProgramFromInput() {
        ProgramBean program = new ProgramBean();

        program.setCruiseId(((CruiseBean) cruiseComboBox.getSelectedItem()).getRealId());
        program.setProgramId(o_programId_Attribut.getText());
        program.setOriginatorCode(o_collateCentreResult.getText());
        program.setDescription(o_description.getText());
        program.setPiName(o_piName.getText());

        program.setProjects(projectTableModel.getEntitiesSet());
        return program;
    }

}
