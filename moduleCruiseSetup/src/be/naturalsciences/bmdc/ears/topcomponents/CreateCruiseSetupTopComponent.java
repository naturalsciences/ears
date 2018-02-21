/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.SeaAreaTableModel;
import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.ChiefScientistTableModel;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.infobar.InfoBar;
import be.naturalsciences.bmdc.ears.netbeans.services.GlobalActionContextProxy;
import be.naturalsciences.bmdc.ears.utils.DateUtilities;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ears.utils.TaskListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//be.ac.mumm//CreateCruiseSetup//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "CreateCruiseSetupTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER//ys
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "be.naturalsciences.bmdc.ears.topcomponents.CreateCruiseSetupTopComponent")
@ActionReferences({
    // @ActionReference(path = "Toolbars/Window", position = 3333,name = "Create new cruise"),
    @ActionReference(path = "Menu/Window/Cruise & program setup", position = 1)
})

@TopComponent.OpenActionRegistration(
        displayName = "#CTL_CreateCruiseSetupAction",
        preferredID = "CreateCruiseSetupTopComponent"
)
@Messages({
    "CTL_CreateCruiseSetupAction=Create new cruise...",
    "CTL_CreateCruiseSetupTopComponent=Create new cruise",
    "HINT_CreateCruiseSetupTopComponent=Create a new program by choosing scientist, harbour,..."
})
public class CreateCruiseSetupTopComponent extends AbstractCruiseTopComponent implements TaskListener {

    public CreateCruiseSetupTopComponent() {
        super();
        setName(Bundle.CTL_CreateCruiseSetupTopComponent());
        setToolTipText(Bundle.HINT_CreateCruiseSetupTopComponent());
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);

        super.addChiefScientist = addChiefScientist;
        super.addSeaArea = addSeaArea;
        super.arrivalHarborListPrincipal = arrivalHarborListPrincipal;
        super.arrivalHarborListSecondary = arrivalHarborListSecondary;
        super.arrivalHarborResult = arrivalHarborResult;
        super.chiefScientistTable = chiefScientistTable;
        super.collateCentreListPrincipal = collateCentreListPrincipal;
        super.collateCentreListSecondary = collateCentreListSecondary;
        super.collateCentreResult = collateCentreResult;
        super.cruiseIdentifier = cruiseIdentifier;
        super.cruiseIdentifierValue = cruiseIdentifierValue;
        super.cruiseNameValue = cruiseNameValue;
        super.inputEndDate = inputEndDate;
        super.inputStartDate = inputStartDate;
        super.jLabel10 = jLabel10;
        super.jLabel2 = jLabel2;
        super.jLabel7 = jLabel7;
        super.jLabel8 = jLabel8;
        super.jLabel9 = jLabel9;
        super.jPanel = jPanel;
        super.jPanel1 = jPanel1;
        super.jPanel5 = jPanel5;
        super.jPanelArrivalHarbor = jPanelArrivalHarbor;
        super.jPanelCollateCentre = jPanelCollateCentre;
        super.jPanelDate = jPanelDate;
        super.jPanelInformation = jPanelInformation;
        super.jPanelObjectives = jPanelObjectives;
        super.jPanelPlatform = jPanelPlatform;
        super.jPanelStartingHarbor = jPanelStartingHarbor;
        super.jScrollPane1 = jScrollPane1;
        super.jScrollPane4 = jScrollPane4;
        super.jScrollPane6 = jScrollPane6;
        super.jScrollPane7 = jScrollPane7;
        super.o_objectiveValue = o_objectiveValue;
        super.platformCodeListPrincipal = platformCodeListPrincipal;
        super.platformCodeResult = platformCodeResult;
        super.removeChiefScientist = removeChiefScientist;
        super.removeSeaArea = removeSeaArea;
        super.saveCruiseButton = saveCruiseButton;
        super.seaAreaTable = seaAreaTable;
        super.startingHarborListPrincipal = startingHarborListPrincipal;
        super.startingHarborListSecondary = startingHarborListSecondary;
        super.startingHarborResult = startingHarborResult;
        super.validationPanel1 = validationPanel1;
    }

    @Override
    protected void __initComponents() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanelInformation = new javax.swing.JPanel();
        cruiseNameValue = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanelArrivalHarbor = new javax.swing.JPanel();
        arrivalHarborListPrincipal = new javax.swing.JComboBox();
        arrivalHarborListSecondary = new javax.swing.JComboBox();
        arrivalHarborResult = new javax.swing.JTextField();
        jPanelObjectives = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        o_objectiveValue = new javax.swing.JTextArea();
        jPanelDate = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        inputStartDate = new be.naturalsciences.bmdc.ears.topcomponents.DateTimePicker();
        inputEndDate = new be.naturalsciences.bmdc.ears.topcomponents.DateTimePicker();
        jPanelPlatform = new javax.swing.JPanel();
        platformCodeListPrincipal = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        platformCodeResult = new javax.swing.JTextField();
        cruiseIdentifier = new javax.swing.JPanel();
        cruiseIdentifierValue = new javax.swing.JTextField();
        jPanelStartingHarbor = new javax.swing.JPanel();
        startingHarborListPrincipal = new javax.swing.JComboBox();
        startingHarborListSecondary = new javax.swing.JComboBox();
        startingHarborResult = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        addSeaArea = new javax.swing.JButton();
        removeSeaArea = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        seaAreaTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        chiefScientistTable = new javax.swing.JTable();
        addChiefScientist = new javax.swing.JButton();
        removeChiefScientist = new javax.swing.JButton();
        jPanelCollateCentre = new javax.swing.JPanel();
        collateCentreListPrincipal = new javax.swing.JComboBox();
        collateCentreListSecondary = new javax.swing.JComboBox();
        collateCentreResult = new javax.swing.JTextField();
        validationPanel1 = new org.netbeans.validation.api.ui.swing.ValidationPanel();
        bt_createCruise = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setLayout(new java.awt.CardLayout());

        jPanelInformation.setBackground(new java.awt.Color(255, 255, 255));
        jPanelInformation.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.jPanelInformation.border.title"))); // NOI18N

        cruiseNameValue.setEditable(false);
        cruiseNameValue.setText(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.cruiseNameValue.text")); // NOI18N
        cruiseNameValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cruiseNameValueFocusLost(evt);
            }
        });
        cruiseNameValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cruiseNameValueActionPerformed(evt);
            }
        });
        cruiseNameValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cruiseNameValueKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cruiseNameValueKeyReleased(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout jPanelInformationLayout = new javax.swing.GroupLayout(jPanelInformation);
        jPanelInformation.setLayout(jPanelInformationLayout);
        jPanelInformationLayout.setHorizontalGroup(
            jPanelInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInformationLayout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cruiseNameValue, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );
        jPanelInformationLayout.setVerticalGroup(
            jPanelInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cruiseNameValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel2))
        );

        jPanelArrivalHarbor.setBackground(new java.awt.Color(255, 255, 255));
        jPanelArrivalHarbor.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.jPanelArrivalHarbor.border.title"))); // NOI18N

        arrivalHarborListPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrivalHarborListPrincipalActionPerformed(evt);
            }
        });

        arrivalHarborListSecondary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrivalHarborListSecondaryActionPerformed(evt);
            }
        });

        arrivalHarborResult.setText(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.arrivalHarborResult.text")); // NOI18N
        arrivalHarborResult.setEnabled(false);

        javax.swing.GroupLayout jPanelArrivalHarborLayout = new javax.swing.GroupLayout(jPanelArrivalHarbor);
        jPanelArrivalHarbor.setLayout(jPanelArrivalHarborLayout);
        jPanelArrivalHarborLayout.setHorizontalGroup(
            jPanelArrivalHarborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelArrivalHarborLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(arrivalHarborListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(arrivalHarborListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(arrivalHarborResult, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanelArrivalHarborLayout.setVerticalGroup(
            jPanelArrivalHarborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelArrivalHarborLayout.createSequentialGroup()
                .addGroup(jPanelArrivalHarborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(arrivalHarborListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(arrivalHarborListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(arrivalHarborResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelObjectives.setBackground(new java.awt.Color(255, 255, 255));
        jPanelObjectives.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.jPanelObjectives.border.title"))); // NOI18N

        o_objectiveValue.setColumns(20);
        o_objectiveValue.setRows(5);
        jScrollPane2.setViewportView(o_objectiveValue);

        javax.swing.GroupLayout jPanelObjectivesLayout = new javax.swing.GroupLayout(jPanelObjectives);
        jPanelObjectives.setLayout(jPanelObjectivesLayout);
        jPanelObjectivesLayout.setHorizontalGroup(
            jPanelObjectivesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelObjectivesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanelObjectivesLayout.setVerticalGroup(
            jPanelObjectivesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelObjectivesLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanelDate.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDate.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.jPanelDate.border.title"))); // NOI18N
        jPanelDate.setOpaque(false);
        jPanelDate.setPreferredSize(new java.awt.Dimension(740, 45));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.jLabel9.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.jLabel10.text")); // NOI18N

        inputStartDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputStartDateActionPerformed(evt);
            }
        });
        inputStartDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                inputStartDatePropertyChange(evt);
            }
        });

        inputEndDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                inputEndDatePropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanelDateLayout = new javax.swing.GroupLayout(jPanelDate);
        jPanelDate.setLayout(jPanelDateLayout);
        jPanelDateLayout.setHorizontalGroup(
            jPanelDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDateLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(inputStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(inputEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanelDateLayout.setVerticalGroup(
            jPanelDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel9)
                .addComponent(jLabel10)
                .addComponent(inputStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(inputEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanelPlatform.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPlatform.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.jPanelPlatform.border.title"))); // NOI18N
        jPanelPlatform.setPreferredSize(new java.awt.Dimension(740, 67));
        jPanelPlatform.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanelPlatformComponentShown(evt);
            }
        });

        platformCodeListPrincipal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                platformCodeListPrincipalItemStateChanged(evt);
            }
        });
        platformCodeListPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                platformCodeListPrincipalActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.jLabel7.text")); // NOI18N

        platformCodeResult.setBackground(new java.awt.Color(240, 240, 240));
        platformCodeResult.setText(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.platformCodeResult.text")); // NOI18N
        platformCodeResult.setEnabled(false);
        platformCodeResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                platformCodeResultActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelPlatformLayout = new javax.swing.GroupLayout(jPanelPlatform);
        jPanelPlatform.setLayout(jPanelPlatformLayout);
        jPanelPlatformLayout.setHorizontalGroup(
            jPanelPlatformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPlatformLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(platformCodeListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103)
                .addComponent(platformCodeResult, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(175, Short.MAX_VALUE))
        );
        jPanelPlatformLayout.setVerticalGroup(
            jPanelPlatformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPlatformLayout.createSequentialGroup()
                .addGroup(jPanelPlatformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(platformCodeListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(platformCodeResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        cruiseIdentifier.setBackground(new java.awt.Color(255, 255, 255));
        cruiseIdentifier.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.cruiseIdentifier.border.title"))); // NOI18N

        cruiseIdentifierValue.setBackground(new java.awt.Color(240, 240, 240));
        cruiseIdentifierValue.setText(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.cruiseIdentifierValue.text")); // NOI18N
        cruiseIdentifierValue.setEnabled(false);
        cruiseIdentifierValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cruiseIdentifierValueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cruiseIdentifierLayout = new javax.swing.GroupLayout(cruiseIdentifier);
        cruiseIdentifier.setLayout(cruiseIdentifierLayout);
        cruiseIdentifierLayout.setHorizontalGroup(
            cruiseIdentifierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cruiseIdentifierLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cruiseIdentifierValue, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(76, Short.MAX_VALUE))
        );
        cruiseIdentifierLayout.setVerticalGroup(
            cruiseIdentifierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cruiseIdentifierLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cruiseIdentifierValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanelStartingHarbor.setBackground(new java.awt.Color(255, 255, 255));
        jPanelStartingHarbor.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.jPanelStartingHarbor.border.title"))); // NOI18N
        jPanelStartingHarbor.setPreferredSize(new java.awt.Dimension(740, 53));

        startingHarborListPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startingHarborListPrincipalActionPerformed(evt);
            }
        });

        startingHarborListSecondary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startingHarborListSecondaryActionPerformed(evt);
            }
        });

        startingHarborResult.setText(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.startingHarborResult.text")); // NOI18N
        startingHarborResult.setEnabled(false);

        javax.swing.GroupLayout jPanelStartingHarborLayout = new javax.swing.GroupLayout(jPanelStartingHarbor);
        jPanelStartingHarbor.setLayout(jPanelStartingHarborLayout);
        jPanelStartingHarborLayout.setHorizontalGroup(
            jPanelStartingHarborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStartingHarborLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(startingHarborListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(startingHarborListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(startingHarborResult, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelStartingHarborLayout.setVerticalGroup(
            jPanelStartingHarborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStartingHarborLayout.createSequentialGroup()
                .addGroup(jPanelStartingHarborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startingHarborListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startingHarborListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startingHarborResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.jPanel4.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(addSeaArea, org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.addSeaArea.text")); // NOI18N
        addSeaArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSeaAreaActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeSeaArea, org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.removeSeaArea.text")); // NOI18N
        removeSeaArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSeaAreaActionPerformed(evt);
            }
        });

        seaAreaTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        seaAreaTable.setModel(new SeaAreaTableModel());
        jScrollPane6.setViewportView(seaAreaTable);
        seaAreaTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(removeSeaArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addSeaArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(addSeaArea)
                        .addGap(18, 18, 18)
                        .addComponent(removeSeaArea))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.jPanel5.border.title"))); // NOI18N
        jPanel5.setPreferredSize(new java.awt.Dimension(740, 257));

        chiefScientistTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        chiefScientistTable.setModel(new ChiefScientistTableModel());
        chiefScientistTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chiefScientistTableFocusLost(evt);
            }
        });
        jScrollPane7.setViewportView(chiefScientistTable);

        org.openide.awt.Mnemonics.setLocalizedText(addChiefScientist, org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.addChiefScientist.text")); // NOI18N
        addChiefScientist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addChiefScientistActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeChiefScientist, org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.removeChiefScientist.text")); // NOI18N
        removeChiefScientist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeChiefScientistActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(removeChiefScientist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addChiefScientist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(addChiefScientist)
                        .addGap(18, 18, 18)
                        .addComponent(removeChiefScientist)))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanelCollateCentre.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCollateCentre.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.jPanelCollateCentre.border.title"))); // NOI18N
        jPanelCollateCentre.setPreferredSize(new java.awt.Dimension(1024, 53));

        collateCentreListPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                collateCentreListPrincipalActionPerformed(evt);
            }
        });

        collateCentreListSecondary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                collateCentreListSecondaryActionPerformed(evt);
            }
        });

        collateCentreResult.setText(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.collateCentreResult.text")); // NOI18N
        collateCentreResult.setEnabled(false);
        collateCentreResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                collateCentreResultActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCollateCentreLayout = new javax.swing.GroupLayout(jPanelCollateCentre);
        jPanelCollateCentre.setLayout(jPanelCollateCentreLayout);
        jPanelCollateCentreLayout.setHorizontalGroup(
            jPanelCollateCentreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCollateCentreLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCollateCentreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(collateCentreListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelCollateCentreLayout.createSequentialGroup()
                        .addComponent(collateCentreListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, 770, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(collateCentreResult, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelCollateCentreLayout.setVerticalGroup(
            jPanelCollateCentreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCollateCentreLayout.createSequentialGroup()
                .addComponent(collateCentreListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCollateCentreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(collateCentreListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(collateCentreResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(bt_createCruise, org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.bt_editCruise.text")); // NOI18N
        bt_createCruise.setFocusable(false);
        bt_createCruise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_createCruiseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cruiseIdentifier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(validationPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(159, 159, 159)
                        .addComponent(bt_createCruise, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanelObjectives, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanelPlatform, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanelInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanelDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanelArrivalHarbor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanelStartingHarbor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanelCollateCentre, javax.swing.GroupLayout.PREFERRED_SIZE, 970, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bt_createCruise)
                    .addComponent(validationPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cruiseIdentifier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelCollateCentre, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelStartingHarbor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelArrivalHarbor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelPlatform, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelObjectives, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(CreateCruiseSetupTopComponent.class, "EditCruiseSetupTopComponent.jPanel4.AccessibleContext.accessibleName")); // NOI18N

        jScrollPane1.setViewportView(jPanel1);

        add(jScrollPane1, "card2");
    }// </editor-fold>//GEN-END:initComponents


    private void theContainerComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_theContainerComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_theContainerComponentShown

    private void bt_createCruiseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_createCruiseActionPerformed

        postCruise();
    }//GEN-LAST:event_bt_createCruiseActionPerformed

    private void collateCentreResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_collateCentreResultActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_collateCentreResultActionPerformed

    private void collateCentreListSecondaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_collateCentreListSecondaryActionPerformed
        super.collateCentreListSecondaryActionPerformedP(evt);
    }//GEN-LAST:event_collateCentreListSecondaryActionPerformed

    private void collateCentreListPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_collateCentreListPrincipalActionPerformed
        super.collateCentreListPrincipalActionPerformedP(evt);
    }//GEN-LAST:event_collateCentreListPrincipalActionPerformed

    private void removeChiefScientistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeChiefScientistActionPerformed
        super.removeChiefScientistActionPerformedP(evt);
    }//GEN-LAST:event_removeChiefScientistActionPerformed

    private void addChiefScientistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addChiefScientistActionPerformed
        super.addChiefScientistActionPerformedP(evt);
    }//GEN-LAST:event_addChiefScientistActionPerformed

    private void chiefScientistTableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chiefScientistTableFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_chiefScientistTableFocusLost

    private void removeSeaAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSeaAreaActionPerformed
        super.removeSeaAreaActionPerformedP(evt);
    }//GEN-LAST:event_removeSeaAreaActionPerformed

    private void addSeaAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSeaAreaActionPerformed
        super.addSeaAreaActionPerformedP(evt);
    }//GEN-LAST:event_addSeaAreaActionPerformed

    private void startingHarborListSecondaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startingHarborListSecondaryActionPerformed
        super.startingHarborListSecondaryActionPerformedP(evt);
    }//GEN-LAST:event_startingHarborListSecondaryActionPerformed

    private void startingHarborListPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startingHarborListPrincipalActionPerformed
        super.startingHarborListPrincipalActionPerformedP(evt);
    }//GEN-LAST:event_startingHarborListPrincipalActionPerformed

    private void cruiseIdentifierValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cruiseIdentifierValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cruiseIdentifierValueActionPerformed

    private void jPanelPlatformComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanelPlatformComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanelPlatformComponentShown

    private void platformCodeResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_platformCodeResultActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_platformCodeResultActionPerformed

    private void platformCodeListPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_platformCodeListPrincipalActionPerformed
        super.platformCodeListPrincipalActionPerformedP(evt);
    }//GEN-LAST:event_platformCodeListPrincipalActionPerformed

    private void platformCodeListPrincipalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_platformCodeListPrincipalItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_platformCodeListPrincipalItemStateChanged

    private void inputEndDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_inputEndDatePropertyChange
        // TODO add your handling code here:
        super.inputEndDatePropertyChangeP(evt);
    }//GEN-LAST:event_inputEndDatePropertyChange

    private void inputStartDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_inputStartDatePropertyChange
        // TODO add your handling code here:
        super.inputStartDatePropertyChangeP(evt);
    }//GEN-LAST:event_inputStartDatePropertyChange

    private void inputStartDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputStartDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputStartDateActionPerformed

    private void arrivalHarborListSecondaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrivalHarborListSecondaryActionPerformed
        super.arrivalHarborListSecondaryActionPerformedP(evt);
    }//GEN-LAST:event_arrivalHarborListSecondaryActionPerformed

    private void arrivalHarborListPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrivalHarborListPrincipalActionPerformed
        super.arrivalHarborListPrincipalActionPerformedP(evt);
    }//GEN-LAST:event_arrivalHarborListPrincipalActionPerformed

    private void cruiseNameValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cruiseNameValueKeyReleased
        super.cruiseNameValueKeyReleasedP(evt);
    }//GEN-LAST:event_cruiseNameValueKeyReleased

    private void cruiseNameValueKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cruiseNameValueKeyTyped
        // TODO add your handling code here:
        //   System.out.println(cruiseNameValue.getText());
    }//GEN-LAST:event_cruiseNameValueKeyTyped

    private void cruiseNameValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cruiseNameValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cruiseNameValueActionPerformed

    private void cruiseNameValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cruiseNameValueFocusLost
        super.cruiseNameValueFocusLostP(evt);
    }//GEN-LAST:event_cruiseNameValueFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addChiefScientist;
    private javax.swing.JButton addSeaArea;
    private javax.swing.JComboBox arrivalHarborListPrincipal;
    private javax.swing.JComboBox arrivalHarborListSecondary;
    private javax.swing.JTextField arrivalHarborResult;
    private javax.swing.JButton bt_createCruise;
    private javax.swing.JTable chiefScientistTable;
    private javax.swing.JComboBox collateCentreListPrincipal;
    private javax.swing.JComboBox collateCentreListSecondary;
    private javax.swing.JTextField collateCentreResult;
    private javax.swing.JPanel cruiseIdentifier;
    private javax.swing.JTextField cruiseIdentifierValue;
    private javax.swing.JTextField cruiseNameValue;
    private be.naturalsciences.bmdc.ears.topcomponents.DateTimePicker inputEndDate;
    private be.naturalsciences.bmdc.ears.topcomponents.DateTimePicker inputStartDate;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelArrivalHarbor;
    private javax.swing.JPanel jPanelCollateCentre;
    private javax.swing.JPanel jPanelDate;
    private javax.swing.JPanel jPanelInformation;
    private javax.swing.JPanel jPanelObjectives;
    private javax.swing.JPanel jPanelPlatform;
    private javax.swing.JPanel jPanelStartingHarbor;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTextArea o_objectiveValue;
    private javax.swing.JComboBox platformCodeListPrincipal;
    private javax.swing.JTextField platformCodeResult;
    private javax.swing.JButton removeChiefScientist;
    private javax.swing.JButton removeSeaArea;
    private javax.swing.JTable seaAreaTable;
    private javax.swing.JComboBox startingHarborListPrincipal;
    private javax.swing.JComboBox startingHarborListSecondary;
    private javax.swing.JTextField startingHarborResult;
    private org.netbeans.validation.api.ui.swing.ValidationPanel validationPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    protected void componentShowing() {
    }

    @Override
    public void componentOpened() {
        group = validationPanel1.getValidationGroup();
        super.componentOpened();
        bt_createCruise.setEnabled(false);

        cruiseNameValue.setEditable(true);

    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    ProgressHandle progr;

    private void postCruise() {
        if (formValidates()) {
            actualCruise = createCruiseFromInput();
            if (actualCruise != null) {
                progr = ProgressHandleFactory.createHandle("Sending cruise info...");
                IResponseMessage response = null;
                AbstractCruiseTopComponent.CruisePoster tsk = new CreateCruiseSetupTopComponent.CruisePoster(progr, response);
                tsk.addListener(this);

                RequestProcessor.getDefault().post(tsk);
                progr.finish();
            }
        } else {
            Messaging.report("Form not valid. Verify it first", Message.State.BAD, this.getClass(), true);
        }
    }

    @Override
    public void checkValidation() {
        if (!formValidates()) {
            bt_createCruise.setEnabled(false);

        } else {
            bt_createCruise.setEnabled(true);
            cruiseIdentifierValue.setText(DateUtilities.formatDate(inputStartDate.getDate()) + "_" + cruiseNameValue.getText());
        }
    }

    @Override
    public void threadComplete(Runnable runner) {
        try {
            progr.finish();
            if (InfoBar.getInstance().noProblemsLately()) {
                //this.close();
                GlobalActionContextProxy.getInstance().add(currentVesselResult.getCurrent()); //causes the vessel to be changed to itself, causing vessel listeners to update their cruise list
            }
        } catch (AssertionError | IllegalStateException e) {
            int a = 5;
        }
    }

}
