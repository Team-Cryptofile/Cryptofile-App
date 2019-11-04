package net.cryptofile.app.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.cryptofile.app.MainActivity;
import net.cryptofile.app.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class LogoutFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        FloatingActionButton floatingActionButton = ((MainActivity)getActivity()).getFloatingActionButton();
        floatingActionButton.hide();


        return inflater.inflate(R.layout.activity_login, null, true);


    }
}
