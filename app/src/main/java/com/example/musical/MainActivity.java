package com.example.musical;

import static com.example.musical.musical_2.button;
import static com.example.musical.musical_2.player;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String items[];
    String items2[];
    String items3[];
    String currentTitle;
    Boolean set=false;
    String currentArtist;
    static ImageButton navpausebtn;
    TextView navsongtitle;
    String currentData;
    RelativeLayout relativeclic1;
    static int signal = 0;
    static String text;
    static String path;
    ArrayList<String> arraylist = new ArrayList<String>();
    ArrayList<String> currentartist = new ArrayList<String>();
    ArrayList<String> currentdata = new ArrayList<String>();

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navpausebtn = findViewById(R.id.navplaybtn);
        navsongtitle = findViewById(R.id.navsongtitle);
        runtime();
         relativeclic1 = (RelativeLayout) findViewById(R.id.RelativeMain1);
        relativeclic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, musical_2.class);
                startActivity(intent);
            }
        });
    }
    public void runtime() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        dostuff();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public void getmusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int song = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                currentTitle = songCursor.getString(songTitle);
                currentArtist = songCursor.getString(songArtist);
                currentData = songCursor.getString(song);

                arraylist.add(currentTitle);
                currentartist.add(currentArtist);
                currentdata.add(currentData);
            } while (songCursor.moveToNext());
        } else {
            Toast.makeText(MainActivity.this, "No Songs", Toast.LENGTH_LONG).show();
        }
        items = new String[arraylist.size()];
        for (int i = 0; i < arraylist.size(); i++) {
            items[i] = arraylist.get(i);
        }
        items2 = new String[currentartist.size()];

        for (int i = 0; i < currentartist.size(); i++) {
            items2[i] = currentartist.get(i);
        }

        items3 = new String[currentdata.size()];

        for (int i = 0; i < currentdata.size(); i++) {
            items3[i] = currentdata.get(i);
        }
    }

    public void dostuff() {
        listView = findViewById(R.id.listv);
        getmusic();
        myadapter adapt = new myadapter(this, items, items2, items3);
        listView.setAdapter(adapt);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                    text = String.valueOf(items[position]);
                    path = String.valueOf(items3[position]);
                    navsongtitle.setText(text);
                    navpausebtn.setImageResource(R.drawable.pause);
                    Intent intent = new Intent(MainActivity.this, musical_2.class);
                    startActivity(intent);
                    relativeclic1.setVisibility(View.VISIBLE);
                    if (signal == 0) {
                        signal = 1;
                    } else if (signal == 1) {
                        signal = 0;
                    }
            }
        });
    }

    class myadapter extends ArrayAdapter<String> {
        Context context;
        String items[];
        String items2[];
        String items3[];

        myadapter(Context c, String[] items, String[] items2, String[] items3) {
            super(c, R.layout.itemlist, R.id.songtext, items);
            context = c;
            this.items = items;
            this.items2 = items2;
            this.items3 = items3;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.itemlist, parent, false);

            TextView tv1 = row.findViewById(R.id.songtext);
            TextView tv2 = row.findViewById(R.id.Artist);

            tv1.setText(items[position]);
            tv2.setText(items2[position]);

            return row;
        }
    }
    public void navpause(View view) {
        status();
    }

    public static void status(){
        if (signal == 0) {
            player.start();
            signal = 1;
            navpausebtn.setImageResource(R.drawable.pause);
            button.setImageResource(R.drawable.pause);
        } else if (signal == 1) {
            // handler.removeCallbacks(updater);
            player.pause();
            signal = 0;
            navpausebtn.setImageResource(R.drawable.play2);
            button.setImageResource(R.drawable.play2);
        }else{
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                player.pause();
                signal = 0;
                navpausebtn.setImageResource(R.drawable.play2);
                button.setImageResource(R.drawable.play2);
            }
        });
        }
    }
}