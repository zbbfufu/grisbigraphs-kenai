/*
 * ImportHistoryViewTopComponent.java
 *
 * Copyright (C) 2009 Francois Duchemin
 *
 * This file is part of GrisbiGraphs.
 *
 * GrisbiGraphs is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * GrisbiGraphs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GrisbiGraphs; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package gg.view.importhistory;

import gg.searchfilter.FieldsVisibility;
import gg.db.entities.FileImport;
import gg.wallet.Wallet;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.joda.time.DateTime;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays the history of the Grisbi file imports.
 */
final class ImportHistoryViewTopComponent extends TopComponent {

    private static ImportHistoryViewTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "gg/resources/icons/ImportHistoryView.png";
    private static final String PREFERRED_ID = "ImportHistoryViewTopComponent";
    private static final byte COLUMN_IMPORTED_ON = 0;
    private static final byte COLUMN_FILE_PATH = 1;
    private static final byte COLUMN_DURATION = 2;
    private static final byte COLUMN_SUCCESS = 3;

    private ImportHistoryViewTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ImportHistoryViewTopComponent.class, "CTL_ImportHistoryViewTopComponent"));
        setToolTipText(NbBundle.getMessage(ImportHistoryViewTopComponent.class, "HINT_ImportHistoryViewTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

        eTableImportHistory.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Imported on", "File", "Duration (s)", "Success"
                }) {

            Class[] types = new Class[]{
                DateTime.class, String.class, Long.class, String.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Object getValueAt(int row, int column) {
                switch (column) {
                    case COLUMN_IMPORTED_ON:
                        DateTime dateTime = (DateTime) super.getValueAt(row, column);
                        return dateTime.toString("EEEE d MMMM yyyy - HH:mm");
                    case COLUMN_FILE_PATH:
                        return super.getValueAt(row, column);
                    case COLUMN_DURATION:
                        long duration = (Long) super.getValueAt(row, column);
                        duration = duration / 1000; // Transform into seconds
                        return duration;
                    case COLUMN_SUCCESS:
                        boolean success = (Boolean) super.getValueAt(row, column);
                        if (success) {
                            return "Yes";
                        } else {
                            return "No";
                        }
                    default:
                        return "";
                }
            }
        });
        
        eTableImportHistory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eTableImportHistory.setColumnHidingAllowed(false);
        eTableImportHistory.setPopupUsedFromTheCorner(false);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        eTableImportHistory.getColumnModel().getColumn(COLUMN_DURATION).setCellRenderer(leftRenderer);

        FieldsVisibility fieldsVisibility = new FieldsVisibility();
        associateLookup(Lookups.singleton(fieldsVisibility));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPaneImportHistory = new javax.swing.JScrollPane();
        eTableImportHistory = new org.netbeans.swing.etable.ETable();

        jScrollPaneImportHistory.setViewportView(eTableImportHistory);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneImportHistory, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneImportHistory, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.netbeans.swing.etable.ETable eTableImportHistory;
    private javax.swing.JScrollPane jScrollPaneImportHistory;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized ImportHistoryViewTopComponent getDefault() {
        if (instance == null) {
            instance = new ImportHistoryViewTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ImportHistoryViewTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ImportHistoryViewTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(ImportHistoryViewTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ImportHistoryViewTopComponent) {
            return (ImportHistoryViewTopComponent) win;
        }
        Logger.getLogger(ImportHistoryViewTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // ------------------------------------------------------
        // Refill the table when the component is opened so that
        // when a new Grisbi file is imported into the embedded DB,
        //  the new row is displayed in the table
        // ------------------------------------------------------

        // Empty the table
        for (int i = eTableImportHistory.getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) eTableImportHistory.getModel()).removeRow(i);
        }

        // Fill the table with the file import logs
        List<FileImport> fileImports = Wallet.getInstance().getFileImports();
        for (FileImport fileImport : fileImports) {
            ((DefaultTableModel) eTableImportHistory.getModel()).addRow(new Object[]{
                        fileImport.getImportedOn(), fileImport.getFilePath(), fileImport.getImportDuration(), fileImport.getSuccess()});
        }
    }

    @Override
    public void componentClosed() {
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return ImportHistoryViewTopComponent.getDefault();
        }
    }
}
