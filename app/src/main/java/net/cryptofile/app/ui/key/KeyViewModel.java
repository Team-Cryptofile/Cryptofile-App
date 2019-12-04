package net.cryptofile.app.ui.key;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.cryptofile.app.data.CryptoService;
import net.cryptofile.app.data.model.KeyEntity;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;

public class KeyViewModel extends AndroidViewModel {
    MutableLiveData<List<KeyEntity>> privkeys;
    MutableLiveData<KeyEntity> selected = new MutableLiveData<>();

    RequestQueue requestQueue;

    public KeyViewModel(Application context) {
        super(context);
        requestQueue = Volley.newRequestQueue(context);
    }


    public LiveData<List<KeyEntity>> getPrivateKeys() throws Exception {
        if (privkeys == null) {
            privkeys = new MutableLiveData<>();
            loadPrivkeys();
        }
        return privkeys;
    }


    protected void loadPrivkeys() throws Exception {
        // TODO: 05.11.2019 Load privatekey list from local database
        List<KeyEntity> keyEntityList = new ArrayList<>();
        List<String> storedKeyList = CryptoService.getAllAliases();
        SecretKey key = null;
        String id;


        for (int i = 0; i < storedKeyList.size(); i++) {
            try {
                id = storedKeyList.get(i);
                key = CryptoService.getKey(id);

                keyEntityList.add(new KeyEntity(id, key));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Key: " + Base64.getEncoder().encodeToString(key.getEncoded()));
        }

        this.privkeys.setValue(keyEntityList);
    }

    public LiveData<KeyEntity> getSelected() {
        return selected;
    }

    void setSelected(KeyEntity selected) {
        this.selected.setValue(selected);
    }


}