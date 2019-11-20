package net.cryptofile.app.ui.fileupload;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.lifecycle.ViewModel;

import net.cryptofile.app.data.MainRepository;
import net.cryptofile.app.data.Result;

public class FileUploadViewModel extends ViewModel {

    private MainRepository mainRepository;

    public FileUploadViewModel(MainRepository mainRepository){
        this.mainRepository = mainRepository;
    }

    @SuppressLint("StaticFieldLeak")
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
