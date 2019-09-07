package com.example.testeandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testeandroid.MainActivity;
import com.example.testeandroid.R;
import com.example.testeandroid.model.Movie;

import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private List<Movie> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public MainRecyclerViewAdapter(Context context, List<Movie> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.mainrecycleritem, parent, false);
        return new ViewHolder(view);
    }

    @Override // binds the data to the TextView in each row
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mData.get(position);
        //Glide.with(context).load("http://172.100.10.101:7202/" + prod.getImage()).placeholder(R.drawable.digital).into(holder.imgMovie);
        //holder.tvID.setText(movie.getId());
        holder.tvTitle.setText(movie.getTitle());
    }

    @Override  // total number of rows
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //ImageView imgMovie;
        TextView tvID, tvTitle;

        ViewHolder(View itemView) {
            super(itemView);
            //imgMovie = itemView.findViewById(R.id.imgMovie);
            //tvID = itemView.findViewById(R.id.idMovie);
            tvTitle = itemView.findViewById(R.id.titleMovie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //System.out.println("clicked!");
            ((MainActivity) context).openMovie(mData.get(getAdapterPosition()));
            //if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    /*public Movie getItem(int id) {
        return mData.get(id);
    }*/

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}