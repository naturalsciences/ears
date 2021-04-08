/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.SeaAreaTableModel;
import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.ChiefScientistTableModel;
import be.naturalsciences.bmdc.ears.base.StaticMetadataSearcher;
import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.IHarbour;
import be.naturalsciences.bmdc.ears.entities.IOrganisation;
import be.naturalsciences.bmdc.ears.entities.IResponseMessage;
import be.naturalsciences.bmdc.ears.entities.ISeaArea;
import be.naturalsciences.bmdc.ears.entities.IVessel;
import be.naturalsciences.bmdc.ears.entities.SeaAreaBean;
import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.ProgramTableModel;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ears.utils.TaskListener;
import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//be.ac.mumm//EidtCruiseSetup//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "EditCruiseTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER//ys
)

@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "be.naturalsciences.bmdc.ears.topcomponents.EditCruiseTopComponent")
/* ys@ActionReference(path = "Menu/Window", position = 7)
 */

@TopComponent.OpenActionRegistration(
        displayName = "#CTL_EditCruiseSetupAction",
        preferredID = "EditCruiseTopComponent"
)
@Messages({
    "CTL_EditCruiseSetupAction=Edit Cruise",
    "CTL_EditCruiseTopComponent=Edit Cruise on ",
    "HINT_EditCruiseTopComponent=This is a Edit Cruise Setup window"
})
public final class EditCruiseTopComponent extends AbstractCruiseTopComponent implements TaskListener {

    private Lookup lookup;

    @Override
    protected void __initComponents() {
        initComponents();
    }

    public EditCruiseTopComponent(CruiseBean cruise) {
        this();
        for (SeaAreaBean sea : cruise.getSeaAreas()) {
            if (sea != null) {
                ISeaArea foundSea = StaticMetadataSearcher.getInstance().getSeaArea(sea.getCode());
                if (foundSea != null) {
                    sea.setName(foundSea.getName());
                }
            }
        }
        lookup = Lookups.singleton(cruise);
        this.actualCruise = cruise;
    }

    private EditCruiseTopComponent() {
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
        super.programTable = programTable;
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

    /*private EditCruiseTopComponent() {
     initComponents();
     setName(Bundle.CTL_CreateCruiseTopComponent());

     setToolTipText(Bundle.HINT_CreateCruiseTopComponent());
     //  putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);
     instance = this; //ys01
     }*/
    @Override
    public Lookup getLookup() {
        return lookup;
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
        jPanelArrivalHarbor = new javax.swing.JPanel();
        arrivalHarborListPrincipal = new javax.swing.JComboBox();
        arrivalHarborListSecondary = new javax.swing.JComboBox();
        arrivalHarborResult = new javax.swing.JTextField();
        jPanelObjectives = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        o_objectiveValue = new javax.swing.JTextArea();
        jPanelDate = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        inputStartDate = new be.naturalsciences.bmdc.ears.topcomponents.DateTimePicker();
        jLabel10 = new javax.swing.JLabel();
        inputEndDate = new be.naturalsciences.bmdc.ears.topcomponents.DateTimePicker();
        jPanelPlatform = new javax.swing.JPanel();
        platformCodeListPrincipal = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        platformCodeResult = new javax.swing.JTextField();
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
        bt_editCruise = new javax.swing.JButton();
        validationPanel1 = new org.netbeans.validation.api.ui.swing.ValidationPanel();
        programPanel = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        programTable = new javax.swing.JTable();
        addProgramBtn = new javax.swing.JButton();
        removeProgramBtn = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setAutoscrolls(true);
        setLayout(new java.awt.CardLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 1366));

        jPanelInformation.setBackground(new java.awt.Color(255, 255, 255));
        jPanelInformation.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.jPanelInformation.border.title"))); // NOI18N
        jPanelInformation.setOpaque(false);

        cruiseNameValue.setEditable(false);
        cruiseNameValue.setText(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.cruiseNameValue.text")); // NOI18N
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

        javax.swing.GroupLayout jPanelInformationLayout = new javax.swing.GroupLayout(jPanelInformation);
        jPanelInformation.setLayout(jPanelInformationLayout);
        jPanelInformationLayout.setHorizontalGroup(
            jPanelInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInformationLayout.createSequentialGroup()
                .addComponent(cruiseNameValue, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 510, Short.MAX_VALUE))
        );
        jPanelInformationLayout.setVerticalGroup(
            jPanelInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cruiseNameValue, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanelArrivalHarbor.setBackground(new java.awt.Color(255, 255, 255));
        jPanelArrivalHarbor.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.jPanelArrivalHarbor.border.title"))); // NOI18N
        jPanelArrivalHarbor.setOpaque(false);

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

        arrivalHarborResult.setText(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.arrivalHarborResult.text")); // NOI18N
        arrivalHarborResult.setEnabled(false);
        arrivalHarborResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrivalHarborResultActionPerformed(evt);
            }
        });

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
                .addComponent(arrivalHarborResult, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelArrivalHarborLayout.setVerticalGroup(
            jPanelArrivalHarborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelArrivalHarborLayout.createSequentialGroup()
                .addGroup(jPanelArrivalHarborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(arrivalHarborListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(arrivalHarborListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(arrivalHarborResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanelObjectives.setBackground(new java.awt.Color(255, 255, 255));
        jPanelObjectives.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.jPanelObjectives.border.title"))); // NOI18N
        jPanelObjectives.setOpaque(false);

        o_objectiveValue.setColumns(20);
        o_objectiveValue.setRows(5);
        jScrollPane2.setViewportView(o_objectiveValue);

        javax.swing.GroupLayout jPanelObjectivesLayout = new javax.swing.GroupLayout(jPanelObjectives);
        jPanelObjectives.setLayout(jPanelObjectivesLayout);
        jPanelObjectivesLayout.setHorizontalGroup(
            jPanelObjectivesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelObjectivesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelObjectivesLayout.setVerticalGroup(
            jPanelObjectivesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelObjectivesLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanelDate.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDate.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.jPanelDate.border.title"))); // NOI18N
        jPanelDate.setOpaque(false);
        jPanelDate.setPreferredSize(new java.awt.Dimension(740, 45));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.jLabel9.text")); // NOI18N
        jPanelDate.add(jLabel9);

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
        jPanelDate.add(inputStartDate);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.jLabel10.text")); // NOI18N
        jPanelDate.add(jLabel10);

        inputEndDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                inputEndDatePropertyChange(evt);
            }
        });
        jPanelDate.add(inputEndDate);

        jPanelPlatform.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPlatform.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.jPanelPlatform.border.title"))); // NOI18N
        jPanelPlatform.setOpaque(false);
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

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.jLabel7.text")); // NOI18N

        platformCodeResult.setBackground(new java.awt.Color(240, 240, 240));
        platformCodeResult.setText(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.platformCodeResult.text")); // NOI18N
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
                .addGap(18, 18, 18)
                .addComponent(platformCodeResult, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(266, Short.MAX_VALUE))
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

        jPanelStartingHarbor.setBackground(new java.awt.Color(255, 255, 255));
        jPanelStartingHarbor.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.jPanelStartingHarbor.border.title"))); // NOI18N
        jPanelStartingHarbor.setOpaque(false);
        jPanelStartingHarbor.setPreferredSize(new java.awt.Dimension(740, 65));

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

        startingHarborResult.setText(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.startingHarborResult.text")); // NOI18N
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
                .addComponent(startingHarborResult)
                .addContainerGap())
        );
        jPanelStartingHarborLayout.setVerticalGroup(
            jPanelStartingHarborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStartingHarborLayout.createSequentialGroup()
                .addGroup(jPanelStartingHarborLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startingHarborListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startingHarborListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startingHarborResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.jPanel4.border.title"))); // NOI18N
        jPanel4.setOpaque(false);

        org.openide.awt.Mnemonics.setLocalizedText(addSeaArea, org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.addSeaArea.text")); // NOI18N
        addSeaArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSeaAreaActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeSeaArea, org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.removeSeaArea.text")); // NOI18N
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 238, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addSeaArea, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeSeaArea, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(addSeaArea)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(removeSeaArea)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.jPanel5.border.title"))); // NOI18N
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(740, 249));

        chiefScientistTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        chiefScientistTable.setModel(new ChiefScientistTableModel());
        chiefScientistTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chiefScientistTableFocusLost(evt);
            }
        });
        jScrollPane7.setViewportView(chiefScientistTable);

        org.openide.awt.Mnemonics.setLocalizedText(addChiefScientist, org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.addChiefScientist.text")); // NOI18N
        addChiefScientist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addChiefScientistActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeChiefScientist, org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.removeChiefScientist.text")); // NOI18N
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
                .addComponent(jScrollPane7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(removeChiefScientist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addChiefScientist, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(addChiefScientist)
                        .addGap(18, 18, 18)
                        .addComponent(removeChiefScientist))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanelCollateCentre.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCollateCentre.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.jPanelCollateCentre.border.title"))); // NOI18N
        jPanelCollateCentre.setOpaque(false);
        jPanelCollateCentre.setPreferredSize(new java.awt.Dimension(740, 107));

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

        collateCentreResult.setText(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.collateCentreResult.text")); // NOI18N
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
                    .addComponent(collateCentreListSecondary, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelCollateCentreLayout.createSequentialGroup()
                        .addGroup(jPanelCollateCentreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(collateCentreListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(collateCentreResult, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelCollateCentreLayout.setVerticalGroup(
            jPanelCollateCentreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCollateCentreLayout.createSequentialGroup()
                .addComponent(collateCentreListPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collateCentreListSecondary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collateCentreResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(bt_editCruise, org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.bt_editCruise.text")); // NOI18N
        bt_editCruise.setFocusable(false);
        bt_editCruise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_editCruiseActionPerformed(evt);
            }
        });

        programPanel.setBackground(new java.awt.Color(255, 255, 255));
        programPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.programPanel.border.title"))); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(addProgramBtn, org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.addProgramBtn.text")); // NOI18N
        addProgramBtn.setActionCommand(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.addProgramBtn.actionCommand")); // NOI18N
        addProgramBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProgramBtnActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeProgramBtn, org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.removeProgramBtn.text")); // NOI18N
        removeProgramBtn.setActionCommand(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.removeProgramBtn.actionCommand")); // NOI18N
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelArrivalHarbor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelObjectives, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelPlatform, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addComponent(validationPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(bt_editCruise, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanelInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanelDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanelStartingHarbor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(programPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanelCollateCentre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(254, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bt_editCruise)
                    .addComponent(validationPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanelDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(programPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanelCollateCentre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanelStartingHarbor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanelArrivalHarbor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanelPlatform, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanelObjectives, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel4.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.jPanel4.AccessibleContext.accessibleName")); // NOI18N
        programPanel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(EditCruiseTopComponent.class, "EditCruiseTopComponent.programPanel.AccessibleContext.accessibleName")); // NOI18N

        jScrollPane1.setViewportView(jPanel1);

        add(jScrollPane1, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void cruiseNameValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cruiseNameValueFocusLost

    }//GEN-LAST:event_cruiseNameValueFocusLost

    private void arrivalHarborListSecondaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrivalHarborListSecondaryActionPerformed
        super.arrivalHarborListSecondaryActionPerformedP(evt);

        /*if (arrivalHarborListSecondary.getSelectedIndex() >= 0) {
         for (String key : mapLabelKeyC38.keySet()) {
         if (arrivalHarborListSecondary.getSelectedItem().toString().equals(key)) {
         //  System.out.println(key + " :: " + mapOrgNameKey.get(key));
         arrivalHarborResult.setText(mapLabelKeyC38.get(key));
         }
                
         }
         }*/
    }//GEN-LAST:event_arrivalHarborListSecondaryActionPerformed

    private void collateCentreListPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_collateCentreListPrincipalActionPerformed
        super.collateCentreListPrincipalActionPerformedP(evt);
    }//GEN-LAST:event_collateCentreListPrincipalActionPerformed

    private void collateCentreListSecondaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_collateCentreListSecondaryActionPerformed
        super.collateCentreListSecondaryActionPerformedP(evt);
    }//GEN-LAST:event_collateCentreListSecondaryActionPerformed

    private void startingHarborListPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startingHarborListPrincipalActionPerformed
        super.startingHarborListPrincipalActionPerformedP(evt);
    }//GEN-LAST:event_startingHarborListPrincipalActionPerformed

    private void startingHarborListSecondaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startingHarborListSecondaryActionPerformed
        super.startingHarborListSecondaryActionPerformedP(evt);
    }//GEN-LAST:event_startingHarborListSecondaryActionPerformed

    private void arrivalHarborListPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrivalHarborListPrincipalActionPerformed
        super.arrivalHarborListPrincipalActionPerformedP(evt);
    }//GEN-LAST:event_arrivalHarborListPrincipalActionPerformed

    private void addSeaAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSeaAreaActionPerformed
        super.addSeaAreaActionPerformedP(evt);
    }//GEN-LAST:event_addSeaAreaActionPerformed

    private void removeSeaAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSeaAreaActionPerformed
        super.removeSeaAreaActionPerformedP(evt);
    }//GEN-LAST:event_removeSeaAreaActionPerformed

    private void addChiefScientistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addChiefScientistActionPerformed
        super.addChiefScientistActionPerformedP(evt);
    }//GEN-LAST:event_addChiefScientistActionPerformed

    private void removeChiefScientistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeChiefScientistActionPerformed
        super.removeChiefScientistActionPerformedP(evt);

    }//GEN-LAST:event_removeChiefScientistActionPerformed

    private void collateCentreResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_collateCentreResultActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_collateCentreResultActionPerformed

    private void cruiseNameValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cruiseNameValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cruiseNameValueActionPerformed

    private void cruiseNameValueKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cruiseNameValueKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_cruiseNameValueKeyTyped

    private void cruiseNameValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cruiseNameValueKeyReleased

    }//GEN-LAST:event_cruiseNameValueKeyReleased

    private void bt_editCruiseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_editCruiseActionPerformed
        // TODO add your handling code here:
        postCruise();
    }//GEN-LAST:event_bt_editCruiseActionPerformed

    private void chiefScientistTableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chiefScientistTableFocusLost
        // TODO add your handling code here:


    }//GEN-LAST:event_chiefScientistTableFocusLost

    private void arrivalHarborResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrivalHarborResultActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_arrivalHarborResultActionPerformed

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

    private void inputStartDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputStartDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputStartDateActionPerformed

    private void inputStartDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_inputStartDatePropertyChange
        // TODO add your handling code here:
        super.inputStartDatePropertyChangeP(evt);
    }//GEN-LAST:event_inputStartDatePropertyChange

    private void inputEndDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_inputEndDatePropertyChange
        // TODO add your handling code here:
        super.inputEndDatePropertyChangeP(evt);
    }//GEN-LAST:event_inputEndDatePropertyChange

    private void programTableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_programTableFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_programTableFocusLost

    private void addProgramBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProgramBtnActionPerformed
        // TODO add your handling code here:
        super.addProgramActionPerformedP(evt);
    }//GEN-LAST:event_addProgramBtnActionPerformed

    private void removeProgramBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeProgramBtnActionPerformed
        // TODO add your handling code here:
        super.removeProgramActionPerformedP(evt);
    }//GEN-LAST:event_removeProgramBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addChiefScientist;
    private javax.swing.JButton addProgramBtn;
    private javax.swing.JButton addSeaArea;
    private javax.swing.JComboBox arrivalHarborListPrincipal;
    private javax.swing.JComboBox arrivalHarborListSecondary;
    private javax.swing.JTextField arrivalHarborResult;
    private javax.swing.JButton bt_editCruise;
    private javax.swing.JTable chiefScientistTable;
    private javax.swing.JComboBox collateCentreListPrincipal;
    private javax.swing.JComboBox collateCentreListSecondary;
    private javax.swing.JTextField collateCentreResult;
    private javax.swing.JTextField cruiseNameValue;
    private be.naturalsciences.bmdc.ears.topcomponents.DateTimePicker inputEndDate;
    private be.naturalsciences.bmdc.ears.topcomponents.DateTimePicker inputStartDate;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTextArea o_objectiveValue;
    private javax.swing.JComboBox platformCodeListPrincipal;
    private javax.swing.JTextField platformCodeResult;
    private javax.swing.JPanel programPanel;
    private javax.swing.JTable programTable;
    private javax.swing.JButton removeChiefScientist;
    private javax.swing.JButton removeProgramBtn;
    private javax.swing.JButton removeSeaArea;
    private javax.swing.JTable seaAreaTable;
    private javax.swing.JComboBox startingHarborListPrincipal;
    private javax.swing.JComboBox startingHarborListSecondary;
    private javax.swing.JTextField startingHarborResult;
    private org.netbeans.validation.api.ui.swing.ValidationPanel validationPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    protected void componentShowing() {
        //super.componentShowing(); //To change body of generated methods, choose Tools | Templates.
        //05042016  platformCodeListPrincipal.setSelectedItem(vesselProperties.getVesselInformation());
        //YS  setName(Bundle.CTL_CreateCruiseTopComponent() + vesselProperties.getVesselInformation());
        //ys ?? setName(vesselProperties.getVesselInformation());

    }

    @Override
    public void componentOpened() {
        group = validationPanel1.getValidationGroup();
        super.componentOpened();

        bt_editCruise.setEnabled(false);
        //   cruiseIdentifierValue.setText(actualCruise.getIdentifier());
        cruiseNameValue.setEditable(false);
        cruiseNameValue.setText(actualCruise.getName());
        inputStartDate.setFormats(StringUtils.SDF_ISO_DATETIME);
        inputEndDate.setFormats(StringUtils.SDF_ISO_DATETIME);
        inputStartDate.setDate(actualCruise.getdStartDate());
        inputEndDate.setDate(actualCruise.getdEndDate());

        collateCentreResult.setText(actualCruise.getCollateCentre());
        startingHarborResult.setText(actualCruise.getDepartureHarbour());
        arrivalHarborResult.setText(actualCruise.getArrivalHarbor());

        platformCodeResult.setText(actualCruise.getPlatform());
        o_objectiveValue.setText(actualCruise.getObjectives());

        IOrganisation organisation = StaticMetadataSearcher.getInstance().getOrganisation(actualCruise.getCollateCentre());
        IHarbour startingHarbour = StaticMetadataSearcher.getInstance().getHarbour(actualCruise.getDepartureHarbour());
        IHarbour arrivalHarbour = StaticMetadataSearcher.getInstance().getHarbour(actualCruise.getArrivalHarbor());
        IVessel vessel = StaticMetadataSearcher.getInstance().getVessel(actualCruise.getPlatform());

        if (organisation != null) {
            collateCentreListPrincipal.setSelectedItem(organisation.getCountryObject());
            collateCentreListSecondary.setSelectedItem(organisation);

        }
        if (startingHarbour != null) {
            startingHarborListPrincipal.setSelectedItem(startingHarbour.getCountryObject());
            startingHarborListSecondary.setSelectedItem(startingHarbour);
        }
        if (arrivalHarbour != null) {
            arrivalHarborListPrincipal.setSelectedItem(arrivalHarbour.getCountryObject());
            arrivalHarborListSecondary.setSelectedItem(arrivalHarbour);
        }
        if (vessel != null) {
            platformCodeListPrincipal.setSelectedItem(vessel);
        }

        chiefScientistModel.addPersons(actualCruise.getChiefScientists());
        seaAreaModel.addEntities(actualCruise.getSeaAreas());
        programModel.addEntities(actualCruise.getPrograms());
        setUpChiefScientistList();
        setUpSeaAreaList();
        setUpProgramList();
    }

    @Override
    public void componentClosed() {
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
            actualCruise = createCruiseFromInput(actualCruise);
            if (actualCruise != null) {
                progr = ProgressHandleFactory.createHandle("Sending cruise info...");
                IResponseMessage response = null;
                AbstractCruiseTopComponent.CruiseModifier tsk = new CreateCruiseTopComponent.CruiseModifier(progr, response);
                tsk.addListener(this);

                RequestProcessor.getDefault().post(tsk);

            }
        } else {
            Messaging.report("Form not valid. Verify it first", Message.State.BAD, this.getClass(), true);
        }
    }

    @Override
    public void checkValidation() {
        if (!formValidates()) {
            bt_editCruise.setEnabled(false);

        } else {
            bt_editCruise.setEnabled(true);
        }
    }

    @Override
    public void threadComplete(Runnable runner) {
        progr.finish();
    }

}
