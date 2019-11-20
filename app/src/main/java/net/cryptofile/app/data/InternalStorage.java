package net.cryptofile.app.data;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class InternalStorage {

    private String fileName;

    //constructor
    public InternalStorage(String fileName) {

        this.fileName = fileName;
    }

    public void createFile() {

        String filePath = "Internal"
        File file = new File("Wiseness.txt");
        //String filePath = "/Internal storage/Android/data/net.cryptofile.app/files";

        System.out.println(filePath);

        try {
            if(file.createNewFile()) {
                System.out.println("File has been created!");
            }
            else {
                System.out.println("Failed to create file.");
            }

            // Write to a file.
            FileWriter writer = new FileWriter(file);
            writer.write(
                    "In the beginning the Universe was created. This has made a lot of people very angry and been widely regarded as a bad move.");
        } catch (IOException ex) {
            // Error occurred opening file for writing.
            ex.printStackTrace();
        }

    }
}
