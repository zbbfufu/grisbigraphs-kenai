/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.view.movementsbalances;

import gg.application.components.FieldsVisibility;
import gg.db.datamodel.Datamodel;
import gg.db.datamodel.SearchFilter;
import gg.db.entities.Account;
import gg.db.entities.Currency;
import gg.db.entities.MoneyContainer;
import gg.utilities.Utilities;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.OutlineModel;
import org.netbeans.swing.outline.RowModel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponentGroup;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//gg.view.movementsbalances//MovementsBalances//EN",
autostore = false)
public final class MovementsBalancesTopComponent extends TopComponent implements LookupListener {

    private static MovementsBalancesTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "gg/resources/icons/MovementsBalances.png";
    private static final String PREFERRED_ID = "MovementsBalancesTopComponent";
    private InstanceContent content = new InstanceContent();
    private Lookup.Result result = null;
    private List<SearchFilter> displayedSearchFilters = new ArrayList<SearchFilter>();
    private FieldsVisibility fieldsVisibility = new FieldsVisibility();

    public MovementsBalancesTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(MovementsBalancesTopComponent.class, "CTL_MovementsBalancesTopComponent"));
        setToolTipText(NbBundle.getMessage(MovementsBalancesTopComponent.class, "HINT_MovementsBalancesTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

        associateLookup(new AbstractLookup(content));

        // Treetable settings
        outlineMovementsBalances.setRootVisible(false);
        outlineMovementsBalances.setPopupUsedFromTheCorner(false);

        fieldsVisibility.setFromVisible(true);
        fieldsVisibility.setToVisible(true);
        fieldsVisibility.setByVisible(true);
        fieldsVisibility.setCurrencyVisible(true);
        fieldsVisibility.setAccountsVisible(true);

        content.set(Collections.singleton(fieldsVisibility), null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPaneMovementsBalances = new javax.swing.JScrollPane();
        outlineMovementsBalances = new org.netbeans.swing.outline.Outline();

        jScrollPaneMovementsBalances.setViewportView(outlineMovementsBalances);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneMovementsBalances, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneMovementsBalances, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPaneMovementsBalances;
    private org.netbeans.swing.outline.Outline outlineMovementsBalances;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized MovementsBalancesTopComponent getDefault() {
        if (instance == null) {
            instance = new MovementsBalancesTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the MovementsBalancesTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized MovementsBalancesTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(MovementsBalancesTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof MovementsBalancesTopComponent) {
            return (MovementsBalancesTopComponent) win;
        }
        Logger.getLogger(MovementsBalancesTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();

        // Register lookup listener on the search filter top component
        result = WindowManager.getDefault().findTopComponent("SearchFilterTopComponent").getLookup().lookupResult(SearchFilter.class);
        result.addLookupListener(this);
        result.allInstances();
        resultChanged(null);

        TopComponentGroup movementsBalancesGroup = WindowManager.getDefault().findTopComponentGroup("MovementsBalancesGroup");
        if (movementsBalancesGroup == null) {
            return;
        }
        movementsBalancesGroup.open();
    }

    @Override
    protected void componentHidden() {
        super.componentHidden();

        // Remove listener on search filter top component
        result.removeLookupListener(this);
        result = null;

        TopComponentGroup movementsBalancesGroup = WindowManager.getDefault().findTopComponentGroup("MovementsBalancesGroup");
        if (movementsBalancesGroup == null) {
            return;
        }
        movementsBalancesGroup.close();
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        @SuppressWarnings("unchecked")
        List<SearchFilter> searchFilters = (List<SearchFilter>) result.allInstances();
        if (!searchFilters.isEmpty() && !searchFilters.equals(displayedSearchFilters)) {
            displayData(searchFilters);
        }
    }

    private void displayData(List<SearchFilter> searchFilters) {
        Utilities.changeCursorWaitStatus(true);

        // Prepare treetable
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(); // Root (Not displayed)
        Map<MoneyContainer, Map<SearchFilter, BigDecimal>> balances =
                new HashMap<MoneyContainer, Map<SearchFilter, BigDecimal>>(); // Map of currency/account and corresponding movement's balance

        for (Currency currency : Datamodel.getActiveCurrencies()) {
            if (!searchFilters.get(0).hasCurrencyFilter() ||
                    (searchFilters.get(0).hasCurrencyFilter() && searchFilters.get(0).getCurrency().compareTo(currency) == 0)) {
                // Add currency to the tree
                DefaultMutableTreeNode currencyNode = new DefaultMutableTreeNode(currency);
                rootNode.add(currencyNode);

                // Currency movements' for each search filter
                Map<SearchFilter, BigDecimal> currencyBalances = new HashMap<SearchFilter, BigDecimal>();
                for (SearchFilter searchFilter : searchFilters) {
                    currencyBalances.put(searchFilter, new BigDecimal(0));
                }

                // Compute the accounts' movements for each search filter
                for (Account account : Datamodel.getActiveAccounts(currency)) {
                    if (!searchFilters.get(0).hasAccountsFilter() ||
                            (searchFilters.get(0).hasAccountsFilter() && searchFilters.get(0).getAccounts().contains(account))) {
                        // Add account to the tree
                        DefaultMutableTreeNode accountNode = new DefaultMutableTreeNode(account);
                        currencyNode.add(accountNode);

                        // Compute the accounts' balances for each search filter
                        Map<SearchFilter, BigDecimal> accountBalances = new HashMap<SearchFilter, BigDecimal>();
                        for (SearchFilter searchFilter : searchFilters) {
                            SearchFilter newSearchFilter = new SearchFilter();
                            newSearchFilter.setCurrency(currency);
                            List<Account> accounts = new ArrayList<Account>();
                            accounts.add(account);
                            newSearchFilter.setAccounts(accounts);
                            newSearchFilter.setPeriod(searchFilter.getPeriod());

                            BigDecimal accountBalance = Datamodel.getBalance(newSearchFilter);
                            accountBalances.put(searchFilter, accountBalance);

                            currencyBalances.put(searchFilter,
                                    currencyBalances.get(searchFilter).add(accountBalance));
                        }
                        balances.put(account, accountBalances);
                    }
                }

                balances.put(currency, currencyBalances);
            }
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);

        OutlineModel outlineModel = DefaultOutlineModel.createOutlineModel(
                treeModel,
                new MovementsBalancesRowModel(searchFilters, balances, searchFilters.get(0).hasAccountsFilter()),
                true,
                "Account");
        outlineMovementsBalances.setModel(outlineModel);

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            outlineMovementsBalances.expandPath(new TreePath(((DefaultMutableTreeNode) rootNode.getChildAt(i)).getPath()));
        }

        this.displayedSearchFilters = searchFilters;

        // Put the balances in the lookup of the TC
        content.set(Collections.singleton(balances), null);
        content.add(fieldsVisibility);

        Utilities.changeCursorWaitStatus(false);
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        MovementsBalancesTopComponent singleton = MovementsBalancesTopComponent.getDefault();
        singleton.readPropertiesImpl(p);
        return singleton;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    private class MovementsBalancesRowModel implements RowModel {

        private List<SearchFilter> searchFilters;
        private Map<MoneyContainer, Map<SearchFilter, BigDecimal>> balances;
        private boolean accountFilter;

        public MovementsBalancesRowModel(List<SearchFilter> searchFilters, Map<MoneyContainer, Map<SearchFilter, BigDecimal>> balances, boolean accountFilter) {
            if (searchFilters == null) {
                throw new IllegalArgumentException("The parameter 'searchFilters' is null");
            }
            if (balances == null) {
                throw new IllegalArgumentException("The parameter 'balances' is null");
            }
            this.searchFilters = searchFilters;
            this.balances = balances;
            this.accountFilter = accountFilter;
        }

        @Override
        public Class getColumnClass(int column) {
            return String.class;
        }

        @Override
        public int getColumnCount() {
            return searchFilters.size();
        }

        @Override
        public String getColumnName(int column) {
            return searchFilters.get(column).getPeriod().toString();
        }

        @Override
        public Object getValueFor(Object node, int column) {
            Object nodeInfo = ((DefaultMutableTreeNode) node).getUserObject();

            if (accountFilter && nodeInfo instanceof Currency) {
                return "";
            }

            if (nodeInfo != null) {
                MoneyContainer moneyContainer = (MoneyContainer) nodeInfo;
                SearchFilter searchFilter = searchFilters.get(column);
                BigDecimal movementValue = balances.get(moneyContainer).get(searchFilter).setScale(2, RoundingMode.HALF_EVEN);

                if (movementValue.compareTo(BigDecimal.ZERO) == 0) {
                    return "";
                } else {
                    return Utilities.getSignedBalance(movementValue);
                }
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
