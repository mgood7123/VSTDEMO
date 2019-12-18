package com.example.vst.demo.java.classexecution.addons.view;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Keep;

// prevent optimizer from removing this class from classes.dex

@Keep
public class main {
    public main(Activity activity) {
        Log.e("init", "CALLED");
        ChangeText c = new ChangeText(activity);
    }
}
