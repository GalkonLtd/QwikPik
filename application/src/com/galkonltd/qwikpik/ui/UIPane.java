package com.galkonltd.qwikpik.ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/14/2015
 */
public abstract class UIPane {

    private java.util.List<UIComponent> components;
    private Graphics2D graphics;
    private GraphicsLabel parent;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    protected void add(UIComponent component) {
        this.components.add(component);
    }

    public GraphicsLabel getParent() {
        return parent;
    }

    protected void update() {
        this.getParent().repaint();
    }

    protected void setParent(GraphicsLabel graphicsLabel) {
        this.parent = graphicsLabel;
    }

    protected Graphics2D getGraphics() {
        return graphics;
    }

    protected void setGraphics(Graphics2D graphics) {
        this.graphics = graphics;
    }

    protected int getX() {
        return x;
    }

    protected int getY() {
        return y;
    }

    protected int getWidth() {
        return width;
    }

    protected int getHeight() {
        return height;
    }

    public UIPane(int x, int y, int width, int height) {
        this.components = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void draw() {
        getGraphics().setClip(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        this.drawContent();
        if (!this.components.isEmpty()) {
            for (UIComponent component : this.components) {
                component.draw();
            }
        }
        getGraphics().setClip(null);
    }

    public void drawContent() {}

    protected void drawImage(Image image, int x, int y) {
        this.getGraphics().drawImage(image, this.getX() + x, this.getY() + y, null);
    }

    protected void drawImage(Image image, int x, int y, float alpha) {
        AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        this.getGraphics().setComposite(alcom);
        this.getGraphics().drawImage(image, this.getX() + x, this.getY() + y, null);
        this.getGraphics().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
    }

    protected void drawCenteredString(String string, int x, int y, Font font, Color color) {
        this.drawCenteredString(string, x, y, font, color, true);
    }

    protected void drawCenteredString(String string, int x, int y, Font font, Color color, boolean shadowed) {
        this.drawString(string, x - this.getParent().getFontMetrics(font).stringWidth(string) / 2, y, font, color, shadowed);
    }

    protected void drawString(String string, int x, int y, Font font, Color color) {
        this.drawString(string, x, y, font, color, true);
    }

    protected void drawString(String string, int x, int y, Font font, Color color, boolean shadowed) {
        if (shadowed) {
            this.drawString(string, x + 1, y + 1, font, Color.BLACK, false);
        }
        this.getGraphics().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.getGraphics().setFont(font);
        this.getGraphics().setColor(color);
        this.getGraphics().drawString(string, this.getX() + x, this.getY() + y);
    }

    protected void drawRect(int x, int y, int width, int height, Color color) {
        this.getGraphics().setColor(color);
        this.getGraphics().drawRect(this.getX() + x, this.getY() + y, width, height);
    }

    protected void fillRect(int x, int y, int width, int height, Color color) {
        this.getGraphics().setColor(color);
        this.getGraphics().fillRect(this.getX() + x, this.getY() + y, width, height);
    }

    protected void drawOval(int x, int y, int width, int height, Color color) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.getGraphics().setColor(color);
        this.getGraphics().drawOval(x, y, width, height);
    }

    protected void fillOval(int x, int y, int width, int height, Color color) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.getGraphics().setColor(color);
        this.getGraphics().fillOval(x, y, width, height);
    }

    protected void drawLine(int x1, int y1, int x2, int y2, Color color) {
        this.getGraphics().setColor(color);
        this.getGraphics().drawLine(this.getX() + x1, this.getY() + y1, this.getX() + x2, this.getY() + y2);
    }

    public void mousePressedHook(MouseEvent e) {}

    protected boolean triggerMousePressed(MouseEvent e) {
        if (!(e.getX() >= this.getX() && e.getX() <= this.getX() + this.getWidth() && e.getY() >= this.getY() && e.getY() <= this.getY() + this.getHeight())) {
            return false;
        }
        this.mousePressedHook(e);
        for (UIComponent component : this.components) {
            if (this.mouseInRegion(e, component.getX(), component.getY(), component.getWidth(), component.getHeight())) {
                component.mousePressedHook(e);
            }
        }
        return false;
    }

    public void mouseMovedHook(MouseEvent e) {}

    protected boolean triggerMouseMove(MouseEvent e) {
        this.mouseMovedHook(e);
        for (UIComponent component : this.components) {
            if (this.mouseInRegion(e, component.getX(), component.getY(), component.getWidth(), component.getHeight())) {
                component.engageHover();
            } else {
                component.releaseHover();
            }
        }
        return false;
    }

    public void mouseReleasedHook(MouseEvent e) {}

    protected boolean triggerMouseRelease(MouseEvent e) {
        this.mouseReleasedHook(e);
        for (UIComponent component : this.components) {
            if (this.mouseInRegion(e, component.getX(), component.getY(), component.getWidth(), component.getHeight())) {
                component.releaseMouseHook(e);
            }
        }
        return false;
    }

    public void mouseDraggedHook(MouseEvent e) {}

    protected boolean triggerMouseDrag(MouseEvent e) {
        this.mouseDraggedHook(e);
        for (UIComponent component : this.components) {
            if (this.mouseInRegion(e, component.getX(), component.getY(), component.getWidth(), component.getHeight())) {
                component.dragMouseHook(e);
            }
        }
        return false;
    }

    public void mouseExitedHook(MouseEvent e) {}

    protected boolean triggerMouseExit(MouseEvent e) {
        this.mouseExitedHook(e);
        for (UIComponent component : this.components) {
            if (this.mouseInRegion(e, component.getX(), component.getY(), component.getWidth(), component.getHeight())) {
                component.exitMouseHook(e);
            }
        }
        return false;
    }

    public void mouseWheelHook(MouseWheelEvent e) {}

    protected boolean triggerMouseWheel(MouseWheelEvent e) {
        this.mouseWheelHook(e);
        for (UIComponent component : this.components) {
            if (this.mouseInRegion(e, component.getX(), component.getY(), component.getWidth(), component.getHeight())) {
                component.mouseWheelHook(e);
            }
        }
        return false;
    }

    public void mousePositionHeldHook(int x, int y) {}

    protected boolean triggerMousePositionHeld(int x, int y) {
        this.mousePositionHeldHook(x, y);
        for (UIComponent component : this.components) {
            if (this.mouseInRegion(x, y, component.getX(), component.getY(), component.getWidth(), component.getHeight())) {
                component.mousePosHeldHook(x, y);
            }
        }
        return false;
    }

    protected boolean mouseInRegion(MouseEvent e, int x, int y, int width, int height) {
        return this.mouseInRegion(e.getX(), e.getY(), x, y, width, height);
    }

    protected boolean mouseInRegion(int eventX, int eventY, int x, int y, int width, int height) {
        return eventX >= this.getX() + x && eventX <= this.getX() + x + width && eventY >= this.getY() + y && eventY <= this.getY() + y + height;
    }

    protected boolean mouseInCircle(MouseEvent e, int circleX, int circleY, int radius) {
        return this.mouseInCircle(e.getX(), e.getY(), circleX, circleY, radius);
    }

    protected boolean mouseInCircle(int eventX, int eventY, int circleX, int circleY, int radius) {
        return java.lang.Math.pow((circleX + radius - eventX), 2) + java.lang.Math.pow((circleY + radius - eventY), 2) < java.lang.Math.pow(radius, 2);
    }

}
