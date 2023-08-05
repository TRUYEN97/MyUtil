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
public class FileModel {

    protected Long id;
    protected String filename;
    protected String filepath;
    protected String appName;
    protected String version;
    protected String md5;

    public Path getLocalPath(String dir) {
        return Path.of(dir, appName, filepath, filename);
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
