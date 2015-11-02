package com.galkonltd.qwikpik.keys;

import com.galkonltd.qwikpik.Utils;
import com.galkonltd.qwikpik.keys.impl.ActiveDisplayKeyBind;
import com.galkonltd.qwikpik.keys.impl.ActiveWindowKeyBind;
import com.galkonltd.qwikpik.keys.impl.FullDisplayKeyBind;
import com.galkonltd.qwikpik.keys.impl.SelectedAreaKeyBind;
import com.nova.buffer.DataBuffer;
import com.nova.buffer.WriteBuffer;
import com.nova.file.ReadWriteFile;
import com.nova.task.Task;
import com.nova.task.TaskManager;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/17/2015
 */
public enum HotKeys {

    CAPTURE_SELECTED_AREA("Selected Area", "Select a rectangular area of the screen to capture.", new SelectedAreaKeyBind()),
    CAPTURE_ACTIVE_WINDOW("Active Window", "Capture only the active window.", new ActiveWindowKeyBind()),
    CAPTURE_ACTIVE_DISPLAY("Active Display", "Capture only the active display (active monitor).", new ActiveDisplayKeyBind()),
    CAPTURE_FULL_DISPLAY("Full Display", "Capture the whole display (any and all monitors).", new FullDisplayKeyBind()),
    ;

    private final String name;
    private final KeyBind keyBind;
    private final String description;

    HotKeys(String name, String description, KeyBind keyBind) {
        this.name = name;
        this.description = description;
        this.keyBind = keyBind;
    }

    public KeyBind getKeyBind() {
        return keyBind;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    private static final int SAVE_VERSION = 0;
    private static final String SAVE_FILE_PATH = Utils.getWorkingDirectory() + "qwikpik.hotkeys";

    private static final ReadWriteFile SAVE_FILE = new ReadWriteFile(SAVE_FILE_PATH) {

        @Override
        public void writeToBuffer(WriteBuffer writeBuffer) {
            writeBuffer.putShort(SAVE_VERSION);
            writeBuffer.putByte(HotKeys.values().length);
            for (int index = 0; index < HotKeys.values().length; index++) {
                HotKeys hotkey = HotKeys.values()[index];
                writeBuffer.putUTF(hotkey.getName());
                hotkey.getKeyBind().toBuffer(writeBuffer);
            }
        }

        @Override
        public void readFromBuffer(DataBuffer dataBuffer) {
            int saveVersion = dataBuffer.getShort();
            switch (saveVersion) {
                case 0: {
                    int settings = dataBuffer.getByte();
                    for (int index = 0; index < settings; index++) {
                        String name = dataBuffer.getUTF();
                        HotKeys hotkey = getHotKeyForName(name);
                        if (hotkey == null) {
                            System.err.print("Hotkey null: " + name);
                            continue;
                        }
                        int[] keycodes = new int[dataBuffer.getByte()];
                        for (int k = 0; k < keycodes.length; k++) {
                            keycodes[k] = dataBuffer.getInt();
                        }
                        hotkey.getKeyBind().set(keycodes);
                    }
                    break;
                }
            }
        }
    };

    public static void save() {
        TaskManager.submitTask(new Task() {
            @Override
            public boolean execute() {
                return SAVE_FILE.write();
            }
        });
    }

    public static void load() {
        SAVE_FILE.read();
    }

    private static HotKeys getHotKeyForName(String name) {
        for (HotKeys hotkeys : HotKeys.values()) {
            if (hotkeys.getName().equalsIgnoreCase(name)) {
                return hotkeys;
            }
        }
        return null;
    }

    public static void close() {
        SAVE_FILE.close();
    }

}