package net.cryptofile.app.data;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ServerDataSource {

    public Result uploadFile(byte[] file, String title, String filetype) {
        HttpURLConnection c = null;

        try {
            URL url = new URL("http://www.cryptofile.net:8080/add");
            String boundary = UUID.randomUUID().toString();
            c = (HttpURLConnection) url.openConnection();

            c.setDoOutput(true);
            c.setRequestMethod("POST");
            c.setRequestProperty("Content-Type", "multipart/form-data;boundary=----WebKitFormBoundary" + boundary);

            DataOutputStream request = new DataOutputStream(c.getOutputStream());

            // File
            request.writeBytes("------WebKitFormBoundary" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"file\"\r\n\r\n");
            request.write(file);
            request.writeBytes( "\r\n");

            // Title
            request.writeBytes("------WebKitFormBoundary" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"title\"\r\n\r\n");
            request.writeBytes(title + "\r\n");

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
