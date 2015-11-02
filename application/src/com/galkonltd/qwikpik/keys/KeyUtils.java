package com.galkonltd.qwikpik.keys;

import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/17/2015
 */
public final class KeyUtils {

    public static boolean isControlKey(int keycode) {
        return keycode == NativeKeyEvent.VC_CONTROL_L || keycode == NativeKeyEvent.VC_CONTROL_R;
    }

    public static boolean isAltKey(int keycode) {
        return keycode == NativeKeyEvent.VC_ALT_L || keycode == NativeKeyEvent.VC_ALT_R;
    }

    public static boolean isShiftKey(int keycode) {
        return keycode == NativeKeyEvent.VC_SHIFT_L || keycode == NativeKeyEvent.VC_SHIFT_R;
    }

    public static String getKeyShortName(int keycode) {
        if (isControlKey(keycode)) {
            return "CTRL";
        } else if (isAltKey(keycode)) {
            return "ALT";
        } else if (isShiftKey(keycode)) {
            return "SHIFT";
        } else if (NativeKeyEvent.getKeyText(keycode).equalsIgnoreCase("Print Screen")) {
            return "PRNT SCRN";
        }
        return NativeKeyEvent.getKeyText(keycode);
    }

}
