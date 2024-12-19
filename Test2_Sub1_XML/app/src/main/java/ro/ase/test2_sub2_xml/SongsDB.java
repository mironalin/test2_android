package ro.ase.test2_sub2_xml;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Song.class}, version = 1, exportSchema = false)
@TypeConverters({Utils.class})
public abstract class SongsDB extends RoomDatabase {
    public static SongsDB INSTANCE;
    public abstract SongsDAO getSongsDAO();

    public static SongsDB getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SongsDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, SongsDB.class, "songs.db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
