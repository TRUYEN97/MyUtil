/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tec02.communication.Communicate;

import java.io.Closeable;

/**
 *
 * @author Administrator
 */
public interface ISender extends Closeable{
    
    boolean sendCommand(String command, Object... params);
    
    boolean insertCommand(String command, Object... params);
}
