package com.galkonltd.qwikpik.ui.impl;

import com.galkonltd.qwikpik.*;
import com.galkonltd.qwikpik.ui.ApplicationWindow;
import com.galkonltd.qwikpik.ui.UIComponent;
import com.galkonltd.qwikpik.ui.UIPane;
import com.nova.utils.SystemUtils;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/14/2015
 */
public class TitleBar extends UIPane {

    private static int hoverIndex = -1;
    private static final int CLOSE_BUTTON_WIDTH = 44;
    private static final int CLOSE_BUTTON_HEIGHT = 19;

    private static final Image ICON = Utils.loadResourceImage("icon16x16");
    private static final Image LOGO = Utils.loadResourceImage("logo");
    public static final Image CLOSE_ICON = Utils.loadResourceImage("close_icon");
    private static final Image MAC_CLOSE_ICON = Utils.loadResourceImage("mac_close_icon");

    public TitleBar(int x, int y, int width, int height) {
        super(x, y, width, height);

        //topbar
        UIComponent topBar = new UIComponent(this, 0, 0, 600, 27) {
            @Override
            public void mousePressedHook(MouseEvent e) {
                ApplicationWindow.setDragging(true, e);
            }

            @Override
            public void releaseMouseHook(MouseEvent e) {
                ApplicationWindow.setDragging(false, e);
            }

            @Override
            public void dragMouseHook(MouseEvent e) {
            }

            @Override
            public void exitMouseHook(MouseEvent e) {
                ApplicationWindow.setDragging(false, e);
            }
        };

        this.add(topBar);
    }

    @Override
    public void drawContent() {
        boolean windows = SystemUtils.isWindows() && !Settings.ENABLE_OTHER_OS_STYLE.isEnabled();
        boolean mac = (SystemUtils.isMac() && !Settings.ENABLE_OTHER_OS_STYLE.isEnabled()) || !windows;
        this.fillRect(0, 0, this.getWidth(), this.getHeight(), Config.LIGHT_1);
        if (windows) {
            this.drawImage(ICON, 6, this.getHeight() / 2 - 7);
            this.drawImage(LOGO, this.getWidth() / 2 - LOGO.getWidth(null) / 2, 7);

            this.fillRect(this.getWidth() - CLOSE_BUTTON_WIDTH, 0, CLOSE_BUTTON_WIDTH, CLOSE_BUTTON_HEIGHT, hoverIndex == 0 ? Config.CLOSE_BUTTON_HOVER_COLOR : Config.CLOSE_BUTTON_COLOR);
            this.drawImage(CLOSE_ICON, this.getWidth() - (CLOSE_BUTTON_WIDTH / 2) - (CLOSE_ICON.getWidth(null) / 2), CLOSE_BUTTON_HEIGHT / 2 - CLOSE_ICON.getHeight(null) / 2);

            this.fillRect(this.getWidth() - (CLOSE_BUTTON_WIDTH + 21), 0, 21, CLOSE_BUTTON_HEIGHT, hoverIndex == 1 ? Config.MINIMIZE_BUTTON_WINDOWS_HOVER_COLOR : Config.MINIMIZE_BUTTON_WINDOWS_COLOR);
            this.fillRect(this.getWidth() - (CLOSE_BUTTON_WIDTH + 21) + 6, 12, 9, 2, Config.MINIMIZE_ICON_WINDOWS_COLOR);
        } else if (mac) {
            this.fillOval(9, this.getHeight() / 2 - 6, 13, 13, hoverIndex == 0 ? Config.CLOSE_BUTTON_COLOR : Config.CLOSE_BUTTON_COLOR);
            if (hoverIndex == 0) {
                this.drawImage(MAC_CLOSE_ICON, 9, this.getHeight() / 2 - 6, 0.65F);
            }

            this.fillOval(30, this.getHeight() / 2 - 6, 13, 13, hoverIndex == 1 ? Config.MINIMIZE_BUTTON_MAC_COLOR : Config.MINIMIZE_BUTTON_MAC_COLOR);
            if (hoverIndex == 1) {
                this.fillRect(30 + 2, this.getHeight() / 2, 9, 1, new Color(0, 0, 0, 100));
            }

            this.drawImage(ICON, this.getWidth() / 2 - LOGO.getWidth(null) / 2 - ICON.getWidth(null) / 2 - 1, this.getHeight() / 2 - 7);
            this.drawImage(LOGO, this.getWidth() / 2 - LOGO.getWidth(null) / 2 + ICON.getWidth(null) / 2 + 1, 7);
        }
    }

    @Override
    public void mouseMovedHook(MouseEvent e) {
        int hover = hoverIndex;
        hoverIndex = -1;

        boolean windows = SystemUtils.isWindows() && !Settings.ENABLE_OTHER_OS_STYLE.isEnabled();
        boolean mac = (SystemUtils.isMac() && !Settings.ENABLE_OTHER_OS_STYLE.isEnabled()) || !windows;
        if (windows) {
            if (this.mouseInRegion(e, this.getWidth() - CLOSE_BUTTON_WIDTH, 0, CLOSE_BUTTON_WIDTH, CLOSE_BUTTON_HEIGHT)) {
                hoverIndex = 0;
            } else if (this.mouseInRegion(e, this.getWidth() - (CLOSE_BUTTON_WIDTH + 21), 0, 21, CLOSE_BUTTON_HEIGHT)) {
                hoverIndex = 1;
            }
        } else if (mac) {
            if (this.mouseInCircle(e, 9, this.getHeight() / 2 - 6, 6)) {
                hoverIndex = 0;
            } else if (this.mouseInCircle(e, 30, this.getHeight() / 2 - 6, 6)) {
                hoverIndex = 1;
            }
        }

        if (hover != hoverIndex) {
            this.update();
        }
    }

    @Override
    public void mousePressedHook(MouseEvent e) {
        boolean windows = SystemUtils.isWindows() && !Settings.ENABLE_OTHER_OS_STYLE.isEnabled();
        boolean mac = (SystemUtils.isMac() && !Settings.ENABLE_OTHER_OS_STYLE.isEnabled()) || !windows;
        if (windows) {
            if (this.mouseInRegion(e, this.getWidth() - CLOSE_BUTTON_WIDTH, 0, CLOSE_BUTTON_WIDTH, CLOSE_BUTTON_HEIGHT)) {
                if (Settings.MINIMIZE_ON_CLOSE.isEnabled()) {
                    ApplicationWindow.minimize();
                    return;
                }
                Application.close();
            } else if (this.mouseInRegion(e, this.getWidth() - (CLOSE_BUTTON_WIDTH + 21), 0, 21, CLOSE_BUTTON_HEIGHT)) {
                ApplicationWindow.minimize();
                return;
            }
        } else if (mac) {
            if (this.mouseInCircle(e, 9, this.getHeight() / 2 - 6, 6)) {
                if (Settings.MINIMIZE_ON_CLOSE.isEnabled()) {
                    ApplicationWindow.minimize();
                    return;
                }
                Application.close();
            } else if (this.mouseInCircle(e, 30, this.getHeight() / 2 - 6, 6)) {
                ApplicationWindow.minimize();
                return;
            }
        }
    }

    @Override
    public void mouseExitedHook(MouseEvent e) {
        this.hoverIndex = -1;
        this.update();
    }

}
