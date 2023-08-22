/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.communication.socket.Unicast.commons;

import com.tec02.Time.TimeBase;
import com.tec02.myloger.MyLoger;
import java.io.File;

/**
 *
 * @author Administrator
 */
public class SocketLogger {

    private final MyLoger loger;
    private final TimeBase timeBase;
    private final String path;

    public SocketLogger(String path) {
        this.loger = new MyLoger();
        this.loger.setSaveMemory(true);
        this.timeBase = new TimeBase();
        this.path = path;
    }
    
    public static String pointToPoint(String from, String to){
        return String.format("%s -> %s", from,to);
    }

    public synchronized boolean addlog(String key, String str, Object... params){
        String logPath = String.format("%s/%s.txt", this.path, this.timeBase.getDate());
        this.loger.setFile(new File(logPath));
        try {
            this.loger.addLog(key, str, params);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
