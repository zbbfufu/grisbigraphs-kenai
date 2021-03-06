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
package gg.view.transactions;

import gg.db.datamodel.Datamodel;
import gg.db.datamodel.Period;
import gg.db.datamodel.PeriodType;
import gg.db.datamodel.SearchCriteria;
import gg.db.entities.Account;
import gg.db.entities.Category;
import gg.db.entities.Currency;
import gg.db.entities.Payee;
import gg.db.entities.Transaction;
import gg.searchfilter.FieldsVisibility;
import gg.searchfilter.SearchFilter;
import gg.utilities.Utilities;
import gg.wallet.Wallet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.joda.time.format.DateTimeFormat;
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

/**
 * Top component which displays transactions.
 */
@ConvertAsProperties(dtd = "-//gg.view.transactions//Transactions//EN", autostore = false)
public final class TransactionsTopComponent extends TopComponent implements LookupListener {

    /** Singleton instance of the topcomponent */
    private static TransactionsTopComponent instance;
    /** Path to the icon used by the component and its open action */
    private static final String ICON_PATH = "gg/resources/icons/Transactions.png";
    /** ID of the component */
    private static final String PREFERRED_ID = "TransactionsTopComponent";
    /** Content available in the lookup of the topcomponent */
    private InstanceContent content = new InstanceContent();
    /** Result for the lookup listener */
    private Lookup.Result<SearchFilter> result = null;
    /** Currently displayed search filter */
    private SearchFilter displayedSearchFilter;
    /** Logger */
    private Logger log = Logger.getLogger(this.getClass().getName());

    /** Creates a new instance of TransactionsTopComponent */
    public TransactionsTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(TransactionsTopComponent.class, "CTL_TransactionsTopComponent"));
        setToolTipText(NbBundle.getMessage(TransactionsTopComponent.class, "HINT_TransactionsTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

        // Outline settings
        outlineTransactions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        outlineTransactions.setRootVisible(false);
        outlineTransactions.setPopupUsedFromTheCorner(false);
        outlineTransactions.setColumnHidingAllowed(false);
        outlineTransactions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set the supported filters
        FieldsVisibility fieldsVisibility = new FieldsVisibility();
        fieldsVisibility.setFromVisible(true);
        fieldsVisibility.setToVisible(true);
        fieldsVisibility.setCurrencyVisible(true);
        fieldsVisibility.setAccountsVisible(true);
        fieldsVisibility.setCategoriesVisible(true);
        fieldsVisibility.setPayeesVisible(true);
        fieldsVisibility.setKeywordsVisible(true);

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

        jScrollPaneTransactions = new javax.swing.JScrollPane();
        outlineTransactions = new org.netbeans.swing.outline.Outline();

        jScrollPaneTransactions.setViewportView(outlineTransactions);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneTransactions, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneTransactions, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPaneTransactions;
    private org.netbeans.swing.outline.Outline outlineTransactions;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     * @return Default instance
     */
    public static synchronized TransactionsTopComponent getDefault() {
        if (instance == null) {
            instance = new TransactionsTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the TransactionsTopComponent instance. Never call {@link #getDefault} directly!
     * @return TransactionsTopComponent instance
     */
    public static synchronized TransactionsTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(TransactionsTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof TransactionsTopComponent) {
            return (TransactionsTopComponent) win;
        }
        Logger.getLogger(TransactionsTopComponent.class.getName()).warning(
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
    public void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    /**
     * Reads properties
     * @param p properties to save
     * @return TopComponent with loaded properties
     */
    public Object readProperties(java.util.Properties p) {
        TransactionsTopComponent singleton = TransactionsTopComponent.getDefault();
        singleton.readPropertiesImpl(p);
        return singleton;
    }

    /**
     * Reads properties
     * @param p Properties to read
     */
    private void readPropertiesImpl(java.util.Properties p) {
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

    /** Registers a lookup listener on the search filter when the topcomponent is activated */
    @Override
    protected void componentActivated() {
        super.componentActivated();

        if (result == null) {
            // Register lookup listener on the search filter top component
            result = WindowManager.getDefault().findTopComponent("SearchFilterTopComponent").getLookup().lookupResult(SearchFilter.class);
            result.addLookupListener(this);
            result.allInstances();

            // Display the transactions
            resultChanged(null);
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
                newSearchFilter.getCurrency().compareTo(oldSearchFilter.getCurrency()) == 0 &&
                newSearchFilter.getAccounts().equals(oldSearchFilter.getAccounts()) &&
                newSearchFilter.getCategories().equals(oldSearchFilter.getCategories()) &&
                newSearchFilter.getPayees().equals(oldSearchFilter.getPayees()) &&
                newSearchFilter.getKeywords().compareTo(oldSearchFilter.getKeywords()) == 0);
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
     * Displays the transactions
     * @param searchFilter Search filter for which the table must be computed
     */
    private void displayData(SearchFilter searchFilter) {
        log.info("Transactions table computed and displayed");

        // Display hourglass cursor
        Utilities.changeCursorWaitStatus(true);

        // Prepare the search criteria to query the database
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPeriod(new Period(searchFilter.getFrom(), searchFilter.getTo(), PeriodType.FREE));
        if (searchFilter.hasCurrencyFilter()) {
            searchCriteria.setCurrency(searchFilter.getCurrency());
        }
        if (searchFilter.hasAccountsFilter()) {
            searchCriteria.setAccounts(searchFilter.getAccounts());
        }
        if (searchFilter.hasPayeesFilter()) {
            searchCriteria.setPayees(searchFilter.getPayees());
        }
        if (searchFilter.hasKeywordsFilter()) {
            searchCriteria.setKeywords(searchFilter.getKeywords());
        }
        if (searchFilter.hasCategoriesFilter()) {
            List<Category> newCategories = new ArrayList<Category>();
            for (Category category : searchFilter.getCategories()) {
                newCategories.add(category);
                if (category.isTopCategory()) {
                    newCategories.addAll(Wallet.getInstance().getSubCategoriesWithParentCategory().get(category));
                }
            }
            searchCriteria.setCategories(newCategories);
        }

        // Get the transactions that match the search criteria
        List<Transaction> transactions = Datamodel.getTransactions(searchCriteria);

        // Get the payees and the categories for each transaction
        Map<Transaction, Payee> payeesWithTransaction = new HashMap<Transaction, Payee>();
        Map<Transaction, Category> categoriesWithTransaction = new HashMap<Transaction, Category>();
        for (Transaction transaction : transactions) {
            payeesWithTransaction.put(transaction, Wallet.getInstance().getPayeesWithId().get(transaction.getPayee().getId()));
            categoriesWithTransaction.put(transaction, Wallet.getInstance().getCategoriesWithId().get(transaction.getCategory().getId()));
        }

        // Prepare the tree
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(); // Root (Not displayed)
        for (Currency currency : Wallet.getInstance().getActiveCurrencies()) {
            // Add currency to the tree
            DefaultMutableTreeNode currencyNode = new DefaultMutableTreeNode(currency);

            // For each active account that belong to the currency
            for (Account account : Wallet.getInstance().getActiveAccountsWithCurrency().get(currency)) {
                // Add account to the tree
                DefaultMutableTreeNode accountNode = new DefaultMutableTreeNode(account);

                for (Transaction transaction : transactions) {
                    if (transaction.getAccount().getId().compareTo(account.getId()) == 0) {
                        // Add transactions that belong to the current account to the tree
                        DefaultMutableTreeNode transactionNode = new DefaultMutableTreeNode(transaction);
                        accountNode.add(transactionNode);
                    }
                }

                // If the account contains transactions matching the search criteria, display the account
                if (accountNode.getChildCount() > 0) {
                    currencyNode.add(accountNode);
                }
            }

            // If the currency contains accounts that contain transactions matching the search criteria, display the currency
            if (currencyNode.getChildCount() > 0) {
                rootNode.add(currencyNode);
            }
        }

        // Create the outline based on the model
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        OutlineModel outlineModel = DefaultOutlineModel.createOutlineModel(
                treeModel,
                new TransactionsRowModel(payeesWithTransaction, categoriesWithTransaction),
                true,
                NbBundle.getMessage(TransactionsTopComponent.class, "TransactionsTopComponent.Account"));
        outlineTransactions.setModel(outlineModel);

        // Expand all nodes of the outline
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode currencyNode = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            outlineTransactions.expandPath(new TreePath(currencyNode.getPath()));

            for (int j = 0; j < currencyNode.getChildCount(); j++) {
                DefaultMutableTreeNode accountNode = (DefaultMutableTreeNode) currencyNode.getChildAt(j);
                outlineTransactions.expandPath(new TreePath(accountNode.getPath()));
            }
        }

        // Resize the columns' widths
        Utilities.packColumns(outlineTransactions);

        // Save the currently displayed search filter
        this.displayedSearchFilter = searchFilter;

        // Display normal cursor
        Utilities.changeCursorWaitStatus(false);
    }

    /** Row model for the Accounts' Balances outline */
    private class TransactionsRowModel implements RowModel {

        /** Payees of the transactions */
        private Map<Transaction, Payee> payeesWithTransaction;
        /** Categories of the transactions */
        private Map<Transaction, Category> categoriesWithTransaction;
        /** Position of 'date' in the model */
        private static final byte COLUMN_DATE = 0;
        /** Position of 'payee' in the model */
        private static final byte COLUMN_PAYEE = 1;
        /** Position of 'category' in the model */
        private static final byte COLUMN_CATEGORY = 2;
        /** Position of 'amount' in the model */
        private static final byte COLUMN_AMOUNT = 3;
        /** Position of 'comment' in the model */
        private static final byte COLUMN_COMMENT = 4;

        /**
         * Creates a new instance of TransactionsRowModel
         * @param payeesWithTransaction Payees of the transactions
         * @param categoriesWithTransaction Categories of the transactions
         */
        public TransactionsRowModel(Map<Transaction, Payee> payeesWithTransaction,
                Map<Transaction, Category> categoriesWithTransaction) {
            this.payeesWithTransaction = payeesWithTransaction;
            this.categoriesWithTransaction = categoriesWithTransaction;
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
            return 5;
        }

        /**
         * Gets the name of a column
         * @param column Column position
         * @return Column name
         */
        @Override
        public String getColumnName(int column) {
            switch (column) {
                case COLUMN_DATE:
                    return NbBundle.getMessage(TransactionsTopComponent.class, "TransactionsTopComponent.Date");
                case COLUMN_PAYEE:
                    return NbBundle.getMessage(TransactionsTopComponent.class, "TransactionsTopComponent.Payee");
                case COLUMN_CATEGORY:
                    return NbBundle.getMessage(TransactionsTopComponent.class, "TransactionsTopComponent.Category");
                case COLUMN_AMOUNT:
                    return NbBundle.getMessage(TransactionsTopComponent.class, "TransactionsTopComponent.Amount");
                case COLUMN_COMMENT:
                    return NbBundle.getMessage(TransactionsTopComponent.class, "TransactionsTopComponent.Comment");
                default:
                    throw new AssertionError("Unknown column: " + column);
            }
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

            // The object is a transaction
            if (nodeUserObject instanceof Transaction) {
                Transaction transaction = (Transaction) nodeUserObject;
                switch (column) {
                    case COLUMN_DATE:
                        value = DateTimeFormat.longDate().withLocale(Locale.US).print(transaction.getDate().toDateMidnight());
                        break;
                    case COLUMN_PAYEE:
                        value = payeesWithTransaction.get(transaction).getName();
                        break;
                    case COLUMN_CATEGORY:
                        Category cat = categoriesWithTransaction.get(transaction);
                        if (cat.isTopCategory()) {
                            value = cat.getName();
                        } else if (cat.getGrisbiSubCategoryId() == Category.NO_SUB_CATEGORY_ID) {
                            value = cat.getParentCategory().getName();
                        } else {
                            value = cat.getParentCategory().getName() + " - " + cat.getName();
                        }
                        break;
                    case COLUMN_AMOUNT:
                        value = Utilities.getBalance(transaction.getAmount());
                        break;
                    case COLUMN_COMMENT:
                        value = transaction.getComment();
                        break;
                    default:
                        throw new AssertionError("Unknown column: " + column);
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
