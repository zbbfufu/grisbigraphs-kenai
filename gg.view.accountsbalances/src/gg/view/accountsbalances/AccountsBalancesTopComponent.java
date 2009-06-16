/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.view.accountsbalances;

import gg.searchfilter.FieldsVisibility;
import gg.db.datamodel.Datamodel;
import gg.db.datamodel.SearchFilter;
import gg.db.entities.Account;
import gg.db.entities.Currency;
import gg.db.entities.MoneyContainer;
import gg.options.Options;
import gg.utilities.Utilities;
import gg.wallet.Wallet;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
 * Top component which displays something.
 */
public final class AccountsBalancesTopComponent extends TopComponent implements LookupListener {

    private static AccountsBalancesTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "gg/resources/icons/AccountsBalances.png";
    private static final String PREFERRED_ID = "AccountsBalancesTopComponent";
    private InstanceContent content = new InstanceContent();
    private Lookup.Result<SearchFilter> result = null;
    private List<SearchFilter> displayedSearchFilters = new ArrayList<SearchFilter>();
    private FieldsVisibility fieldsVisibility = new FieldsVisibility();

    private AccountsBalancesTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(AccountsBalancesTopComponent.class, "CTL_AccountsBalancesTopComponent"));
        setToolTipText(NbBundle.getMessage(AccountsBalancesTopComponent.class, "HINT_AccountsBalancesTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

        // Treetable settings
        outlineAccountsBalances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        outlineAccountsBalances.setRootVisible(false);
        outlineAccountsBalances.setPopupUsedFromTheCorner(false);
        outlineAccountsBalances.setColumnHidingAllowed(false);

        // Set the supported fields from the search filter
        fieldsVisibility.setFromVisible(true);
        fieldsVisibility.setToVisible(true);
        fieldsVisibility.setByVisible(true);
        fieldsVisibility.setCurrencyVisible(true);
        fieldsVisibility.setAccountsVisible(true);
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
     */
    public static synchronized AccountsBalancesTopComponent getDefault() {
        if (instance == null) {
            instance = new AccountsBalancesTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the AccountsBalancesTopComponent instance. Never call {@link #getDefault} directly!
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

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
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

    @Override
    protected void componentActivated() {
        super.componentActivated();

        // Register lookup listener on the search filter top component
        if (result == null) {
            result = WindowManager.getDefault().findTopComponent("SearchFilterTopComponent").getLookup().lookupResult(SearchFilter.class);
            result.addLookupListener(this);
            result.allInstances();
            resultChanged(null);
        }

        TopComponentGroup accountsBalancesGroup = WindowManager.getDefault().findTopComponentGroup("AccountsBalancesGroup");
        if (accountsBalancesGroup == null) {
            return;
        }
        accountsBalancesGroup.open();
    }

    @Override
    protected void componentHidden() {
        super.componentHidden();

        // Remove listener on search filter top component
        result.removeLookupListener(this);
        result = null;

        TopComponentGroup accountsBalancesGroup = WindowManager.getDefault().findTopComponentGroup("AccountsBalancesGroup");
        if (accountsBalancesGroup == null) {
            return;
        }
        accountsBalancesGroup.close();
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
        long start = System.currentTimeMillis();

        // (Account ID --> Account)
        Map<Long, Account> accountsMap = Wallet.getInstance().getAccountsWithId();
        // (Currency --> List of accounts)
        Map<Currency, List<Account>> currencyAccountsMap = new HashMap<Currency, List<Account>>();
        List<Currency> currencies = Wallet.getInstance().getActiveCurrencies();
        for (Currency currency : currencies) {
            List<Account> currencyAccounts = Wallet.getInstance().getActiveAccountsWithCurrency().get(currency);
            currencyAccountsMap.put(currency, currencyAccounts);
        }

        // Map that contains the accounts' balances by currency/account and search filter
        // ((Currency/Account) --> (SearchFilter --> Currency/Account balance))
        Map<MoneyContainer, Map<SearchFilter, BigDecimal>> moneyContainerBalancesMap =
                new HashMap<MoneyContainer, Map<SearchFilter, BigDecimal>>();

        // Compute the accounts' balances for each search filter
        for (SearchFilter searchFilter : searchFilters) {
            // Get all accounts' balances for the current search filter
            List accountsBalancesForCurrentSearchFilter = Datamodel.getAccountsBalancesUntil(searchFilter);

            // For each account, add (SearchFilter --> Account balance)
            for (Object rowAccountBalanceObject : accountsBalancesForCurrentSearchFilter) {
                Object[] rowAccountBalance = (Object[]) rowAccountBalanceObject;
                Long accountId = (Long) rowAccountBalance[0];
                Account account = accountsMap.get(accountId);
                assert (account != null);
                BigDecimal accountBalance = (BigDecimal) rowAccountBalance[1];
                assert (accountBalance != null);
                accountBalance = accountBalance.add(account.getInitialAmount());

                // Map that contains the account's balance for the current search filter
                // (SearchFilter --> Account balance)
                Map<SearchFilter, BigDecimal> accountBalanceForCurrentSearchFilterMap =
                        new HashMap<SearchFilter, BigDecimal>();
                // If accounts' balances have already been added for other SearchFilters
                if (moneyContainerBalancesMap.get(account) != null) {
                    accountBalanceForCurrentSearchFilterMap.putAll(moneyContainerBalancesMap.get(account));
                }
                // Add the balance of the current account
                accountBalanceForCurrentSearchFilterMap.put(searchFilter, accountBalance);


                // Add (SearchFilter, Account balance) to the current account
                moneyContainerBalancesMap.put(account, accountBalanceForCurrentSearchFilterMap);
            }

            // Compute the currency balance
            for (Currency currency : currencies) {
                BigDecimal currencyBalance = new BigDecimal(0);

                for (Account account : currencyAccountsMap.get(currency)) {
                    if (!searchFilters.get(0).hasAccountsFilter() ||
                            searchFilters.get(0).getAccounts().contains(account)) {
                        Map<SearchFilter, BigDecimal> accountBalanceForCurrentSearchFilterMap =
                                moneyContainerBalancesMap.get(account);
                        if (accountBalanceForCurrentSearchFilterMap != null) {
                            // Get the balance of the accountfor the current search filter
                            BigDecimal accountBalance = accountBalanceForCurrentSearchFilterMap.get(searchFilter);

                            // Add it to the currency's balance
                            if (accountBalance != null) {
                                currencyBalance = currencyBalance.add(accountBalance);
                            }
                        }
                    }
                }

                // Map that contains the currency's balance for the current search filter
                // (Search filter --> Currency's balance)
                Map<SearchFilter, BigDecimal> currencyBalancesMap =
                        new HashMap<SearchFilter, BigDecimal>();
                // If currency' balances have already been added for other SearchFilters
                if (moneyContainerBalancesMap.get(currency) != null) {
                    currencyBalancesMap.putAll(moneyContainerBalancesMap.get(currency));
                }
                // Add the balance of the currency
                currencyBalancesMap.put(searchFilter, currencyBalance);

                // Add (SearchFilter, Currency balance> to the current currency
                moneyContainerBalancesMap.put(currency, currencyBalancesMap);
            }
        }

        // Prepare treetable
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(); // Root (Not displayed)

        // Add the currencies and the accounts into the tree
        for (Currency currency : currencies) {
            // Add the currency <currency> if
            // - the user didn't select any currency in the search filter or
            // - <currency> has been selected in the search filter
            if (!searchFilters.get(0).hasCurrencyFilter() ||
                    searchFilters.get(0).getCurrency().compareTo(currency) == 0) {
                // Add currency to the tree
                DefaultMutableTreeNode currencyNode = new DefaultMutableTreeNode(currency);
                rootNode.add(currencyNode);

                // Add the accounts of <currency> into the tree
                for (Account account : currencyAccountsMap.get(currency)) {
                    // Add the account <account> if:
                    // - the user didn't select any account in the search filter or
                    // - <account> has been selected in the search filter
                    if (!searchFilters.get(0).hasAccountsFilter() ||
                            searchFilters.get(0).getAccounts().contains(account)) {
                        // Add account to the tree
                        DefaultMutableTreeNode accountNode = new DefaultMutableTreeNode(account);
                        currencyNode.add(accountNode);
                    }
                }
            }
        }

        // Create the tree model based on the root
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);

        // Create the outline model based on the tree model
        OutlineModel outlineModel = DefaultOutlineModel.createOutlineModel(
                treeModel,
                new AccountsBalancesRowModel(searchFilters, moneyContainerBalancesMap),
                true,
                "Account");
        outlineAccountsBalances.setModel(outlineModel);

        // Expand the tree
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            outlineAccountsBalances.expandPath(new TreePath(((DefaultMutableTreeNode) rootNode.getChildAt(i)).getPath()));
        }

        // Save the currently displayed list of search filters
        this.displayedSearchFilters = searchFilters;

        content.set(Collections.singleton(moneyContainerBalancesMap), null);
        content.add(fieldsVisibility);

        long end = System.currentTimeMillis();
        System.out.println("duration=" + (end - start));
        Utilities.changeCursorWaitStatus(false);
    }

    private class AccountsBalancesRowModel implements RowModel {

        private List<SearchFilter> searchFilters;
        private Map<MoneyContainer, Map<SearchFilter, BigDecimal>> balances;

        public AccountsBalancesRowModel(List<SearchFilter> searchFilters, Map<MoneyContainer, Map<SearchFilter, BigDecimal>> balances) {
            if (searchFilters == null) {
                throw new IllegalArgumentException("The parameter 'searchFilters' is null");
            }
            if (balances == null) {
                throw new IllegalArgumentException("The parameter 'balances' is null");
            }
            this.searchFilters = searchFilters;
            this.balances = balances;
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
                SearchFilter searchFilter = searchFilters.get(column);
                Map<SearchFilter, BigDecimal> moneyContainerBalances = balances.get(moneyContainer);
                if (moneyContainerBalances != null) {
                    BigDecimal moneyContainerBalanceSearchFilter = moneyContainerBalances.get(searchFilter);
                    assert (moneyContainerBalanceSearchFilter != null);
                    if (moneyContainerBalanceSearchFilter.compareTo(BigDecimal.ZERO) != 0 ||
                            Options.displayZero()) {
                        value = Utilities.getBalance(moneyContainerBalanceSearchFilter);
                    }
                }
            }

            return value;
        }

        @Override
        public boolean isCellEditable(Object node, int column) {
            return false;
        }

        @Override
        public void setValueFor(Object node, int column, Object value) {
        }
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return AccountsBalancesTopComponent.getDefault();
        }
    }
}
