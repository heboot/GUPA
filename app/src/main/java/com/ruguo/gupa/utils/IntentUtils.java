package com.ruguo.gupa.utils;

import android.content.Context;
import android.content.Intent;

public class IntentUtils {

    private static Intent intent;

    public static void doIntent(Context context, Class clazz) {
        intent = new Intent(context, clazz);
        context.startActivity(intent);
    }


}
