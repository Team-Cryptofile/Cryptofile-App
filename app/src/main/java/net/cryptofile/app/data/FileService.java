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
import java.util.stream.Collectors;

public class FileService {

    private static List<FileEntry> fileList = new ArrayList<>();
    private static String storedFilelistFolder = "/data/data/net.cryptofile.app/filelist.json";
    private static FileReader fileReader = null;
    private static FileWriter fileWriter = null;
    private static BufferedReader bufferedReader = null;
    private static BufferedWriter bufferedWriter = null;
    private static String response = null;

    
    public static void readFromStoredFiles() throws Exception {
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
        boolean fileExists = json.has(file.getId());

        if (!fileExists) {
            JSONArray fileList = new JSONArray();
            JSONObject fileDetails = new JSONObject();
            fileDetails.put("title", file.getTitle());
            fileDetails.put("type", "");
            fileList.put(fileDetails);
            json.put(file.getId(), fileList);

            fileWriter = new FileWriter(new File(storedFilelistFolder).getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json.toString());
            bufferedWriter.close();
            readFromStoredFiles();
        } else {
            System.out.println("File already exists");
        }
    }

    private static void removeDuplicatesFromList(){
        List<FileEntry> unsortedList = fileList;
        fileList = unsortedList.stream().distinct().collect(Collectors.toList());
    }
}
