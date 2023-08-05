/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tec02.gui.panelGui;

import com.tec02.common.JOptionUtil;
import com.tec02.common.API.JsonBodyAPI;
import com.tec02.common.Keyword;
import com.tec02.common.API.RestAPI;
import com.tec02.gui.Panelupdate;
import com.tec02.common.PropertiesModel;
import javax.swing.Timer;

/**
 *
 * @author Administrator
 */
public class UserInfomation extends Panelupdate {

    private final Timer timer;
    private final RestAPI restAPI;
    private final PropertiesModel model;

    /**
     * Creates new form UserInfo
     *
     * @param restAPI
     * @throws java.io.IOException
     */
    public UserInfomation(RestAPI restAPI) throws Exception {
        initComponents();
        this.restAPI = restAPI;
        this.model = PropertiesModel.getInstance();
        this.timer = new Timer(500, (e) -> {
            String name = "Unknow";
            String role = "...";
            if (this.isUserAvalid()) {
                name = this.restAPI.extractUsername();
                role = this.restAPI.extractUserRole();
                this.btLogIn_out.setText(LOGOUT);
            } else {
                this.restAPI.logout();
                this.btLogIn_out.setText(LOGIN);
            }
            this.lbUserName.setText(name);
            this.lbUserRole.setText(role);
        });
        this.timer.start();
    }
    private static final String LOGOUT = "Logout";
    private static final String LOGIN = "Login";

    private boolean isUserAvalid() {
        return this.restAPI != null && this.restAPI.isTokenValid();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lbUserRole = new javax.swing.JLabel();
        lbUserName = new javax.swing.JLabel();
        btLogIn_out = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        lbUserRole.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbUserRole.setText("...");
        lbUserRole.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbUserName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbUserName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbUserName.setText("...");
        lbUserName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(lbUserRole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(17, 17, 17))
                    .addComponent(lbUserName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbUserRole, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btLogIn_out.setText("Login");
        btLogIn_out.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLogIn_outActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 44, Short.MAX_VALUE)
                        .addComponent(btLogIn_out)
                        .addGap(0, 43, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btLogIn_out)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btLogIn_outActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLogIn_outActionPerformed
        // TODO add your handling code here:
        if (this.restAPI.isTokenValid()) {
            this.restAPI.logout();
        }else{
            JOptionUtil.login("Login", (name, password) -> {
                var response = this.restAPI.sendPost(this.model.getProperty(Keyword.Url.LOGIN), JsonBodyAPI.builder()
                        .put("userid", name)
                        .put("password", password));
                if (!response.isFailStatusAndShowMessage(true)) {
                    this.restAPI.setJwtToken(response.getData());
                }
            }, null);
        }
    }//GEN-LAST:event_btLogIn_outActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btLogIn_out;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbUserName;
    private javax.swing.JLabel lbUserRole;
    // End of variables declaration//GEN-END:variables
}
