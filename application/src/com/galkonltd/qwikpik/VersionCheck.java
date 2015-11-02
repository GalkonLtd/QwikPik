package com.galkonltd.qwikpik;

import com.nova.file.FileUtils;
import com.nova.utils.JarUtils;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 10/7/2015
 *
 * @author Galkon
 */
public final class VersionCheck {

    public static boolean versionMatches() {
        InputStream input = null;
        try {
            input = new URL("http://www.qwikpik.it/version.php").openStream();
            final String liveVersion = IOUtils.toString(input);
            if (Config.VERSION.equalsIgnoreCase(liveVersion)) {
                return true;
            }
            int response = JOptionPane.showConfirmDialog(null, "Your version (" + Config.VERSION + ") of QwikPik is outdated! Would you like to update now?", "Version " + liveVersion + " Available", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                byte[] updaterArchive = FileUtils.inputStreamToByteArray(new URL("http://www.qwikpik.it/builds/QwikPikUpdater.jar").openStream());
                if (updaterArchive == null || updaterArchive.length == 0) {
                    System.err.println("Major error downloading update archive!");
                    return false;
                }
                String updaterPath = Utils.getWorkingDirectory() + "updater.jar";
                FileUtils.writeToFile(updaterPath, updaterArchive);
                JarUtils.launchJar("QwikPik Updater", new File(updaterPath), "com.galkonltd.qwikpik.updater.QwikPikUpdater", null, new String[]{ Config.START_ARCHIVE_PATH, "http://www.qwikpik.it/builds/qwikpik-" + liveVersion + ".jar" });
                System.exit(0);
                return false;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                IOUtils.closeQuietly(input);
            }
        }
        return false;
    }

}