package com.galkonltd.qwikpik.ui.impl;

import com.galkonltd.qwikpik.Config;
import com.galkonltd.qwikpik.ui.UIPane;
import com.galkonltd.qwikpik.ui.GraphicsLabel;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/14/2015
 */
public final class ContentPane {

    private GraphicsLabel graphicsLabel;

    public GraphicsLabel getGraphicsLabel() {
        return graphicsLabel;
    }

    public ContentPane() {
        UIPane background = new UIPane(0, 0, 600, 400) {
            @Override
            public void drawContent() {
                this.fillRect(0, 0, this.getWidth(), this.getHeight(), Config.DARK_1);
            }
        };

        UIPane titlebar = new TitleBar(0, 0, 600, 27);

        UIPane sidebar = new SideBar(0, 28, 138, 372);

        UIPane contentArea = new ContentArea(139, 28, 462, 372);

        graphicsLabel = new GraphicsLabel(background, titlebar, sidebar, contentArea);
    }

}
