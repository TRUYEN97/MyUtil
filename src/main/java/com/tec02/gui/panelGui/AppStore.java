/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tec02.gui.panelGui;

import com.tec02.appStore.analysis.AppPackage;
import com.tec02.appStore.StoreLoger;
import com.tec02.appStore.StorePanel;
import com.tec02.appStore.analysis.AppManagement;
import com.tec02.common.Keyword;
import com.tec02.common.ProgramInformation;
import com.tec02.common.RequestParam;
import com.tec02.common.RestAPI;
import com.tec02.gui.Panelupdate;
import com.tec02.gui.model.PropertiesModel;
import java.io.IOException;
import javax.swing.Timer;

/**
 *
 * @author Administrator
 */
public class AppStore extends Panelupdate {

    private final UserInfomation userInfo;
    private final StorePanel storePanel;
    private final AppPackage appPackage;
    private final StoreLoger loger;
    private final AppManagement appManagement;

    /**
     * Creates new form AppStore
     *
     * @param api
     * @throws java.io.IOException
     */
    public AppStore(RestAPI api) throws IOException {
        initComponents();
        this.loger = new StoreLoger();
        this.appManagement = new AppManagement(api, this.loger);
        this.storePanel = new StorePanel(this.appManagement, 3, 4);
        this.userInfo = new UserInfomation(api);
        this.pnUser.add(userInfo);
        this.userInfo.update();
        this.pnStore.add(this.storePanel);
        this.storePanel.update();
        this.appPackage = new AppPackage(api, this.loger);
        checkAppUpdate();
        new Timer(PropertiesModel.getInteger(Keyword.Store.UPDATE_TIME, 20000), (e) -> {
            checkAppUpdate();
        }).start();
    }

    private void checkAppUpdate() {
        this.appPackage.checkAppUpdate(RequestParam.builder().addParam(Keyword.PC_NAME,
                ProgramInformation.getInstance().getPcName()));
        this.appManagement.checkUpdate(this.appPackage.getApps().values());
        this.storePanel.updateApp();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnStore = new javax.swing.JPanel();
        pnUser = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtShowMessage = new javax.swing.JTextArea();

        pnStore.setBackground(new java.awt.Color(204, 255, 255));
        pnStore.setLayout(new javax.swing.BoxLayout(pnStore, javax.swing.BoxLayout.LINE_AXIS));

        pnUser.setBackground(new java.awt.Color(204, 204, 255));
        pnUser.setLayout(new javax.swing.BoxLayout(pnUser, javax.swing.BoxLayout.LINE_AXIS));

        txtShowMessage.setEditable(false);
        txtShowMessage.setColumns(15);
        txtShowMessage.setRows(5);
        txtShowMessage.setTabSize(1);
        jScrollPane1.setViewportView(txtShowMessage);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnUser, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnStore, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnStore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnUser, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnStore;
    private javax.swing.JPanel pnUser;
    private javax.swing.JTextArea txtShowMessage;
    // End of variables declaration//GEN-END:variables
}