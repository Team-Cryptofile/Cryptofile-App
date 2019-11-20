package net.cryptofile.app.ui.fileupload;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;

import net.cryptofile.app.R;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class FileUploadActivity extends AppCompatActivity {

    private static final int REQUEST_GET_SINGLE_FILE= 1;

    FileUploadViewModel viewModel;
    String fileLocationString;
    byte[] fileAsBytes = null;
    TextView detectedFiletypeText;
    TextView fileLocationText;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_upload_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FileUploadFragment.newInstance())
                    .commitNow();
        }

        viewModel = ViewModelProviders.of(this).get(FileUploadViewModel.class);

        final Button selectFilebutton = findViewById(R.id.selectUploadFilebutton);
        final TextInputEditText titleInput = findViewById(R.id.textInputEditText);
        fileLocationText = findViewById(R.id.textViewFilelocation);
        final TextView filetypeLabel = findViewById(R.id.textViewFiletypeLabel);
        detectedFiletypeText = findViewById(R.id.textViewDetectedFileType);
        final Button submitBtn = findViewById(R.id.uploadSubmitBtn);

        selectFilebutton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_GET_SINGLE_FILE);
        });

        submitBtn.setOnClickListener(v -> {
            viewModel.submitFile(fileAsBytes, titleInput.getText().toString(), detectedFiletypeText.getText().toString());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_GET_SINGLE_FILE) {
                    Uri selectedFile = data.getData();

                    final String path = selectedFile.getPath();//getPath(selectedFile);

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



}
