/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class AppUpdateModel extends AppModel {

    private final Map<Object, FileModel> removeFiles = new HashMap<>();

    public Map<Object, FileModel> getRemoveFiles() {
        return removeFiles;
    }

    public void setRemoveFiles(Map<Object, FileModel> removeFiles) {
        this.removeFiles.clear();
        this.addRemoveFiles(removeFiles);
    }

    public void addRemoveFile(FileModel removeFile) {
        if (removeFile == null) {
            return;
        }
        this.removeFiles.put(removeFile.getId(), removeFile);
    }

    public void addRemoveFiles(Map<Object, FileModel> removeFiles) {
        if (removeFiles == null) {
            return;
        }
        for (FileModel fileModel : removeFiles.values()) {
            addRemoveFile(fileModel);
        }
    }
}
