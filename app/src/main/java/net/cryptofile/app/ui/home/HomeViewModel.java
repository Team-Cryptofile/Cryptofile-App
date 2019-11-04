package net.cryptofile.app.ui.home;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.cryptofile.app.data.model.File;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class HomeViewModel extends AndroidViewModel {

    MutableLiveData<List<File>> fileList;
    MutableLiveData<File> selected = new MutableLiveData<>();

    RequestQueue requestQueue;

    public HomeViewModel(Application context) {
        super(context);
        requestQueue = Volley.newRequestQueue(context);
    }

    LiveData<List<File>> getFileList() {
        if(fileList == null) {
            fileList = new MutableLiveData<>();
            loadFileList();
        }

        return fileList;
    }

    public LiveData<File> getSelected() {
        return selected;
    }

    public void setSelected(File selected) {
        this.selected.setValue(selected);
    }

    protected void loadFileList() {
        // TODO: 29.10.2019 Load file list from local database
        List<File> fileList = new ArrayList<>();


        fileList.add(new File(UUID.randomUUID().toString(), "Title 1"));
        fileList.add(new File(UUID.randomUUID().toString(), "Title 2"));
        fileList.add(new File(UUID.randomUUID().toString(), "Title 3"));
        fileList.add(new File(UUID.randomUUID().toString(), "Title 4"));
        fileList.add(new File(UUID.randomUUID().toString(), "Title 5"));
        fileList.add(new File(UUID.randomUUID().toString(), "Title 6"));

        File.FILES.addAll(fileList);
        this.fileList.setValue(File.FILES);
    }

}