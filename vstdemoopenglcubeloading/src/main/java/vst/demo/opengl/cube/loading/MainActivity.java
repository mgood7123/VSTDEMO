package vst.demo.opengl.cube.loading;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import vst.manager.VST;
import vst.manager.VstManager;

public class MainActivity extends AppCompatActivity {
        VstManager vstManager = null;
        VST v = null;
        VST.CLASS c = null;
        Object i = null;
        ViewGroup view = null;
        ReentrantLock lock = new ReentrantLock();

        void vstLoad() {
                if (!lock.isHeldByCurrentThread()) lock.lock();
                final boolean isMainThread = Thread.currentThread() == getMainLooper().getThread();
                Log.i("THREAD: " + Thread.currentThread(), "vstLoad: " + isMainThread);
                if (vstManager == null) {
                        vstManager = new VstManager();
                        if (v == null) v = vstManager.loadPackage(
                                MainActivity.this,
                                "vst.demo.opengl.addons.cube",
                                false
                        );
                        if (c == null) c = vstManager.loadClass(v, "main");
                        if (i == null) i = vstManager.newInstance(c, "main");
                        vstManager.invokeMethod(
                                c, i,
                                "onCreate",
                                Activity.class,
                                v.activity
                        );
                        view = (ViewGroup) vstManager.invokeMethod(
                                c, i,
                                "onViewRequest",
                                Context.class,
                                v.activityApplicationContext
                        );
                        runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                        ((ViewGroup) findViewById(R.id.LINEARLAYOUT)).addView(view);
                                }
                        });
                        vstManager.invokeMethod(c, i, "onStart");
                        vstManager.invokeMethod(c, i, "onResume");
                }
                if (lock.isHeldByCurrentThread()) lock.unlock();
        }

        void vstUnload() {
                if (!lock.isHeldByCurrentThread()) lock.lock();
                if (vstManager != null) {
                        vstManager.invokeMethod(c, i, "onPause");
                        vstManager.invokeMethod(c, i, "onStop");
                        vstManager.invokeMethod(
                                c, i,
                                "onViewDestroy",
                                Context.class,
                                v.activityApplicationContext
                        );
                        vstManager.invokeMethod(c, i, "onDestroy");
                        runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                        ((ViewGroup) findViewById(R.id.LINEARLAYOUT)).removeView(view);
                                        view = null;
                                }
                        });
                        vstManager = null;
                }
                if (lock.isHeldByCurrentThread()) lock.unlock();
        }

        Thread loop; // this is initialized to null
        AtomicBoolean looping = new AtomicBoolean(false);
        Object loopPause = new Object();

        public void startThread() {
                if (loop == null) {
                        // our loop does not exist, create it and start it
                        looping.set(true);
                        loop = new Thread() {

                                @Override
                                public void run() {
                                        super.run();
                                        try {
                                                while (true) {
                                                        vstLoad();
                                                        Thread.sleep(75);
                                                        vstUnload();
                                                        Thread.sleep(75);
                                                        // if looping is false,
                                                        // pause the loop
                                                        if (!looping.get()) {
                                                                synchronized (loopPause) {
                                                                        loopPause.wait();
                                                                }
                                                        }
                                                }
                                        } catch (InterruptedException e) {
                                                e.printStackTrace();
                                        }
                                }
                        };
                        loop.start();
                } else {
                        // our loop is already running, it may be paused
                        if (!looping.get()) {
                                // our loop is paused, resume it
                                looping.set(true);
                                synchronized (loopPause) {
                                        loopPause.notify();
                                }
                        }
                }
        }

        void endThread() {
                if (looping.get() == false) return;
                // is our loop is not paused, pause it
                looping.set(false);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                findViewById(R.id.aquireLoop).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                startThread();
                        }
                });
                findViewById(R.id.releaseLoop).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                endThread();
                        }
                });

                findViewById(R.id.aquire).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                vstLoad();
                        }
                });


                findViewById(R.id.release).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                vstUnload();
                        }
                });

                // callOnClick
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