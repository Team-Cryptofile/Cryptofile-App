package net.cryptofile.app.ui.Keyset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.cryptofile.app.R;

public class PrivatekeyListFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RecyclerView root = (RecyclerView) inflater.inflate(R.layout.fragment_privatekey_list, container, false);
        root.setLayoutManager(new LinearLayoutManager(root.getContext()));
        PrivatekeyViewModel model = ViewModelProviders.of(this.getActivity()).get(PrivatekeyViewModel.class);
        try {
            model.getPrivateKeys().observe(this, privkeys ->
                    root.setAdapter(new MyPrivateKeyRecyclerViewAdapter(privkeys)));
        } catch (Exception  e) {
            e.printStackTrace();
        }

        return root;
    }
}