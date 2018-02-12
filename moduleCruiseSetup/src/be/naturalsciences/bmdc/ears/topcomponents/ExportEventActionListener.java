/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.entities.CruiseBean;
import be.naturalsciences.bmdc.ears.entities.EventBean;
import be.naturalsciences.bmdc.ears.entities.NavBean;
import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.entities.ThermosalBean;
import be.naturalsciences.bmdc.ears.entities.VesselBean;
import be.naturalsciences.bmdc.ears.entities.WeatherBean;
import be.naturalsciences.bmdc.ears.rest.RestClientNav;
import be.naturalsciences.bmdc.ears.rest.RestClientThermosal;
import be.naturalsciences.bmdc.ears.rest.RestClientWeather;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventListener;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.SAVE_DIALOG;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;

/**
 *
 * @author thomas
 */
public class ExportEventActionListener implements EventListener {

    public static final String EXPORT_FILE_NAME = "EARS_export_events.csv";

    public boolean PRINT_PROPS_ONTO = false;

    public void actionPerformed(ExportEventAction e) {

        Thread thr = new Thread() {
            public void run() {
                /*VesselBean currentVessel = (e.getVessel() != null) ? e.getVessel().getConcept() : null;
                CruiseBean currentCruise = (e.getCruise() != null) ? e.getCruise().getConcept() : null;
                ProgramBean currentProgram = (e.getProgram() != null) ? e.getProgram().getConcept() : null;*/
                Collection<EventBean> events = e.getEvents();

                RestClientNav restNav = null;
                try {
                    restNav = new RestClientNav();
                } catch (ConnectException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (EarsException ex) {
                    Exceptions.printStackTrace(ex);
                }

                RestClientWeather restWeather = null;
                try {
                    restWeather = new RestClientWeather();
                } catch (ConnectException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (EarsException ex) {
                    Exceptions.printStackTrace(ex);
                }

                RestClientThermosal restThermosal = null;
                try {
                    restThermosal = new RestClientThermosal();
                } catch (ConnectException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (EarsException ex) {
                    Exceptions.printStackTrace(ex);
                }

                // eventClient.getAllEvents().stream().forEach(System.out::println);
                //String csvFile = NbBundle.getMessage(GeneralToolBarAction.class, "NameOfFileExport");
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

                        FileWriter writer;

                        writer = new FileWriter(fileChooser.getSelectedFile().getPath());
                        List<String> header = new ArrayList(Arrays.asList("Time stamp", "Actor", "Tool category", "Tool", "Process", "Action",
                                "Acquisition Timestamp", "Latitude", "Longitude", "Depth", "Surface water temperature"));
                        Map<String, String> properties = new TreeMap<>();
                        for (EventBean event : events) {
                            for (EventBean.Property property : event.getProperties()) {
                                properties.put(property.code, property.name);
                            }
                        }
                        for (Map.Entry<String, String> entry : properties.entrySet()) {
                            String propertyName = entry.getValue();
                            header.add(propertyName);
                        }

                        header.addAll(Arrays.asList("Heading", "Course over Ground", "Speed over Ground", "Salinity","Conductivity","Sigma T","Wind speed", "Wind direction","Air temperature","Air pressure","Solar Radiation"));

                        String[] entry = new String[header.size()];
                        entry = header.toArray(entry);
                        CSVWriter csvWriter = new CSVWriter(writer, '\t');
                        csvWriter.writeNext(entry, false);
                        for (EventBean event : events) {
                            OffsetDateTime ts = event.getTimeStampDt();
                            NavBean nav = restNav.getNearestNav(ts);
                            WeatherBean wt = restWeather.getNearestWeather(ts);
                            ThermosalBean th = restThermosal.getNearestThermosal(ts);
                            List<String> elements = new ArrayList(Arrays.asList(
                                    event.getTimeStamp(),
                                    event.getActor(),
                                    event.getToolCategoryName(),
                                    event.getToolNames(),
                                    event.getProcessName(),
                                    event.getActionName(),
                                    (nav != null) ? nav.getTimeStamp() : "",
                                    (nav != null) ? nav.getLat() : "",
                                    (nav != null) ? nav.getLon() : "",
                                    (nav != null) ? nav.getDepth() : "",
                                    (th != null) ? th.getSurfaceWaterTemperature().toString() : ""
                            ));

                            for (String propertyUrl : properties.keySet()) {
                                Set<String> propertyValues = event.getPropertyValues(propertyUrl);
                                if (propertyValues != null) {
                                    elements.add(StringUtils.join(propertyValues, ","));
                                } else {
                                    elements.add("");
                                }
                            }     
                            elements.addAll(Arrays.asList((nav != null) ? nav.getHeading() : "",
                                    (nav != null) ? nav.getCog() : "",
                                    (nav != null) ? nav.getSog() : "",
                                    (th != null) ? th.getSalinity().toString() : "",
                                    (th != null) ? th.getConductivity().toString() : "",
                                    (th != null) ? th.getSigmat().toString() : "",         
                                    (wt != null) ? wt.getWindSpeed().toString() : "",
                                    (wt != null) ? wt.getWindDirection().toString() : "",
                                    (wt != null) ? wt.getAirTemperature().toString() : "",
                                    (wt != null) ? wt.getAirPressure().toString() : "",
                                    (wt != null) ? wt.getSolarRadiation().toString() : ""));
                            
                            
                            if (PRINT_PROPS_ONTO) {
                                elements.add(event.getProperty());
                            }
                            entry = new String[elements.size()];
                            entry = elements.toArray(entry);

                            csvWriter.writeNext(entry, true);
                        }
                        writer.flush();
                        writer.close();
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
