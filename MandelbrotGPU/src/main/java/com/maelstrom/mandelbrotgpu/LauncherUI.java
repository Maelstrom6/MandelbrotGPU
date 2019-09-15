/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maelstrom.mandelbrotgpu;

import com.maelstrom.mandelbrotgpu.mappers.Mapper;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris
 */
public class LauncherUI extends javax.swing.JFrame {

    /**
     * Creates new form LauncherUI
     */
    public LauncherUI() {
        initComponents();
        btnRefreshActionPerformed(null);
    }

    FractalSettings oldSettings = getSettings();
    Mapper obj = new Mapper("Mandelbrot");
    String previousFractalType = "Mandelbrot";

    /*public void run() {
        while (true) {
            FractalSettings newSettings = getSettings();
            if (oldSettings.equals(newSettings)) {

            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(LauncherUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }*/
    //(-0.17457822107526513-1.0714276713957467j)
    //(0.014895494704209648-0.84814878823994j)
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblImage = new javax.swing.JLabel();
        txtSizeXView = new javax.swing.JTextField();
        txtSizeYView = new javax.swing.JTextField();
        txtLeftest = new javax.swing.JTextField();
        txtRightest = new javax.swing.JTextField();
        txtHighest = new javax.swing.JTextField();
        txtLowest = new javax.swing.JTextField();
        cmbFractalType = new javax.swing.JComboBox<>();
        txtMaxI = new javax.swing.JTextField();
        txtf0Re = new javax.swing.JTextField();
        txtf0Im = new javax.swing.JTextField();
        txtOrbitID = new javax.swing.JTextField();
        txtThreshold = new javax.swing.JTextField();
        txtAntibuddha = new javax.swing.JTextField();
        txtFn = new javax.swing.JTextField();
        txtSizeXSave = new javax.swing.JTextField();
        txtSizeYSave = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnExportJSON = new javax.swing.JButton();
        btnImportJSON = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        txtMagnification = new javax.swing.JTextField();
        txtCentreX = new javax.swing.JTextField();
        txtCentreY = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtOperators = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cmbColorScheme = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        cmbNotableSettings = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        txtFileName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtSizeXView.setText("500");

        txtSizeYView.setText("500");

        txtLeftest.setText("-2");

        txtRightest.setText("2");

        txtHighest.setText("2");

        txtLowest.setText("-2");

        cmbFractalType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mandelbrot", "Buddha", "Orbit", "Julia", "Buddha Julia" }));

        txtMaxI.setText("100");

        txtf0Re.setText("0");

        txtf0Im.setText("0");

        txtOrbitID.setText("1");

        txtThreshold.setText("2");

        txtAntibuddha.setText("False");

        txtFn.setText("addComplex(powComplex(zn, 2), c)");

        txtSizeXSave.setText("1000");

        txtSizeYSave.setText("1000");

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnExportJSON.setText("Export JSON");
        btnExportJSON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportJSONActionPerformed(evt);
            }
        });

        btnImportJSON.setText("Import JSON");
        btnImportJSON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportJSONActionPerformed(evt);
            }
        });

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        txtMagnification.setText("2");

        txtCentreX.setText("0");

        txtCentreY.setText("0");

        jLabel1.setText("Cenre and magnifcation");

        jLabel2.setText("Width and height");

        jLabel3.setText("Border limits");

        jLabel4.setText("Iterations and threshold");

        jLabel5.setText("Fractal type");

        jLabel6.setText("Formula");

        jLabel7.setText("C for Julia and Orbit");

        jLabel8.setText("Orbit ID for orbits");

        jLabel9.setText("Create antibuddha");

        jLabel10.setText("Operator IDs");

        cmbColorScheme.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Blues", "Greens" }));

        jLabel11.setText("Color scheme");

        cmbNotableSettings.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "The box", "Cave", "Tree", "Spider", "Bedbug", "Snowglobe", "Gates", "Diamond", "Kidney", "Starfish", "Scorpion", "Mandelbrot V2" }));

        jLabel12.setText("Presets");

        txtFileName.setText("File name");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCentreX, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtSizeXView, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                            .addComponent(txtCentreY))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMagnification, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtSizeYView, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 32, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaxI, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbFractalType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnImportJSON)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnExportJSON))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtOperators, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(txtLeftest, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(58, 58, 58)
                                        .addComponent(txtRightest, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(56, 56, 56)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtLowest, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtHighest, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(btnRefresh)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtf0Re, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtAntibuddha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                                    .addComponent(txtOrbitID, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtf0Im, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbColorScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbNotableSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSave)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSizeXSave, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSizeYSave, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSizeYView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSizeXView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMagnification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCentreX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCentreY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHighest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLeftest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtRightest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLowest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaxI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbFractalType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtOperators, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtf0Im, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtf0Re, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtOrbitID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAntibuddha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbColorScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbNotableSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnExportJSON)
                            .addComponent(btnImportJSON))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSizeYSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSizeXSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSave)
                            .addComponent(txtFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 7, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Saves the settings but using the chosen size
        FractalSettings settings = getSelectedSettings("save");
        try{
            obj.savePNG(obj.createProgramAndImage(settings), System.getProperty("user.dir") + "\\"+txtFileName.getText()+".png");
            JOptionPane.showMessageDialog(this, "Saved Successfully to the current working directory.", "Nice", 1);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Failed to save the image.\n"
                    + "Make sure the characters in the file name are viable.\n"
                    + "Check that the size of the image are inputted as integers.", "Oof", 2);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    // Refreshes the view of the mandelbrot
    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // Get all the settings from the UI
        FractalSettings settings = getSelectedSettings("view");
        
        // If user chose a preset, replace UI settings with the preset settings
        if(cmbNotableSettings.getSelectedIndex()!=0){
            setSelectedSettings(settings);
        }
        
        // Create a new context and command queues for a new fractal type
        if(!previousFractalType.equals(cmbFractalType.getSelectedItem()+"")){
            obj = new Mapper(settings.fractalType);
            previousFractalType = cmbFractalType.getSelectedItem()+"";
        }
        
        BufferedImage img = obj.createProgramAndImage(settings);
        ImageIcon image = new ImageIcon(img);
        lblImage.setIcon(image);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnExportJSONActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportJSONActionPerformed
        // Copies Settings to the clipboard
        FractalSettings settings = getSelectedSettings("view");
        String settingsJSON = settings.exportJSON();

        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = defaultToolkit.getSystemClipboard();
        clipboard.setContents(new StringSelection(settingsJSON), null);
        JOptionPane.showMessageDialog(this, "Settings copied to clipboard", "Nice", 1);
    }//GEN-LAST:event_btnExportJSONActionPerformed

    private void btnImportJSONActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportJSONActionPerformed
        // Inserts JSON clipboard data to settings and updates the input
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Clipboard systemClipboard = defaultToolkit.getSystemClipboard();
        DataFlavor dataFlavor = DataFlavor.stringFlavor;
        String settingsJSON = "";

        if (systemClipboard.isDataFlavorAvailable(dataFlavor)) {
            try {
                settingsJSON = systemClipboard.getData(dataFlavor) + "";
            } catch (UnsupportedFlavorException ex) {
                JOptionPane.showMessageDialog(this, "You have something copied that is not JSON.\n" + ex, "Oof", 2);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Something went wrong with the paste of JSON data.\n" + ex, "Oof", 2);
            }
        }

        FractalSettings settings = FractalSettings.importJSON(settingsJSON);

        setSelectedSettings(settings);
        btnRefreshActionPerformed(evt);

    }//GEN-LAST:event_btnImportJSONActionPerformed

    public void setSelectedSettings(FractalSettings settings) {
        txtCentreX.setText("");
        txtCentreY.setText("");
        txtMagnification.setText("");

        txtSizeXView.setText(settings.sizeX + "");
        txtSizeYView.setText(settings.sizeY + "");

        txtLeftest.setText(settings.leftest + "");
        txtRightest.setText(settings.rightest + "");
        txtHighest.setText(settings.highest + "");
        txtLowest.setText(settings.lowest + "");

        txtMaxI.setText(settings.maxIterations + "");
        txtThreshold.setText(settings.threshold + "");

        cmbFractalType.setSelectedItem(settings.fractalType);
        txtFn.setText(settings.fn + "");
        String operators = "";
        if (!settings.transformOperators.isEmpty()) {
            for (int operator : settings.transformOperators) {
                operators = operators + " " + operator;
            }
            operators = operators.substring(1);
        }
        txtOperators.setText(operators);

        txtf0Re.setText(settings.f0Re + "");
        txtf0Im.setText(settings.f0Im + "");
        txtOrbitID.setText(settings.orbitID + "");
        txtAntibuddha.setText(settings.antiBuddha + "");
        
        cmbColorScheme.setSelectedIndex(settings.colorSchemeID-1);
        
        cmbNotableSettings.setSelectedIndex(0);

    }

    public FractalSettings getSelectedSettings(String toSaveOrView) {
        FractalSettings settings = new FractalSettings();
        try {

            switch (toSaveOrView.toLowerCase()) {
                case "save":
                    settings.sizeX = Integer.parseInt(txtSizeXSave.getText());
                    settings.sizeY = Integer.parseInt(txtSizeYSave.getText());
                    break;
                default:
                    settings.sizeX = Integer.parseInt(txtSizeXView.getText());
                    settings.sizeY = Integer.parseInt(txtSizeYView.getText());
            }

            if (txtMagnification.getText().isEmpty()
                    || txtCentreX.getText().isEmpty()
                    || txtCentreY.getText().isEmpty()) {
                settings.leftest = Double.parseDouble(txtLeftest.getText());
                settings.rightest = Double.parseDouble(txtRightest.getText());
                settings.highest = Double.parseDouble(txtHighest.getText());
                settings.lowest = Double.parseDouble(txtLowest.getText());
            } else {
                settings.leftest = Double.parseDouble(txtCentreX.getText()) - Double.parseDouble(txtMagnification.getText());
                settings.rightest = Double.parseDouble(txtCentreX.getText()) + Double.parseDouble(txtMagnification.getText());
                settings.highest = Double.parseDouble(txtCentreY.getText()) + Double.parseDouble(txtMagnification.getText());
                settings.lowest = Double.parseDouble(txtCentreY.getText()) - Double.parseDouble(txtMagnification.getText());
            }

            settings.maxIterations = Integer.parseInt(txtMaxI.getText());
            settings.threshold = Double.parseDouble(txtThreshold.getText());

            settings.fractalType = cmbFractalType.getSelectedItem() + "";
            settings.fn = txtFn.getText();
            Scanner sc=new Scanner(txtOperators.getText());
            while(sc.hasNext()){
                settings.transformOperators.add(Integer.parseInt(sc.next()));
            }

            settings.f0Re = Double.parseDouble(txtf0Re.getText());
            settings.f0Im = Double.parseDouble(txtf0Im.getText());

            settings.orbitID = Integer.parseInt(txtOrbitID.getText());

            settings.antiBuddha = Boolean.parseBoolean(txtAntibuddha.getText().toLowerCase());

            settings.calculateComplex = true;
            
            settings.colorSchemeID = cmbColorScheme.getSelectedIndex()+1;
            
            switch(cmbNotableSettings.getSelectedIndex()){
                case 0:
                    break;
                case 1:
                    settings = NotableSettings.TheBox();
                    break;
                case 2:
                    settings = NotableSettings.Cave();
                    break;
                case 3:
                    settings = NotableSettings.Tree();
                    break;
                case 4:
                    settings = NotableSettings.Spider();
                    break;
                case 5:
                    settings = NotableSettings.Bedbug();
                    break;
                case 6:
                    settings = NotableSettings.Snowglobe();
                    break;
                case 7:
                    settings = NotableSettings.GatesOfHeaven();
                    break;
                case 8:
                    settings = NotableSettings.Diamond();
                    break;
                case 9:
                    settings = NotableSettings.Kidney();
                    break;
                case 10:
                    settings = NotableSettings.Starfish();
                    break;
                case 11:
                    settings = NotableSettings.Scorpion();
                    break;
                case 12:
                    settings = NotableSettings.MandelbrotV2();
                    break;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "We need numbers as inputs\n" + e, "Oof", 2);
        }
        return settings;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LauncherUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LauncherUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LauncherUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LauncherUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LauncherUI l = new LauncherUI();
                l.setVisible(true);
            }
        });
    }

    private static FractalSettings getSettings() {
        return new FractalSettings();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExportJSON;
    private javax.swing.JButton btnImportJSON;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbColorScheme;
    private javax.swing.JComboBox<String> cmbFractalType;
    private javax.swing.JComboBox<String> cmbNotableSettings;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblImage;
    private javax.swing.JTextField txtAntibuddha;
    private javax.swing.JTextField txtCentreX;
    private javax.swing.JTextField txtCentreY;
    private javax.swing.JTextField txtFileName;
    private javax.swing.JTextField txtFn;
    private javax.swing.JTextField txtHighest;
    private javax.swing.JTextField txtLeftest;
    private javax.swing.JTextField txtLowest;
    private javax.swing.JTextField txtMagnification;
    private javax.swing.JTextField txtMaxI;
    private javax.swing.JTextField txtOperators;
    private javax.swing.JTextField txtOrbitID;
    private javax.swing.JTextField txtRightest;
    private javax.swing.JTextField txtSizeXSave;
    private javax.swing.JTextField txtSizeXView;
    private javax.swing.JTextField txtSizeYSave;
    private javax.swing.JTextField txtSizeYView;
    private javax.swing.JTextField txtThreshold;
    private javax.swing.JTextField txtf0Im;
    private javax.swing.JTextField txtf0Re;
    // End of variables declaration//GEN-END:variables
}
