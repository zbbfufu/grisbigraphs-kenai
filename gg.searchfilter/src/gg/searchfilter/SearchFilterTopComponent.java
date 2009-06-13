/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.searchfilter;

import gg.application.components.FieldsVisibility;
import gg.db.datamodel.Datamodel;
import gg.db.datamodel.Period;
import gg.db.datamodel.PeriodType;
import gg.db.datamodel.Periods;
import gg.db.datamodel.SearchFilter;
import gg.db.entities.Account;
import gg.db.entities.Category;
import gg.db.entities.Currency;
import gg.db.entities.Payee;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import org.joda.time.LocalDate;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Top component which displays something.
 */
final class SearchFilterTopComponent extends TopComponent implements LookupListener {

    private static SearchFilterTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "gg/resources/icons/SearchFilter.png";
    private static final String PREFERRED_ID = "SearchFilterTopComponent";
    private InstanceContent content = new InstanceContent();
    private DefaultListModel listModelAccounts = new DefaultListModel();
    private DefaultMutableTreeNode rootCategoryNode = new DefaultMutableTreeNode();
    private DefaultTreeModel treeModelCategories = new DefaultTreeModel(rootCategoryNode);
    private DefaultListModel listModelPayees = new DefaultListModel();
    private static Lookup.Result result = null;
    private JLabel jLabelFrom;
    private JXDatePicker jXDatePickerFrom;
    private JLabel jLabelTo;
    private JXDatePicker jXDatePickerTo;
    private JLabel jLabelBy;
    private JComboBox jComboBoxBy;
    private JLabel jLabelCurrency;
    private JComboBox jComboBoxCurrency;
    private JLabel jLabelAccounts;
    private JScrollPane jScrollPaneAccounts;
    private JList jListAccounts;
    private JLabel jLabelCategories;
    private JScrollPane jScrollPaneCategories;
    private JTree jTreeCategories;
    private JLabel jLabelPayees;
    private JScrollPane jScrollPanePayees;
    private JList jListPayees;
    private JLabel jLabelKeywords;
    private JTextField jTextFieldKeywords;
    private JButton jButtonSearch;
    private JLabel jLabelNoFieldsSupported;

    private SearchFilterTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(SearchFilterTopComponent.class, "CTL_SearchFilterTopComponent"));
        setToolTipText(NbBundle.getMessage(SearchFilterTopComponent.class, "HINT_SearchFilterTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

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

            public void actionPerformed(ActionEvent evt) {
                jComboBoxCurrencyActionPerformed();
            }
        });

        jLabelAccounts = new JLabel(NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jLabelAccounts.text"));
        jListAccounts = new JList(listModelAccounts);
        jScrollPaneAccounts = new JScrollPane(jListAccounts);

        jLabelCategories = new JLabel(NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jLabelCategories.text"));
        jTreeCategories = new JTree(treeModelCategories);
        jScrollPaneCategories = new JScrollPane(jTreeCategories);
        jTreeCategories.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        jTreeCategories.setShowsRootHandles(true);

        jLabelPayees = new JLabel(NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jLabelPayees.text"));
        jListPayees = new JList(listModelPayees);
        jScrollPanePayees = new JScrollPane(jListPayees);

        jLabelKeywords = new JLabel(NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jLabelKeywords.text"));
        jTextFieldKeywords = new JTextField();
        jTextFieldKeywords.setPreferredSize(jXDatePickerTo.getPreferredSize());

        JPanel jPanelSearch = new JPanel(new BorderLayout());
        jButtonSearch = new JButton(NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jButtonSearch.text"));
        jButtonSearch.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                jButtonSearchActionPerformed();
            }
        });
        jPanelSearch.add(jButtonSearch, BorderLayout.EAST);

        jLabelNoFieldsSupported = new JLabel(
                NbBundle.getMessage(SearchFilterTopComponent.class, "SearchFilterTopComponent.jLabelInformation.text"),
                JLabel.CENTER);

        // Set the jPanelSearchFilter panel's layout to GridBagLayout
        // and create a FormUtility to add things to it
        jPanelSearchFilter.setLayout(new GridBagLayout());
        FormUtility jPanelSearchUtility = new FormUtility();

        // Add fields on the form
        jPanelSearchUtility.addFirstField(jLabelFrom, jPanelSearchFilter);
        jPanelSearchUtility.addLastField(jXDatePickerFrom, jPanelSearchFilter);

        jPanelSearchUtility.addFirstField(jLabelTo, jPanelSearchFilter);
        jPanelSearchUtility.addLastField(jXDatePickerTo, jPanelSearchFilter);

        jPanelSearchUtility.addFirstField(jLabelBy, jPanelSearchFilter);
        jPanelSearchUtility.addLastField(jComboBoxBy, jPanelSearchFilter);

        jPanelSearchUtility.addFirstField(jLabelCurrency, jPanelSearchFilter);
        jPanelSearchUtility.addLastField(jComboBoxCurrency, jPanelSearchFilter);

        jPanelSearchUtility.addFirstField(jLabelAccounts, jPanelSearchFilter);
        jPanelSearchUtility.addLastField(jScrollPaneAccounts, jPanelSearchFilter);

        jPanelSearchUtility.addFirstField(jLabelCategories, jPanelSearchFilter);
        jPanelSearchUtility.addLastField(jScrollPaneCategories, jPanelSearchFilter);

        jPanelSearchUtility.addFirstField(jLabelPayees, jPanelSearchFilter);
        jPanelSearchUtility.addLastField(jScrollPanePayees, jPanelSearchFilter);

        jPanelSearchUtility.addFirstField(jLabelKeywords, jPanelSearchFilter);
        jPanelSearchUtility.addLastField(jTextFieldKeywords, jPanelSearchFilter);

        jPanelSearchUtility.addLastField(jLabelNoFieldsSupported, jPanelSearchFilter);

        jPanelSearchUtility.addLastField(jPanelSearch, jPanelSearchFilter);

        // By default no field is visible
        FieldsVisibility fieldsVisibility = new FieldsVisibility();
        setVisibility(fieldsVisibility);
    }

    /** Loads the "type of graph" and the "type of period" comboboxes */
    private void loadComboboxes() {
        // combobox "period type"
        jComboBoxBy.removeAllItems();
        jComboBoxBy.addItem(PeriodType.DAY);
        jComboBoxBy.addItem(PeriodType.WEEK);
        jComboBoxBy.addItem(PeriodType.MONTH);
        jComboBoxBy.addItem(PeriodType.YEAR);

        // combobox "currency"
        jComboBoxCurrency.removeAllItems();
        for (Currency currency : Datamodel.getActiveCurrencies()) {
            jComboBoxCurrency.addItem(currency);
        }

        // tree "categories"
        rootCategoryNode.removeAllChildren();
        for (Category category : Datamodel.getTopCategories()) {
            if (!category.getSystemProperty()) {
                DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category);
                rootCategoryNode.add(categoryNode);

                for (Category subCategory : Datamodel.getSubCategories(category)) {
                    DefaultMutableTreeNode subCategoryNode = new DefaultMutableTreeNode(subCategory);
                    categoryNode.add(subCategoryNode);
                }
            }
        }
        expandAll(jTreeCategories);
        jTreeCategories.setRootVisible(false);

        // listbox "payees"
        listModelPayees.removeAllElements();
        for (Payee payee : Datamodel.getPayees()) {
            if (!payee.getSystemProperty()) {
                listModelPayees.addElement(payee);
            }
        }
    }

    public void expandAll(JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
    }

    private void jComboBoxCurrencyActionPerformed() {
        // Filter accounts on the selected currency
        listModelAccounts.clear();

        Object selectedCurrencyObject = jComboBoxCurrency.getSelectedItem();

        if (selectedCurrencyObject != null) { // Currency selected
            Currency selectedCurrency = (Currency) selectedCurrencyObject;
            // Display accounts that belong to the selected currency
            for (Account account : Datamodel.getActiveAccounts(selectedCurrency)) {
                listModelAccounts.addElement(account);
            }
        }
    }

    private void jButtonSearchActionPerformed() {
        Currency selectedCurrency = null;
        List<Account> selectedAccounts = new ArrayList<Account>();
        List<Category> selectedCategories = new ArrayList<Category>();
        List<Payee> selectedPayees = new ArrayList<Payee>();
        List<String> enteredKeywords = new ArrayList<String>();
        List<SearchFilter> searchFilters = new ArrayList<SearchFilter>();

        if (jXDatePickerFrom.getDate() == null) {
            // 'from' date has not been entered
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    "Please enter a date in the field 'from'",
                    NotifyDescriptor.WARNING_MESSAGE);
            d.setTitle("Period invalid");
            DialogDisplayer.getDefault().notify(d);
            return;
        } else if (jXDatePickerTo.getDate() == null) {
            // 'to' date has not been entered
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    "Please enter a date in the field 'to'",
                    NotifyDescriptor.WARNING_MESSAGE);
            d.setTitle("Period invalid");
            DialogDisplayer.getDefault().notify(d);
            return;
        } else {
            // Get the 'from' and 'to' dates
            LocalDate from = new LocalDate(jXDatePickerFrom.getDate());
            LocalDate to = new LocalDate(jXDatePickerTo.getDate());

            // Check that 'from' is before 'to'
            if (from.compareTo(to) > 0) {
                NotifyDescriptor d = new NotifyDescriptor.Message(
                        "The entered period is not valid.\n" +
                        "'From' should be before 'To'.",
                        NotifyDescriptor.WARNING_MESSAGE);
                d.setTitle("Period invalid");
                DialogDisplayer.getDefault().notify(d);
                return;
            }

            // Get 'by' (day, week, month, year)
            assert (jComboBoxBy.getSelectedIndex() != -1);
            PeriodType periodType = (PeriodType) jComboBoxBy.getSelectedItem();
            Periods periods = new Periods(from, to, periodType);

            // Check the number of periods
            if (periods.getPeriods().size() > 10) {
                NotifyDescriptor d = new NotifyDescriptor.Message(
                        "Only 10 periods can be displayed: please enter new dates",
                        NotifyDescriptor.WARNING_MESSAGE);
                d.setTitle("Period invalid");
                DialogDisplayer.getDefault().notify(d);
                return;
            }

            // Get selected currency
            Object selectedCurrencyObject = jComboBoxCurrency.getSelectedItem();
            selectedCurrency = (Currency) selectedCurrencyObject;

            // Get selected accounts
            int[] selectedAccountsIndices = jListAccounts.getSelectedIndices();
            for (int i = 0; i < selectedAccountsIndices.length; i++) {
                Object selectedAccountObject = jListAccounts.getModel().getElementAt(selectedAccountsIndices[i]);
                selectedAccounts.add((Account) selectedAccountObject);
            }

            // Get selected categories
            TreePath[] paths = jTreeCategories.getSelectionPaths();
            if (paths != null) { // category selected
                for (TreePath path : paths) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    Object selectedCategoryObject = node.getUserObject();
                    Category category = (Category) selectedCategoryObject;
                    selectedCategories.add(category);
                }
            }

            // Get selected payees
            int[] selectedPayeesIndices = jListPayees.getSelectedIndices();
            for (int i = 0; i < selectedPayeesIndices.length; i++) {
                Object selectedPayeeObject = jListPayees.getModel().getElementAt(selectedPayeesIndices[i]);
                selectedPayees.add((Payee) selectedPayeeObject);
            }

            // Get selected keywords
            String keywords = jTextFieldKeywords.getText();
            if (keywords.compareTo("") != 0) {
                String[] keywordsSplit = keywords.split(" ");
                for (int i = 0; i < keywordsSplit.length; i++) {
                    enteredKeywords.add(keywordsSplit[i]);
                }
            }

            // Create the search filters (one for each period)
            for (Period period : periods.getPeriods()) {
                SearchFilter searchFilter = new SearchFilter();

                searchFilter.setPeriod(period);
                searchFilter.setCurrency(selectedCurrency);
                searchFilter.setAccounts(selectedAccounts);
                searchFilter.setCategories(selectedCategories);
                searchFilter.setPayees(selectedPayees);
                searchFilter.setKeywords(enteredKeywords);
                searchFilter.setIncludeTransferTransactions(true);

                searchFilters.add(searchFilter);
            }

            // Put the list of SearchFilter in the lookup of the TC
            content.set(searchFilters, null);
        }
    }

    /** This method is called from within the constructor to
     * initialize the jPanelSearchFilter.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPaneSearchFilter = new javax.swing.JScrollPane();
        jPanelSearchFilterContainer = new javax.swing.JPanel();
        jPanelSearchFilter = new javax.swing.JPanel();

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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelSearchFilter;
    private javax.swing.JPanel jPanelSearchFilterContainer;
    private javax.swing.JScrollPane jScrollPaneSearchFilter;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized SearchFilterTopComponent getDefault() {
        if (instance == null) {
            instance = new SearchFilterTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the SearchFilterTopComponent instance. Never call {@link #getDefault} directly!
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

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        result = Utilities.actionsGlobalContext().lookupResult(FieldsVisibility.class);
        result.addLookupListener(this);
        result.allInstances();

        loadComboboxes();
    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        result = null;
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

    public void resultChanged(LookupEvent ev) {
        Collection instances = result.allInstances();

        if (!instances.isEmpty()) {
            FieldsVisibility fieldsVisibility = (FieldsVisibility) instances.iterator().next();
            setVisibility(fieldsVisibility);
        }
    }

    public void setVisibility(FieldsVisibility fieldsVisibility) {
        jLabelFrom.setVisible(fieldsVisibility.isFromVisible());
        jXDatePickerFrom.setVisible(fieldsVisibility.isFromVisible());

        jLabelTo.setVisible(fieldsVisibility.isToVisible());
        jXDatePickerTo.setVisible(fieldsVisibility.isToVisible());

        jLabelBy.setVisible(fieldsVisibility.isByVisible());
        jComboBoxBy.setVisible(fieldsVisibility.isByVisible());

        jLabelCurrency.setVisible(fieldsVisibility.isCurrencyVisible());
        jComboBoxCurrency.setVisible(fieldsVisibility.isCurrencyVisible());

        jLabelAccounts.setVisible(fieldsVisibility.isAccountsVisible());
        jScrollPaneAccounts.setVisible(fieldsVisibility.isAccountsVisible());

        jLabelCategories.setVisible(fieldsVisibility.isCategoriesVisible());
        jScrollPaneCategories.setVisible(fieldsVisibility.isCategoriesVisible());

        jLabelPayees.setVisible(fieldsVisibility.isPayeesVisible());
        jScrollPanePayees.setVisible(fieldsVisibility.isPayeesVisible());

        jLabelKeywords.setVisible(fieldsVisibility.isKeywordsVisible());
        jTextFieldKeywords.setVisible(fieldsVisibility.isKeywordsVisible());

        if (fieldsVisibility.isFromVisible() || fieldsVisibility.isToVisible() ||
                fieldsVisibility.isByVisible() || fieldsVisibility.isCurrencyVisible() ||
                fieldsVisibility.isAccountsVisible() || fieldsVisibility.isCategoriesVisible() ||
                fieldsVisibility.isPayeesVisible() || fieldsVisibility.isKeywordsVisible()) {
            jButtonSearch.setVisible(true);
            jLabelNoFieldsSupported.setVisible(false);
        } else {
            jButtonSearch.setVisible(false);
            jLabelNoFieldsSupported.setVisible(true);
        }
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return SearchFilterTopComponent.getDefault();
        }
    }
}
