/*
 * GeneralPanel.java
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
package gg.options;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;

/**
 * General options panel
 * @author Francois Duchemin
 */
public final class GeneralPanel extends javax.swing.JPanel implements DocumentListener {

    /** General option controller */
    private final GeneralOptionsPanelController controller;
    /** Key used to identify the 'display zeros' property */
    public static final String DISPLAY_ZEROS_KEY = "DisplayZeros";
    /** Key used to identify the 'calculate sums' property */
    public static final String CALCULATE_SUMS_KEY = "CalculateSums";
    /** Key used to identify the 'maximum number of periods' property */
    public static final String MAX_PERIODS_KEY = "MaxPeriods";

    /**
     * Creates a new instance of GeneralPanel
     * @param controller General option controller to associate with the general option panel
     */
    public GeneralPanel(GeneralOptionsPanelController controller) {
        this.controller = controller;
        initComponents();

        // Initialize the error message
        jLabelErrorMessage.setText("");

        // Listen to changes in form fields and call controller.changed() so that valid() method is called
        jTextFieldMaxPeriods.getDocument().addDocumentListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBoxDisplayZeros = new javax.swing.JCheckBox();
        jCheckBoxCalculateSums = new javax.swing.JCheckBox();
        jLabelMaxPeriods = new javax.swing.JLabel();
        jTextFieldMaxPeriods = new javax.swing.JTextField();
        jLabelErrorMessage = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxDisplayZeros, org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.jCheckBoxDisplayZeros.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxCalculateSums, org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.jCheckBoxCalculateSums.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabelMaxPeriods, org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.jLabelMaxPeriods.text")); // NOI18N

        jTextFieldMaxPeriods.setText(org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.jTextFieldMaxPeriods.text")); // NOI18N

        jLabelErrorMessage.setForeground(java.awt.Color.red);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelErrorMessage, org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.jLabelErrorMessage.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelErrorMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                    .addComponent(jCheckBoxCalculateSums)
                    .addComponent(jCheckBoxDisplayZeros)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelMaxPeriods)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldMaxPeriods, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBoxCalculateSums)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxDisplayZeros)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelMaxPeriods)
                    .addComponent(jTextFieldMaxPeriods, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                .addComponent(jLabelErrorMessage)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Reads settings and initializes GUI
     */
    public void load() {
        jCheckBoxDisplayZeros.setSelected(Options.displayZero());
        jCheckBoxCalculateSums.setSelected(Options.calculateSums());
        jTextFieldMaxPeriods.setText(String.valueOf(Options.getMaxPeriods()));
    }

    /**
     * Saves settings
     */
    public void store() {
        NbPreferences.forModule(GeneralPanel.class).putBoolean(DISPLAY_ZEROS_KEY, jCheckBoxDisplayZeros.isSelected());
        NbPreferences.forModule(GeneralPanel.class).putBoolean(CALCULATE_SUMS_KEY, jCheckBoxCalculateSums.isSelected());
        NbPreferences.forModule(GeneralPanel.class).putInt(MAX_PERIODS_KEY, Integer.parseInt(jTextFieldMaxPeriods.getText()));
    }

    /**
     * Checks whether all fields have been entered correctly or not
     * @return true if the form is valid
     */
    public boolean valid() {
        if (jTextFieldMaxPeriods.getText().length() == 0) {
            jLabelErrorMessage.setText(NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.EnterMaxPeriods"));
            return false;
        }
        if (!isNumber(jTextFieldMaxPeriods.getText())) {
            jLabelErrorMessage.setText(NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.MaxPeriodsNumeric"));
            return false;
        }

        jLabelErrorMessage.setText("");
        return true;
    }

    /**
     * Checks if a string is numeric or not
     * @param string String to check
     * @return true if the string is a number, false otherwise
     */
    private static boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBoxCalculateSums;
    private javax.swing.JCheckBox jCheckBoxDisplayZeros;
    private javax.swing.JLabel jLabelErrorMessage;
    private javax.swing.JLabel jLabelMaxPeriods;
    private javax.swing.JTextField jTextFieldMaxPeriods;
    // End of variables declaration//GEN-END:variables

    /**
     * Gives notification that there was an insert into one of the text fields on the form
     * @param e the document event
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        controller.changed();
    }

    /**
     * Gives notification that a portion of text has been removed in one of the text fields on the form
     * @param e the document event
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        controller.changed();
    }

    /**
     * Gives notification that an attribute or set of attributes changed for one of the text fields on the form
     * @param e the document event
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
        controller.changed();
    }
}
