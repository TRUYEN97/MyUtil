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
    private String cammand;
    private boolean awaysUpdate;
    private String description;
    private Path pathRun;
    private FileModel fileProgram;
    private final Map<Object, FileModel> files = new HashMap<>();

    public FileModel getFileProgram() {
        return fileProgram;
    }

    public String getCammand() {
        return cammand;
    }
    
    public Path getPathRun() {
        return pathRun;
    }

    public void setPathRun(Path pathRun) {
        this.pathRun = pathRun;
    }
    
    public boolean isAwaysUpdate() {
        return awaysUpdate;
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
