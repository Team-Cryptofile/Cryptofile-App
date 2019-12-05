package net.cryptofile.app.ui.key;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import net.cryptofile.app.R;

import java.util.Base64;


public class KeyFragment extends Fragment {

    private TextView id;
    private TextView privKey;
    private Button copyButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        KeyViewModel model = ViewModelProviders.of(this.getActivity()).get(KeyViewModel.class);

        model.getSelected().observe(this, privkey -> {
            try {
                id.setText(model.selected.getValue().getId());
                privKey.setText(Base64.getEncoder().encodeToString((model.selected.getValue().getKey().getEncoded())));
            } catch (Exception e) {
                e.printStackTrace();
            }

            copyButton.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Cryptofile key", model.selected.getValue().getId() + ":" + Base64.getEncoder().encodeToString((model.selected.getValue().getKey().getEncoded())));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Key copied to clipboard!", Toast.LENGTH_SHORT).show();
            });

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privatekey_detail, container, false);

        id = view.findViewById(R.id.textViewPrivkeyId);
        privKey = view.findViewById(R.id.textViewPrivkey_priv);
        copyButton = view.findViewById(R.id.copyKeyButton);

        return view;
    }


}
