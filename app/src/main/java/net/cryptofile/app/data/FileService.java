package net.cryptofile.app.data;

import net.cryptofile.app.data.model.FileEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileService {

    private static List<FileEntry> fileList = new ArrayList<>();

    public FileService() {
    }

    public void addFile(String id, String title) {
        FileEntry file = new FileEntry(id, title);
        fileList.add(file);
        storeFile(file);
    }

    public static List<FileEntry> getFileList(){
        removeDuplicatesFromList();
        return fileList;
    }

    private void storeFile(FileEntry file){
        // TODO: 01.12.2019 Pick files from filestorage (file information, not file content)
    }

    private static void removeDuplicatesFromList(){
        List<FileEntry> unsortedList = fileList;
        fileList = unsortedList.stream().distinct().collect(Collectors.toList());
    }
}
