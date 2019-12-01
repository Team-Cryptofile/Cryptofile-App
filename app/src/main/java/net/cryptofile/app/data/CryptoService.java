package net.cryptofile.app.data;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CryptoService {

    public CryptoService() {
    }

    public SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(4096);
        return keyGen.generateKey();
    }

    public void storeKey(SecretKey key, String uuid) throws KeyStoreException {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(key);
        keyStore.setKeyEntry(uuid, secretKeyEntry.getSecretKey().getEncoded(), null);
    }
/*
    public SecretKey getKey(String uuid) throws  Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        return keyStore.getKey(uuid, null).;
    }

 */

}