package com.galkonltd.qwikpik.listeners;

import com.galkonltd.qwikpik.ui.ApplicationWindow;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.awt.*;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/13/2015
 */
public class GlobalMouseListener implements NativeMouseInputListener {

    private int clickX = -1;
    private int clickY = -1;
    private int dragX = -1;
    private int dragY = -1;
    private int mouseX = -1;
    private int mouseY = -1;

    private Rectangle getDragRectangle() {
        int startX = Math.min(clickX, dragX);
        int startY = Math.min(clickY, dragY);
        int endX = Math.max(clickX, dragX);
        int endY = Math.max(clickY, dragY);
        int width = endX - startX;
        int height = endY - startY;
        return new Rectangle(startX, startY, width, height);
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {

    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        this.clickX = this.dragX = e.getX();
        this.clickY = this.dragY = e.getY();
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        this.clickX = this.dragX = -1;
        this.clickY = this.dragY = -1;
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        int lastDragX = this.dragX;
        int lastDragY = this.dragY;
        this.dragX = e.getX();
        this.dragY = e.getY();
        int dragOffsetX = Math.min(this.dragX, lastDragX) == this.dragX ? -(lastDragX - this.dragX) : (this.dragX - lastDragX);
        int dragOffsetY = Math.min(this.dragY, lastDragY) == this.dragY ? -(lastDragY - this.dragY) : (this.dragY - lastDragY);

        if (ApplicationWindow.isDragging()) {
            ApplicationWindow window = ApplicationWindow.getInstance();
            window.setLocation(window.getX() + dragOffsetX, window.getY() + dragOffsetY);
        }
    }

}
