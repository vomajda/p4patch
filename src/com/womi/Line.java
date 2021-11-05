package com.womi;

import java.util.Arrays;

public class Line {
    public static final String PREFIX_OLD = "---";
    public static final String PREFIX_NEW = "+++";
    public static final String NULL = "/dev/null";

    private String[] line;

    public Line(String line) {
        this.line = line.split("\\s+");
    }

    public String getPrefix() {
        return line[0];
    }

    public String getBody() {
        return line[1];
    }

    public void setBody(String body) {
        line[1] = body;
    }

    public boolean isOld() {
        return getPrefix().equals(PREFIX_OLD);
    }

    public boolean isCurrent() {
        return getPrefix().equals(PREFIX_NEW);
    }

    @Override
    public String toString() {
        return String.join(" ", line);
    }

    public static boolean isControlLine(String line) {
        if (line.length() < 3) {
            return false;
        }
        String firstThreeCharsOfLine = line.substring(0, 3);
        return firstThreeCharsOfLine.equals(PREFIX_OLD) || firstThreeCharsOfLine.equals(PREFIX_NEW);
    }
}
