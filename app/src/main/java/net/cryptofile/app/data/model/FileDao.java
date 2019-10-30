package net.cryptofile.app.data.model;

import java.util.List;
import java.util.UUID;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FileDao {
    @Query("SELECT * FROM File")
    List<File> getAll();

    @Query("SELECT * FROM file WHERE id IN (:fileIds)")
    List<File> loadAllByIds(String fileIds);


    @Insert
    void insertAll(File... files);

    @Delete
    void delete(File file);
}
