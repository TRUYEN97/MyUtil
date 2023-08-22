/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.analysis;

import com.tec02.appStore.AppConsole;
import com.tec02.appStore.model.AppUpdateModel;
import com.tec02.appStore.model.FileModel;
import com.tec02.common.JOptionUtil;
import com.tec02.common.Keyword;
import com.tec02.communication.Communicate.Impl.Cmd.Cmd;
import com.tec02.common.PropertiesModel;
import com.tec02.communication.socket.Unicast.Server.HandleManagement;
import java.io.File;
import java.nio.file.Path;
import javax.swing.Icon;
import javax.swing.JPasswordField;
import javax.swing.Timer;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Administrator
 */
public class AppProcess {

    private final String dir;
    private final Cmd cmd;
    private final HandleManagement handleManagement;
    private final AppConsole appConsole;
    private Thread thread;
    private AppUpdateModel appModel;
    private Path runFile;
    private boolean updateStatus;
    private boolean removedStatus;
    private boolean needReset;

    public AppProcess(HandleManagement handleManagement) {
        this.handleManagement = handleManagement;
        this.appConsole = new AppConsole();
        this.dir = PropertiesModel.getConfig(Keyword.Store.LOCAL_APP);
        this.cmd = new Cmd();
        new Timer(5000, (e) -> {
            if (this.appModel == null) {
                return;
            }
            if (this.appModel.isAlwaysRun() || needReset) {
                runApp();
            }
        }).start();
    }

    public void setNeedReset(boolean needReset) {
        if (isRuning()) {
            this.needReset = needReset;
        }
    }

    public void sendCommandUpdateOrShowMessage() {
        if (isRuning()) {
            if (!sendCommandToApp("update")) {
                this.appConsole.clear();
                if (!this.appConsole.isVisible()) {
                    this.appConsole.display(String.format("%s - Warning", getAppName()));
                }
                this.appConsole.set(String.format("Program %s need to update\r\nPlease! reset program",
                        getAppName()));
            }
        }
    }

    public void setCommandRemoveOrShowMessage() {
        if (isRuning()) {
            if (!sendCommandToApp("quit")) {
                this.appConsole.clear();
                if (!this.appConsole.isVisible()) {
                    this.appConsole.display(String.format("%s - Warning", getAppName()));
                }
                this.appConsole.set(String.format("Program %s need to close\r\nPlease! Close program",
                        getAppName()));
            }
        }
    }

    public void setWaitRemovedStatus(boolean removed) {
        if (removed) {
            setUpdateStatus(false);
        } else if (this.appConsole.isVisible()) {
            this.appConsole.dispose();
        }
        this.removedStatus = removed;
    }

    public void setUpdateStatus(boolean isUpdate) {
        if (isUpdate) {
            setWaitRemovedStatus(false);
        } else if (this.appConsole.isVisible()) {
            this.appConsole.dispose();
        }
        this.updateStatus = isUpdate;

    }

    public AppUpdateModel getAppModel() {
        return appModel;
    }

    public Object getId() {
        if (this.appModel == null) {
            return null;
        }
        return this.appModel.getId();
    }

    public void clear() {
        this.appModel = null;
        this.updateStatus = false;
        this.runFile = null;
    }

    public void setRunFile(AppUpdateModel appModel) {
        this.appModel = appModel;
        this.runFile = this.appModel.getFileProgram().getLocalPath(dir);
    }

    public boolean isRuning() {
        if (appModel == null) {
            return false;
        } else if (thread != null && thread.isAlive()) {
            return true;
        }
        if (checkWithCmd(String.format("tasklist.exe /nh /FI \"WINDOWTITLE eq %s\"",
                this.appModel.getName()))) {
            return true;
        }
        return this.runFile != null && checkWithCmd(String.format("tasklist.exe /nh /FI \"IMAGENAME eq %s\"",
                this.runFile.getFileName()));
    }

    public boolean stop() {
        if (appModel == null) {
            return false;
        }
        if (killWithCmd(String.format("taskkill.exe /FI \"WINDOWTITLE eq %s\"",
                this.appModel.getName()))) {
            return true;
        }
        return this.runFile != null && killWithCmd(String.format("taskkill.exe /FI \"IMAGENAME eq %s\"",
                this.runFile.getFileName()));
    }

    public synchronized void runApp() {
        if (appModel == null || !isReadyRun()) {
            return;
        }
        this.thread = new Thread(() -> {
            String password = getPassword();
            if (password != null && !password.isBlank()) {
                JPasswordField passwordField = new JPasswordField();
                JOptionUtil.showObject(passwordField, "Password");
                String pass = new String(passwordField.getPassword());
                if (!pass.equals(password)) {
                    return;
                }
            }
            String fileName = runFile.getFileName().toString().toLowerCase();
            String commandRun;
            String version = appModel.getFileProgram().getVersion();
            if (fileName.endsWith(".jar")) {
                commandRun = "java -jar";
            } else if (fileName.endsWith(".py")) {
                commandRun = "python";
            } else {
                commandRun = "";
            }
            if (!cmd.insertCommand(String.format("cd %s && start \"%s\" /MIN %s %s %s %s",
                    runFile.getParent(), this.appModel.getName(), commandRun,
                    fileName, version, this.appModel.getName()))) {
                return;
            }
            this.needReset = false;
            cmd.readAll();
        });
        thread.start();
    }

    public String getDescription() {
        if (this.appModel == null) {
            return null;
        }
        FileModel fileProgarm = appModel.getFileProgram();
        return String.format("Name: %s\r\nVersion: %s\r\nDetail: %s\r\n- %s",
                appModel.getName(),
                fileProgarm == null ? "" : fileProgarm.getVersion(),
                appModel.getDescription(),
                fileProgarm == null ? "" : fileProgarm.getDescription()
        );
    }

    public String getAppName() {
        if (this.appModel == null) {
            return null;
        }
        return this.appModel.getName();
    }

    public Icon getIcon(int w, int h) {
        if (this.appModel == null) {
            return null;
        }
        return FileSystemView
                .getFileSystemView()
                .getSystemIcon(runFile.toFile(), w, h);
    }

    public String getPassword() {
        if (this.appModel == null) {
            return null;
        }
        return this.appModel.getPassword();
    }

    public File getFileLocation() {
        if (this.appModel == null) {
            return null;
        }
        return runFile.toFile();
    }

    public boolean isUpdateStatus() {
        return updateStatus;
    }

    public boolean isWaitRemove() {
        return removedStatus;
    }

    private boolean sendCommandToApp(String mess) {
        var handler = this.handleManagement.getClientHandler(getAppName());
        if (handler != null) {
            return handler.send(mess);
        }
        return false;
    }

    private boolean checkWithCmd(String command) {
        Cmd cmdChecker = new Cmd();
        if (!cmdChecker.sendCommand(command)) {
            return false;
        }
        String response = cmdChecker.readAll();
        return response != null && !response.contains("INFO: No tasks are running which match the specified criteria.");
    }

    private boolean killWithCmd(String command) {
        Cmd cmdKiller = new Cmd();
        if (!cmdKiller.sendCommand(command)) {
            return false;
        }
        String response = cmdKiller.readAll();
        return response != null && response.contains("SUCCESS: Sent termination signal to the process with PID");
    }

    public boolean isAlwaysUpdate() {
        return this.appModel != null && this.appModel.isAlwaysUpdate();
    }

    public File getLocalPath(String appDir) {
        if (appModel != null) {
            return appModel.getLocalPath(appDir).toFile();
        }
        return null;
    }

    private boolean isReadyRun() {
        return !(isRuning() || isUpdateStatus() || isWaitRemove());
    }

}
