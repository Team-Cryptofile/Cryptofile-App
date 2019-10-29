package net.cryptofile.app.ui.home;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.cryptofile.app.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class HomeViewModel extends AndroidViewModel {

    MutableLiveData<List<DummyContent.DummyItem>> fileList;
    MutableLiveData<DummyContent.DummyItem> selected = new MutableLiveData<>();

    RequestQueue requestQueue;

    public HomeViewModel(Application context) {
        super(context);
        requestQueue = Volley.newRequestQueue(context);
    }

    LiveData<List<DummyContent.DummyItem>> getFileList() {
        if(fileList == null) {
            fileList = new MutableLiveData<>();
            loadFileList();
        }

        return fileList;
    }

    public LiveData<DummyContent.DummyItem> getSelected() {
        return selected;
    }

    protected void loadFileList() {
        // TODO: 29.10.2019 Load file list from local database
        List<DummyContent.DummyItem> fileList = new ArrayList<>();
        DummyContent dummy = new DummyContent();

        fileList.add(dummy.createDummyItem(1));
        fileList.add(dummy.createDummyItem(2));
        fileList.add(dummy.createDummyItem(3));
        fileList.add(dummy.createDummyItem(4));

        this.fileList.setValue(fileList);
    }

}