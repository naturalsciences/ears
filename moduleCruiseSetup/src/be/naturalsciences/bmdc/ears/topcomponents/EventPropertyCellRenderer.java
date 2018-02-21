/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.entities.EventBean;
import be.naturalsciences.bmdc.ears.topcomponents.tablemodel.EventTableModel;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author thomas
 */
public class EventPropertyCellRenderer extends DefaultTableCellRenderer {

    private boolean active;

    public EventPropertyCellRenderer(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setFont(CreateEventTopComponent.DEFAULT_FONT);
        EventTableModel entityTableModelEvent = (EventTableModel) table.getModel();
        //  if (entityTableModelEvent.getRowCount() > row) {
        entityTableModelEvent.getRowCount();

        EventBean event = entityTableModelEvent.getEntityAt(row);
        return new JButton("" + event.getEventId());
       // if (isActive()) {

            /*EventBean event = null;
            try {
                event = entityTableModelEvent.getEntityAt(row);
            } catch (Exception e) {
                int a = 5;
            }
            Collection<Individuals> allIndividuals = (Collection< Individuals>) Utilities.actionsGlobalContext().lookupAll(Individuals.class);

            boolean propertyAdded = false;
            for (Individuals individuals : allIndividuals) {
                if (individuals.getModel() instanceof VesselOntology) {

                    for (SpecificEventDefinition sev : individuals.get(SpecificEventDefinition.class)) {
                        if (sev.equals(event)) {
                            for (be.naturalsciences.bmdc.ears.ontology.entities.Property property : sev.getPropertyCollection()) {
                                event.attachProperty(property, null);
                                propertyAdded = true;
                            }
                        }
                    }
                }
            }*/
        //    return new JButton("Active: Edit event" + event.getEventId());
            /*if (propertyAdded) {
                return new JButton("Edit"); //Mandated properties
            }

            if (value instanceof Set) {
                return new JButton("Edit"); //Own properties
            } else {
                return null;
            }*/
            // }
            //  return null;

   /*    } else {
            return new JButton("Inactive: Edit event" + event.getEventId());
        }*/
    }
}
