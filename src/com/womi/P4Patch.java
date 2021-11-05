package com.womi;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class P4Patch {
    private final String fileName;
    private final StringBuilder stringBuilder;
    private final Change currentChange;

    public P4Patch(String fileName) {
        this.fileName = fileName;
        this.currentChange = new Change();
        this.stringBuilder = new StringBuilder();
    }

    private void processControlLine(String line) {
        Line controlLine = new Line(line);
        if (controlLine.isOld()) {
            if (currentChange.getOld() != null) {
                log("Old change already set! Source file might be corrupt");
            }
            currentChange.setOld(controlLine);
        }
        if (controlLine.isCurrent()) {
            if (currentChange.getCurrent() != null) {
                log("Current change already set! Source file might be corrupt");
            }
            currentChange.setCurrent(controlLine);
        }
        if (currentChange.isComplete()) {
            copyOldChangeBodyToCurrentOneOnEdit();
            appendChange();
            currentChange.reset();
        }
    }

    private void copyOldChangeBodyToCurrentOneOnEdit() {
        if (currentChange.isEdit()) {
            currentChange.getCurrent().setBody(currentChange.getOld().getBody());
        }
    }

    private void appendChange() {
        stringBuilder.append(currentChange.getOld().toString());
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(currentChange.getCurrent().toString());
        stringBuilder.append(System.lineSeparator());
    }

    private void processCodeLine(String line) {
        stringBuilder.append(line);
        stringBuilder.append(System.lineSeparator());
    }

    private void processInputFile() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (Line.isControlLine(line)) {
                    processControlLine(line);
                } else {
                    processCodeLine(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeResultFile();
    }

    private void writeResultFile() {
        String resultFileName = fileName + ".patch";
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFileName), StandardCharsets.UTF_8))) {
            writer.write(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        log("Successfully written file " + resultFileName);
    }

    public static void main(String[] args) {
        P4Patch engine = new P4Patch(args[0]);
        engine.processInputFile();
    }

    private void log(String message) {
        System.out.println(message);
    }
}
