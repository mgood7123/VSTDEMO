package vst.demo.java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import vst.manager.VST;
import vst.manager.VstManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VstManager vstManager = new VstManager();
        VST v = vstManager.loadPackage(this, "view");
        int id = vstManager.getResourceId(v, "layout", "activity_main");
        View view = vstManager.loadView(v, id);
        if (view != null) {
            setContentView(view);
            int textViewId = vstManager.getResourceId(v, "id", "textView");
            TextView textView = vstManager.findViewById(v, textViewId);
            textView.setText("MODIFIED!");
        }
    }
}
