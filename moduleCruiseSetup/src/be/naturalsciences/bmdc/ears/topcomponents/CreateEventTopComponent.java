/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.listeners.ExportEventActionListener;
import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.EventTableModel;
import be.naturalsciences.bmdc.ears.comparator.ActorComparator;
import be.naturalsciences.bmdc.ears.entities.Actor;
import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.CurrentActor;
import be.naturalsciences.bmdc.ears.entities.CurrentCruise;
import be.naturalsciences.bmdc.ears.entities.CurrentProgram;
import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.entities.EventBean;
import be.naturalsciences.bmdc.ears.entities.ICruise;
import be.naturalsciences.bmdc.ears.entities.IProgram;
import be.naturalsciences.bmdc.ears.entities.IVessel;
import be.naturalsciences.bmdc.ears.entities.PropertyBean;
import be.naturalsciences.bmdc.ears.listeners.SelectOnClickPopupMenuListener;
import be.naturalsciences.bmdc.ears.netbeans.services.GlobalActionContextProxy;
import be.naturalsciences.bmdc.ears.netbeans.services.SingletonResult;
import be.naturalsciences.bmdc.ears.ontology.Individuals;
import be.naturalsciences.bmdc.ears.ontology.ProgramOntology;
import be.naturalsciences.bmdc.ears.ontology.VesselOntology;
import be.naturalsciences.bmdc.ears.ontology.entities.Property;
import be.naturalsciences.bmdc.ears.ontology.entities.SpecificEventDefinition;
import be.naturalsciences.bmdc.ears.properties.Configs;
import be.naturalsciences.bmdc.ears.rest.RestClientEvent;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ears.utils.SwingUtils;
import be.naturalsciences.bmdc.ears.utils.TableColumnAdjuster;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import com.github.lgooddatepicker.tableeditors.DateTableEditor;
import com.github.lgooddatepicker.tableeditors.TimeTableEditor;
import com.github.lgooddatepicker.zinternaltools.InternalUtilities;
import gnu.trove.map.hash.THashMap;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.PatternSyntaxException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;

;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//be.ac.mumm//Test//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "CreateEventTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "be.naturalsciences.bmdc.ears.topcomponents.CreateEventTopComponent")
@ActionReferences({
    // @ActionReference(path = "Toolbars/Window", position = 3333,name = "Create/edit events"),
    @ActionReference(path = "Menu/Window", position = 1)
})

@TopComponent.OpenActionRegistration(
        displayName = "#CTL_CreateEventAction",
        preferredID = "CreateEventTopComponent"
)
@Messages({
    "CTL_CreateEventAction=Create/edit events...",
    "CTL_CreateEventTopComponent=Create/edit events",
    "HINT_CreateEventTopComponent=Manage events"
})
public final class CreateEventTopComponent extends TopComponent implements LookupListener {

    private final InstanceContent content = new InstanceContent();
    public final static Font DEFAULT_FONT = new Font("Sans Serif", Font.BOLD, 12);
    public final static int DEFAULT_ROW_HEIGHT = 25;

    private TableColumn actorColumn;
    private TableColumn propertyColumn;

    private Lookup.Result<EventBean> eventResult;
    private Lookup.Result<Actor> actorResult;
    private SingletonResult<CurrentVessel, IVessel> currentVesselResult;
    private SingletonResult<CurrentCruise, ICruise> currentCruiseResult;
    private SingletonResult<CurrentProgram, IProgram> currentProgramResult;

    private ExportEventActionListener exportEventActionListener;

    private TableRowSorter<TableModel> eventTableSorter;

    private final Action editPropertyEventAction;

    private static RestClientEvent restClientEvent;

    private EventTableModel model;
    JPopupMenu popupMenu = new JPopupMenu();

    private Map<TableColumn, int[]> columnWidths;

    public CreateEventTopComponent() {
        initComponents();
        try {
            restClientEvent = new RestClientEvent();
        } catch (ConnectException ex) {
            Messaging.report("Note that the webservices are offline. The events can't be retrieved.", ex, this.getClass(), true);
        } catch (EarsException ex) {
            Messaging.report(ex.getMessage(), ex, this.getClass(), true);
        }
        associateLookup(new AbstractLookup(content));
        setName(Bundle.CTL_CreateEventTopComponent());
        setToolTipText(Bundle.HINT_CreateEventTopComponent());

        actorResult = Utilities.actionsGlobalContext().lookupResult(Actor.class);
        actorResult.addLookupListener(this);

        eventResult = Utilities.actionsGlobalContext().lookupResult(EventBean.class);//lookupEvent.lookupResult(EventBean.class);
        eventResult.addLookupListener(this);

        currentVesselResult = new SingletonResult<>(CurrentVessel.class, this);
        currentCruiseResult = new SingletonResult<>(CurrentCruise.class, this);
        currentProgramResult = new SingletonResult<>(CurrentProgram.class, this);

        exportEventActionListener = new ExportEventActionListener();

        eventTable.setRowHeight(DEFAULT_ROW_HEIGHT);
        eventTable.setFont(DEFAULT_FONT);
        eventTable.setFillsViewportHeight(true);

        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem copyItemNow = new JMenuItem("Copy to now");
        JMenuItem deleteItem = new JMenuItem("Delete");

        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] modelRows = SwingUtils.convertSelectedRowIndicesToModel(eventTable);

                for (int i = 0; i < modelRows.length; i++) {
                    EventBean event = ((EventTableModel) eventTable.getModel()).getEntityAt(modelRows[i]).clone();
                    event.setEventId(event.buildEventId());
                    GlobalActionContextProxy.getInstance().addEnsureOne(event);
                }
            }
        });
        copyItemNow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventTable.getSelectedRow();
                int[] modelRows = SwingUtils.convertSelectedRowIndicesToModel(eventTable);

                for (int i = 0; i < modelRows.length; i++) {
                    EventBean event = ((EventTableModel) eventTable.getModel()).getEntityAt(modelRows[i]).clone();

                    event.setEventId(event.buildEventId());
                    event.setTimeStampDt(OffsetDateTime.now());
                    GlobalActionContextProxy.getInstance().addEnsureOne(event);
                }
            }
        });
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = eventTable.getSelectedRows();
                if (selectedRows.length > 0) {
                    int[] modelRows = SwingUtils.convertSelectedRowIndicesToModel(eventTable);
                    String messageBoxHeader = null;
                    StringBuilder messageBoxText = new StringBuilder("<html>");

                    if (selectedRows.length == 1) {
                        messageBoxHeader = "Delete row " + (selectedRows[0] + 1) + "?";
                    } else {
                        messageBoxHeader = "Delete rows " + (Arrays.stream(selectedRows).min().getAsInt() + 1) + "-" + (Arrays.stream(selectedRows).max().getAsInt() + 1 + "?");
                    }
                    messageBoxText.append(messageBoxHeader + ": ");
                    messageBoxText.append("<ul>");
                    for (int i = 0; i < modelRows.length; i++) {
                        EventBean event = ((EventTableModel) eventTable.getModel()).getEntityAt(modelRows[i]);
                        messageBoxText.append("<li>");
                        messageBoxText.append(event.getDescription());
                        messageBoxText.append("</li>");
                    }
                    messageBoxText.append("</ul>");
                    messageBoxText.append("</html>");
                    int rep = SwingUtils.createYNDialogAndGetResponse(eventTable, messageBoxText.toString(), messageBoxHeader);
                    if (rep == 0) {
                        ArrayUtils.reverse(modelRows); //first delete the highest indices, then move to the lowest indices
                        for (int i = 0; i < modelRows.length; i++) {
                            EventBean event = ((EventTableModel) eventTable.getModel()).getEntityAt(modelRows[i]);
                            ((EventTableModel) eventTable.getModel()).removeEntity(event);
                        }
                    }
                    resizeToEvents(eventTable);
                }

            }
        });
        popupMenu.add(copyItem);
        popupMenu.add(copyItemNow);
        popupMenu.add(deleteItem);
        eventTable.setComponentPopupMenu(popupMenu);

        popupMenu.addPopupMenuListener(new SelectOnClickPopupMenuListener(eventTable, popupMenu));

        editPropertyEventAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int modelRow = Integer.valueOf(e.getActionCommand());
                EventBean event = getModel().getEntityAt(modelRow);
                JDialog dialog = new EventPropertyDialog(eventTable, null, null, event);

                dialog.setVisible(true);
            }
        };

        /*eventTable.getParent().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                if (eventTable.getParent().getWidth() > 400) { //actual width smaller than wished width 
                    eventTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                } else {
                    eventTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                }
            }
        });*//*> eventTable.getPreferredSize().width*/

        columnWidths = new THashMap();
    }

    public static RestClientEvent getRestClientEvent() {
        return restClientEvent;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        o_ScrollPaneForEventTable = new javax.swing.JScrollPane();
        eventTable = new javax.swing.JTable()
        ;
        jToolBar1 = new javax.swing.JToolBar();
        timeZoneCheckBox = new javax.swing.JCheckBox();
        toolCatCheckBox = new javax.swing.JCheckBox();
        processCheckBox = new javax.swing.JCheckBox();
        actorCheckBox = new javax.swing.JCheckBox();
        programCheckbox = new javax.swing.JCheckBox();
        labelCheckbox = new javax.swing.JCheckBox();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        comboFilterDateOfEvent = new javax.swing.JComboBox();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jButton1 = new javax.swing.JButton();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        jLabel1 = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        mainActorCombobox = new javax.swing.JComboBox();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        exportEventButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1535, 1135));

        o_ScrollPaneForEventTable.setAutoscrolls(true);
        o_ScrollPaneForEventTable.setPreferredSize(new java.awt.Dimension(1535, 1135));
        o_ScrollPaneForEventTable.setRequestFocusEnabled(false);

        eventTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        eventTable.setMaximumSize(new java.awt.Dimension(32767, 32767));
        eventTable.setMinimumSize(new java.awt.Dimension(20, 20));
        eventTable.setPreferredSize(new java.awt.Dimension(1535, 1135));
        o_ScrollPaneForEventTable.setViewportView(eventTable);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        timeZoneCheckBox.setSelected(true);
        timeZoneCheckBox.setText(org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.timeZoneCheckBox.text")); // NOI18N
        timeZoneCheckBox.setToolTipText(org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.timeZoneCheckBox.toolTipText")); // NOI18N
        timeZoneCheckBox.setFocusable(false);
        timeZoneCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        timeZoneCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                timeZoneCheckBoxItemStateChanged(evt);
            }
        });
        timeZoneCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeZoneCheckBoxActionPerformed(evt);
            }
        });
        jToolBar1.add(timeZoneCheckBox);

        toolCatCheckBox.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(toolCatCheckBox, org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.toolCatCheckBox.text")); // NOI18N
        toolCatCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                toolCatCheckBoxItemStateChanged(evt);
            }
        });
        toolCatCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolCatCheckBoxActionPerformed(evt);
            }
        });
        jToolBar1.add(toolCatCheckBox);

        processCheckBox.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(processCheckBox, org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.processCheckBox.text")); // NOI18N
        processCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                processCheckBoxItemStateChanged(evt);
            }
        });
        processCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processCheckBoxActionPerformed(evt);
            }
        });
        jToolBar1.add(processCheckBox);

        actorCheckBox.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(actorCheckBox, org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.actorCheckBox.text")); // NOI18N
        actorCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                actorCheckBoxItemStateChanged(evt);
            }
        });
        actorCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actorCheckBoxActionPerformed(evt);
            }
        });
        jToolBar1.add(actorCheckBox);

        programCheckbox.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(programCheckbox, org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.programCheckbox.text")); // NOI18N
        programCheckbox.setFocusable(false);
        programCheckbox.setMaximumSize(new java.awt.Dimension(96, 32));
        programCheckbox.setMinimumSize(new java.awt.Dimension(96, 32));
        programCheckbox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        programCheckbox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                programCheckboxItemStateChanged(evt);
            }
        });
        programCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programCheckboxActionPerformed(evt);
            }
        });
        jToolBar1.add(programCheckbox);

        labelCheckbox.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(labelCheckbox, org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.labelCheckbox.text")); // NOI18N
        labelCheckbox.setFocusable(false);
        labelCheckbox.setMaximumSize(new java.awt.Dimension(96, 32));
        labelCheckbox.setMinimumSize(new java.awt.Dimension(96, 32));
        labelCheckbox.setPreferredSize(new java.awt.Dimension(96, 32));
        labelCheckbox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        labelCheckbox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                labelCheckboxItemStateChanged(evt);
            }
        });
        labelCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labelCheckboxActionPerformed(evt);
            }
        });
        jToolBar1.add(labelCheckbox);
        jToolBar1.add(filler7);
        jToolBar1.add(filler3);

        comboFilterDateOfEvent.setMaximumSize(new java.awt.Dimension(100, 25));
        comboFilterDateOfEvent.setMinimumSize(new java.awt.Dimension(100, 25));
        comboFilterDateOfEvent.setPreferredSize(new java.awt.Dimension(100, 25));
        comboFilterDateOfEvent.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                comboFilterDateOfEventFocusGained(evt);
            }
        });
        comboFilterDateOfEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboFilterDateOfEventActionPerformed(evt);
            }
        });
        jToolBar1.add(comboFilterDateOfEvent);
        jToolBar1.add(filler2);

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jToolBar1.add(filler5);
        jToolBar1.add(filler1);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.jLabel1.text")); // NOI18N
        jToolBar1.add(jLabel1);
        jToolBar1.add(filler6);

        mainActorCombobox.setMaximumSize(new java.awt.Dimension(200, 32767));
        mainActorCombobox.setMinimumSize(new java.awt.Dimension(200, 25));
        mainActorCombobox.setPreferredSize(new java.awt.Dimension(200, 25));
        mainActorCombobox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mainActorComboboxItemStateChanged(evt);
            }
        });
        mainActorCombobox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                mainActorComboboxFocusGained(evt);
            }
        });
        mainActorCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainActorComboboxActionPerformed(evt);
            }
        });
        jToolBar1.add(mainActorCombobox);
        jToolBar1.add(filler4);
        jToolBar1.add(filler8);

        org.openide.awt.Mnemonics.setLocalizedText(exportEventButton, org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.exportEventButton.text")); // NOI18N
        exportEventButton.setFocusable(false);
        exportEventButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        exportEventButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        exportEventButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportEventButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(exportEventButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1523, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(o_ScrollPaneForEventTable, javax.swing.GroupLayout.DEFAULT_SIZE, 1523, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1110, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(35, 35, 35)
                    .addComponent(o_ScrollPaneForEventTable, javax.swing.GroupLayout.DEFAULT_SIZE, 1094, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void actorCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actorCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_actorCheckBoxActionPerformed

    private void actorCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_actorCheckBoxItemStateChanged
        // TODO add your handling code here:
        updateColumnWidth(evt, this.getModel().findColumn(EventTableModel.ACTOR));
    }//GEN-LAST:event_actorCheckBoxItemStateChanged

    private void toolCatCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolCatCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toolCatCheckBoxActionPerformed

    private void processCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_processCheckBoxItemStateChanged
        // TODO add your handling code here:
        updateColumnWidth(evt, this.getModel().findColumn(EventTableModel.PROCESS));
    }//GEN-LAST:event_processCheckBoxItemStateChanged

    private void toolCatCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_toolCatCheckBoxItemStateChanged
        // TODO add your handling code here:
        updateColumnWidth(evt, this.getModel().findColumn(EventTableModel.TOOL_CATEGORY));
    }//GEN-LAST:event_toolCatCheckBoxItemStateChanged

    private void mainActorComboboxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mainActorComboboxItemStateChanged
        Actor actor = null;
        if (evt.getItem() instanceof Actor) {
            actor = (Actor) evt.getItem();
        }
        if (actor != null) {
            GlobalActionContextProxy.getInstance().add(CurrentActor.getInstance(actor));
        }
    }//GEN-LAST:event_mainActorComboboxItemStateChanged

    private void mainActorComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainActorComboboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mainActorComboboxActionPerformed

    private void processCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_processCheckBoxActionPerformed

    private void timeZoneCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_timeZoneCheckBoxItemStateChanged
        // TODO add your handling code here:
        updateColumnWidth(evt, this.getModel().findColumn(EventTableModel.TIMEZONE));
    }//GEN-LAST:event_timeZoneCheckBoxItemStateChanged

    private void timeZoneCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeZoneCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_timeZoneCheckBoxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String text = (String) comboFilterDateOfEvent.getSelectedItem();
        boolean isDate = true;
        try {
            StringUtils.SDF_ISO_DATE.parse(text);
        } catch (ParseException ex) {
            isDate = false;
        }
        if (isDate) {
            try {
                eventTableSorter.setRowFilter(RowFilter.regexFilter(text));
            } catch (PatternSyntaxException pse) {
                Messaging.report("Bad regex pattern. Could not sort or limit table", pse, this.getClass(), false);
            }
        } else {
            eventTableSorter.setRowFilter(null);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void comboFilterDateOfEventFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_comboFilterDateOfEventFocusGained
        // TODO add your handling code here:
        comboFilterDateOfEvent.setPopupVisible(true);
        comboFilterDateOfEvent.removeAllItems();

        comboFilterDateOfEvent.addItem("Select a date...");
        for (String date : this.getModel().getAllDates()) {
            comboFilterDateOfEvent.addItem(date);
        }
        int maxVisibleRows = comboFilterDateOfEvent.getMaximumRowCount();
        comboFilterDateOfEvent.setMaximumRowCount(maxVisibleRows + 1);
    }//GEN-LAST:event_comboFilterDateOfEventFocusGained

    private void mainActorComboboxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mainActorComboboxFocusGained
        // TODO add your handling code here:
        CurrentActor currentActor = Utilities.actionsGlobalContext().lookup(CurrentActor.class);

        if (currentActor != null) {
            Actor a = currentActor.getConcept();
            mainActorCombobox.setSelectedItem(a);
        }
    }//GEN-LAST:event_mainActorComboboxFocusGained

    private void exportEventButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportEventButtonActionPerformed
        // TODO add your handling code here:
        ExportEventAction e = new ExportEventAction(this, getViewedEvents(), currentVesselResult.getCurrent(), currentCruiseResult.getCurrent(), currentProgramResult.getCurrent());
        exportEventActionListener.actionPerformed(e);
    }//GEN-LAST:event_exportEventButtonActionPerformed

    private void comboFilterDateOfEventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboFilterDateOfEventActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboFilterDateOfEventActionPerformed

    private void programCheckboxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_programCheckboxItemStateChanged
        // TODO add your handling code here:
        updateColumnWidth(evt, this.getModel().findColumn(EventTableModel.PROGRAM));
    }//GEN-LAST:event_programCheckboxItemStateChanged

    private void programCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programCheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_programCheckboxActionPerformed

    private void labelCheckboxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_labelCheckboxItemStateChanged
        // TODO add your handling code here:
        updateColumnWidth(evt, this.getModel().findColumn(EventTableModel.LABEL));
    }//GEN-LAST:event_labelCheckboxItemStateChanged

    private void labelCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labelCheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_labelCheckboxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox actorCheckBox;
    private javax.swing.JComboBox comboFilterDateOfEvent;
    private javax.swing.JTable eventTable;
    private javax.swing.JButton exportEventButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JCheckBox labelCheckbox;
    private javax.swing.JComboBox mainActorCombobox;
    private javax.swing.JScrollPane o_ScrollPaneForEventTable;
    private javax.swing.JCheckBox processCheckBox;
    private javax.swing.JCheckBox programCheckbox;
    private javax.swing.JCheckBox timeZoneCheckBox;
    private javax.swing.JCheckBox toolCatCheckBox;
    // End of variables declaration//GEN-END:variables

    /**
     * *
     * Sets the column with the given int to its preferred column width or zero
     * width depending on the given Checkbox (de)selection event.
     *
     * @param evt
     * @param column
     * @param max
     * @param min
     */
    private void updateColumnWidth(ItemEvent evt, int column) {
        final TableColumnAdjuster tca = new TableColumnAdjuster(eventTable);
        TableColumnModel tcm = eventTable.getColumnModel();
        TableColumn tableColumn = tcm.getColumn(column);
        int[] columnWidth = columnWidths.get(tableColumn);
        if (evt.getStateChange() == evt.DESELECTED) {
            tableColumn.setWidth(0);
            tableColumn.setMaxWidth(0);
            tableColumn.setMinWidth(0);
            tableColumn.setPreferredWidth(0);
        } else {
            tableColumn.setMaxWidth(columnWidth[1]);
            tableColumn.setMinWidth(columnWidth[0]);
            tableColumn.setPreferredWidth(columnWidth[0]);
            tca.restoreColumns();
        }
    }

    private void setColumnWidths(TableColumn column, int max, int min) {
        columnWidths.put(column, new int[]{min, max});
        if (min == max) {
            column.setResizable(false);
        } else {
            column.setResizable(true);
        }
        column.setMaxWidth(max);
        column.setMinWidth(min);
        column.setPreferredWidth(min);
    }

    private List<EventBean> getViewedEvents() {
        List<EventBean> result = new ArrayList<>();
        for (int i = 0; i < eventTableSorter.getViewRowCount(); i++) {
            result.add(this.getModel().getEntityAt(eventTable.convertRowIndexToModel(i)));
        }
        return result;
    }

    private EventTableModel getModel() {
        return (EventTableModel) eventTable.getModel();
    }

    @Override
    protected void componentShowing() {
    }

    private void updateEventModel() {
        if (restClientEvent != null && currentCruiseResult.getCurrent() != null) {
            CruiseBean currentCruise = currentCruiseResult.getCurrent().getConcept();
            List<EventBean> events = null;
            try {
                events = (List<EventBean>) restClientEvent.getEventByCruise(currentCruise);
            } catch (ConnectException ex) {
                Messaging.report("Note that the webservices are offline. The event list can't be retrieved.", ex, this.getClass(), true);
            }
            if (events != null) {
                Collection<Individuals> allIndividuals = (Collection< Individuals>) Utilities.actionsGlobalContext().lookupAll(Individuals.class);
                for (EventBean event : events) {
                    if (event.getProperties() != null) {
                        for (PropertyBean property : event.getProperties()) {
                            try {
                                Set<Property> properties = Individuals.getConcepts(allIndividuals, new URI(property.getCode()), true, Property.class);
                                for (Property earsProperty : properties) {
                                    property.setIsMandatory(earsProperty.isMandatory());
                                    property.setIsMultiple(earsProperty.isMultiple());
                                    property.setValueClass(earsProperty.getValueClass());
                                }
                            } catch (URISyntaxException ex) {
                                Messaging.report("URISyntaxException", ex, this.getClass(), false);
                            }
                        }
                    }
                    for (Individuals individuals : allIndividuals) {
                        if (individuals.getModel() instanceof ProgramOntology) {

                            for (SpecificEventDefinition sev : individuals.get(SpecificEventDefinition.class)) {
                                if (sev.equals(event)) {
                                    for (be.naturalsciences.bmdc.ears.ontology.entities.Property property : sev.getPropertyCollection()) {

                                        event.attachProperty(property, null);
                                    }
                                }
                            }
                        }
                        if (individuals.getModel() instanceof VesselOntology) {
                            for (SpecificEventDefinition sev : individuals.get(SpecificEventDefinition.class)) {
                                if (sev.equals(event)) {
                                    for (be.naturalsciences.bmdc.ears.ontology.entities.Property property : sev.getPropertyCollection()) {
                                        event.attachProperty(property, null);
                                    }
                                }
                            }
                        }
                    }
                }

                model = new EventTableModel(eventTable, events);
                eventTable.setModel(model);

                /*int newHeight = DEFAULT_ROW_HEIGHT * events.size();
                Dimension dim = new Dimension(eventTable.getWidth(), newHeight);
                eventTable.setPreferredSize(dim);*/
                resizeToEvents(eventTable);
            }
        }
    }

    public static void resizeToEvents(JTable table) {
        int size = table.getModel().getRowCount();
        int newHeight = DEFAULT_ROW_HEIGHT * size;
        Dimension dim = new Dimension(table.getWidth(), newHeight);
        table.setPreferredSize(dim);
    }

    @Override
    public void componentOpened() {
        mainActorCombobox.removeAllItems();

        Set<Actor> actors = new TreeSet<>(new ActorComparator());
        if (!Configs.getOverrideEventsAsAnonymous()) {
            actors.addAll(actorResult.allInstances());
            for (Actor actor : actors) {
                mainActorCombobox.addItem(actor);
            }
        }

        if (restClientEvent != null && currentCruiseResult.getCurrent() != null) {

            updateEventModel();

            TableColumnModel tableColumnModel = eventTable.getColumnModel();

            int dateColumnId = model.findColumn(EventTableModel.DATE);
            int timeColumnId = model.findColumn(EventTableModel.TIME);
            int timeZoneColumnId = model.findColumn(EventTableModel.TIMEZONE);
            int toolCategoryColumnId = model.findColumn(EventTableModel.TOOL_CATEGORY);
            int toolColumnId = model.findColumn(EventTableModel.TOOL);
            int processColumnId = model.findColumn(EventTableModel.PROCESS);
            int actionColumnId = model.findColumn(EventTableModel.ACTION);
            int actorColumnId = model.findColumn(EventTableModel.ACTOR);
            int programColumnId = model.findColumn(EventTableModel.PROGRAM);
            int labelColumnId = model.findColumn(EventTableModel.LABEL);
            int propertyColumnId = model.findColumn(EventTableModel.PROPERTIES);
            //  int deleteColumnId = model.findColumn(EventTableModel.DELETE);

            TableColumn dateColumn = tableColumnModel.getColumn(dateColumnId);
            TableColumn timeColumn = tableColumnModel.getColumn(timeColumnId);
            TableColumn timeZoneColumn = tableColumnModel.getColumn(timeZoneColumnId);
            TableColumn toolCategoryColumn = tableColumnModel.getColumn(toolCategoryColumnId);
            TableColumn toolColumn = tableColumnModel.getColumn(toolColumnId);
            TableColumn processColumn = tableColumnModel.getColumn(processColumnId);
            TableColumn actionColumn = tableColumnModel.getColumn(actionColumnId);
            actorColumn = tableColumnModel.getColumn(actorColumnId);
            TableColumn programColumn = tableColumnModel.getColumn(programColumnId);
            TableColumn labelColumn = tableColumnModel.getColumn(labelColumnId);
            propertyColumn = tableColumnModel.getColumn(propertyColumnId);
            // TableColumn deleteColumn = tableColumnModel.getColumn(deleteColumnId);

            setColumnWidths(dateColumn, 80, 80);
            setColumnWidths(timeColumn, 80, 70);
            setColumnWidths(timeZoneColumn, 80, 50);
            setColumnWidths(toolCategoryColumn, 1000, 80);
            setColumnWidths(toolColumn, 1000, 80);
            setColumnWidths(processColumn, 1000, 80);
            setColumnWidths(actionColumn, 1000, 80);
            setColumnWidths(actorColumn, 500, 80);
            setColumnWidths(programColumn, 500, 80);
            setColumnWidths(labelColumn, 500, 80);
            setColumnWidths(propertyColumn, 100, 100);
            //  setColumnWidths(deleteColumn, 60, 60);

            eventTable.getTableHeader().setReorderingAllowed(true);

            // This decides how many clicks are required to edit a cell in the table editors demo.
            // (Set this to 1 or 2 clicks, as desired.)
            int clickCountToEdit = 1;
            // Set all the default table editors to start with the desired number of clicks.
            // The default table editors are the ones supplied by the JTable class.
            InternalUtilities.setDefaultTableEditorsClicks(eventTable, clickCountToEdit);
            // Add table renderers and editors for the LocalDate, LocalTime, and LocalDateTime types.
            //
            // Note: The editors and renders for each type should be separate instances of the 
            // matching table editor class. Don't use the same instance as both a renderer and editor.
            // If you did, it would be immediately obvious because the cells would not render properly.
            eventTable.setDefaultRenderer(LocalDate.class, new DateTableEditor());
            DateTableEditor dateEdit = new DateTableEditor();
            dateEdit.clickCountToEdit = clickCountToEdit;
            eventTable.setDefaultEditor(LocalDate.class, dateEdit);

            eventTable.setDefaultRenderer(LocalTime.class, new TimeTableEditor());
            TimeTableEditor timeEdit = new TimeTableEditor();
            timeEdit.clickCountToEdit = clickCountToEdit;
            eventTable.setDefaultEditor(LocalTime.class, timeEdit);

            //add actor column
            actorColumn.setCellRenderer(new ActorCellRenderer());
            actorColumn.setCellEditor(new ActorCellEditor(actors));

            //add delete button
//            ButtonColumn deleteButtonColumn = new ButtonColumn(eventTable, deleteEventAction, deleteColumnId, "Delete event");
            //add properties button
            ButtonColumn editPropertyButtonColumn = new ButtonColumn(eventTable, editPropertyEventAction, propertyColumnId, "Edit properties");

            eventTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

            //sorting
            eventTable.setAutoCreateRowSorter(true);
            eventTableSorter = new TableRowSorter<>(this.getModel());
            eventTable.setRowSorter(eventTableSorter);
        }

    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing

    }

    @Override
    public void componentActivated() {
        // TODO add custom code on component closing

    }

    @Override
    public void componentDeactivated() {
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

    private static Object previousEvent;

    @Override
    public void resultChanged(LookupEvent ev) {

        if (ev.getSource().equals(actorResult)) {

            mainActorCombobox.removeAllItems();
            Set<Actor> actors = new TreeSet<>(new ActorComparator());
            if (!Configs.getOverrideEventsAsAnonymous()) {
                actors.addAll(actorResult.allInstances());
            } else {
                actors.clear();
                GlobalActionContextProxy.getInstance().add(CurrentActor.getInstance(null));
            }
            for (Actor actor : actors) {
                mainActorCombobox.addItem(actor);
            }
            if (actorColumn != null) {
                actorColumn.setCellEditor(new ActorCellEditor(actors));
            }
        }

        // if (ev.getSource().equals(eventResult)) {
        Collection c = ((Lookup.Result) ev.getSource()).allInstances();
        //ev.getSource().equals(eventResult)
        for (Object c1 : c) {
            if (c1 instanceof EventBean && !c1.equals(previousEvent)) {
                previousEvent = c1;
                EventBean currentEvent = (EventBean) c1;
                this.getModel().addEntity(currentEvent);
                if (eventTable.getHeight() < eventTable.getModel().getRowCount() * DEFAULT_ROW_HEIGHT) {
                    int newHeight = eventTable.getHeight() + DEFAULT_ROW_HEIGHT;
                    Dimension dim = new Dimension(eventTable.getWidth(), newHeight);
                    eventTable.setPreferredSize(dim);
                }
                eventTable.scrollRectToVisible(eventTable.getCellRect(eventTable.getModel().getRowCount() + 1, 0, true));
            }
        }

        if (currentCruiseResult.matches(ev)) {
            if (restClientEvent != null) {
                //updateEventModel();

                componentOpened();
            }
        }
    }
}
