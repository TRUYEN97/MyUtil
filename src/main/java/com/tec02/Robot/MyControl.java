/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.Robot;

import Robot.W32API;
import java.awt.event.KeyEvent;

/**
 *
 * @author Administrator
 */
public class MyControl {
    
    private final MyRobot myRobot;
    
    public MyControl() {
        this.myRobot = new MyRobot();
    }
    
    public W32API.HWND findWindows(String Tile) {
        return User32.INSTANCE.FindWindow(0, Tile);
    }
    
    public boolean showWindows(W32API.HWND hwnd) {
        if (hwnd == null) {
            return false;
        }
        return User32.INSTANCE.ShowWindow(hwnd, 9)
                && User32.INSTANCE.SetForegroundWindow(hwnd);
    }
    
    public boolean showAndMoveMouse(String title, int x, int y) {
        if (title == null || x < 0 || y < 0) {
            return false;
        }
        User32.HWND thisWindows = findWindows(title);
        if (!showWindows(thisWindows)) {
            return false;
        }
        moveMouseOnWindow(thisWindows, x, y);
        return true;
    }
    
    public void sendMessger(String mess) {
        this.myRobot.sendMessager(mess);
    }
    
    public boolean moveMouseOnWindow(W32API.HWND hwnd, int x, int y) {
        int[] location = new int[4];
        if (!User32.INSTANCE.GetWindowRect(hwnd, location)) {
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

    public boolean closeWindows(W32API.HWND hwnd) {
        return User32.INSTANCE.CloseWindow(hwnd);
    }
}
