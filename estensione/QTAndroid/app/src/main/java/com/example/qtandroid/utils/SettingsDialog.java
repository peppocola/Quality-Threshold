package com.example.qtandroid.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.qtandroid.R;

public class SettingsDialog extends AppCompatDialogFragment {

    private EditText ip;
    private EditText port;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = ThemeUtils.getBuilder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.settings_layout, null);
        builder.setView(view);
        builder.setTitle("Impostazioni di connessione");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ConnectionHandler.getInstance().setAddress(ip.getText().toString());
                if (checkPort()) {
                    ConnectionHandler.getInstance().setPort(Integer.parseInt(port.getText().toString()));
                    dismiss();
                } else Toast.makeText(getContext(), "Porta non valida", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("CHIUDI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        ip = view.findViewById(R.id.ip);
        port = view.findViewById(R.id.port);


        String ips = ConnectionHandler.getInstance().getIp();

        if (ips != null && !ips.equals("")) {
            ip.setText(ips);
        }


        int checkport = ConnectionHandler.getInstance().getPort();

        if (checkport != 0) {
            port.setText(String.format("%d", checkport));
        }


        return builder.create();
    }


    private boolean checkPort() {
        String sPort = port.getText().toString();
        if (sPort.equals("")) return false;
        int iPort = Integer.parseInt(sPort);
        return 1024 <= iPort && iPort <= 65535;
    }

}