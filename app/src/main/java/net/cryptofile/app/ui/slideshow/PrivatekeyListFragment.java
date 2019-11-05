package net.cryptofile.app.ui.slideshow;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.cryptofile.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class PrivatekeyListFragment extends Fragment {

    private PrivatekeyViewModel privatekeyViewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RecyclerView root = (RecyclerView) inflater.inflate(R.layout.fragment_privatekey_list, container, false);
        root.setLayoutManager(new LinearLayoutManager(root.getContext()));
        PrivatekeyViewModel model = ViewModelProviders.of(this.getActivity()).get(PrivatekeyViewModel.class);
        try {
            model.getPrivateKeys().observe(this, privkeys -> root.setAdapter(new MyPrivateKeyRecyclerViewAdapter(privkeys)));
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }



        /*
        privatekeyViewModel =
                ViewModelProviders.of(this).get(PrivatekeyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_privatekey, container, false);
        final TextView textView = root.findViewById(R.id.text_privatekey);
        privatekeyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

         */
        return root;
    }
}