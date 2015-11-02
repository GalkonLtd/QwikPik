package com.galkonltd.qwikpik.ui;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/14/2015
 */
public class UIComponent {

    public void setParent(UIPane parent) {
        this.parent = parent;
    }

    private UIPane parent;

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    private int x;
    private int y;
    private final int width;
    private final int height;
    private boolean hovered;

    public void update() {
        this.getParent().update();
    }

    public UIPane getParent() {
        return parent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public UIComponent(UIPane parent, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parent = parent;
        this.hovered = false;
    }

    public void draw() {}

    public void mousePressedHook(MouseEvent e) {}

    public void releaseMouseHook(MouseEvent e) {}

    public void dragMouseHook(MouseEvent e) {}

    public void exitMouseHook(MouseEvent e) {}

    public void mouseWheelHook(MouseEvent e) {}

    public void mousePosHeldHook(int x, int y) {}

    public void engageHover() {
        this.hovered = true;
        this.getParent().getParent().repaint();
    }

    public void releaseHover() {
        this.hovered = false;
        this.getParent().getParent().repaint();
    }

    protected void drawImage(Image image, int x, int y) {
        this.getParent().drawImage(image, this.getX() + x, this.getY() + y);
    }

    protected void drawText(String string, int x, int y, Font font, Color color) {
        this.getParent().getGraphics().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.getParent().drawString(string, this.getX() + x, this.getY() + y, font, color);
    }

    protected void drawRect(int x, int y, int width, int height, Color color) {
        this.getParent().drawRect(this.getX() + x, this.getY() + y, width, height, color);
    }

    protected void fillRect(int x, int y, int width, int height, Color color) {
        this.getParent().fillRect(x, y, width, height, color);
    }

    protected boolean isHovered() {
        return hovered;
    }
}