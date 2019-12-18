package com.example.vst.demo.opengl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.vstmanager.VST;
import com.example.vstmanager.VstManager;

public class MainActivity extends AppCompatActivity {
    GLES3JNIView mView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VstManager vstManager = new VstManager();
//        VST v = vstManager.loadPackage(this, "cube");
        mView = new GLES3JNIView(getApplication());
        setContentView(mView);

//        int id = vstManager.getResourceId(v, "layout", "activity_main");
//        View view = vstManager.loadView(v, id);
//        if (view != null) {
//            setContentView(view);
//            vstManager.initialize(v, "main");
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }
}
