package vst.demo.java.cpp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

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
            vstManager.initialize(v, "main");
        }
    }
}
