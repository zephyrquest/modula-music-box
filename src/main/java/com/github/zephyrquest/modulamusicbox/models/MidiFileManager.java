package com.github.zephyrquest.modulamusicbox.models;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MidiFileManager {
    private List<File> midiFiles;


    public MidiFileManager() {
        loadMidiFiles();
    }

    public File getMidiFile(String name) {
        return midiFiles
                .stream()
                .filter(file -> file.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public List<File> getMidiFiles() {
        return midiFiles;
    }

    private void loadMidiFiles() {
        Path midiSamplesPath = Paths.get("midi_samples");
        midiFiles = new ArrayList<>();

        if(Files.exists(midiSamplesPath) && Files.isDirectory(midiSamplesPath)) {
            File midiFilesDirectory = midiSamplesPath.toFile();
            File[] files = midiFilesDirectory.listFiles();
            if(files != null) {
                midiFiles.addAll(Arrays.asList(files));
            }
        }
    }
}
