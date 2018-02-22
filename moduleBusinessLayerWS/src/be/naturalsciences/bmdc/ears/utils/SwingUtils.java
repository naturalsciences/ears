/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.utils;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 *
 * @author thomas
 */
public class SwingUtils {

    public static final int COMBOBOX_MAX_ROW_COUNT = 20;

    public static void setScrollSpeed(JScrollPane pane, int speed) {
        if (pane != null) {
            JScrollBar bar = pane.getVerticalScrollBar();
            if (bar != null) {
                bar.setUnitIncrement(speed);
            }
        }
    }

    public static boolean modelContains(ComboBoxModel model, Object o) {
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).equals(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return The insert point for a new value. If the value is found the
     * insert point can be any of the possible positions that keeps the
     * collection sorted (.33 or 3.3 or 33.).
     */
    private static int insertPoint(AbstractListModel model, Object key) {
        int low = 0;
        int high = model.getSize() - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            Comparable midVal = (Comparable) model.getElementAt(mid);
            int cmp = midVal.compareTo(key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }

        return low;  // key not found
    }

    /**
     * *
     * Add an comparable object to a DefaultComboBoxModel and ensure it remains
     * sorted.
     *
     * @param model
     * @param o
     */
    public static void addToComboBox(DefaultComboBoxModel<Comparable> model, Comparable o) {
        if (!modelContains(model, o)) {
            int insertPoint = insertPoint(model, o);
            model.insertElementAt(o, insertPoint);
        }
    }

    public static void addToComboBox(JComboBox box, Object o) {
        if (!modelContains(box.getModel(), o)) {
            box.addItem(o);
        }
    }

    /* public static void createDialog(JDialog dialog, Component parent) {
        JOptionPane pane = new JOptionPane();
        pane.add(dialog);
        JDialog dialog = pane.createDialog(parent, title);
        dialog.setLocationRelativeTo(parent);
        dialog.setModal(true);
        dialog.setVisible(true);
    }*/
    public static int createYNDialogAndGetResponse(Component parentComponent, String message, String title) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = pane.createDialog(parentComponent, title);

        /*Double cx = closeToThisComponent.getLocation().getX() - parentComponent.getLocation().getX();
        Double cy = closeToThisComponent.getLocation().getY() - parentComponent.getLocation().getY();
        dialog.setLocation(cx.intValue(), cy.intValue());*/
        Point location = MouseInfo.getPointerInfo().getLocation();
        dialog.setLocation(location);
        dialog.setModal(true);
        dialog.setVisible(true);
        if (pane.getValue() != null) {
            return ((Integer) pane.getValue());
        } else {
            return JOptionPane.CANCEL_OPTION;
        }
    }
}
