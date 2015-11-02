package com.galkonltd.qwikpik.task.impl;

import com.galkonltd.qwikpik.Utils;
import com.nova.task.Task;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/15/2015
 */
public class OpenUrlTask extends Task {

    private final String url;

    public OpenUrlTask(String url) {
        this.url = url;
    }

    @Override
    public boolean execute() {
        Utils.launchBrowser(this.url);
        return true;
    }

}
