/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.analysis;

import com.tec02.appStore.AppConsole;
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
    private final MessageManage messageManage;
    private boolean hasChange;

    public AppManagement(RestAPI api, StoreLoger loger) {
        this.appDir = PropertiesModel.getConfig(Keyword.Store.LOCAL_APP);
        this.backupDir = PropertiesModel.getConfig(Keyword.Store.LOCAL_BACKUP);
        Util.deleteFolder(new File(appDir));
        this.checkAppChanged = new Repository(loger);
        this.appProccesses = new HashMap<>();
        this.appRemoves = new HashMap<>();
        this.messageManage = new MessageManage();
        this.loger = loger;
        new Thread(() -> {
            while (true) {
                if (appRemoves.isEmpty()) {
                    return;
                }
                List<Object> idRemoves = new ArrayList<>();
                for (Object id : appRemoves.keySet()) {
                    stopAppIfAwalysUpdate(id);
                    var app = appRemoves.get(id);
                    if (isChangeable(id)) {
                        this.checkAppChanged.deleteApp(app, appDir);
                        if (app.isDeleteAll()) {
                            idRemoves.add(id);
                        }
                        hasChange = true;
                    } else {
                        messageManage.showAppConsole(id, app.getName(), "This program need to reset!");
                    }
                }
                for (Object id : idRemoves) {
                    if (this.appRemoves.containsKey(id)) {
                        this.appRemoves.remove(id);
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
            }
        }).start();
    }

    public void checkUpdate(Collection<AppModel> newApps) {
        Map<Object, AppRemove> appRemoveds = this.checkAppChanged.setData(newApps);
        Map<Object, AppModel> apps = this.checkAppChanged.getAppModels();
        for (Object id : appRemoveds.keySet()) {
            AppRemove appRemove = appRemoveds.get(id);
            if (this.appProccesses.containsKey(id)) {
                stopAppIfAwalysUpdate(id);
                if (isChangeable(id)) {
                    hasChange = true;
                    this.checkAppChanged.deleteApp(appRemove, appDir);
                } else {
                    this.appRemoves.put(id, appRemove);
                    this.appProccesses.get(id).setNeedToUpdate(true);
                }
            }
        }
        for (Object id : apps.keySet()) {
            AppModel appModel = apps.get(id);
            AppProccess proccess;
            if ((proccess = this.appProccesses.get(id)) == null) {
                hasChange = true;
                proccess = new AppProccess();
            }
            try {
                proccess.setRunFile(appModel);
                stopAppIfAwalysUpdate(id);
                if (checkAppUpdate(appModel, id)) {
                    hasChange = true;
                    proccess.setNeedToUpdate(false);
                }
            } catch (Exception ex) {
                proccess.setNeedToUpdate(true);
                messageManage.showAppConsole(id, appModel.getName(), ex.getMessage());
            }
            this.appProccesses.put(id, proccess);
        }
        this.appProccesses.keySet().retainAll(apps.keySet());
    }

    private void stopAppIfAwalysUpdate(Object id) {
        var appProcess = this.appProccesses.get(id);
        if (appProcess != null && appProcess.isAlwaysUpdate()) {
            appProcess.stop();
        }
    }

    private boolean isChangeable(Object id) {
        if (!appProccesses.containsKey(id)) {
            return true;
        }
        AppProccess appProccess = appProccesses.get(id);
        return !appProccess.isRuning();
    }

    public boolean isHasChange() {
        return hasChange;
    }

    public Map<Object, AppProccess> getAppProccesses() {
        hasChange = false;
        return appProccesses;
    }

    private boolean checkAppUpdate(AppModel appModel, Object id) throws Exception {
        boolean rs = false;
        if (isUpdateFileProgram(appModel, id)) {
            rs = true;
        }
        if (isUpdateFilesConfig(appModel, id)) {
            rs = true;
        }
        return rs;
    }

    private boolean isUpdateFilesConfig(AppModel appModel, Object id) throws Exception {
        boolean rs = false;
        for (FileModel fileModel : appModel.getFiles().values()) {
            if (isFileNeedToUpdate(fileModel)) {
                if (isChangeable(id)) {
                    copyFromBackup(fileModel);
                } else {
                    throw new Exception(String.format("Program %s has a new config file\r\nPlease! reset program",
                            appModel.getName()));
                }
                rs = true;
            }
        }
        return rs;
    }

    private boolean isUpdateFileProgram(AppModel appModel, Object id) throws Exception {
        if (isFileNeedToUpdate(appModel.getFileProgram())) {
            FileModel fileProgram = appModel.getFileProgram();
            if (isChangeable(id)) {
                copyFromBackup(fileProgram);
            } else {
                throw new Exception(String.format("Program %s has a new version %s\r\nPlease! reset program",
                        appModel.getName(),
                        fileProgram == null ? "" : fileProgram.getVersion()));
            }
            return true;
        }
        return false;
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

    class MessageManage {

        private final Map<Object, AppConsole> messConsoles = new HashMap<>();

        public void showAppConsole(Object id, String title, String mess) {
            if (!messConsoles.containsKey(id)) {
                messConsoles.put(id, new AppConsole());
            }
            AppConsole appConsole = messConsoles.get(id);
            appConsole.clear();
            appConsole.display(title);
            appConsole.set(mess);
        }
    }

}
