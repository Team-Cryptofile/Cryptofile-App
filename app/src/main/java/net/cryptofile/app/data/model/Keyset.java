package net.cryptofile.app.data.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


public class Keyset {



    public static final List<Keyset> KEYSETS = new ArrayList<Keyset>();

    public Keyset(@NonNull String id, String privkey, String pubkey) {

    }

    private static void addItem(Keyset keyset) {
        KEYSETS.add(keyset);
    }

}