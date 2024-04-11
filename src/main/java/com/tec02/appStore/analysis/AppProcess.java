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
import com.tec02.user32.User32Util;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
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
    private final HandleManagement handleManagement;
    private final AppConsole appConsole;
    private final Win32p win32p;
    private final TaskListp taskList;
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
        this.taskList = new TaskListp();
        if (this.taskList.taskListAvaiable()) {
            this.win32p = null;
        } else {
            this.win32p = new Win32p();
        }
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
        this.runFile = null;
        this.appConsole.clear();
        this.updateStatus = false;
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
        if (this.win32p == null) {
            return this.taskList.isRuning();
        } else {
            return this.win32p.isRuning();
        }
    }

    public boolean stop() {
        if (appModel == null) {
            return false;
        }
        if (this.win32p == null) {
            return this.taskList.stop();
        } else {
            return this.win32p.stop();
        }
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
            this.needReset = false;
            Cmd cmd = new Cmd();
            if (!cmd.insertCommand(String.format("cd \"%s\" && start \"%s\" /MIN %s \"%s\" \"%s\" \"%s\"",
                    runFile.getParent(), getWindowTitle(), commandRun,
                    fileName, version, this.appModel.getName()))) {
            }
            cmd.waitFor();
        });
        thread.start();
    }

    private String getWindowTitle() {
        return String.format("%s - runing", this.appModel.getName());
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

    class TaskListp {

        public TaskListp() {
        }

        private boolean taskListAvaiable() {
            if(!PropertiesModel.getBoolean("useTaskList", false)){
                return false;
            }
            Cmd cmd = new Cmd();
            if (cmd.sendCommand("tasklist.exe")) {
                return cmd.readAll().contains("Image Name");
            }
            return false;
        }

        private boolean stop() {
            if (killWithCmd(String.format("taskkill.exe /FI \"WINDOWTITLE eq %s\"",
                    getWindowTitle()))) {
                return true;
            }
            return runFile != null && killWithCmd(String.format("taskkill.exe /FI \"IMAGENAME eq %s\"",
                    runFile.getFileName()));
        }

        private boolean isRuning() {
            if (checkWithCmd(String.format("tasklist.exe /nh /FI \"WINDOWTITLE eq %s\"",
                    getWindowTitle()))) {
                return true;
            }
            return runFile != null && checkWithCmd(String.format("tasklist.exe /nh /FI \"IMAGENAME eq %s\"",
                    runFile.getFileName()));
        }

        private boolean checkWithCmd(String command) {
            Cmd cmdChecker = new Cmd();
            if (!cmdChecker.sendCommand(command)) {
                return false;
            }
            String response = cmdChecker.readAll();
            cmdChecker.waitFor();
            return response != null && !response.contains("INFO: No tasks are running which match the specified criteria.");
        }

        private boolean killWithCmd(String command) {
            Cmd cmdKiller = new Cmd();
            if (!cmdKiller.sendCommand(command)) {
                return false;
            }
            String response = cmdKiller.readAll();
            cmdKiller.waitFor();
            return response != null && response.contains("SUCCESS: Sent termination signal to the process with PID");
        }
    }

    class Win32p {

        private final User32Util user32Util;

        public Win32p() {
            this.user32Util = new User32Util();
        }

        private boolean killProcess() {
            List<Integer> pids = getPID();
            for (Integer pid : pids) {
                user32Util.killProcessByPID(pid);
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }
            return !isRuning();
        }

        private List<Integer> getPID() {
            List<Integer> pids = user32Util.findProcess(getWindowTitle());
            if (pids.isEmpty()) {
                pids = user32Util.findProcess(String.format("GDI+ Window (%s)", runFile.getFileName()));
            }
            return pids;
        }

        private boolean isRuning() {
            return !getPID().isEmpty();
        }

        private boolean stop() {
            return killProcess() || killProcess();
        }
    }

}
