package com.galkonltd.qwikpik;

import com.galkonltd.qwikpik.ui.ApplicationWindow;
import com.galkonltd.qwikpik.ui.impl.ToggleButton;
import com.nova.buffer.DataBuffer;
import com.nova.buffer.WriteBuffer;
import com.nova.file.ReadWriteFile;
import com.nova.task.Task;
import com.nova.task.TaskManager;
import com.nova.utils.SystemUtils;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/17/2015
 */
public enum Settings {

    OPEN_ON_APPLICATION_START("Open window when " + Config.APPLICATION_NAME + " starts", true),
    ENABLE_KEY_LISTENING("Enable key listening", true),
    ALWAYS_ON_TOP("Always on top", false),
    MINIMIZE_ON_CLOSE("Minimize on close", true),
    CONFIRM_EXIT("Confirm on exit", true),
    NOTIFICATION_SOUNDS("Notification sounds", true),
    AUTO_OPEN_UPLOAD("Auto-open uploads in default web browser", false),
    AUTO_COPY_UPLOAD("Auto-copy uploads to your clipboard", true),
    ROUNDED_WINDOW_CORNERS("Rounded window corners", SystemUtils.isMac()),
    ENABLE_OTHER_OS_STYLE("Enable " + (SystemUtils.isMac() ? "Windows" : "Mac") + "-style window", false),
    ;

    private final String name;
    private final ToggleButton toggleButton;
    private boolean enabled;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.toggleButton.setEnabled(enabled);
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public ToggleButton getToggleButton() {
        return toggleButton;
    }

    Settings(String name, boolean enabled) {
        this.name = name;
        final Settings settings = this;
        this.enabled = enabled;
        this.toggleButton = new ToggleButton(enabled) {
            @Override
            public void toggle() {
                settings.setEnabled(!settings.isEnabled());
                switch (settings) {
                    case ALWAYS_ON_TOP: {
                        ApplicationWindow.getInstance().setAlwaysOnTop(settings.isEnabled());
                        break;
                    }
                    case ROUNDED_WINDOW_CORNERS: {
                        ApplicationWindow.setRoundedWindow(settings.isEnabled());
                        break;
                    }
                    case ENABLE_OTHER_OS_STYLE: {
                        boolean windows = SystemUtils.isWindows() && !ENABLE_OTHER_OS_STYLE.isEnabled();
                        boolean mac = (SystemUtils.isMac() && !ENABLE_OTHER_OS_STYLE.isEnabled()) || !windows;
                        ApplicationWindow.update();
                        break;
                    }
                }
                save();
            }
        };
    }

    private static final int SAVE_VERSION = 0;
    private static final String SAVE_FILE_PATH = Utils.getWorkingDirectory() + "qwikpik.settings";

    private static final ReadWriteFile SAVE_FILE = new ReadWriteFile(SAVE_FILE_PATH) {

        @Override
        public void writeToBuffer(WriteBuffer writeBuffer) {
            writeBuffer.putShort(SAVE_VERSION);
            writeBuffer.putByte(Settings.values().length);
            for (int index = 0; index < Settings.values().length; index++) {
                Settings settings = Settings.values()[index];
                writeBuffer.putBoolean(settings.isEnabled());
                writeBuffer.putUTF(settings.toString());
            }
        }

        @Override
        public void readFromBuffer(DataBuffer dataBuffer) {
            int saveVersion = dataBuffer.getShort();
            switch (saveVersion) {
                case 0: {
                    int settings = dataBuffer.getByte();
                    for (int index = 0; index < settings; index++) {
                        boolean enabled = dataBuffer.getBoolean();
                        String name = dataBuffer.getUTF();
                        Settings setting = getSettingForValue(name);
                        if (setting == null) {
                            continue;
                        }
                        setting.setEnabled(enabled);
                    }
                    break;
                }
            }
        }

    };

    public static void close() {
        SAVE_FILE.close();
    }

    /**
     * Saves the settings on another thread.
     */
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

    /**
     * Returns the {@link Settings} for the specified name.
     * @param value
     * @return null if setting is not found
     */
    private static Settings getSettingForValue(String value) {
        for (Settings settings : Settings.values()) {
            if (settings.toString().equalsIgnoreCase(value)) {
                return settings;
            }
        }
        return null;
    }

}
