package com.example.musical;

import static com.example.musical.MainActivity.navpausebtn;
import static com.example.musical.MainActivity.path;
import static com.example.musical.MainActivity.signal;
import static com.example.musical.MainActivity.status;
import static com.example.musical.MainActivity.text;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class musical_2 extends AppCompatActivity {
    TextView position;
    TextView endtime;
   static ImageButton button;
    SeekBar seekprog;
    Runnable updater;
    TextView songtitle2 ;
    Handler handler = new Handler();
    static MediaPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musical2);
        button = findViewById(R.id.playbtn);
        songtitle2 = findViewById(R.id.songtitle2);
        songtitle2.setText(text);
        position = findViewById(R.id.position_time);
        endtime = findViewById(R.id.end_time);
        songtitle2.setText(text);
        button.setImageResource(R.drawable.pause);
        player = MediaPlayer.create(this, Uri.parse(path));
            player.start();

        // SEEKBAR

        seekprog = findViewById(R.id.seekprog);
        seekprog.setMax(player.getDuration());

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekprog.setProgress(player.getCurrentPosition());
            }
        }, 0, 900);
        seekprog.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                handler.postDelayed(updater, 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekprog.getProgress());
            }
        });

        //timer

        //current timer

        //runable
        updater = new Runnable() {
            @Override
            public void run() {
                long currentduration = player.getCurrentPosition();
                position.setText(convert(currentduration));
            }
        };

        //endtimer

        int endtimer = Integer.parseInt(String.valueOf(player.getDuration()));
        int hours = endtimer / 1000 / 60 / 60;
        int minute = endtimer / 1000 / 60;
        int second = (endtimer % 60000) / 1000;
        if (hours > 0) {
            endtime.setText(hours + ":" + minute + second);
        }
        if (second <= 9) {
            endtime.setText(String.valueOf(minute + ":0" + second));
        } else {
            endtime.setText(String.valueOf(minute + ":" + second));
        }
//        status();

    }
    //PLAYER

    public void play(View view) {
        status();
    }

    private String convert(long millisecond) {
        String timerstring = "";
        String secondstring;
        int hours = (int) (millisecond / (1000 * 60 * 60));
        int minutes = (int) (millisecond % (1000 * 60 * 60)) / (1000 * 60);
        int second = (int) ((millisecond % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            timerstring = hours + ":";
        }
        if (second < 10) {
            secondstring = "0" + second;
        } else {
            secondstring = "" + second;
        }
        timerstring = timerstring + minutes + ":" + secondstring;
        return timerstring;
    }
public void nextbtn(View v){

}
}
