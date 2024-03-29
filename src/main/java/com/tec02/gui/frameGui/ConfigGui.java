/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.tec02.gui.frameGui;

import com.alibaba.fastjson2.JSONObject;
import com.tec02.API.JsonBodyAPI;
import com.tec02.API.RequestParam;
import com.tec02.API.Response;
import com.tec02.API.RestAPI;
import com.tec02.Jmodel.Component.MyTable;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class ConfigGui extends AbsDisplayAble {

    private final MyTable tableModel;
    private final RestAPI api;
    private String addUrl;
    private String getUrl;
    private String key;
    private String deleteUrl;

    /**
     * Creates new form ConfigGui
     *
     * @param api
     */
    public ConfigGui(RestAPI api) {
        initComponents();
        this.tableModel = new MyTable(tbShow);
        if (api == null) {
            throw new NullPointerException("restApi is null!");
        }
        this.api = api;
    }

    public void setGetUrl(String getUrl) {
        this.getUrl = getUrl;
    }

    public void setAddUrl(String addUrl, String key) {
        this.addUrl = addUrl;
        this.key = key;
    }

    public void setDeleteUrl(String deleteUrl) {
        this.deleteUrl = deleteUrl;
    }

    public void reset() {
        this.txtInput.setText(null);
        this.tableModel.clear();
        this.setAddUrl(null, null);
        this.setDeleteUrl(null);
        this.setGetUrl(null);
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
        tbShow = new javax.swing.JTable();
        txtInput = new javax.swing.JTextField();
        btAdd = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tbShow.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbShow);

        txtInput.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInputKeyPressed(evt);
            }
        });

        btAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btAdd.setText("Add");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btDelete.setText("Delete");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtInput)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btDelete)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAdd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btDelete)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        addNewItem();
    }//GEN-LAST:event_btAddActionPerformed

    public void addNewItem() throws HeadlessException {
        String input = this.txtInput.getText();
        if (input == null || input.isBlank()) {
            JOptionPane.showMessageDialog(null, "input is empty!");
            return;
        }
        Response response = this.api.sendPost(addUrl, JsonBodyAPI.builder().put(key, input));
        if (response.isFailStatusAndShowMessage(true)) {
            return;
        }
        this.txtInput.setText(null);
        getList();
    }

    public void getList() throws HeadlessException {
        this.tableModel.clear();
        Response response = this.api.sendGet(getUrl);
        if (!response.getStatus()) {
            return;
        }
        List<JSONObject> items = response.getDatas();
        if (items == null || items.isEmpty()) {
            return;
        }
        ConfigGui.this.tableModel.initTable(items.get(0).keySet());
        for (var item : items) {
            ConfigGui.this.tableModel.addRow(item);
        }
    }

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        // TODO add your handling code here:
        int[] index = this.tableModel.getSelectedRows();
        if (index.length == 0) {
            return;
        }
        Object[] ids = this.tableModel.getRowValues(index, "id");
        Response response = this.api.sendDelete(this.deleteUrl, RequestParam.builder().addParam("id", ids));
        if(response.isFailStatusAndShowMessage(true)){
            return;
        }
        JOptionPane.showMessageDialog(null, response.getMessage());
        getList();
    }//GEN-LAST:event_btDeleteActionPerformed

    private void txtInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInputKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
             addNewItem();
        }
    }//GEN-LAST:event_txtInputKeyPressed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btDelete;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbShow;
    private javax.swing.JTextField txtInput;
    // End of variables declaration//GEN-END:variables
}
