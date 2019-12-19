package vst.demo.java.classexecution.addons.view;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

public class ChangeText {
    public ChangeText(Activity activity) {
        Log.e("ChangeText", "CALLED");
        TextView textView = activity.findViewById(R.id.textView);
        textView.setText("MODIFIED!");
    }
}
