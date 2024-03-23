/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

import com.tec02.communication.Communicate.Impl.Cmd.Cmd;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class PropertiesModel {

    private static volatile PropertiesModel instance;
    private final Properties properties;
    private String serverIp;

    private PropertiesModel() throws Exception {
        properties = new Properties();
        init();
    }

    public final void init() throws Exception {
        properties.load(getClass().getResourceAsStream("/config.properties"));
        File config = new File("./config/ip.txt");
        if (!config.exists() || !(serverIp = Files.readString(config.toPath()))
                .matches("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}$")
                || !ping(serverIp)) {
            Properties ipConfig = new Properties();
            ipConfig.load(getClass().getResourceAsStream("/ip.properties"));
            String[] ips = ipConfig.getProperty(Keyword.Url.IPS).split(",");
            for (String ip : ips) {
                ip = ip.trim();
                if (ping(ip)) {
                    serverIp = ip;
                    break;
                }
            }
            if (!config.exists()) {
                config.getParentFile().mkdirs();
                config.createNewFile();
            }
            try ( FileWriter fileWriter = new FileWriter(config)) {
                fileWriter.write(serverIp);
            }
        }
    }

    private boolean ping(String ip) {
        Cmd cmd = new Cmd();
        cmd.sendCommand(String.format("ping %s -n 1", ip));
        String rp = cmd.readAll();
        return rp.contains("TTL=");
    }

    public String getServerIp() {
        return serverIp;
    }

    public static boolean getBoolean(String key, boolean defaultVal) {
        String str = getConfig(key);
        if (str == null) {
            return defaultVal;
        } else {
            return Boolean.parseBoolean(str);
        }
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
        if (str == null || !str.trim().matches("^-?[0-9]+$")) {
            return defaultValue;
        } else {
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

    public String getProperty(String key) {
        String str = properties.getProperty(key);
        if (str.contains("(ip)")) {
            str = str.replaceAll("\\(ip\\)", serverIp);
        }
        return str;
    }

    public String getProperty(String key, String defaultVal) {
        String str = properties.getProperty(key, defaultVal);
        if (str.contains("(ip)")) {
            str = str.replaceAll("\\(ip\\)", serverIp);
        }
        return str;
    }

}
