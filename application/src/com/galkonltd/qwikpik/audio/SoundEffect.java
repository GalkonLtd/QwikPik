package com.galkonltd.qwikpik.audio;

import com.galkonltd.qwikpik.Settings;

public enum SoundEffect {

        SHUTTER("shutter.mp3", 3),
        SUCCESS("success.mp3", 3),
        FAIL("fail.mp3", 3),
        ;

        private final MP3 mp3;
        private final int duration;

        SoundEffect(String file, int duration) {
            this.mp3 = new MP3(file);
            this.duration = duration;
        }

        public void play() {
            if (!Settings.NOTIFICATION_SOUNDS.isEnabled()) {
                return;
            }
            this.mp3.play(this.duration);
        }
    }