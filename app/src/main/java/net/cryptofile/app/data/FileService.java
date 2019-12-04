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
import java.util.Iterator;
import java.util.List;

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

    public static void addFile(String id, String title, String filetype) throws Exception {
        FileEntry file = new FileEntry(id, title, filetype);                   // TODO: 02.12.2019 Change this when filetype is implemented
        storeFile(file);
    }

    public static List<FileEntry> getFileList() throws Exception {
        JSONObject json = new JSONObject(response);
        Iterator<String> iterator = json.keys();
        List<FileEntry> reverseList = new ArrayList<>();

        while (iterator.hasNext()) {
            String uuid = iterator.next();
            System.out.println("Readed uuid from json: " + uuid +
                    "\nReaded title: " + json.getJSONArray(uuid).getJSONObject(0).get("title"));

            FileEntry fileEntry = new FileEntry(uuid, json.getJSONArray(uuid).getJSONObject(0).get("title").toString(), null); // TODO: 02.12.2019 Change this when filetype is implemented
            reverseList.add(fileEntry);
        }
        fileList.clear();
        for (int i = reverseList.size(); i-- > 0; ) {
            fileList.add(reverseList.get(i));
        }

        return fileList;
    }

    private static void storeFile(FileEntry file) throws Exception {
        JSONObject json = new JSONObject(response);
        boolean fileExists = json.has(file.getId());

        if (!fileExists) {
            JSONArray fileList = new JSONArray();
            JSONObject fileDetails = new JSONObject();
            fileDetails.put("title", file.getTitle());
            fileDetails.put("type", file.getFileType());
            fileList.put(fileDetails);
            json.put(file.getId(), fileList);

            fileWriter = new FileWriter(new File(storedFilelistFolder).getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json.toString(2));
            bufferedWriter.close();
            readFromStoredFiles();
        } else {
            System.out.println("File already exists");
        }
    }

    public static void delete(String uuid) throws Exception {
        JSONObject json = new JSONObject(response);
        if (json.has(uuid)) {
            json.remove(uuid);

            fileWriter = new FileWriter(new File(storedFilelistFolder).getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json.toString(2));
            bufferedWriter.close();
            readFromStoredFiles();
        } else {
            System.out.println("This uuid does not exist!");
        }
    }
}
