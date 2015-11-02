package com.galkonltd.qwikpik.jna;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/24/2015
 */
interface User32 extends com.sun.jna.platform.win32.User32 {

    User32 INSTANCE = (User32) Native.loadLibrary(User32.class, W32APIOptions.UNICODE_OPTIONS);

    HWND GetDesktopWindow();
}
