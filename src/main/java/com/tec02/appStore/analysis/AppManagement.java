/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.analysis;

import com.tec02.appStore.StoreLoger;
import com.tec02.appStore.model.AppModel;
import com.tec02.appStore.model.AppUpdateModel;
import com.tec02.appStore.model.FileModel;
import com.tec02.common.Keyword;
import com.tec02.API.RestAPI;
import com.tec02.common.JOptionUtil;
import com.tec02.common.Util;
import com.tec02.common.PropertiesModel;
import com.tec02.gui.frameGui.AbsDisplayAble;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class AppManagement {

    private final Repository checkAppChanged;
    private final String appDir;
    private final StoreLoger loger;
    private final String backupDir;
    private final List<AppProcess> appRemoves;
    private final AppProccessManagemant proccessManagemant;
    private final Thread threadRemove;

    public AppManagement(RestAPI api, StoreLoger loger, AbsDisplayAble view) throws Exception {
        this.appDir = PropertiesModel.getConfig(Keyword.Store.LOCAL_APP);
        this.backupDir = PropertiesModel.getConfig(Keyword.Store.LOCAL_BACKUP);
        this.proccessManagemant = new AppProccessManagemant(view);
        this.checkAppChanged = new Repository(loger);
        this.appRemoves = new ArrayList<>();
        this.loger = loger;
        this.threadRemove = new Thread(() -> {
            while (true) {
                try {
                    if (!appRemoves.isEmpty()) {
                        for (int i = 0; i < appRemoves.size(); i++) {
                            AppProcess app = appRemoves.get(i);
                            if (isUpdateable(app)) {
                                this.checkAppChanged.clear(app.getLocalPath(appDir));
                                this.proccessManagemant.remove(app.getId());
                                this.appRemoves.remove(app);
                            } else {
                                app.setCommandRemoveOrShowMessage();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionUtil.showMessage(e.getMessage());
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
            }
        });
    }

    public void init() {
        if (!threadRemove.isAlive()) {
            threadRemove.start();
        }
    }

    public synchronized void checkUpdate(Collection<? extends AppModel> newApps) {
        List<AppModel> appRemoveds = this.checkAppChanged.setData(newApps);
        for (AppModel appRemove : appRemoveds) {
            Object id = appRemove.getId();
            if (this.proccessManagemant.containsKey(id)) {
                var appProcess = this.proccessManagemant.get(id);
                this.appRemoves.add(appProcess);
                appProcess.setWaitRemovedStatus(true);
            }
        }
        Map<Object, AppUpdateModel> apps = this.checkAppChanged.getAppModels();
        for (Object id : apps.keySet()) {
            AppUpdateModel appModel = apps.get(id);
            AppProcess appProcess = this.proccessManagemant.create(appModel);
            if (this.appRemoves.contains(appProcess)) {
                this.appRemoves.remove(appProcess);
                appProcess.setWaitRemovedStatus(false);
            }
            update(appProcess);
        }
    }

    public AppProccessManagemant getAppProccesses() {
        return proccessManagemant;
    }

    private void update(AppProcess proccess) {
        AppUpdateModel model = proccess.getAppModel();
        var removeFiles = model.getRemoveFiles();
        if (removeFiles != null && !removeFiles.isEmpty()) {
            proccess.setUpdateStatus(true);
            proccess.setNeedReset(true);
            if (isUpdateable(proccess)) {
                if (this.checkAppChanged.deleteAppFiles(model, appDir)) {
                    proccess.setUpdateStatus(false);
                }
            } else {
                proccess.sendCommandUpdateOrShowMessage();
                return;
            }
        }
        if (isFileNeedToUpdate(model.getFileProgram())) {
            proccess.setUpdateStatus(true);
            proccess.setNeedReset(true);
            if (isUpdateable(proccess)) {
                if (copyFromBackup(model.getFileProgram())) {
                    proccess.setUpdateStatus(false);
                }
            } else {
                proccess.sendCommandUpdateOrShowMessage();
                return;
            }
        }
        Collection<FileModel> fileModels = model.getFiles().values();
        if (!fileModels.isEmpty()) {
            for (FileModel fileModel : fileModels) {
                if (isFileNeedToUpdate(fileModel)) {
                    proccess.setUpdateStatus(true);
                    proccess.setNeedReset(true);
                    if (isUpdateable(proccess)) {
                        if (copyFromBackup(fileModel)) {
                            proccess.setUpdateStatus(false);
                        } else {
                            return;
                        }
                    } else {
                        proccess.sendCommandUpdateOrShowMessage();
                        return;
                    }
                }
            }
        }
    }

    private boolean isFileNeedToUpdate(FileModel fileModel) {
        if (fileModel == null) {
            return false;
        }
        File file = fileModel.getLocalPath(appDir).toFile();
        return !file.exists() || !Util.md5File(file.getPath())
                .equals(fileModel.getMd5());
    }

    private boolean copyFromBackup(FileModel fileModel) {
        if (fileModel == null) {
            return true;
        }
        File file = fileModel.getLocalPath(appDir).toFile();
        File fileBackUp = fileModel.getLocalPath(backupDir).toFile();
        try {
            Util.copyFile(fileBackUp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            this.loger.addLog("BACKUP", "%s -> %s, ok", fileBackUp.getPath(), file.getPath());
            return true;
        } catch (IOException ex) {
            this.loger.addLog("BACKUP", "%s -> %s, Failed - %s",
                    fileBackUp.getPath(),
                    file.getPath(),
                    ex.getMessage());
            return false;
        }
    }

    private boolean isUpdateable(AppProcess proccess) {
        AppUpdateModel model = proccess.getAppModel();
        return (model.isAlwaysUpdate() && proccess.stop()) || !proccess.isRuning();
    }

}
