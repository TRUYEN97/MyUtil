/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tec02.user32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;

/**
 *
 * @author lihaibin
 */
public interface User32 extends W32API {

    public final User32 INSTANCE = (User32) Native.load("user32", User32.class, DEFAULT_OPTIONS);

    int GW_CHILD = 5;
    int GW_HWNDNEXT = 2;

    int GetWindowThreadProcessId(WinDef.HWND hWnd, IntByReference lpdwProcessId);

    int GetWindowText(WinDef.HWND hWnd, char[] lpString, int nMaxCount);

    WinDef.HWND GetDesktopWindow();

    WinDef.HWND GetWindow(WinDef.HWND hWnd, int uCmd);

    boolean ShowWindow(WinDef.HWND hWnd, int nCmdShow);

    boolean SetForegroundWindow(WinDef.HWND hWnd);

    WinDef.HWND FindWindow(String winClass, String title);

    WinDef.HWND FindWindow(int winClass, String title);

    boolean CloseWindowStation(WinDef.HWND hWnd);

    WinDef.HWND FindWindowEx(WinDef.HWND hWnd, WinDef.HWND childWnd, int wParam, int lParam);

    WinDef.HWND FindWindowEx(WinDef.HWND hWnd, int childWnd, int wParam, int lParam);

    WinDef.HWND FindWindowEx(WinDef.HWND hWnd, int childWnd, String cClass, String title);

    boolean MoveWindow(WinDef.HWND hWnd, int X, int Y, int nWidth, int nHeight);

    boolean ShowOwnedPopups(WinDef.HWND hWnd, boolean fshow);

    boolean ShowWindow(WinDef.HWND hWnd);

    boolean CloseWindow(WinDef.HWND hWnd);//haibin.li

    boolean PostMessage(WinDef.HWND hWnd, Integer Msg, Integer wParam, Integer lParam);

    boolean PostMessage(WinDef.HWND hWnd, int Msg, int wParam, int lParam);

    boolean PostMessage(WinDef.HWND hWnd, String Msg, int wParam, int lParam);

    boolean PostMessage(WinDef.HWND hWnd, String Msg, String wParam, String lParam);

    boolean PostMessage(WinDef.HWND hWnd, int Msg, String wParam, String lParam);

    boolean PostMessage(WinDef.HWND hWnd, int Msg, String wParam, int lParam);

    boolean PostMessage(WinDef.HWND hWnd, String Msg, String wParam, int lParam);

    void keybd_event(String bVk, String bScan, String dwFlags, String dwExtralnfo);

    void keybd_event(int bVk, String bScan, String dwFlags, String dwExtralnfo);

    void keybd_event(String bVk, int bScan, int dwFlags, int dwExtralnfo);

    void keybd_event(int bVk, int bScan, int dwFlags, int dwExtralnfo);

    void keybd_event(int bVk, int bScan, String dwFlags, int dwExtralnfo);

    void keybd_event(String bVk, int bScan, String dwFlags, int dwExtralnfo);

    boolean SendMessage(WinDef.HWND hWnd, Integer Msg, Integer wParam, Integer lParam);

    boolean SendMessage(WinDef.HWND hWnd, int Msg, int wParam, int lParam);

    boolean SendMessage(WinDef.HWND hWnd, String Msg, int wParam, int lParam);

    boolean SendMessage(WinDef.HWND hWnd, String Msg, String wParam, String lParam);

    boolean SendMessage(WinDef.HWND hWnd, int Msg, String wParam, String lParam);

    boolean SendMessage(WinDef.HWND hWnd, int Msg, String wParam, int lParam);

    boolean SendMessage(WinDef.HWND hWnd, String Msg, String wParam, int lParam);

    boolean GetWindowRect(WinDef.HWND hWnd, int[] xywh); //获取窗体大小

    WinDef.HWND GetForegroundWindow();   //返回用户当前工作用的窗口句柄

    WinDef.HWND GetCapture();      //该函数取得捕获了鼠标的窗口（如果存在）的句柄

    WinDef.HWND SetCapture(WinDef.HWND hWnd);      //该函数取得捕获了鼠标的窗口（如果存在）的句柄

    boolean ReleaseCapture(); //该函数从当前线程中的窗口释放鼠标捕获，并恢复通常的鼠标输入处理。捕获鼠标的窗口接收所有的鼠标输入（无论光标的位置在哪里），除非点击鼠标键时，光标热点在另一个线程的窗口中。

    boolean GetCursorPos(int[] mouseXY);      //获取鼠标位置

    WinDef.HWND WindowFromPoint(int[] mouseXY);   //获取位置的句柄

    WinDef.HWND GetCursor(); //该函数检取当前光标的句柄。

    boolean GetWindowText(WinDef.HWND hWnd, byte[] title, int nMaxCount);

    boolean GetWindowText(WinDef.HWND hWnd, byte[] title); //检取对话框中与某控件相关的标题或文本

    boolean SetWindowTextW(WinDef.HWND hWnd, byte[] title); // 设置给定窗口的标题栏的文字

    boolean SetWindowTextA(WinDef.HWND hWnd, byte[] title); // 设置给定窗口的控件的文字

    WinDef.HWND ChildWindowFromPoint(WinDef.HWND hWnd, int[] xy);   //获取位置的句柄

    Long GetWindowLong(WinDef.HWND hWnd, int nlndex);

    boolean CallWindowProc(Long nlndex, WinDef.HWND hWnd, String Msg, String wParam, String IParam);

    boolean EnumWindows(WinUser.WNDENUMPROC wndenumproc, Object object);
}
