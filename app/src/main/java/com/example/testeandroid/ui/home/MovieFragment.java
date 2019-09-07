package com.example.testeandroid.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testeandroid.R;
import com.example.testeandroid.adapter.MainRecyclerViewAdapter;
import com.example.testeandroid.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MovieFragment extends Fragment {

    private RecyclerView mainRecyclerView;
    private MainRecyclerViewAdapter adapter;
    private ArrayList<Movie> allMovies;
    private RequestQueue queueData;
    private DefaultRetryPolicy retryPolicy;
    private String apikey = "25606ec67d22aa33004f509cbd5ee440";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        this.queueData = Volley.newRequestQueue(getActivity());
        allMovies = new ArrayList<>();
        mainRecyclerView = rootView.findViewById(R.id.mainRecyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mainRecyclerView.getContext(),
                lm.getOrientation());
        mainRecyclerView.addItemDecoration(dividerItemDecoration);
        mainRecyclerView.setLayoutManager(lm);
        adapter = new MainRecyclerViewAdapter(getActivity(), allMovies);
        mainRecyclerView.setAdapter(adapter);
        retryPolicy = new DefaultRetryPolicy(
                10000, 5, 2f);
        requestMovies(1);
        return rootView;
    }

    private void searchMovies(String name) {

    }

    private void requestMovies(int page) {
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + apikey + "&language=pt&page=" + page;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray rst = response.getJSONArray("results");
                            for (int i = 0; i < rst.length(); i++) {
                                JSONObject m = rst.getJSONObject(i);
                                Movie movie = new Movie(m.getString("id"), m.getString("title"));
                                allMovies.add(movie);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }
        );
        request.setRetryPolicy(retryPolicy);
        request.setTag("requestMovies");
        queueData.add(request);
    }

}