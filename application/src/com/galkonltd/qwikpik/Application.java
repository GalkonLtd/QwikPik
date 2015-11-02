package com.galkonltd.qwikpik;

import com.galkonltd.qwikpik.keys.HotKeys;
import com.galkonltd.qwikpik.listeners.GlobalKeyListener;
import com.galkonltd.qwikpik.listeners.GlobalMouseListener;
import com.galkonltd.qwikpik.ui.ApplicationWindow;
import com.galkonltd.qwikpik.upload.UploadHandler;
import com.galkonltd.qwikpik.upload.UploadHistory;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/13/2015
 */
public final class Application {

    public static void main(String[] args) {
        try {
            String path = Application.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            Config.START_ARCHIVE_PATH = path;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        VersionCheck.versionMatches();

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        } catch (UnsatisfiedLinkError e) {
            JOptionPane.showMessageDialog(null, "QwikPik is already running!", "Error 0x0", JOptionPane.ERROR_MESSAGE);
            return;
        }

        GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
        GlobalMouseListener mouseListener = new GlobalMouseListener();
        GlobalScreen.addNativeMouseListener(mouseListener);
        GlobalScreen.addNativeMouseMotionListener(mouseListener);

        Settings.load();
        HotKeys.load();
        UploadHistory.load();
        //UploadHistory.generateThumbnails();

        UploadHandler.initialize();
        TrayHandler.initialize();

        if (Settings.OPEN_ON_APPLICATION_START.isEnabled()) {
            ApplicationWindow.open();
        }
    }

    /**
     * Closes the application.
     */
    public static void close() {
        if (Settings.CONFIRM_EXIT.isEnabled()) {
            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (response != JOptionPane.YES_OPTION) {
                return;
            }
        }
        Settings.close();
        HotKeys.close();
        UploadHistory.close();
        TrayHandler.closeTray();
        System.exit(0);
    }

}