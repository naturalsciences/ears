/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.topcomponents;

import be.naturalsciences.bmdc.ears.entities.CurrentCruise;
import be.naturalsciences.bmdc.ears.entities.EventBean;
import be.naturalsciences.bmdc.ears.entities.PropertyBean;
import be.naturalsciences.bmdc.ears.entities.ICruise;
import be.naturalsciences.bmdc.ears.entities.ProgramBean;
import be.naturalsciences.bmdc.ears.netbeans.services.SingletonResult;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
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
import javax.swing.JOptionPane;
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

    private final EventBean event;

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

    public EventPropertyDialog(Component parentComponent, ActionListener okListener, ActionListener cancelListener, EventBean event) throws HeadlessException {
        super(getWindowForComponent(parentComponent), "Properties", ModalityType.APPLICATION_MODAL);
        this.event = event;
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

    private JComponent getPropertyValueField(PropertyBean property, Object defaultValue) {
        JComponent propertyValueField = null;
        if (property.getValueClass() == null) {
            JTextField propertyValueTextField = new JTextField(20);
            propertyValueField = propertyValueTextField;
            if (property.getValue() != null) {
                propertyValueTextField.setText(property.getValue());
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
                    if ((propertyValueTextField.getText().equals(defaultValue) || propertyValueTextField.getText().isEmpty()) && property.isMandatory()) {
                        propertyValueTextField.setText(property.getValue());
                        okButton.setEnabled(false);
                        Messaging.report("A mandatory property can't be set to the default value =('" + defaultValue + "'). Your changes have been reset.", Message.State.BAD, EventPropertyDialog.class, true);
                    } else {
                        property.setValue(propertyValueTextField.getText());
                        okButton.setEnabled(true);
                    }
                }

            });

        } else if (property.getValueClass().equals("Program")) {

            JComboBox propertyValueComboBox = new JComboBox();
            propertyValueComboBox.setName(property.getCode());
            propertyValueComboBox.setEditable(false);
            propertyValueField = propertyValueComboBox;
            propertyValueComboBox.addItem(defaultValue);

            Collection<? extends ProgramBean> allInstances = programResult.allInstances();
            for (ProgramBean program : allInstances) {
                if (currentCruiseResult.getCurrent() != null && currentCruiseResult.getCurrent().getConcept() != null && currentCruiseResult.getCurrent().getConcept().getRealId().equalsIgnoreCase(program.getCruiseId())) {
                    propertyValueComboBox.addItem(program.getProgramId());
                }
            }

            if (property.getValue() == null || property.getValue().equals(defaultValue)) {
                propertyValueComboBox.setSelectedItem(defaultValue); //let the DocumentListener decide whether this is a legal change
            } else {
                propertyValueComboBox.setSelectedItem(property.getValue().trim()); //let the DocumentListener decide whether this is a legal change
            }

            propertyValueComboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent ie) {
                    if (ie.getStateChange() == ItemEvent.SELECTED) {
                        update();
                    }

                }

                private void update() {
                    if (propertyValueComboBox.getSelectedItem().equals(defaultValue) && property.isMandatory()) {
                        propertyValueComboBox.setSelectedItem(property.getValue());
                        okButton.setEnabled(false);
                        Messaging.report("A mandatory property can't be set to the default value =('" + defaultValue + "'). Your changes have been reset.", Message.State.BAD, EventPropertyDialog.class, true);
                    } else if (propertyValueComboBox.getSelectedItem().equals(defaultValue)) {
                        property.setValue("");
                        okButton.setEnabled(true);
                    } else {
                        String newValue = (String) propertyValueComboBox.getSelectedItem();
                        for (PropertyBean existingProperty : event.getProperties()) {
                            if (existingProperty != null && existingProperty.getValue() != null) {
                                System.out.println(existingProperty.getCode() + ": " + existingProperty.getValue());
                                if (existingProperty.getValue().equals(newValue)) {
                                    System.out.println(existingProperty.getCode() + ": " + existingProperty.getValue() + " equals " + newValue);
                                    okButton.setEnabled(false);
                                    Messaging.report("A property denoting a " + property.getValueClass() + " can't have multiple identical values. The value already occurs.", Message.State.BAD, EventPropertyDialog.class, true);

                                    return;
                                }
                            }
                        }
                        property.setValue((String) propertyValueComboBox.getSelectedItem());
                        event.attachProperty(property);
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

        Set<PropertyBean> properties = this.event.getProperties();

        JPanel propertyPane = new JPanel();
        propertyPane.setLayout(new MigLayout());

        contentPane.add(propertyPane);
        for (PropertyBean property : properties) {
            JLabel propertyKeyLabel = new JLabel(property.getName());

            propertyPane.add(propertyKeyLabel);
            JComponent propertyValueField = null;
            if (property.getValueClass() == null) {
                propertyValueField = getPropertyValueField(property, "");
            } else {
                propertyValueField = getPropertyValueField(property, "No " + property.getValueClass());
            }
            if (property.isMultiple()) {
                multipleSelectionFields.put(property.getCode(), 1);
                boolean addButtonCondition = true;
                boolean minusButtonCondition = true;
                int maximum = 5;
                if (propertyValueField instanceof JComboBox) {
                    JComboBox propertyValueComboBox = (JComboBox) propertyValueField;
                    maximum = propertyValueComboBox.getItemCount();
                    if (multipleSelectionFields.get(property.getCode()) > propertyValueComboBox.getItemCount()) {
                        addButtonCondition = false;
                    }
                    if (multipleSelectionFields.get(property.getCode()) > 1) {
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

    private class PropertyFieldRow {

    }

    private JButton getAddButton(JPanel pane, PropertyBean property, int maximum) {
        JButton addButton = new JButton("+");
        if (addButtons.get(property.getCode()) != null) {
            List addButtonList = addButtons.get(property.getCode());
            addButtonList.add(addButton);
            addButtons.put(property.getCode(), addButtonList);
        } else {
            List addButtonList = new ArrayList();
            addButtonList.add(addButton);
            addButtons.put(property.getCode(), addButtonList);
        }
        if (addButtons.get(property.getCode()).size() == maximum) {
            addButton.setEnabled(false);
        }
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                PropertyBean propertyCopy = property.clone();
                propertyCopy.setValue(null);
                multipleSelectionFields.put(propertyCopy.getCode(), multipleSelectionFields.get(propertyCopy.getCode()) + 1);

                JLabel propertyKeyLabel = new JLabel(propertyCopy.getName());
                JComponent propertyValueField = null;
                if (property.getValueClass() == null) {
                    propertyValueField = getPropertyValueField(propertyCopy, "");
                } else {
                    propertyValueField = getPropertyValueField(propertyCopy, "No " + propertyCopy.getValueClass());
                }

                if (multipleSelectionFields.get(propertyCopy.getCode()) < maximum - 1) {
                    pane.add(propertyKeyLabel);
                    pane.add(propertyValueField);
                    JButton newAddButton = getAddButton(pane, propertyCopy, maximum);
                    pane.add(newAddButton);
                    pane.add(getMinusButton(pane, propertyCopy, propertyKeyLabel, propertyValueField, newAddButton, addButton), "wrap");
                } else {
                    for (JButton button : addButtons.get(propertyCopy.getCode())) {
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

    private JButton getMinusButton(JPanel pane, PropertyBean property, JLabel propertyKeyLabel, JComponent propertyValueField, JButton addButtonOfOwnRow, JButton callingAddButton) {
        JButton minusButton = new JButton("-");
        if (minusButtons.get(property.getCode()) != null) {
            List addButtonList = minusButtons.get(property.getCode());
            addButtonList.add(minusButton);
            minusButtons.put(property.getCode(), addButtonList);
        } else {
            List minusButtonList = new ArrayList();
            minusButtonList.add(minusButton);
            minusButtons.put(property.getCode(), minusButtonList);
        }
        if (minusButtons.get(property.getCode()).size() == 1) {
            minusButton.setEnabled(false);
        }
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                if (multipleSelectionFields.get(property.getCode()) > 2) {
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
                    for (JButton button : addButtons.get(property.getCode())) {
                        button.setEnabled(true);
                    }
                    for (JButton button : minusButtons.get(property.getCode())) {
                        button.setEnabled(false);
                    }
                    /*if (callingAddButton != null) {
                        callingAddButton.setEnabled(true);
                    }*/
                }
                event.getProperties().remove(property);
                property.setValue("");
                multipleSelectionFields.put(property.getCode(), multipleSelectionFields.get(property.getCode()) - 1);

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
