package com.example.testeandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikelau.croperino.Croperino;
import com.mikelau.croperino.CroperinoConfig;
import com.mikelau.croperino.CroperinoFileUtil;

import java.io.File;

public class RegisterActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    ImageView uimg;
    TextView tvUName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Registro");
        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        editor = prefs.edit();
        tvUName = findViewById(R.id.editUName);
        uimg = findViewById(R.id.imgUser);
        uimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageOrigin();
            }
        });

        Button uOKBtn = findViewById(R.id.btOKUser);
        uOKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasName, hasImage;
                System.out.println("Check name, image anda save");
                if (tvUName.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Escolha um nome de usuário.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    hasName = true;
                }

                if (uimg.getDrawable() == null) {
                    Toast.makeText(RegisterActivity.this, "Escolha uma imagem.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    hasImage = true;
                }

                if (hasName && hasImage) {
                    editor.putString("username", tvUName.getText().toString());
                    editor.putString("userimage", CroperinoFileUtil.getTempFile().toString());
                    editor.putString("firsttime","no");
                    editor.commit();
                    finish();
                }
            }
        });

        new CroperinoConfig("IMG_" + System.currentTimeMillis() + ".jpg", "/teste/Pictures", "/sdcard/teste/Pictures");
        CroperinoFileUtil.verifyStoragePermissions(this);
        CroperinoFileUtil.setupDirectory(this);
    }

    private void chooseImageOrigin() {
        Croperino.prepareChooser(this, "Escolha uma imagem.", ContextCompat.getColor(this, android.R.color.background_dark));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CroperinoConfig.REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Croperino.runCropImage(CroperinoFileUtil.getTempFile(), this, true, 1, 1, R.color.gray, R.color.gray_variant);
                }
                break;
            case CroperinoConfig.REQUEST_PICK_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    CroperinoFileUtil.newGalleryFile(data, this);
                    Croperino.runCropImage(CroperinoFileUtil.getTempFile(), this, true, 0, 0, R.color.gray, R.color.gray_variant);
                }
                break;
            case CroperinoConfig.REQUEST_CROP_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = Uri.fromFile(CroperinoFileUtil.getTempFile());
                    System.out.println(uri.toString());
                    uimg.setImageURI(uri);
                }
                break;
            default:
                break;
        }
    }
}
