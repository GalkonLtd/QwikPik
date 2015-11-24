package com.galkonltd.qwikpik.ui.impl;

import com.galkonltd.qwikpik.Config;
import com.galkonltd.qwikpik.ui.UIPane;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/14/2015
 */
public class SideBar extends UIPane {

    public static final String[] SETTING_CATEGORIES = {
            "Menu",
    };

    public static final String[][] SETTINGS_SUBCATEGORIES = {
            { "About", "Capture", "Hotkeys", "Settings", "Uploaders", "Upload History" },
    };

    public static final int[] ABOUT_LOC = { 0, 0 };
    public static final int[] CAPTURE_LOC = { 0, 1 };
    public static final int[] HOTKEYS_LOC = { 0, 2 };
    public static final int[] SETTINGS_LOC = { 0, 3 };
    public static final int[] UPLOADERS_LOC = { 0, 4 };
    public static final int[] UPLOAD_HISTORY_LOC = { 0, 5 };

    private static int[] categoryPosition = { 0, 0 };
    private static int[] hoverPosition = { 0, 0 };
    private static int categoryX = 7;
    private static int categoryY = 22;
    private static int hoverIndex = -1;

    public static boolean currentSelectionMatches(int cat, int sub) {
        return categoryPosition[0] == cat && categoryPosition[1] == sub;
    }

    public static boolean hoverMatches(int cat, int sub) {
        return hoverPosition[0] == cat && hoverPosition[1] == sub;
    }

    public SideBar(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public static int[] getCategoryPosition() {
        return categoryPosition;
    }

    @Override
    public void drawContent() {
        int x = categoryX;
        int y = categoryY;

        this.fillRect(0, 0, this.getWidth(), this.getHeight(), Config.LIGHT_1);

        for (int index = 0; index < SETTING_CATEGORIES.length; index++) {
            this.drawString(SETTING_CATEGORIES[index].toUpperCase(), x, y, Config.REGULAR_FONT, Config.FONT_COLOR);
            y += 18;
            for (int sub = 0; sub < SETTINGS_SUBCATEGORIES[index].length; sub++) {
                boolean selected = currentSelectionMatches(index, sub);
                boolean hovered = hoverMatches(index, sub);
                if (selected || hovered) {
                    this.fillRect(x - 7, y - 11, 4, 13, Config.SPECTRUM_COLORS[sub]);
                }
                this.drawString(SETTINGS_SUBCATEGORIES[index][sub], x + 15 + (SETTINGS_SUBCATEGORIES[index][sub].equalsIgnoreCase("Settings") ? 1 : 0), y, Config.REGULAR_FONT, hovered ? Color.WHITE : selected ? new Color(200, 200, 200) : Config.FONT_COLOR);
                y += 16;
                if (sub == SETTINGS_SUBCATEGORIES[index].length - 1) {
                    y += 10;
                }
            }
        }
    }

    @Override
    public void mousePressedHook(MouseEvent e) {
        int y = categoryY;

        for (int index = 0; index < SETTING_CATEGORIES.length; index++) {
            y += 18;
            for (int sub = 0; sub < SETTINGS_SUBCATEGORIES[index].length; sub++) {
                if (this.mouseInRegion(e, 0, y - 10, this.getWidth(), 14)) {
                    categoryPosition[0] = index;
                    categoryPosition[1] = sub;
                    this.update();
                }
                y += 16;
                if (sub == SETTINGS_SUBCATEGORIES[index].length - 1) {
                    y += 10;
                }
            }
        }
    }

    @Override
    public void mouseMovedHook(MouseEvent e) {
        int y = categoryY;

        int hover = hoverIndex;
        hoverIndex = -1;

        int[] hoverSubCat = hoverPosition;
        hoverPosition[0] = hoverPosition[1] = -1;

        for (int index = 0; index < SETTING_CATEGORIES.length; index++) {
            y += 18;
            for (int sub = 0; sub < SETTINGS_SUBCATEGORIES[index].length; sub++) {
                if (this.mouseInRegion(e, 0, y - 10, this.getWidth(), 14)) {
                    hoverSubCat[0] = index;
                    hoverSubCat[1] = sub;
                }
                y += 16;
                if (sub == SETTINGS_SUBCATEGORIES[index].length - 1) {
                    y += 10;
                }
            }
        }

        if (hover != hoverIndex || !hoverSubCat.equals(hoverPosition)) {
            this.update();
        }
    }

    @Override
    public void mouseExitedHook(MouseEvent e) {
        hoverIndex = -1;
        hoverPosition[0] = hoverPosition[1] = -1;
        this.update();
    }

}
