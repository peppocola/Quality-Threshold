package com.example.qtandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.qtandroid.utils.ActivityUtils;
import com.example.qtandroid.utils.ConnectionUtils;
import com.example.qtandroid.utils.SettingsDialog;
import com.example.qtandroid.utils.ThemeUtils;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

public class MainActivity extends AppCompatActivity {

    private Switch darkSwitch;
    private ProgressBar progressBar;

    private RadioGroup select;
    private RadioButton selected;

    public static void openMainActivity(Context context) {
        ActivityUtils.open(MainActivity.class, context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(ThemeUtils.defaultTheme());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        darkSwitch = findViewById(R.id.DarkSwitch);
        darkSwitch.setChecked(AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES);

        ThemeUtils.listen(MainActivity.this, darkSwitch);

        Button buttonDetails;
        Button buttonCluster;

        buttonDetails = findViewById(R.id.details);
        setDetailsButton(buttonDetails, MainActivity.this);

        progressBar = findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.INVISIBLE);
        select = findViewById(R.id.select);
        buttonCluster = findViewById(R.id.esegui);
        setClusterButton(buttonCluster, progressBar, MainActivity.this);

    }

    protected void setDetailsButton(Button button, final Context context) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Details.openDetails(context);
            }
        });
    }

    protected void setClusterButton(Button button, final ProgressBar progressBar, final Context context) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isNewCluster = select.getCheckedRadioButtonId() == R.id.newcluster;
                boolean isFileCluster = select.getCheckedRadioButtonId() == R.id.filecluster;

                if (!(isNewCluster || isFileCluster)) {
                    final AlertDialog.Builder builder = ThemeUtils.getBuilder(MainActivity.this);
                    builder
                            .setMessage(R.string.noSelect)
                            .setCancelable(true)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else if (ConnectionUtils.absentConnection(MainActivity.this)) {
                    ConnectionUtils.openConnectionDialog(MainActivity.this);
                } else {
                    Bundle bundle = new Bundle();
                    if (isNewCluster) {
                        bundle.putInt(AskData.TYPE, AskData.NEW_CLUSTER);
                        progressBar.setVisibility(View.VISIBLE);
                        AskData.openAskData(context, bundle);
                    } else {
                        bundle.putInt(AskData.TYPE, AskData.FILE_CLUSTER);
                        progressBar.setVisibility(View.VISIBLE);
                        AskData.openAskData(context, bundle);
                    }
                }
            }
        });
    }

    public void checkRadio(View v) {
        int radioId = select.getCheckedRadioButtonId();
        selected = findViewById(radioId);
        Toast.makeText(this, "Hai selezionato: " + selected.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = ThemeUtils.getBuilder(this);
        builder
                .setMessage(R.string.exit)
                .setCancelable(true)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.contacts) {
            Contacts.openContacts(MainActivity.this);
            return true;
        } else if (item.getItemId() == R.id.settings) {
            openSettingsDialog(MainActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        progressBar.setVisibility(View.INVISIBLE);
        super.onPostResume();

    }

    public void openSettingsDialog(Context context) {
        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.show(getSupportFragmentManager(), "SettingsDialog");


    }

}



