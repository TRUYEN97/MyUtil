/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.Robot;

import com.sun.jna.platform.win32.WinDef;
import com.tec02.user32.User32;
import java.awt.event.KeyEvent;

/**
 *
 * @author Administrator
 */
public class MyControl {
    
    private final MyRobot myRobot;
    private final User32 user32;
    
    public MyControl() {
        this.myRobot = new MyRobot();
        this.user32 = User32.INSTANCE;
    }
    
    public boolean showWindows(WinDef.HWND hwnd) {
        if (hwnd == null) {
            return false;
        }
        return user32.ShowWindow(hwnd, 9)
                && user32.SetForegroundWindow(hwnd);
    }
    
    public boolean showAndMoveMouse(String title, int x, int y) {
        if (title == null || x < 0 || y < 0) {
            return false;
        }
        WinDef.HWND thisWindows = user32.FindWindow(0, title);
        if (!showWindows(thisWindows)) {
            return false;
        }
        moveMouseOnWindow(thisWindows, x, y);
        return true;
    }
    
    public void sendMessger(String mess) {
        this.myRobot.sendMessager(mess);
    }
    
    public boolean moveMouseOnWindow(WinDef.HWND hwnd, int x, int y) {
        int[] location = new int[4];
        if (!user32.GetWindowRect(hwnd, location)) {
            return false;
        }
        this.myRobot.moveMouse(location[0] + x, location[1] + y);
        return true;
    }
    
    public void clearAtTextMouse() {
        this.myRobot.deleteAtMouse();
    }
    
    public void enter() {
        this.myRobot.sendKey(KeyEvent.VK_ENTER);
    }

    public void click() {
        this.myRobot.click();
    }

    public boolean closeWindows(WinDef.HWND hwnd) {
        return user32.CloseWindow(hwnd);
    }
}
