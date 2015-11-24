package com.galkonltd.qwikpik.ui;

import com.galkonltd.qwikpik.Config;
import com.galkonltd.qwikpik.Scalr;
import com.galkonltd.qwikpik.Utils;
import com.galkonltd.qwikpik.ui.impl.TitleBar;
import com.nova.task.Task;
import com.nova.task.TaskManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/24/2015
 */
public class PreviewWindow extends JWindow {

    private static final Image CLIPBOARD_IMAGE = Utils.loadResourceImage("clipboard_icon");

    private final String url;
    private BufferedImage image;
    private int hoverIndex = -1;

    public PreviewWindow(String url, BufferedImage image) {
        this.url = url;
        this.image = Scalr.resize(image, 300, 200);
        Dimension size = new Dimension(1, this.image.getHeight() + 20);
        this.setMaximumSize(size);
        this.setMinimumSize(size);
        this.setPreferredSize(size);
        this.setSize(size);

        this.setBackground(Config.LIGHT_1);
        this.setForeground(Config.LIGHT_1);

        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);

        this.setAlwaysOnTop(true);
        this.setVisible(true);
        this.toFront();

        Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        this.setLocation(screenSize.width, screenSize.height - this.getHeight() - 5);

        TaskManager.submitTask(new Task() {
            @Override
            public boolean execute() {
                while (getLocation().getX() > (screenSize.width - (getWidth() + 5))) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (getWidth() < 300) {
                        increaseWidth(5);
                    }
                    setLocation(getLocation().x - 5, getLocation().y);
                }
                return true;
            }
        });

        TaskManager.submitTask(new Task() {
            @Override
            public boolean execute() {
                int time = 10;
                while (time > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    time--;
                }
                close();
                return true;
            }
        });
    }

    private void increaseWidth(int offset) {
        Dimension size = new Dimension(this.getSize().width + offset, this.getSize().height);
        this.setMaximumSize(size);
        this.setMinimumSize(size);
        this.setPreferredSize(size);
        this.setSize(size);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setFont(Config.BOLD_FONT);
        g2d.setColor(Color.BLACK);
        g2d.drawString(this.url, 6, 15);
        g2d.setColor(hoverIndex == 0 ? Color.WHITE : Config.FONT_COLOR);
        g2d.drawString(this.url, 5, 14);

        g2d.drawImage(CLIPBOARD_IMAGE, this.getWidth() - TitleBar.CLOSE_ICON.getWidth(null) - 5 - CLIPBOARD_IMAGE.getWidth(null) - 3, 11 - CLIPBOARD_IMAGE.getHeight(null) / 2, null);

        g2d.drawImage(TitleBar.CLOSE_ICON, this.getWidth() - TitleBar.CLOSE_ICON.getWidth(null) - 5, 10 - TitleBar.CLOSE_ICON.getHeight(null) / 2, null);

        g2d.drawImage(this.image, getWidth() / 2 - this.image.getWidth() / 2, 20, null);
    }

    private final MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            if (mouseInRegion(e, 0, 0, 5 + getFontMetrics(Config.BOLD_FONT).stringWidth(url) + 5, 20)) {
                Utils.openURL(url);
            } else if (mouseInRegion(e, getWidth() - TitleBar.CLOSE_ICON.getWidth(null) - 5 - CLIPBOARD_IMAGE.getWidth(null) - 3, 11 - CLIPBOARD_IMAGE.getHeight(null) / 2, 11, 11)) {
                Utils.copyToClipboard(url);
            } else if (mouseInRegion(e, getWidth() - TitleBar.CLOSE_ICON.getWidth(null) - 5, 5, 9, 9)) {
                close();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            int hover = hoverIndex;
            hoverIndex = -1;

            if (mouseInRegion(e, 0, 0, 5 + getFontMetrics(Config.BOLD_FONT).stringWidth(url) + 5, 20)) {
                hoverIndex = 0;
            } else if (mouseInRegion(e, getWidth() - TitleBar.CLOSE_ICON.getWidth(null) - 5 - CLIPBOARD_IMAGE.getWidth(null) - 3, 11 - CLIPBOARD_IMAGE.getHeight(null) / 2, 11, 11)) {
                hoverIndex = 1;
            } else if (mouseInRegion(e, getWidth() - TitleBar.CLOSE_ICON.getWidth(null) - 5, 5, 9, 9)) {
                hoverIndex = 2;
            }

            if (hover != hoverIndex) {
                repaint();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            hoverIndex = -1;
            repaint();
        }
    };

    private void close() {
        this.dispose();
        this.image.flush();
        this.image = null;
        System.gc();
    }

    private boolean mouseInRegion(MouseEvent e, int x, int y, int width, int height) {
        return e.getX() >= x && e.getX() <= x + width && e.getY() >= y && e.getY() <= y + height;
    }

}