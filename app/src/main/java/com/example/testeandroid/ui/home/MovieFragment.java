package com.example.testeandroid.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testeandroid.MainActivity;
import com.example.testeandroid.R;
import com.example.testeandroid.adapter.MainRecyclerViewAdapter;
import com.example.testeandroid.model.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class MovieFragment extends Fragment {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private RecyclerView mainRecyclerView;
    private MainRecyclerViewAdapter adapter;
    private ArrayList<Movie> showingMovies;
    private HashMap<String, Movie> offlineMovies;
    private RequestQueue queueData;
    private DefaultRetryPolicy retryPolicy;
    private String apikey = "25606ec67d22aa33004f509cbd5ee440";
    private Menu menu;
    private Gson gson;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.maintoolbar);
        toolbar.setTitle("Movies");
        prepareMenu();
        prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        editor = prefs.edit();
        gson = new Gson();
        this.queueData = Volley.newRequestQueue(getActivity());
        showingMovies = new ArrayList<>();
        Type type = new TypeToken<HashMap<String, Movie>>() {
        }.getType();
        offlineMovies = gson.fromJson(prefs.getString("offlinemovies", "[]"), type);
        //System.out.println("*************************************" + offlineMovies);
        mainRecyclerView = rootView.findViewById(R.id.mainRecyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mainRecyclerView.getContext(),
                lm.getOrientation());
        mainRecyclerView.addItemDecoration(dividerItemDecoration);
        mainRecyclerView.setLayoutManager(lm);
        adapter = new MainRecyclerViewAdapter(getActivity(), showingMovies);
        mainRecyclerView.setAdapter(adapter);
        retryPolicy = new DefaultRetryPolicy(
                10000, 5, 2f);
        requestPageMovies(1);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //prepareMenu();
    }

    private void prepareMenu() {

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        menu = ((MainActivity) getActivity()).getMenu();
                        MenuItem item = menu.findItem(R.id.menuSearch);
                        item.setVisible(true);
                        item = menu.findItem(R.id.menuListChange);
                        item.setVisible(true);
                        Toolbar toolbar = getActivity().findViewById(R.id.maintoolbar);
                        toolbar.setVisibility(View.VISIBLE);
                    }
                },
                500);
    }

    private void searchMovies(String name) {

    }

    private void requestPageMovies(int page) {
        showingMovies.clear();
        if (((MainActivity) getActivity()).workMode()) {
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
                                    showingMovies.add(movie);
                                    offlineMovies.put(m.getString("id"), movie);
                                }
                                editor.putString("offlinemovies", gson.toJson(offlineMovies));
                                editor.commit();
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
            request.setTag("requestPageMovies");
            queueData.add(request);
        } else {
            showingMovies.addAll(offlineMovies.values());
            adapter.notifyDataSetChanged();
        }

    }

}