package com.bloomberryspecial;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DrawStyle {
    LINE(true, false),
    POINTS(false, true),
    BOTH(true, true);

    boolean linesEnabled;
    boolean pointsEnabled;
}
