package ro.ase.test2_sub1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongsDAO {
    @Insert
    void insertSong(Song song);

    @Query("SELECT * FROM SONGS")
    List<Song> getSongs();

    @Query("SELECT * FROM SONGS WHERE noOfViews > 3000")
    List<Song> getGrossingSongs();
}
