package com.example.wheeloffortune;

import android.media.MediaPlayer;
import android.media.SoundPool;

import androidx.appcompat.app.AppCompatActivity;

public class Audio extends AppCompatActivity {
    private MediaPlayer audio;



    public Audio (Palabra palabra){
        if(palabra.isMusicapersonalizada() == true){

        }else{
            audioVictoria();
        }

    }

    public void audioVictoria() {
        MediaPlayer win = MediaPlayer.create(this, R.raw.victoriasound);
        win.start();

    }

    public void audioCorrecto() {
        MediaPlayer correct = MediaPlayer.create(this, R.raw.acierto);
        correct.start();
    }

    public void audioIncorrecto() {
        MediaPlayer lose = MediaPlayer.create(this, R.raw.losesound);
        lose.start();
    }

    public void audioGiro() {
        MediaPlayer giro = MediaPlayer.create(this, R.raw.spinningeffect);
        giro.start();
    }

    public void yatraMusic() {
        MediaPlayer yatra = MediaPlayer.create(this, R.raw.yatra);
        yatra.start();
    }
}
