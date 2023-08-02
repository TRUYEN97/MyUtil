/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tec02.gui.panelGui;

import com.alibaba.fastjson.JSONObject;
import com.tec02.common.JOptionUtil;
import com.tec02.common.JsonBodyAPI;
import com.tec02.common.Keyword;
import com.tec02.common.RequestParam;
import com.tec02.common.RestAPI;
import com.tec02.common.RestUtil;
import com.tec02.gui.Panelupdate;
import com.tec02.gui.frameGui.Component.MyTable;
import com.tec02.gui.frameGui.Component.PopupMenu;
import com.tec02.gui.model.PropertiesModel;
import java.awt.HeadlessException;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class UploadFileProgramPanel extends Panelupdate {

    private final FileUploadPn fileUploadPn;
    private final RestUtil restUtil;
    private final MyTable myTable;
    private String pName;
    private String sName;
    private String lName;
    private Object fileID;

    /**
     * Creates new form UploadFileProgramPanel
     *
     * @param restAPI
     */
    public UploadFileProgramPanel(RestAPI restAPI) {
        initComponents();
        this.restUtil = new RestUtil(restAPI);
        this.myTable = new MyTable(tbShow);
        this.fileUploadPn = new FileUploadPn();
        this.fileUploadPn.setIsOnlyOneFile(true);
        this.pnUp.add(this.fileUploadPn);
        this.myTable.setDoubleClickAction((evt) -> {
            showVersionInfo(myTable.getRowSelectedMapValue());
        });
        PopupMenu tbMenu = this.myTable.getMenu();
        tbMenu.addItemMenu("Save", (e) -> {
            Object version = myTable.getRowSelectedValue(Keyword.NAME);
            if (version == null) {
                return;
            }
            this.restUtil.downloadFileSaveByChooseFile(
                    PropertiesModel.getConfig(Keyword.Url.FileProgram.GET_LAST_VERSION_DOWNLOAD),
                    RequestParam.builder()
                            .addParam(Keyword.ID, fileID)
                            .addParam(Keyword.VERSION, version)
            );
        });
        this.fileUploadPn.setUploadAction((input) -> {
            if (!input.hasChanged()) {
                return;
            }
            String des = input.getDescriptiton();
            String version = input.getVersion();
            String folder = input.getDir();
            if (des == null || des.isBlank()) {
                JOptionPane.showMessageDialog(null, "Description is empty!");
                return;
            }
            if (version == null || version.isBlank()) {
                JOptionPane.showMessageDialog(null, "version is empty!");
                return;
            }
            String name = input.getFileName();
            if (name == null || name.isBlank()) {
                JOptionPane.showMessageDialog(null, "file name is empty!");
                return;
            }
            File[] files = input.getSelectedFiles();
            boolean enable = input.isCheckBoxSelected();
            if (files != null && files.length == 1) {
                this.restUtil.uploadFile(PropertiesModel.getConfig(Keyword.Url.FileProgram.POST),
                        RequestParam.builder()
                                .addParam("pName", pName)
                                .addParam("sName", sName)
                                .addParam("lName", lName),
                        new JsonBodyAPI().put(Keyword.DIR, folder)
                                .put(Keyword.ID, fileID)
                                .put(Keyword.NAME, name)
                                .put(Keyword.VERSION, version)
                                .put(Keyword.DESCRIPTION, des)
                                .put(Keyword.ENABLE, enable),
                        files[0].getPath());
            } else {
                this.restUtil.update(PropertiesModel.getConfig(Keyword.Url.FileProgram.PUT), null,
                        JsonBodyAPI.builder()
                                .put(Keyword.ID, fileID)
                                .put(Keyword.NAME, name)
                                .put(Keyword.DIR, folder)
                                .put(Keyword.ENABLE, enable));
            }
            getFileVersion();
        });
    }

    private void getFileVersion() throws HeadlessException {
        JSONObject fileInfo = this.restUtil.getList(
                PropertiesModel.getConfig(Keyword.Url.FileProgram.GET),
                RequestParam.builder()
                        .addParam(Keyword.ID, fileID));
        if(fileInfo == null){
            return;
        }
        List<JSONObject> list = this.restUtil.getList(
                PropertiesModel.getConfig(Keyword.Url.FileProgram.GET_VERSION),
                RequestParam.builder()
                        .addParam(Keyword.ID, fileID));
        this.myTable.setDatas(list);
        this.fileUploadPn.setLbID(
                String.format("%s - %s", 
                fileID,
                fileInfo.getString(Keyword.NAME)));
        this.fileUploadPn.setDir(fileInfo.getString(Keyword.PATH));
        this.fileUploadPn.setFileName(fileInfo.getString(Keyword.NAME));
        if (list != null && !list.isEmpty()) {
            showVersionInfo(list.get(0));
        }
    }

    @Override
    public void update() {
        super.update();
        this.fileUploadPn.update();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnUp = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbShow = new javax.swing.JTable();

        pnUp.setLayout(new javax.swing.BoxLayout(pnUp, javax.swing.BoxLayout.LINE_AXIS));

        tbShow.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbShow);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(pnUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnUp;
    private javax.swing.JTable tbShow;
    // End of variables declaration//GEN-END:variables

    public void setFileInfo(String pName, String sName, String lName, Object fileID, String name) {
        this.pName = pName;
        this.sName = sName;
        this.lName = lName;
        this.fileID = fileID;
        setBorder(javax.swing.BorderFactory.createTitledBorder(String.valueOf(name)));
        this.getFileVersion();
    }

    public void clear() {
        this.myTable.clear();
        this.fileID = null;
        this.fileUploadPn.clear();
    }

    private void showVersionInfo(Map<String, Object> map) {
        Object enable = map.get(Keyword.ENABLE);
        this.fileUploadPn.setCheckBox(enable == null ? false : Boolean.parseBoolean(enable.toString()));
        this.fileUploadPn.setDescription(getInfomation(map));
    }

    private String getInfomation(Map map) {
        return String.format("Version: %s\r\nMD5: %s\r\nDetail: %s",
                map.get(Keyword.NAME),
                map.get("md5"),
                map.get(Keyword.DESCRIPTION));
    }
}
