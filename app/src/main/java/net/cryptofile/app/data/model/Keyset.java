package net.cryptofile.app.data.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


public class Keyset {

    String id = null;
    String privKey = null;
    String pubKey = null;


    public static final List<Keyset> KEYSETS = new ArrayList<Keyset>();

    public Keyset(@NonNull String id, String privkey, String pubkey) {

        this.id = id;
        this.privKey = privkey;
        this.pubKey = pubkey;

    }

    private static void addItem(Keyset keyset) {
        KEYSETS.add(keyset);
    }


    public String getId() {
        return id;
    }

    public String getPrivKey() {
        return privKey;
    }

    public String getPubKey() {
        return pubKey;
    }
}

