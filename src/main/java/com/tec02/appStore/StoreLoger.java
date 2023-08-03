/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore;

import Time.TimeBase;
import com.tec02.common.Keyword;
import com.tec02.common.PropertiesModel;
import com.tec02.myloger.MyLoger;

/**
 *
 * @author Administrator
 */
public class StoreLoger {
    
    private final MyLoger loger;
    private final String dirLog;
    private final TimeBase timeBase;

    public StoreLoger() {
        this.loger = new MyLoger();
        this.loger.setSaveMemory(true);
        this.dirLog = PropertiesModel.getConfig(Keyword.Store.LOG_DIR);
        this.timeBase = new TimeBase();
    }

    public void addLog(String key, String format, Object... params) {
        loger.setFile(String.format("%s/%s.txt", dirLog, timeBase.getDate()));
        loger.addLog(key, format, params);
    }
    
}
