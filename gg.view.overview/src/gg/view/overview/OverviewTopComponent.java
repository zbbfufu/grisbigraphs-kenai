/*
 * OverviewTopComponent.java
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
package gg.view.overview;

import gg.db.datamodel.Datamodel;
import gg.db.entities.Account;
import gg.db.entities.Currency;
import gg.db.entities.MoneyContainer;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.OutlineModel;
import org.netbeans.swing.outline.RowModel;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.windows.TopComponentGroup;

/**
 * Top component which displays the overview
 */
final class OverviewTopComponent extends TopComponent {

    private static OverviewTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "gg/resources/icons/Overview.png";
    private static final String PREFERRED_ID = "OverviewTopComponent";

    private OverviewTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(OverviewTopComponent.class, "CTL_OverviewTopComponent"));
        setToolTipText(NbBundle.getMessage(OverviewTopComponent.class, "HINT_OverviewTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        outlineOverview.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        outlineOverview.setColumnHidingAllowed(false);
        outlineOverview.setPopupUsedFromTheCorner(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPaneOverview = new javax.swing.JScrollPane();
        outlineOverview = new org.netbeans.swing.outline.Outline();

        jScrollPaneOverview.setViewportView(outlineOverview);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneOverview, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneOverview, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPaneOverview;
    private org.netbeans.swing.outline.Outline outlineOverview;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized OverviewTopComponent getDefault() {
        if (instance == null) {
            instance = new OverviewTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the OverviewTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized OverviewTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(OverviewTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof OverviewTopComponent) {
            return (OverviewTopComponent) win;
        }
        Logger.getLogger(OverviewTopComponent.class.getName()).warning(
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
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(); // Root (Not displayed)
        Map<MoneyContainer, String> balances = new HashMap<MoneyContainer, String>(); // Map of currency/account and corresponding balance

        for (Currency currency : Datamodel.getCurrencies()) {
            if (currency.getActive()) { // Display only active currencies in the overview
                DefaultMutableTreeNode currencyNode = new DefaultMutableTreeNode(currency);
                rootNode.add(currencyNode);
                balances.put(currency, currency.getBalance().toString());

                for (Account account : currency.getAccounts()) {
                    if (account.getActive()) { // Display only active accounts
                        DefaultMutableTreeNode accountNode = new DefaultMutableTreeNode(account);
                        currencyNode.add(accountNode);
                        balances.put(account, account.getBalance().toString());
                    }
                }
            }
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);

        OutlineModel outlineModel = DefaultOutlineModel.createOutlineModel(
                treeModel, new OverviewRowModel(balances), true, "Account");

        outlineOverview.setModel(outlineModel);
        outlineOverview.setRootVisible(false);
        outlineOverview.setPopupUsedFromTheCorner(false);
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            outlineOverview.expandPath(new TreePath(((DefaultMutableTreeNode) rootNode.getChildAt(i)).getPath()));
        }
    }

    @Override
    public void componentClosed() {
        TopComponentGroup overviewGroup = WindowManager.getDefault().findTopComponentGroup("OverviewGroup");
        if (overviewGroup == null) {
            System.out.println("overviewgroup null!!");
            return;
        }
        overviewGroup.close();
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();
        TopComponentGroup overviewGroup = WindowManager.getDefault().findTopComponentGroup("OverviewGroup");
        if (overviewGroup == null) {
            return;
        }
        overviewGroup.open();
    }

    @Override
    protected void componentHidden() {
        super.componentDeactivated();
        TopComponentGroup overviewGroup = WindowManager.getDefault().findTopComponentGroup("OverviewGroup");
        if (overviewGroup == null) {
            return;
        }
        overviewGroup.close();
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
            return OverviewTopComponent.getDefault();
        }
    }

    private class OverviewRowModel implements RowModel {

        private Map<MoneyContainer, String> balances;

        public OverviewRowModel(Map<MoneyContainer, String> balances) {
            if (balances == null) {
                throw new IllegalArgumentException("The parameter 'balances' is null");
            }
            this.balances = balances;
        }

        @Override
        public Class getColumnClass(int column) {
            return String.class;
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public String getColumnName(int column) {
            return "Balance";
        }

        @Override
        public Object getValueFor(Object node, int column) {
            Object nodeInfo = ((DefaultMutableTreeNode) node).getUserObject();

            if (column == 0 && nodeInfo != null) {
                MoneyContainer moneyContainer = (MoneyContainer) nodeInfo;
                return balances.get(moneyContainer);
            }
            return null;
        }

        @Override
        public boolean isCellEditable(Object node, int column) {
            return false;
        }

        @Override
        public void setValueFor(Object node, int column, Object value) {
        }
    }
}
