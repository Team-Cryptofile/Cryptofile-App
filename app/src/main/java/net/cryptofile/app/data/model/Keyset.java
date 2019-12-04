package net.cryptofile.app.data.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;


public class Keyset {

    String id = null;
    SecretKey key = null;


    public static final List<Keyset> KEYSETS = new ArrayList<>();

    public Keyset(@NonNull String id, SecretKey key) {

        this.id = id;
        this.key = key;

    }

    private static void addItem(Keyset keyset) {
        KEYSETS.add(keyset);
    }


    public String getId() {
        return id;
    }

    public SecretKey getKey() {
        return key;
    }

}

