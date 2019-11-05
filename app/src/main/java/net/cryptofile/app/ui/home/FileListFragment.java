package net.cryptofile.app.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.cryptofile.app.R;

public class FileListFragment extends Fragment {

    //private FileViewModel homeViewModel;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel =
        //        ViewModelProviders.of(this).get(FileViewModel.class);
        //View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        //homeViewModel.getText().observe(this, new Observer<String>() {
        //    @Override
        //    public void onChanged(@Nullable String s) {
        //        textView.setText(s);
        //    }
        //});
        //return root;

        final RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_file_list, container, false);
        view.setLayoutManager(new LinearLayoutManager(view.getContext()));
        FileViewModel model = ViewModelProviders.of(this.getActivity()).get(FileViewModel.class);
        model.getFileList().observe(this, fileList ->
                view.setAdapter(new MyFileRecyclerViewAdapter(fileList)));
        return view;
    }
}