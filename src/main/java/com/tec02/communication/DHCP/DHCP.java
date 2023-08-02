/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.communication.DHCP;

import Time.TimeBase;
import com.tec02.myloger.MyLoger;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import org.dhcp4java.DHCPBadPacketException;
import org.dhcp4java.DHCPConstants;
import static org.dhcp4java.DHCPConstants.DHO_DHCP_LEASE_TIME;
import static org.dhcp4java.DHCPConstants.DHO_DHCP_SERVER_IDENTIFIER;
import static org.dhcp4java.DHCPConstants.DHO_ROUTERS;
import static org.dhcp4java.DHCPConstants.DHO_SUBNET_MASK;
import org.dhcp4java.DHCPOption;
import org.dhcp4java.DHCPPacket;
import org.dhcp4java.DHCPResponseFactory;

/**
 *
 * @author Administrator
 */
public class DHCP implements Runnable {

    private static volatile DHCP instance;
    private final MyLoger loger;
    private final MyLoger macRequestLog;
    private final DhcpData dhcpData;
    private String dhcpHost;
    private File logdir;
    private JTextArea view;
    private InetAddress host_Address;
    private DHCPOption[] commonOptions;
    private int leaseTime = 3600 * 24 * 7;
    private Thread thread;
    private final Arp arp;
    private final TimeBase timeBase;

    private DHCP() {
        this.loger = new MyLoger();
        this.macRequestLog = new MyLoger();
        this.dhcpData = DhcpData.getInstance();
        this.arp = new Arp();
        this.timeBase = new TimeBase();
    }

    public static DHCP getgetInstance() {
        DHCP ins = DHCP.instance;
        if (ins == null) {
            synchronized (DHCP.class) {
                ins = DHCP.instance;
                if (ins == null) {
                    DHCP.instance = ins = new DHCP();
                }
            }
        }
        return ins;
    }

    public void setView(JTextArea view) {
        this.view = view;
        showInfo();
    }

    private void showInfo() {
        if (this.view != null && this.dhcpHost != null) {
            String mess = String.format("////DHCP//////\r\nSet net IP: %s\r\nSet MAC Length: %s\r\nLease time: %s",
                    this.dhcpHost,
                    this.dhcpData.getMACLength(),
                    this.leaseTime);
            this.view.setText(mess);
        }
    }

    public boolean setNetIP(String netIp) {
        if (!MacUtil.isIPAddr(netIp)) {
            JOptionPane.showMessageDialog(null, "Net IP is null or it's not addr! \r\n" + netIp);
            System.exit(0);
        }
        this.dhcpHost = netIp;
        if (this.dhcpData.setNetIP(netIp)) {
            showInfo();
            return true;
        }
        return false;
    }

    public boolean init(File logPath, int leaseTime) {
        if (leaseTime <= 0) {
            return false;
        }
        this.leaseTime = leaseTime;
        DHCPPacket temp = new DHCPPacket();
        this.logdir = logPath;
        this.loger.setSaveMemory(true);
        this.macRequestLog.setSaveMemory(true);
        this.macRequestLog.clear();
        if (isNotHostAddress(this.dhcpHost)) {
            String mess = "The network card cannot be found to \"" + this.dhcpHost + "\"";
            JOptionPane.showMessageDialog(null, mess, "Tip",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            temp.setOptionAsInetAddress(DHO_DHCP_SERVER_IDENTIFIER, this.host_Address);
            temp.setOptionAsInt(DHO_DHCP_LEASE_TIME, leaseTime);
            temp.setOptionAsInetAddress(DHO_SUBNET_MASK, "255.255.255.0");
            temp.setOptionAsInetAddress(DHO_ROUTERS, "0.0.0.0");
            this.commonOptions = temp.getOptionsArray();
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean init(File logPath) {
        return init(logPath, leaseTime);
    }

    private boolean isNotHostAddress(String dhcpHost) {
        String HostName;
        try {
            HostName = InetAddress.getLocalHost().getHostAddress();
            if (HostName.isBlank()) {
                return false;
            }
            InetAddress[] addrs = InetAddress.getAllByName(dhcpHost);
            String temp;
            for (InetAddress addr : addrs) {
                temp = addr.getHostAddress();
                if (temp.equals(this.dhcpHost)) {
                    this.host_Address = addr;
                    return false;
                }
            }
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void run() {
        try ( DatagramSocket socket = new DatagramSocket(DHCPConstants.BOOTP_REQUEST_PORT, host_Address)) {
            DatagramPacket pac = new DatagramPacket(new byte[1500], 1500);
            DHCPPacket dhcp;
            while (true) {
                socket.receive(pac);
                dhcp = DHCPPacket.getPacket(pac);
                String mac = bytesToHex(dhcp.getChaddr()).substring(0, 12);
                String ip = dhcpData.getIP(mac);
                String macRequest = String.format("DHCP requests: %s - %s", mac, ip);
                System.out.println(macRequest);
                this.macRequestLog.setFile(new File(String.format("%s/MacRequest/%s.txt",
                        logdir.getPath(), this.timeBase.getDate())));
                this.macRequestLog.addLog(macRequest);
                showInfo(mac, ip);
                if (ip == null) {
                    continue;
                }
                DHCPPacket d;
                DatagramPacket dp;
                InetAddress address = InetAddress.getByName(ip);
                String logPath = String.format("%s/MACIP/%s.txt", logdir.getPath(), this.timeBase.getDate());
                loger.setFile(new File(logPath));
                switch (dhcp.getDHCPMessageType()) {
                    case DHCPConstants.DHCPDISCOVER -> {
                        d = DHCPResponseFactory.makeDHCPOffer(dhcp, address, leaseTime, host_Address, "", commonOptions);
                        byte[] res = d.serialize();
                        dp = new DatagramPacket(res, res.length, InetAddress.getByName("255.255.255.255"), DHCPConstants.BOOTP_REPLY_PORT);
                        socket.send(dp);
                        loger.addLog("==============================================");
                        loger.addLog(host_Address.getHostAddress());
                        loger.addLog("DISCOVER", "DHCP PORT: " + dp.getPort());
                        loger.addLog("DISCOVER", "DHCP ADDRESS: " + dp.getAddress().toString());
                        loger.addLog("DISCOVER", "DHCP SOCK ADDRESS: " + dp.getSocketAddress().toString());
                    }
                    case DHCPConstants.DHCPREQUEST -> {
                        d = DHCPResponseFactory.makeDHCPAck(dhcp, address, leaseTime, host_Address, "", commonOptions);
                        byte[] res = d.serialize();
                        dp = new DatagramPacket(res, res.length, InetAddress.getByName("255.255.255.255"), DHCPConstants.BOOTP_REPLY_PORT);
                        socket.send(dp);
                        loger.addLog("REQUEST", ip + " - " + mac);
                        loger.addLog("REQUEST", "dhcp request ok");
                        loger.addLog("*******************************************");
                        sendARP();
                    }
                }
            }
        } catch (IOException | DHCPBadPacketException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "Tip", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    private void showInfo(String mac, String ip) {
        showMess(String.format("DHCP: %s\r\nIP: %s\r\nMAC length: %s\r\nLease time: %s",
                mac, ip, this.dhcpData.getMACLength(), leaseTime));
    }

    private void showMess(String mess) {
        if (view == null) {
            return;
        }
        view.setText(mess);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public boolean setMacLenth(int macLength) {
        if (this.dhcpData.setMacLength(macLength)) {
            showInfo();
            return true;
        }
        return false;
    }

    private void sendARP() {
        if (this.thread == null || !this.thread.isAlive()) {
            this.thread = new Thread(this.arp);
            this.thread.start();
        }
    }
}
