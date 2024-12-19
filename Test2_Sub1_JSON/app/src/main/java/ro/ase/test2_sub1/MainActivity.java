package ro.ase.test2_sub1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    public final static String songsURL= "https://pastebin.com/raw/vkXBEwnG";
    private List<Song> songs;
    Button btnSaveData, btnSyncData, btnViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();

        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongsDAO dao = SongsDB.getInstance(getApplicationContext()).getSongsDAO();
                for (Song song : songs) {
                    dao.insertSong(song);
                }
                Toast.makeText(getApplicationContext(), "Database successfully inserted", Toast.LENGTH_LONG).show();
            }
        });

        btnSyncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService threadPool = Executors.newCachedThreadPool();
                Future<List<Song>> future = threadPool.submit(new Callable<List<Song>>() {
                    @Override
                    public List<Song> call() throws Exception {
                        List<Song> songs = new ArrayList<>();
                        StringBuilder json = new StringBuilder();
                        HttpURLConnection conn = (HttpURLConnection) new URL(songsURL).openConnection();
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                            String line;
                            while((line = br.readLine()) != null) {
                                json.append(line);
                            }
                        }
                        conn.disconnect();

                        if (json != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(json.toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("songs");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String songTitle = obj.getString("songTitle");
                                    String artist = obj.getString("artist");
                                    int noOfViews = obj.getInt("noOfViews");
                                    Date songReleaseDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(obj.getString("songReleaseDate"));

                                    songs.add(new Song(songTitle, artist, noOfViews, songReleaseDate));
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        return songs;
                    }
                });
                try {
                    threadPool.shutdown();
                    songs = future.get();
                    if (songs.size() == 4) {
                        Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "FAILURE", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewDataActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initComponents() {
        songs = new ArrayList<>();
        btnSaveData = findViewById(R.id.btnSaveData);
        btnSyncData = findViewById(R.id.btnSyncData);
        btnViewData = findViewById(R.id.btnViewData);
    }
}