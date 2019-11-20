package net.cryptofile.app.ui.Keyset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import net.cryptofile.app.R;


public class PrivatekeyFragment extends Fragment {

    private TextView id;
    private TextView privKey;
    private TextView pubkey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrivatekeyViewModel model = ViewModelProviders.of(this.getActivity()).get(PrivatekeyViewModel.class);

        model.getSelected().observe(this, privkey -> {
            try {
                id.setText(model.selected.getValue().getId());
                privKey.setText(model.selected.getValue().getPrivKey());
                pubkey.setText(model.selected.getValue().getPubKey());
            }catch (Exception e){
                e.printStackTrace();
            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privatekey_detail, container, false);

        id = view.findViewById(R.id.textViewPrivkeyId);
        privKey = view.findViewById(R.id.textViewPrivkey_priv);
        pubkey = view.findViewById(R.id.textViewPrivkey_pub);

        return view;
    }

}
