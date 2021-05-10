/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.listeners;

import be.naturalsciences.bmdc.ears.rest.RestClient;
import be.naturalsciences.bmdc.ears.topcomponents.ExportEventAction;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.EventListener;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.SAVE_DIALOG;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author thomas
 */
public class EventExportActionListener implements EventListener {

    public static final String EXPORT_FILE_NAME = "EARS_export_events.csv";

    public boolean PRINT_PROPS_ONTO = false;

    public void actionPerformed(ExportEventAction exportEventAction) {

        Thread thr = new Thread() {
            public void run() {
              
                try {
                    // parent component of the dialog
                    JFrame parentFrame = new JFrame();

                    JFileChooser fileChooser = new JFileChooser() {
                        @Override
                        public void approveSelection() {
                            File f = getSelectedFile();
                            if (f.exists() && getDialogType() == SAVE_DIALOG) {
                                int result = JOptionPane.showConfirmDialog(this,
                                        "This filename already exists. Do you want to overwrite it?", "Existing file",
                                        JOptionPane.YES_NO_CANCEL_OPTION);
                                switch (result) {
                                    case JOptionPane.YES_OPTION:
                                        super.approveSelection();
                                        return;
                                    case JOptionPane.CANCEL_OPTION:
                                        cancelSelection();
                                        return;
                                    default:
                                        return;
                                }
                            }
                            super.approveSelection();
                        }
                    };
                    fileChooser.setSelectedFile(new File(EXPORT_FILE_NAME));
                    fileChooser.setDialogTitle("Save the selected events");

                    int userSelection = fileChooser.showSaveDialog(parentFrame);

                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        URL baseURL = RestClient.getBaseURL();
                        if(exportEventAction.getCruise() != null){
                            String cruiseIdentifier = exportEventAction.getCruise().getName();
                            FileUtils.copyURLToFile(new URL(baseURL,"ears3/events.csv?cruiseIdentifier="+cruiseIdentifier), fileChooser.getSelectedFile());
                        }else if (exportEventAction.getProgram() != null){
                            String programIdentifier = exportEventAction.getProgram().getName();
                            FileUtils.copyURLToFile(new URL(baseURL,"ears3/events.csv?programIdentifier="+programIdentifier), fileChooser.getSelectedFile());
                        }
                        Messaging.report("Events exported to " + fileChooser.getSelectedFile().getPath(), Message.State.GOOD, this.getClass(), true);
                    }
                } catch (IOException ex) {
                    Messaging.report("Events not exported", ex, this.getClass(), true);
                }
            }
        };
        thr.start();
    }

}
