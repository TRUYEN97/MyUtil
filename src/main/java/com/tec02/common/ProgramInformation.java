/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

import com.tec02.communication.Communicate.Impl.Cmd.Cmd;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class ProgramInformation {

    private static volatile ProgramInformation instance;
    private final List<Inet4Address> ipV4s;
    private final List<Inet6Address> ipV6s;
    private String pc_name;

    private ProgramInformation() {
        this.ipV4s = new ArrayList<>();
        this.ipV6s = new ArrayList<>();
        scanHostIp();
        this.pc_name = getComputerName();
    }

    public static ProgramInformation getInstance() {
        ProgramInformation ins = ProgramInformation.instance;
        if (ins == null) {
            synchronized (ProgramInformation.class) {
                ins = ProgramInformation.instance;
                if (ins == null) {
                    ProgramInformation.instance = ins = new ProgramInformation();
                }
            }
        }
        return ins;
    }

    public String getPcName() {
        return pc_name;
    }

    public String getIpV4() {
        for (Inet4Address ipV4 : ipV4s) {
            String ip = ipV4.getHostAddress();
            if (ip != null && ip.startsWith("10.")) {
                return ip;
            }
        }
        return "";
    }

    private void scanHostIp() {
        try {
            for (Enumeration<NetworkInterface> eni = NetworkInterface.getNetworkInterfaces(); eni.hasMoreElements();) {
                final NetworkInterface ifc = eni.nextElement();
                if (ifc.isUp()) {
                    for (Enumeration<InetAddress> ena = ifc.getInetAddresses(); ena.hasMoreElements();) {
                        InetAddress address = ena.nextElement();
                        if (address instanceof Inet4Address ipv4) {
                            this.ipV4s.add(ipv4);
                        } else if (address instanceof Inet6Address ipv6) {
                            this.ipV6s.add(ipv6);
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    private String getComputerName() {
        String os = getSystemName();
        if (os.startsWith("Windows")) {
            return getWindowsHostName();
        } else if (os.startsWith("Mac")) {
            return getMacHostName();
        }
        return "";
    }

    private String getWindowsHostName() {
        try {
            if (InetAddress.getLocalHost() == null) {
                return "";
            }
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getMacHostName() {
        Cmd cmd1 = new Cmd();
        cmd1.sendCommand("networksetup -getcomputername");
        return cmd1.readAll();
    }

    private String getSystemName() {
        Properties sysProperty = System.getProperties();
        return sysProperty.getProperty("os.name");
    }

}
