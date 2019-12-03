package net.cryptofile.app.data;

import android.content.Context;
import android.os.Environment;
import android.security.keystore.KeyProperties;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

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

    public static byte[] encrypt(SecretKey key, byte[] fileBytes) throws Exception {
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = cipher.getIV();
        byte[] encryptedFileBytes = cipher.doFinal(fileBytes);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(iv);
        outputStream.write(":::::".getBytes(StandardCharsets.UTF_8));
        outputStream.write(encryptedFileBytes);

        return outputStream.toByteArray();
    }

    public static byte[] decrypt(SecretKey key, byte[] encryptedBytes) throws Exception {
        String[] split = new String(encryptedBytes, StandardCharsets.UTF_8).split(new String(":::::".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8), 1);
        byte[] iv = split[0].getBytes(StandardCharsets.UTF_8);
        byte[] encryptedFileBytes = split[1].getBytes(StandardCharsets.UTF_8);

        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        final GCMParameterSpec spec = new GCMParameterSpec(256, iv);

        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        return cipher.doFinal(encryptedFileBytes);
    }

}