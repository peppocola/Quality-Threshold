package com.example.qtandroid;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qtandroid.utils.ActivityUtils;
import com.example.qtandroid.utils.ThemeUtils;

public class Details extends AppCompatActivity {

    public static void openDetails(Context context) {
        ActivityUtils.open(Details.class, context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(ThemeUtils.defaultTheme());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
