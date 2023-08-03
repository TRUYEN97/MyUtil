/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class PropertiesModel {

    private static volatile PropertiesModel instance;
    private final Properties properties;

    private PropertiesModel() throws Exception {
        properties = new Properties();
        init();
    }

    public final void init() throws Exception {
        properties.load(getClass().getResourceAsStream("/config.properties"));
    }

    public static PropertiesModel getInstance() throws Exception {
        PropertiesModel ins = PropertiesModel.instance;
        if (ins == null) {
            synchronized (PropertiesModel.class) {
                ins = PropertiesModel.instance;
                if (ins == null) {
                    PropertiesModel.instance = ins = new PropertiesModel();
                }
            }
        }
        return ins;
    }
    
    public static int getInteger(String key, int defaultValue) {
        String str = getConfig(key);
        if(str == null || !str.trim().matches("^-?[0-9]+$")){
            return defaultValue;
        }else{
            return Integer.valueOf(str);
        }
    }
    
    public static String getConfig(String key) {
        try {
            return getInstance().getProperty(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static String getConfig(String key, String defaultValue) {
        try {
            return getInstance().getProperty(key, defaultValue);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Properties getProperties() {
        return properties;
    }
    
    public String getProperty(String key){
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultVal){
        return properties.getProperty(key, defaultVal);
    }

}
