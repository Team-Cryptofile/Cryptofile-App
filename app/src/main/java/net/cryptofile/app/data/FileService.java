package net.cryptofile.app.data;

import net.cryptofile.app.data.model.FileEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileService {

    private static List<FileEntry> fileList = new ArrayList<>();
    private static String storedFilelistFolder = "/data/data/net.cryptofile.app/filelist.json";
    private static FileReader fileReader = null;
    private static FileWriter fileWriter = null;
    private static BufferedReader bufferedReader = null;
    private static BufferedWriter bufferedWriter = null;
    private static String response = null;

    
    public static void initialize() throws Exception {
        File file = new File(storedFilelistFolder);
        if(!file.exists() | file.length() == 0){
            file.createNewFile();
            fileWriter = new FileWriter(file.getAbsoluteFile());
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("{}");
            bufferedWriter.close();
        }


        StringBuffer output = new StringBuffer();
        fileReader = new FileReader(file);
        bufferedReader = new BufferedReader(fileReader);
        String line;

        while ((line = bufferedReader.readLine()) != null){
            output.append(line).append("\n");
        }

        response = output.toString();
        bufferedReader.close();
    }

    public static void addFile(String id, String title) throws Exception {
        FileEntry file = new FileEntry(id, title);
        fileList.add(file);
        storeFile(file);
    }

    public static List<FileEntry> getFileList(){
        removeDuplicatesFromList();
        return fileList;
    }

    private static void storeFile(FileEntry file) throws Exception {
        JSONObject json = new JSONObject(response);
        JSONObject fileInJson = new JSONObject();
        JSONArray fileList = new JSONArray();

        fileInJson.put("UUID", file.getId());
        fileInJson.put("title", file.getTitle());
        fileInJson.put("type", "");

        fileList.put(fileInJson);
        json.put("Files", fileList); // TODO: 02.12.2019 Make sure objects are added and not overwritten.

        fileWriter = new FileWriter(new File(storedFilelistFolder).getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(fileInJson.toString());
        bufferedWriter.close();
    }

    private static void removeDuplicatesFromList(){
        List<FileEntry> unsortedList = fileList;
        fileList = unsortedList.stream().distinct().collect(Collectors.toList());
    }
}
