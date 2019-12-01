package net.cryptofile.app.ui.Keyset;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.cryptofile.app.data.CryptoService;
import net.cryptofile.app.data.model.Keyset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;

public class PrivatekeyViewModel extends AndroidViewModel {
    MutableLiveData<List<Keyset>> privkeys;
    MutableLiveData<Keyset> selected = new MutableLiveData<>();

    RequestQueue requestQueue;

    public PrivatekeyViewModel(Application context) {
        super(context);
        requestQueue = Volley.newRequestQueue(context);
    }


    public LiveData<List<Keyset>> getPrivateKeys() throws Exception {
        if (privkeys == null){
            privkeys = new MutableLiveData<>();
            loadPrivkeys();
        }
        return privkeys;
    }


    protected void loadPrivkeys() throws Exception {
        // TODO: 05.11.2019 Load privatekey list from local database
        List<Keyset> keysetList = new ArrayList<>();
        List<String> storedKeyList = CryptoService.getAllAliases();
        SecretKey key = null;
        String id;


        for (int i = 0; i < storedKeyList.size(); i++) {
            try {
                id = storedKeyList.get(i);
                key = CryptoService.getKey(id);

                keysetList.add(new Keyset(id, key));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Key: " + Base64.getEncoder().encodeToString(key.getEncoded()));
        }

        this.privkeys.setValue(keysetList);
    }

    public LiveData<Keyset> getSelected() {
        return selected;
    }

    void setSelected(Keyset selected){
        this.selected.setValue(selected);
    }
}