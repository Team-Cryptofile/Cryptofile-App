package net.cryptofile.app.ui.files;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import net.cryptofile.app.R;
import net.cryptofile.app.data.CryptoService;
import net.cryptofile.app.data.FileService;

import java.util.Base64;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;


public class FileFragment extends Fragment {

    TextView id;
    TextView title;
    TextView fileType;
    Button copyButton;
    Button deleteButton;
    ImageView imageView;
    String stringToSend = null;


    /*

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     * /
    public FileFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FileFragment newInstance(int columnCount) {
        FileFragment fragment = new FileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FileViewModel model = ViewModelProviders.of(this.getActivity()).get(FileViewModel.class);
        /*
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
         */

        model.getSelected().observe(this, file -> {
            try {
                id.setText(model.selected.getValue().getId());
                title.setText(model.selected.getValue().getTitle());
                fileType.setText(model.selected.getValue().getFileType());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                stringToSend = model.selected.getValue().getId() + ":" + Base64.getEncoder().encodeToString(CryptoService.getKey(model.selected.getValue().getId()).getEncoded());
                Bitmap bitmap = encodeAsBitmap(stringToSend);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            copyButton.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = null; //(model.selected.getValue().getKey().getEncoded())));
                try {

                    clip = ClipData.newPlainText("Cryptofile key", stringToSend);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Key copied to clipboard!", Toast.LENGTH_SHORT).show();
            });

            deleteButton.setOnClickListener(view -> {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                try {
                                    FileService.delete(model.selected.getValue().getId());
                                    CryptoService.delete(model.selected.getValue().getId());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                getActivity().finish();
                                startActivity(getActivity().getIntent());
                                dialogInterface.dismiss();

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialogInterface.cancel();
                        }
                    }


                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setMessage("Are you sure you want to delete this file?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            });

        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_detail, container, false);

        id = view.findViewById(R.id.textViewFileId);
        title = view.findViewById(R.id.textViewFileTitle);
        fileType = view.findViewById(R.id.textViewFileType);
        copyButton = view.findViewById(R.id.copyKeyButton);
        deleteButton = view.findViewById(R.id.deleteFileButton);
        imageView = view.findViewById(R.id.qrCode);
        return view;
    }


    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;

        int width = Resources.getSystem().getDisplayMetrics().widthPixels - 50;
        System.out.println("Image width: " + width);
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, width, width, null);
        } catch (IllegalArgumentException iae) {
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
        return bitmap;
    }

}
