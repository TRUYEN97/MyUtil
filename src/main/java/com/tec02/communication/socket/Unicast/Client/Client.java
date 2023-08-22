/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.communication.socket.Unicast.Client;

import Unicast.commons.Interface.IIsConnect;
import java.net.Socket;
import Unicast.commons.Interface.Idisconnect;
import Unicast.commons.Keywords;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import Unicast.commons.Interface.IObjectClientReceiver;
import com.tec02.communication.socket.Unicast.commons.SocketLogger;

/**
 *
 * @author Administrator
 */
public class Client implements Runnable, Idisconnect, IIsConnect {

    private final String host;
    private final int port;
    private final IObjectClientReceiver clientReceiver;
    private final SocketLogger logger;
    private Socket socket;
    private PrintWriter outputStream;
    private BufferedReader inputStream;
    private boolean connect;
    private boolean debug;

    public Client(String host, int port, IObjectClientReceiver objectAnalysis) {
        this.host = host;
        this.port = port;
        this.logger = new SocketLogger("log/socket/client");
        this.clientReceiver = objectAnalysis;
        this.debug = false;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean connect() {
        try {
            this.socket = new Socket(host, port);
            this.outputStream = new PrintWriter(socket.getOutputStream(), true);
            this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.logger.addlog(Keywords.CLIENT, "Connect to host: %s, port: %s", host, port);
            this.connect = true;
            return true;
        } catch (Exception ex) {
            if (debug) {
                ex.printStackTrace();
                this.logger.addlog(Keywords.ERROR, ex.getLocalizedMessage());
            }
            return false;
        }
    }

    public boolean send(String data) {
        try {
            if (!isConnect()) {
                return false;
            }
            this.outputStream.println(data);
            this.logger.addlog(SocketLogger.pointToPoint(Keywords.CLIENT, host), data);
            return true;
        } catch (Exception e) {
            if (debug) {
                e.printStackTrace();
                this.logger.addlog(Keywords.ERROR, e.getLocalizedMessage());
            }
            return false;
        }
    }

    @Override
    public void run() {
        try {
            String data;
            while (isConnect() && (data = this.inputStream.readLine()) != null) {
                if (data.trim().isBlank()) {
                    continue;
                }
                this.logger.addlog(SocketLogger.pointToPoint(host, Keywords.CLIENT), data);
                this.clientReceiver.receiver(data);
            }
        } catch (Exception ex) {
            if (debug) {
                ex.printStackTrace();
                this.logger.addlog("ERROR", ex.getLocalizedMessage());
            }
        } finally {
            disconnect();
        }
    }

    @Override
    public boolean isConnect() {
        return this.socket != null && this.socket.isConnected() && connect;
    }

    @Override
    public boolean disconnect() {
        if(!isConnect()){
            return true;
        }
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }
            connect = false;
            this.logger.addlog(host, "disconnected!");
            return true;
        } catch (Exception e) {
            if (debug) {
                e.printStackTrace();
                this.logger.addlog("ERROR", e.getLocalizedMessage());
            }
            return false;
        }
    }

}
