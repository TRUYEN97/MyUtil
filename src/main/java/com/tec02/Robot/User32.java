/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tec02.Robot;

import Robot.W32API;
import Robot.W32API.HWND;
import com.sun.jna.Native;

/**
 *
 * @author lihaibin
 */
public interface User32 extends W32API {

    public final User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, DEFAULT_OPTIONS);

    boolean ShowWindow(HWND hWnd, int nCmdShow);

    boolean SetForegroundWindow(HWND hWnd);

    HWND FindWindow(String winClass, String title);

    HWND FindWindow(int winClass, String title);

    boolean CloseWindowStation(HWND hWnd);

    HWND FindWindowEx(HWND hWnd, HWND childWnd, int wParam, int lParam);

    HWND FindWindowEx(HWND hWnd, int childWnd, int wParam, int lParam);

    HWND FindWindowEx(HWND hWnd, int childWnd, String cClass, String title);

    boolean MoveWindow(HWND hWnd, int X, int Y, int nWidth, int nHeight);

    boolean ShowOwnedPopups(HWND hWnd, boolean fshow);

    boolean ShowWindow(HWND hWnd);

    boolean CloseWindow(HWND hWnd);//haibin.li

    boolean PostMessage(HWND hWnd, Integer Msg, Integer wParam, Integer lParam);

    boolean PostMessage(HWND hWnd, int Msg, int wParam, int lParam);

    boolean PostMessage(HWND hWnd, String Msg, int wParam, int lParam);

    boolean PostMessage(HWND hWnd, String Msg, String wParam, String lParam);

    boolean PostMessage(HWND hWnd, int Msg, String wParam, String lParam);

    boolean PostMessage(HWND hWnd, int Msg, String wParam, int lParam);

    boolean PostMessage(HWND hWnd, String Msg, String wParam, int lParam);

    void keybd_event(String bVk, String bScan, String dwFlags, String dwExtralnfo);

    void keybd_event(int bVk, String bScan, String dwFlags, String dwExtralnfo);

    void keybd_event(String bVk, int bScan, int dwFlags, int dwExtralnfo);

    void keybd_event(int bVk, int bScan, int dwFlags, int dwExtralnfo);

    void keybd_event(int bVk, int bScan, String dwFlags, int dwExtralnfo);

    void keybd_event(String bVk, int bScan, String dwFlags, int dwExtralnfo);

    boolean SendMessage(HWND hWnd, Integer Msg, Integer wParam, Integer lParam);

    boolean SendMessage(HWND hWnd, int Msg, int wParam, int lParam);

    boolean SendMessage(HWND hWnd, String Msg, int wParam, int lParam);

    boolean SendMessage(HWND hWnd, String Msg, String wParam, String lParam);

    boolean SendMessage(HWND hWnd, int Msg, String wParam, String lParam);

    boolean SendMessage(HWND hWnd, int Msg, String wParam, int lParam);

    boolean SendMessage(HWND hWnd, String Msg, String wParam, int lParam);

    boolean GetWindowRect(HWND hWnd, int[] xywh); //获取窗体大小

    HWND GetForegroundWindow();   //返回用户当前工作用的窗口句柄

    HWND GetCapture();      //该函数取得捕获了鼠标的窗口（如果存在）的句柄

    HWND SetCapture(HWND hWnd);      //该函数取得捕获了鼠标的窗口（如果存在）的句柄

    boolean ReleaseCapture(); //该函数从当前线程中的窗口释放鼠标捕获，并恢复通常的鼠标输入处理。捕获鼠标的窗口接收所有的鼠标输入（无论光标的位置在哪里），除非点击鼠标键时，光标热点在另一个线程的窗口中。

    boolean GetCursorPos(int[] mouseXY);      //获取鼠标位置

    HWND WindowFromPoint(int[] mouseXY);   //获取位置的句柄

    HWND GetCursor(); //该函数检取当前光标的句柄。

    boolean GetWindowText(HWND hWnd, byte[] title, int nMaxCount); //检取对话框中与某控件相关的标题或文本

    boolean GetWindowText(HWND hWnd, byte[] title); //检取对话框中与某控件相关的标题或文本

    boolean SetWindowTextW(HWND hWnd, byte[] title); // 设置给定窗口的标题栏的文字

    boolean SetWindowTextA(HWND hWnd, byte[] title); // 设置给定窗口的控件的文字

    HWND ChildWindowFromPoint(HWND hWnd, int[] xy);   //获取位置的句柄

    Long GetWindowLong(HWND hWnd, int nlndex);

    boolean CallWindowProc(Long nlndex, HWND hWnd, String Msg, String wParam, String IParam);
}
