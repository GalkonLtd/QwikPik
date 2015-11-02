package com.galkonltd.qwikpik.updater;

import com.nova.updater.JarUpdater;
import com.nova.utils.JarUtils;

import java.io.File;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 10/7/2015
 *
 * @author Galkon
 */
public class QwikPikUpdater extends JarUpdater {

    public static void main(String[] args) {
        JarUpdater updater = new QwikPikUpdater(args[0], args[1]);
        updater.startDownload();
    }

    public QwikPikUpdater(String updatePath, String downloadUrl) {
        super("QwikPik", updatePath, downloadUrl, "com.galkonltd.qwikpik.Application", null, null);
    }

    @Override
    public void launch() {
        JarUtils.launchJar(getAppName(), new File(getDownload().getSaveFile()), getMainClass(), getVmArgs(), getMainArgs());
        System.exit(0);
    }

}
