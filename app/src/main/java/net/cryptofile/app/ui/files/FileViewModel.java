package net.cryptofile.app.ui.files;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.cryptofile.app.data.FileService;
import net.cryptofile.app.data.model.FileEntry;

import java.util.List;


public class FileViewModel extends AndroidViewModel {

    MutableLiveData<List<FileEntry>> fileList;
    MutableLiveData<FileEntry> selected = new MutableLiveData<>();

    RequestQueue requestQueue;

    public FileViewModel(Application context) {
        super(context);
        requestQueue = Volley.newRequestQueue(context);
    }

    LiveData<List<FileEntry>> getFileList() throws Exception {
        if (fileList == null) {
            fileList = new MutableLiveData<>();
            loadFileList();
        }

        return fileList;
    }

    public LiveData<FileEntry> getSelected() {
        return selected;
    }

    public void setSelected(FileEntry selected) {
        this.selected.setValue(selected);
    }

    protected void loadFileList() throws Exception {
        this.fileList.setValue(FileService.getFileList());
    }

    private void loadFab() {
        FloatingActionButton fab;
    }

}