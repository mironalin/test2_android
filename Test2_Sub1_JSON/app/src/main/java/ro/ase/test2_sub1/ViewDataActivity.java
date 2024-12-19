package ro.ase.test2_sub1;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class ViewDataActivity extends AppCompatActivity {
    Button btnSort;
    ListView lvSongs;
    List<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        initComponents();
        SongsDAO dao = SongsDB.getInstance(getApplicationContext()).getSongsDAO();

        songs = dao.getSongs();

        ArrayAdapter<Song> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, songs);
        lvSongs.setAdapter(adapter);

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songs.clear();
                List<Song> newSongs = dao.getGrossingSongs();
                Toast.makeText(getApplicationContext(), "got " + new Integer(songs.size()).toString() + " songs, views > 3000", Toast.LENGTH_LONG).show();
                songs.addAll(newSongs);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initComponents() {
        btnSort = findViewById(R.id.btnSort);
        lvSongs = findViewById(R.id.lvSongs);
    }
}