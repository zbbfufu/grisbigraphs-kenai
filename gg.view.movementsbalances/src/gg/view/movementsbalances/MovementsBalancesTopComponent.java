/*
 * MovementsBalancesTopComponent.java
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
package gg.view.movementsbalances;

import gg.searchfilter.FieldsVisibility;
import gg.db.datamodel.Datamodel;
import gg.db.datamodel.Period;
import gg.db.datamodel.PeriodType;
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
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
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
 * Top component which displays the movements' balances evolution over time.
 */
@ConvertAsProperties(dtd = "-//gg.view.movementsbalances//MovementsBalances//EN", autostore = false)
public final class MovementsBalancesTopComponent extends TopComponent implements LookupListener {

    /** Singleton instance of the topcomponent */
    private static MovementsBalancesTopComponent instance;
    /** Path to the icon used by the component and its open action */
    private static final String ICON_PATH = "gg/resources/icons/MovementsBalances.png";
    /** ID of the component */
    private static final String PREFERRED_ID = "MovementsBalancesTopComponent";
    /** Content available in the lookup of the topcomponent */
    private InstanceContent content = new InstanceContent();
    /** Result for the lookup listener */
    private Lookup.Result result = null;
    /** Currently displayed search filter */
    private SearchFilter displayedSearchFilter;
    /** Defines which filters are supported by this view */
    private FieldsVisibility fieldsVisibility = new FieldsVisibility();
    /** Logger */
    private Logger log = Logger.getLogger(this.getClass().getName());

    /** Creates a new instance of MovementsBalancesTopComponent */
    public MovementsBalancesTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(MovementsBalancesTopComponent.class, "CTL_MovementsBalancesTopComponent"));
        setToolTipText(NbBundle.getMessage(MovementsBalancesTopComponent.class, "HINT_MovementsBalancesTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

        // Outline settings
        outlineMovementsBalances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        outlineMovementsBalances.setRootVisible(false);
        outlineMovementsBalances.setPopupUsedFromTheCorner(false);
        outlineMovementsBalances.setColumnHidingAllowed(false);
        outlineMovementsBalances.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set the supported filters
        fieldsVisibility.setFromVisible(true);
        fieldsVisibility.setToVisible(true);
        fieldsVisibility.setByVisible(true);
        fieldsVisibility.setCurrencyVisible(true);
        fieldsVisibility.setAccountsVisible(true);
        content.set(Collections.singleton(fieldsVisibility), null);

        // Initialize the topcomponent's lookup
        associateLookup(new AbstractLookup(content));
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
     * @return Default instance
     */
    public static synchronized MovementsBalancesTopComponent getDefault() {
        if (instance == null) {
            instance = new MovementsBalancesTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the MovementsBalancesTopComponent instance. Never call {@link #getDefault} directly!
     * @return MovementsBalancesTopComponent instance
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
        MovementsBalancesTopComponent singleton = MovementsBalancesTopComponent.getDefault();
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

    /**
     * Registers a lookup listener on the search filter and opens the movements' balances group
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

            // Display the movements' balances
            resultChanged(null);
        }

        // Open movements' balances group
        TopComponentGroup movementsBalancesGroup = WindowManager.getDefault().findTopComponentGroup("MovementsBalancesGroup");
        if (movementsBalancesGroup != null) {
            movementsBalancesGroup.open();
        }
    }

    /**
     * Unregisters the lookup listener and close the movements' balances group
     * when the topcomponent is hidden
     */
    @Override
    protected void componentHidden() {
        super.componentHidden();

        // Remove listener on search filter top component
        result.removeLookupListener(this);
        result = null;

        // Close movements' balances group
        TopComponentGroup movementsBalancesGroup = WindowManager.getDefault().findTopComponentGroup("MovementsBalancesGroup");
        if (movementsBalancesGroup != null) {
            movementsBalancesGroup.close();
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
    
    /** Called when the lookup content is changed (button Search clicked in Search Filter tc) */
    @Override
    public void resultChanged(LookupEvent ev) {
        Collection instances = result.allInstances();

        if (!instances.isEmpty()) {
            // Get the filters specified by the user
            SearchFilter searchFilter = (SearchFilter) instances.iterator().next();

            // If the filters specified by the user are different from the ones that are already displayed, refresh table
            if (!isSame(searchFilter, displayedSearchFilter) &&
                    searchFilter.getPeriodType().compareTo(PeriodType.FREE) != 0) {
                displayData(searchFilter);
            }
        }
    }

    /**
     * Displays the currency/accounts' movements by period
     * @param searchFilter Search filter for which the table must be computed
     */
    private void displayData(SearchFilter searchFilter) {
        log.info("Movements' balances table computed and displayed");

        // Display hourglass cursor
        Utilities.changeCursorWaitStatus(true);

        // Prepare treetable
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(); // Root (Not displayed)

        // Map containing the movements' balances by currency/account and by search criteria
        // ((Currency/Account) --> (SearchCriteria --> Currency/Account movement balance))
        Map<MoneyContainer, Map<SearchCriteria, BigDecimal>> balances =
                new HashMap<MoneyContainer, Map<SearchCriteria, BigDecimal>>();
        
        // List of periods (one per column)
        Periods periods = new Periods(searchFilter.getFrom(),
                searchFilter.getTo(),
                searchFilter.getPeriodType());

        // List of search criteria (one per column)
        List<SearchCriteria> searchCriterias = new ArrayList<SearchCriteria>();
        for (Period period : periods.getPeriods()) {
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setPeriod(period);
            searchCriteria.setCurrency(searchFilter.getCurrency());
            searchCriteria.setAccounts(searchFilter.getAccounts());

            searchCriterias.add(searchCriteria);
        }

        // Add the currencies into the table
        for (Currency currency : Wallet.getInstance().getActiveCurrencies()) {
            if (!searchFilter.hasCurrencyFilter() ||
                    (searchFilter.hasCurrencyFilter() && searchFilter.getCurrency().compareTo(currency) == 0)) {
                // Add currency to the tree
                DefaultMutableTreeNode currencyNode = new DefaultMutableTreeNode(currency);
                rootNode.add(currencyNode);

                // Map containing the currency's movements by search criteria
                Map<SearchCriteria, BigDecimal> currencyBalances = new HashMap<SearchCriteria, BigDecimal>();
                for (SearchCriteria searchCriteria : searchCriterias) {
                    currencyBalances.put(searchCriteria, new BigDecimal(0));
                }

                // Compute the accounts' movements for each search criteria (for each period)
                for (Account account : Wallet.getInstance().getActiveAccountsWithCurrency().get(currency)) {
                    if (!searchFilter.hasAccountsFilter() ||
                            (searchFilter.hasAccountsFilter() && searchFilter.getAccounts().contains(account))) {
                        // Add account to the tree
                        DefaultMutableTreeNode accountNode = new DefaultMutableTreeNode(account);
                        currencyNode.add(accountNode);

                        // Compute the accounts' movements for each search criteria
                        Map<SearchCriteria, BigDecimal> accountBalances = new HashMap<SearchCriteria, BigDecimal>();
                        for (SearchCriteria searchCriteria : searchCriterias) {
                            SearchCriteria newSearchCriteria = new SearchCriteria();
                            newSearchCriteria.setCurrency(currency);
                            List<Account> accounts = new ArrayList<Account>();
                            accounts.add(account);
                            newSearchCriteria.setAccounts(accounts);
                            newSearchCriteria.setPeriod(searchCriteria.getPeriod());

                            BigDecimal accountBalance = Datamodel.getBalance(newSearchCriteria);
                            accountBalances.put(searchCriteria, accountBalance);

                            currencyBalances.put(searchCriteria,
                                    currencyBalances.get(searchCriteria).add(accountBalance));
                        }
                        balances.put(account, accountBalances);
                    }
                }

                balances.put(currency, currencyBalances);
            }
        }

        // Create the outline based on the model
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        OutlineModel outlineModel = DefaultOutlineModel.createOutlineModel(
                treeModel,
                new MovementsBalancesRowModel(searchCriterias, balances),
                true,
                NbBundle.getMessage(MovementsBalancesTopComponent.class, "MovementsBalancesTopComponent.Account"));
        outlineMovementsBalances.setModel(outlineModel);

        // Expand all nodes of the outline
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            outlineMovementsBalances.expandPath(new TreePath(((DefaultMutableTreeNode) rootNode.getChildAt(i)).getPath()));
        }

        // Resize the columns' widths
        Utilities.packColumns(outlineMovementsBalances);

        // Save the currently displayed search filter
        this.displayedSearchFilter = searchFilter;

        // Put the balances map in the lookup so that it can be displayed as a chart by another topcomponent
        content.set(Collections.singleton(balances), null);
        content.add(fieldsVisibility); // Add a description of the supported filters for the search filter topcomponent

        // Display normal cursor
        Utilities.changeCursorWaitStatus(false);
    }

    /** Row model for the Movements' balances outline */
    private class MovementsBalancesRowModel implements RowModel {

        /** List of search criteria (columns) */
        private List<SearchCriteria> searchCriterias;
        /** Currency/Account balances by search criteria */
        private Map<MoneyContainer, Map<SearchCriteria, BigDecimal>> balances;

        /**
         * Creates a new instance of MovementsBalancesRowModel
         * @param searchCriterias Search criteria to display (one per period)
         * @param balances Currency/Account balances by search criteria
         */
        public MovementsBalancesRowModel(List<SearchCriteria> searchCriterias, Map<MoneyContainer, Map<SearchCriteria, BigDecimal>> balances) {
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
                BigDecimal movementValue = balances.get(moneyContainer).get(searchCriteria);
                assert (movementValue != null);

                if (movementValue.compareTo(BigDecimal.ZERO) != 0 || Options.displayZero()) {
                    value = Utilities.getSignedBalance(movementValue);
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
