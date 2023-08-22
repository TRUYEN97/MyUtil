/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.communication.socket.Unicast.Server;

import Unicast.commons.Interface.IIsConnect;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import Unicast.commons.Interface.Idisconnect;
import Unicast.commons.Keywords;
import com.tec02.communication.socket.Unicast.commons.Interface.IFilter;
import com.tec02.communication.socket.Unicast.commons.Interface.IObjectServerReceiver;
import com.tec02.communication.socket.Unicast.commons.SocketLogger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *
 * @author Administrator
 */
public class ClientHandler implements Runnable, Idisconnect, IIsConnect {

    private final Socket socket;
    private final PrintWriter outputStream;
    private final BufferedReader inputStream;
    private final HandleManagement handlerManager;
    private final SocketLogger logger;
    private IObjectServerReceiver objectAnalysis;
    private IFilter filter;
    private boolean connect;
    private boolean debug;
    private static long number = 0;
    private String name;

    public ClientHandler(Socket socket, SocketLogger logger, HandleManagement handlerManager) throws IOException {
        this.socket = socket;
        this.logger = logger;
        this.outputStream = new PrintWriter(socket.getOutputStream(), true);
        this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.connect = true;
        this.handlerManager = handlerManager;
        this.name = String.format("Client-%s", ClientHandler.number++);
        this.logger.addlog(Keywords.SERVER, "%s connected! - ip: %s", name, this.socket.getLocalSocketAddress());
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getName() {
        return name;
    }

    public void setObjectAnalysis(IObjectServerReceiver objectAnalysis) {
        if (objectAnalysis == null) {
            return;
        }
        this.objectAnalysis = objectAnalysis;
    }

    public String getHostAddress() {
        InetAddress address;
        address = socket.getInetAddress();
        if (address == null) {
            return null;
        }
        return address.getHostAddress();
    }

    public int getPort() {
        return this.socket.getPort();
    }

    @Override
    public boolean isConnect() {
        return this.socket != null && this.socket.isConnected() && connect;
    }

    public void setFilter(IFilter filter) {
        this.filter = filter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            if (this.filter == null || (name = this.filter.filter(this)) != null) {
                this.handlerManager.add(name, this);
                String data;
                while ((data = readLine()) != null) {
                    if (data.trim().isBlank()) {
                        continue;
                    }
                    this.logger.addlog(SocketLogger.pointToPoint(name, Keywords.SERVER), data);
                    this.objectAnalysis.receiver(this, data);
                }
            }
        } catch (IOException e) {
            if (debug) {
                e.printStackTrace();
            }
            this.logger.addlog("ERROR", e.getLocalizedMessage());
        } finally {
            disconnect();
        }
    }

    public String readLine() throws IOException {
        return this.inputStream.readLine();
    }

    @Override
    public boolean disconnect() {
        try (socket; outputStream; inputStream) {
            this.handlerManager.disconnect(this);
            this.connect = false;
            this.logger.addlog(Keywords.SERVER, "%s disconnected! - ip: %s", this.name, socket.getLocalSocketAddress());
            return true;
        } catch (Exception e) {
            if (debug) {
                e.printStackTrace();
                this.logger.addlog("ERROR", e.getLocalizedMessage());
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
            this.logger.addlog(SocketLogger.pointToPoint(Keywords.SERVER, name), data);
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
