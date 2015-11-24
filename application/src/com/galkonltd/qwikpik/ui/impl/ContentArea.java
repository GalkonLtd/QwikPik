package com.galkonltd.qwikpik.ui.impl;

import com.galkonltd.qwikpik.Config;
import com.galkonltd.qwikpik.Settings;
import com.galkonltd.qwikpik.Utils;
import com.galkonltd.qwikpik.keys.HotKeys;
import com.galkonltd.qwikpik.keys.KeyBindListener;
import com.galkonltd.qwikpik.ui.ApplicationWindow;
import com.galkonltd.qwikpik.ui.UIPane;
import com.galkonltd.qwikpik.upload.UploadHandler;
import com.galkonltd.qwikpik.upload.UploadResult;
import com.galkonltd.qwikpik.upload.UploadHistory;
import com.galkonltd.qwikpik.upload.uploader.Uploader;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Iterator;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/14/2015
 */
public class ContentArea extends UIPane {

    private static final Image LOCKBUTTON_LOCKED = Utils.loadResourceImage("lockbutton_locked");
    private static final Image LOCKBUTTON_UNLOCKED = Utils.loadResourceImage("lockbutton_unlocked");
    private static final Image LOCKBUTTON_HOVER = Utils.loadResourceImage("lockbutton_hover");
    private static final Image LOCK_IMAGE = Utils.loadResourceImage("lock_icon");
    private static final Image UNLOCKED_IMAGE = Utils.loadResourceImage("unlocked_icon");
    private static final Image EYE_IMAGE = Utils.loadResourceImage("eye_icon");
    private static final Image TRASH_IMAGE = Utils.loadResourceImage("trash_icon");
    private static final Image CAMERA_IMAGE = Utils.loadResourceImage("camera_icon");

    private static int captureHoverIndex = -1, captureCameraHoverIndex = -1, hotkeyHoverIndex = -1, uploadHistoryHoverIndex = -1, uploadEntryHoverIndex = -1;
    private static int uploadHistoryScrollOffset = 0;

    private static int hotkeyError = -1;

    public ContentArea(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawContent() {
        if (SideBar.currentSelectionMatches(SideBar.ABOUT_LOC[0], SideBar.ABOUT_LOC[1])) {//ABOUT
            int y = 140;
            this.drawCenteredString("Welcome to " + Config.APPLICATION_NAME + " (v " + Config.VERSION + ")!", this.getWidth() / 2, y, Config.BOLD_FONT, Config.FONT_COLOR);
            this.drawCenteredString("QwikPik is a user-friendly, cross-platform, and easy-to-use screen capturing utility.", this.getWidth() / 2, y += 20, Config.REGULAR_FONT, Config.FONT_COLOR);
            this.drawCenteredString("QwikPik is still in the early stages of development, therefor bugs may occur. Any", this.getWidth() / 2, y += 20, Config.REGULAR_FONT, Config.FONT_COLOR);
            this.drawCenteredString("and all feedback is welcome!", this.getWidth() / 2, y += 20, Config.REGULAR_FONT, Config.FONT_COLOR);
            this.drawCenteredString("www.qwikpik.it", this.getWidth() / 2, y += 20, Config.BOLD_FONT, new Color(100, 100, 255));
        } else if (SideBar.currentSelectionMatches(SideBar.CAPTURE_LOC[0], SideBar.CAPTURE_LOC[1])) {//capture
            int y = 0;
            int realIndex = 0;
            for (int index = 0; index < HotKeys.values().length; index++) {
                HotKeys keyMacro = HotKeys.values()[index];
                if (keyMacro == HotKeys.CAPTURE_ACTIVE_DISPLAY && Config.getDisplayCount() < 2) {
                    continue;
                }
                this.fillRect(0, y, this.getWidth(), 40, realIndex % 2 == 0 ? Config.LIGHT_2 : Config.LIGHT_1);
                this.drawCenteredString(keyMacro.getName(), this.getWidth() / 2, y + 15, Config.BOLD_FONT, captureHoverIndex == realIndex ? Color.WHITE : Config.FONT_COLOR);
                this.drawCenteredString(keyMacro.getDescription(), this.getWidth() / 2, y + 30, Config.REGULAR_FONT, captureHoverIndex == realIndex ? Color.WHITE : Config.FONT_COLOR);
                if (captureHoverIndex == realIndex) {
                    this.drawImage(CAMERA_IMAGE, this.getWidth() - (CAMERA_IMAGE.getWidth(null) + 10), y + 40 / 2 - CAMERA_IMAGE.getHeight(null) / 2, captureCameraHoverIndex == realIndex ? 1F : 0.5F);
                }
                y += 40;
                realIndex++;
            }
        } else if (SideBar.currentSelectionMatches(SideBar.SETTINGS_LOC[0], SideBar.SETTINGS_LOC[1])) {//settings
            int y = 0;
            for (int index = 0; index < Settings.values().length; index++) {
                Settings settings = Settings.values()[index];
                ToggleButton toggleButton = settings.getToggleButton();
                
                this.fillRect(0, y + (index * 30), this.getWidth(), 30, index % 2 == 0 ? Config.LIGHT_2 : Config.LIGHT_1);
                this.drawString(settings.getName(), 5, y + 19 + (index * 30), Config.REGULAR_FONT, Config.FONT_COLOR);
                if (toggleButton.getParent() == null) {
                    toggleButton.setParent(this);
                    toggleButton.setX(this.getWidth() - 66);
                    toggleButton.setY(y + 5 + (index * 30));
                }
                toggleButton.draw();
            }
        } else if (SideBar.currentSelectionMatches(SideBar.UPLOADERS_LOC[0], SideBar.UPLOADERS_LOC[1])) {//UPLOADERS
            int y = 0;
            int index = 0;
            Iterator<Uploader> it = UploadHandler.getUploaders().values().iterator();
            while (it.hasNext()) {
                Uploader uploader = it.next();
                this.fillRect(0, y + (index * 40), this.getWidth(), 40, index % 2 == 0 ? Config.LIGHT_2 : Config.LIGHT_1);
                this.drawCenteredString(uploader.getName(), this.getWidth() / 2, y + 15 + (index * 40), Config.BOLD_FONT, captureHoverIndex == index ? Color.WHITE : Config.FONT_COLOR);
                this.drawCenteredString(uploader.getDescription(), this.getWidth() / 2, y + 30 + (index * 40), Config.REGULAR_FONT, captureHoverIndex == index ? Color.WHITE : Config.FONT_COLOR);
                index++;
            }
        } else if (SideBar.currentSelectionMatches(SideBar.HOTKEYS_LOC[0], SideBar.HOTKEYS_LOC[1])) {//hotkeys
            this.fillRect(0, 0, this.getWidth(), 30, Config.LIGHT_1);
            this.drawCenteredString("Screen Capturing Hotkeys", this.getWidth() / 2, 18, Config.BOLD_FONT, Config.FONT_COLOR);
            int y = 30;
            int realIndex = 0;
            int editingHotkeyIndex = KeyBindListener.getInstance().getEditingHotKeyIndex();
            for (int index = 0; index < HotKeys.values().length; index++) {
                HotKeys keyMacro = HotKeys.values()[index];
                if (keyMacro == HotKeys.CAPTURE_ACTIVE_DISPLAY && Config.getDisplayCount() < 2) {
                    continue;
                }
                boolean editing = editingHotkeyIndex == realIndex;
                this.fillRect(0, y, this.getWidth(), 30, realIndex % 2 == 0 ? Config.LIGHT_2 : Config.LIGHT_1);
                if (editing) {
                    this.drawRect(0, y, this.getWidth() - 2, 28, Config.GREEN);
                }
                this.drawString(keyMacro.getName(), 5, y + 19, Config.REGULAR_FONT, Config.FONT_COLOR);
                String string = (editing ? "*" : "") + keyMacro.getKeyBind().toString();
                this.drawString(string, this.getWidth() - 35 - this.getParent().getFontMetrics(Config.REGULAR_FONT).stringWidth(string), y + 19, Config.REGULAR_FONT, Config.FONT_COLOR);

                this.drawImage(editing ? LOCKBUTTON_UNLOCKED : LOCKBUTTON_LOCKED, this.getWidth() - 5 - 22, y + 3);
                if (hotkeyHoverIndex == realIndex) {
                    this.drawImage(LOCKBUTTON_HOVER, this.getWidth() - 5 - 22, y + 3);
                }
                this.drawImage(editing ? UNLOCKED_IMAGE : LOCK_IMAGE, this.getWidth() - 5 - 19 + 3, y + 6);
                realIndex++;
                y += 30;
            }
            if (editingHotkeyIndex != -1) {
                this.drawCenteredString("You are editing a hotkey.", this.getWidth() / 2, 250, Config.REGULAR_FONT, Config.GREEN);
                this.drawCenteredString("Enter a keyboard combination and click the lock button to save it.", this.getWidth() / 2, 265, Config.REGULAR_FONT, Config.GREEN);
            }
            switch (hotkeyError) {
                case 0: {
                    this.fillRect(0, 0, this.getWidth(), this.getHeight(), new Color(196, 90, 90, 175));
                    this.drawCenteredString("You must save the hotkey you are currently editing.", this.getWidth() / 2, this.getHeight() / 2 - 7, Config.REGULAR_FONT, Config.FONT_COLOR);
                    this.drawCenteredString("(Click to dismiss)", this.getWidth() / 2, this.getHeight() / 2 + 7, Config.REGULAR_FONT.deriveFont(11F), Config.FONT_COLOR);
                    break;
                }
            }
            if (!Settings.ENABLE_KEY_LISTENING.isEnabled()) {
                this.fillRect(0, 0, this.getWidth(), this.getHeight(), new Color(40, 40, 40, 200));
                this.drawCenteredString("You must enable key listening to enable hotkeys.", this.getWidth() / 2, this.getHeight() / 2, Config.REGULAR_FONT, Config.FONT_COLOR);
            }
        } else if (SideBar.currentSelectionMatches(SideBar.UPLOAD_HISTORY_LOC[0], SideBar.UPLOAD_HISTORY_LOC[1])) {//upload history
            int y = 0 - uploadHistoryScrollOffset;
            for (int index = 0; index < UploadHistory.size(); index++) {
                boolean hovered = uploadHistoryHoverIndex == index;

                this.fillRect(0, y + (index * 30), this.getWidth(), 30, index % 2 == 0 ? Config.LIGHT_2 : Config.LIGHT_1);

                UploadResult upload = UploadHistory.getUploads().get(UploadHistory.size() - 1 - index);
                this.drawString(upload.getUrl(), 5, y + 19 + (index * 30), Config.REGULAR_FONT, hovered ? Color.WHITE : Config.FONT_COLOR);
                //String date = Utils.getDate(upload.getTimestamp()).toString();
                //this.drawString(date, this.getWidth() - ApplicationWindow.getStringWidth(Config.REGULAR_FONT, date) - 5, y + 19 + (index * 30), Config.REGULAR_FONT, hovered ? Color.WHITE : Config.FONT_COLOR);

                if (hovered) {
                    int x = this.getWidth() - 50;
                    this.drawImage(EYE_IMAGE, x, y + 8 + (index * 30), uploadEntryHoverIndex == 0 ? 1F : 0.5f);
                    this.drawImage(TRASH_IMAGE, x + EYE_IMAGE.getWidth(null) + 5, y + 5 + (index * 30), uploadEntryHoverIndex == 1 ? 1F : 0.5f);
                }
            }
        }
    }

    @Override
    public void mousePressedHook(MouseEvent e) {
        if (SideBar.currentSelectionMatches(SideBar.CAPTURE_LOC[0], SideBar.CAPTURE_LOC[1])) {//capture
            int y = 0;
            int realIndex = 0;
            for (int index = 0; index < HotKeys.values().length; index++) {
                if (HotKeys.values()[index] == HotKeys.CAPTURE_ACTIVE_DISPLAY && Config.getDisplayCount() < 2) {
                    continue;
                }
                if (this.mouseInRegion(e, this.getWidth() - (CAMERA_IMAGE.getWidth(null) + 10), y + 40 / 2 - CAMERA_IMAGE.getHeight(null) / 2, CAMERA_IMAGE.getWidth(null), CAMERA_IMAGE.getHeight(null))) {
                    Utils.capture(HotKeys.values()[index]);
                    return;
                }
                realIndex++;
                y += 40;
            }
        } else if (SideBar.currentSelectionMatches(SideBar.SETTINGS_LOC[0], SideBar.SETTINGS_LOC[1])) {//settings
            for (int index = 0; index < Settings.values().length; index++) {
                Settings settings = Settings.values()[index];
                ToggleButton toggleButton = settings.getToggleButton();
                if (this.mouseInRegion(e, toggleButton.getX(), toggleButton.getY(), toggleButton.getWidth(), toggleButton.getHeight())) {
                    toggleButton.mousePressedHook(e);
                    return;
                }
            }
        } else if (SideBar.currentSelectionMatches(SideBar.HOTKEYS_LOC[0], SideBar.HOTKEYS_LOC[1])) {//hotkeys
            if (hotkeyError == 0) {
                if (this.mouseInRegion(e, 0, 0, this.getWidth(), this.getHeight())) {
                    hotkeyError = -1;
                    this.update();
                    return;
                }
            }
            if (!Settings.ENABLE_KEY_LISTENING.isEnabled() || hotkeyError != -1) {
                return;
            }
            int y = 30;
            int realIndex = 0;
            int editingIndex = KeyBindListener.getInstance().getEditingHotKeyIndex();
            for (int index = 0; index < HotKeys.values().length; index++) {
                if (HotKeys.values()[index] == HotKeys.CAPTURE_ACTIVE_DISPLAY && Config.getDisplayCount() < 2) {
                    continue;
                }
                if (this.mouseInRegion(e, this.getWidth() - 5 - 19, y + 5, 19, 19)) {
                    if (editingIndex == realIndex) {
                        KeyBindListener.getInstance().setEditingHotkeys(false);
                        KeyBindListener.getInstance().setEditingHotKeyIndex(-1);
                        HotKeys.save();
                        this.update();
                        return;
                    }
                    if (editingIndex != -1) {
                        hotkeyError = 0;
                        this.update();
                        return;
                    }
                    KeyBindListener.getInstance().setEditingHotkeys(true);
                    KeyBindListener.getInstance().setEditingHotKeyIndex(realIndex);
                    this.update();
                    return;
                }
                realIndex++;
                y += 30;
            }
        } else if (SideBar.currentSelectionMatches(SideBar.UPLOAD_HISTORY_LOC[0], SideBar.UPLOAD_HISTORY_LOC[1])) {//upload history
            int y = 0 - uploadHistoryScrollOffset;
            for (int index = 0; index < UploadHistory.size(); index++) {
                UploadResult upload = UploadHistory.getUploads().get(UploadHistory.size() - 1 - index);
                int x = this.getWidth() - 50;
                if (this.mouseInRegion(e, x, y + 8 + (index * 30), 20, 14)) {//view
                    Utils.openURL(upload.getUrl());
                } else if (this.mouseInRegion(e, x + EYE_IMAGE.getWidth(null) + 5, y + 4 + (index * 30), 15, 19)) {//trash
                    UploadHistory.remove(UploadHistory.size() - 1 - index);
                    this.update();
                }
            }
        }
    }

    @Override
    public void mouseMovedHook(MouseEvent e) {
        int captureHover = captureHoverIndex;
        captureHoverIndex = -1;

        int captureCamera = captureCameraHoverIndex;
        captureCameraHoverIndex = -1;

        int hotkeyHover = hotkeyHoverIndex;
        hotkeyHoverIndex = -1;

        int uploadHistoryHover = uploadHistoryHoverIndex;
        uploadHistoryHoverIndex = -1;

        int uploadEntryHover = uploadEntryHoverIndex;
        uploadEntryHoverIndex = -1;

        if (ApplicationWindow.getInstance().getCursor() != Cursor.getDefaultCursor()) {
            ApplicationWindow.getInstance().setCursor(Cursor.getDefaultCursor());
        }

        if (SideBar.currentSelectionMatches(SideBar.CAPTURE_LOC[0], SideBar.CAPTURE_LOC[1])) {//capture
            int y = 0;
            int realIndex = 0;
            for (int index = 0; index < HotKeys.values().length; index++) {
                if (HotKeys.values()[index] == HotKeys.CAPTURE_ACTIVE_DISPLAY && Config.getDisplayCount() < 2) {
                    continue;
                }
                if (this.mouseInRegion(e, 0, y, this.getWidth(), 40)) {
                    captureHoverIndex = realIndex;
                    if (this.mouseInRegion(e, this.getWidth() - (CAMERA_IMAGE.getWidth(null) + 10), y + 40 / 2 - CAMERA_IMAGE.getHeight(null) / 2, CAMERA_IMAGE.getWidth(null), CAMERA_IMAGE.getHeight(null))) {
                        captureCameraHoverIndex = realIndex;
                    }
                }
                y += 40;
                realIndex++;
            }
        } else if (SideBar.currentSelectionMatches(SideBar.SETTINGS_LOC[0], SideBar.SETTINGS_LOC[1])) {//settings
            for (int index = 0; index < Settings.values().length; index++) {
                Settings settings = Settings.values()[index];
                ToggleButton toggleButton = settings.getToggleButton();
                if (this.mouseInRegion(e, toggleButton.getX(), toggleButton.getY(), toggleButton.getWidth(), toggleButton.getHeight())) {
                    toggleButton.engageHover();
                } else {
                    toggleButton.releaseHover();
                }
            }
        } else if (SideBar.currentSelectionMatches(SideBar.HOTKEYS_LOC[0], SideBar.HOTKEYS_LOC[1])) {//hotkeys
            if (!Settings.ENABLE_KEY_LISTENING.isEnabled() || hotkeyError != -1) {
                return;
            }
            int y = 30;
            int realIndex = 0;
            for (int index = 0; index < HotKeys.values().length; index++) {
                if (HotKeys.values()[index] == HotKeys.CAPTURE_ACTIVE_DISPLAY && Config.getDisplayCount() < 2) {
                    continue;
                }
                if (this.mouseInRegion(e, this.getWidth() - 5 - 19, y + 5, 19, 19)) {
                    hotkeyHoverIndex = realIndex;
                }
                realIndex++;
                y += 30;
            }
        } else if (SideBar.currentSelectionMatches(SideBar.UPLOAD_HISTORY_LOC[0], SideBar.UPLOAD_HISTORY_LOC[1])) {//upload history
            int y = 0 - uploadHistoryScrollOffset;
            for (int index = 0; index < UploadHistory.size(); index++) {
                if (this.mouseInRegion(e, 0, y + (index * 30), this.getWidth(), 30)) {
                    UploadResult upload = UploadHistory.getUploads().get(UploadHistory.size() - 1 - index);
                    uploadHistoryHoverIndex = index;

                    int x = this.getWidth() - 50;
                    if (this.mouseInRegion(e, x, y + 8 + (index * 30), 20, 14)) {//view
                        uploadEntryHoverIndex = 0;
                    } else if (this.mouseInRegion(e, x + EYE_IMAGE.getWidth(null) + 5, y + 4 + (index * 30), 15, 19)) {//trash
                        uploadEntryHoverIndex = 1;
                    }
                }
            }
        }

        if (captureCamera != captureCameraHoverIndex || uploadEntryHover != uploadEntryHoverIndex || uploadHistoryHover != uploadHistoryHoverIndex || captureHover != captureHoverIndex || hotkeyHover != hotkeyHoverIndex) {
            this.update();
        }
    }

    @Override
    public void mouseExitedHook(MouseEvent e) {
        if (SideBar.currentSelectionMatches(SideBar.CAPTURE_LOC[0], SideBar.CAPTURE_LOC[1])) {//capture
            captureHoverIndex = -1;
            captureCameraHoverIndex = -1;
        } else if (SideBar.currentSelectionMatches(SideBar.SETTINGS_LOC[0], SideBar.SETTINGS_LOC[1])) {//settings
            for (int index = 0; index < Settings.values().length; index++) {
                Settings settings = Settings.values()[index];
                ToggleButton toggleButton = settings.getToggleButton();
                if (this.mouseInRegion(e, toggleButton.getX(), toggleButton.getY(), toggleButton.getWidth(), toggleButton.getHeight())) {
                    toggleButton.releaseHover();
                    toggleButton.exitMouseHook(e);
                }
            }
        } else if (SideBar.currentSelectionMatches(SideBar.HOTKEYS_LOC[0], SideBar.HOTKEYS_LOC[1])) {//hotkeys
            hotkeyHoverIndex = -1;
        } else if(SideBar.currentSelectionMatches(SideBar.UPLOAD_HISTORY_LOC[0], SideBar.UPLOAD_HISTORY_LOC[1])) {//upload history
            uploadHistoryHoverIndex = -1;
        }
        this.update();
    }

    @Override
    public void mouseWheelHook(MouseWheelEvent e) {
        if (SideBar.currentSelectionMatches(SideBar.UPLOAD_HISTORY_LOC[0], SideBar.UPLOAD_HISTORY_LOC[1])) {//upload history
            if (UploadHistory.size() < 12) {
                return;
            }
            int offset = uploadHistoryScrollOffset;
            if (this.mouseInRegion(e, 0, 0, this.getWidth(), this.getHeight())) {
                uploadHistoryScrollOffset += e.getWheelRotation() * 30;
                if (uploadHistoryScrollOffset < 0) {
                    uploadHistoryScrollOffset = 0;
                }
                if (uploadHistoryScrollOffset >= (UploadHistory.size() - 12) * 30) {
                    uploadHistoryScrollOffset = (UploadHistory.size() - 12) * 30;
                }
            }
            this.mouseMovedHook(e);
            if (offset != uploadHistoryScrollOffset) {
                this.update();
            }
        }
    }

    @Override
    public void mousePositionHeldHook(int x, int y) {
        if (SideBar.currentSelectionMatches(SideBar.CAPTURE_LOC[0], SideBar.CAPTURE_LOC[1])) {
            int offsetY = 0;
            for (int index = 0; index < HotKeys.values().length; index++) {
                if (HotKeys.values()[index] == HotKeys.CAPTURE_ACTIVE_DISPLAY && Config.getDisplayCount() < 2) {
                    continue;
                }
                if (this.mouseInRegion(x, y, 0, offsetY + (index * 40), this.getWidth(), 40)) {
                    System.out.println("Hovered over: " + index);
                }
            }
        }
    }

}
