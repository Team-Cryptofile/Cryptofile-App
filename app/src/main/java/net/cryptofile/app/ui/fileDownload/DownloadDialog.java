package net.cryptofile.app.ui.fileDownload;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.style.TabStopSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import net.cryptofile.app.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String uuid = editUuid.getText().toString();
                        listener.applyText(uuid);
                        downloadFile(uuid);
                    }
                });

        editUuid = view.findViewById(R.id.edit_uuid);

        String uuid = editUuid.getText().toString();
        downloadFile(uuid);
        return builder.create();
    }


    public void downloadFile(String uuid) {

        try {
            String urlPath = "cryptofile.net:8080/get/";
            if(uuid != null) {
                URL url = new URL(urlPath + uuid);
                InputStream in = url.openStream();
                //FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
                Files.copy(in, Paths.get(uuid), StandardCopyOption.REPLACE_EXISTING);
                in.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

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
