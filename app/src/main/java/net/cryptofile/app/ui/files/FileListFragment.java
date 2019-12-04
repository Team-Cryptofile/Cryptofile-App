package net.cryptofile.app.ui.files;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import net.cryptofile.app.MainActivity;
import net.cryptofile.app.R;
import net.cryptofile.app.data.FileService;
import net.cryptofile.app.data.Result;
import net.cryptofile.app.data.model.FileEntry;

import java.util.List;
import java.util.Objects;

public class FileListFragment extends Fragment {

    private static final String LOG_TAG = MainActivity.class.getName();
    MutableLiveData<List<FileEntry>> fileList;

    @SuppressLint("StaticFieldLeak")
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        SwipeRefreshLayout view = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_file_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        FileViewModel model = ViewModelProviders.of(Objects.requireNonNull(this.getActivity())).get(FileViewModel.class);
        try {
            model.getFileList().observe(this, fileList ->
                    recyclerView.setAdapter(new FileRecyclerViewAdapter(fileList)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        view.setOnRefreshListener(
                () -> {
                    Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                    // This method performs the actual data-refresh operation.
                    // The method calls setRefreshing(false) when it's finished.
                    new AsyncTask<Void, Void, Result>() {
                        @Override
                        protected Result doInBackground(Void... voids) {

                            try {
                                loadFileList();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    };
                    view.setRefreshing(false);
                }
        );
        return view;
    }

    protected void loadFileList() throws Exception {
        this.fileList.setValue(FileService.getFileList());
    }
}