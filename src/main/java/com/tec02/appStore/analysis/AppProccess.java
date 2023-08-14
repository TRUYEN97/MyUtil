/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.analysis;

import com.tec02.appStore.model.AppModel;
import com.tec02.appStore.model.FileModel;
import com.tec02.common.JOptionUtil;
import com.tec02.common.Keyword;
import com.tec02.communication.Communicate.Impl.Cmd.Cmd;
import com.tec02.common.PropertiesModel;
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
public class AppProccess {

    private final String dir;
    private final Cmd cmd;
    private final Timer timer;
    private Thread thread;
    private AppModel appModel;
    private Path runFile;
    private boolean needUpdate;

    public AppProccess() {
        this.dir = PropertiesModel.getConfig(Keyword.Store.LOCAL_APP);
        this.cmd = new Cmd();
        this.timer = new Timer(5000, (e) -> {
            if (this.appModel.isAlwaysRun()) {
                runApp();
            } else {
                AppProccess.this.timer.stop();
            }
        });
    }

    public AppModel getAppModel() {
        return appModel;
    }

    public Object getId() {
        if (this.appModel == null) {
            return null;
        }
        return this.appModel.getId();
    }

    public void setRunFile(AppModel appModel) {
        this.appModel = appModel;
        this.needUpdate = false;
        this.runFile = this.appModel.getFileProgram().getLocalPath(dir);
        if (this.appModel.isAlwaysRun()) {
            this.timer.start();
        } else {
            this.timer.stop();
        }
    }

    public boolean isRuning() {
        if (appModel != null && thread != null && thread.isAlive()) {
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
        if (appModel == null || isRuning() || needUpdate) {
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
                commandRun = String.format("cd %s && start \"%s\" /MIN java -jar %s %s %s",
                        runFile.getParent(), this.appModel.getName(),
                        fileName, version, this.appModel.getName());
            } else {
                commandRun = String.format("cd %s && start \"%s\" /MIN %s %s %s",
                        runFile.getParent(), this.appModel.getName(),
                        fileName, version, this.appModel.getName());
            }
            if (!cmd.insertCommand(commandRun)) {
                return;
            }
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

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedToUpdate(boolean isUpdate) {
        this.needUpdate = isUpdate;
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

}
