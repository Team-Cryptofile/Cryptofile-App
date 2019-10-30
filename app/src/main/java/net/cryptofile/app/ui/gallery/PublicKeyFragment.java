package net.cryptofile.app.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.cryptofile.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class PublicKeyFragment extends Fragment {

    private PublicKeyViewModel publickeyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        publickeyViewModel =
                ViewModelProviders.of(this).get(PublicKeyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_publickey, container, false);
        final TextView textView = root.findViewById(R.id.text_publickey);
        publickeyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


}