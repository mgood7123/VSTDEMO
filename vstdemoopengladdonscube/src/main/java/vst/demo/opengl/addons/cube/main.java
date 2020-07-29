package vst.demo.opengl.addons.cube;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;

// prevent optimizer from removing this class from classes*.dex

@Keep
public class main {
    NativeView n = null;
    Activity activity = null;
    Context context = null;

    public class MyListener implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            Toast toast = Toast.makeText(context,
                    "This demo combines Java UI and native EGL + OpenGL renderer",
                    Toast.LENGTH_LONG);
            toast.show();
        }
    }

    // aquire the view
    public ViewGroup onViewRequest(Context mContext) {
        Log.i(n.TAG, "onViewRequest(Activity, Context)");

        // build layout
        RelativeLayout rel = new RelativeLayout(context);
        rel.addView(n.surfaceView);

        // set text
        TextView text = new TextView(context);
        text.setText("Hello World! Try clicking the screen");
        text.setTextSize(60f);
        text.setTextColor(Color.WHITE);
        rel.addView(text);

        return rel;
    }

    // release the view
    public void onViewDestroy(Context mContext) {
        Log.i(n.TAG, "onViewDestroy(Context, ViewGroup)");
    }

    public void onCreate(Activity mActivity) {
        if (activity == null) activity = mActivity;
        if (context == null) context = activity;
        if (n == null) n = new NativeView(context);

        Log.i(n.TAG, "onCreate()");
        n.surfaceView.setOnClickListener(new MyListener());
    }

    public void onStart() {
        NativeView.nativeOnStart();
    }
    public void onResume() {
        NativeView.nativeOnResume();
    }
    public void onPause() {
        NativeView.nativeOnPause();
    }
    public void onStop() {
        NativeView.nativeOnStop();
    }
    public void onDestroy() {
        activity = null;
        context = null;
        n = null;
    }
}
