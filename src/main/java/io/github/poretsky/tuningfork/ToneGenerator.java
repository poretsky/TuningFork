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
    private float[] waveBuffer;

    private Thread generator;

    private volatile boolean playing;
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
        playing = false;
        fi = 0.0;
        delta = 0.0;
    }

    void start(double freq) {
        delta = DOUBLE_PI * freq / player.getSampleRate();
        if (!playing) {
            fi = 0.0;
            playing = true;
            generator = new Thread(this::generate);
            generator.start();
            player.play();
        }
    }

    void stop() {
        if (playing) {
            playing = false;
            player.pause();
            player.flush();
            try {
                generator.join();
            } catch (InterruptedException ex) {
            }
        }
    }

    boolean isPlaying() {
        return playing;
    }

    private void generate() {
        while (playing) {
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
