package com.example.testeandroid.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.testeandroid.R;
import com.example.testeandroid.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    RecyclerView mainRecyclerView;
    ArrayList<Movie> allMovies;
    private RequestQueue queueData;
    private Context context;
    private DefaultRetryPolicy retryPolicy;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        this.queueData = Volley.newRequestQueue(context);
        mainRecyclerView = root.findViewById(R.id.mainRecyclerview);
        retryPolicy = new DefaultRetryPolicy(
                10000, 5, 2f);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    private void requestMovies() {
        String url = "http://" + "/product/subcategory/";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject m = response.getJSONObject(i);
                                Movie movie = new Movie(m.getString("id"), m.getString("name"));
                                allMovies.add(movie);
                            } catch (JSONException e) {
                                System.out.println(e);
                            }
                        }
                        //call something
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //cancelAllRequests();
                        System.out.println(error);
                    }
                }
        );
        request.setRetryPolicy(retryPolicy);
        request.setTag("requestMovies");
        queueData.add(request);
    }

}