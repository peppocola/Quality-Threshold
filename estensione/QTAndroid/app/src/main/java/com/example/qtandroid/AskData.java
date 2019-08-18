package com.example.qtandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qtandroid.utils.ActivityUtils;
import com.example.qtandroid.utils.ConnectionHandler;
import com.example.qtandroid.utils.ConnectionUtils;
import com.example.qtandroid.utils.ThemeUtils;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

public class AskData extends AppCompatActivity {

    private int ID;
    private String radius = "";
    private Spinner spinner;
    private EditText askRadius;
    private boolean enabled = false;
    private AlertDialog alertDialog;

    private static final String IP = "paologas91.ddns.net";
    private static final int PORT = 8080;

    public static final String TYPE = "type";
    public static final String RESULT = "result";
    public static final String DEFAULT_SPINNER = "--------";
    public static final int NEW_CLUSTER = 1;
    public static final int FILE_CLUSTER = 2;

    public static void openAskData(Context context, Bundle bundle) {
        ActivityUtils.openWithParams(AskData.class, context, bundle);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(ThemeUtils.defaultTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_data);

        Bundle b = getIntent().getExtras();
        ID = -1;
        if (b != null)
            ID = b.getInt(TYPE);



        TextView textView = findViewById(R.id.titolox);
        if (ID == NEW_CLUSTER) {
            textView.setText(R.string.newcluster);
        } else if (ID == FILE_CLUSTER) {
            textView.setText(R.string.filecluster);
        } else finish();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button button;
        button = findViewById(R.id.eseguifc);
        setButton(button, AskData.this);

        spinner = findViewById(R.id.spinner);
        askRadius = findViewById(R.id.insRadius);

        askRadius.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                radius = askRadius.getText().toString();
                enabled = !(spinner.getSelectedItem().toString().equals(DEFAULT_SPINNER)) && !radius.isEmpty();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this,
                R.layout.color_spinner_layout);

        ConnectionUtils.checkConnection(this);
        try {
            ConnectionHandler connectionHandler = ConnectionHandler.getInstance();
            if (!connectionHandler.isConnected()) {
                if (connectionHandler.getIp() == null || connectionHandler.getIp().equals("") || connectionHandler.getPort() == 0) {
                    connectionHandler.setAddress(IP);
                    connectionHandler.setPort(PORT);
                }
                connectionHandler.connect();
            }
            if (connectionHandler.isConnected()) {
                LinkedList<String> tables = connectionHandler.getTables();
                tables.add(0, DEFAULT_SPINNER);
                adapter.addAll(tables);
            } else {
                AlertDialog.Builder builder = ThemeUtils.getBuilder(this);
                builder
                        .setTitle(R.string.ServerUnreachableTitle)
                        .setMessage(R.string.ServerUnreachableMessage)
                        .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.openMainActivity(AskData.this);
                                (AskData.this).finish();
                            }
                        })
                        .setCancelable(false);

                alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                enabled = !(spinner.getSelectedItem().toString().equals(DEFAULT_SPINNER)) && !radius.isEmpty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    protected void setButton(Button button, final Context context) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enabled) {

                    if (Double.parseDouble(radius) > 0) {
                        ConnectionUtils.checkConnection(context);
                        String result = "";
                        Bundle bundle = new Bundle();

                        try {

                            switch (ID) {
                                case NEW_CLUSTER:
                                    result = ConnectionHandler.getInstance().learnDB(spinner.getSelectedItem().toString(), Double.parseDouble(radius));
                                    bundle.putInt(TYPE, AskData.NEW_CLUSTER);
                                    break;
                                case FILE_CLUSTER:
                                    result = ConnectionHandler.getInstance().learnFile(spinner.getSelectedItem().toString(), Double.parseDouble(radius));
                                    bundle.putInt(TYPE, AskData.FILE_CLUSTER);
                                    break;
                                default:
                            }
                            bundle.putString(RESULT, result);
                            if (ConnectionUtils.absentConnection(AskData.this)) {
                                ConnectionUtils.openConnectionDialogAbort(AskData.this);
                                ConnectionHandler.getInstance().disconnect();
                            } else DisplayResults.openDisplayResults(context, bundle);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), R.string.zeroradius, Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getApplicationContext(), R.string.fill, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        ConnectionHandler.getInstance().disconnect();
    }

}
