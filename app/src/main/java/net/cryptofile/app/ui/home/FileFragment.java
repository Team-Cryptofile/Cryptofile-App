package net.cryptofile.app.ui.home;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.cryptofile.app.R;
import net.cryptofile.app.data.CryptoService;
import net.cryptofile.app.data.model.FileEntry;

import java.util.Base64;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


public class FileFragment extends Fragment {

    TextView id;
    TextView title;
    TextView fileType;
    Button copyButton;


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
            }catch (Exception e){
                e.printStackTrace();
            }

            copyButton.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = null; //(model.selected.getValue().getKey().getEncoded())));
                try {
                    clip = ClipData.newPlainText( "Cryptofile key", model.selected.getValue().getId() + ":" + Base64.getEncoder().encodeToString(CryptoService.getKey(model.selected.getValue().getId()).getEncoded()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Key copied to clipboard!", Toast.LENGTH_SHORT).show();
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


        /*
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyFileRecyclerViewAdapter(FileEntry.FILES, mListener));
        }

         */
        return view;
    }

/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     * /
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(FileEntry item);
    }

 */

}
