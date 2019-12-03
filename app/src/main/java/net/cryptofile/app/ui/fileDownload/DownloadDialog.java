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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
                    String title = "";
                    URL urlTitle = new URL(urlPath + "title/" + uuidInput);
                    HttpURLConnection c = (HttpURLConnection) urlTitle.openConnection();
                    if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        title = c.getResponseMessage();
                    } else {
                        System.out.println("Could not get the title: " + c.getResponseMessage());
                    }
                    c.disconnect();

                    FileService.addFile(uuidInput, title);
                } else if (isUuidWithKey) { // This runs if there is a UUID and a key attached to it
                    CryptoService.saveKey(uuidInput);

                    String[] split = uuidInput.split(":", 2);
                    String uuid = split[0];


                    // Grabbing file title from server
                    String title = "";
                    URL urlTitle = new URL(urlPath + "title/" + uuid);
                    System.out.println("URL: " + urlTitle);
                    HttpURLConnection c = (HttpURLConnection) urlTitle.openConnection();
                    if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        title = c.getResponseMessage();
                        System.out.println("Recieved title: " + title);
                    } else {
                        System.out.println("Could not get the title: " + c.getResponseMessage());
                    }
                    c.disconnect();

                    // Adds file to filelist
                    FileService.addFile(uuid, title);

                    // Getting the file itself
                    URL url = new URL(urlPath + uuid);
                    InputStream in = url.openStream();
                    Path filePath =  Paths.get(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), uuid);

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
}
