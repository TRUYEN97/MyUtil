/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.communication.Communicate;

import Time.WaitTime.AbsTime;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author Administrator
 */
public abstract class AbsCommunicate extends AbsShowException implements ISender, IReadStream, Closeable {

    protected AbsStreamReadable input;
    protected PrintStream out;

    protected AbsCommunicate() {
    }

    @Override
    public boolean setStreamReadable(AbsStreamReadable readable) {
        try {
            close();
            this.input = readable;
            return true;
        } catch (IOException ex) {
            showException(ex);
            return false;
        }
    }

    @Override
    public void setDebug(boolean debug) {
        super.setDebug(debug);
        if (input != null) {
            this.input.setDebug(debug);
        }
    }
    

    @Override
    public boolean sendCommand(String command, Object... params) {
        input.clearResult();
        return insertCommand(command, params);
    }

    @Override
    public boolean insertCommand(String command, Object... params) {
        try {
            out.println(String.format(command, params));
            out.flush();
            return true;
        } catch (Exception ex) {
            showException(ex);
            return false;
        }
    }

    @Override
    public String readLine() {
        return input.readLine();
    }

    @Override
    public String readAll() {
        return input.readAll();
    }

    @Override
    public String readAll(AbsTime tiker) {
        return input.readAll(tiker);
    }

    @Override
    public String readUntil(String... regex) {
        return input.readUntil(regex);
    }

    @Override
    public String readUntil(AbsTime tiker, String... regex) {
        return input.readUntil(tiker, regex);
    }

    @Override
    public String readLine(AbsTime tiker) {
        return input.readLine(tiker);
    }

    @Override
    public StringBuffer getStringResult() {
        return this.input.getStringResult();
    }
    
    protected abstract void closeThis() throws IOException;

    @Override
    public void close() throws IOException {
        if (input != null) {
            input.close();
        }
        closeThis();
    }
}
