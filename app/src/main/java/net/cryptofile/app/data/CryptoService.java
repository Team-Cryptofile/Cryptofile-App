package net.cryptofile.app.data;

import android.content.Context;
import android.os.Environment;
import android.security.keystore.KeyProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CryptoService {
    private static KeyStore keyStore;
    private static char[] password = "password".toCharArray();
    private static String keyLocation = "/data/data/net.cryptofile.app/cryptokeys.bks"; // getFilesdir don't work in static classes
    private static String type = KeyStore.getDefaultType();

    static {
        try {
            if (!(new File(keyLocation).exists())){
                keyStore = KeyStore.getInstance(type);
                keyStore.load(null, null);
                keyStore.store(new FileOutputStream(keyLocation),password);
            }

            keyStore = KeyStore.getInstance(type);
            keyStore.load(new FileInputStream(keyLocation), password);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static SecretKey generateKey() throws Exception {
        final KeyGenerator keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES);
        keyGen.init(4096);
        return keyGen.generateKey();
    }

    public static void storeKey(SecretKey key, String uuid) throws Exception {
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(key);
        KeyStore.ProtectionParameter kspp = new KeyStore.PasswordProtection(password);

        System.out.println("Default keystore: " + type);


        keyStore.setEntry(uuid, secretKeyEntry, kspp);
        keyStore.store(new FileOutputStream(keyLocation), password);


        System.out.println("Stored key: " + Base64.getEncoder().encodeToString(getKey(uuid).getEncoded()));
    }

    public static SecretKey getKey(String uuid) throws  Exception {
        return (SecretKey) keyStore.getKey(uuid, password);
    }

    public static void saveKey(String uuidAndKey) throws Exception{
        String[] splittedString = uuidAndKey.split(":", 1);
        byte[] keyBytes = Base64.getDecoder().decode(splittedString[1].getBytes());

        keyStore.setKeyEntry(splittedString[0], keyBytes, null);
    }

    public static ArrayList<String> getAllAliases() throws KeyStoreException {
        return Collections.list(keyStore.aliases());
    }

}