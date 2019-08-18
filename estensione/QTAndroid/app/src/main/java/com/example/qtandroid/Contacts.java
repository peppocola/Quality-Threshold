package com.example.qtandroid;

import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qtandroid.utils.ActivityUtils;
import com.example.qtandroid.utils.ThemeUtils;

public class Contacts extends AppCompatActivity {

    public static void openContacts(Context context) {
        ActivityUtils.open(Contacts.class, context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(ThemeUtils.defaultTheme());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView c1 = findViewById(R.id.Contributor1);
        TextView c2 = findViewById(R.id.Contributor2);
        TextView c3 = findViewById(R.id.Contributor3);


        c1.setMovementMethod(LinkMovementMethod.getInstance());
        c2.setMovementMethod(LinkMovementMethod.getInstance());
        c3.setMovementMethod(LinkMovementMethod.getInstance());


    }
}
