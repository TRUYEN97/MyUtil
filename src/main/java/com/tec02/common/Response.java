/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

import com.alibaba.fastjson.JSONObject;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Administrator
 */
public class Response {

    private final int code;
    private final String response;
    private DataWareHouse wareHouse;
    public static final String MESSAGE = "message";
    public static final String DATA = "data";
    public static final String RESULT = "result";
    private JTextComponent textComponent;

    public Response(int code, String response) {
        this.code = code;
        this.response = response;
        if (response == null || response.isBlank()) {
            this.wareHouse = new DataWareHouse();
            return;
        }
        try {
            this.wareHouse = new DataWareHouse(JSONObject.parseObject(response));
        } catch (Exception e) {
        }
    }

    public void setTextComponent(JTextComponent textComponent) {
        this.textComponent = textComponent;
        if (this.textComponent != null) {
            this.textComponent.setText(null);
        }
    }

    public JTextComponent getTextComponent() {
        return textComponent;
    }

    public String getMessage() {
        if (code == 403) {
            return String.format("Access permissions insufficient to access");
        }
        if (code == 404 || code == -1 || this.wareHouse == null) {
            return this.response;
        }
        if (!isResponeseAvalid()) {
            return null;
        }
        return this.wareHouse.getString(MESSAGE, this.wareHouse.getString("Message", response));
    }

    public boolean isResponeseAvalid() {
        return this.wareHouse != null;
    }

    public <T> T getData() {
        if (!isResponeseAvalid()) {
            return null;
        }
        Object value = this.wareHouse.get(DATA);
        if (value == null) {
            return null;
        }
        return (T) value;
    }

    public <T> List<T> getDatas() {
        if (!isResponeseAvalid()) {
            return null;
        }
        return this.wareHouse.getListJsonArray(DATA);
    }

    public String getResponse() {
        return response;
    }

    public boolean getStatus() {
        return isResponeseAvalid() && (this.wareHouse.getBoolean(RESULT, false)
                || getStringEqualsIgnoreCase(RESULT, "pass"));
    }

    public boolean getStringEquals(String key, String target) {
        try {
            String value = this.wareHouse.getString(key);
            return value != null && value.equals(target);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean getStringEqualsIgnoreCase(String key, String target) {
        try {
            String value = this.wareHouse.getString(key);
            return value != null && value.equalsIgnoreCase(target);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFailStatusAndShowMessage() throws HeadlessException {
        if (!getStatus()) {
            String mess = getMessage();
            if (this.textComponent != null) {
                this.textComponent.setText(mess);
            } else {
                JOptionPane.showMessageDialog(null, String.valueOf(mess));
            }
            return true;
        }
        return false;
    }

    public int getCode() {
        return code;
    }

}
