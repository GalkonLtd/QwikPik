package com.galkonltd.qwikpik.listeners;

import com.galkonltd.qwikpik.Settings;
import com.galkonltd.qwikpik.keys.KeyBindListener;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.HashMap;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/13/2015
 */
public class GlobalKeyListener implements NativeKeyListener {

    public KeyBindListener getKeyBindListener() {
        return keyBindListener;
    }

    private final KeyBindListener keyBindListener = new KeyBindListener(this);
    private final HashMap<Integer, String> heldKeyMap = new HashMap<>();

    public HashMap<Integer, String> getHeldKeyMap() {
        return heldKeyMap;
    }

    /**
     * Caches the keycode of the pressed key in the keymap.
     * @param keycode
     */
    private void pressKey(int keycode) {
        if (!Settings.ENABLE_KEY_LISTENING.isEnabled()) {
            return;
        }
        heldKeyMap.put(keycode, NativeKeyEvent.getKeyText(keycode));
        this.keyBindListener.processHeldKeys();
    }

    /**
     * Releases the key from the keymap.
     * @param keycode
     */
    private void releaseKey(int keycode) {
        heldKeyMap.remove(keycode);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int keycode = e.getKeyCode();
        pressKey(keycode);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        int keycode = e.getKeyCode();
        releaseKey(keycode);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {

    }

}
