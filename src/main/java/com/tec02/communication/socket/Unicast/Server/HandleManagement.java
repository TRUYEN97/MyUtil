/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.communication.socket.Unicast.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class HandleManagement {

    private final HashMap<String, ClientHandler> handlers;

    public HandleManagement() {
        this.handlers = new HashMap<>();
    }

    public void add(String name, ClientHandler handler) {
       var old = this.handlers.put(name, handler);
       if(old != null){
           old.disconnect();
       }
    }

    public void disconnect(String name) {
        this.handlers.remove(name);
    }
    
    public void setDebug(boolean debug){
        for (ClientHandler handler : this.handlers.values()) {
            handler.setDebug(debug);
        }
    }
    
    public ClientHandler getClientHandler(String name){
        return this.handlers.get(name);
    }

    public void disconnect(ClientHandler handler) {
        if (handler == null) {
            return;
        }
        List<String> keyRemoved = new ArrayList<>();
        for (String key : this.handlers.keySet()) {
            if (this.handlers.get(key).equals(handler)) {
                keyRemoved.add(key);
            }
        }
        for (String key : keyRemoved) {
            this.handlers.remove(key);
        }
    }

    public int size() {
        return this.handlers.size();
    }
}
