package com.example.qtandroid.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qtandroid.MainActivity;
import com.example.qtandroid.R;

public final class ConnectionUtils {

    private ConnectionUtils() {
    }

    public static void openConnectionDialog(@NonNull final Context context) {

        AlertDialog.Builder builder;
        if (ThemeUtils.isDarkMode()) {
            builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        } else builder = new AlertDialog.Builder(context);

        builder
                .setTitle(R.string.alertConnection)
                .setMessage(R.string.alertConnectionMessage)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((AppCompatActivity) context).finish();
                    }
                })
                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (absentConnection(context)) {
                            openConnectionDialog(context);
                        } else
                            Toast.makeText(context, R.string.ConnectionOK, Toast.LENGTH_LONG).show();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void openConnectionDialogAbort(@NonNull final Context context) {

        AlertDialog.Builder builder;
        if (ThemeUtils.isDarkMode()) {
            builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        } else builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.alertConnection)
                .setMessage(R.string.alertConnectionMessage)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((AppCompatActivity) context).finish();
                        MainActivity.openMainActivity(context);
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public static boolean absentConnection(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        /*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return !(capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)));
        }else
        */
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return !(activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        } else {
            return true;
        }
    }

    public static void checkConnection(Context context) {
        if (ConnectionUtils.absentConnection(context)) {
            ConnectionUtils.openConnectionDialog(context);
        }
    }

}
