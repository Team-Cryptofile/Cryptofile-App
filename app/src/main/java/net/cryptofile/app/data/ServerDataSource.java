package net.cryptofile.app.data;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ServerDataSource {

    public Result uploadFile(File file, String title, String filetype) {
        HttpURLConnection c = null;

        try {
            URL url = new URL("http://www.cryptofile.net:8080/add");
            String boundary = UUID.randomUUID().toString();
            c = (HttpURLConnection) url.openConnection();

            c.setDoOutput(true);
            c.setRequestMethod("POST");
            c.setRequestProperty("Content-Type", "multipart/form-data;charset=UTF-8;boundary=----WebKitFormBoundary" + boundary);

            DataOutputStream request = new DataOutputStream(c.getOutputStream());

            // Title
            request.writeBytes("------WebKitFormBoundary" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"title\"\r\n");
            request.writeBytes("Content-Type: text/plain\r\n\r\n");
            request.writeBytes(title + "\r\n");

            // File
            request.writeBytes("------WebKitFormBoundary" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"binary\"\r\n");
            request.writeBytes("Content-Type: application/octet-stream\r\n\r\n");


            // Sending file as buffered bytes
            FileInputStream fileInputStream = new FileInputStream(file);
            int availableBytes = fileInputStream.available();
            int maxBufferSize = 4 * 1024;
            int bufferSize = Math.min(availableBytes, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int readBytes = fileInputStream.read(buffer, 0, bufferSize);
            while (readBytes > 0){
                request.write(buffer, 0, bufferSize);
                availableBytes = fileInputStream.available();
                bufferSize = Math.min(availableBytes,maxBufferSize);
                readBytes = fileInputStream.read(buffer, 0, bufferSize);
            }

            request.writeBytes( "\r\n");

            request.writeBytes("------WebKitFormBoundary" + boundary + "--\r\n");
            request.flush();

            if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8));
                String response = bufferedReader.readLine();
                System.out.println(response);
                c.getInputStream().close();
                return new Result.Success<>(response);
            }
            return new Result.Error(new IOException("Error uploading file: " + c.getResponseMessage()));
        } catch (Exception e) {
            System.err.println("Failed to call "+ e);
            return new Result.Error(new IOException("Error creating item", e));
        } finally {
            if(c != null) c.disconnect();
        }
    }
}
