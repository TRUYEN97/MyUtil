/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui.frameGui;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;

/**
 *
 * @author Administrator
 */
public abstract class AbsDisplayAble extends javax.swing.JFrame {

    private final Set<JFrame> subFrame;

    protected AbsDisplayAble() {
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {

        }
        //</editor-fold>
        /* Create and display the form */
        //</editor-fold>

        /* Create and display the form */
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent evt) {
                closeAllSubFrame();
            }
        });
        this.subFrame = new HashSet<>();
    }

    public void addSubFrame(JFrame frame) {
        if (frame == null) {
            return;
        }
        this.subFrame.add(frame);
    }

    protected void closeAllSubFrame() {
        for (JFrame jFrame : subFrame) {
            jFrame.dispose();
        }
    }

    public void display(String titleName) {
        java.awt.EventQueue.invokeLater(() -> {
            if (titleName != null) {
                setTitle(titleName);
            }
            setVisible(true);
        });
    }
}
