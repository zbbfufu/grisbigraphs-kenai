/*
 * ImportHistoryTopComponent.java
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
import gg.utilities.Utilities;
import gg.wallet.Wallet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.joda.time.DateTime;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays the history of the Grisbi file imports.
 */
@ConvertAsProperties(dtd = "-//gg.view.importhistory//ImportHistory//EN", autostore = false)
public final class ImportHistoryTopComponent extends TopComponent {

    /** Singleton instance of the topcomponent */
    private static ImportHistoryTopComponent instance;
    /** Path to the icon used by the component and its open action */
    private static final String ICON_PATH = "gg/resources/icons/ImportHistory.png";
    /** ID of the component */
    private static final String PREFERRED_ID = "ImportHistoryTopComponent";
    /** Position of 'Imported on' in the model */
    private static final byte COLUMN_IMPORTED_ON = 0;
    /** Position of 'File path' in the model */
    private static final byte COLUMN_FILE_PATH = 1;
    /** Position of 'duration' in the model */
    private static final byte COLUMN_DURATION = 2;
    /** Position of 'success' in the model */
    private static final byte COLUMN_SUCCESS = 3;
    /** Logger */
    private Logger log = Logger.getLogger(this.getClass().getName());

    /** Creates a new instance of ImportHistoryTopComponent */
    public ImportHistoryTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ImportHistoryTopComponent.class, "CTL_ImportHistoryTopComponent"));
        setToolTipText(NbBundle.getMessage(ImportHistoryTopComponent.class, "HINT_ImportHistoryTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

        // Initialize the table that shows the file imports
        eTableImportHistory.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] {
                    NbBundle.getMessage(ImportHistoryTopComponent.class, "ImportHistoryTopComponent.ImportedOn"),
                    NbBundle.getMessage(ImportHistoryTopComponent.class, "ImportHistoryTopComponent.File"),
                    NbBundle.getMessage(ImportHistoryTopComponent.class, "ImportHistoryTopComponent.Duration"),
                    NbBundle.getMessage(ImportHistoryTopComponent.class, "ImportHistoryTopComponent.Success")
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
                            return NbBundle.getMessage(ImportHistoryTopComponent.class, "ImportHistoryTopComponent.Yes");
                        } else {
                            return NbBundle.getMessage(ImportHistoryTopComponent.class, "ImportHistoryTopComponent.No");
                        }
                    default:
                        throw new AssertionError("Unknown column: " + column);
                }
            }
        });

        // Set the properties of the table that shows the file imports
        eTableImportHistory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eTableImportHistory.setColumnHidingAllowed(false);
        eTableImportHistory.setPopupUsedFromTheCorner(false);
        eTableImportHistory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Left align the durations
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        eTableImportHistory.getColumnModel().getColumn(COLUMN_DURATION).setCellRenderer(leftRenderer);

        // No filter can be applied to this view
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
     * @return Default instance
     */
    public static synchronized ImportHistoryTopComponent getDefault() {
        if (instance == null) {
            instance = new ImportHistoryTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ImportHistoryTopComponent instance. Never call {@link #getDefault} directly!
     * @return ImportHistoryTopComponent instance
     */
    public static synchronized ImportHistoryTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(ImportHistoryTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ImportHistoryTopComponent) {
            return (ImportHistoryTopComponent) win;
        }
        Logger.getLogger(ImportHistoryTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    /**
     * Gets the persistence type
     * @return Persistence type
     */
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    /**
     * Saves properties
     * @param p Properties to save
     */
    public void writeProperties(Properties p) {
        p.setProperty("version", "1.0");
    }

    /**
     * Reads properties
     * @param p properties to save
     * @return TopComponent with loaded properties
     */
    public Object readProperties(Properties p) {
        ImportHistoryTopComponent singleton = ImportHistoryTopComponent.getDefault();
        singleton.readPropertiesImpl(p);
        return singleton;
    }

    /**
     * Reads properties
     * @param p Properties to read
     */
    private void readPropertiesImpl(Properties p) {
        String version = p.getProperty("version");
    }

    /**
     * Gets the topcomponent's ID
     * @return Topcomponent's ID
     */
    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    /** Fill the table with the file imports when the topcomponent is opened */
    @Override
    public void componentOpened() {
        // Refill the table when the component is opened so that when a new Grisbi file is
        // imported into the embedded DB, the new row is displayed in the table
        log.info("Import history table computed and displayed");

        // Display hourglass cursor
        Utilities.changeCursorWaitStatus(true);

        // Empty the table
        for (int i = eTableImportHistory.getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) eTableImportHistory.getModel()).removeRow(i);
        }

        // Fill the table with the file import logs
        List<FileImport> fileImports = Wallet.getInstance().getFileImports();
        for (FileImport fileImport : fileImports) {
            ((DefaultTableModel) eTableImportHistory.getModel()).addRow(new Object[]{
                        fileImport.getImportedOn(),
                        fileImport.getFilePath(),
                        fileImport.getImportDuration(),
                        fileImport.getSuccess()});
        }

        // Resize the columns' widths
        Utilities.packColumns(eTableImportHistory);

        // Display normal cursor
        Utilities.changeCursorWaitStatus(false);
    }
}
