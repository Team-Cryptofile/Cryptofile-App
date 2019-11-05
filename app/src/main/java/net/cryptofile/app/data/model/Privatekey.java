package net.cryptofile.app.data.model;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Privatekey {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    String id;

    @ColumnInfo(name = "privkey")
    String privkey;

    @ColumnInfo(name = "pubkey")
    String pubkey;

    public static final List<Privatekey> KEYS = new ArrayList<Privatekey>();

    public Privatekey(@NonNull String id, String privkey, String pubkey) {
        this.id = id;
        this.privkey = privkey;
        this.pubkey = pubkey;
    }

    private static void addItem(Privatekey key) {
        KEYS.add(key);
    }

}