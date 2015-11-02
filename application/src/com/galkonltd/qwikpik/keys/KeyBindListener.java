package com.galkonltd.qwikpik.keys;

import com.galkonltd.qwikpik.listeners.GlobalKeyListener;
import com.galkonltd.qwikpik.ui.ApplicationWindow;

import java.util.Iterator;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/17/2015
 */
public class KeyBindListener {

    public static KeyBindListener getInstance() {
        return instance;
    }

    private static KeyBindListener instance;

    private final GlobalKeyListener keyListener;

    public boolean isEditingHotKeys() {
        return editingHotKeys;
    }

    private boolean editingHotKeys;

    public int getEditingHotKeyIndex() {
        return editingHotKeyIndex;
    }

    public void setEditingHotKeyIndex(int editingHotKeyIndex) {
        this.editingHotKeyIndex = editingHotKeyIndex;
    }

    private int editingHotKeyIndex = -1;

    public KeyBindListener(GlobalKeyListener keyListener) {
        this.keyListener = keyListener;
        instance = this;
    }

    public String getHeldKeyCombination() {
        String combo = "";
        int index = 0;
        Iterator<Integer> it = this.keyListener.getHeldKeyMap().keySet().iterator();
        while (it.hasNext()) {
            int keycode = it.next();
            String keyname = this.keyListener.getHeldKeyMap().get(keycode);
            combo += keyname + (index < (this.keyListener.getHeldKeyMap().keySet().size() - 1) ? " + " : "");
            index++;
        }
        return combo;
    }

    public void processHeldKeys() {
        processKeyCombination(this.getHeldKeyCodes());
    }

    public int[] getHeldKeyCodes() {
        Iterator<Integer> it = this.keyListener.getHeldKeyMap().keySet().iterator();
        int index = 0;
        int[] keycodes = new int[this.keyListener.getHeldKeyMap().keySet().size()];
        while (it.hasNext()) {
            int keycode = it.next();
            keycodes[index] = keycode;
            index++;
        }
        return keycodes;
    }

    public void processKeyCombination(int[] keycodes) {
        if (this.editingHotKeys) {
            HotKeys hotkeys = HotKeys.values()[this.editingHotKeyIndex];
            hotkeys.getKeyBind().set(keycodes);
            ApplicationWindow.getInstance().repaint();
            return;
        }
        for (HotKeys keyMacro : HotKeys.values()) {
            KeyBind keyBind = keyMacro.getKeyBind();
            if (keyBind.matches(keycodes)) {
                keyBind.execute();
                return;
            }
        }
    }

    public void setEditingHotkeys(boolean editingHotkeys) {
        this.editingHotKeys = editingHotkeys;
    }
}
