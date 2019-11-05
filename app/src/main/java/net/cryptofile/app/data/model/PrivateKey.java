package net.cryptofile.app.data.model;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PrivateKey {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    String id;

    @ColumnInfo(name = "privkey")
    String privkey;

    @ColumnInfo(name = "pubkey")
    String pubkey;

    public static final List<PrivateKey> KEYS = new ArrayList<PrivateKey>();

    public PrivateKey(@NonNull String id, String privkey, String pubkey) {
        this.id = id;
        this.privkey = privkey;
        this.pubkey = pubkey;
    }

    private static void addItem(PrivateKey key) {
        KEYS.add(key);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isPair(String privkey, String pubkey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String testMessage = "Test message";
        System.out.println("Test message: " + testMessage);

        java.security.PrivateKey privateKey = getPrivateKey(privkey);
        PublicKey publicKey = getPublicKey(pubkey);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encryptedBytes = cipher.doFinal(testMessage.getBytes());
        System.out.println("Encrypted message: " + encryptedBytes.toString());

        Cipher cipher1 = Cipher.getInstance("RSA");
        cipher1.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedBytes = cipher1.doFinal(encryptedBytes);
        String decryptedMessage = new String(decryptedBytes);
        System.out.println("Decrypted message: " + decryptedMessage);

        return decryptedMessage.equals(testMessage);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static PublicKey getPublicKey(String pubkey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] data = Base64.getDecoder().decode(pubkey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static java.security.PrivateKey getPrivateKey(String privkey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String pk = new String(privkey, "UTF-8");
        byte[] data = Base64.getDecoder().decode(privkey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }

}