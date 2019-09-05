package br.com.itbam.pedipag.controller;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import br.com.itbam.pedipag.R;
import br.com.itbam.pedipag.image.BatchImageDownloadReceiver;
import br.com.itbam.pedipag.image.IImageDownloadReceiver;
import br.com.itbam.pedipag.image.ImageDowloadService;
import br.com.itbam.pedipag.model.ItemVenda;
import br.com.itbam.pedipag.printer.PrinterService;
import br.com.itbam.pedipag.view.MainActivity;
import br.com.itbam.pedipag.view.WelcomeActivity;

import static android.os.Looper.getMainLooper;

public class LUtil {
    private static Context context;
    public static int timeCounter = 0;

    public static void setContext(Context ctx) {
        context = ctx;
    }

    public static void highlightViewInLayout(View view, LinearLayout layout) {
        for (int index = 0; index < layout.getChildCount(); ++index) {
            View nextChild = layout.getChildAt(index);
            nextChild.setBackground(context.getDrawable(R.drawable.buttonmenu));
            if (view == nextChild)
                nextChild.setBackground(context.getDrawable(R.drawable.buttonmenuselected));
        }
    }

    public static void createProjectFolder() {
        File folder = new File(context.getApplicationContext().getFilesDir(), "pedpag/");
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
            ((MainActivity) context).init();
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

    public static void activateTimer(final TextView tv) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM - HH:mm:ss", Locale.getDefault());
        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timeCounter++;
                if (timeCounter > 60 * 1) {
                    ((MainActivity) context).showScreenSaver();
                }
                String dateString = sdf.format(System.currentTimeMillis());
                tv.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                someHandler.postDelayed(this, 1000);
                tv.setText(dateString);
            }
        }, 10);
    }

    public static void choosePrinter(ArrayList<ItemVenda> itens, String URL_NFCe) {
        if (isEmulator()) {
            System.out.println(print(itens));
        } else {
            new ImageDowloadService(context).setOnDownloadCompleted(new IImageDownloadReceiver() {
                @Override
                public void DownloadFinished(BatchImageDownloadReceiver receiver) {
                    PrinterService printer = new PrinterService();
                    printer.PrintBitmap(receiver.mBitmap);
                }
            }).StartDownload(ImageDowloadService.DownloadType.Other, URL_NFCe, -1);
        }
    }

    public static String fillSpaces(String str, int amount) {
        while (str.length() < amount) {
            str += " ";
        }
        return str;
    }

    public static String fp(String str, int amount) {
        return fillSpaces(str, amount);
    }


    public static String print(ArrayList<ItemVenda> itens) {
        String exitToPrinter = "";
        int index = 0;
        exitToPrinter += "123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*" + "\n";
        exitToPrinter += "item\tid\t" + fp("nome", 20) + fp("preço", 8) + "\tqntde\ttotal" + "\n";
        for (ItemVenda item : itens) {
            index++;
            exitToPrinter += String.format("%02d", index) + ":\t" + item.getId() + "\t" + fp(item.getName(), 20) + fp(NumberFormat.getCurrencyInstance().format(item.getPrice()), 8) + "\t\t" + item.getQuantity() +
                    "\t" + (NumberFormat.getCurrencyInstance().format(item.getPrice() * item.getQuantity())) + "\n";
        }
        return exitToPrinter;
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
