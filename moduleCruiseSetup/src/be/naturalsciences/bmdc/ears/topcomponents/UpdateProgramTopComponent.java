/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.FilterableTableModel;
import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.EntityTableModel;
import be.naturalsciences.bmdc.ears.entities.CurrentVessel;
import be.naturalsciences.bmdc.ears.entities.IVessel;
import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.netbeans.services.SingletonResult;
import be.naturalsciences.bmdc.ears.rest.RestClientProgram;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.InputOutput;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//be.ac.mumm//UpdateProgram//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "UpdateProgramTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "be.naturalsciences.bmdc.ears.topcomponents.UpdateProgramTopComponent")
@ActionReferences({
    // @ActionReference(path = "Toolbars/Window", position = 3333,name = "Edit program"),
    @ActionReference(path = "Menu/Window/Cruise & program setup", position = 4)
})
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_UpdateProgramAction",
        preferredID = "UpdateProgramTopComponent"
)
@Messages({
    "CTL_UpdateProgramAction=Edit program...",
    "CTL_UpdateProgramTopComponent=Search for programs",
    "HINT_UpdateProgramTopComponent=Search for programs"
})
public final class UpdateProgramTopComponent extends TopComponent implements LookupListener {

    ProgramTableModel model;
    private InputOutput io;
    private static UpdateProgramTopComponent instance;

    private SingletonResult<CurrentVessel, IVessel> currentVesselResult;

    RestClientProgram restClientProgram;

    public UpdateProgramTopComponent() {
        initComponents();
        try {
            restClientProgram = new RestClientProgram();
        } catch (ConnectException ex) {
            Messaging.report("Note that the webservices are offline. The programs can't be retrieved.", ex, this.getClass(), true);
        } catch (EarsException ex) {
            Messaging.report(ex.getMessage(), ex, this.getClass(), true);
        }
        setName(Bundle.CTL_UpdateProgramTopComponent());
        setToolTipText(Bundle.HINT_UpdateProgramTopComponent());
        instance = this;

        currentVesselResult = new SingletonResult(CurrentVessel.class, this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        o_refreshListProgram = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cruiseNameTextField = new be.naturalsciences.bmdc.ears.topcomponents.SearchTextField();
        programNameTextField = new be.naturalsciences.bmdc.ears.topcomponents.SearchTextField();
        piTextField = new be.naturalsciences.bmdc.ears.topcomponents.SearchTextField();
        jLabel3 = new javax.swing.JLabel();
        o_editProgram = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        o_Program = new javax.swing.JTable();

        org.openide.awt.Mnemonics.setLocalizedText(o_refreshListProgram, org.openide.util.NbBundle.getMessage(UpdateProgramTopComponent.class, "UpdateProgramTopComponent.o_refreshListProgram.text")); // NOI18N
        o_refreshListProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o_refreshListProgramActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(UpdateProgramTopComponent.class, "UpdateProgramTopComponent.jPanel3.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(UpdateProgramTopComponent.class, "UpdateProgramTopComponent.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(UpdateProgramTopComponent.class, "UpdateProgramTopComponent.jLabel2.text")); // NOI18N

        cruiseNameTextField.setText(org.openide.util.NbBundle.getMessage(UpdateProgramTopComponent.class, "UpdateProgramTopComponent.cruiseNameTextField.text")); // NOI18N
        cruiseNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cruiseNameTextFieldActionPerformed(evt);
            }
        });

        programNameTextField.setText(org.openide.util.NbBundle.getMessage(UpdateProgramTopComponent.class, "UpdateProgramTopComponent.programNameTextField.text")); // NOI18N
        programNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programNameTextFieldActionPerformed(evt);
            }
        });

        piTextField.setText(org.openide.util.NbBundle.getMessage(UpdateProgramTopComponent.class, "UpdateProgramTopComponent.piTextField.text")); // NOI18N
        piTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                piTextFieldActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(UpdateProgramTopComponent.class, "UpdateProgramTopComponent.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cruiseNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(programNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(piTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addGap(0, 48, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cruiseNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(programNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(piTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(o_editProgram, org.openide.util.NbBundle.getMessage(UpdateProgramTopComponent.class, "UpdateProgramTopComponent.o_editProgram.text")); // NOI18N
        o_editProgram.setActionCommand(org.openide.util.NbBundle.getMessage(UpdateProgramTopComponent.class, "UpdateProgramTopComponent.o_editProgram.actionCommand")); // NOI18N
        o_editProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o_editProgramActionPerformed(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        jScrollPane1.setViewportView(o_Program);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(o_editProgram, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(o_refreshListProgram, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(o_refreshListProgram)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(o_editProgram)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(150, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void o_editProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o_editProgramActionPerformed
        // TODO add your handling code here:
        editProgram();
    }//GEN-LAST:event_o_editProgramActionPerformed

    private void o_refreshListProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o_refreshListProgramActionPerformed
        if (currentVesselResult.getCurrent() != null) {
            try {
                model.refresh(restClientProgram.getProgramByVessel(currentVesselResult.getCurrent().getConcept()));
            } catch (EarsException ex) {
                Messaging.report(ex.getMessage(), ex, this.getClass(), true);
            } catch (ConnectException ex) {
                Messaging.report(ex.getMessage(), ex, this.getClass(), true);
            }
            o_Program.repaint();
        }
    }//GEN-LAST:event_o_refreshListProgramActionPerformed

    private void cruiseNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cruiseNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cruiseNameTextFieldActionPerformed

    private void programNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_programNameTextFieldActionPerformed

    private void piTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_piTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_piTextFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private be.naturalsciences.bmdc.ears.topcomponents.SearchTextField cruiseNameTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable o_Program;
    private javax.swing.JButton o_editProgram;
    private javax.swing.JButton o_refreshListProgram;
    private be.naturalsciences.bmdc.ears.topcomponents.SearchTextField piTextField;
    private be.naturalsciences.bmdc.ears.topcomponents.SearchTextField programNameTextField;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening

        /*if (EditCruiseSetupTopComponent.getInstance() instanceof EditCruiseSetupTopComponent) {//ys01
         EditCruiseSetupTopComponent currentCruiseTopComponent = EditCruiseSetupTopComponent.getInstance();//ys01
         currentCruiseTopComponent.close();//ys01
         }*/
 /*if (UpdateCruiseTopComponent.getInstance() instanceof UpdateCruiseTopComponent) {
         UpdateCruiseTopComponent currentUpdateCruiseTopComponent = UpdateCruiseTopComponent.getInstance();
         currentUpdateCruiseTopComponent.close();//ys01
         }*/
        //Create a table with a sorter.
        //client = new RestClientProgram();
        List list = null;
        if (currentVesselResult.getCurrent() != null && restClientProgram != null) {
            try {
                list = new ArrayList(restClientProgram.getProgramByVessel(currentVesselResult.getCurrent().getConcept()));
            } catch (EarsException ex) {
                Messaging.report(ex.getMessage(), ex, this.getClass(), true);
            } catch (ConnectException ex) {
                Messaging.report(ex.getMessage(), ex, this.getClass(), true);
            }
        }
        if (list != null) {
            model = new ProgramTableModel(o_Program, list);

            TableRowSorter<ProgramTableModel> sorter = new TableRowSorter<>(model);

            o_Program.setModel(model);
            o_Program.setRowSorter(sorter);
            o_Program.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            o_Program.setRowSelectionAllowed(true);
            o_Program.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            final JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem editItem = new JMenuItem("Edit");
            JSeparator sep = new JSeparator();

            JMenuItem deleteItem = new JMenuItem("Delete");

            editItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    editProgram();
                }
            });
            deleteItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    NotifyDescriptor.Confirmation confirm = new NotifyDescriptor.Confirmation("Do you want to delete this program? Unfortunately this is not possible as the functionality is not provided in the EARS web services", "Delete program", NotifyDescriptor.YES_NO_OPTION);

                    Object result = DialogDisplayer.getDefault().notify(confirm);
                    if (result != NotifyDescriptor.YES_OPTION) {
                        return;
                    }
                    //JOptionPane.showMessageDialog(UpdateCruiseTopComponent.this, "Right-click performed on table and choose DELETE");
                    /*ProgramBean program = model.getEntityAt(o_Program.getSelectedRow());
                    TopComponent tc = findTopComponent(program);
                    if (tc != null) {
                        tc.close();
                    }
                    removeProgram(program); //remove from ws*/
                }
            });
            popupMenu.add(editItem);
            popupMenu.add(sep);
            popupMenu.add(deleteItem);
            o_Program.setComponentPopupMenu(popupMenu);

            SearchProgramByCruise byCruise = new SearchProgramByCruise(cruiseNameTextField, model, o_Program, sorter);
            cruiseNameTextField.accept(byCruise);
            SearchProgramByName byName = new SearchProgramByName(programNameTextField, model, o_Program, sorter);
            programNameTextField.accept(byName);
            SearchProgramByPi byPi = new SearchProgramByPi(piTextField, model, o_Program, sorter);
            piTextField.accept(byPi);
        }
    }

    private TopComponent findTopComponent(ProgramBean program) {
        Set<TopComponent> openTopComponents = WindowManager.getDefault().getRegistry().getOpened();
        for (TopComponent tc : openTopComponents) {
            if (tc.getLookup().lookup(ProgramBean.class) == program) {
                return tc;
            }
        }
        return null;
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
        //instance.close();//ys01
        instance = null;
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

    @Override
    public void resultChanged(LookupEvent le) {
        if (currentVesselResult.getCurrent() != null) {
            try {
                model.refresh(restClientProgram.getProgramByVessel(currentVesselResult.getCurrent().getConcept()));
            } catch (EarsException ex) {
                Messaging.report(ex.getMessage(), ex, this.getClass(), true);
            } catch (ConnectException ex) {
                Messaging.report(ex.getMessage(), ex, this.getClass(), true);
            }
            o_Program.repaint();
        }
    }

    private void editProgram() {
        if (o_Program.getSelectedRowCount() == 1) {
            int row = o_Program.convertRowIndexToModel(o_Program.getSelectedRow());

            ProgramBean currentlyEditedProgram = model.getEntityAt(row);

            TopComponent tc = findTopComponent(currentlyEditedProgram);
            if (tc == null) {
                tc = new EditProgramSetupTopComponent(currentlyEditedProgram);
                tc.open();
            }
            tc.requestActive();
            tc.setDisplayName("Editing " + currentlyEditedProgram.getProgramId());
        }
    }

    private final static String[] columnNames = {ProgramTableModel.CRUISE,
        ProgramTableModel.PROGRAM_NAME, ProgramTableModel.PI};

    public class ProgramTableModel extends EntityTableModel<ProgramBean> implements FilterableTableModel {

        public final static String CRUISE = "Cruise name (code)";
        public final static String PROGRAM_NAME = "Program name";
        public final static String PI = "Principal investigator";

        public ProgramTableModel() {
            super(null, null);
        }

        public ProgramTableModel(JTable table, List<ProgramBean> entities) {
            super(table, entities);
        }

        @Override
        public String[] getColumns() {
            return columnNames;
        }

        @Override
        public void addRow() {
            entities.add(new ProgramBean());
        }

        @Override
        public Object getValueAt(ProgramBean entity, int columnIndex) {

            switch (getColumnName(columnIndex)) {
                case CRUISE:
                    return entity.getCruiseId();
                case PROGRAM_NAME:
                    return entity.getProgramId();
                case PI:
                    return entity.getPiName();
                default:
                    return null;
            }
        }

        @Override
        public List<Integer> getRowsByQueryAndColumn(String searchBy, String column) {
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < entities.size(); i++) {
                String name = (String) getValueAt(i, findColumn(column));
                if (name.contains(searchBy)) {
                    result.add(i);
                }
            }
            return result;
        }

    }

    public static UpdateProgramTopComponent getInstance() {
        return instance;
    }
}
