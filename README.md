# ModulaMusicBox

**ModulaMusicBox** is a Java-based MIDI visualization and playback tool that brings a digital keyboard experience right 
to your screen. Load a MIDI file or select one from the predefined options, start the track, watch the keyboard illuminate 
in real time with each note played, and interact with the interface to play your own notes directly on the keyboard.

## Features

- **MIDI File Loading**: Load MIDI files directly within the app or select one from the available options in the dropdown menu.
- **Real-Time Key Visualization**: Observe which keys are currently being played on a MIDI track.
- **Instrument Channel Selection**: Choose individual instrument channels to focus on the notes of a specific instrument.
- **Interactive Keyboard**: Click on the digital keyboard to play notes manually and interact with the music.
- **Track Controls Configuration**: Use the dropdown menu to change the instrument for a specific channel 
or adjust the beats per minute to alter the track's playback speed. Solo and mute channels to focus on specific instruments.

![ModulaMusicBox](doc/modulamusicbox.png)

## Requirements

- Java 17 or above.

## Installation

1. Clone repository:
    ```bash
    git clone https://github.com/zephyrquest/modula-music-box.git
    ```

2. Move inside root folder:
    ```bash
    cd modula-music-box
    ```

3. Start the application
    ```bash
    java -jar ModulaMusicBox.jar
    ```