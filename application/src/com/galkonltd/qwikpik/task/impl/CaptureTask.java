package com.galkonltd.qwikpik.task.impl;

import com.galkonltd.qwikpik.*;
import com.galkonltd.qwikpik.audio.SoundEffect;
import com.galkonltd.qwikpik.audio.SoundPlayer;
import com.galkonltd.qwikpik.keys.HotKeys;
import com.galkonltd.qwikpik.ui.PreviewWindow;
import com.galkonltd.qwikpik.upload.UploadHandler;
import com.galkonltd.qwikpik.upload.UploadResult;
import com.nova.task.Task;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import sun.awt.ComponentFactory;
import sun.awt.image.SunWritableRaster;
import sun.security.util.SecurityConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.*;
import java.awt.peer.RobotPeer;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/14/2015
 */
public class CaptureTask extends Task {

    private static long lastCapture;
    private static boolean capturing = false;

    private final HotKeys capture;

    public CaptureTask(HotKeys capture) {
        this.capture = capture;
    }

    @Override
    public boolean execute() {
        if (capturing) {
            System.err.println("Capturing already, cannot capture now!");
            return false;
        }
        if (System.currentTimeMillis() - lastCapture < 1000) {
            System.err.println("Spamming capture button - filtered!");
            return false;
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lastCapture = System.currentTimeMillis();
        try {
            executeCapture(capture);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private static void executeCapture(HotKeys capture) throws Exception {
        BufferedImage capturedImage = null;
        final DirectRobot directRobot = new DirectRobot();

        switch (capture) {
            case CAPTURE_SELECTED_AREA: {
                final Rectangle screenRect = Utils.getFullDisplaySize();
                final BufferedImage previewImage = directRobot.createScreenCapture(screenRect);

                final int[] destX = {-1};
                final int[] destY = {-1};
                final int[] srcX = {-1};
                final int[] srcY = {-1};

                Dimension dimension = screenRect.getSize();
                JWindow window = new JWindow() {
                    @Override
                    public void paint(Graphics g) {
                        g.drawImage(previewImage, 0, 0, null);

                        g.setColor(new Color(0, 0, 0, 100));
                        g.fillRect(0, 0, previewImage.getWidth(), previewImage.getWidth());

                        if (srcX[0] != destX[0] || srcY[0] != destY[0]) {
                            int x1 = (srcX[0] < destX[0]) ? srcX[0] : destX[0];
                            int y1 = (srcY[0] < destY[0]) ? srcY[0] : destY[0];
                            int x2 = (srcX[0] > destX[0]) ? srcX[0] : destX[0];
                            int y2 = (srcY[0] > destY[0]) ? srcY[0] : destY[0];
                            int width = (x2-x1)+1;
                            int height = (y2-y1)+1;
                            try {
                                g.drawImage(previewImage.getSubimage(x1, y1, width, height), x1, y1, null);
                                //g.setColor(new Color(0, 0, 0, 100));
                                //g.fillRect(x1, y1, width, height);
                            } catch (Exception e) {
                                System.err.println(srcX[0] + ", " + destX[0] + ", " + srcY[0] + ", " + destY[0]);
                                e.printStackTrace();
                            }
                        }
                    }
                };
                MouseAdapter mouseAdapter = new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            window.dispose();
                            capturing = false;
                            return;
                        }
                        srcX[0] = destX[0] = e.getX();
                        srcY[0] = destY[0] = e.getY();
                        window.repaint();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (srcX[0] != destX[0] || srcY[0] != destY[0]) {
                            SoundPlayer.play(SoundEffect.SHUTTER);

                            int x1 = (srcX[0] < destX[0]) ? srcX[0] : destX[0];
                            int y1 = (srcY[0] < destY[0]) ? srcY[0] : destY[0];
                            int x2 = (srcX[0] > destX[0]) ? srcX[0] : destX[0];
                            int y2 = (srcY[0] > destY[0]) ? srcY[0] : destY[0];
                            int width = (x2-x1)+1;
                            int height = (y2-y1)+1;
                            try {
                                upload(previewImage.getSubimage(x1, y1, width, height));
                            } catch (Exception e1) {
                                System.err.println(srcX[0] + ", " + destX[0] + ", " + srcY[0] + ", " + destY[0]);
                                e1.printStackTrace();
                            }

                            window.dispose();
                            capturing = false;
                        }
                        window.repaint();
                    }

                    @Override
                    public void mouseMoved(MouseEvent e) {
                    }

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        destX[0] = e.getX();
                        destY[0] = e.getY();
                        window.repaint();
                    }
                };
                window.addMouseMotionListener(mouseAdapter);
                window.addMouseListener(mouseAdapter);
                window.setSize(dimension);
                window.setMaximumSize(dimension);
                window.setMinimumSize(dimension);
                window.setLocationRelativeTo(null);
                window.setLocation(screenRect.x, screenRect.y);
                window.setVisible(true);
                window.setAlwaysOnTop(true);
                window.requestFocus();
                window.toFront();
                window.repaint();
                capturing = true;
                return;
            }

            case CAPTURE_ACTIVE_WINDOW: {
                SoundPlayer.play(SoundEffect.SHUTTER);
                WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
                WinDef.RECT rect = new WinDef.RECT();
                User32.INSTANCE.GetWindowRect(hwnd, rect);
                capturedImage = directRobot.createScreenCapture(rect.toRectangle());
                break;
            }

            case CAPTURE_ACTIVE_DISPLAY: {
                SoundPlayer.play(SoundEffect.SHUTTER);
                GraphicsDevice currentDevice = MouseInfo.getPointerInfo().getDevice();
                capturedImage = directRobot.createScreenCapture(currentDevice.getDefaultConfiguration().getBounds());
                break;
            }

            case CAPTURE_FULL_DISPLAY: {
                SoundPlayer.play(SoundEffect.SHUTTER);
                Rectangle screenRect = Utils.getFullDisplaySize();
                capturedImage = directRobot.createScreenCapture(screenRect);
                break;
            }
        }

        capturing = false;

        if (capturedImage == null) {
            System.err.print("No image was captured!");
            return;
        }

        upload(capturedImage);
    }

    private static void upload(BufferedImage image) {
        TrayHandler.setImage(TrayHandler.UPLOAD_LOGO);
        UploadResult upload = UploadHandler.uploadImage(image);
        TrayHandler.setImage(TrayHandler.SMALL_LOGO);
        if (upload == null) {
            SoundPlayer.play(SoundEffect.FAIL);
            return;
        }
        String url = upload.getUrl();
        EventQueue.invokeLater(() -> new PreviewWindow(url, image));
        SoundPlayer.play(SoundEffect.SUCCESS);
        if (Settings.AUTO_COPY_UPLOAD.isEnabled()) {
            Utils.copyToClipboard(url);
        }
        if (Settings.AUTO_OPEN_UPLOAD.isEnabled()) {
            Utils.openURL(url);
        }
    }

}
