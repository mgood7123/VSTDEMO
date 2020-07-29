package vst.demo.opengl.cube.loading;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import vst.manager.VST;
import vst.manager.VstManager;

public class MainActivity extends AppCompatActivity {
        VstManager vstManager = null;
        VST v = null;
        VST.CLASS c = null;
        Object i = null;
        ViewGroup view = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                findViewById(R.id.aquire).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (vstManager == null) {
                                        vstManager = new VstManager();
                                        if (MainActivity.this.v == null) MainActivity.this.v = vstManager.loadPackage(MainActivity.this, "vst.demo.opengl.addons.cube", false);
                                        if (c == null) c = vstManager.loadClass(MainActivity.this.v, "main");
                                        if (i == null) i = vstManager.newInstance(c, "main");
                                        vstManager.invokeMethod(
                                                c, i,
                                                "onCreate",
                                                Activity.class,
                                                MainActivity.this.v.activity
                                        );
                                        view = (ViewGroup) vstManager.invokeMethod(
                                                c, i,
                                                "onViewRequest",
                                                Context.class,
                                                MainActivity.this.v.activityApplicationContext
                                        );
                                        ((ViewGroup) findViewById(R.id.l)).addView(view);
                                        vstManager.invokeMethod(c, i, "onStart");
                                        vstManager.invokeMethod(c, i, "onResume");
                                }
                        }
                });


                findViewById(R.id.release).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (vstManager != null) {
                                        vstManager.invokeMethod(c, i, "onPause");
                                        vstManager.invokeMethod(c, i, "onStop");
                                        vstManager.invokeMethod(
                                                c, i,
                                                "onViewDestroy",
                                                Context.class,
                                                MainActivity.this.v.activityApplicationContext
                                        );
                                        vstManager.invokeMethod(c, i, "onDestroy");

                                        ((ViewGroup) findViewById(R.id.l)).removeView(view);
                                        view = null;
                                        vstManager = null;
                                }
                        }
                });
        }

        @Override
        protected void onStart() {
                super.onStart();
                if (vstManager != null) vstManager.invokeMethod(c, i, "onStart");
        }

        @Override
        protected void onResume() {
                super.onResume();
                if (vstManager != null) vstManager.invokeMethod(c, i, "onResume");
        }

        @Override
        protected void onPause() {
                super.onPause();
                if (vstManager != null) vstManager.invokeMethod(c, i, "onPause");
        }

        @Override
        protected void onStop() {
                super.onStop();
                if (vstManager != null) vstManager.invokeMethod(c, i, "onStop");
        }
}