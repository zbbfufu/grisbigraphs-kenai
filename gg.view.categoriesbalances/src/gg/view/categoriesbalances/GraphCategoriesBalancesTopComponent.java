/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.view.categoriesbalances;

import gg.db.datamodel.SearchFilter;
import gg.db.entities.Category;
import gg.utilities.Utilities;
import gg.wallet.Wallet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
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
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.Lookup;
import org.openide.util.LookupListener;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//gg.view.categoriesbalances//GraphCategoriesBalances//EN",
autostore = false)
public final class GraphCategoriesBalancesTopComponent extends TopComponent implements LookupListener {

    private static GraphCategoriesBalancesTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "gg/resources/icons/GraphCategoriesBalances.png";
    private Lookup.Result result = null;
    private static final String PREFERRED_ID = "GraphCategoriesBalancesTopComponent";

    public GraphCategoriesBalancesTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(GraphCategoriesBalancesTopComponent.class, "CTL_GraphCategoriesBalancesTopComponent"));
        setToolTipText(NbBundle.getMessage(GraphCategoriesBalancesTopComponent.class, "HINT_GraphCategoriesBalancesTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelCategoriesBalances = new javax.swing.JPanel();

        jPanelCategoriesBalances.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelCategoriesBalances, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelCategoriesBalances, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelCategoriesBalances;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized GraphCategoriesBalancesTopComponent getDefault() {
        if (instance == null) {
            instance = new GraphCategoriesBalancesTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the GraphCategoriesBalancesTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized GraphCategoriesBalancesTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(GraphCategoriesBalancesTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof GraphCategoriesBalancesTopComponent) {
            return (GraphCategoriesBalancesTopComponent) win;
        }
        Logger.getLogger(GraphCategoriesBalancesTopComponent.class.getName()).warning(
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
        // Register lookup listener on the accounts' balance table top component
        if (result == null) {
            result = WindowManager.getDefault().findTopComponent("CategoriesBalancesTopComponent").
                    getLookup().lookupResult(Map.class);
            result.addLookupListener(this);
            result.allInstances();
        }

        resultChanged(null);
    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        result = null;
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        GraphCategoriesBalancesTopComponent singleton = GraphCategoriesBalancesTopComponent.getDefault();
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

    @Override
    public void resultChanged(LookupEvent ev) {
        Collection instances = result.allInstances();
        if (!instances.isEmpty()) {
            @SuppressWarnings("unchecked")
            Map<Long, Map<SearchFilter, BigDecimal>> balances =
                    (Map<Long, Map<SearchFilter, BigDecimal>>) instances.iterator().next();
            displayData(balances);
        }
    }

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

    private void displayData(Map<Long, Map<SearchFilter, BigDecimal>> balances) {
        Utilities.changeCursorWaitStatus(true);

        // Create the dataset (that will contain the categories' balances)
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
        chart.setBackgroundPaint(jPanelCategoriesBalances.getBackground());
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);

        // Grid lines
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // Set the orientation of the categories on the domain axis (X axis)
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        // Add the series on the chart
        for (Long categoryId : balances.keySet()) {
            Category category = Wallet.getInstance().getCategoriesWithId().get(categoryId);
            assert (category != null);

            SortedSet<SearchFilter> sortedSearchFilters = new TreeSet<SearchFilter>(
                    balances.get(categoryId).keySet());
            for (SearchFilter searchFilter : sortedSearchFilters) {

                if ((!searchFilter.hasCategoriesFilter() && category.isTopCategory() && !category.getSystemProperty()) ||
                        searchFilter.getCategories().contains(category)) {

                    BigDecimal balance = new BigDecimal(0);
                    if (balances.get(categoryId) != null && balances.get(categoryId).get(searchFilter) != null) {
                        balance = balances.get(categoryId).get(searchFilter);
                    }

                    dataset.addValue(
                            balance,
                            category.getName(),
                            searchFilter.getPeriod());
                }
            }

        }

        // Series' shapes
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        for (int i = 0; i < dataset.getRowCount(); i++) {
            renderer.setSeriesShapesVisible(i, true);
            renderer.setSeriesShape(i, ShapeUtilities.createDiamond(2F));
        }
        // Set the scale of the chart
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRange(true);
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // Create the chart panel that contains the chart
        ChartPanel chartPanel = new ChartPanel(chart);

        // Display the chart
        jPanelCategoriesBalances.removeAll();
        jPanelCategoriesBalances.add(chartPanel, BorderLayout.CENTER);
        jPanelCategoriesBalances.updateUI();

        Utilities.changeCursorWaitStatus(false);
    }
}
