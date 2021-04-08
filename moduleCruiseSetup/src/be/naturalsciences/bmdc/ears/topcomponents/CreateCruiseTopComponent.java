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
import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.ProgramTableModel;
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
        preferredID = "CreateCruiseTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER//ys
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "be.naturalsciences.bmdc.ears.topcomponents.CreateCruiseTopComponent")
@ActionReferences({
    // @ActionReference(path = "Toolbars/Window", position = 3333,name = "Create new cruise"),
    @ActionReference(path = "Menu/Window/Cruise & program setup", position = 1)
})

@TopComponent.OpenActionRegistration(
        displayName = "#CTL_CreateCruiseSetupAction",
        preferredID = "CreateCruiseTopComponent"
)
@Messages({
    "CTL_CreateCruiseSetupAction=New cruise",
    "CTL_CreateCruiseTopComponent=Create new cruise",
    "HINT_CreateCruiseTopComponent=Create a new cruise by choosing scientist, harbour,..."
})
public class CreateCruiseTopComponent extends AbstractCruiseTopComponent implements TaskListener {

    public CreateCruiseTopComponent() {
        super();
        setName(Bundle.CTL_CreateCruiseTopComponent());
        setToolTipText(Bundle.HINT_CreateCruiseTopComponent());
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);

        super.addChiefScientist = addChiefScientist;
        super.addSeaArea = addSeaArea;
        super.arrivalHarborListPrincipal = arrivalHarborListPrincipal;
        super.arrivalHarborListSecondary = arrivalHarborListSecondary;
        super.arrivalHarborResult = arrivalHarborResult;
        super.chiefScientistTable = chiefScientistTable;
        super.programTable=programTable;
        super.collateCentreListPrincipal = collateCentreListPrincipal;
        super.collateCentreListSecondary = collateCentreListSecondary;
        super.collateCentreResult = collateCentreResult;
        super.cruiseIdentifier = cruiseIdentifier;
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
        super.jPanel5 = csPanel;
        super.jPanelArrivalHarbor = arrivalHarborPanel;
        super.jPanelCollateCentre = collateCentrePanel;
        super.jPanelDate = datePanel;
        super.jPanelInformation = namePanel;
        super.jPanelObjectives = objectivesPanel;
        super.jPanelPlatform = platformPanel;
        super.jPanelStartingHarbor = startingHarborPanel;
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
        namePanel = new javax.swing.JPanel();
        cruiseNameValue = new javax.swing.JTextField();
        arrivalHarborPanel = new javax.swing.JPanel();
        arrivalHarborListPrincipal = new javax.swing.JComboBox();
        arrivalHarborListSecondary = new javax.swing.JComboBox();
        arrivalHarborResult = new javax.swing.JTextField();
        objectivesPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        o_objectiveValue = new javax.swing.JTextArea();
        datePanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        inputStartDate = new be.naturalsciences.bmdc.ears.topcomponents.DateTimePicker();
        inputEndDate = new be.naturalsciences.bmdc.ears.topcomponents.DateTimePicker();
        platformPanel = new javax.swing.JPanel();
        platformCodeListPrincipal = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        platformCodeResult = new javax.swing.JTextField();
        startingHarborPanel = new javax.swing.JPanel();
        startingHarborListPrincipal = new javax.swing.JComboBox();
        startingHarborListSecondary = new javax.swing.JComboBox();
        startingHarborResult = new javax.swing.JTextField();
        seaAreaPanel = new javax.swing.JPanel();
        addSeaArea = new javax.swing.JButton();
        removeSeaArea = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        seaAreaTable = new javax.swing.JTable();
        csPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        chiefScientistTable = new javax.swing.JTable();
        addChiefScientist = new javax.swing.JButton();
        removeChiefScientist = new javax.swing.JButton();
        collateCentrePanel = new javax.swing.JPanel();
        collateCentreListPrincipal = new javax.swing.JComboBox();
        collateCentreListSecondary = new javax.swing.JComboBox();
        collateCentreResult = new javax.swing.JTextField();
        validationPanel1 = new org.netbeans.validation.api.ui.swing.ValidationPanel();
        bt_createCruise = new javax.swing.JButton();
        programPanel = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        programTable = new javax.swing.JTable();
        addProgramBtn = new javax.swing.JButton();
        removeProgramBtn = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setLayout(new java.awt.CardLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 1516));

        namePanel.setBackground(new java.awt.Color(255, 255, 255));
        namePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.jPanelInformation.border.title"))); // NOI18N
        namePanel.setOpaque(false);
        namePanel.setPreferredSize(new java.awt.Dimension(695, 45));

        cruiseNameValue.setEditable(false);
        cruiseNameValue.setText(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.cruiseNameValue.text")); // NOI18N
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cruiseNameValueKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cruiseNameValueKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout namePanelLayout = new javax.swing.GroupLayout(namePanel);
        namePanel.setLayout(namePanelLayout);
        namePanelLayout.setHorizontalGroup(
            namePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanelLayout.createSequentialGroup()
                .addComponent(cruiseNameValue, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        namePanelLayout.setVerticalGroup(
            namePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cruiseNameValue, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        arrivalHarborPanel.setBackground(new java.awt.Color(255, 255, 255));
        arrivalHarborPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.jPanelArrivalHarbor.border.title"))); // NOI18N
        arrivalHarborPanel.setOpaque(false);

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

        arrivalHarborResult.setText(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.arrivalHarborResult.text")); // NOI18N
        arrivalHarborResult.setEnabled(false);

        javax.swing.GroupLayout arrivalHarborPanelLayout = new javax.swing.GroupLayout(arrivalHarborPanel);
        arrivalHarborPanel.setLayout(arrivalHarborPanelLayout);
        arrivalHarborPanelLayout.setHorizontalGroup(
            arrivalHarborPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(arrivalHarborPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(arrivalHarborListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(arrivalHarborListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(arrivalHarborResult, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        arrivalHarborPanelLayout.setVerticalGroup(
            arrivalHarborPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(arrivalHarborPanelLayout.createSequentialGroup()
                .addGroup(arrivalHarborPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(arrivalHarborListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(arrivalHarborListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(arrivalHarborResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        objectivesPanel.setBackground(new java.awt.Color(255, 255, 255));
        objectivesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.jPanelObjectives.border.title"))); // NOI18N
        objectivesPanel.setOpaque(false);

        o_objectiveValue.setColumns(20);
        o_objectiveValue.setRows(5);
        jScrollPane2.setViewportView(o_objectiveValue);

        javax.swing.GroupLayout objectivesPanelLayout = new javax.swing.GroupLayout(objectivesPanel);
        objectivesPanel.setLayout(objectivesPanelLayout);
        objectivesPanelLayout.setHorizontalGroup(
            objectivesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, objectivesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        objectivesPanelLayout.setVerticalGroup(
            objectivesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(objectivesPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        datePanel.setBackground(new java.awt.Color(255, 255, 255));
        datePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.jPanelDate.border.title"))); // NOI18N
        datePanel.setOpaque(false);
        datePanel.setPreferredSize(new java.awt.Dimension(740, 45));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.jLabel9.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.jLabel10.text")); // NOI18N

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

        javax.swing.GroupLayout datePanelLayout = new javax.swing.GroupLayout(datePanel);
        datePanel.setLayout(datePanelLayout);
        datePanelLayout.setHorizontalGroup(
            datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(inputStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(inputEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        datePanelLayout.setVerticalGroup(
            datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel9)
                .addComponent(jLabel10)
                .addComponent(inputStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(inputEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        platformPanel.setBackground(new java.awt.Color(255, 255, 255));
        platformPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.jPanelPlatform.border.title"))); // NOI18N
        platformPanel.setOpaque(false);
        platformPanel.setPreferredSize(new java.awt.Dimension(740, 67));
        platformPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                platformPanelComponentShown(evt);
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

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.jLabel7.text")); // NOI18N

        platformCodeResult.setBackground(new java.awt.Color(240, 240, 240));
        platformCodeResult.setText(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.platformCodeResult.text")); // NOI18N
        platformCodeResult.setEnabled(false);
        platformCodeResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                platformCodeResultActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout platformPanelLayout = new javax.swing.GroupLayout(platformPanel);
        platformPanel.setLayout(platformPanelLayout);
        platformPanelLayout.setHorizontalGroup(
            platformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(platformPanelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(platformCodeListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103)
                .addComponent(platformCodeResult, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        platformPanelLayout.setVerticalGroup(
            platformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(platformPanelLayout.createSequentialGroup()
                .addGroup(platformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(platformCodeListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(platformCodeResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        startingHarborPanel.setBackground(new java.awt.Color(255, 255, 255));
        startingHarborPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.jPanelStartingHarbor.border.title"))); // NOI18N
        startingHarborPanel.setOpaque(false);
        startingHarborPanel.setPreferredSize(new java.awt.Dimension(740, 53));

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

        startingHarborResult.setText(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.startingHarborResult.text")); // NOI18N
        startingHarborResult.setEnabled(false);

        javax.swing.GroupLayout startingHarborPanelLayout = new javax.swing.GroupLayout(startingHarborPanel);
        startingHarborPanel.setLayout(startingHarborPanelLayout);
        startingHarborPanelLayout.setHorizontalGroup(
            startingHarborPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startingHarborPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(startingHarborListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(startingHarborListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(startingHarborResult, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        startingHarborPanelLayout.setVerticalGroup(
            startingHarborPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startingHarborPanelLayout.createSequentialGroup()
                .addGroup(startingHarborPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startingHarborListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startingHarborListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startingHarborResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        seaAreaPanel.setBackground(new java.awt.Color(255, 255, 255));
        seaAreaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.jPanel4.border.title"))); // NOI18N
        seaAreaPanel.setOpaque(false);

        org.openide.awt.Mnemonics.setLocalizedText(addSeaArea, org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.addSeaArea.text")); // NOI18N
        addSeaArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSeaAreaActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeSeaArea, org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.removeSeaArea.text")); // NOI18N
        removeSeaArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSeaAreaActionPerformed(evt);
            }
        });

        seaAreaTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        seaAreaTable.setModel(new SeaAreaTableModel());
        jScrollPane6.setViewportView(seaAreaTable);
        seaAreaTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        javax.swing.GroupLayout seaAreaPanelLayout = new javax.swing.GroupLayout(seaAreaPanel);
        seaAreaPanel.setLayout(seaAreaPanelLayout);
        seaAreaPanelLayout.setHorizontalGroup(
            seaAreaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(seaAreaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6)
                .addGap(18, 18, 18)
                .addGroup(seaAreaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(removeSeaArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addSeaArea, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        seaAreaPanelLayout.setVerticalGroup(
            seaAreaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(seaAreaPanelLayout.createSequentialGroup()
                .addGroup(seaAreaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(seaAreaPanelLayout.createSequentialGroup()
                        .addComponent(addSeaArea)
                        .addGap(18, 18, 18)
                        .addComponent(removeSeaArea))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 31, Short.MAX_VALUE))
        );

        csPanel.setBackground(new java.awt.Color(255, 255, 255));
        csPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.jPanel5.border.title"))); // NOI18N
        csPanel.setOpaque(false);
        csPanel.setPreferredSize(new java.awt.Dimension(740, 257));

        chiefScientistTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        chiefScientistTable.setModel(new ChiefScientistTableModel());
        chiefScientistTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chiefScientistTableFocusLost(evt);
            }
        });
        jScrollPane7.setViewportView(chiefScientistTable);

        org.openide.awt.Mnemonics.setLocalizedText(addChiefScientist, org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.addChiefScientist.text")); // NOI18N
        addChiefScientist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addChiefScientistActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeChiefScientist, org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.removeChiefScientist.text")); // NOI18N
        removeChiefScientist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeChiefScientistActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout csPanelLayout = new javax.swing.GroupLayout(csPanel);
        csPanel.setLayout(csPanelLayout);
        csPanelLayout.setHorizontalGroup(
            csPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(csPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(csPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addChiefScientist, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeChiefScientist))
                .addContainerGap())
        );
        csPanelLayout.setVerticalGroup(
            csPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(csPanelLayout.createSequentialGroup()
                .addGroup(csPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(csPanelLayout.createSequentialGroup()
                        .addComponent(addChiefScientist)
                        .addGap(18, 18, 18)
                        .addComponent(removeChiefScientist))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 36, Short.MAX_VALUE))
        );

        collateCentrePanel.setBackground(new java.awt.Color(255, 255, 255));
        collateCentrePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.jPanelCollateCentre.border.title"))); // NOI18N
        collateCentrePanel.setOpaque(false);
        collateCentrePanel.setPreferredSize(new java.awt.Dimension(1024, 53));

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

        collateCentreResult.setText(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.collateCentreResult.text")); // NOI18N
        collateCentreResult.setEnabled(false);
        collateCentreResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                collateCentreResultActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout collateCentrePanelLayout = new javax.swing.GroupLayout(collateCentrePanel);
        collateCentrePanel.setLayout(collateCentrePanelLayout);
        collateCentrePanelLayout.setHorizontalGroup(
            collateCentrePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(collateCentrePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(collateCentrePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(collateCentreListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(collateCentreResult, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(collateCentreListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, 717, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        collateCentrePanelLayout.setVerticalGroup(
            collateCentrePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(collateCentrePanelLayout.createSequentialGroup()
                .addComponent(collateCentreListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collateCentreListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collateCentreResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(bt_createCruise, org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.bt_editCruise.text")); // NOI18N
        bt_createCruise.setFocusable(false);
        bt_createCruise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_createCruiseActionPerformed(evt);
            }
        });

        programPanel.setBackground(new java.awt.Color(255, 255, 255));
        programPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "CreateCruiseTopComponent.programPanel.border.title"))); // NOI18N
        programPanel.setOpaque(false);
        programPanel.setPreferredSize(new java.awt.Dimension(740, 257));

        chiefScientistTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        programTable.setModel(new ProgramTableModel());
        programTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                programTableFocusLost(evt);
            }
        });
        jScrollPane8.setViewportView(programTable);

        org.openide.awt.Mnemonics.setLocalizedText(addProgramBtn, org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "CreateCruiseTopComponent.addProgramBtn.text")); // NOI18N
        addProgramBtn.setActionCommand(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "CreateCruiseTopComponent.addProgramBtn.actionCommand")); // NOI18N
        addProgramBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProgramBtnActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeProgramBtn, org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "CreateCruiseTopComponent.removeProgramBtn.text")); // NOI18N
        removeProgramBtn.setActionCommand(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "CreateCruiseTopComponent.removeProgramBtn.actionCommand")); // NOI18N
        removeProgramBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeProgramBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout programPanelLayout = new javax.swing.GroupLayout(programPanel);
        programPanel.setLayout(programPanelLayout);
        programPanelLayout.setHorizontalGroup(
            programPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(programPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(programPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addProgramBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeProgramBtn))
                .addContainerGap())
        );
        programPanelLayout.setVerticalGroup(
            programPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(programPanelLayout.createSequentialGroup()
                .addGroup(programPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(programPanelLayout.createSequentialGroup()
                        .addComponent(addProgramBtn)
                        .addGap(18, 18, 18)
                        .addComponent(removeProgramBtn))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(validationPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bt_createCruise, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(objectivesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(arrivalHarborPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                    .addComponent(datePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(collateCentrePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                    .addComponent(startingHarborPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(platformPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(seaAreaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(programPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(csPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(254, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bt_createCruise)
                    .addComponent(validationPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(namePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(datePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(csPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(programPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(collateCentrePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(startingHarborPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(arrivalHarborPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(platformPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(objectivesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(seaAreaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );

        seaAreaPanel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(CreateCruiseTopComponent.class, "EditCruiseTopComponent.jPanel4.AccessibleContext.accessibleName")); // NOI18N

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

    private void platformPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_platformPanelComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_platformPanelComponentShown

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

    private void addProgramBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProgramBtnActionPerformed
        // TODO add your handling code here:
        super.addProgramActionPerformedP(evt);
    }//GEN-LAST:event_addProgramBtnActionPerformed

    private void removeProgramBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeProgramBtnActionPerformed
        // TODO add your handling code here:
        super.removeProgramActionPerformedP(evt);
    }//GEN-LAST:event_removeProgramBtnActionPerformed

    private void programTableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_programTableFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_programTableFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addChiefScientist;
    private javax.swing.JButton addProgramBtn;
    private javax.swing.JButton addSeaArea;
    private javax.swing.JComboBox arrivalHarborListPrincipal;
    private javax.swing.JComboBox arrivalHarborListSecondary;
    private javax.swing.JPanel arrivalHarborPanel;
    private javax.swing.JTextField arrivalHarborResult;
    private javax.swing.JButton bt_createCruise;
    private javax.swing.JTable chiefScientistTable;
    private javax.swing.JComboBox collateCentreListPrincipal;
    private javax.swing.JComboBox collateCentreListSecondary;
    private javax.swing.JPanel collateCentrePanel;
    private javax.swing.JTextField collateCentreResult;
    private javax.swing.JTextField cruiseNameValue;
    private javax.swing.JPanel csPanel;
    private javax.swing.JPanel datePanel;
    private be.naturalsciences.bmdc.ears.topcomponents.DateTimePicker inputEndDate;
    private be.naturalsciences.bmdc.ears.topcomponents.DateTimePicker inputStartDate;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JPanel namePanel;
    private javax.swing.JTextArea o_objectiveValue;
    private javax.swing.JPanel objectivesPanel;
    private javax.swing.JComboBox platformCodeListPrincipal;
    private javax.swing.JTextField platformCodeResult;
    private javax.swing.JPanel platformPanel;
    private javax.swing.JPanel programPanel;
    private javax.swing.JTable programTable;
    private javax.swing.JButton removeChiefScientist;
    private javax.swing.JButton removeProgramBtn;
    private javax.swing.JButton removeSeaArea;
    private javax.swing.JPanel seaAreaPanel;
    private javax.swing.JTable seaAreaTable;
    private javax.swing.JComboBox startingHarborListPrincipal;
    private javax.swing.JComboBox startingHarborListSecondary;
    private javax.swing.JPanel startingHarborPanel;
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
        setUpSeaAreaList();
        setUpChiefScientistList();
        setUpProgramList();
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
                AbstractCruiseTopComponent.CruisePoster tsk = new CreateCruiseTopComponent.CruisePoster(progr, response);
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
          //  cruiseIdentifierValue.setText(DateUtilities.formatDate(inputStartDate.getDate()) + "_" + cruiseNameValue.getText());
        }
    }

    @Override
    public void threadComplete(Runnable runner) {
        try {
            progr.finish();
            if (InfoBar.getInstance().noProblemsLately()) {
                GlobalActionContextProxy.getInstance().add(currentVesselResult.getCurrent()); //causes the vessel to be changed to itself, causing vessel listeners to update their cruise list
            }
        } catch (AssertionError | IllegalStateException e) {
            int a = 5;
        }
    }

}
