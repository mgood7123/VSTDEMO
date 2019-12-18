package com.example.vst.demo.java.classexecution;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.vstmanager.VST;
import com.example.vstmanager.VstManager;

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
