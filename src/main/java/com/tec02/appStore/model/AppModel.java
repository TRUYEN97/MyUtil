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

    protected Object id;
    protected String name;
    protected String password;
    protected boolean alwaysRun;
    protected boolean alwaysUpdate;
    protected String description;
    protected FileModel fileProgram;
    protected final Map<Object, FileModel> files = new HashMap<>();

    public FileModel getFileProgram() {
        return fileProgram;
    }

    public void setFileProgram(FileModel fileProgram) {
        this.fileProgram = fileProgram;
    }
    
    public boolean isAlwaysRun() {
        return alwaysRun;
    }

    public boolean isAlwaysUpdate() {
        return alwaysUpdate;
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

    public void setId(Object id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAlwaysRun(boolean alwaysRun) {
        this.alwaysRun = alwaysRun;
    }

    public void setAlwaysUpdate(boolean alwaysUpdate) {
        this.alwaysUpdate = alwaysUpdate;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    

}
