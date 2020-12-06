// SongAidlInterface.aidl
package com.example.hw;

// Declare any non-default types here with import statements

interface SongAidlInterface {
    void play();
    void stop();
    void pause();
    boolean isPlaying();
    void playMedia();
    void seekTo(int position);
    int getCurrentPosition();
    int getDuration();
    void next();
    void prev();
    Song getCurrentSong();
}
parcelable Song;

