/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tec02.gui.panelGui;

import com.tec02.gui.IAction;
import com.tec02.gui.Panelupdate;
import com.tec02.gui.frameGui.Component.MyChooser;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;

/**
 *
 * @author Administrator
 */
public class FileUploadPn extends Panelupdate {

    private String dir = "";
    private String fileName = "";
    private String version = "";
    private boolean enable = false;
    private final MyChooser chooser;
    private List<selectFileModel> fileSelected;
    private IAction<FileUploadPn> uploadAction;

    /**
     * Creates new form FileUploadPn
     */
    public FileUploadPn() {
        initComponents();
        this.chooser = new MyChooser();
        this.checkBoxGroup.add(cbBoxFile);
        this.checkBoxGroup.add(cbBoxFiles);
        this.checkBoxGroup.add(cbBoxFolderAndFile);
        this.cbBoxFolderAndFile.setSelected(true);
    }

    public void setIsOnlyOneFile(boolean isOnlyOneFile) {
        if (isOnlyOneFile) {
            this.cbBoxFile.setSelected(true);
        }
        this.cbBoxFiles.setEnabled(!isOnlyOneFile);
        this.cbBoxFolderAndFile.setEnabled(!isOnlyOneFile);
    }

    public String getDescriptiton() {
        return txtAreaDes.getText();
    }

    public void setUploadAction(IAction<FileUploadPn> uploadAction) {
        this.uploadAction = uploadAction;
    }

    public String getDir() {
        return this.txtFolder.getText();
    }

    public String getFileName() {
        return this.txtName.getText();
    }

    public void setFileName(String fileName) {
        this.txtName.setText(fileName);
        this.fileName = this.txtName.getText();
    }

    public void setDir(String dir) {
        this.txtFolder.setText(dir);
        this.dir = this.txtFolder.getText();
    }

    public String getVersion() {
        return this.txtVersion.getText();
    }

    public void setVersion(String version) {
        this.txtVersion.setText(version);
        this.version = this.txtVersion.getText();
    }

    public void setLbID(String str) {
        this.lbID.setText(str);
    }

    public void setDescription(String description) {
        this.txtAreaDes.setText(description);
    }

    public boolean isCheckBoxSelected() {
        return this.cbEnable.isSelected();
    }

    public void setCheckBox(boolean enable) {
        this.cbEnable.setSelected(enable);
        this.enable = this.cbEnable.isSelected();
    }

    public boolean hasChanged() {
        return this.enable != isCheckBoxSelected() || !this.dir.equals(getDir())
                || !this.version.equals(getVersion())
                || !this.fileName.equals(getFileName())
                || (fileSelected != null && !fileSelected.isEmpty());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        checkBoxGroup = new javax.swing.ButtonGroup();
        btUpload = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtFolder = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtVersion = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lbID = new javax.swing.JLabel();
        cbEnable = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtAreaLocalFolder = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        btChosse = new javax.swing.JButton();
        cbBoxFile = new javax.swing.JRadioButton();
        cbBoxFolderAndFile = new javax.swing.JRadioButton();
        cbBoxFiles = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAreaDes = new javax.swing.JTextArea();

        btUpload.setText("UpLoad");
        btUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUploadActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtName.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("File name");

        txtFolder.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFolder.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Folder");

        txtVersion.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtVersion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtVersion.setText("1.0.0");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Version");

        lbID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cbEnable.setSelected(true);
        cbEnable.setText("Enable");
        cbEnable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Choose file", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N

        txtAreaLocalFolder.setEditable(false);
        txtAreaLocalFolder.setColumns(20);
        txtAreaLocalFolder.setRows(4);
        txtAreaLocalFolder.setTabSize(1);
        jScrollPane4.setViewportView(txtAreaLocalFolder);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btChosse.setText("Choose");
        btChosse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btChosseActionPerformed(evt);
            }
        });

        cbBoxFile.setText("File");

        cbBoxFolderAndFile.setText("Folder and file");

        cbBoxFiles.setText("Files");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbBoxFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbBoxFiles)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(cbBoxFolderAndFile))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btChosse)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbBoxFile)
                    .addComponent(cbBoxFolderAndFile)
                    .addComponent(cbBoxFiles))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btChosse)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFolder)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtVersion)
                            .addComponent(cbEnable, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lbID, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbEnable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Description"));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        txtAreaDes.setColumns(20);
        txtAreaDes.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtAreaDes.setRows(5);
        txtAreaDes.setTabSize(2);
        jScrollPane3.setViewportView(txtAreaDes);

        jPanel4.add(jScrollPane3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btUpload)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btUpload)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUploadActionPerformed
        if (this.uploadAction != null) {
            this.uploadAction.action(this);
        }
    }//GEN-LAST:event_btUploadActionPerformed

    private void btChosseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btChosseActionPerformed
        // TODO add your handling code here:
        if ((fileSelected = hasChooseFile()) != null) {
            txtAreaDes.setText(null);
            if (fileSelected.size() > 1) {
                txtAreaLocalFolder.setText(null);
                boolean a = false;
                for (var file : fileSelected) {
                    if (a) {
                        txtAreaLocalFolder.append("\r\n");
                    } else {
                        a = true;
                    }
                    txtAreaLocalFolder.append(file.getLocalPath().toString());
                }
            } else if (fileSelected.size() == 1) {
                txtAreaLocalFolder.setText(fileSelected.get(0).getLocalPath().toString());
                if (txtName.getText().isBlank()) {
                    txtName.setText(fileSelected.get(0).getLocalPath().getFileName().toString());
                }
            }
        }
    }//GEN-LAST:event_btChosseActionPerformed

    private List<selectFileModel> hasChooseFile() {
        int rs;
        boolean isChooseFile = cbBoxFile.isSelected();
        boolean isChooseFiles = cbBoxFiles.isSelected();
        if (isChooseFile) {
            rs = this.chooser.showOpenFile(null, "");
        } else if (isChooseFiles) {
            rs = this.chooser.showOpenMultiFile(null, "");
        } else {
            rs = this.chooser.showOpenMutiFileOrFolder(null, "");
        }
        if (rs == JFileChooser.APPROVE_OPTION) {
            txtAreaDes.setText(null);
            File selectedFile = this.chooser.getSelectedFile();
            File[] selectedFiles = this.chooser.getSelectedFiles();
            if (isChooseFile) {
                return List.of(new selectFileModel(selectedFile.toPath(), null));
            } else {
                return getAllFileFrom(selectedFiles, selectedFile.getParentFile());
            }
        }
        return null;
    }
    
    private List<selectFileModel> getAllFileFrom(File[] files, File curr){
        List<selectFileModel> fileModels = new ArrayList<>();
        for (File file : files) {
            if(file.isDirectory()){
                fileModels.addAll(getAllFiles(file, curr));
            }else{
                fileModels.add(new selectFileModel(file.toPath(), curr.toPath()));
            }
        }
        return fileModels;
    }

    private List<selectFileModel> getAllFiles(File root, File curr) {
        List<selectFileModel> fileModels = new ArrayList<>();
        File[] listFiles = root.listFiles();
        if(listFiles == null){
            return List.of();
        }
        for (File file : listFiles) {
            if(file.isDirectory()){
                fileModels.addAll(getAllFiles(file, curr));
            }else{
                fileModels.add(new selectFileModel(file.toPath(), curr.toPath()));
            }
        }
        return fileModels;
    }

    public List<selectFileModel> getFileSelected() {
        return fileSelected;
    }
    
    public void clear() {
        setFileName(null);
        setDir(null);
        setVersion("1.0.0");
        setCheckBox(false);
        setName(null);
        this.txtAreaLocalFolder.setText(null);
        this.txtAreaDes.setText(null);
        this.fileSelected = null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btChosse;
    private javax.swing.JButton btUpload;
    private javax.swing.JRadioButton cbBoxFile;
    private javax.swing.JRadioButton cbBoxFiles;
    private javax.swing.JRadioButton cbBoxFolderAndFile;
    private javax.swing.JCheckBox cbEnable;
    private javax.swing.ButtonGroup checkBoxGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lbID;
    private javax.swing.JTextArea txtAreaDes;
    private javax.swing.JTextArea txtAreaLocalFolder;
    private javax.swing.JTextField txtFolder;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtVersion;
    // End of variables declaration//GEN-END:variables
    
    
    
    public class selectFileModel{
        private final Path localPath;
        private final Path curr;

        public selectFileModel(Path localPath, Path curr) {
            this.localPath = localPath;
            this.curr = curr;
        }

        public Path getLocalPath() {
            return localPath;
        }

        public Path getCurr() {
            return curr;
        }
        
        public Path getSubPath(){
            if(this.curr == null){
                return localPath;
            }
            return this.curr.relativize(localPath);
        }
    }
}
