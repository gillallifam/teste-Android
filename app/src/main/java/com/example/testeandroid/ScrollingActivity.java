package com.example.testeandroid;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.testeandroid.model.Movie;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
        //Toolbar toolbar = findViewById(R.id.toolbar);
        Toolbar toolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.collapsingToolbar_layout);
        toolbarLayout.setTitle("New Title.");
        movie = (Movie) getIntent().getSerializableExtra("movie");

        final View bg = findViewById(R.id.collapsingToolbar_layout);

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w300/" + movie.getBackdrop_path())
                .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        bg.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        setTitle(movie.getTitle());
        TextView tvTitle = findViewById(R.id.moviePopularity);
        tvTitle.setText("Popularity: " + movie.getPopularity());
        TextView tvRelease = findViewById(R.id.movieRelease);
        tvRelease.setText("Release date: " + movie.getRelease_date());
        TextView tvOverview = findViewById(R.id.movieOverview);
        tvOverview.setText(movie.getOverview());


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(movie.getTitle());
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                String shareBody = movie.getTitle() + " www.google.com";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Teste Android");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Compartilhar"));
            }
        });
    }
}
