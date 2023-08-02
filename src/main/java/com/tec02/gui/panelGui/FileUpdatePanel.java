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
import com.tec02.gui.frameGui.Component.MyChooser;
import com.tec02.gui.frameGui.Component.MyTable;
import com.tec02.gui.frameGui.Component.PopupMenu;
import com.tec02.gui.frameGui.treeFile.TreeFolder;
import com.tec02.gui.model.PropertiesModel;
import java.awt.HeadlessException;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class FileUpdatePanel extends Panelupdate {
    
    private final RestUtil restUtil;
    private final MyTable myTable;
    private final TreeFolder treeFolder;
    private final FileUploadPn fileUploadPn;
    private Object fgroupId;
    private Object fileID;

    /**
     * Creates new form FileUpdatePanel
     *
     * @param restAPI
     */
    public FileUpdatePanel(RestAPI restAPI) {
        initComponents();
        this.restUtil = new RestUtil(restAPI);
        this.myTable = new MyTable(tbShowVersions);
        this.treeFolder = new TreeFolder(tree, false);
        this.fileUploadPn = new FileUploadPn();
        this.pnFileUpload.add(this.fileUploadPn);
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
            File[] files = input.getSelectedFiles();
            boolean enable = input.isCheckBoxSelected();
            if (files == null || files.length <= 1) {
                String name = input.getFileName();
                if (name == null || name.isBlank()) {
                    JOptionPane.showMessageDialog(null, "file name is empty!");
                    return;
                }
                if (files == null || files.length == 0) {
                    this.restUtil.update(PropertiesModel.getConfig(Keyword.Url.File.PUT), 
                            null,
                            JsonBodyAPI.builder()
                                    .put(Keyword.ID, fileID)
                                    .put(Keyword.NAME, name)
                                    .put(Keyword.DIR, folder)
                                    .put(Keyword.ENABLE, enable)
                                    .put(Keyword.PARENT_ID, fgroupId));
                } else {
                    this.restUtil.uploadFile(PropertiesModel.getConfig(Keyword.Url.File.POST),
                            new JsonBodyAPI().put(Keyword.DIR, folder)
                                    .put(Keyword.ID, fileID)
                                    .put(Keyword.NAME, name)
                                    .put(Keyword.VERSION, version)
                                    .put(Keyword.DESCRIPTION, des)
                                    .put(Keyword.PARENT_ID, fgroupId)
                                    .put(Keyword.ENABLE, enable),
                            files[0].getPath());
                }
            } else {
                for (File file : files) {
                    if (!file.exists()) {
                        JOptionUtil.showMessage("file not exists! %s", file);
                        continue;
                    }
                    if (this.restUtil.uploadFile(PropertiesModel.getConfig(Keyword.Url.File.POST),
                            new JsonBodyAPI().put(Keyword.DIR, folder)
                                    .put(Keyword.ID, fileID)
                                    .put(Keyword.NAME, file.getName())
                                    .put(Keyword.VERSION, version)
                                    .put(Keyword.DESCRIPTION, des)
                                    .put(Keyword.PARENT_ID, fgroupId)
                                    .put(Keyword.ENABLE, enable),
                            file.getPath())) {
                    } else {
                        break;
                    }
                }

            }
            getAllFile();
        });
        PopupMenu menu = this.treeFolder.getPopupMenu();
        menu.addItemMenu("update tree", (e) -> {
            getAllFile();
        });
        menu.addItemMenu("Delete", (e) -> {
            List<Object> ids = this.treeFolder.getLeafSelectedValues("id");
            if (ids == null || ids.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nothing to delete");
                return;
            }
            int option = JOptionPane.showConfirmDialog(null, "Do you want to delete this List?", "Warning", JOptionPane.YES_NO_OPTION);
            if (option != JOptionPane.OK_OPTION) {
                return;
            }
            this.restUtil.delete(PropertiesModel.getConfig(Keyword.Url.File.DELETE),
                    RequestParam.builder().addParam("id", ids));
            getAllFile();
        });
        this.treeFolder.setDoubleClickAction((evt) -> {
            TreeFolder.FileNode fileNode = this.treeFolder.getNodeSelected();
            if (fileNode == null || fileNode.isFolder()) {
                initNewFileFormat(fileNode);
            } else {
                showFileSeclectedInfo(fileNode);
            }
        });
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
                    PropertiesModel.getConfig(Keyword.Url.File.GET_LAST_VERSION_DOWNLOAD),
                    RequestParam.builder()
                            .addParam(Keyword.ID, fileID)
                            .addParam(Keyword.VERSION, version)
            );
        });
    }

    private void showVersionInfo(Map<String, Object> map) {
        Object enable = map.get(Keyword.ENABLE);
        this.fileUploadPn.setCheckBox(enable == null ? false : Boolean.parseBoolean(enable.toString()));
        this.fileUploadPn.setDescription(getInfomation(map));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnFileUpload = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbShowVersions = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();

        setBorder(javax.swing.BorderFactory.createTitledBorder("..."));

        pnFileUpload.setBackground(new java.awt.Color(204, 204, 255));
        pnFileUpload.setBorder(javax.swing.BorderFactory.createTitledBorder("File Info"));
        pnFileUpload.setLayout(new javax.swing.BoxLayout(pnFileUpload, javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        tbShowVersions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbShowVersions);

        jPanel2.add(jScrollPane1);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        tree.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jScrollPane2.setViewportView(tree);

        jPanel3.add(jScrollPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnFileUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnFileUpload, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void setSorderName(String name) {
        setBorder(javax.swing.BorderFactory.createTitledBorder(name));
    }

    private void showFileSeclectedInfo(TreeFolder.FileNode fileNode) throws HeadlessException {
        clearInfo();
        getFileVersion(fileID = fileNode.getData(Keyword.ID));
        Path path = fileNode.getNodePath();
        this.fileUploadPn.setLbID(String.format("File: %s - %s", fileID,
                fileNode.getData(Keyword.NAME)));
        this.fileUploadPn.setDir(path.getParent() == null ? null : path.getParent().toString());
        this.fileUploadPn.setFileName(path.getFileName() == null ? null : path.getFileName().toString());
    }

    private void getFileVersion(Object id) throws HeadlessException {
        List<JSONObject> list = this.restUtil.getList(
                PropertiesModel.getConfig(Keyword.Url.File.GET_VERSION),
                RequestParam.builder()
                        .addParam(Keyword.ID, id));
        this.myTable.setDatas(list);
        if (list != null && !list.isEmpty()) {
            showVersionInfo(list.get(0));
        }
    }

    private void initNewFileFormat(TreeFolder.FileNode fileNode) {
        clearInfo();
        if (fileNode != null) {
            this.fileUploadPn.setDir(fileNode.getNodePath().toString());
        }
        this.fileUploadPn.setLbID("New file");
    }

    private void getAllFile() throws HeadlessException {
        new Thread() {
            @Override
            public void run() {
                try {
                    List<JSONObject> list = restUtil.getList(
                            PropertiesModel.getConfig(Keyword.Url.File.GET),
                            RequestParam.builder().addParam("id", fgroupId));
                    clear();
                    if (list == null) {
                        return;
                    }
                    for (JSONObject fileInfo : list) {
                        treeFolder.addFile(fileInfo.getString("path"),
                                fileInfo.getString(Keyword.NAME), fileInfo);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                } finally {
                    treeFolder.refresh();
                }
            }
        }.start();
    }

    @Override
    public void update() {
        super.update();
        this.fileUploadPn.update();
    }

    public void clear() {
        this.treeFolder.clear();
        clearInfo();
    }

    private void clearInfo() {
        this.myTable.clear();
        this.fileID = null;
        this.fileUploadPn.clear();
    }


    private String getInfomation(Map map) {
        return String.format("Version: %s\r\nMD5: %s\r\nDetail: %s",
                map.get(Keyword.NAME),
                map.get("md5"),
                map.get(Keyword.DESCRIPTION));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnFileUpload;
    private javax.swing.JTable tbShowVersions;
    private javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables

    public void setFileInfo(Object fgId, Object fgName) {
        this.fgroupId = fgId;
        this.setSorderName(String.valueOf(fgName));
        this.getAllFile();
    }
}
