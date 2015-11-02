package com.galkonltd.qwikpik;

import com.galkonltd.qwikpik.keys.HotKeys;
import com.galkonltd.qwikpik.task.impl.CaptureTask;
import com.galkonltd.qwikpik.task.impl.OpenUrlTask;
import com.nova.task.TaskManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/15/2015
 */
public final class Utils {

    /**
     * The clipboard instance.
     */
    private static final Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();

    /**
     * The working directory path.
     */
    private static final String WORKING_DIRECTORY = getWorkingDirectory();

    /**
     * Returns the working directory of the program.
     * @return
     */
    public static String getWorkingDirectory() {
        if (WORKING_DIRECTORY != null) {
            return WORKING_DIRECTORY;
        }
        String dir = System.getProperty("user.home") + File.separator + ("." + Config.APPLICATION_NAME.toLowerCase()) + File.separator;
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    /**
     * Returns milliseconds as a {@link Date}.
     * @param milliseconds
     * @return
     */
    public static Date getDate(long milliseconds) {
        return new Date(milliseconds);
    }

    /**
     * Launches the specified url using the system's default browser.
     * @param url
     */
    public static void openURL(String url) {
        TaskManager.submitTask(new OpenUrlTask(url));
    }

    /**
     * Runs the capture task for the specified {@link HotKeys}.
     * @param capture
     */
    public static void capture(HotKeys capture) {
        TaskManager.submitTask(new CaptureTask(capture));
    }

    /**
     * Copies the given string to the user's clipboard.
     * @param string
     */
    public static void copyToClipboard(String string) {
        StringSelection stringSelection = new StringSelection(string);
        CLIPBOARD.setContents(stringSelection, null);
    }

    /**
     * Returns the clipboard contents as a string.
     * @return
     */
    public static String pasteFromClipboard() {
        try {
            return CLIPBOARD.getData(DataFlavor.stringFlavor).toString();
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Opens a URL in the operating system's default web browser.
     * @param url
     */
    public static void launchBrowser(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException e) {
                System.err.println("Desktop supported, IOException occurred while opening url: " + url);
                e.printStackTrace();
            } catch (URISyntaxException e) {
                System.err.println("Desktop supported, URISyntaxException occurred while opening url: " + url);
                e.printStackTrace();
            }
        } else {
            Runtime runtime = Runtime.getRuntime();
            if (com.sun.javafx.Utils.isMac()) {
                String[] args = {"osascript", "-e", "open location \"" + url + "\""};
                try {
                    runtime.exec(args);
                } catch (IOException e) {
                    System.err.println("Desktop not supported, IOException occurred while opening url (osascript): " + url);
                    e.printStackTrace();
                }
            } else {
                try {
                    runtime.exec("xdg-open " + url);
                } catch (IOException e) {
                    System.err.println("Desktop not supported, IOException occurred while opening url (xdg-open): " + url);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns the file at the specified resource path as an {@link InputStream}.
     * @param resourcePath
     * @return
     */
    public static InputStream resourceToInputStream(String resourcePath) {
        return Utils.class.getResourceAsStream("/com/galkonltd/qwikpik/resources/" + resourcePath);
    }

    /**
     * Loads a resource file as an {@link Image} (.png).
     * @param image
     * @return
     */
    public static Image loadResourceImage(String image) {
        try {
            return ImageIO.read(resourceToInputStream(image + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Loads an image from the working directory/images folder.
     * @param fileName
     * @return
     */
    public static BufferedImage loadImage(String fileName) {
        File file = new File(WORKING_DIRECTORY + "images" + File.separator + fileName);
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Error loading image: " + fileName);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Saves an image with the specified file name to the working directory/images folder.
     * @param fileName
     * @param image
     */
    public static void saveImage(String fileName, BufferedImage image) {
        if (image == null) {
            System.err.print("Null image: " + fileName);
            return;
        }
        File dir = new File(WORKING_DIRECTORY + "images");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        try {
            ImageIO.write(image, "png", file);
            image.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;

    public static String formatTimestamp(long timestamp) {
        return DATE_FORMAT.format(new Date(timestamp)).toString();
    }

    public static byte[] imageToByteArray(BufferedImage image) {
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos );
            baos.flush();
            byte[] data = baos.toByteArray();
            baos.close();
            return data;
        }catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Get the full display size (all monitors).
     * @return The screen size
     */
    public static Rectangle getFullDisplaySize() {
        Rectangle2D result = new Rectangle2D.Double();
        GraphicsEnvironment localGE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice gd : localGE.getScreenDevices()) {
            for (GraphicsConfiguration graphicsConfiguration : gd.getConfigurations()) {
                result.union(result, graphicsConfiguration.getBounds(), result);
            }
        }
        return result.getBounds();
    }

    public static Rectangle getRealScreenSize() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();

        Rectangle allScreenBounds = new Rectangle();
        for (GraphicsDevice screen : screens) {
            Rectangle screenBounds = screen.getDefaultConfiguration().getBounds();

            allScreenBounds.width += screenBounds.width;
            allScreenBounds.height = Math.max(allScreenBounds.height, screenBounds.height);

            if (screenBounds.x < allScreenBounds.y || screenBounds.y < allScreenBounds.y) {
                allScreenBounds.x = Math.min(allScreenBounds.x, screenBounds.x);
                allScreenBounds.y = Math.min(allScreenBounds.y, screenBounds.y);
            }
        }
        return allScreenBounds;
    }

    /**
     * Returns the number of displays the computer has.
     * @return
     */
    public static int getDisplayCount() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        return screens.length;
    }

}
