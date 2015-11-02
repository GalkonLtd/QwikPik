package com.galkonltd.qwikpik.audio;

import com.nova.task.Task;
import com.nova.task.TaskManager;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/14/2015
 */
public final class SoundPlayer {

    public static void play(SoundEffect effect) {
        TaskManager.submitTask(new Task() {
            @Override
            public boolean execute() {
                effect.play();
                return true;
            }
        });
    }

}
