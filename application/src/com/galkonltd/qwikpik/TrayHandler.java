package com.galkonltd.qwikpik;

import com.galkonltd.qwikpik.keys.HotKeys;
import com.galkonltd.qwikpik.ui.ApplicationWindow;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." � Sydney Harris
 * Created on 8/13/2015
 */
public final class TrayHandler {

    public static final Image SMALL_LOGO = Utils.loadResourceImage("icon16x16");
    public static final Image UPLOAD_LOGO = Utils.loadResourceImage("upload_icon");

    private static TrayIcon trayIcon;
    private static SystemTray tray;

    public static void closeTray() {
        tray.remove(trayIcon);
    }

    public static void setImage(Image image) {
        trayIcon.setImage(image);
    }

    public static void initialize() {
        if (SystemTray.isSupported()) {
            tray = SystemTray.getSystemTray();
            try {
                trayIcon = new TrayIcon(SMALL_LOGO);
                trayIcon.setToolTip(Config.APPLICATION_NAME);

                trayIcon.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getClickCount() >= 2) {
                            ApplicationWindow.open();
                        }
                    }
                });

                PopupMenu menu = new PopupMenu();

                MenuItem show = new MenuItem("Show " + Config.APPLICATION_NAME);
                show.addActionListener(e -> ApplicationWindow.open());
                menu.add(show);

                MenuItem center = new MenuItem("Center " + Config.APPLICATION_NAME);
                center.addActionListener(e -> {
                    ApplicationWindow.open();
                    ApplicationWindow.getInstance().setLocationRelativeTo(null);
                });
                menu.add(center);

                menu.addSeparator();

                Menu captureMenu = new Menu("Capture");
                for (int index = 0; index < HotKeys.values().length; index++) {
                    HotKeys capture = HotKeys.values()[index];
                    MenuItem item = new MenuItem(capture.getName());
                    item.addActionListener(e -> Utils.capture(capture));
                    captureMenu.add(item);
                }
                menu.add(captureMenu);

                menu.addSeparator();

                MenuItem item = new MenuItem("Exit");
                item.addActionListener(e -> Application.close());
                menu.add(item);

                trayIcon.setPopupMenu(menu);
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

}
