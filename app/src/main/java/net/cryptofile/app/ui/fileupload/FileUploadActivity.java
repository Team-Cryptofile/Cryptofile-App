package net.cryptofile.app.ui.fileupload;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import net.cryptofile.app.MainActivity;
import net.cryptofile.app.R;
import net.cryptofile.app.data.CryptoService;
import net.cryptofile.app.data.FileService;
import net.cryptofile.app.data.MainRepository;
import net.cryptofile.app.data.Result;
import net.cryptofile.app.data.ServerDataSource;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.crypto.SecretKey;

public class FileUploadActivity extends AppCompatActivity {

    private static final int REQUEST_GET_SINGLE_FILE = 1;

    File fileAsBytes = null;
    TextInputEditText detectedFiletypeText;
    TextView fileLocationText;
    Button submitBtn;
    TextInputEditText titleInput;

    String returnedUuid;

    MainRepository mainRepository;
    Result response;
    SecretKey key;
    String filePath;
    Uri selectedFile;
    String TEMP_FILE_PATH;

    TextView statusText;
    ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_upload_activity);

        mainRepository = new MainRepository(new ServerDataSource());
        TEMP_FILE_PATH  = getCacheDir() + "/uploadfile.tmp";

        final Button selectFilebutton = findViewById(R.id.selectUploadFilebutton);
        titleInput = findViewById(R.id.textInputEditText);
        fileLocationText = findViewById(R.id.textViewFilelocation);
        detectedFiletypeText = findViewById(R.id.textViewDetectedFileType);
        submitBtn = findViewById(R.id.uploadSubmitBtn);
        statusText = findViewById(R.id.uploadStatusText);
        progressBar = findViewById(R.id.uploadProgressBar);

        progressBar.setVisibility(View.GONE);


        selectFilebutton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_GET_SINGLE_FILE);
        });

        submitBtn.setOnClickListener(v -> {
            submitBtn.setEnabled(false);
            statusText.setText("Uploading...");
            progressBar.setVisibility(View.VISIBLE);

            submitFile(fileAsBytes, titleInput.getText().toString(), detectedFiletypeText.getText().toString());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_GET_SINGLE_FILE) {
                    selectedFile = data.getData();

                     filePath = selectedFile.getPath();

                    if (filePath != null) {
                        encryptFile();
                    }else {
                        System.out.println("Path is null!");
                    }

                }
            }
        } catch (Exception  e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("StaticFieldLeak")
    public void encryptFile(){
        new AsyncTask<Void, Void, Void>(){
            File tempFile;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try {
                    String ft = new Tika().detect(filePath); // Detects filetype
                    if (!(ft.isEmpty() || ft.matches("application/octet-stream"))) {
                        detectedFiletypeText.setText(ft.split("/")[1]);
                    }

                    statusText.setText("Encrypting...");
                    progressBar.setVisibility(View.VISIBLE);

                    key = CryptoService.generateKey();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedFile);
                    byte[] encryptedBytes = CryptoService.encrypt(key, IOUtils.toByteArray(inputStream));

                    // Write selected file to temporary file
                    tempFile = new File(TEMP_FILE_PATH);
                    OutputStream outputStream = new FileOutputStream(tempFile);
                    outputStream.write(encryptedBytes);
                    inputStream.close();
                } catch (Exception e){
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                statusText.setText("Encrypted");
                progressBar.setVisibility(View.GONE);

                fileAsBytes = tempFile;
                fileLocationText.setText(filePath.substring(filePath.lastIndexOf("/")+1));

            }
        }.execute();
    }


    @SuppressLint("StaticFieldLeak")
    public void submitFile(File file, String title, String filetype) {
        new AsyncTask<Void, Void, Result>() {

            @Override
            protected Result doInBackground(Void... voids) {
                try {
                    response = mainRepository.uploadFile(file, title, filetype);
                    System.out.println(response.toString());
                    if (response instanceof Result.Success) {
                        returnedUuid = ((Result.Success<String>) response).getData();
                        System.out.println("Recieved ID: " + returnedUuid);
                    }
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Result.Error(e);
                }
            }

            @Override
            protected void onPostExecute(Result result) {
                if (result instanceof Result.Success) {
                    System.out.println("File submitted");

                    statusText.setText("Uploaded");
                    progressBar.setVisibility(View.GONE);

                    try {
                        PrintWriter writer = new PrintWriter(file);
                        writer.print("");
                        writer.close();
                        redirect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("File not submitted");

                    statusText.setText("Upload failed");
                    progressBar.setVisibility(View.GONE);

                    try {
                        redirect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    private void redirect() throws Exception {
        if (response instanceof Result.Success) {
            CryptoService.storeKey(key, returnedUuid);
            FileService.addFile(returnedUuid, titleInput.getText().toString(), detectedFiletypeText.getText().toString());
            Toast.makeText(this, "File successfully uploaded", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Toast.makeText(this, "Something went wrong, file failed to be uploaded!", Toast.LENGTH_LONG).show();
            submitBtn.setEnabled(true);
        }
    }

}
