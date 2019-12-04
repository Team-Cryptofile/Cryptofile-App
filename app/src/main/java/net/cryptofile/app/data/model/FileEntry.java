package net.cryptofile.app.data.model;

import net.cryptofile.app.data.CryptoService;

public class FileEntry {

    private String id = null;
    private String title = null;
    private String fileType = null;

    public FileEntry(String id, String title, String fileType) {

        this.id = id;
        this.title = title;
        this.fileType = fileType;

    }


    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getFileType() {
        if (fileType != null) {
            return fileType;
        } else {
            return "";
        }
    }

    public boolean hasKey() throws Exception {
        return CryptoService.getKey(id) != null;
    }
}