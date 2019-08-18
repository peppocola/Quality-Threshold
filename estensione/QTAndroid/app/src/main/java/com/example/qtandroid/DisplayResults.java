package com.example.qtandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qtandroid.utils.ActivityUtils;
import com.example.qtandroid.utils.ConnectionHandler;
import com.example.qtandroid.utils.ConnectionUtils;
import com.example.qtandroid.utils.ThemeUtils;

public class DisplayResults extends AppCompatActivity {

    private int type;
    private String result;

    public static void openDisplayResults(Context context, Bundle bundle) {
        ActivityUtils.openWithParams(DisplayResults.class, context, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeUtils.defaultTheme());
        Bundle bundle = getIntent().getExtras();
        result = bundle.getString(AskData.RESULT);
        type = bundle.getInt(AskData.TYPE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView textView = findViewById(R.id.textView);

        boolean isNull = result == null;
        boolean isEmpty = !isNull && result.equals(ConnectionHandler.EMPTY);
        boolean isFull = !isNull && !isEmpty && result.equals(ConnectionHandler.FULL);
        boolean fileNotFound = !isNull && type == AskData.FILE_CLUSTER && result.equals(ConnectionHandler.FNF);

        String title = "";
        String message = "";


        if (fileNotFound) {
            title = getResources().getString(R.string.TitleFNF);
            message = getResources().getString(R.string.MessageFNF);

        } else if (isFull) {
            title = getResources().getString(R.string.TitleIsFull);
            message = getResources().getString(R.string.MessageIsFull);

        } else if (isEmpty) {
            title = getResources().getString(R.string.TitleIsEmpty);
            message = getResources().getString(R.string.MessageIsEmpty);
        }

        if (isEmpty || isFull) {

            AlertDialog.Builder builder = ThemeUtils.getBuilder(this);
            builder
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            dialogInterface.cancel();
                            MainActivity.openMainActivity(DisplayResults.this);
                        }
                    })
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            dialogInterface.cancel();
                            Bundle bundle = new Bundle();
                            bundle.putInt(AskData.TYPE, type);
                            AskData.openAskData(DisplayResults.this, bundle);
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (fileNotFound) {

            AlertDialog.Builder builder = ThemeUtils.getBuilder(this);
            builder
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            dialogInterface.cancel();
                            MainActivity.openMainActivity(DisplayResults.this);
                        }
                    })
                    .setPositiveButton(R.string.GoToNewCluster, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            dialogInterface.cancel();
                            Bundle bundle = new Bundle();
                            bundle.putInt(AskData.TYPE, AskData.NEW_CLUSTER);
                            AskData.openAskData(DisplayResults.this, bundle);
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else {
            textView.setText(result);
        }

        Button home = findViewById(R.id.home);
        Button back = findViewById(R.id.back);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionHandler.getInstance().disconnect();
                MainActivity.openMainActivity(DisplayResults.this);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(AskData.TYPE, type);
                if (ConnectionUtils.absentConnection(DisplayResults.this)) {
                    ConnectionUtils.openConnectionDialogAbort(DisplayResults.this);
                    ConnectionHandler.getInstance().disconnect();
                } else AskData.openAskData(DisplayResults.this, bundle);
            }
        });
    }

    @Override
    public void onBackPressed() {

        Bundle bundle = new Bundle();
        bundle.putInt(AskData.TYPE, type);
        if (ConnectionUtils.absentConnection(DisplayResults.this)) {
            ConnectionUtils.openConnectionDialogAbort(DisplayResults.this);
            ConnectionHandler.getInstance().disconnect();
        } else {
            finish();
            AskData.openAskData(DisplayResults.this, bundle);

        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        ConnectionHandler.getInstance().disconnect();
        super.onDestroy();
    }
}
