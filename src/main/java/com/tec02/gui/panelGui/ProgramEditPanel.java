/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tec02.gui.panelGui;

import com.tec02.common.API.JsonBodyAPI;
import com.tec02.common.API.RequestParam;
import com.tec02.common.API.RestAPI;
import com.tec02.common.API.RestUtil;
import com.tec02.common.JOptionUtil;
import com.tec02.common.Keyword;
import com.tec02.common.PropertiesModel;
import com.tec02.gui.Panelupdate;
import com.tec02.gui.frameGui.Component.MyTable;
import com.tec02.gui.frameGui.Component.PopupMenu;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class ProgramEditPanel extends Panelupdate {

    /**
     * Creates new form ProgramEditPanel
     */
    private boolean enable;
    private boolean alwaysRun;
    private boolean alwaysUpdate;
    private String password;
    private final RestUtil restUtil;
    private final MyTable fileProgramTable;
    private final MyTable pcTable;
    private final MyTable fGroupTable;
    private final FileUpdatePanel updateFile;
    private final UploadFileProgramPanel fileProgramPanel;
    private Object programId;

    public ProgramEditPanel(RestAPI api) {
        initComponents();
        this.restUtil = new RestUtil(api);
        this.fileProgramPanel = new UploadFileProgramPanel(api);
        this.fileProgramTable = new MyTable(tbFileProgram);
        this.fGroupTable = new MyTable(tbFgroup);
        this.pcTable = new MyTable(tbPc);
        this.updateFile = new FileUpdatePanel(api);
        initFileProgramTable();
        initFgroup();
    }

    private void initFileProgramTable() {
        PopupMenu fProgrampMenu = this.fileProgramTable.getMenu();
        this.fileProgramTable.setDoubleClickAction((input) -> {
            showFileProgram();
        });
        fProgrampMenu.addItemMenu("view", (e) -> {
            showFileProgram();
        });
        fProgrampMenu.addItemMenu("change program", (e) -> {
            Map<String, Object> fileProgram = JOptionUtil.getTableSelectedItem("select file-program",
                    this.restUtil.getList(PropertiesModel.getConfig(Keyword.Url.Program.GET_PROGRAMS),
                            RequestParam.builder().addParam(Keyword.ID, programId)));
            if (fileProgram == null || fileProgram.isEmpty()) {
                return;
            }
            this.restUtil.update(PropertiesModel.getConfig(Keyword.Url.Program.PUT_FILE_PROGRAM),
                    RequestParam.builder().addParam(Keyword.ID, programId),
                    JsonBodyAPI.builder()
                            .put(Keyword.ID, fileProgram.get(Keyword.ID)));
            refreshProgramVersions();
        });
        fProgrampMenu.addItemMenu("remove program", (e) -> {
            if (fileProgramTable.isDataEmpty()) {
                return;
            }
            this.restUtil.update(PropertiesModel.getConfig(Keyword.Url.Program.PUT_FILE_PROGRAM),
                    RequestParam.builder().addParam(Keyword.ID, programId)
                            .addParam(Keyword.ACTION, "1"),
                    JsonBodyAPI.builder()
                            .put(Keyword.ID, fileProgramTable.getRowValue(0, Keyword.ID)));
            refreshProgramVersions();
        });
    }

    private void showFileProgram() {
        if (programId == null) {
            return;
        }
        Object name = this.fileProgramTable.getRowValue(0, Keyword.NAME);
        this.fileProgramPanel.clear();
        this.fileProgramPanel.setFileInfo(null, null, null, programId, name.toString());
        JOptionUtil.showObject(this.fileProgramPanel, "File viewer");
        refreshProgramVersions();
    }

    private void initFgroup() {
        this.fGroupTable.setDoubleClickAction((input) -> {
            Object fgId = this.fGroupTable.getRowSelectedValue(Keyword.ID);
            if (fgId == null) {
                return;
            }
            String name = this.fGroupTable.getRowSelectedValue(Keyword.NAME);
            this.updateFile.setFileInfo(fgId, name);
            JOptionUtil.showObject(this.updateFile, name);
        });
        PopupMenu fGroupMenu = this.fGroupTable.getMenu();
        fGroupMenu.addItemMenu("Add file", (e) -> {
            var fileGroups = JOptionUtil.getTableSelectedItems("select file-group",
                    this.restUtil.getList(PropertiesModel.getConfig(Keyword.Url.Program.GET_FGROUPS),
                            RequestParam.builder().addParam(Keyword.ID, programId)));
            if (fileGroups == null || fileGroups.isEmpty()) {
                return;
            }
            List<Object> ids = new ArrayList<>();
            for (Map fileGroup : fileGroups) {
                ids.add(fileGroup.get(Keyword.ID));
            }
            this.restUtil.update(PropertiesModel.getConfig(Keyword.Url.Program.PUT_FGROUP),
                    RequestParam.builder().addParam(Keyword.ID, programId),
                    JsonBodyAPI.builder()
                            .put(Keyword.IDS, ids));
            refeshFgroups();
        });
        fGroupMenu.addItemMenu("remove file", (e) -> {
            var fileGroups = JOptionUtil.getTableSelectedItems("remove file-group",
                    this.restUtil.getList(PropertiesModel.getConfig(Keyword.Url.Program.GET_FGROUP),
                            RequestParam.builder().addParam(Keyword.ID, programId)));
            if (fileGroups == null || fileGroups.isEmpty()) {
                return;
            }
            List<Object> ids = new ArrayList<>();
            for (Map fileGroup : fileGroups) {
                ids.add(fileGroup.get(Keyword.ID));
            }
            this.restUtil.update(PropertiesModel.getConfig(Keyword.Url.Program.PUT_FGROUP),
                    RequestParam.builder().addParam(Keyword.ACTION, "1"),
                    JsonBodyAPI.builder().put(Keyword.ID, programId).put(Keyword.IDS, ids));
            refeshFgroups();
        });
    }

    public void setProgramId(Object programId) {
        this.programId = programId;
        refreshProgramVersions();
        refreshPcs();
        refeshFgroups();
    }

    private void refeshFgroups() throws HeadlessException {
        this.setFgroups(this.restUtil.getList(
                PropertiesModel.getConfig(Keyword.Url.Program.GET_FGROUP),
                RequestParam.builder().addParam(Keyword.ID, this.programId)));
    }

    private void refreshPcs() throws HeadlessException {
        this.setPCs(this.restUtil.getList(
                PropertiesModel
                        .getConfig(Keyword.Url.Program.GET_PCS),
                RequestParam.builder()
                        .addParam(Keyword.ID, this.programId)));
    }

    private void refreshProgramVersions() {
        this.fileProgramTable.setDatas(this.restUtil.getList(PropertiesModel
                .getConfig(Keyword.Url.Program.GET_FILE_PROGRAM_VSERSION),
                RequestParam.builder().addParam(Keyword.ID, programId))
        );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        tbFileProgram = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        cbEnable = new javax.swing.JRadioButton();
        cbAlwaysRun = new javax.swing.JRadioButton();
        txtPassword = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbAwaysUpdate = new javax.swing.JRadioButton();
        pnU = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbPc = new javax.swing.JTable();
        pnd = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbFgroup = new javax.swing.JTable();

        tbFileProgram.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tbFileProgram);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Description");

        txtDescription.setEditable(false);
        txtDescription.setColumns(20);
        txtDescription.setRows(4);
        txtDescription.setTabSize(1);
        jScrollPane2.setViewportView(txtDescription);

        cbEnable.setSelected(true);
        cbEnable.setText("Enable");
        cbEnable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        cbAlwaysRun.setSelected(true);
        cbAlwaysRun.setText("Always run");
        cbAlwaysRun.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        txtPassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Password (max: 20)");

        cbAwaysUpdate.setSelected(true);
        cbAwaysUpdate.setText("Always update");
        cbAwaysUpdate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtPassword)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cbEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cbAwaysUpdate)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cbAlwaysRun, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbAlwaysRun)
                    .addComponent(cbEnable)
                    .addComponent(cbAwaysUpdate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnU.setBackground(new java.awt.Color(204, 204, 255));
        pnU.setBorder(javax.swing.BorderFactory.createTitledBorder("Pc"));

        tbPc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(tbPc);

        javax.swing.GroupLayout pnULayout = new javax.swing.GroupLayout(pnU);
        pnU.setLayout(pnULayout);
        pnULayout.setHorizontalGroup(
            pnULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnULayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 843, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnULayout.setVerticalGroup(
            pnULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnULayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnd.setBackground(new java.awt.Color(204, 204, 255));
        pnd.setBorder(javax.swing.BorderFactory.createTitledBorder("File group"));

        tbFgroup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane7.setViewportView(tbFgroup);

        javax.swing.GroupLayout pndLayout = new javax.swing.GroupLayout(pnd);
        pnd.setLayout(pndLayout);
        pndLayout.setHorizontalGroup(
            pndLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pndLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7)
                .addContainerGap())
        );
        pndLayout.setVerticalGroup(
            pndLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pndLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(pnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void setEnable(Boolean b) {
        this.cbEnable.setSelected(b == null ? false : b);
        enable = this.cbEnable.isSelected();
    }
    

    public boolean getEnable() {
        return this.cbEnable.isSelected();
    }

    public void setAlwaysUpdate(Boolean b) {
        this.cbAwaysUpdate.setSelected(b == null ? false : b);
        alwaysUpdate = this.cbAwaysUpdate.isSelected();
    }

    public boolean getAlwaysUpdate() {
        return this.cbAwaysUpdate.isSelected();
    }
    
    public void setAlwaysRun(Boolean b) {
        this.cbAlwaysRun.setSelected(b == null ? false : b);
        alwaysRun = this.cbAlwaysRun.isSelected();
    }

    public boolean getAlwaysRun() {
        return this.cbAlwaysRun.isSelected();
    }

    public void setDescription(String description) {
        this.txtDescription.setText(description);
    }

    public void setPassword(String password) {
        txtPassword.setText(password);
        this.password = txtPassword.getText();
    }

    public String getPassword() {
        return txtPassword.getText();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton cbAlwaysRun;
    private javax.swing.JRadioButton cbAwaysUpdate;
    private javax.swing.JRadioButton cbEnable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JPanel pnU;
    private javax.swing.JPanel pnd;
    private javax.swing.JTable tbFgroup;
    private javax.swing.JTable tbFileProgram;
    private javax.swing.JTable tbPc;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtPassword;
    // End of variables declaration//GEN-END:variables

    public boolean hasChange() {
        return !password.equals(getPassword())
                || enable != getEnable()
                || alwaysUpdate != getAlwaysUpdate()
                || alwaysRun != getAlwaysRun()
                ;
    }

    public void setPCs(List<? extends Map> list) {
        this.pcTable.clear();
        this.pcTable.setDatas(list);
    }

    public void setFgroups(List<? extends Map> list) {
        this.fGroupTable.clear();
        this.fGroupTable.setDatas(list);
    }
}
