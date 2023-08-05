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
public class AppModel {

    private Object id;
    private String name;
    private String password;
    private boolean awaysRun;
    private String description;
    private FileModel fileProgram;
    private final Map<Object, FileModel> files = new HashMap<>();

    public FileModel getFileProgram() {
        return fileProgram;
    }

    public void setFileProgram(FileModel fileProgram) {
        this.fileProgram = fileProgram;
    }
    
    public boolean isAwaysRun() {
        return awaysRun;
    }

    public Path getLocalPath(String dir) {
        return Path.of(dir, name);
    }

    public void setFiles(Map<Object, FileModel> files) {
        this.files.clear();
        this.files.putAll(files);
    }

    public Object getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getDescription() {
        return description;
    }

    public Map<Object, FileModel> getFiles() {
        return files;
    }

    public boolean isEmpty() {
        return files.isEmpty();
    }

}
