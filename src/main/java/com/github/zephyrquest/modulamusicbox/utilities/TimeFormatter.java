package com.github.zephyrquest.modulamusicbox.utilities;

public class TimeFormatter {
    public String format(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        return String.format("%d:%02d", minutes, seconds);
    }
}
