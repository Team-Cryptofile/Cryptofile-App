package net.cryptofile.app.ui.fileupload;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;

import net.cryptofile.app.R;
import net.cryptofile.app.data.MainRepository;
import net.cryptofile.app.data.Result;
import net.cryptofile.app.data.ServerDataSource;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.sql.DataSource;

public class FileUploadActivity extends AppCompatActivity {

    private static final int REQUEST_GET_SINGLE_FILE= 1;

    byte[] fileAsBytes = null;
    TextView detectedFiletypeText;
    TextView fileLocationText;

    MainRepository mainRepository;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_upload_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FileUploadFragment.newInstance())
                    .commitNow();
        }

        mainRepository = new MainRepository(new ServerDataSource());

        final Button selectFilebutton = findViewById(R.id.selectUploadFilebutton);
        final TextInputEditText titleInput = findViewById(R.id.textInputEditText);
        fileLocationText = findViewById(R.id.textViewFilelocation);
        detectedFiletypeText = findViewById(R.id.textViewDetectedFileType);
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

                    final String path = selectedFile.getPath();

                    if (path != null) {
                        InputStream inputStream = getContentResolver().openInputStream(selectedFile);

                        detectedFiletypeText.setText(new Tika().detect(path));  // Detects filetype

                        // TODO: 19.11.2019 Encrypt file here


                        fileAsBytes = IOUtils.toByteArray(inputStream);
                        inputStream.close();
                        fileLocationText.setText(path.substring(path.lastIndexOf("/")+1));
                    }else {
                        System.out.println("Path is null!");
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void submitFile(byte[] file, String title, String filetype) {
        new AsyncTask<Void, Void, Result>() {

            @Override
            protected Result doInBackground(Void... voids) {
                try {
                    Result response = mainRepository.uploadFile(file, title, filetype);
                    String id = response.toString();
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
                } else {
                    System.out.println("File not submitted");
                }
            }
        }.execute();
    }

}
