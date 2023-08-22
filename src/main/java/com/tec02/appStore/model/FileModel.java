/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.model;

import java.nio.file.Path;
import java.util.Objects;

/**
 *
 * @author Administrator
 */
public class FileModel implements Cloneable{

    protected Long id;
    protected String filename;
    protected String filepath;
    protected String appName;
    protected String version;
    protected String description;
    protected String md5;

    public Path getLocalPath(String dir) {
        return Path.of(dir, appName, filepath, filename);
    }

    public String getDescription() {
        return description;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }
    
    public Long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getMd5() {
        return md5;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
    
    

    @Override
    public FileModel clone(){
        FileModel clone = new FileModel();
        clone.setAppName(appName);
        clone.setDescription(description);
        clone.setFilename(filename);
        clone.setFilepath(filepath);
        clone.setId(id);
        clone.setMd5(md5);
        clone.setVersion(version);
        return clone;
    }
    
    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FileModel appFile) {
            return appFile.id.equals(id)
                    && appFile.md5.equalsIgnoreCase(md5)
                    && appFile.filename.equalsIgnoreCase(filename)
                    && appFile.filepath.equalsIgnoreCase(filepath);
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.filename);
        hash = 59 * hash + Objects.hashCode(this.filepath);
        hash = 59 * hash + Objects.hashCode(this.md5);
        return hash;
    }

}
