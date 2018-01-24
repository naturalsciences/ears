/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

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
import be.naturalsciences.bmdc.ears.netbeans.services.GlobalActionContextProxy;
import be.naturalsciences.bmdc.ears.netbeans.services.SingletonResult;
import be.naturalsciences.bmdc.ears.ontology.Individuals;
import be.naturalsciences.bmdc.ears.ontology.ProgramOntology;
import be.naturalsciences.bmdc.ears.ontology.VesselOntology;
import be.naturalsciences.bmdc.ears.ontology.entities.Property;
import be.naturalsciences.bmdc.ears.ontology.entities.SpecificEventDefinition;
import be.naturalsciences.bmdc.ears.properties.Configs;
import be.naturalsciences.bmdc.ears.rest.RestClientEvent;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ears.utils.TableColumnAdjuster;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import com.github.lgooddatepicker.tableeditors.DateTableEditor;
import com.github.lgooddatepicker.tableeditors.TimeTableEditor;
import com.github.lgooddatepicker.zinternaltools.InternalUtilities;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.PatternSyntaxException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;

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
    //private List<EventBean> events = new ArrayList();
    //private Set<String> actors = new THashSet<>();
    private TableColumn actorColumn;
    private Lookup.Result<EventBean> eventResult;
    private Lookup.Result<Actor> actorResult;
    private SingletonResult<CurrentVessel, IVessel> currentVesselResult;
    private SingletonResult<CurrentCruise, ICruise> currentCruiseResult;
    private SingletonResult<CurrentProgram, IProgram> currentProgramResult;

    ExportEventActionListener exportEventActionListener;

    EventPropertyCellRenderer eventPropertyCellRenderer;

    TableRowSorter<TableModel> eventTableSorter;
    //private List<String> eventDateForFilter = new ArrayList();
    //private ArrayList distinctList;
    //Set set = new HashSet();

    private final Action deleteEventAction;

    private static RestClientEvent restClientEvent;

    private EntityTableModelEvent model;
    JPopupMenu popupMenu = new JPopupMenu();

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

        //eventTable.setBackground(new Color(190, 240, 255));
        eventTable.setRowHeight(25);
        eventTable.setFont(DEFAULT_FONT);
        deleteEventAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();

                int modelRow = Integer.valueOf(e.getActionCommand());

                JOptionPane optionPane = new JOptionPane();
                int rep = JOptionPane.showConfirmDialog(
                        optionPane.getParent(), "Delete this event ?"
                        + "\n",
                        "Do you really want to delete this event " + "[" + modelRow + "]",
                        JOptionPane.OK_CANCEL_OPTION);
                if (rep == 0) {
                    ((EntityTableModelEvent) table.getModel()).removeRow(modelRow);
                }

            }
        };

        eventTable.getParent().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                if (eventTable.getParent().getWidth() > 400/*> eventTable.getPreferredSize().width*/) { //actual width smaller than wished width
                    eventTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                } else {
                    eventTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                }
            }
        });

        eventPropertyCellRenderer = new EventPropertyCellRenderer(false);
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
        checkBox_TZ = new javax.swing.JCheckBox();
        checkBox_ToolCat = new javax.swing.JCheckBox();
        checkBox_Process = new javax.swing.JCheckBox();
        checkBox_Actor = new javax.swing.JCheckBox();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        comboFilterDateOfEvent = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        mainActorCombobox = new javax.swing.JComboBox();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
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

        checkBox_TZ.setSelected(true);
        checkBox_TZ.setText(org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.checkBox_TZ.text")); // NOI18N
        checkBox_TZ.setToolTipText(org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.checkBox_TZ.toolTipText")); // NOI18N
        checkBox_TZ.setFocusable(false);
        checkBox_TZ.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        checkBox_TZ.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkBox_TZItemStateChanged(evt);
            }
        });
        checkBox_TZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox_TZActionPerformed(evt);
            }
        });
        jToolBar1.add(checkBox_TZ);

        checkBox_ToolCat.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(checkBox_ToolCat, org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.checkBox_ToolCat.text")); // NOI18N
        checkBox_ToolCat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkBox_ToolCatItemStateChanged(evt);
            }
        });
        checkBox_ToolCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox_ToolCatActionPerformed(evt);
            }
        });
        jToolBar1.add(checkBox_ToolCat);

        checkBox_Process.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(checkBox_Process, org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.checkBox_Process.text")); // NOI18N
        checkBox_Process.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkBox_ProcessItemStateChanged(evt);
            }
        });
        checkBox_Process.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox_ProcessActionPerformed(evt);
            }
        });
        jToolBar1.add(checkBox_Process);

        checkBox_Actor.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(checkBox_Actor, org.openide.util.NbBundle.getMessage(CreateEventTopComponent.class, "CreateEventTopComponent.checkBox_Actor.text")); // NOI18N
        checkBox_Actor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkBox_ActorItemStateChanged(evt);
            }
        });
        checkBox_Actor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox_ActorActionPerformed(evt);
            }
        });
        jToolBar1.add(checkBox_Actor);
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
        jToolBar1.add(filler2);

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
        jToolBar1.add(filler1);

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

    private void checkBox_ActorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox_ActorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox_ActorActionPerformed

    private void checkBox_ActorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBox_ActorItemStateChanged
        // TODO add your handling code here:
        updateColumnWidth(evt, this.getModel().findColumn(EntityTableModelEvent.ACTOR), 200, 500);
    }//GEN-LAST:event_checkBox_ActorItemStateChanged

    private void checkBox_ToolCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox_ToolCatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox_ToolCatActionPerformed

    private void checkBox_ProcessItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBox_ProcessItemStateChanged
        // TODO add your handling code here:
        updateColumnWidth(evt, this.getModel().findColumn(EntityTableModelEvent.PROCESS), 200, 1000);
    }//GEN-LAST:event_checkBox_ProcessItemStateChanged

    private void checkBox_ToolCatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBox_ToolCatItemStateChanged
        // TODO add your handling code here:
        updateColumnWidth(evt, this.getModel().findColumn(EntityTableModelEvent.TOOL_CATEGORY), 200, 1000);
    }//GEN-LAST:event_checkBox_ToolCatItemStateChanged

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

    private void checkBox_ProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox_ProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox_ProcessActionPerformed

    private void checkBox_TZItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBox_TZItemStateChanged
        // TODO add your handling code here:
        updateColumnWidth(evt, this.getModel().findColumn(EntityTableModelEvent.TIMEZONE), 80, 80);
    }//GEN-LAST:event_checkBox_TZItemStateChanged

    private void checkBox_TZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox_TZActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox_TZActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String text = (String) comboFilterDateOfEvent.getSelectedItem();
        boolean isDate = true;
        try {
            StringUtils.ISO_DATE_FORMAT.parse(text);
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkBox_Actor;
    private javax.swing.JCheckBox checkBox_Process;
    private javax.swing.JCheckBox checkBox_TZ;
    private javax.swing.JCheckBox checkBox_ToolCat;
    private javax.swing.JComboBox comboFilterDateOfEvent;
    private javax.swing.JTable eventTable;
    private javax.swing.JButton exportEventButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JButton jButton1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JComboBox mainActorCombobox;
    private javax.swing.JScrollPane o_ScrollPaneForEventTable;
    // End of variables declaration//GEN-END:variables

    private void updateColumnWidth(ItemEvent evt, int column, int max, int min) {
        final TableColumnAdjuster tca = new TableColumnAdjuster(eventTable);
        TableColumnModel tcm = eventTable.getColumnModel();

        if (evt.getStateChange() == evt.DESELECTED) {
            eventTable.getColumnModel().getColumn(column).setWidth(0);
            eventTable.getColumnModel().getColumn(column).setMaxWidth(0);
            eventTable.getColumnModel().getColumn(column).setMinWidth(0);
            eventTable.getColumnModel().getColumn(column).setPreferredWidth(0);
        } else {

            eventTable.getColumnModel().getColumn(column).setMaxWidth(max);
            eventTable.getColumnModel().getColumn(column).setMinWidth(min);
            eventTable.getColumnModel().getColumn(column).setPreferredWidth(min);
            tca.restoreColumns();
        }
    }

    private List<EventBean> getViewedEvents() {
        List<EventBean> result = new ArrayList<>();
        for (int i = 0; i < eventTableSorter.getViewRowCount(); i++) {
            result.add(this.getModel().getEntityAt(eventTable.convertRowIndexToModel(i)));
        }
        return result;
    }

    private EntityTableModelEvent getModel() {
        return (EntityTableModelEvent) eventTable.getModel();
    }

    private void setColumnWidths(TableColumn column, int max, int min) {
        if (min == max) {
            column.setResizable(false);
        } else {
            column.setResizable(true);
        }
        column.setMaxWidth(max);
        column.setMinWidth(min);
        column.setPreferredWidth(min);
    }

    /*private void deleteBlankLine(JTable table) {

        if (table.getModel().getValueAt(0, 1).toString().length() == 0) {
            ((DefaultTableModel) table.getModel()).removeRow(1);
            ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        }
    }*/
    @Override
    protected void componentShowing() {
        // comboFilterDateOfEvent.removeAllItems();
        // eventDateForFilter.add("");
    }

    private void updateEventModel() {
        if (restClientEvent != null && currentCruiseResult.getCurrent() != null) {
            CruiseBean currentCruise = currentCruiseResult.getCurrent().getConcept();
            List<EventBean> events = (List<EventBean>) restClientEvent.getEventByCruise(currentCruise);
            //Collection lookupAll = Utilities.actionsGlobalContext().lookupAll(Individuals.class);

            Collection<Individuals> allIndividuals = (Collection< Individuals>) Utilities.actionsGlobalContext().lookupAll(Individuals.class);
            for (EventBean event : events) {
                if (event.getProperties() != null) {
                    for (EventBean.Property property : event.getProperties()) {
                        try {
                            Set<Property> properties = Individuals.getConcepts(allIndividuals, new URI(property.code), true, Property.class);
                            for (Property earsProperty : properties) {
                                property.isMandatory = earsProperty.isMandatory();
                                property.isMultiple = earsProperty.isMultiple();
                                property.valueClass = earsProperty.getValueClass();
                            }
                        } catch (URISyntaxException ex) {
                            Exceptions.printStackTrace(ex);
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

            model = new EntityTableModelEvent(eventTable, events);
            eventTable.setModel(model);
        }
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

        if (restClientEvent != null) {
            updateEventModel();

            TableColumnModel tableColumnModel = eventTable.getColumnModel();

            int dateColumnId = model.findColumn(EntityTableModelEvent.DATE);
            int timeColumnId = model.findColumn(EntityTableModelEvent.TIME);
            int timeZoneColumnId = model.findColumn(EntityTableModelEvent.TIMEZONE);
            int toolCategoryColumnId = model.findColumn(EntityTableModelEvent.TOOL_CATEGORY);
            int toolColumnId = model.findColumn(EntityTableModelEvent.TOOL);
            int processColumnId = model.findColumn(EntityTableModelEvent.PROCESS);
            int actionColumnId = model.findColumn(EntityTableModelEvent.ACTION);
            int actorColumnId = model.findColumn(EntityTableModelEvent.ACTOR);
            int propertyColumnId = model.findColumn(EntityTableModelEvent.PROPERTIES);
            int deleteColumnId = model.findColumn(EntityTableModelEvent.DELETE);

            TableColumn dateColumn = tableColumnModel.getColumn(dateColumnId);
            TableColumn timeColumn = tableColumnModel.getColumn(timeColumnId);
            TableColumn timeZoneColumn = tableColumnModel.getColumn(timeZoneColumnId);
            TableColumn toolCategoryColumn = tableColumnModel.getColumn(toolCategoryColumnId);
            TableColumn toolColumn = tableColumnModel.getColumn(toolColumnId);
            TableColumn processColumn = tableColumnModel.getColumn(processColumnId);
            TableColumn actionColumn = tableColumnModel.getColumn(actionColumnId);
            actorColumn = tableColumnModel.getColumn(actorColumnId);
            TableColumn propertyColumn = tableColumnModel.getColumn(propertyColumnId);

            setColumnWidths(dateColumn, 80, 80);
            setColumnWidths(timeColumn, 80, 50);
            setColumnWidths(timeZoneColumn, 80, 50);
            setColumnWidths(toolCategoryColumn, 1000, 80);
            setColumnWidths(toolColumn, 1000, 80);
            setColumnWidths(processColumn, 1000, 80);
            setColumnWidths(actionColumn, 1000, 80);
            setColumnWidths(actorColumn, 500, 80);
            setColumnWidths(propertyColumn, 100, 100);

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
            eventTable.getTableHeader().setReorderingAllowed(false);

            eventTable.setDefaultRenderer(LocalDate.class, new DateTableEditor());
            DateTableEditor dateEdit = new DateTableEditor();
            dateEdit.clickCountToEdit = clickCountToEdit;
            eventTable.setDefaultEditor(LocalDate.class, dateEdit);

            eventTable.setDefaultRenderer(LocalTime.class, new TimeTableEditor());
            TimeTableEditor timeEdit = new TimeTableEditor();
            timeEdit.clickCountToEdit = clickCountToEdit;
            eventTable.setDefaultEditor(LocalTime.class, timeEdit);

            // Explicitly set the default editor instance (data type) for each column, by looking at 
            // the most common data type found in each column.
            zSetAllColumnEditorsAndRenderers(eventTable);

            //add actor column
            actorColumn.setCellRenderer(new ActorCellRenderer());
            actorColumn.setCellEditor(new ActorCellEditor(actors));

            //add delete button
            ButtonColumn deleteButtonColumn = new ButtonColumn(eventTable, deleteEventAction, deleteColumnId);
            tableColumnModel.getColumn(deleteColumnId).setMaxWidth(60);
            tableColumnModel.getColumn(deleteColumnId).setMinWidth(60);
            tableColumnModel.getColumn(deleteColumnId).setPreferredWidth(60);

            //add property column
            propertyColumn.setCellRenderer(eventPropertyCellRenderer);
            propertyColumn.setCellEditor(new EventPropertyEditor(eventTable));

            eventTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

            //sorting by something
            //https://docs.oracle.com/javase/7/docs/api/javax/swing/RowFilter.html
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
        eventPropertyCellRenderer.setActive(true);
        // TODO add custom code on component closing

    }

    @Override
    public void componentDeactivated() {
        eventPropertyCellRenderer.setActive(false);
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
        for (Object c1 : c) {
            if (c1 instanceof be.naturalsciences.bmdc.ears.entities.EventBean && !c1.equals(previousEvent)) {
                previousEvent = c1;
                EventBean currentEvent = (EventBean) c1;
                this.getModel().addEntity(currentEvent);
            }
        }

        if (currentCruiseResult.matches(ev)) {
            if (restClientEvent != null) {
                //updateEventModel();
                componentOpened();
            }
        }
    }

    /**
     * zSetAllColumnEditorsAndRenderers,
     *
     * Note: This function is a bit complex, and it's not necessary to
     * understand the source of this function to understand this demo. The most
     * important sections of the demo are located in the constructor and the
     * DemoTableModel class. The reader may wish to skip reading this function
     * until the other parts of the demo are read.
     *
     * This sets the default editors and renderers for each column, by looking
     * at the most common data types found in the columns. Before this function
     * is called, any needed custom editors and renderers should already be
     * added to the table instance for the matching data types. This can be done
     * using the functions "table.setDefaultRenderer()" and
     * "table.setDefaultEditor()".
     *
     * The values in the table columns will be examined to determine which
     * editor should be used for each column. By default, the standard JTable
     * class will set the default column editors and renders using only the data
     * in the first row. In this case, the wrong editor can be assigned if the
     * first row happens to contain a null value (or contains an unexpected
     * value). In contrast, this function can examine any desired number of rows
     * to find non-null values, until a chosen sample size is reached.
     *
     * By default, this function will read the first 30 starting rows to find
     * non-null values. After the starting rows are read, this function will
     * sample up to 70 of the remaining rows ("bulk" rows) in the table at
     * regularly spaced intervals. The bulk rows will sampled from all the
     * remaining rows in the table.
     *
     * Up to the first 21 non-null values ("maxFoundSamplesToExamine") that are
     * encountered will determine which editor will be used for a particular
     * column. The data type that is used will be whichever data type is most
     * frequently encountered within those values. If no non-null values are
     * found in the sampled rows, then the "generic editor" (the string editor)
     * will be used for that column.
     */
    private void zSetAllColumnEditorsAndRenderers(JTable table) {
        // These variables decide how many samples to look at in each column.
        int maxStartRowsToRead = 30;
        int maxBulkRowsToRead = 70;
        int maxFoundSamplesToExamine = 21;
        // Gather some variables that we will need..
        TableModel model = table.getModel();
        int columnCount = model.getColumnCount();
        int rowCount = model.getRowCount();
        // Do nothing if the table is empty.
        if (columnCount < 1 || rowCount < 1) {
            return;
        }
        // Calculate the increment for looping through the bulk rows.
        int bulkRowIncrement = Math.max(1, (rowCount / maxBulkRowsToRead));
        // Loop through all the columns.
        columnLoop:
        for (int columnIndex = 0; columnIndex < columnCount; ++columnIndex) {
            TableColumn column = table.getColumnModel().getColumn(columnIndex);
            ArrayList<Class> nonNullTypes = new ArrayList<Class>();
            // Loop through all the rows that should be sampled.
            rowLoop:
            for (int rowIndex = 0; (rowIndex < rowCount);
                    rowIndex += ((rowIndex < maxStartRowsToRead) ? 1 : bulkRowIncrement)) {
                // Get the value in each row.
                Object value = model.getValueAt(rowIndex, columnIndex);
                if (value == null) {
                    continue;
                }
                // Save any found non-null types.
                nonNullTypes.add(value.getClass());
                // If we have already found "maxFoundSamplesToExamine" types, then use those 
                // samples to determine the column type.
                if (nonNullTypes.size() >= maxFoundSamplesToExamine) {
                    Class mostCommonType = InternalUtilities.getMostCommonElementInList(nonNullTypes);
                    column.setCellRenderer(table.getDefaultRenderer(mostCommonType));
                    column.setCellEditor(table.getDefaultEditor(mostCommonType));
                    continue columnLoop;
                }
            } // End: rowLoop
            // There are no more rows to examine.
            // If we found any non-null types at all, then use those to choose the column type.
            if (nonNullTypes.size() > 0) {
                Class mostCommonType = InternalUtilities.getMostCommonElementInList(nonNullTypes);
                column.setCellRenderer(table.getDefaultRenderer(mostCommonType));
                column.setCellEditor(table.getDefaultEditor(mostCommonType));
            } else {
                // When no types are found in a column, we will use the generic editor.
                column.setCellRenderer(table.getDefaultRenderer(Object.class));
                column.setCellEditor(table.getDefaultEditor(Object.class));
            }
        }
    }

    /**
     * *
     * Update the events in the provided List<EventBean>. Set properties to
     * correct, actual mandatoryness and multiplicity. Set toolcategories,
     * tools, processes actions and properties to correct, actual names. This
     * update is based on the base ontology, the vessel ontology and the program
     * ontology.
     *
     * @param events
     */
    /* private void updateEvents(List<EventBean> events) {
        IBaseOntology baseOntology = Utilities.actionsGlobalContext().lookup(IBaseOntology.class);
        IVesselOntology vesselOntology = Utilities.actionsGlobalContext().lookup(IVesselOntology.class);
        Map<String, AsConcept> founds = new THashMap<>();
        for (EventBean event : events) {
            event.getToolCategory();
        }

    }*/
}
