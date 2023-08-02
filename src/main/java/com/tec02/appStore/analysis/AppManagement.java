/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.analysis;

import com.tec02.appStore.StoreLoger;
import com.tec02.appStore.model.AppModel;
import com.tec02.appStore.model.FileModel;
import com.tec02.common.Keyword;
import com.tec02.common.RestAPI;
import com.tec02.common.Util;
import com.tec02.gui.model.PropertiesModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class AppManagement {

    private final Repository checkAppChanged;
    private final String appDir;
    private final Map<Object, AppProccess> appProccesses;
    private final StoreLoger loger;
    private final String backupDir;

    public AppManagement(RestAPI api, StoreLoger loger) {
        this.appDir = PropertiesModel.getConfig(Keyword.Store.LOCAL_APP);
        this.backupDir = PropertiesModel.getConfig(Keyword.Store.LOCAL_BACKUP);
        this.checkAppChanged = new Repository(loger);
        this.appProccesses = new HashMap<>();
        this.loger = loger;
    }

    public void checkUpdate(Collection<AppModel> newApps) {
        Map<Object, AppModel> appRemoveds = this.checkAppChanged.setData(newApps);
        Map<Object, AppModel> apps = this.checkAppChanged.getAppModels();
        if(apps.isEmpty()){
            if(appProccesses.isEmpty()){
                this.checkAppChanged.clear(new File(appDir));
                return;
            }
            for (AppProccess appProccess : appProccesses.values()) {
                if(!appProccess.isRuning()){
                    this.checkAppChanged.deleteApp(
                            appProccess.getAppModel().getFiles().values(),
                            appDir);
                }
            }
        }
        AppProccess appProccess;
        AppModel appModel;
        AppModel appRemoved;
        for (Object id : appRemoveds.keySet()) {
            appModel = apps.get(id);
            appRemoved = appRemoveds.get(id);
            if ((appProccess = appProccesses.get(id)) != null) {
                if (appProccess.isRuning()) {
                    System.out.println("is running");
                }
                appProccesses.remove(id);
            }
            if (appRemoved != null) {
                this.checkAppChanged.deleteApp(appRemoved.getFiles().values(), appDir);
            }
            if (appModel != null) {
                checkAppUpdate(appRemoved);
            }
        }
        for (Object key : apps.keySet()) {
            if (appRemoveds.containsKey(key)) {
                continue;
            }
            checkAppUpdate(apps.get(key));
        }
        for (Object key : apps.keySet()) {
            AppProccess proccess;
            AppModel appModel1 = apps.get(key);
            if ((proccess = appProccesses.get(key)) != null) {
                proccess.setRunFile(appModel1);
            } else {
                proccess = new AppProccess();
                proccess.setRunFile(appModel1);
                appProccesses.put(key, proccess);
            }
        }
    }

    public Map<Object, AppProccess> getAppProccesses() {
        return appProccesses;
    }
    
    private void checkAppUpdate(AppModel appModel) {
        checkAppUpdate(appModel.getFileProgram());
        for (FileModel fileModel : appModel.getFiles().values()) {
            checkAppUpdate( fileModel);
        }
    }

    private void checkAppUpdate(FileModel fileModel) {
        File fileBackUp;
        File file = fileModel.getLocalPath(appDir).toFile();
        if (!file.exists()
                || !Util.md5File(file.getPath())
                        .equals(fileModel.getMd5())) {
            fileBackUp = fileModel.getLocalPath(backupDir).toFile();
            try {
                Util.copyFile(fileBackUp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                this.loger.addLog("BACKUP", "%s -> %s, ok", fileBackUp.getPath(), file.getPath());
            } catch (IOException ex) {
                this.loger.addLog("BACKUP", "%s -> %s, Failed - %s",
                        fileBackUp.getPath(),
                        file.getPath(),
                        ex.getMessage());
            }
        }
    }

}
