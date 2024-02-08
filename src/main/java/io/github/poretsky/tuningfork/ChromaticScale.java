/*
 * Copyright (c) 2024 Igor B. Poretsky
 *
 * SPDX-License-Identifier: MIT
 */

package io.github.poretsky.tuningfork;

import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

class ChromaticScale extends ScaleAdapter {

    private static final double[] notes = {
        Scale.H,
        Scale.B,
        Scale.A,
        Scale.G_SHARP,
        Scale.G,
        Scale.F_SHARP,
        Scale.F,
        Scale.E,
        Scale.D_SHARP,
        Scale.D,
        Scale.C_SHARP,
        Scale.C
    };

    private static final String[] items = { "H", "B", "A", "G#", "G", "F#", "F", "E", "D#", "D", "C#", "C" };

    private static final int[] descriptions = {
        R.string.H,
        R.string.B,
        R.string.A,
        R.string.G_sharp,
        R.string.G,
        R.string.F_sharp,
        R.string.F,
        R.string.E,
        R.string.D_sharp,
        R.string.D,
        R.string.C_sharp,
        R.string.C
    };


    ChromaticScale(MainActivity activity) {
        super(activity, items, notes, descriptions);
    }

    @Override
    protected void decorateItemView(TextView textView, int position, boolean active) {
        if (position == 1 || position == 3 || position == 5 ||
            position == 8 || position == 10) {
            textView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.black));
            if (active) {
                textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_light));
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_music_note_white, 0);
            } else {
                textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
            }
        } else {
            textView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
            if (active) {
                textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark));
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_music_note_black, 0);
            } else {
                textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
            }
        }
    }

}
