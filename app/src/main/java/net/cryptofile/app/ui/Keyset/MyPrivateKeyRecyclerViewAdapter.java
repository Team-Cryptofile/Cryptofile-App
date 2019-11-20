package net.cryptofile.app.ui.Keyset;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import net.cryptofile.app.R;
import net.cryptofile.app.data.model.Keyset;

import java.util.List;

/**
 */
public class MyPrivateKeyRecyclerViewAdapter extends RecyclerView.Adapter<MyPrivateKeyRecyclerViewAdapter.ViewHolder> {

    private final List<Keyset> mValues;

    PrivatekeyViewModel model;


    public MyPrivateKeyRecyclerViewAdapter(List<Keyset> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_privatekey, parent, false);

        model = ViewModelProviders.of((FragmentActivity) parent.getContext()).get(PrivatekeyViewModel.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.format("%s", mValues.get(position).getId()));
        holder.mView.setOnClickListener(view -> model.setSelected(holder.mItem));
        //holder.mPrivkeyView.setText(String.format("%s", mValues.get(position).getPrivkey()));
        //holder.mContentView.setText(mValues.get(position).content);
/*
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

 */
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        //public final TextView mPrivkeyView;
        //public final TextView mPubkeyView;
        public Keyset mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.privateKey_id);
            //mPrivkeyView = view.findViewById(R.id.privateKey_priv);
            //mPubkeyView = view.findViewById(R.id.privateKey_pub);
        }

        //@Override
        //public String toString() {
        //    return super.toString() + " '" + mContentView.getText() + "'";
        //}
    }
}
