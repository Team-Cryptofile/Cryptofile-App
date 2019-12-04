package net.cryptofile.app.data.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;


public class KeyEntity {

    String id = null;
    SecretKey key = null;


    public static final List<KeyEntity> KEY_ENTITIES = new ArrayList<>();

    public KeyEntity(@NonNull String id, SecretKey key) {

        this.id = id;
        this.key = key;

    }

    private static void addItem(KeyEntity keyEntity) {
        KEY_ENTITIES.add(keyEntity);
    }


    public String getId() {
        return id;
    }

    public SecretKey getKey() {
        return key;
    }

}

