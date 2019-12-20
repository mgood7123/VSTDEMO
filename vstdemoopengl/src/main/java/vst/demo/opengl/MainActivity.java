package vst.demo.opengl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import vst.manager.VST;
import vst.manager.VstManager;

public class MainActivity extends AppCompatActivity {
    VstManager vstManager = null;
    VST v = null;
    VST.CLASS c = null;
    Object i = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vstManager = new VstManager();
        v = vstManager.loadPackage(this, "cube", true);
        c = vstManager.loadClass(v, "main");
        i = vstManager.newInstance(c, "main");
        vstManager.invokeMethod(
                c, i,
                "onCreate",
                new Class[] { Activity.class, Context.class},
                new Object[] { v.activity, v.activityApplicationContext }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        vstManager.invokeMethod(c, i, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        vstManager.invokeMethod(c, i, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        vstManager.invokeMethod(c, i, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        vstManager.invokeMethod(c, i, "onStop");
    }
}
