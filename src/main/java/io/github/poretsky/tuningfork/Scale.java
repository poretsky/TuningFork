/*
 * Copyright (c) 2024 Igor B. Poretsky
 *
 * SPDX-License-Identifier: MIT
 */

package io.github.poretsky.tuningfork;

class Scale {

    // Intervals
    static final double OCTAVE = 2.0;
    static final double TRITONE = Math.sqrt(OCTAVE);
    static final double MAJOR_THIRD = Math.cbrt(OCTAVE);
    static final double MINOR_THIRD = Math.sqrt(TRITONE);

    // Notes
    static final double A = 440.0;
    static final double E_FLAT = A / TRITONE;
    static final double D_SHARP = E_FLAT;
    static final double F = A / MAJOR_THIRD;
    static final double E_SHARP = F;
    static final double F_SHARP = A / MINOR_THIRD;
    static final double G = E_FLAT * MAJOR_THIRD;
    static final double G_SHARP = F * MINOR_THIRD;
    static final double A_FLAT = G_SHARP;
    static final double D = F / MINOR_THIRD;
    static final double C = A * MINOR_THIRD / OCTAVE;
    static final double C_SHARP = A * MAJOR_THIRD / OCTAVE;
    static final double D_FLAT = C_SHARP;
    static final double E = C * MAJOR_THIRD;
    static final double B = F_SHARP * MAJOR_THIRD;
    static final double H_FLAT = B;
    static final double A_SHARP = B;
    static final double H = E_FLAT * OCTAVE / MAJOR_THIRD;
    static final double H_SHARP = C * OCTAVE;

}
