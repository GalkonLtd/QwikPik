package com.galkonltd.qwikpik.audio;

import com.galkonltd.qwikpik.Utils;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/13/2015
 */
public final class MP3 {

    private String filename;
    private Player player;

    public MP3(String filename) {
        this.filename = filename;
    }

    public void close() {
        if (player != null) {
            player.close();
        }
    }

    public void play(int duration) {
        new Thread() {
            public void run() {
                try {
                    InputStream fis = Utils.resourceToInputStream("sounds/" + filename);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    player = new Player(bis);
                } catch (Exception e) {
                    System.out.println("Problem playing file " + filename);
                    System.out.println(e);
                }
                try {
                    player.play();
                } catch (Exception e) {
                    System.out.println(e);
                }
                try {
                    Thread.sleep(duration * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                player.close();
            }
        }.start();
    }

}
