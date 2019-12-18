package com.example.vst.demo.java.cpp.addons.view;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

public class main {
    public main(Activity activity) {
        Log.e("main", "CALLED");
        // Example of a call to a native method
        TextView tv = activity.findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
