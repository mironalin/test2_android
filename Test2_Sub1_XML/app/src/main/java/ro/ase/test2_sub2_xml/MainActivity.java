package ro.ase.test2_sub2_xml;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    public final static String songsURL= "https://pastebin.com/raw/UKZDEH6L";
    private List<Song> songs;
    Button btnSaveData, btnSyncData, btnViewData;
    private Song song;
    private String text;
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
                Toast.makeText(getApplicationContext(), R.string.database_inserted, Toast.LENGTH_LONG).show();
            }
        });

        btnSyncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService executorService = Executors.newCachedThreadPool();
                Future<List<Song>> future = executorService.submit(new Callable<List<Song>>() {
                    @Override
                    public List<Song> call() throws Exception {
                        List<Song> songs = new ArrayList<>();
                        StringBuilder result = new StringBuilder();
                        HttpURLConnection conn = (HttpURLConnection) new URL(songsURL).openConnection();
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                result.append(line);
                            }
                        }

                        conn.disconnect();

                        try {
                            XmlPullParser parser = Xml.newPullParser();
                            parser.setInput(new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8)), "utf-8");
                            int eventType = parser.getEventType();

                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                String tagName = parser.getName();
                                switch (eventType) {
                                    case XmlPullParser.START_TAG:
                                        if (tagName.equalsIgnoreCase("Song")) {
                                            song = new Song();
                                        }
                                        break;
                                    case XmlPullParser.TEXT:
                                        text = parser.getText();
                                        break;
                                    case XmlPullParser.END_TAG:
                                        if (tagName.equalsIgnoreCase("songTitle")) {
                                            song.setSongTitle(text);
                                        } else if (tagName.equalsIgnoreCase("artist")) {
                                            song.setArtist(text);
                                        } else if (tagName.equalsIgnoreCase("noOfViews")) {
                                            song.setNoOfViews(Integer.parseInt(text));
                                        } else if (tagName.equalsIgnoreCase("songReleaseDate")) {
                                            song.setSongReleaseDate(Utils.fromString(text));
                                        } else if (tagName.equalsIgnoreCase("Song")) {
                                            songs.add(song);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                try {
                                    eventType = parser.next();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } catch (XmlPullParserException e) {
                            throw new RuntimeException(e);
                        }
                        return songs;
                    }
                });
                try {
                    executorService.shutdown();
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