package com.example.testeandroid;

import android.content.Intent;
import android.os.Bundle;

import com.example.testeandroid.model.Movie;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class ScrollingActivity extends AppCompatActivity {

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle("New Title.");

        movie = (Movie) getIntent().getSerializableExtra("movie");
        setTitle(movie.getTitle());
        TextView tvTitle = findViewById(R.id.movieTitle);
        tvTitle.setText(movie.getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(movie.getTitle());
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                String shareBody = movie.getTitle()+ " www.google.com";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Teste Android");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Compartilhar"));
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();
            }
        });
    }
}
