/*
 * Copyright (c) 2024 Igor B. Poretsky
 *
 * SPDX-License-Identifier: MIT
 */

package io.github.poretsky.tuningfork;

import android.view.View;

class ClassicGuitar extends ScaleAdapter {

    private static final double[] strings = {
        Scale.E,
        Scale.H / Scale.OCTAVE,
        Scale.G / Scale.OCTAVE,
        Scale.D / Scale.OCTAVE,
        Scale.A / Scale.OCTAVE / Scale.OCTAVE,
        Scale.E / Scale.OCTAVE / Scale.OCTAVE
    };

    private static final String[] items = { "1", "2", "3", "4", "5", "6" };

    private static final int[] descriptions = {
        R.string.first,
        R.string.second,
        R.string.third,
        R.string.fourth,
        R.string.fifth,
        R.string.sixth
    };

    ClassicGuitar(MainActivity activity) {
        super(activity, items, strings, descriptions);
    }

}
