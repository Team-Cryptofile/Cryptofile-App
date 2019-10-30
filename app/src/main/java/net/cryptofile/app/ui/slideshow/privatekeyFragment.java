package net.cryptofile.app.ui.slideshow;

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

public class privatekeyFragment extends Fragment {

    private net.cryptofile.app.ui.slideshow.privatekeyViewModel privatekeyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        privatekeyViewModel =
                ViewModelProviders.of(this).get(net.cryptofile.app.ui.slideshow.privatekeyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_privatekey, container, false);
        final TextView textView = root.findViewById(R.id.text_privatekey);
        privatekeyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}