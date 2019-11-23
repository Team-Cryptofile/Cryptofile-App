package net.cryptofile.app.data.model;

import java.io.File;

public class FileEntry {

    private String id = null;
    private String title = null;
    private String fileType = null;
    private File file;

    public FileEntry(String id, String title) {

        this.id = id;
        this.title = title;

    }


    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getFileType() {
        return this.getFileType();
    }
}