/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.view.accountsbalances;

import gg.db.datamodel.Datamodel;
import gg.db.datamodel.SearchFilter;
import gg.db.entities.Account;
import gg.db.entities.Currency;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.util.ShapeUtilities;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
final class GraphAccountsBalancesTopComponent extends TopComponent implements LookupListener {

    private static GraphAccountsBalancesTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "gg/resources/icons/GraphAccountsBalances.png";
    private static final String PREFERRED_ID = "GraphAccountsBalancesTopComponent";
    private Lookup.Result result = null;

    private GraphAccountsBalancesTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(GraphAccountsBalancesTopComponent.class, "CTL_GraphAccountsBalancesTopComponent"));
        setToolTipText(NbBundle.getMessage(GraphAccountsBalancesTopComponent.class, "HINT_GraphAccountsBalancesTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelAccountsBalances = new javax.swing.JPanel();

        jPanelAccountsBalances.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelAccountsBalances, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelAccountsBalances, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelAccountsBalances;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized GraphAccountsBalancesTopComponent getDefault() {
        if (instance == null) {
            instance = new GraphAccountsBalancesTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the GraphAccountsBalancesTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized GraphAccountsBalancesTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(GraphAccountsBalancesTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof GraphAccountsBalancesTopComponent) {
            return (GraphAccountsBalancesTopComponent) win;
        }
        Logger.getLogger(GraphAccountsBalancesTopComponent.class.getName()).warning(
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
        // Register lookup listener
        Lookup.Template<SearchFilter> tpl = new Lookup.Template<SearchFilter>(SearchFilter.class);
        result = WindowManager.getDefault().findTopComponent("SearchFilterTopComponent").getLookup().lookup(tpl);
        if (result != null) {
            result.addLookupListener(this);
        }
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

    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result r = (Lookup.Result) ev.getSource();
        @SuppressWarnings("unchecked")
        List<SearchFilter> searchFilters = (List<SearchFilter>) r.allInstances();
        if (!searchFilters.isEmpty()) {
            displayData(searchFilters);
        }
    }

    private void displayData(List<SearchFilter> searchFilters) {
        // Create the dataset (that will contain the accounts' balances)
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Create an empty chart
        JFreeChart chart = ChartFactory.createLineChart(
                "", // chart title
                "", // x axis label
                "Amount", // y axis label
                dataset, // data displayed in the chart
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
                );

        // Chart color
        chart.setBackgroundPaint(jPanelAccountsBalances.getBackground());
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);

        // Grid lines
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // Serie color
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, ShapeUtilities.createDiamond(2F));
        renderer.setSeriesPaint(0, new Color(49, 106, 196));

        // Set the orientation of the categories on the domain axis (X axis)
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        // Add the series on the chart
        for (Currency currency : Datamodel.getCurrencies()) {
            if (currency.getActive() &&
                    (!searchFilters.get(0).hasCurrencyFilter() ||
                    (searchFilters.get(0).hasCurrencyFilter() && searchFilters.get(0).getCurrency().compareTo(currency) == 0))) {
                for (Account account : currency.getAccounts()) {
                    if (account.getActive() &&
                            (!searchFilters.get(0).hasAccountsFilter() ||
                            (searchFilters.get(0).hasAccountsFilter() && searchFilters.get(0).getAccounts().contains(account)))) {

                        for (SearchFilter searchFilter : searchFilters) {
                            SearchFilter newSearchFilter = new SearchFilter();
                            newSearchFilter.setCurrency(currency);
                            List<Account> accounts = new ArrayList<Account>();
                            accounts.add(account);
                            newSearchFilter.setAccounts(accounts);
                            newSearchFilter.setPeriod(searchFilter.getPeriod());

                            dataset.addValue(
                                    Datamodel.getBalanceUntil(newSearchFilter).add(account.getInitialAmount()),
                                    account.toString(),
                                    newSearchFilter.getPeriod());
                        }
                    }
                }
            }
        }

        // Set the scale of the chart
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRange(true);
        rangeAxis.setAutoRangeIncludesZero(false);

        // Create the chart panel that contains the chart
        ChartPanel chartPanel = new ChartPanel(chart);

        // Display the chart
        jPanelAccountsBalances.removeAll();
        jPanelAccountsBalances.add(chartPanel, BorderLayout.CENTER);
        jPanelAccountsBalances.updateUI();
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return GraphAccountsBalancesTopComponent.getDefault();
        }
    }
}
