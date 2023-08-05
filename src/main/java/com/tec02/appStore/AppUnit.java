/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tec02.appStore;

import com.tec02.appStore.analysis.AppProccess;
import com.tec02.common.JOptionUtil;
import com.tec02.gui.Panelupdate;
import com.tec02.gui.frameGui.Component.PopupMenu;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

/**
 *
 * @author 21AK22
 */
public class AppUnit extends Panelupdate {

    private Color bgColor;
    private final PopupMenu popupMenu;
    private AppProccess appProccess;
    private final Timer timer;

    public AppUnit() {
        initComponents();
        update();
        this.popupMenu = new PopupMenu();
        this.popupMenu.addItemMenu("Run", (e) -> {
            runApp();
        });
        this.timer = new Timer(500, (e) -> {
            if (appProccess != null &&appProccess.isRuning()) {
                this.lb_icon.setBorder(new LineBorder(Color.GREEN, 2, true));
            } else {
                AppUnit.this.timer.stop();
                this.lb_icon.setBorder(null);
            }
        });
        this.popupMenu.addItemMenu("Show console", (e) -> {
            appProccess.showConsole();
        });

    }

    @Override
    public final void update() {
        super.update();
        this.bgColor = getBackground();
        this.pnAppVid.setBackground(bgColor);
        this.validate();
    }
    
    

    private void runApp() {
        if (this.appProccess.isRuning()) {
            JOptionUtil.showMessage("%s is runing", this.appProccess.getAppName());
        } else {
            if (appProccess == null) {
                return;
            }
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            this.timer.start();
            appProccess.runApp();
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public void setAppProccess(AppProccess appProccess) {
        AppUnit.this.appProccess = appProccess;
        display();
    }

    public void clear() {
        this.appProccess = null;
        this.lb_icon.setIcon(null);
        this.lbName.setText(null);
        this.validate();
    }

//    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnAppVid = new javax.swing.JPanel();
        lb_icon = new javax.swing.JLabel();
        lbName = new javax.swing.JLabel();

        setAlignmentX(0.1F);
        setAlignmentY(0.1F);
        setPreferredSize(new java.awt.Dimension(100, 120));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        lb_icon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lb_icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_icon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lb_icon.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout pnAppVidLayout = new javax.swing.GroupLayout(pnAppVid);
        pnAppVid.setLayout(pnAppVidLayout);
        pnAppVidLayout.setHorizontalGroup(
            pnAppVidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnAppVidLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnAppVidLayout.setVerticalGroup(
            pnAppVidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnAppVidLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(pnAppVid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
            .addComponent(lbName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(pnAppVid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        // TODO add your handling code here: 
        if (appProccess == null) {
            return;
        }
        this.pnAppVid.setBackground(bgColor.darker());
        this.setToolTipText(appProccess.getAppName());
    }//GEN-LAST:event_formMouseEntered

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        if (appProccess == null) {
            return;
        }
        if (evt.getClickCount() > 1 && evt.getButton() == MouseEvent.BUTTON1) {
            runApp();
        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            this.popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }

    }//GEN-LAST:event_formMouseClicked

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        // TODO add your handling code here:
        this.pnAppVid.setBackground(bgColor);
    }//GEN-LAST:event_formMouseExited

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        // TODO add your handling code here:
        if (appProccess == null) {
            return;
        }
        showIcon();
    }//GEN-LAST:event_formComponentResized


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lb_icon;
    private javax.swing.JPanel pnAppVid;
    // End of variables declaration//GEN-END:variables

    private void display() {
        if (appProccess == null) {
            return;
        }
        showIcon();
        showAppName();
        this.validate();
    }

    private void showAppName() {
        this.lb_icon.setVisible(false);
        this.lbName.setText(appProccess.getAppName());
        this.lb_icon.setVisible(true);
    }

    private void showIcon() {
        if (appProccess != null) {
            this.lb_icon.setIcon(appProccess.getIcon(50, 50));
        }
    }

    public boolean containAppID(Object id) {
        if (appProccess == null) {
            return false;
        }
        return appProccess.getId().equals(id);
    }

    public boolean isFree() {
        return appProccess == null;
    }
}
