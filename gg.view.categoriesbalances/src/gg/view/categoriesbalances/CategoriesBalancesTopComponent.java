/*
 * CategoriesBalancesTopComponent.java
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
package gg.view.categoriesbalances;

import gg.searchfilter.FieldsVisibility;
import gg.db.datamodel.Datamodel;
import gg.db.datamodel.Period;
import gg.db.datamodel.Periods;
import gg.db.datamodel.SearchCriteria;
import gg.db.entities.Category;
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
 * Top component which displays the balances' evolution by categories/sub-categories and by period.
 */
@ConvertAsProperties(dtd = "-//gg.view.categoriesbalances//CategoriesBalances//EN", autostore = false)
public final class CategoriesBalancesTopComponent extends TopComponent implements LookupListener {

    /** Singleton instance of the topcomponent */
    private static CategoriesBalancesTopComponent instance;
    /** path to the icon used by the component and its open action */
    private static final String ICON_PATH = "gg/resources/icons/CategoriesBalances.png";
    /** ID of the component */
    private static final String PREFERRED_ID = "CategoriesBalancesTopComponent";
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

    /** Creates a new instance of CategoriesBalancesTopComponent */
    public CategoriesBalancesTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(CategoriesBalancesTopComponent.class, "CTL_CategoriesBalancesTopComponent"));
        setToolTipText(NbBundle.getMessage(CategoriesBalancesTopComponent.class, "HINT_CategoriesBalancesTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

        // Outline settings
        outlineCategoriesBalances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        outlineCategoriesBalances.setRootVisible(false);
        outlineCategoriesBalances.setPopupUsedFromTheCorner(false);
        outlineCategoriesBalances.setColumnHidingAllowed(false);
        outlineCategoriesBalances.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set the supported filters
        fieldsVisibility.setFromVisible(true);
        fieldsVisibility.setToVisible(true);
        fieldsVisibility.setByVisible(true);
        fieldsVisibility.setCurrencyVisible(true);
        fieldsVisibility.setAccountsVisible(true);
        fieldsVisibility.setCategoriesVisible(true);
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

        jScrollPaneCategoriesBalances = new javax.swing.JScrollPane();
        outlineCategoriesBalances = new org.netbeans.swing.outline.Outline();

        jScrollPaneCategoriesBalances.setViewportView(outlineCategoriesBalances);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneCategoriesBalances, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneCategoriesBalances, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPaneCategoriesBalances;
    private org.netbeans.swing.outline.Outline outlineCategoriesBalances;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     * @return Default instance
     */
    public static synchronized CategoriesBalancesTopComponent getDefault() {
        if (instance == null) {
            instance = new CategoriesBalancesTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the CategoriesBalancesTopComponent instance. Never call {@link #getDefault} directly!
     * @return CategoriesBalancesTopComponent instance
     */
    public static synchronized CategoriesBalancesTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(CategoriesBalancesTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof CategoriesBalancesTopComponent) {
            return (CategoriesBalancesTopComponent) win;
        }
        Logger.getLogger(CategoriesBalancesTopComponent.class.getName()).warning(
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
        CategoriesBalancesTopComponent singleton = CategoriesBalancesTopComponent.getDefault();
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
     * Registers a lookup listener on the search filter and opens the categories' balances group
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

        // Open categories' balance group
        TopComponentGroup categoriesBalancesGroup = WindowManager.getDefault().findTopComponentGroup("CategoriesBalancesGroup");
        if (categoriesBalancesGroup != null) {
            categoriesBalancesGroup.open();
        }
    }

    /**
     * Unregisters the lookup listener and close the categories' balances group
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

        // Close categories' balance group
        TopComponentGroup categoriesBalancesGroup = WindowManager.getDefault().findTopComponentGroup("CategoriesBalancesGroup");
        if (categoriesBalancesGroup != null) {
            categoriesBalancesGroup.close();
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
                newSearchFilter.getAccounts().equals(oldSearchFilter.getAccounts()) &&
                newSearchFilter.getCategories().equals(oldSearchFilter.getCategories()));
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
     * Should a category be added to the tree?<BR/>
     * A category must be added to the tree in the following cases:
     * <UL>
     * <LI>The category has been selected in the search filter window</LI>
     * <LI>The parent category of the category has been selected in the search filter window</LI>
     * <LI>A sub-category of the category has been selected in the search filter window</LI>
     * </UL>
     * @param selectedCategories List of categories that have been selected by the user in the search filter window
     * @param category Category to test (should this category be added to the tree?)
     * @return true if the category must be added into the tree, false otherwise
     */
    private boolean isCategoryToDisplay(List<Category> selectedCategories, Category category) {
        if (selectedCategories.contains(category)) {
            // If <category> has been selected in the search filter window
            return true;
        }

        for (Category selectedCategory : selectedCategories) {
            if (selectedCategory.isTopCategory() && Wallet.getInstance().getSubCategoriesWithParentCategory().get(selectedCategory).contains(category)) {
                // If a sub-category of <category> has been selected in the search filter window
                return true;
            } else if (!selectedCategory.isTopCategory() &&
                    selectedCategory.getParentCategory().getId().compareTo(category.getId()) == 0) {
                // If the parent category of <category> has been selected in the search filter window
                return true;
            }
        }

        return false;
    }

    /**
     * Displays the categories/sub-categories balances by period
     * @param searchFilter Search filter for which the table must be computed
     */
    private void displayData(SearchFilter searchFilter) {
        log.info("Categories' balances table computed and displayed");

        // Display hourglass cursor
        Utilities.changeCursorWaitStatus(true);

        // Map containing the categories' balances by category ID and search criteria
        // ((Category ID) --> (SearchCriteria --> Category balance))
        Map<Long, Map<SearchCriteria, BigDecimal>> categoryBalances =
                new HashMap<Long, Map<SearchCriteria, BigDecimal>>();

        // List of search criteria (one per column)
        List<SearchCriteria> searchCriterias = new ArrayList<SearchCriteria>();

        // List of periods (one per column)
        Periods periods = new Periods(searchFilter.getFrom(),
                searchFilter.getTo(),
                searchFilter.getPeriodType());

        // Compute the sub-categories' balances (categories have always a balance of 0)
        for (Period period : periods.getPeriods()) {
            // Define the search criteria to query the database
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setPeriod(period);
            searchCriteria.setCurrency(searchFilter.getCurrency());
            searchCriteria.setAccounts(searchFilter.getAccounts());
            searchCriteria.setCategories(searchFilter.getCategories());

            searchCriterias.add(searchCriteria);

            // Get all categories' (categories and sub-categories) balances for the current search criteria
            List categoriesBalancesForCurrentSearchFilter = Datamodel.getCategoriesBalances(searchCriteria);

            // For each category, add (SearchCriteria --> Category balance)
            for (Object rowCategoryBalanceObject : categoriesBalancesForCurrentSearchFilter) {
                Object[] rowCategoryBalance = (Object[]) rowCategoryBalanceObject;
                // Get category ID
                Long categoryId = (Long) rowCategoryBalance[0];
                assert (categoryId != null);
                // Get category balance
                BigDecimal categoryBalance = (BigDecimal) rowCategoryBalance[1];
                assert (categoryBalance != null);

                // Map containing the category's balances
                // (SearchCriteria --> Category balance)
                Map<SearchCriteria, BigDecimal> balancesForCurrentCategory =
                        new HashMap<SearchCriteria, BigDecimal>();
                // If categories' balances have already been added for other search criteria
                if (categoryBalances.get(categoryId) != null) {
                    balancesForCurrentCategory.putAll(categoryBalances.get(categoryId));
                }
                // Add the balance of the current category
                balancesForCurrentCategory.put(searchCriteria, categoryBalance);


                // Add (SearchCriteria, Category balance) to the current category
                categoryBalances.put(categoryId, balancesForCurrentCategory);
            }

            // Compute the top-categories' balances
            for (Category topCategory : Wallet.getInstance().getTopCategories()) {
                // Initialize the category's balance
                BigDecimal topCategoryBalance = new BigDecimal(0);

                // Add the balance of each sub-category that will added to the tree
                for (Category subCategory : Wallet.getInstance().getSubCategoriesWithParentCategory().get(topCategory)) {
                    // The sub-category <subCategory> will be added to the tree if:
                    // - the user didn't select any category in the search filter or
                    // - <subCategory> has been selected in the search filter,
                    //   or the parent category of <subCategory> has been selected in the search filter
                    if (!searchCriteria.hasCategoriesFilter() ||
                            isCategoryToDisplay(searchCriteria.getCategories(), subCategory)) {
                        Map<SearchCriteria, BigDecimal> subCategoryBalances =
                                categoryBalances.get(subCategory.getId());
                        if (subCategoryBalances != null) {
                            // Get the balance of the sub-category for the current search criteria
                            BigDecimal subCategoryBalance = subCategoryBalances.get(searchCriteria);

                            // Add it to the category's balance
                            if (subCategoryBalance != null) { // Add the sub-category's balance
                                topCategoryBalance = topCategoryBalance.add(subCategoryBalance);
                            } else { // Put the balance 0 in the sub-category's map
                                subCategoryBalances.put(searchCriteria, new BigDecimal(0));
                            }
                        } else {
                            Map<SearchCriteria, BigDecimal> subCategoryBalanceZero = new HashMap<SearchCriteria, BigDecimal>();
                            subCategoryBalanceZero.put(searchCriteria, new BigDecimal(0));

                            categoryBalances.put(subCategory.getId(), subCategoryBalanceZero);
                        }
                    }
                }

                // Map that contains the category's balance for the current search criteria
                // (Search criteria --> Category's balance)
                Map<SearchCriteria, BigDecimal> topCategoryBalances =
                        new HashMap<SearchCriteria, BigDecimal>();
                // If categories' balances have already been added for other SearchFilters
                if (categoryBalances.get(topCategory.getId()) != null) {
                    topCategoryBalances.putAll(categoryBalances.get(topCategory.getId()));
                }
                // Add the balance of the category
                topCategoryBalances.put(searchCriteria, topCategoryBalance);

                // Add (SearchCriteria, Category balance> to the current category
                categoryBalances.put(topCategory.getId(), topCategoryBalances);
            }
        }

        // Prepare the tree model
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(); // Root (Not displayed)

        // Add the categories and the sub-categories into the tree
        for (Category category : Wallet.getInstance().getTopCategories()) {
            // Create the category <category> if:
            // - the user didn't select any category in the search filter or
            // - <category> has been selected in the search filter or
            // - a sub-category of <category> has been selected in the search filter
            if (!searchFilter.hasCategoriesFilter() ||
                    isCategoryToDisplay(searchFilter.getCategories(), category)) {

                DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category);

                // Add the sub-categories into the tree
                for (Category subCategory : Wallet.getInstance().getSubCategoriesWithParentCategory().get(category)) {
                    // Create the sub-category <subCategory> if:
                    // - the user didn't select any category in the search filter or
                    // - <subCategory> has been selected in the search filter,
                    //   or the parent category of <subCategory> has been selected in the search filter
                    // AND
                    // - there are some balances' movements for <subCategory> or
                    //   <subCategory> has been explicitly selected in the seatch filter (in which case 0 are written)
                    if ((!searchFilter.hasCategoriesFilter() || isCategoryToDisplay(searchFilter.getCategories(), subCategory)) &&
                            (categoryBalances.get(subCategory.getId()) != null || searchFilter.getCategories().contains(subCategory))) {
                        // Add the sub-category node into the tree
                        DefaultMutableTreeNode subCategoryNode = new DefaultMutableTreeNode(subCategory);
                        categoryNode.add(subCategoryNode);
                    }
                }

                // Add the category node into the tree only if 
                // - it contains sub-categories or
                // - the category has been selected in the search filter
                if (categoryNode.getChildCount() > 0 ||
                        (searchFilter.hasCategoriesFilter() && searchFilter.getCategories().contains(category))) {
                    rootNode.add(categoryNode);
                }
            }
        }

        // Create the outline based on the model
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        OutlineModel outlineModel = DefaultOutlineModel.createOutlineModel(
                treeModel,
                new CategoriesBalancesRowModel(searchCriterias, categoryBalances),
                true,
                NbBundle.getMessage(CategoriesBalancesTopComponent.class, "CategoriesBalancesTopComponent.Category"));
        outlineCategoriesBalances.setModel(outlineModel);

        // Expand all nodes of the outline
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            outlineCategoriesBalances.expandPath(new TreePath(((DefaultMutableTreeNode) rootNode.getChildAt(i)).getPath()));
        }

        // Resize the columns' widths
        Utilities.packColumns(outlineCategoriesBalances);

        // Save the currently displayed search filter
        this.displayedSearchFilter = searchFilter;

        // Put the balances map in the lookup so that it can be displayed as a chart by another topcomponent
        content.set(Collections.singleton(categoryBalances), null);
        content.add(fieldsVisibility); // Add a description of the supported filters for the search filter topcomponent

        // Display normal cursor
        Utilities.changeCursorWaitStatus(false);
    }

    /** Row model for the Categories' balances outline */
    private class CategoriesBalancesRowModel implements RowModel {

        /** List of search criteria (columns) */
        private List<SearchCriteria> searchCriterias;
        /** Categories/Sub-categories balances by search criteria */
        private Map<Long, Map<SearchCriteria, BigDecimal>> balances;

        /**
         * Creates a new instance of CategoriesBalancesRowModel
         * @param searchCriterias Search criteria to display (one per column)
         * @param balances Category/Sub-category balances by search criteria
         */
        public CategoriesBalancesRowModel(List<SearchCriteria> searchCriterias, Map<Long, Map<SearchCriteria, BigDecimal>> balances) {
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

            // The object is a category (top-category or sub-category)
            Category category = (Category) nodeUserObject;

            // Display the category's balance value
            // - if the category is a sub-category or
            // - if the category is a top-category and the user wants to see the sums
            if (!category.isTopCategory() || Options.calculateSums()) {
                SearchCriteria searchCriteria = searchCriterias.get(column);
                assert (searchCriteria != null);
                BigDecimal categoryBalance = balances.get(category.getId()).get(searchCriteria);
                assert (categoryBalance != null);
                if (categoryBalance.compareTo(BigDecimal.ZERO) != 0 || Options.displayZero()) {
                    value = Utilities.getSignedBalance(categoryBalance);
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
