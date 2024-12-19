package ro.ase.test2_sub1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {
    TextView tvSongTitle, tvArtist, tvNoOfViews, tvSongReleaseDate;
    private int resource;
    private List<Song> objects;
    private LayoutInflater inflater;

    public SongAdapter(@NonNull Context context, int resource, @NonNull List<Song> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.resource = resource;
        this.objects = objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = inflater.inflate(this.resource, parent, false);
        initVisualComponents(row);
        Song song = objects.get(position);
        populateVisualComponents(song);

        return row;
    }

    private void initVisualComponents(View row) {
        tvSongTitle = row.findViewById(R.id.tvSongTitle);
        tvArtist = row.findViewById(R.id.tvArtist);
        tvNoOfViews = row.findViewById(R.id.tvNoOfViews);
        tvSongReleaseDate = row.findViewById(R.id.tvSongReleaseDate);
    }

    private void populateVisualComponents(Song song) {
        tvSongTitle.setText(song.getSongTitle());
        tvArtist.setText(song.getArtist());
        tvNoOfViews.setText(String.valueOf(song.getNoOfViews()));
        tvSongReleaseDate.setText(Utils.fromDate(song.getSongReleaseDate()));
    }
}
