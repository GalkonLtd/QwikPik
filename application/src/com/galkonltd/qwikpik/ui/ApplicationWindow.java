package com.galkonltd.qwikpik.ui;

import com.galkonltd.qwikpik.Config;
import com.galkonltd.qwikpik.Settings;
import com.galkonltd.qwikpik.ui.impl.ContentPane;
import com.galkonltd.qwikpik.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/13/2015
 */
public final class ApplicationWindow extends JFrame {

    private static boolean dragging = false;
    private static ApplicationWindow instance;

    private static Point dragStartPoint = new Point(-1, -1);
    private final GraphicsLabel graphicsLabel;

    public static boolean isDragging() {
        return dragging;
    }

    public static void setDragging(boolean dragging, MouseEvent e) {
        ApplicationWindow.dragging = dragging;
        if (dragging) {
            if (dragStartPoint.equals(new Point(-1, -1))) {
                dragStartPoint = new Point(e.getX(), e.getY());
            }
        } else {
            dragStartPoint = new Point(-1, -1);
        }
    }

    public static ApplicationWindow getInstance() {
        return instance;
    }

    public static void open() {
        if (instance == null) {
            EventQueue.invokeLater(() -> instance = new ApplicationWindow());
            return;
        }
        instance.setVisible(true);
        instance.toFront();
        instance.requestFocus();
    }

    public static void minimize() {
        instance.setVisible(false);
    }

    public ApplicationWindow() {
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(new Dimension(600, 400));
        this.setTitle(Config.APPLICATION_NAME);
        this.setResizable(false);
        this.setUndecorated(true);
        java.util.List<Image> icons = new ArrayList<>();
        icons.add(Utils.loadResourceImage("icon64x64"));
        icons.add(Utils.loadResourceImage("icon32x32"));
        icons.add(Utils.loadResourceImage("icon16x16"));
        this.setIconImages(icons);

        this.getContentPane().setLayout(null);
        ContentPane contentPane = new ContentPane();
        graphicsLabel = contentPane.getGraphicsLabel();
        graphicsLabel.setBounds(0, 0, 600, 400);
        this.getContentPane().add(graphicsLabel);

        this.setShape(Settings.ROUNDED_WINDOW_CORNERS.isEnabled());
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.toFront();
        this.requestFocus();
    }

    public void setShape(boolean rounded) {
        this.setShape(rounded ? new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), 13, 13) : null);
    }

    public static void setRoundedWindow(boolean rounded) {
        getInstance().setShape(rounded);
    }

    public static int getStringWidth(Font font, String string) {
        return instance.getFontMetrics(font).stringWidth(string);
    }

    public static void update() {
        instance.graphicsLabel.update();
    }

}
