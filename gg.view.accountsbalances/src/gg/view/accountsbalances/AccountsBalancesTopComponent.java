/*
 * AccountsBalancesTopComponent.java
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
package gg.view.accountsbalances;

import gg.searchfilter.FieldsVisibility;
import gg.db.datamodel.Datamodel;
import gg.db.datamodel.Period;
import gg.db.datamodel.Periods;
import gg.db.datamodel.SearchCriteria;
import gg.db.entities.Account;
import gg.db.entities.Currency;
import gg.db.entities.MoneyContainer;
import gg.options.Options;
import gg.searchfilter.SearchFilter;
import gg.utilities.Utilities;
import gg.wallet.Wallet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.OutlineModel;
import org.netbeans.swing.outline.RowModel;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.windows.TopComponentGroup;

/**
 * Top component which displays the accounts' balances evolution over time.
 */
@ConvertAsProperties(dtd = "-//gg.view.accountsbalances//AccountsBalances//EN", autostore = false)
public final class AccountsBalancesTopComponent extends TopComponent implements LookupListener {

    /** Singleton instance of the topcomponent */
    private static AccountsBalancesTopComponent instance;
    /** Path to the icon used by the component and its open action */
    private static final String ICON_PATH = "gg/resources/icons/AccountsBalances.png";
    /** ID of the component */
    private static final String PREFERRED_ID = "AccountsBalancesTopComponent";
    /** Content available in the lookup of the topcomponent */
    private InstanceContent content = new InstanceContent();
    /** Result for the lookup listener */
    private Lookup.Result<SearchFilter> result = null;
    /** Currently displayed search filter */
    private SearchFilter displayedSearchFilter;
    /** Defines which filters are supported by this view */
    private FieldsVisibility fieldsVisibility = new FieldsVisibility();
    /** Logger */
    private Logger log = Logger.getLogger(this.getClass().getName());

    /** Creates a new instance of AccountsBalancesTopComponent */
    public AccountsBalancesTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(AccountsBalancesTopComponent.class, "CTL_AccountsBalancesTopComponent"));
        setToolTipText(NbBundle.getMessage(AccountsBalancesTopComponent.class, "HINT_AccountsBalancesTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

        // Outline settings
        outlineAccountsBalances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        outlineAccountsBalances.setRootVisible(false);
        outlineAccountsBalances.setPopupUsedFromTheCorner(false);
        outlineAccountsBalances.setColumnHidingAllowed(false);
        outlineAccountsBalances.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set the supported filters
        fieldsVisibility.setFromVisible(true);
        fieldsVisibility.setToVisible(true);
        fieldsVisibility.setByVisible(true);
        fieldsVisibility.setCurrencyVisible(true);
        fieldsVisibility.setAccountsVisible(true);

        // Initialize the topcomponent's lookup
        content.set(Collections.singleton(fieldsVisibility), null);
        associateLookup(new AbstractLookup(content));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPaneAccountsBalances = new javax.swing.JScrollPane();
        outlineAccountsBalances = new org.netbeans.swing.outline.Outline();

        jScrollPaneAccountsBalances.setViewportView(outlineAccountsBalances);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneAccountsBalances, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneAccountsBalances, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPaneAccountsBalances;
    private org.netbeans.swing.outline.Outline outlineAccountsBalances;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     * @return Default instance
     */
    public static synchronized AccountsBalancesTopComponent getDefault() {
        if (instance == null) {
            instance = new AccountsBalancesTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the AccountsBalancesTopComponent instance. Never call {@link #getDefault} directly!
     * @return AccountsBalancesTopComponent instance
     */
    public static synchronized AccountsBalancesTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(AccountsBalancesTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof AccountsBalancesTopComponent) {
            return (AccountsBalancesTopComponent) win;
        }
        Logger.getLogger(AccountsBalancesTopComponent.class.getName()).warning(
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
        AccountsBalancesTopComponent singleton = AccountsBalancesTopComponent.getDefault();
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

    /**
     * Registers a lookup listener on the search filter and opens the accounts' balances group
     * when the topcomponent is activated
     */
    @Override
    protected void componentActivated() {
        super.componentActivated();

        if (result == null) {
            // Register lookup listener on the search filter top component
            result = WindowManager.getDefault().findTopComponent("SearchFilterTopComponent").getLookup().lookupResult(SearchFilter.class);
            result.addLookupListener(this);
            result.allInstances();

            // Display accounts' balances
            resultChanged(null);
        }

        // Open accounts' balance group
        TopComponentGroup accountsBalancesGroup = WindowManager.getDefault().findTopComponentGroup("AccountsBalancesGroup");
        if (accountsBalancesGroup != null) {
            accountsBalancesGroup.open();
        }
    }

    /**
     * Unregisters the lookup listener and close the accounts' balances group
     * when the topcomponent is hidden
     */
    @Override
    protected void componentHidden() {
        super.componentHidden();

        // Remove listener on search filter top component
        if (result != null) {
            result.removeLookupListener(this);
            result = null;
        }

        // Close accounts' balances group
        TopComponentGroup accountsBalancesGroup = WindowManager.getDefault().findTopComponentGroup("AccountsBalancesGroup");
        if (accountsBalancesGroup != null) {
            accountsBalancesGroup.close();
        }
    }

    /**
     * Are two search filters identic?
     * @param newSearchFilter New search filter
     * @param oldSearchFilter Old search filter
     * @return true if the search filters have the same values for the fields that are supported by this view
     */
    private boolean isSame(SearchFilter newSearchFilter, SearchFilter oldSearchFilter) {
        assert (newSearchFilter != null);

        return (oldSearchFilter != null &&
                newSearchFilter.getFrom().compareTo(oldSearchFilter.getFrom()) == 0 &&
                newSearchFilter.getTo().compareTo(oldSearchFilter.getTo()) == 0 &&
                newSearchFilter.getPeriodType().compareTo(oldSearchFilter.getPeriodType()) == 0 &&
                newSearchFilter.getCurrency().compareTo(oldSearchFilter.getCurrency()) == 0 &&
                newSearchFilter.getAccounts().equals(oldSearchFilter.getAccounts()));
    }

    /** Called when the lookup content is changed (button 'Search' clicked in the 'Search Filter' tc) */
    @Override
    public void resultChanged(LookupEvent ev) {
        Collection instances = result.allInstances();

        if (!instances.isEmpty()) {
            // Get the filters specified by the user
            SearchFilter searchFilter = (SearchFilter) instances.iterator().next();

            // If the filters specified by the user are different from the ones that are already displayed, refresh table
            if (!isSame(searchFilter, displayedSearchFilter)) {
                displayData(searchFilter);
            }
        }
    }

    /**
     * Displays the currency/accounts' balances by period
     * @param searchFilter Search filter for which the table must be computed
     */
    private void displayData(SearchFilter searchFilter) {
        log.info("Accounts' balances table computed and displayed");

        // Display hourglass cursor
        Utilities.changeCursorWaitStatus(true);

        // Map containing the balances by currency/account and by search criteria
        // ((Currency/Account) --> (SearchCriteria --> Currency/Account balance))
        Map<MoneyContainer, Map<SearchCriteria, BigDecimal>> moneyContainerBalances =
                new HashMap<MoneyContainer, Map<SearchCriteria, BigDecimal>>();

        // List of search criteria (one per column)
        List<SearchCriteria> searchCriterias = new ArrayList<SearchCriteria>();

        // List of periods (one per column)
        Periods periods = new Periods(searchFilter.getFrom(),
                searchFilter.getTo(),
                searchFilter.getPeriodType());

        // Compute the accounts' balances for each period
        for (Period period : periods.getPeriods()) {

            // Define the search criteria to query the database
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setPeriod(period);
            searchCriteria.setCurrency(searchFilter.getCurrency());
            searchCriteria.setAccounts(searchFilter.getAccounts());

            searchCriterias.add(searchCriteria);

            // Get all accounts' balances for the current search criteria
            List accountsBalancesForCurrentSearchFilter = Datamodel.getAccountsBalancesUntil(searchCriteria);

            // For each account, add (SearchCriteria --> Account balance)
            for (Object rowAccountBalanceObject : accountsBalancesForCurrentSearchFilter) {
                Object[] rowAccountBalance = (Object[]) rowAccountBalanceObject;
                // Get account
                Long accountId = (Long) rowAccountBalance[0];
                assert (accountId != null);
                Account account = Wallet.getInstance().getAccountsWithId().get(accountId);
                assert (account != null);
                // Get account balance
                BigDecimal accountBalance = (BigDecimal) rowAccountBalance[1];
                assert (accountBalance != null);
                accountBalance = accountBalance.add(account.getInitialAmount());

                // Map that contains the account's balance for the current search criteria
                // (SearchCriteria --> Account balance)
                Map<SearchCriteria, BigDecimal> balancesForCurrentAccount =
                        new HashMap<SearchCriteria, BigDecimal>();
                // If accounts' balances have already been added for this account
                if (moneyContainerBalances.get(account) != null) {
                    balancesForCurrentAccount.putAll(moneyContainerBalances.get(account));
                }
                // Add the balance of the current account
                balancesForCurrentAccount.put(searchCriteria, accountBalance);


                // Add (SearchCriteria, Account balance) to the current account
                moneyContainerBalances.put(account, balancesForCurrentAccount);
            }

            // Compute the currency balance
            for (Currency currency : Wallet.getInstance().getActiveCurrencies()) {
                BigDecimal currencyBalance = new BigDecimal(0);

                // For each active account that belong to the currency
                for (Account account : Wallet.getInstance().getActiveAccountsWithCurrency().get(currency)) {
                    if (!searchCriteria.hasAccountsFilter() ||
                            searchCriteria.getAccounts().contains(account)) {
                        // Map that contains the current account's balances
                        Map<SearchCriteria, BigDecimal> balancesForCurrentAccount =
                                moneyContainerBalances.get(account);
                        if (balancesForCurrentAccount != null) {
                            // Get the balance of the account for the current search criteria
                            BigDecimal accountBalance = balancesForCurrentAccount.get(searchCriteria);
                            assert (accountBalance != null);

                            // Add it to the currency's balance
                            currencyBalance = currencyBalance.add(accountBalance);
                        }
                    }
                }

                // Map that contains the currency's balances for the current currency
                // (Search criteria --> Currency's balance)
                Map<SearchCriteria, BigDecimal> balancesForCurrentCurrency = new HashMap<SearchCriteria, BigDecimal>();
                // If currency' balances have already been added for other search criteria
                if (moneyContainerBalances.get(currency) != null) {
                    balancesForCurrentCurrency.putAll(moneyContainerBalances.get(currency));
                }
                // Add the balance of the currency for the current search criteria
                balancesForCurrentCurrency.put(searchCriteria, currencyBalance);

                // Add (SearchCriteria, Currency balance) to the current currency
                moneyContainerBalances.put(currency, balancesForCurrentCurrency);
            }
        }

        // Prepare the tree
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(); // Root (Not displayed)

        // Add the currencies and the accounts into the tree
        for (Currency currency : Wallet.getInstance().getActiveCurrencies()) {
            // Add the currency <currency> if
            // - the user didn't select any currency in the search filter or
            // - <currency> has been selected in the search filter
            if (!searchFilter.hasCurrencyFilter() ||
                    searchFilter.getCurrency().compareTo(currency) == 0) {
                // Add currency to the tree
                DefaultMutableTreeNode currencyNode = new DefaultMutableTreeNode(currency);
                rootNode.add(currencyNode);

                // Add the accounts of <currency> into the tree
                for (Account account : Wallet.getInstance().getActiveAccountsWithCurrency().get(currency)) {
                    // Add the account <account> if:
                    // - the user didn't select any account in the search filter or
                    // - <account> has been selected in the search filter
                    if (!searchFilter.hasAccountsFilter() ||
                            searchFilter.getAccounts().contains(account)) {
                        // Add account to the tree
                        DefaultMutableTreeNode accountNode = new DefaultMutableTreeNode(account);
                        currencyNode.add(accountNode);
                    }
                }
            }
        }

        // Create the outline based on the model
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        OutlineModel outlineModel = DefaultOutlineModel.createOutlineModel(
                treeModel,
                new AccountsBalancesRowModel(searchCriterias, moneyContainerBalances),
                true,
                NbBundle.getMessage(AccountsBalancesTopComponent.class, "AccountsBalancesTopComponent.Account"));
        outlineAccountsBalances.setModel(outlineModel);

        // Expand all nodes of the outline
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            outlineAccountsBalances.expandPath(new TreePath(((DefaultMutableTreeNode) rootNode.getChildAt(i)).getPath()));
        }

        // Resize the columns' widths
        Utilities.packColumns(outlineAccountsBalances);

        // Save the currently displayed search filter
        this.displayedSearchFilter = searchFilter;

        // Put the balances map so that it can be displayed as a chart by another topcomponent
        content.set(Collections.singleton(moneyContainerBalances), null);
        content.add(fieldsVisibility); // Add a description of the supported filters for the search filter topcomponent

        // Display normal cursor
        Utilities.changeCursorWaitStatus(false);
    }

    /** Row model for the Accounts' Balances outline */
    private class AccountsBalancesRowModel implements RowModel {

        /** List of search criteria (columns) */
        private List<SearchCriteria> searchCriterias;
        /** Currency/Account balances by search criteria */
        private Map<MoneyContainer, Map<SearchCriteria, BigDecimal>> balances;

        /**
         * Creates a new instance of AccountsBalancesRowModel
         * @param searchCriterias Search criteria to display (one per column)
         * @param balances Currency/Account balances by search criteria
         */
        public AccountsBalancesRowModel(List<SearchCriteria> searchCriterias, Map<MoneyContainer, Map<SearchCriteria, BigDecimal>> balances) {
            if (searchCriterias == null) {
                throw new IllegalArgumentException("The parameter 'searchCriterias' is null");
            }
            if (balances == null) {
                throw new IllegalArgumentException("The parameter 'balances' is null");
            }
            this.searchCriterias = searchCriterias;
            this.balances = balances;
        }

        /**
         * Gets the type of a column
         * @param column Column position
         * @return Type of the column
         */
        @Override
        public Class getColumnClass(int column) {
            return String.class;
        }

        /**
         * Gets the number of columns
         * @return Number of columns
         */
        @Override
        public int getColumnCount() {
            return searchCriterias.size();
        }

        /**
         * Gets the name of a column
         * @param column Column position
         * @return Column name
         */
        @Override
        public String getColumnName(int column) {
            return searchCriterias.get(column).getPeriod().toString();
        }

        /**
         * Gets the value of a cell
         * @param node Node
         * @param column Column position
         * @return Cell value
         */
        @Override
        public Object getValueFor(Object node, int column) {
            // Value to display in the current cell
            String value = "";

            // Get the object contained in the current cell
            Object nodeUserObject = ((DefaultMutableTreeNode) node).getUserObject();
            assert (nodeUserObject != null);

            // The object is either a currency or an account
            MoneyContainer moneyContainer = (MoneyContainer) nodeUserObject;

            // Display the currency or the account's balance value
            // - if the object is an account or
            // - if the object is a currency and if the user wants to see the sums
            if (moneyContainer instanceof Account || Options.calculateSums()) {
                SearchCriteria searchCriteria = searchCriterias.get(column);
                assert (searchCriteria != null);
                BigDecimal moneyContainerBalance = balances.get(moneyContainer).get(searchCriteria);
                assert (moneyContainerBalance != null);
                if (moneyContainerBalance.compareTo(BigDecimal.ZERO) != 0 || Options.displayZero()) {
                    value = Utilities.getBalance(moneyContainerBalance);
                }
            }

            return value;
        }

        /**
         * Is a cell editable?
         * @param node Node
         * @param column Column position
         * @return true if the cell can be edited
         */
        @Override
        public boolean isCellEditable(Object node, int column) {
            return false;
        }

        /**
         * Sets a value for a cell
         * @param node Node
         * @param column Column position
         * @param value New cell value
         */
        @Override
        public void setValueFor(Object node, int column, Object value) {
            // Not needed
        }
    }
}
