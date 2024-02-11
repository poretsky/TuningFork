/*
 * Copyright (c) 2024 Igor B. Poretsky
 *
 * SPDX-License-Identifier: MIT
 */

package io.github.poretsky.tuningfork;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

class ToneGenerator {

    private static final double DOUBLE_PI = 2.0 * Math.PI;

    private final AudioTrack player;
    private final float[] waveBuffer;

    private Thread generator;

    private volatile int playing;
    private volatile double fi;
    private volatile double delta;


    ToneGenerator() {
        player = new AudioTrack.Builder()
            .setAudioAttributes(new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build())
            .setAudioFormat(new AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
                            .setSampleRate(AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC))
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build())
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build();
        waveBuffer = new float[player.getBufferSizeInFrames()];
        playing = 0;
        fi = 0.0;
        delta = 0.0;
        generator = new Thread(this::generate);
        generator.start();
        player.play();
    }

    void start(double freq) {
        delta = DOUBLE_PI * freq / player.getSampleRate();
        if (playing < 0) {
            fi = 0.0;
            playing = 0;
            generator = new Thread(this::generate);
            generator.start();
            player.play();
        }
        playing = 1;
        player.setVolume(1);
    }

    void mute() {
        player.setVolume(0);
        playing = 0;
        delta = 0.0;
        fi = 0.0;
    }

    void stop() {
        if (playing >= 0) {
            player.setVolume(0);
            playing = -1;
            player.pause();
            player.flush();
            try {
                generator.join();
            } catch (InterruptedException ex) {
            }
        }
    }

    boolean isPlaying() {
        return playing > 0;
    }

    private void generate() {
        while (playing >= 0) {
            for (int i = 0; i < waveBuffer.length; i++) {
                waveBuffer[i] = (float) Math.sin(fi);
                fi += delta;
                if (fi > DOUBLE_PI)
                    fi -= DOUBLE_PI;
            }
            player.write(waveBuffer, 0, waveBuffer.length, AudioTrack.WRITE_BLOCKING);
        }
    }

}
