package net.cryptofile.app.ui.fileDownload;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import net.cryptofile.app.R;
import net.cryptofile.app.data.CryptoService;
import net.cryptofile.app.data.FileService;

import org.apache.tika.io.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DownloadDialog extends AppCompatDialogFragment {

    private EditText editUuid;
    private DownloadDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_download_dialog, null);

        builder.setView(view)
                .setTitle("Download a file")
                .setNegativeButton("cancel", (dialog, which) -> {

                })
                .setPositiveButton("confirm", (dialog, which) -> {
                    String uuid = editUuid.getText().toString();
                    listener.applyText(uuid);
                    downloadFile(uuid);
                });

        editUuid = view.findViewById(R.id.edit_uuid);
        return builder.create();
    }


    public void downloadFile(String uuidInput) {
        boolean isOnlyUuid = uuidInput.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        boolean isUuidWithKey= uuidInput.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}:.+=$");
        Thread thread = new Thread(() -> {
            try {
                String urlPath = "http://cryptofile.net:8080/get/";
                if(isOnlyUuid) {    // This runs if UUID is valid but have no key
                    URL url = new URL(urlPath + uuidInput);

                    // Grabbing file title from server
                    String title = getDetailFromServer("title", uuidInput);

                    // Grabbing file filetype from server
                    String filetype = getDetailFromServer("filetype", uuidInput);

                    FileService.addFile(uuidInput, title, filetype);
                } else if (isUuidWithKey) { // This runs if there is a UUID and a key attached to it
                    CryptoService.saveKey(uuidInput);

                    String[] split = uuidInput.split(":", 2);
                    String uuid = split[0];


                    // Grabbing file title from server
                    String title = getDetailFromServer("title", uuid);

                    // Grabbing file filetype from server
                    String filetype = getDetailFromServer("filetype", uuid);

                    // Adds file to filelist
                    FileService.addFile(uuid, title, filetype);

                    // Getting the file itself
                    URL url = new URL(urlPath + uuid);
                    InputStream in = url.openStream();
                    Path filePath =  Paths.get(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), uuid + "." + filetype);

                    byte[] decryptedBytes = CryptoService.decrypt(CryptoService.getKey(uuid), IOUtils.toByteArray(in));
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedBytes);
                    Files.copy(byteArrayInputStream, filePath);
                    in.close();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DownloadDialogListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DownloadDialogListener");
        }

    }

    public interface DownloadDialogListener {
        void applyText(String uuid);
    }

    public String getDetailFromServer(String detail, String uuid) throws Exception {
        String urlPath = "http://cryptofile.net:8080/get/";
        String detailString = "";
        URL urlFiletype = new URL(urlPath + detail + "/" + uuid);
        System.out.println("URL: " + urlFiletype);
        HttpURLConnection c = (HttpURLConnection) urlFiletype.openConnection();
        c.setRequestMethod("GET");
        if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            detailString = content.toString();
            System.out.println("Recieved " + detail + ": " + detailString);
        } else {
            System.out.println("Could not get the " + detail + ": " + c.getErrorStream());
        }
        c.disconnect();
        return detailString;
    }
}
