/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.user32;

/**
 *
 * @author Administrator
 */
public class Win32ProcessInfo {

    private final int processId;
    private final String windowTitle;

    public Win32ProcessInfo(int processId, String windowTitle) {
        this.processId = processId;
        this.windowTitle = windowTitle;
    }

    public int getProcessId() {
        return processId;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

}
