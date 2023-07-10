/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.tec02.gui.frameGui;

import com.tec02.gui.frameGui.Component.MyChooser;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class LogView extends AbsDisplayAble {

    private final MyChooser chooser;

    /**
     * Creates new form LogView
     */
    public LogView() {
        initComponents();
        this.chooser = new MyChooser();
    }

    public void clear() {
        this.txtAreaLog.setText(null);
    }

    public void append(String log) {
        this.txtAreaLog.append(log);
    }

    public void setLog(String log) {
        this.txtAreaLog.setText(log);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        txtAreaLog.setColumns(20);
        txtAreaLog.setRows(5);
        txtAreaLog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAreaLogKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtAreaLog);

        getContentPane().add(jScrollPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtAreaLogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaLogKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_S) {
            try {
                String log = this.txtAreaLog.getText();
                if (this.chooser.showSaveDialog(this, null) == JFileChooser.APPROVE_OPTION) {
                    File file = this.chooser.getSelectedFile();
                    this.chooser.setNewFile(file);
                    Files.write(file.toPath(), log.getBytes());
                    JOptionPane.showMessageDialog(null, "Save file : " + file.getPath());
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        String.format("Save file failed!\r\nPlease try again.\r\n%s",
                                e.getLocalizedMessage()));
            }
        }
    }//GEN-LAST:event_txtAreaLogKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtAreaLog;
    // End of variables declaration//GEN-END:variables
}
