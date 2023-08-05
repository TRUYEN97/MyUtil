/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.analysis;

import com.tec02.appStore.StoreLoger;
import com.tec02.appStore.model.AppModel;
import com.tec02.appStore.model.AppRemove;
import com.tec02.appStore.model.FileModel;
import com.tec02.common.Keyword;
import com.tec02.common.API.RestAPI;
import com.tec02.common.Util;
import com.tec02.common.PropertiesModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Timer;

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
    private final Map<Object, AppRemove> appRemoves;

    public AppManagement(RestAPI api, StoreLoger loger) {
        this.appDir = PropertiesModel.getConfig(Keyword.Store.LOCAL_APP);
        this.backupDir = PropertiesModel.getConfig(Keyword.Store.LOCAL_BACKUP);
        Util.deleteFolder(new File(appDir));
        this.checkAppChanged = new Repository(loger);
        this.appProccesses = new HashMap<>();
        this.appRemoves = new HashMap<>();
        this.loger = loger;
        new Timer(1000, (e) -> {
            System.out.println(appRemoves);
            if (appRemoves.isEmpty()) {
                return;
            }
            List<Object> removeIds = new ArrayList<>();
            for (Object id : appRemoves.keySet()) {
                if (isChangeable(id)) {
                    this.checkAppChanged.deleteApp(appRemoves.get(id), appDir);
                    removeIds.add(id);
                }
            }
            for (Object id : removeIds) {
                appRemoves.remove(id);
            }
        }).start();
    }

    public void checkUpdate(Collection<AppModel> newApps) {
        Map<Object, AppRemove> appRemoveds = this.checkAppChanged.setData(newApps);
        Map<Object, AppModel> apps = this.checkAppChanged.getAppModels();
        for (Object id : appRemoveds.keySet()) {
            AppRemove appRemove = appRemoveds.get(id);
            if (this.appProccesses.containsKey(id)) {
                if (isChangeable(id)) {
                    this.checkAppChanged.deleteApp(appRemove, appDir);
                } else {
                    this.appRemoves.put(id, appRemove);
                }
            }
        }
        for (Object id : apps.keySet()) {
            AppModel appModel = apps.get(id);
            AppProccess proccess;
            if ((proccess = this.appProccesses.get(id)) == null) {
                proccess = new AppProccess();
            }
            try {
                checkAppUpdate(appModel, id);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            proccess.setRunFile(appModel);
            this.appProccesses.put(id, proccess);
        }
        List<Object> idRemoves = new ArrayList<>();
        for (Object id : this.appProccesses.keySet()) {
            if (!apps.containsKey(id)) {
                if (isChangeable(id)) {
                    idRemoves.add(id);
                }
            }
        }
        for (Object id : idRemoves) {
            if (this.appProccesses.containsKey(id)) {
                AppModel appRemoved = this.appProccesses.get(id).getAppModel();
                this.appProccesses.remove(id);
                Util.deleteFolder(appRemoved.getLocalPath(appDir).toFile());
            }
        }
    }

    private boolean isChangeable(Object id) {
        AppProccess appProccess;
        return (appProccess = appProccesses.get(id)) == null || !appProccess.isRuning();
    }

    public Map<Object, AppProccess> getAppProccesses() {
        return appProccesses;
    }

    private void checkAppUpdate(AppModel appModel, Object id) throws Exception {
        if (isFileNeedToUpdate(appModel.getFileProgram())) {
            FileModel fileProgram = appModel.getFileProgram();
            if (isChangeable(id)) {
                copyFromBackup(fileProgram);
            } else {
                throw new Exception(String.format("Program %s has a new version %s",
                        appModel.getName(),
                        fileProgram == null ? "" : fileProgram.getVersion()));
            }
        }
        for (FileModel fileModel : appModel.getFiles().values()) {
            if (isFileNeedToUpdate(fileModel)) {
                if (isChangeable(id)) {
                    copyFromBackup(fileModel);
                } else {
                    throw new Exception("Program %s has a new config file");
                }
            }
        }
    }

    private boolean isFileNeedToUpdate(FileModel fileModel) {
        File file = fileModel.getLocalPath(appDir).toFile();
        return !file.exists() || !Util.md5File(file.getPath())
                .equals(fileModel.getMd5());
    }

    private void copyFromBackup(FileModel fileModel) {
        File file = fileModel.getLocalPath(appDir).toFile();
        File fileBackUp = fileModel.getLocalPath(backupDir).toFile();
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
