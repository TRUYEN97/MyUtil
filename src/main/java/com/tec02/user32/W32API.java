/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tec02.user32;

import java.util.HashMap;
import java.util.Map;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;

@SuppressWarnings({"unchecked", "serial"})
/**
 *
 * @author lihaibin
 */
public interface W32API extends StdCallLibrary {

    public Map UNICODE_OPTIONS = new HashMap() {

        private static final long serialVersionUID = 1L;

        {
            put(OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
            put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
        }
    };
    public Map ASCII_OPTIONS = new HashMap() {

        {
            put(OPTION_TYPE_MAPPER, W32APITypeMapper.ASCII);
            put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.ASCII);
        }
    };
    public Map DEFAULT_OPTIONS = Boolean.getBoolean("w32.ascii") ? ASCII_OPTIONS : UNICODE_OPTIONS;

}
