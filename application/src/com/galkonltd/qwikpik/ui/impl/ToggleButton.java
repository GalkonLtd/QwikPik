package com.galkonltd.qwikpik.ui.impl;

import com.galkonltd.qwikpik.ui.UIComponent;
import com.galkonltd.qwikpik.ui.UIPane;
import com.galkonltd.qwikpik.Utils;
import com.nova.task.Task;
import com.nova.task.TaskManager;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/17/2015
 */
public abstract class ToggleButton extends UIComponent {

    private static final Image toggleBackground = Utils.loadResourceImage("toggle_background");
    private static final Image toggleButton = Utils.loadResourceImage("toggle_button");
    private static final Image toggleHover = Utils.loadResourceImage("toggle_button_hover");

    private boolean enabled;
    private int offsetX;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.offsetX = enabled ? 29 : 0;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public ToggleButton(boolean enabled) {
        this(null, -1, -1, enabled);
    }

    public ToggleButton(UIPane parent, int x, int y) {
        this(parent, x, y, true);
    }

    public ToggleButton(UIPane parent, int x, int y, boolean enabled) {
        super(parent, x, y, 60, 19);
        this.enabled = enabled;
        this.offsetX = enabled ? 29 : 0;
    }

    @Override
    public void draw() {
        this.drawImage(toggleBackground, 0, 0);
        this.drawImage(this.isHovered() ? toggleHover : toggleButton, -1 + this.offsetX, -1);
    }

    @Override
    public void mousePressedHook(MouseEvent e) {
        TaskManager.submitTask(new Task() {
            @Override
            public boolean execute() {
                if (!enabled) {
                    while (offsetX < 29) {
                        offsetX += 5;
                        if (offsetX > 29) {
                            offsetX = 29;
                        }
                        update();
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                    while (offsetX > 0) {
                        offsetX -= 5;
                        if (offsetX < 0) {
                            offsetX = 0;
                        }
                        update();
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                enabled = !enabled;
                toggle();
                return true;
            }
        });
    }

    public abstract void toggle();

}
