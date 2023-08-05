/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.model;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class AppRemove{
    private String name;
    private boolean deleteAll;
    private FileModel fileProgram;
    private Map<Object, FileModel> files = new HashMap<>();

    public Path getLocalPath(String dir) {
        return Path.of(dir, name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isDeleteAll() {
        return deleteAll;
    }

    public void setFileProgram(FileModel fileProgram) {
        this.fileProgram = fileProgram;
    }

    public FileModel getFileProgram() {
        return fileProgram;
    }

    public Map<Object, FileModel> getFiles() {
        return files;
    }

    public void setFiles(Map<Object, FileModel> files) {
        this.files = files;
    }
    
    public void setDeleteAll(boolean deleteAll) {
        this.deleteAll = deleteAll;
    }
    
}
