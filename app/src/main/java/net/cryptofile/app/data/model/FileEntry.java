package net.cryptofile.app.data.model;

import android.content.Context;

import java.io.File;

public class FileEntry {

    private String id = null;
    private String title = null;
    private String fileType = null;
    private File file;

    public FileEntry(String id, String title, String fileType, File file) {

        this.id = id;
        this.title = title;
        this.fileType = fileType;
        this.file = file;

    }

    public void write() {
        Context context = null;
        System.out.println(context.getFilesDir());
    }

    public void create() {

    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }
}



