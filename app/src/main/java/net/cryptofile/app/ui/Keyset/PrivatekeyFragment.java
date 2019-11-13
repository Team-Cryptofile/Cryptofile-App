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

    TextView id;
    TextView privkey;
    TextView pubkey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrivatekeyViewModel model = ViewModelProviders.of(this.getActivity()).get(PrivatekeyViewModel.class);

        model.getSelected().observe(this, prvkey -> {
            try {
                id.setText(model.selected.getValue().getId());
                privkey.setText(model.selected.getValue().getPrivkey());
                pubkey.setText(model.selected.getValue().getPubkey());
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
        privkey = view.findViewById(R.id.textViewPrivkey_priv);
        pubkey = view.findViewById(R.id.textViewPrivkey_pub);

        return view;
    }

}
