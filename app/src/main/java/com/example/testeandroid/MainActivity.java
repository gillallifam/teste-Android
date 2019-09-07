package com.example.testeandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

import com.example.testeandroid.model.Movie;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    boolean workonline;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        editor = prefs.edit();

        Toolbar toolbar = findViewById(R.id.maintoolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.ic_home_black_24dp);

        //editor.clear();
        //editor.commit();
        //System.out.println("USER: "+prefs.getString("username",""));
        //System.out.println("IMG: " + prefs.getString("userimage", ""));

        /*ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);*/

        if (prefs.getString("firsttime", "yes").equals("yes")) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }

        if (SUtil.isOnline(this)) {
            workonline = true;
            Snackbar.make(findViewById(R.id.container),"Your Text Here",Snackbar.LENGTH_SHORT).show();
            System.out.println("Working online");
        } else {
            workonline = false;
            System.out.println("Working offline");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_movie, R.id.navigation_perfil).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public boolean workMode() {
        return workonline;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("onCreateOptionsMenu");
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public Menu getMenu() {
        return menu;
    }

    public void restart() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void openMovie(Movie movie) {
        System.out.println(movie.getTitle());
        Intent intent = new Intent(this, ScrollingActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

}
