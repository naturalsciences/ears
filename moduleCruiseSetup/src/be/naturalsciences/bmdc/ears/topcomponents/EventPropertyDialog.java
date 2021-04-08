/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.entities.CurrentCruise;
import be.naturalsciences.bmdc.ears.entities.ICruise;
import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.netbeans.services.SingletonResult;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import eu.eurofleets.ears3.dto.EventDTO;
import eu.eurofleets.ears3.dto.PropertyDTO;
import gnu.trove.map.hash.THashMap;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author thomas
 */
class EventPropertyDialog extends JDialog implements LookupListener {

    private final EventDTO event;

    private JButton okButton;
    private Map<String, List<JButton>> addButtons;
    private Map<String, List<JButton>> minusButtons;

    private String defaultValuePrefix = "No ";

    private Lookup.Result<ProgramBean> programResult;

    private SingletonResult<CurrentCruise, ICruise> currentCruiseResult;

    Collection<ProgramBean> currentPrograms;

    private final Map<String, Integer> multipleSelectionFields = new HashMap();

    static Window getWindowForComponent(Component parentComponent) {
        if (parentComponent instanceof Frame || parentComponent instanceof Dialog) {
            return (Window) parentComponent;
        }
        return getWindowForComponent(parentComponent.getParent());

    }

    public EventPropertyDialog(Component parentComponent, ActionListener okListener, ActionListener cancelListener, EventDTO event) throws HeadlessException {
        super(getWindowForComponent(parentComponent), "Properties", ModalityType.APPLICATION_MODAL);
        this.event = event;
        if (this.event.getProperties() != null) {
            programResult = Utilities.actionsGlobalContext().lookupResult(ProgramBean.class);
            //programResult.addLookupListener(this);
            currentCruiseResult = new SingletonResult<>(CurrentCruise.class, this);
            currentPrograms = new ArrayList();
            addButtons = new THashMap();
            minusButtons = new THashMap();
            init(okListener, cancelListener);
            pack();
            /* Double cx = closeToThisComponent.getLocation().getX() - parentComponent.getLocation().getX();
        Double cy = closeToThisComponent.getLocation().getY() - parentComponent.getLocation().getY();*/
            Point location = MouseInfo.getPointerInfo().getLocation();
            setLocation(location);

            setResizable(true);
        }
    }

    protected JPanel initSaveButton(ActionListener okListener, ActionListener cancelListener) {
        //Locale locale = getLocale();
        String okString = "Save";//UIManager.getString("ColorChooser.okText", locale);
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        okButton = new JButton(okString);
        getRootPane().setDefaultButton(okButton);
        okButton.getAccessibleContext().setAccessibleDescription(okString);
        okButton.setActionCommand("OK");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                CreateEventTopComponent.getRestClientEvent().modifyEvent(event);
                getParent().repaint();
            }
        });
        if (okListener != null) {
            okButton.addActionListener(okListener);
        }
        buttonPane.add(okButton);

        if (JDialog.isDefaultLookAndFeelDecorated()) {
            boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
            if (supportsWindowDecorations) {
                getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
            }
        }
        return buttonPane;
        //  applyComponentOrientation(((c == null) ? getRootPane() : c).getComponentOrientation());

        //   this.addWindowListener(new javax.swing.ColorChooserDialog.Closer());
    }

    private JComponent getPropertyValueField(PropertyDTO property, Object defaultValue) {
        JComponent propertyValueField = null;
        if (property.valueClass == null) {
            JTextField propertyValueTextField = new JTextField(20);
            propertyValueField = propertyValueTextField;
            if (property.value != null) {
                propertyValueTextField.setText(property.value);
            } else if (defaultValue instanceof String) {
                propertyValueTextField.setText((String) defaultValue);
            }
            propertyValueTextField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    update();
                }

                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                private void update() {
                    if ((propertyValueTextField.getText().equals(defaultValue) || propertyValueTextField.getText().isEmpty()) && property.mandatory) {
                        propertyValueTextField.setText(property.value);
                        okButton.setEnabled(false);
                        Messaging.report("A mandatory property can't be set to the default value =('" + defaultValue + "'). Your changes have been reset.", Message.State.BAD, EventPropertyDialog.class, true);
                    } else {
                        property.value = propertyValueTextField.getText();
                        okButton.setEnabled(true);
                    }
                }

            });

        } else if (property.valueClass.equals("Program")) {

            JComboBox propertyValueComboBox = new JComboBox();
            propertyValueComboBox.setName(property.key.identifier);
            propertyValueComboBox.setEditable(false);
            propertyValueField = propertyValueComboBox;
            propertyValueComboBox.addItem(defaultValue);

            //Collection<? extends ProgramBean> allInstances = programResult.allInstances();
            if (currentCruiseResult.getCurrent() != null && currentCruiseResult.getCurrent().getConcept() != null) {
                for (ProgramBean program : currentCruiseResult.getCurrent().getConcept().getPrograms()) {
                    propertyValueComboBox.addItem(program.getProgramId());
                }
            }

            if (property.value == null || property.value.equals(defaultValue)) {
                propertyValueComboBox.setSelectedItem(defaultValue); //let the DocumentListener decide whether this is a legal change
            } else {
                propertyValueComboBox.setSelectedItem(property.value.trim()); //let the DocumentListener decide whether this is a legal change
            }

            propertyValueComboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent ie) {
                    if (ie.getStateChange() == ItemEvent.SELECTED) {
                        update();
                    }

                }

                private void update() {
                    if (propertyValueComboBox.getSelectedItem().equals(defaultValue) && property.mandatory) {
                        propertyValueComboBox.setSelectedItem(property.value);
                        okButton.setEnabled(false);
                        Messaging.report("A mandatory property can't be set to the default value =('" + defaultValue + "'). Your changes have been reset.", Message.State.BAD, EventPropertyDialog.class, true);
                    } else if (propertyValueComboBox.getSelectedItem().equals(defaultValue)) {
                        property.value = "";
                        okButton.setEnabled(true);
                    } else {
                        String newValue = (String) propertyValueComboBox.getSelectedItem();
                        for (PropertyDTO existingProperty : event.getProperties()) {
                            if (existingProperty != null && existingProperty.value != null) {
                                System.out.println(existingProperty.key + ": " + existingProperty.value);
                                if (existingProperty.value.equals(newValue)) {
                                    System.out.println(existingProperty.key + ": " + existingProperty.value + " equals " + newValue);
                                    okButton.setEnabled(false);
                                    Messaging.report("A property denoting a " + property.valueClass + " can't have multiple identical values. The value already occurs.", Message.State.BAD, EventPropertyDialog.class, true);

                                    return;
                                }
                            }
                        }
                        property.value = ((String) propertyValueComboBox.getSelectedItem());
                        event.attachProperty(property, null);
                        okButton.setEnabled(true);
                    }
                }
            });
            /*((JTextComponent) propertyValueComboBox.getEditor().getEditorComponent()).getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    update();
                }

                public void removeUpdate(DocumentEvent e) {
                    // update();
                }

                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                private void update() {
                    if (propertyValueComboBox.getSelectedItem().equals(defaultValue) && property.isMandatory) {
                        propertyValueComboBox.setSelectedItem(property.value);
                        okButton.setEnabled(false);
                        Messaging.report("A mandatory property can't be set to the default value =('" + defaultValue + "'). Your changes have been reset.", Message.State.BAD, EventPropertyDialog.class, true);
                    } else if (propertyValueComboBox.getSelectedItem().equals(defaultValue)) {
                        property.value = "";
                        okButton.setEnabled(true);
                    } else {
                        String newValue = (String) propertyValueComboBox.getSelectedItem();
                        for (PropertyBean existingProperty : event.getProperties()) {
                            if (existingProperty != null && existingProperty.value != null) {
                                System.out.println(existingProperty.code + ": " + existingProperty.value);
                                if (existingProperty.value.equals(newValue)) {
                                    System.out.println(existingProperty.code + ": " + existingProperty.value + " equals " + newValue);
                                    okButton.setEnabled(false);
                                    Messaging.report("A property denoting a " + property.valueClass + " can't have multiple identical values. The value already occurs.", Message.State.BAD, EventPropertyDialog.class, true);

                                    return;
                                }
                            }
                        }
                        property.value = (String) propertyValueComboBox.getSelectedItem();
                        event.getProperties().add(property);
                        okButton.setEnabled(true);
                    }
                }

            });*/

        }
        return propertyValueField;
    }

    protected void init(ActionListener okListener, ActionListener cancelListener) {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel buttonPane = initSaveButton(okListener, cancelListener);

        contentPane.add(buttonPane, BorderLayout.NORTH);

        Set<PropertyDTO> properties = this.event.getProperties();

        JPanel propertyPane = new JPanel();
        propertyPane.setLayout(new MigLayout());

        contentPane.add(propertyPane);
        for (PropertyDTO property : properties) {
            JLabel propertyKeyLabel = new JLabel(property.key.name);

            propertyPane.add(propertyKeyLabel);
            JComponent propertyValueField = null;
            if (property.valueClass == null) {
                propertyValueField = getPropertyValueField(property, "");
            } else {
                propertyValueField = getPropertyValueField(property, "No " + property.valueClass);
            }
            if (property.multiple) {
                multipleSelectionFields.put(property.key.identifier, 1);
                boolean addButtonCondition = true;
                boolean minusButtonCondition = true;
                int maximum = 5;
                if (propertyValueField instanceof JComboBox) {
                    JComboBox propertyValueComboBox = (JComboBox) propertyValueField;
                    maximum = propertyValueComboBox.getItemCount();
                    if (multipleSelectionFields.get(property.key.identifier) > propertyValueComboBox.getItemCount()) {
                        addButtonCondition = false;
                    }
                    if (multipleSelectionFields.get(property.key.identifier) > 1) {
                        minusButtonCondition = false;
                    }
                }

                if (addButtonCondition) {
                    propertyPane.add(propertyValueField);
                    JButton addButton = getAddButton(propertyPane, property, maximum);
                    if (minusButtonCondition) {
                        propertyPane.add(addButton);
                        JButton minusButton = getMinusButton(propertyPane, property, propertyKeyLabel, propertyValueField, addButton, null);
                        propertyPane.add(minusButton, "wrap");
                    } else {
                        propertyPane.add(addButton, "wrap");
                    }

                } else {
                    propertyPane.add(propertyValueField);
                }
            } else {
                propertyPane.add(propertyValueField, "wrap");
            }
        }
    }

    private JButton getAddButton(JPanel pane, PropertyDTO property, int maximum) {
        JButton addButton = new JButton("+");
        if (addButtons.get(property.key.name) != null) {
            List addButtonList = addButtons.get(property.key.name);
            addButtonList.add(addButton);
            addButtons.put(property.key.name, addButtonList);
        } else {
            List addButtonList = new ArrayList();
            addButtonList.add(addButton);
            addButtons.put(property.key.identifier, addButtonList);
        }
        if (addButtons.get(property.key.identifier).size() == maximum) {
            addButton.setEnabled(false);
        }
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                PropertyDTO propertyCopy = property.clone();
                propertyCopy.value = null;
                multipleSelectionFields.put(propertyCopy.key.identifier, multipleSelectionFields.get(propertyCopy.key.identifier) + 1);

                JLabel propertyKeyLabel = new JLabel(propertyCopy.key.name);
                JComponent propertyValueField = null;
                if (property.valueClass == null) {
                    propertyValueField = getPropertyValueField(propertyCopy, "");
                } else {
                    propertyValueField = getPropertyValueField(propertyCopy, "No " + propertyCopy.valueClass);
                }

                if (multipleSelectionFields.get(propertyCopy.key.identifier) < maximum - 1) {
                    pane.add(propertyKeyLabel);
                    pane.add(propertyValueField);
                    JButton newAddButton = getAddButton(pane, propertyCopy, maximum);
                    pane.add(newAddButton);
                    pane.add(getMinusButton(pane, propertyCopy, propertyKeyLabel, propertyValueField, newAddButton, addButton), "wrap");
                } else {
                    for (JButton button : addButtons.get(propertyCopy.key.identifier)) {
                        button.setEnabled(false);
                    }
                    pane.add(propertyKeyLabel);
                    pane.add(propertyValueField);
                    pane.add(getMinusButton(pane, propertyCopy, propertyKeyLabel, propertyValueField, null, addButton), "wrap");
                }
                pane.revalidate();
                pane.repaint();
                EventPropertyDialog.this.repaint();
                EventPropertyDialog.this.pack();
            }
        };

        addButton.addActionListener(al);

        return addButton;
    }

    private JButton getMinusButton(JPanel pane, PropertyDTO property, JLabel propertyKeyLabel, JComponent propertyValueField, JButton addButtonOfOwnRow, JButton callingAddButton) {
        JButton minusButton = new JButton("-");
        if (minusButtons.get(property.key.identifier) != null) {
            List addButtonList = minusButtons.get(property.key.identifier);
            addButtonList.add(minusButton);
            minusButtons.put(property.key.identifier, addButtonList);
        } else {
            List minusButtonList = new ArrayList();
            minusButtonList.add(minusButton);
            minusButtons.put(property.key.identifier, minusButtonList);
        }
        if (minusButtons.get(property.key.identifier).size() == 1) {
            minusButton.setEnabled(false);
        }
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                if (multipleSelectionFields.get(property.key.identifier) > 2) {
                    pane.remove(propertyKeyLabel);
                    pane.remove(propertyValueField);
                    if (addButtonOfOwnRow != null) {
                        pane.remove(addButtonOfOwnRow);
                    }
                    if (minusButton != null) {
                        pane.remove(minusButton);
                    }

                } else {
                    pane.remove(propertyKeyLabel);
                    pane.remove(propertyValueField);
                    if (addButtonOfOwnRow != null) {
                        pane.remove(addButtonOfOwnRow);
                    }
                    if (minusButton != null) {
                        pane.remove(minusButton);
                    }
                    for (JButton button : addButtons.get(property.key.identifier)) {
                        button.setEnabled(true);
                    }
                    for (JButton button : minusButtons.get(property.key.identifier)) {
                        button.setEnabled(false);
                    }
                    /*if (callingAddButton != null) {
                        callingAddButton.setEnabled(true);
                    }*/
                }
                event.getProperties().remove(property);
                property.value = "";
                multipleSelectionFields.put(property.key.identifier, multipleSelectionFields.get(property.key.identifier) - 1);

                pane.revalidate();
                pane.repaint();
                EventPropertyDialog.this.repaint();
                EventPropertyDialog.this.pack();
            }
        };

        minusButton.addActionListener(al);
        return minusButton;
    }

    @Override
    public void resultChanged(LookupEvent le) {
    }
}
