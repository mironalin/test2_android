package ro.ase.test2_sub2_xml;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "songs")
public class Song implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String songTitle;
    private String artist;
    private int noOfViews;
    private Date songReleaseDate;

    public Song(String songTitle, String artist, int noOfViews, Date songReleaseDate) {
        setSongTitle(songTitle);
        setArtist(artist);
        setNoOfViews(noOfViews);
        setSongReleaseDate(songReleaseDate);
    }

    public Song() {}



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        if (songTitle == null) {
            throw new RuntimeException("Song title can't be null");
        }
        this.songTitle = songTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getNoOfViews() {
        return noOfViews;
    }

    public void setNoOfViews(int noOfViews) {
        if (noOfViews <= 0) {
            throw new RuntimeException("No of views must be > 0");
        }
        this.noOfViews = noOfViews;
    }

    public Date getSongReleaseDate() {
        return songReleaseDate;
    }

    public void setSongReleaseDate(Date songReleaseDate) {
        this.songReleaseDate = songReleaseDate;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songTitle='" + songTitle + '\'' +
                ", artist='" + artist + '\'' +
                ", noOfViews=" + noOfViews +
                ", songReleaseDate=" + songReleaseDate +
                '}';
    }
}
