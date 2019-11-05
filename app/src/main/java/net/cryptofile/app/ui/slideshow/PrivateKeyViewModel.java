package net.cryptofile.app.ui.slideshow;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.cryptofile.app.data.GenerateKeys;
import net.cryptofile.app.data.model.PrivateKey;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class PrivateKeyViewModel extends ViewModel {
    MutableLiveData<List<PrivateKey>> privkeys;
    MutableLiveData<String> mText = new MutableLiveData<>();

    public PrivateKeyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is privatekey fragment");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LiveData<List<PrivateKey>> getPrivateKeys() throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        if (privkeys == null){
            privkeys = new MutableLiveData<>();
            loadPrivkeys();
        }
        return privkeys;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void loadPrivkeys() throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        List<PrivateKey> privateKeyList = new ArrayList<PrivateKey>();
        GenerateKeys keys = null;
        String pubkey = null;
        String privkey = null;

        try {
            keys = new GenerateKeys(1024);
            keys.createKeys();
            pubkey = keys.getPublicKey().toString();
            privkey = keys.getPrivateKey().toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        if (PrivateKey.isPair(privkey,pubkey)){
            System.out.println("The keys is working!!!");
        }else{
            System.out.println("The keys does not work :(");
        }

        privateKeyList.add(new PrivateKey(UUID.randomUUID().toString(), privkey, pubkey));
    }

    public MutableLiveData<String> getSelected() {
        return mText;
    }
}