/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tec02.appStore;

import com.tec02.appStore.analysis.AppPackage;
import com.tec02.appStore.analysis.AppManagement;
import com.tec02.API.JsonBodyAPI;
import com.tec02.common.Keyword;
import com.tec02.common.PcInformation;
import com.tec02.API.RequestParam;
import com.tec02.API.RestAPI;
import com.tec02.gui.Panelupdate;
import com.tec02.common.PropertiesModel;
import com.tec02.common.RestUtil;
import com.tec02.common.JOptionUtil;
import com.tec02.gui.frameGui.AbsDisplayAble;
import com.tec02.gui.panelGui.UserInfomation;

/**
 *
 * @author Administrator
 */
public class AppStore extends Panelupdate {

    private final UserInfomation userInfo;
    private final StorePanel storePanel;
    private final AppPackage appPackage;
    private final AppManagement appManagement;
    private final RestUtil restUtil;
    private final PcInformation pcInfo;
    private final Object lock;
    private final Thread threadUpdateBackup;
    private final Thread threadUpdateApp;
    private final Thread threadUpdateUI;
    private final StoreLoger loger;
    private boolean pcUpdate;

    /**
     * Creates new form AppStore
     *
     * @param api
     * @param view
     * @throws java.io.IOException
     */
    public AppStore(RestAPI api, AbsDisplayAble view) throws Exception {
        initComponents();
        api.setTextComponent(txtShowMessage);
        this.pcInfo = PcInformation.getInstance();
        this.restUtil = new RestUtil(api);
        this.loger = new StoreLoger();
        this.appPackage = new AppPackage(api, loger);
        this.appManagement = new AppManagement(api, loger, view);
        this.storePanel = new StorePanel(this.appManagement, 3, 3);
        this.pnStore.add(this.storePanel);
        this.storePanel.update();
        this.userInfo = new UserInfomation(api);
        this.pnUser.add(userInfo);
        this.userInfo.update();
        this.lock = new Object();
        this.threadUpdateBackup = new Thread(() -> {
            int time = PropertiesModel.getInteger(Keyword.Store.UPDATE_TIME, 10000);
            while (true) {
                try {
                    synchronized (lock) {
                        checkAppUpdate();
                        lock.notifyAll();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    this.loger.addLog("ERROR", "backup loop: %s", ex.getLocalizedMessage());
                    JOptionUtil.showMessage(ex.getMessage());
                }
                try {
                    Thread.sleep(time);
                } catch (InterruptedException ex) {
                }
            }
        });
        this.threadUpdateApp = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            while (true) {
                try {
                    synchronized (lock) {
                        this.appManagement.checkUpdate(this.appPackage.getApps().values());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    this.loger.addLog("ERROR", "app loop check: %s", ex.getLocalizedMessage());
                    JOptionUtil.showMessage(ex.getMessage());
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
        });
        this.threadUpdateUI = new Thread(() -> {
            while (true) {
                try {
                    this.storePanel.updateDisplay();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    this.loger.addLog("ERROR", "Show UI loop: %s", ex.getLocalizedMessage());
                    JOptionUtil.showMessage(ex.getMessage());
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
            }
        });
    }

    public void run() {
        if (!this.threadUpdateBackup.isAlive()) {
            this.threadUpdateBackup.start();
        }
        if (!this.threadUpdateApp.isAlive()) {
            this.threadUpdateApp.start();
        }
        if (!this.threadUpdateUI.isAlive()) {
            this.threadUpdateUI.start();
        }
        this.appManagement.init();
    }

    private void updatePcInfo() {
        if (pcInfo.isChanged() || !pcUpdate) {
            pcUpdate = this.restUtil.update(PropertiesModel.getConfig(Keyword.Url.Pc.PUT_INFO),
                    RequestParam.builder().addPath(pcInfo.getPcName()),
                    JsonBodyAPI.builder()
                            .put("os", pcInfo.getSystemName())
                            .put("mac", pcInfo.getMacs().toString())
                            .put("ip", pcInfo.getIps().toString()), false
            );
        }
    }

    private void checkAppUpdate() {
        updatePcInfo();
        this.appPackage.checkAppUpdate(RequestParam.builder().addParam(Keyword.PC_NAME,
                PcInformation.getInstance().getPcName()));
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

        pnStore.setBackground(new java.awt.Color(153, 204, 255));
        pnStore.setLayout(new javax.swing.BoxLayout(pnStore, javax.swing.BoxLayout.LINE_AXIS));

        pnUser.setBackground(new java.awt.Color(204, 204, 255));
        pnUser.setLayout(new javax.swing.BoxLayout(pnUser, javax.swing.BoxLayout.LINE_AXIS));

        txtShowMessage.setEditable(false);
        txtShowMessage.setColumns(15);
        txtShowMessage.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtShowMessage.setLineWrap(true);
        txtShowMessage.setRows(5);
        txtShowMessage.setTabSize(1);
        txtShowMessage.setWrapStyleWord(true);
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
                .addComponent(pnStore, javax.swing.GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
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
