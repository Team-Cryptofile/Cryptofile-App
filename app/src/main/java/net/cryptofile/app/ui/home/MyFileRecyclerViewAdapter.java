package net.cryptofile.app.ui.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.cryptofile.app.R;
import net.cryptofile.app.data.model.File;

import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

/**
 */
public class MyFileRecyclerViewAdapter extends RecyclerView.Adapter<MyFileRecyclerViewAdapter.ViewHolder> {

    private final List<File> mValues;

    FileViewModel model;

    public MyFileRecyclerViewAdapter(List<File> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_file, parent, false);

        model = ViewModelProviders.of((FragmentActivity) parent.getContext()).get(FileViewModel.class);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.format("%s",mValues.get(position).getId()));
        holder.mContentView.setText(String.format("%s",mValues.get(position).getTitle()));
        holder.mView.setOnClickListener(v -> model.setSelected(holder.mItem));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public File mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
