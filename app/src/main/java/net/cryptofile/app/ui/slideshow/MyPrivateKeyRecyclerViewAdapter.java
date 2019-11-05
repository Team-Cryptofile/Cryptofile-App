package net.cryptofile.app.ui.slideshow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import net.cryptofile.app.R;
import net.cryptofile.app.data.model.Privatekey;

import java.util.List;

/**
 */
public class MyPrivateKeyRecyclerViewAdapter extends RecyclerView.Adapter<MyPrivateKeyRecyclerViewAdapter.ViewHolder> {

    private final List<Privatekey> mValues;

    PrivatekeyViewModel model;


    public MyPrivateKeyRecyclerViewAdapter(List<Privatekey> items) {
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
        public final TextView mContentView;
        public Privatekey mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.privateKey_id);
            mContentView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
