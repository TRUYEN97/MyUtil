/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.Robot;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 *
 * @author Administrator
 */
public class MyRobot {

    private Robot robot;

    public MyRobot() {
        try {
            this.robot = new Robot();
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    public boolean sendMessager(String mess) {
        try {
            for (char key : mess.toUpperCase().toCharArray()) {
                sendKey(key);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void moveMouse(int x, int y) {
        this.robot.mouseMove(x, y);
    }

    public void deleteAtMouse() {
        click();
        click();
        sendKey(KeyEvent.VK_DELETE);
    }

    public void sendKey(int kiTu) {
        this.robot.keyPress(kiTu);
        this.robot.delay(10);
        this.robot.keyRelease(kiTu);
        this.robot.delay(10);
    }

    public void click() {
        this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        this.robot.delay(200);
        this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        this.robot.delay(20);
    }
}
