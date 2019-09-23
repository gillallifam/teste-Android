package com.example.testeandroid.ui.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.testeandroid.MainActivity;
import com.example.testeandroid.R;
import com.example.testeandroid.ScrollingActivity;

import static android.content.Context.MODE_PRIVATE;

public class PerfilFragment extends Fragment {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Menu menu;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        editor = prefs.edit();
        Toolbar toolbar = getActivity().findViewById(R.id.maintoolbar);
        //toolbar.setVisibility(View.GONE);
        toolbar.setTitle("Profile");
        prepareMenu();
        TextView tv = root.findViewById(R.id.tvUserName);
        tv.setText(prefs.getString("username", "Unknow"));
        Bitmap bitmap = BitmapFactory.decodeFile(prefs.getString("userimage", ""));
        ImageView uimg = root.findViewById(R.id.ivUserImage);
        Glide.with(getActivity()).asBitmap().load(bitmap).apply(RequestOptions.circleCropTransform()).into(uimg);


        uimg.setImageBitmap(bitmap);
        Button btLogout = root.findViewById(R.id.btPerfilLogout);
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Confirmar");
                builder.setMessage("Deseja fazer logout?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString("firsttime", "yes");
                        editor.commit();
                        dialog.dismiss();
                        ((MainActivity) getActivity()).restart();
                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        return root;
    }

    private void prepareMenu() {
        this.menu = ((MainActivity) getActivity()).getMenu();
        MenuItem item = menu.findItem(R.id.menuSearch);
        item.setVisible(false);
        item = menu.findItem(R.id.menuListChange);
        item.setVisible(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}