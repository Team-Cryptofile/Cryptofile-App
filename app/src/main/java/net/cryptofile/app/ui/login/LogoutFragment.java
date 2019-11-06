package net.cryptofile.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.cryptofile.app.R;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;

public class LogoutFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       /*  Hides toolbar from view

       ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        Hides floating action bar from view

        FloatingActionButton floatingActionButton = ((MainActivity)getActivity()).getFloatingActionButton();
        floatingActionButton.hide();
        */


       startActivity(new Intent(this.getActivity(),LogoutActivity.class));


        return inflater.inflate(R.layout.activity_logout, container, false);




    }
}
