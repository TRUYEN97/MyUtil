/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tec02.gui.panelGui;

import com.alibaba.fastjson.JSONObject;
import com.tec02.common.API.JsonBodyAPI;
import com.tec02.common.Keyword;
import com.tec02.common.API.RequestParam;
import com.tec02.common.API.RestAPI;
import com.tec02.common.API.RestUtil;
import com.tec02.gui.IAction;
import com.tec02.gui.Panelupdate;
import com.tec02.gui.frameGui.Component.MyTable;
import com.tec02.gui.frameGui.Component.PopupMenu;
import com.tec02.common.PropertiesModel;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class TableAndLocation extends Panelupdate {

    private final LocationFilter locationFilter;
    private final MyTable myTable;
    private final RestUtil restUtil;
    private String urlGet;
    private String urlPost;
    private String urlDelete;
    private String urlPut;

    /**
     * Creates new form TableAndLocation
     *
     * @param aPI
     */
    public TableAndLocation(RestAPI aPI) {
        initComponents();
        this.myTable = new MyTable(tbShow);
        this.restUtil = new RestUtil(aPI);
        this.locationFilter = new LocationFilter(
                aPI
                , PropertiesModel.getConfig(Keyword.Url.Product.GET)
                , PropertiesModel.getConfig(Keyword.Url.Station.GET)
                , PropertiesModel.getConfig(Keyword.Url.Line.GET));
        this.pnFilter.add(this.locationFilter);
        this.locationFilter.update();
    }

    @Override
    public void update() {
        super.update();
        this.locationFilter.update();
        this.locationFilter.refresh();
        find();
    }

    public PopupMenu getSelectedMenu() {
        return myTable.getSelectedMenu();
    }

    public void setSelectedMenu(PopupMenu selectedMenu) {
        this.myTable.setSelectedMenu(selectedMenu);
    }

    public boolean update(RequestParam param, JsonBodyAPI bodyAPI) {
        restUtil.setShowJoptionMess(true);
        boolean rs = this.restUtil.update(urlPut, param, bodyAPI);
        this.find();
        return rs;
    }

    public <T> List<T> getListTableSelectedValue(String columnName) {
        return this.myTable.getRowSelectedValues(columnName);
    }

    public MyTable getTableModel() {
        return myTable;
    }

    public void setUrlAdd(String addUrl) {
        this.urlPost = addUrl;
    }

    public void setUrlGet(String getUrl) {
        this.urlGet = getUrl;
    }

    public void setUrlPut(String urlPut) {
        this.urlPut = urlPut;
    }

    public void setUrlDelete(String urlDetlete) {
        this.urlDelete = urlDetlete;
    }

    public void setMenu(PopupMenu menu) {
        this.myTable.setMenu(menu);
    }

    public PopupMenu getMenu() {
        return this.myTable.getMenu();
    }

    public void setDoubleClickAction(IAction<MouseEvent> action) {
        this.myTable.setDoubleClickAction(action);
    }

    public void clear() {
        this.myTable.clear();
    }

    public void getList(String name) throws HeadlessException {
        clear();
        restUtil.setShowJoptionMess(false);
        List<JSONObject> items = this.restUtil.getList(urlGet,
                RequestParam.builder()
                        .addParam("pName", getProductSelection())
                        .addParam("sName", getStationSelection())
                        .addParam("lName", getLineSelection())
                        .addParam("name", name));
        if (items == null || items.isEmpty()) {
            return;
        }
        if (myTable != null) {
            myTable.initTable(items.get(0).keySet());
            for (var item : items) {
                myTable.addRow(item);
            }
        }
    }

    public void addNew(JsonBodyAPI bodyAPI) throws HeadlessException {
        // TODO add your handling code here:
        restUtil.setShowJoptionMess(true);
        this.restUtil.addNew(urlPost, new RequestParam()
                .addParam("pName", getProductSelection())
                .addParam("sName", getStationSelection())
                .addParam("lName", getLineSelection()), bodyAPI);
        this.getList(null);
    }

    public void uploadFile(JsonBodyAPI bodyAPI, String path) {
        restUtil.setShowJoptionMess(true);
        this.restUtil.uploadFile(urlPost, 
                new RequestParam()
                .addParam("pName", getProductSelection())
                .addParam("sName", getStationSelection())
                .addParam("lName", getLineSelection()),
                bodyAPI, path);
        this.getList(null);
    }

    public void deleteSeleled() {
        List<Object> ids = myTable.getRowSelectedValues("id");
        if (ids == null || ids.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nothing to delete");
            return;
        }
        int option = JOptionPane.showConfirmDialog(null, "Do you want to delete this List?", "Warning", JOptionPane.YES_NO_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        restUtil.setShowJoptionMess(true);
        this.restUtil.delete(urlDelete, RequestParam.builder().addParam("id", ids));
        getList(null);
    }

    public String getProductSelection() {
        return this.locationFilter.getProductSelection();
    }

    public String getStationSelection() {
        return this.locationFilter.getStationSelection();
    }

    public String getLineSelection() {
        return this.locationFilter.getLineSelection();
    }

    public List<String> getProductItems() {
        return this.locationFilter.getProductItems();
    }

    public List<String> getStationItems() {
        return this.locationFilter.getStationItems();
    }

    public List<String> getLineItems() {
        return this.locationFilter.getLineItems();
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
        jPanel1 = new javax.swing.JPanel();
        btFind = new javax.swing.JButton();
        pnFilter = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        tbShow.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbShow);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        btFind.setText("Find");
        btFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFindActionPerformed(evt);
            }
        });

        pnFilter.setBackground(new java.awt.Color(153, 153, 255));
        pnFilter.setLayout(new javax.swing.BoxLayout(pnFilter, javax.swing.BoxLayout.LINE_AXIS));

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
        });

        jLabel1.setText("Search name");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnFilter, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 2, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btFind, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSearch)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btFind)
                .addGap(36, 36, 36))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFindActionPerformed
        find();
    }//GEN-LAST:event_btFindActionPerformed

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            find();
        }
    }//GEN-LAST:event_txtSearchKeyPressed

    public void find() throws HeadlessException {
        // TODO add your handling code here:
        String name = txtSearch.getText();
        this.getList(name.isBlank() ? null : name);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnFilter;
    private javax.swing.JTable tbShow;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables

    public Object getTableSelectedValue(String columnName) {
        return this.myTable.getRowSelectedValue(columnName);
    }
    
    public <T> T getTableSelectedValueT(String columnName) {
        return this.myTable.getRowSelectedValue(columnName);
    }
}
