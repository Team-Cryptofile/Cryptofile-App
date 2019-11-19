package net.cryptofile.app.ui.fileupload;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import net.cryptofile.app.R;
import net.cryptofile.app.data.MainRepository;
import net.cryptofile.app.data.Result;

import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;

public class FileUploadActivity extends AppCompatActivity {

    private MainRepository mainRepository;
    private static final int REQUEST_GET_SINGLE_FILE= 1;

    String fileLocationString;
    byte[] fileAsBytes = null;
    TextView fileLocationText;
    TextView detectedFiletypeText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_upload_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FileUploadFragment.newInstance())
                    .commitNow();
        }

        final Button selectFilebutton = findViewById(R.id.selectUploadFilebutton);
        final TextInputEditText titleInput = findViewById(R.id.textInputEditText);
        final TextView fileLocationText = findViewById(R.id.textViewFilelocation);
        final TextView filetypeLabel = findViewById(R.id.textViewFiletypeLabel);
        final TextView detectedFiletypeText = findViewById(R.id.textViewDetectedFileType);
        final Button submitBtn = findViewById(R.id.uploadSubmitBtn);

        selectFilebutton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_GET_SINGLE_FILE);
        });

        submitBtn.setOnClickListener(v -> {
            submitFile(fileAsBytes, titleInput.getText().toString(), detectedFiletypeText.getText().toString());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_GET_SINGLE_FILE) {
                    Uri selectedFile = data.getData();

                    final String path = getPathFromURI(selectedFile);
                    if (path != null) {
                        File file = new File(path);
                        //selectedFile = Uri.fromFile(file);
                        detectedFiletypeText.setText(new Tika().detect(file));  // Detects filetype

                        // TODO: 19.11.2019 Encrypt file here

                        fileAsBytes = Files.readAllBytes(file.toPath());
                        fileLocationText.setText(path);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {BaseColumns._ID};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(BaseColumns._ID);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public void submitFile(byte[] file, String title, String filetype) {
         new AsyncTask<Void, Void, Result>() {

             @Override
             protected Result doInBackground(Void... voids)  {
                Result response = null;
                 try {
                     response = mainRepository.uploadFile(file, title, filetype);
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                return response;
             }

             @Override
             protected void onPostExecute(Result result) {
                 if (result instanceof Result.Success) {
                     System.out.println("File submitted");
                 } else {
                    System.out.println("File not submitted");
                }
             }
         }.execute();
    }
}
