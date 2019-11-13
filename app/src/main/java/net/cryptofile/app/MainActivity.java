package net.cryptofile.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import net.cryptofile.app.ui.home.FileViewModel;
import net.cryptofile.app.ui.slideshow.PrivatekeyViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    // TODO: 22.10.19  create 'check if logged in' function
    //SET TO EITHER TRUE OR FALSE FOR TESTING PURPOSES
    boolean loggedIn = true;

    FloatingActionButton fab;
    FloatingActionButton fabItem1;
    FloatingActionButton fabItem2;

    boolean isFABOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(loggedIn) {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            fab = findViewById(R.id.icon);
            fabItem1 = findViewById(R.id.fab1);
            fabItem2 = findViewById(R.id.fab2);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isFABOpen){
                        showFABMenu();
                    }
                    else{
                        closeFABMenu();
                    }
                }
            });

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_files, R.id.nav_publickey, R.id.nav_privatekey, R.id.nav_help, R.id.nav_settings, R.id.nav_logout)
                    .setDrawerLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
            ViewModelProviders.of(this).get(FileViewModel.class).getSelected().observe(this, selected ->
                    navController.navigate(R.id.actionFileDetailFragment));
            ViewModelProviders.of(this).get(PrivatekeyViewModel.class).getSelected().observe(this, selected ->
                    navController.navigate(R.id.actionPrivkeyDetailFragment));
        }
        else {
            setContentView(R.layout.activity_login);

        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

    //gets floating action button
    public FloatingActionButton getFloatingActionButton() {
        return fab;

    }

    private void showFABMenu(){
        isFABOpen=true;
        fabItem1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabItem2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fabItem1.show();
        fabItem2.show();
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fabItem1.animate().translationY(0);
        fabItem2.animate().translationY(0);
        fabItem1.hide();
        fabItem2.hide();
    }

}