package net.cryptofile.app;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import net.cryptofile.app.data.FileService;
import net.cryptofile.app.ui.key.KeyViewModel;
import net.cryptofile.app.ui.fileDownload.DownloadDialog;
import net.cryptofile.app.ui.fileupload.FileUploadActivity;
import net.cryptofile.app.ui.files.FileViewModel;


public class MainActivity extends AppCompatActivity implements DownloadDialog.DownloadDialogListener {

    private AppBarConfiguration mAppBarConfiguration;

    //SET TO EITHER TRUE OR FALSE FOR TESTING PURPOSES
    boolean loggedIn = true;

    FloatingActionButton uploadButton;
    FloatingActionButton downloadButton;
    FloatingActionButton plusButton;

    boolean isFABOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (loggedIn) {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            uploadButton = findViewById(R.id.upload_fab);
            downloadButton = findViewById(R.id.download_fab);
            plusButton = findViewById(R.id.plus_fab);

            uploadButton.hide();
            downloadButton.hide();

            plusButton.setOnClickListener(view -> {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            });

            try {
                FileService.readFromStoredFiles();
            } catch (Exception e) {
                e.printStackTrace();
            }

            downloadButton.setOnClickListener(view -> openDownloadDialog());

            uploadButton.setOnClickListener(this::uploadFile);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_files, R.id.nav_privatekey, R.id.nav_help, R.id.nav_settings, R.id.nav_logout)
                    .setDrawerLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
            ViewModelProviders.of(this).get(FileViewModel.class).getSelected().observe(this, selected ->
                    navController.navigate(R.id.actionFileDetailFragment));
            ViewModelProviders.of(this).get(KeyViewModel.class).getSelected().observe(this, selected ->
                    navController.navigate(R.id.actionPrivkeyDetailFragment));
        } else {
            setContentView(R.layout.activity_login);

        }
        String[] requiredPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, requiredPermissions, 0);

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


    private void showFABMenu() {
        isFABOpen = true;
        downloadButton.animate().translationY(-getResources().getDimension(R.dimen.standard_90));
        uploadButton.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
        downloadButton.show();
        uploadButton.show();
    }

    private void closeFABMenu() {
        isFABOpen = false;
        downloadButton.animate().translationY(0);
        uploadButton.animate().translationY(0);
        downloadButton.hide();
        uploadButton.hide();
    }


    private void uploadFile(View view) {
        closeFABMenu();
        startActivity(new Intent(this, FileUploadActivity.class));
    }

    //TODO send a request to restAPI server to confirm connection. Change return statement.
    private boolean loginCheck() {

        return false;
    }

    private void openDownloadDialog() {
        closeFABMenu();
        DownloadDialog downloadDialog = new DownloadDialog();
        downloadDialog.show(getSupportFragmentManager(), "download dialog");

    }

    @Override
    public void applyText(String uuid) {
        DownloadDialog downloadDialog = new DownloadDialog();
    }
}