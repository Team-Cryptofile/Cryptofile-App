package net.cryptofile.app.data;

import android.security.keystore.KeyProperties;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoService {
    private static KeyStore keyStore;
    private static char[] password = "password".toCharArray();
    private static String keyLocation = "/data/data/net.cryptofile.app/cryptokeys.bks"; // getFilesdir don't work in static classes
    private static String type = KeyStore.getDefaultType();

    static {
        try {
            if (!(new File(keyLocation).exists())) {
                keyStore = KeyStore.getInstance(type);
                keyStore.load(null, null);
                keyStore.store(new FileOutputStream(keyLocation), password);
            }

            keyStore = KeyStore.getInstance(type);
            keyStore.load(new FileInputStream(keyLocation), password);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static SecretKey generateKey() throws Exception {
        final KeyGenerator keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES);
        keyGen.init(256);
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

    public static SecretKey getKey(String uuid) throws Exception {
        return (SecretKey) keyStore.getKey(uuid, password);
    }

    public static void saveKey(String uuidAndKey) throws Exception {
        String[] splittedString = uuidAndKey.split(":", 2);
        byte[] keyBytes = Base64.getDecoder().decode(splittedString[1].getBytes(StandardCharsets.UTF_8));
        SecretKey secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
        storeKey(secretKey, splittedString[0]);
    }

    public static ArrayList<String> getAllAliases() throws KeyStoreException {
        return Collections.list(keyStore.aliases());
    }

    public static byte[] encrypt(SecretKey key, byte[] fileBytes) throws Exception {
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = cipher.getIV();
        byte[] encryptedFileBytes = cipher.doFinal(fileBytes);

        System.out.println("IV encrypt: " + new String(iv, StandardCharsets.UTF_8));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(iv);
        outputStream.write(encryptedFileBytes);

        return outputStream.toByteArray();
    }

    public static byte[] decrypt(SecretKey key, byte[] encryptedBytes) throws Exception {
        byte[] iv = Arrays.copyOf(encryptedBytes, 12);
        byte[] encryptedFileBytes = Arrays.copyOfRange(encryptedBytes, 12, encryptedBytes.length);

        System.out.println("IV decrypt: " + new String(iv, StandardCharsets.UTF_8));

        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        final GCMParameterSpec spec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        return cipher.doFinal(encryptedFileBytes);
    }

}