package com.galkonltd.qwikpik;

import java.awt.*;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/14/2015
 */
public final class Config {

    /**
     * The application name.
     */
    public static final String APPLICATION_NAME = "QwikPik";
    public static final String VERSION = "0.4.1";

    public static final Color FONT_COLOR = new Color(173, 175, 178);
    public static final Font CALIBRI_12 = new Font("Calibri", 0, 12);
    public static final Font CALIBRI_12_BOLD = new Font("Calibri", Font.BOLD, 12);
    public static final Color LIGHT_1 = new Color(40, 40, 40);
    public static final Color LIGHT_2 = new Color(34, 34, 34);
    public static final Color DARK_1 = new Color(24, 24, 24);
    public static final Color GREEN = new Color(130, 215, 124);

    public static final Color CLOSE_BUTTON_COLOR = new Color(255, 96, 82);
    public static final Color CLOSE_BUTTON_HOVER_COLOR = new Color(225, 76, 69);

    public static final Color MINIMIZE_BUTTON_MAC_COLOR = new Color(255, 190, 6);
    public static final Color MINIMIZE_BUTTON_MAC_HOVER_COLOR = new Color(194, 150, 33);

    public static final Color MINIMIZE_BUTTON_WINDOWS_COLOR = new Color(40, 40, 40);
    public static final Color MINIMIZE_BUTTON_WINDOWS_HOVER_COLOR = new Color(50, 50, 50);
    public static final Color MINIMIZE_ICON_WINDOWS_COLOR = new Color(219, 219, 219);

    public static final Color[] SPECTRUM_COLORS = {
        Color.RED,
        Color.ORANGE,
        Color.YELLOW,
        Color.GREEN,
        new Color(50, 145, 255),
        new Color(124, 51, 230),
        new Color(171, 18, 246),
    };
    public static String START_ARCHIVE_PATH = "QwikPik.jar";

    /**
     * Returns the number of displays the computer has.
     * @return
     */
    public static int getDisplayCount() {
        return displayCount;
    }

    /**
     * The current number of displays (monitors) available (default 1).
     */
    private static final int displayCount = Utils.getDisplayCount();

}