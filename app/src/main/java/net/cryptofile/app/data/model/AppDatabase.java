package net.cryptofile.app.data.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {File.class}, version = 1, exportSchema = false) // TODO: 29.10.2019  set the exportSchema to true
public abstract class AppDatabase extends RoomDatabase {
    public abstract FileDao fileDao();
}

//// === Use this to create an instance of the database ===
// AppDatabase db = Room.databaseBuilder(getApplicationContext(),
//        AppDatabase.class, "database-name").build();