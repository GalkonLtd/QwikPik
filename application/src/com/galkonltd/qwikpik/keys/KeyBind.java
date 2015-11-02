package com.galkonltd.qwikpik.keys;

import com.nova.buffer.WriteBuffer;

import java.util.ArrayList;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/17/2015
 */
public abstract class KeyBind {

    private final java.util.List<Integer> keycodes = new ArrayList<>();

    public KeyBind(int ...keycodes) {
        for (int key : keycodes) {
            this.keycodes.add(key);
        }
    }

    public abstract void execute();

    public void set(int ...keycodes) {
        this.keycodes.clear();
        for (int key : keycodes) {
            this.keycodes.add(key);
        }
    }

    public boolean containsKey(int keycode) {
        return this.keycodes.contains(keycode);
    }

    public boolean matches(int ...keycodes) {
        if (keycodes.length != this.keycodes.size()) {
            return false;
        }
        for (int key : keycodes) {
            if (KeyUtils.isControlKey(key) && this.containsControl()) {
                continue;
            }
            if (KeyUtils.isShiftKey(key) && this.containsShift()) {
                continue;
            }
            if (KeyUtils.isAltKey(key) && this.containsAlt()) {
                continue;
            }
            if (!this.keycodes.contains(key)) {
                return false;
            }
        }
        return true;
    }

    private boolean containsControl() {
        for (int key : this.keycodes) {
            if (KeyUtils.isControlKey(key)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsShift() {
        for (int key : this.keycodes) {
            if (KeyUtils.isShiftKey(key)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsAlt() {
        for (int key : this.keycodes) {
            if (KeyUtils.isAltKey(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String bind = "";
        int index = 0;
        for (int key : this.keycodes) {
            bind += KeyUtils.getKeyShortName(key) + ((index < this.keycodes.size() - 1) ? " + " : "");
            index++;
        }
        return bind;
    }

    public String[] keysToStrings() {
        String[] keys = new String[this.keycodes.size()];
        int index = 0;
        for (int key : this.keycodes) {
            keys[index] = KeyUtils.getKeyShortName(key);
            index++;
        }
        return keys;
    }

    public void toBuffer(WriteBuffer buffer) {
        buffer.putByte(this.keycodes.size());
        for (int key : this.keycodes) {
            buffer.putInt(key);
        }
    }

}
