package com.galkonltd.qwikpik.keys.impl;

import com.galkonltd.qwikpik.Utils;
import com.galkonltd.qwikpik.keys.HotKeys;
import com.galkonltd.qwikpik.keys.KeyBind;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/17/2015
 */
public class ActiveDisplayKeyBind extends KeyBind {

    public ActiveDisplayKeyBind() {
        super(NativeKeyEvent.VC_SHIFT_L, NativeKeyEvent.VC_PRINTSCREEN);
    }

    @Override
    public void execute() {
        Utils.capture(HotKeys.CAPTURE_ACTIVE_DISPLAY);
    }

}
