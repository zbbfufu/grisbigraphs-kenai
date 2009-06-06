/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gg.application.components;

/**
 *
 * @author Francois Duchemin
 */
public class FieldsVisibility {

    private boolean fromVisible;
    private boolean toVisible;
    private boolean byVisible;
    private boolean currencyVisible;
    private boolean accountsVisible;
    private boolean categoriesVisible;
    private boolean payeesVisible;
    private boolean keywordsVisible;

    public FieldsVisibility() {
        setFromVisible(false);
        setToVisible(false);
        setByVisible(false);
        setCurrencyVisible(false);
        setAccountsVisible(false);
        setCategoriesVisible(false);
        setPayeesVisible(false);
        setKeywordsVisible(false);
    }

    /**
     * @return the fromVisible
     */
    public boolean isFromVisible() {
        return fromVisible;
    }

    /**
     * @param fromVisible the fromVisible to set
     */
    public void setFromVisible(boolean fromVisible) {
        this.fromVisible = fromVisible;
    }

    /**
     * @return the toVisible
     */
    public boolean isToVisible() {
        return toVisible;
    }

    /**
     * @param toVisible the toVisible to set
     */
    public void setToVisible(boolean toVisible) {
        this.toVisible = toVisible;
    }

    /**
     * @return the byVisible
     */
    public boolean isByVisible() {
        return byVisible;
    }

    /**
     * @param byVisible the byVisible to set
     */
    public void setByVisible(boolean byVisible) {
        this.byVisible = byVisible;
    }

    /**
     * @return the currencyVisible
     */
    public boolean isCurrencyVisible() {
        return currencyVisible;
    }

    /**
     * @param currencyVisible the currencyVisible to set
     */
    public void setCurrencyVisible(boolean currencyVisible) {
        this.currencyVisible = currencyVisible;
    }

    /**
     * @return the accountsVisible
     */
    public boolean isAccountsVisible() {
        return accountsVisible;
    }

    /**
     * @param accountsVisible the accountsVisible to set
     */
    public void setAccountsVisible(boolean accountsVisible) {
        this.accountsVisible = accountsVisible;
    }

    /**
     * @return the categoriesVisible
     */
    public boolean isCategoriesVisible() {
        return categoriesVisible;
    }

    /**
     * @param categoriesVisible the categoriesVisible to set
     */
    public void setCategoriesVisible(boolean categoriesVisible) {
        this.categoriesVisible = categoriesVisible;
    }

    /**
     * @return the payeesVisible
     */
    public boolean isPayeesVisible() {
        return payeesVisible;
    }

    /**
     * @param payeesVisible the payeesVisible to set
     */
    public void setPayeesVisible(boolean payeesVisible) {
        this.payeesVisible = payeesVisible;
    }

    /**
     * @return the keywordsVisible
     */
    public boolean isKeywordsVisible() {
        return keywordsVisible;
    }

    /**
     * @param keywordsVisible the keywordsVisible to set
     */
    public void setKeywordsVisible(boolean keywordsVisible) {
        this.keywordsVisible = keywordsVisible;
    }

}
