/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.model;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class PropertiesModel {

    private static volatile PropertiesModel instance;
    private final Properties properties;

    private PropertiesModel() throws IOException {
        properties = new Properties();
        init();
    }

    public final void init() throws IOException {
        properties.load(new FileReader("src\\main\\resources\\config.properties"));
    }

    public static PropertiesModel getInstance() throws IOException {
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
    
    public static String getConfig(String key) {
        try {
            return getInstance().getProperty(key);
        } catch (IOException ex) {
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
