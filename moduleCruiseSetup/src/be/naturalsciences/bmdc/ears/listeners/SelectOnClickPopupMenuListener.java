/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.listeners;

import java.awt.Point;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author thomas
 */
public class SelectOnClickPopupMenuListener implements PopupMenuListener {

    private final JTable table;
    private final JPopupMenu popupMenu;

    public SelectOnClickPopupMenuListener(JTable table, JPopupMenu popupMenu) {
        this.table = table;
        this.popupMenu = popupMenu;
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int[] selectedRows = table.getSelectedRows();
                if (selectedRows.length <= 1) { //If none or one row is selected, make a new selection. If more than 1 row is selected, do nothing and keep them all selected. 
                    int rowAtPoint = table.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), table));
                    if (rowAtPoint > -1) {
                        table.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                    }
                }
            }
        });
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
        // TODO Auto-generated method stub

    }

}
