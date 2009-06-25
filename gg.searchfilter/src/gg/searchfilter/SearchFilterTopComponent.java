/*
 * SearchFilterTopComponent.java
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
package gg.searchfilter;

import gg.application.Constants;
import gg.db.datamodel.PeriodType;
import gg.db.datamodel.Periods;
import gg.db.entities.Account;
import gg.db.entities.Category;
import gg.db.entities.Currency;
import gg.db.entities.Payee;
import gg.options.Options;
import gg.wallet.Wallet;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.jdesktop.swingx.JXDatePicker;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Top component which displays the fields that permit to filter the results on currencies,
 * accounts, period, categories, payees and payees.
 */
@ConvertAsProperties(dtd = "-//gg.searchfilter//SearchFilter//EN", autostore = false)
public final class SearchFilterTopComponent extends TopComponent implements LookupListener {

    /** Singleton instance of the topcomponent */
    private static SearchFilterTopComponent instance;
    /** Path to the icon used by the component and its open action */
    private static final String ICON_PATH = "gg/resources/icons/SearchFilter.png";
    /** ID of the component */
    private static final String PREFERRED_ID = "SearchFilterTopComponent";
    /** Content available in the lookup of the topcomponent (search filter object) */
    private InstanceContent content = new InstanceContent();
    /** Model for the accounts list */
    private DefaultListModel listModelAccounts = new DefaultListModel();
    /** Root of the categories tree */
    private DefaultMutableTreeNode rootCategoryNode = new DefaultMutableTreeNode();
    /** Model for the categories tree */
    private DefaultTreeModel treeModelCategories = new DefaultTreeModel(rootCategoryNode);
    /** Model for the payees list */
    private DefaultListModel listModelPayees = new DefaultListModel();
    /** Result of a lookup request */
    private static Lookup.Result result = null;
    /** From label */
    private JLabel jLabelFrom;
    /** From field */
    private JXDatePicker jXDatePickerFrom;
    /** To label */
    private JLabel jLabelTo;
    /** To field */
    private JXDatePicker jXDatePickerTo;
    /** By label */
    private JLabel jLabelBy;
    /** By field */
    private JComboBox jComboBoxBy;
    /** Currency label */
    private JLabel jLabelCurrency;
    /** Currency field */
    private JComboBox jComboBoxCurrency;
    /** Accounts label */
    private JLabel jLabelAccounts;
    /** Scrollpane that contains the accounts field */
    private JScrollPane jScrollPaneAccounts;
    /** Accounts field */
    private JList jListAccounts;
    /** Categories label */
    private JLabel jLabelCategories;
    /** Scrollpane that contains the categories field */
    private JScrollPane jScrollPaneCategories;
    /** Categories field */
    private JTree jTreeCategories;
    /** Payees label */
    private JLabel jLabelPayees;
    /** Scrollpane that contains the payees field */
    private JScrollPane jScrollPanePayees;
    /** Payees field */
    private JList jListPayees;
    /** Keywords label */
    private JLabel jLabelKeywords;
    /** Keywords field */
    private JTextField jTextFieldKeywords;
    /** Search button */
    private JButton jButtonSearch;
    /** 'No field supported' label */
    private JLabel jLabelNoFieldSupported;
    /** Key used to identify the 'date from' property */
    private static final String DATE_FROM_KEY = "DateFrom";
    /** Key used to identify the 'date to' property */
    private static final String DATE_TO_KEY = "DateTo";
    /** Key used to identify the 'by' property */
    private static final String PERIOD_TYPE_KEY = "PeriodType";

    /** Creates a new instance of SearchFilterTopComponent */
    public SearchFilterTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(SearchFilterTopComponent.class, "CTL_SearchFilterTopComponent"));
        setToolTipText(NbBundle.getMessage(SearchFilterTopComponent.class, "HINT_SearchFilterTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

        // Initialize the topcomponent's lookup
        associateLookup(new AbstractLookup(content));

        // Initiate the controls
        jLabelFrom = new JLabel(NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jLabelFrom.text"));
        jXDatePickerFrom = new JXDatePicker();

        jLabelTo = new JLabel(NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jLabelTo.text"));
        jXDatePickerTo = new JXDatePicker();

        jLabelBy = new JLabel(NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jLabelBy.text"));
        jComboBoxBy = new JComboBox();
        jComboBoxBy.setPreferredSize(jXDatePickerTo.getPreferredSize());

        jLabelCurrency = new JLabel(NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jLabelCurrency.text"));
        jComboBoxCurrency = new JComboBox();
        jComboBoxCurrency.setPreferredSize(jXDatePickerTo.getPreferredSize());
        jComboBoxCurrency.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                // Currency selected --> display the corresponding accounts
                jComboBoxCurrencyActionPerformed();
            }
        });

        jLabelAccounts = new JLabel(NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jLabelAccounts.text"));
        jListAccounts = new JList(listModelAccounts);
        jScrollPaneAccounts = new JScrollPane(jListAccounts);
        jListAccounts.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                // Right click --> display the popup menu
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    jPopupMenuAccounts.show(jListAccounts, evt.getX(), evt.getY());
                }
            }
        });

        jLabelCategories = new JLabel(NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jLabelCategories.text"));
        jTreeCategories = new JTree(treeModelCategories);
        jScrollPaneCategories = new JScrollPane(jTreeCategories);
        jTreeCategories.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        jTreeCategories.setShowsRootHandles(true);
        jTreeCategories.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                // Right click --> display the popup menu
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    jPopupMenuCategories.show(jTreeCategories, evt.getX(), evt.getY());
                }
            }
        });

        jLabelPayees = new JLabel(NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jLabelPayees.text"));
        jListPayees = new JList(listModelPayees);
        jScrollPanePayees = new JScrollPane(jListPayees);
        jListPayees.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                // Right click --> display the popup menu
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    jPopupMenuPayees.show(jListPayees, evt.getX(), evt.getY());
                }
            }
        });

        jLabelKeywords = new JLabel(NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jLabelKeywords.text"));
        jTextFieldKeywords = new JTextField();
        jTextFieldKeywords.setPreferredSize(jXDatePickerTo.getPreferredSize());

        JPanel jPanelSearch = new JPanel(new BorderLayout());
        jButtonSearch = new JButton(NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jButtonSearch.text"));
        jButtonSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                search();
            }
        });
        jPanelSearch.add(jButtonSearch, BorderLayout.EAST);

        jLabelNoFieldSupported = new JLabel(
                NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jLabelInformation.text"),
                JLabel.CENTER);

        // Set the jPanelSearchFilter panel's layout to GridBagLayout
        // and create a FormUtility to add things to it
        jPanelSearchFilter.setLayout(new GridBagLayout());
        FormUtility jPanelSearchUtility = new FormUtility();

        // Add fields on jPanelSearchFilter
        jPanelSearchUtility.addLabel(jLabelFrom, jPanelSearchFilter);
        jPanelSearchUtility.addField(jXDatePickerFrom, jPanelSearchFilter);

        jPanelSearchUtility.addLabel(jLabelTo, jPanelSearchFilter);
        jPanelSearchUtility.addField(jXDatePickerTo, jPanelSearchFilter);

        jPanelSearchUtility.addLabel(jLabelBy, jPanelSearchFilter);
        jPanelSearchUtility.addField(jComboBoxBy, jPanelSearchFilter);

        jPanelSearchUtility.addLabel(jLabelCurrency, jPanelSearchFilter);
        jPanelSearchUtility.addField(jComboBoxCurrency, jPanelSearchFilter);

        jPanelSearchUtility.addLabel(jLabelAccounts, jPanelSearchFilter);
        jPanelSearchUtility.addField(jScrollPaneAccounts, jPanelSearchFilter);

        jPanelSearchUtility.addLabel(jLabelCategories, jPanelSearchFilter);
        jPanelSearchUtility.addField(jScrollPaneCategories, jPanelSearchFilter);

        jPanelSearchUtility.addLabel(jLabelPayees, jPanelSearchFilter);
        jPanelSearchUtility.addField(jScrollPanePayees, jPanelSearchFilter);

        jPanelSearchUtility.addLabel(jLabelKeywords, jPanelSearchFilter);
        jPanelSearchUtility.addField(jTextFieldKeywords, jPanelSearchFilter);

        jPanelSearchUtility.addField(jLabelNoFieldSupported, jPanelSearchFilter);

        jPanelSearchUtility.addField(jPanelSearch, jPanelSearchFilter);

        // By default no field is visible
        FieldsVisibility fieldsVisibility = new FieldsVisibility();
        setVisibility(fieldsVisibility);
    }

    /** Loads default values for the fields 'from', 'to', 'by', 'currency', 'categories' and 'payees' */
    private void loadDefaultValues() {
        // Retrieve 'from' date (the date is saved between sessions)
        long storedDateFromMillis = NbPreferences.forModule(SearchFilterTopComponent.class).getLong(
                DATE_FROM_KEY,
                new DateTime().minusMonths(1).getMillis());
        jXDatePickerFrom.setDate(new Date(storedDateFromMillis));

        // Retrieve 'to' date (the date is saved between sessions)
        long storedDateToMillis = NbPreferences.forModule(SearchFilterTopComponent.class).getLong(
                DATE_TO_KEY,
                new DateTime().getMillis());
        jXDatePickerTo.setDate(new Date(storedDateToMillis));

        // Load values in combobox "By"
        jComboBoxBy.removeAllItems();
        jComboBoxBy.addItem(PeriodType.DAY);
        jComboBoxBy.addItem(PeriodType.WEEK);
        jComboBoxBy.addItem(PeriodType.MONTH);
        jComboBoxBy.addItem(PeriodType.YEAR);

        // Retrieve 'by' (the type of period is saved between sessions)
        String storedPeriodTypeString = NbPreferences.forModule(SearchFilterTopComponent.class).get(
                PERIOD_TYPE_KEY,
                PeriodType.WEEK.name());
        assert (storedPeriodTypeString != null);
        PeriodType storedPeriodType = PeriodType.valueOf(storedPeriodTypeString);
        assert (storedPeriodType != null);

        int i = 0;
        int indexToSelect = 0;
        boolean periodFound = false;
        while (i < jComboBoxBy.getItemCount() && !periodFound) {
            if (jComboBoxBy.getItemAt(i).equals(storedPeriodType)) {
                indexToSelect = i;
                periodFound = true;
            }
            i++;
        }
        jComboBoxBy.setSelectedIndex(indexToSelect);

        assert (jXDatePickerFrom.getDate() != null);
        assert (jXDatePickerTo.getDate() != null);
        assert (jXDatePickerFrom.getDate().compareTo(jXDatePickerTo.getDate()) <= 0);

        // Combobox "currency"
        jComboBoxCurrency.removeAllItems();
        for (Currency currency : Wallet.getInstance().getActiveCurrencies()) {
            jComboBoxCurrency.addItem(currency);
        }

        // Tree "categories"
        rootCategoryNode.removeAllChildren();
        for (Category category : Wallet.getInstance().getTopCategories()) {
            if (!category.getSystemProperty()) {
                DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category);
                rootCategoryNode.add(categoryNode);

                for (Category subCategory : Wallet.getInstance().getSubCategoriesWithParentCategory().get(category)) {
                    DefaultMutableTreeNode subCategoryNode = new DefaultMutableTreeNode(subCategory);
                    categoryNode.add(subCategoryNode);
                }
            }
        }
        treeModelCategories.reload(rootCategoryNode);
        expandAll(jTreeCategories);
        jTreeCategories.setRootVisible(false);

        // Listbox "payees"
        listModelPayees.removeAllElements();
        for (Payee payee : Wallet.getInstance().getPayees()) {
            if (!payee.getSystemProperty()) {
                listModelPayees.addElement(payee);
            }
        }
    }

    /**
     * Expands all the nodes of a tree
     * @param tree Tree to expand
     */
    private void expandAll(JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
    }

    /** Updates the content of the 'accounts' combobox with the accounts that belong to the selected currency */
    private void jComboBoxCurrencyActionPerformed() {
        listModelAccounts.clear();

        // Get the selected currency
        Object selectedCurrencyObject = jComboBoxCurrency.getSelectedItem();
        if (selectedCurrencyObject != null) { // Currency selected
            Currency selectedCurrency = (Currency) selectedCurrencyObject;

            // Add accounts that belong to the selected currency
            for (Account account : Wallet.getInstance().getActiveAccountsWithCurrency().get(selectedCurrency)) {
                listModelAccounts.addElement(account);
            }
        }
    }

    /** When the button 'Search' is clicked, creates a searchfilter object and put it in the lookup */
    public void search() {
        if (jComboBoxBy.isVisible() && jXDatePickerFrom.getDate() == null) {
            // 'from' date has not been entered
            NotifyDescriptor message = new NotifyDescriptor.Message(
                    "Please enter a date in the field 'from'",
                    NotifyDescriptor.WARNING_MESSAGE);
            message.setTitle(Constants.APPLICATION_TITLE);
            DialogDisplayer.getDefault().notify(message);
            return;
        }

        if (jComboBoxBy.isVisible() && jXDatePickerTo.getDate() == null) {
            // 'to' date has not been entered
            NotifyDescriptor message = new NotifyDescriptor.Message(
                    "Please enter a date in the field 'to'",
                    NotifyDescriptor.WARNING_MESSAGE);
            message.setTitle(Constants.APPLICATION_TITLE);
            DialogDisplayer.getDefault().notify(message);
            return;
        }

        // Get the 'from' and 'to' dates
        LocalDate from;
        LocalDate to;
        if (jXDatePickerFrom.getDate() != null) {
            from = new LocalDate(jXDatePickerFrom.getDate());
        } else {
            from = new LocalDate(1990, 1, 1);
        }

        if (jXDatePickerTo.getDate() != null) {
            to = new LocalDate(jXDatePickerTo.getDate());
        } else {
            to = new LocalDate();
        }

        // Check that 'from' is before 'to'
        if (from.compareTo(to) > 0) {
            NotifyDescriptor message = new NotifyDescriptor.Message(
                    "The entered period is invalid.\n" +
                    "'From' should be before 'To'.",
                    NotifyDescriptor.WARNING_MESSAGE);
            message.setTitle(Constants.APPLICATION_TITLE);
            DialogDisplayer.getDefault().notify(message);
            return;
        }

        // Get 'by' (type of period: day, week, month, year)
        PeriodType periodType;
        if (jComboBoxBy.isVisible()) {
            assert (jComboBoxBy.getSelectedIndex() != -1);
            periodType = (PeriodType) jComboBoxBy.getSelectedItem();
            assert (periodType != null);
        } else {
            periodType = PeriodType.FREE;
        }

        Periods periods = new Periods(from, to, periodType);
        assert (periods != null);

        // Check that the number of periods is not too big
        int maxNumberPeriods = Options.getMaxPeriods(); // Get the max number of periods allowed in the options
        if (periods.getPeriods().size() > maxNumberPeriods) {
            NotifyDescriptor message = new NotifyDescriptor.Message(
                    "Only " + maxNumberPeriods + " periods can be displayed: please enter new dates",
                    NotifyDescriptor.WARNING_MESSAGE);
            message.setTitle(Constants.APPLICATION_TITLE);
            DialogDisplayer.getDefault().notify(message);
            return;
        }

        // Save 'from', 'to' and 'by' fields so that they will be loaded by default when the component will be opened
        NbPreferences.forModule(SearchFilterTopComponent.class).putLong(
                DATE_FROM_KEY,
                new DateTime(jXDatePickerFrom.getDate()).getMillis());
        NbPreferences.forModule(SearchFilterTopComponent.class).putLong(
                DATE_TO_KEY,
                new DateTime(jXDatePickerTo.getDate()).getMillis());
        NbPreferences.forModule(SearchFilterTopComponent.class).put(
                PERIOD_TYPE_KEY,
                periodType.name());

        // Get selected currency
        Object selectedCurrencyObject = jComboBoxCurrency.getSelectedItem();
        assert (selectedCurrencyObject != null);
        Currency selectedCurrency = (Currency) selectedCurrencyObject;
        assert (selectedCurrency != null);

        // Get selected accounts
        List<Account> selectedAccounts = new ArrayList<Account>();
        int[] selectedAccountsIndices = jListAccounts.getSelectedIndices();
        for (int i = 0; i < selectedAccountsIndices.length; i++) {
            Object selectedAccountObject = jListAccounts.getModel().getElementAt(selectedAccountsIndices[i]);
            assert (selectedAccountObject != null);

            selectedAccounts.add((Account) selectedAccountObject);
        }

        // Get selected categories
        List<Category> selectedCategories = new ArrayList<Category>();
        TreePath[] paths = jTreeCategories.getSelectionPaths();
        if (paths != null) { // category selected
            for (TreePath path : paths) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                assert (node != null);
                Object selectedCategoryObject = node.getUserObject();
                assert (selectedCategoryObject != null);
                Category category = (Category) selectedCategoryObject;
                assert (category != null);

                selectedCategories.add(category);
            }
        }

        // Get selected payees
        List<Payee> selectedPayees = new ArrayList<Payee>();
        int[] selectedPayeesIndices = jListPayees.getSelectedIndices();
        for (int i = 0; i < selectedPayeesIndices.length; i++) {
            Object selectedPayeeObject = jListPayees.getModel().getElementAt(selectedPayeesIndices[i]);
            assert (selectedPayeeObject != null);

            selectedPayees.add((Payee) selectedPayeeObject);
        }

        // Get selected keywords
        String keywords = jTextFieldKeywords.getText();

        // Create the search filter
        SearchFilter searchFilter = new SearchFilter(from, to, periodType, selectedCurrency,
                selectedAccounts, selectedCategories, selectedPayees, keywords);

        // Put the search filter in the lookup of the TC
        content.set(Collections.singleton(searchFilter), null);
    }

    /** This method is called from within the constructor to
     * initialize the jPanelSearchFilter.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenuCategories = new javax.swing.JPopupMenu();
        jMenuItemSelectAllCategories = new javax.swing.JMenuItem();
        jMenuItemDeselectAllCategories = new javax.swing.JMenuItem();
        jPopupMenuAccounts = new javax.swing.JPopupMenu();
        jMenuItemSelectAllAccounts = new javax.swing.JMenuItem();
        jMenuItemDeselectAllAccounts = new javax.swing.JMenuItem();
        jPopupMenuPayees = new javax.swing.JPopupMenu();
        jMenuItemSelectAllPayees = new javax.swing.JMenuItem();
        jMenuItemDeselectAllPayees = new javax.swing.JMenuItem();
        jScrollPaneSearchFilter = new javax.swing.JScrollPane();
        jPanelSearchFilterContainer = new javax.swing.JPanel();
        jPanelSearchFilter = new javax.swing.JPanel();

        org.openide.awt.Mnemonics.setLocalizedText(jMenuItemSelectAllCategories, org.openide.util.NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jMenuItemSelectAllCategories.text")); // NOI18N
        jMenuItemSelectAllCategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSelectAllCategoriesActionPerformed(evt);
            }
        });
        jPopupMenuCategories.add(jMenuItemSelectAllCategories);

        org.openide.awt.Mnemonics.setLocalizedText(jMenuItemDeselectAllCategories, org.openide.util.NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jMenuItemDeselectAllCategories.text")); // NOI18N
        jMenuItemDeselectAllCategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeselectAllCategoriesActionPerformed(evt);
            }
        });
        jPopupMenuCategories.add(jMenuItemDeselectAllCategories);

        org.openide.awt.Mnemonics.setLocalizedText(jMenuItemSelectAllAccounts, org.openide.util.NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jMenuItemSelectAllAccounts.text")); // NOI18N
        jMenuItemSelectAllAccounts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSelectAllAccountsActionPerformed(evt);
            }
        });
        jPopupMenuAccounts.add(jMenuItemSelectAllAccounts);

        org.openide.awt.Mnemonics.setLocalizedText(jMenuItemDeselectAllAccounts, org.openide.util.NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jMenuItemDeselectAllAccounts.text")); // NOI18N
        jMenuItemDeselectAllAccounts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeselectAllAccountsActionPerformed(evt);
            }
        });
        jPopupMenuAccounts.add(jMenuItemDeselectAllAccounts);

        org.openide.awt.Mnemonics.setLocalizedText(jMenuItemSelectAllPayees, org.openide.util.NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jMenuItemSelectAllPayees.text")); // NOI18N
        jMenuItemSelectAllPayees.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSelectAllPayeesActionPerformed(evt);
            }
        });
        jPopupMenuPayees.add(jMenuItemSelectAllPayees);

        org.openide.awt.Mnemonics.setLocalizedText(jMenuItemDeselectAllPayees, org.openide.util.NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jMenuItemDeselectAllPayees.text")); // NOI18N
        jMenuItemDeselectAllPayees.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeselectAllPayeesActionPerformed(evt);
            }
        });
        jPopupMenuPayees.add(jMenuItemDeselectAllPayees);

        setLayout(new java.awt.BorderLayout());

        jPanelSearchFilterContainer.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanelSearchFilterLayout = new javax.swing.GroupLayout(jPanelSearchFilter);
        jPanelSearchFilter.setLayout(jPanelSearchFilterLayout);
        jPanelSearchFilterLayout.setHorizontalGroup(
            jPanelSearchFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 334, Short.MAX_VALUE)
        );
        jPanelSearchFilterLayout.setVerticalGroup(
            jPanelSearchFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 579, Short.MAX_VALUE)
        );

        jPanelSearchFilterContainer.add(jPanelSearchFilter, java.awt.BorderLayout.NORTH);

        jScrollPaneSearchFilter.setViewportView(jPanelSearchFilterContainer);

        add(jScrollPaneSearchFilter, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemSelectAllCategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSelectAllCategoriesActionPerformed
        // Select all categories
        jTreeCategories.setSelectionInterval(0, jTreeCategories.getRowCount());
    }//GEN-LAST:event_jMenuItemSelectAllCategoriesActionPerformed

    private void jMenuItemDeselectAllCategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeselectAllCategoriesActionPerformed
        // Deselect all categories
        jTreeCategories.clearSelection();
    }//GEN-LAST:event_jMenuItemDeselectAllCategoriesActionPerformed

    private void jMenuItemSelectAllAccountsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSelectAllAccountsActionPerformed
        // Select all accounts
        jListAccounts.setSelectionInterval(0, jListAccounts.getModel().getSize() - 1);
    }//GEN-LAST:event_jMenuItemSelectAllAccountsActionPerformed

    private void jMenuItemDeselectAllAccountsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeselectAllAccountsActionPerformed
        // Deselect all accounts
        jListAccounts.clearSelection();
    }//GEN-LAST:event_jMenuItemDeselectAllAccountsActionPerformed

    private void jMenuItemSelectAllPayeesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSelectAllPayeesActionPerformed
        jListPayees.setSelectionInterval(0, jListPayees.getModel().getSize() - 1);
    }//GEN-LAST:event_jMenuItemSelectAllPayeesActionPerformed

    private void jMenuItemDeselectAllPayeesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeselectAllPayeesActionPerformed
        // Deselect all payees
        jListPayees.clearSelection();
    }//GEN-LAST:event_jMenuItemDeselectAllPayeesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMenuItemDeselectAllAccounts;
    private javax.swing.JMenuItem jMenuItemDeselectAllCategories;
    private javax.swing.JMenuItem jMenuItemDeselectAllPayees;
    private javax.swing.JMenuItem jMenuItemSelectAllAccounts;
    private javax.swing.JMenuItem jMenuItemSelectAllCategories;
    private javax.swing.JMenuItem jMenuItemSelectAllPayees;
    private javax.swing.JPanel jPanelSearchFilter;
    private javax.swing.JPanel jPanelSearchFilterContainer;
    private javax.swing.JPopupMenu jPopupMenuAccounts;
    private javax.swing.JPopupMenu jPopupMenuCategories;
    private javax.swing.JPopupMenu jPopupMenuPayees;
    private javax.swing.JScrollPane jScrollPaneSearchFilter;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     * @return Default instance
     */
    public static synchronized SearchFilterTopComponent getDefault() {
        if (instance == null) {
            instance = new SearchFilterTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the SearchFilterTopComponent instance. Never call {@link #getDefault} directly!
     * @return SearchFilterTopComponent instance
     */
    public static synchronized SearchFilterTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(SearchFilterTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof SearchFilterTopComponent) {
            return (SearchFilterTopComponent) win;
        }
        Logger.getLogger(SearchFilterTopComponent.class.getName()).warning(
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
        SearchFilterTopComponent singleton = SearchFilterTopComponent.getDefault();
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

    /** Registers listener when the topcomponent is opened */
    @Override
    public void componentOpened() {
        // Add a listener on the lookup of the global context (selected tc)
        // - SearchFilterTopComponent listens to the class: FieldsVisibility
        // - Depending on the value of the object, filters (from/to/by/currencies/accounts...)
        // will be activated/deactivated
        if (result == null) {
            result = Utilities.actionsGlobalContext().lookupResult(FieldsVisibility.class);
            result.addLookupListener(this);
            result.allInstances();

            // Load the default values in fields (from/to/by/currencies/accounts...)
            loadDefaultValues();

            // Search with the current selection
            search();
        }
    }

    /** Unregisters listener when the component is closed */
    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        result = null;
    }

    /**
     * Updates the visibility of the filters (from/to/by/currencies/accounts...)
     * @param ev Lookup event
     */
    @Override
    public void resultChanged(LookupEvent ev) {
        Collection instances = result.allInstances();

        if (!instances.isEmpty()) {
            FieldsVisibility fieldsVisibility = (FieldsVisibility) instances.iterator().next();
            setVisibility(fieldsVisibility);

            search();
        }
    }

    /**
     * Sets the visibility of the filters (from/to/by/currencies/accounts...) depending of the content of fieldsVisibility
     * @param fieldsVisibility Describes which filters should be visible and which should not
     */
    public void setVisibility(FieldsVisibility fieldsVisibility) {
        // From
        jLabelFrom.setVisible(fieldsVisibility.isFromVisible());
        jXDatePickerFrom.setVisible(fieldsVisibility.isFromVisible());

        // To
        jLabelTo.setVisible(fieldsVisibility.isToVisible());
        jXDatePickerTo.setVisible(fieldsVisibility.isToVisible());

        // By
        jLabelBy.setVisible(fieldsVisibility.isByVisible());
        jComboBoxBy.setVisible(fieldsVisibility.isByVisible());

        // Currency
        jLabelCurrency.setVisible(fieldsVisibility.isCurrencyVisible());
        jComboBoxCurrency.setVisible(fieldsVisibility.isCurrencyVisible());

        // Accounts
        jLabelAccounts.setVisible(fieldsVisibility.isAccountsVisible());
        jScrollPaneAccounts.setVisible(fieldsVisibility.isAccountsVisible());

        // Categories
        jLabelCategories.setVisible(fieldsVisibility.isCategoriesVisible());
        jScrollPaneCategories.setVisible(fieldsVisibility.isCategoriesVisible());

        // Payees
        jLabelPayees.setVisible(fieldsVisibility.isPayeesVisible());
        jScrollPanePayees.setVisible(fieldsVisibility.isPayeesVisible());

        // Keywords
        jLabelKeywords.setVisible(fieldsVisibility.isKeywordsVisible());
        jTextFieldKeywords.setVisible(fieldsVisibility.isKeywordsVisible());

        // If at least one filter is visible, display the Search button
        // Otherwise display a label: "No field supported for the current view"
        if (fieldsVisibility.isFromVisible() || fieldsVisibility.isToVisible() ||
                fieldsVisibility.isByVisible() || fieldsVisibility.isCurrencyVisible() ||
                fieldsVisibility.isAccountsVisible() || fieldsVisibility.isCategoriesVisible() ||
                fieldsVisibility.isPayeesVisible() || fieldsVisibility.isKeywordsVisible()) {
            jButtonSearch.setVisible(true);
            jLabelNoFieldSupported.setVisible(false);
        } else {
            jButtonSearch.setVisible(false);
            jLabelNoFieldSupported.setVisible(true);
        }
    }
}
