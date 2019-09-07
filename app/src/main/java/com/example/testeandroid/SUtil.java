package com.example.testeandroid;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.Looper.getMainLooper;

public class SUtil {

    public static void createProjectFolder(Context context) {
        File folder = new File(context.getApplicationContext().getFilesDir(), "teste/");
        boolean success;
        if (!folder.exists()) {
            success = folder.mkdirs();
            if (success) {
                System.err.println("Pasta do projeto criada.");
            } else {
                System.err.println("Falha ao criar pasta do projeto.");
            }
        } else {
            System.err.println("A pasta do projeto já existe.");
        }
    }

    public static void checkConnection(final String srv, final int port) {//testa conexao com o servidor antes de inicializar
        System.out.println("Verificando conexão.");
        if (isReachable(srv, port, 10000)) {
            //((MainActivity) context).init();
        } else {
            Handler netHandler = new Handler(getMainLooper());
            netHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkConnection(srv, port);
                }
            }, 10000);
        }
    }

    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isReachable(String addr, int openPort, int timeOutMillis) {
        try {
            try (Socket soc = new Socket()) {
                soc.connect(new InetSocketAddress(addr, openPort), timeOutMillis);
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }
}
