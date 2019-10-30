package net.cryptofile.app.data.model;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class File {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    String id;

    @ColumnInfo(name = "title")
    String title;

    @ColumnInfo(name = "fileType")
    String fileType;

    @ColumnInfo(name = "file")
    byte[] file;

    public static final List<File> FILES = new ArrayList<File>();

    //public File(String id) {
    //    this.id = id;
    //}

    public File(@NonNull String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getFileType() {
        return fileType;
    }

    public byte[] getFile() {
        return file;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     *  This is for making it easier to list files to show on filespage.
     * @param file adds files to a list to show on filespage.
     */
    private static void addItem(File file) {
        FILES.add(file);
    }

}
