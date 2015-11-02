package com.galkonltd.qwikpik.ui;

import com.nova.task.Task;
import com.nova.task.TaskManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/14/2015
 */
public class GraphicsLabel extends JLabel {

    private final java.util.List<UIPane> UIPanes;

    public GraphicsLabel(UIPane... components) {
        this.UIPanes = new ArrayList<>();
        if (components != null && components.length > 0) {
            for (UIPane pane : components) {
                pane.setParent(this);
                this.UIPanes.add(pane);
            }
        }
        final class LabelMouseAdapter extends MouseAdapter {

            private int mouseX = -1;
            private int mouseY = -1;
            private long mouseLastMoved = -1;
            private boolean hoverEventTriggered = false;

            public void process() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (this.mouseLastMoved == -1) {
                        continue;
                    }
                    if (System.currentTimeMillis() - this.mouseLastMoved >= 6000 && this.hoverEventTriggered) {
                        for (UIPane pane : UIPanes) {
                            if (pane.triggerMousePositionHeld(-1, -1)) {
                                break;
                            }
                        }
                        this.mouseLastMoved = -1;
                        this.hoverEventTriggered = false;
                        continue;
                    }
                    if (System.currentTimeMillis() - this.mouseLastMoved >= 1000 && !this.hoverEventTriggered) {
                        for (UIPane pane : UIPanes) {
                            if (pane.triggerMousePositionHeld(this.mouseX, this.mouseY)) {
                                break;
                            }
                        }
                        this.hoverEventTriggered = true;
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                for (UIPane pane : UIPanes) {
                    if (pane.triggerMousePressed(e)) {
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                for (UIPane pane : UIPanes) {
                    if (pane.triggerMouseRelease(e)) {
                        break;
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                this.mouseX = e.getX();
                this.mouseY = e.getY();
                this.mouseLastMoved = System.currentTimeMillis();
                this.hoverEventTriggered = false;
                for (UIPane pane : UIPanes) {
                    if (pane.triggerMouseMove(e)) {
                        break;
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                this.mouseX = e.getX();
                this.mouseY = e.getY();
                this.mouseLastMoved = System.currentTimeMillis();
                this.hoverEventTriggered = false;
                for (UIPane pane : UIPanes) {
                    if (pane.triggerMouseDrag(e)) {
                        break;
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (this.mouseLastMoved != -1 || this.hoverEventTriggered) {
                    this.mouseLastMoved = -1;
                    this.hoverEventTriggered = false;
                }
                this.mouseX = -1;
                this.mouseY = -1;
                for (UIPane pane : UIPanes) {
                    if (pane.triggerMouseExit(e)) {
                        break;
                    }
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                for (UIPane pane : UIPanes) {
                    if (pane.triggerMouseWheel(e)) {
                        break;
                    }
                }
            }
        };
        LabelMouseAdapter mouseAdapter = new LabelMouseAdapter();
        this.addMouseWheelListener(mouseAdapter);
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);

        TaskManager.submitTask(new Task() {
            @Override
            public boolean execute() {
                mouseAdapter.process();
                return true;
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        for (UIPane pane : this.UIPanes) {
            pane.setGraphics((Graphics2D) g);
            pane.draw();
        }
    }

    public void update() {
        this.repaint();
    }

}
