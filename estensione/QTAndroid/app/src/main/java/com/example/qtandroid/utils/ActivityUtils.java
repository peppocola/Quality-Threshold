package com.example.qtandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public final class ActivityUtils {

    private ActivityUtils() {
    }

    public static void open(Class<?> activity, Context context) {
        Intent i = new Intent(context, activity);
        context.startActivity(i);
    }

    public static void openWithParams(Class<?> activity, Context context, Bundle params) {
        Intent i = new Intent(context, activity);
        i.putExtras(params);
        context.startActivity(i);
    }

}
