/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tec02.communication.Communicate.Impl.Cmd;

import com.tec02.communication.Communicate.AbsCommunicate;
import com.tec02.communication.Communicate.AbsStreamReadable;
import com.tec02.communication.Communicate.IReadStream;
import com.tec02.communication.Communicate.ISender;
import com.tec02.communication.Communicate.ReadStreamOverTime;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class Cmd extends AbsCommunicate implements ISender, IReadStream {

    private Process process;
    private final ProcessBuilder builder;

    public Cmd() {
        this.input = new ReadStreamOverTime();
        this.builder = new ProcessBuilder();
        this.builder.redirectErrorStream(true);
    }

    public Cmd(AbsStreamReadable reader) {
        this.input = reader;
        this.builder = new ProcessBuilder();
        this.builder.redirectErrorStream(true);
    }

    @Override
    public boolean insertCommand(String command, Object... params) {
        destroy();
        this.builder.command("cmd.exe", "/c", String.format(command, params));
        try {
            this.process = builder.start();
            this.input.setReader(process.getInputStream());
            return true;
        } catch (IOException ex) {
            showException(ex);
            return false;
        }
    }
    
    public int waitFor() throws InterruptedException{
        return this.process.waitFor();
    }

    public void destroy() {
        try {
            close();
        } catch (IOException e) {
            showException(e);
        }
    }


    @Override
    protected void closeThis() throws IOException {
        if (process != null) {
            process.destroy();
        }
    }

}
