/*
 * Copyright (c) 2024 Igor B. Poretsky
 *
 * SPDX-License-Identifier: MIT
 */

package io.github.poretsky.tuningfork;

import android.view.View;

class RussianGuitar extends ScaleAdapter {

    private static final double[] strings = {
        Scale.D,
        Scale.H / Scale.OCTAVE,
        Scale.G / Scale.OCTAVE,
        Scale.D / Scale.OCTAVE,
        Scale.H / Scale.OCTAVE / Scale.OCTAVE,
        Scale.G / Scale.OCTAVE / Scale.OCTAVE,
        Scale.D / Scale.OCTAVE / Scale.OCTAVE
    };

    private static final String[] items = { "1", "2", "3", "4", "5", "6", "7" };

    private static final int[] descriptions = {
        R.string.first,
        R.string.second,
        R.string.third,
        R.string.fourth,
        R.string.fifth,
        R.string.sixth,
        R.string.seventh
    };

    RussianGuitar(MainActivity activity) {
        super(activity, items, strings, descriptions);
    }

}
