package com.example.testeandroid.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.testeandroid.MainActivity;
import com.example.testeandroid.R;
import com.example.testeandroid.model.Movie;

import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private List<Movie> mData;
    private LayoutInflater mInflater;
    private Context context;
    private String type;

    public MainRecyclerViewAdapter(Context context, List<Movie> data, String type) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.type = type;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (type) {
            case "linear":
                view = mInflater.inflate(R.layout.mainrecycleritemlist, parent, false);
                break;
            case "grid":
                view = mInflater.inflate(R.layout.mainrecycleritemgrid, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Movie movie = mData.get(position);
        if (movie.isLike()) holder.ivStar.setImageResource(R.drawable.ic_star_y_24dp);
        else holder.ivStar.setImageResource(R.drawable.ic_star_border_black_24dp);
        holder.ivStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movie.isLike()) {
                    movie.setLike(true);
                    holder.ivStar.setImageResource(R.drawable.ic_star_y_24dp);

                } else {
                    movie.setLike(false);
                    holder.ivStar.setImageResource(R.drawable.ic_star_border_black_24dp);
                }
            }
        });
        if (holder.ivPoster != null) {
            Glide.with(context).load("https://image.tmdb.org/t/p/w92/" +
                    movie.getPoster_path()).apply(RequestOptions.circleCropTransform()).into(holder.ivPoster);
        } else {
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w300/" + movie.getBackdrop_path())
                    .into(new CustomTarget<Drawable>() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            holder.posterBg.setBackground(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }

        holder.tvVoteCount.setText(String.valueOf(movie.getVote_count()));
        holder.tvVoteAverage.setText(String.valueOf(movie.getVote_average()));
        holder.tvTitle.setText(movie.getTitle());
    }

    @Override  // total number of rows
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivPoster, ivMovie, ivStar;
        TextView tvTitle, tvVoteCount, tvVoteAverage;
        LinearLayout posterBg;

        ViewHolder(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            ivStar = itemView.findViewById(R.id.ivStar);
            tvTitle = itemView.findViewById(R.id.titleMovie);
            tvVoteCount = itemView.findViewById(R.id.tvVotecount);
            tvVoteAverage = itemView.findViewById(R.id.tvFavorite);
            posterBg = itemView.findViewById(R.id.posterBg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Main activity take care of this
            ((MainActivity) context).openMovie(mData.get(getAdapterPosition()));
        }
    }

    public void setType(String type) {
        this.type = type;
    }
}