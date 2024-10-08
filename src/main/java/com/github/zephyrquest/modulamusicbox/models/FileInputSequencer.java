package com.github.zephyrquest.modulamusicbox.models;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileInputSequencer {
    private Sequencer sequencer;
    private Transmitter transmitter;
    private Sequence currentSequence;
    private List<File> midiFiles;


    public FileInputSequencer() {
        initSequencer();
        loadMidiFiles();
    }

    public void startSequencer() {
        if(sequencer.isOpen() && sequencer.getSequence() != null) {
            sequencer.start();
        }
    }

    public void stopSequencer() {
        if(sequencer.isOpen()) {
            sequencer.stop();
        }
    }

    public void rewindSequencer() {
        if(sequencer.isOpen()) {
            sequencer.setTickPosition(0);
        }
    }

    public void setSequence(String fileName) {
        var midiFileOpt = midiFiles
                .stream()
                .filter(midiFile -> midiFile.getName().equals(fileName))
                .findFirst();

        if(midiFileOpt.isEmpty()) {
            return;
        }

        try {
            currentSequence = MidiSystem.getSequence(midiFileOpt.get());
            sequencer.setSequence(currentSequence);
        } catch (InvalidMidiDataException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeSequencer() {
        sequencer.close();
    }

    public Transmitter getTransmitter() {
        return transmitter;
    }

    public List<File> getMidiFiles() {
        return midiFiles;
    }

    private void initSequencer() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            transmitter = sequencer.getTransmitter();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
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
