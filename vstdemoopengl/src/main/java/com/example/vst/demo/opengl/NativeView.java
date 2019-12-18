package com.example.vst.demo.opengl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class NativeView {
    String TAG = "EglSample";

    // this is part of graphics manager
    public native void nativeSetSurface(Surface surface);

    View surfaceView = null;
    SurfaceHolderCallback surfaceHolderCallback = null;

    public NativeView(Context context) {
        System.loadLibrary("nativeegl");
        surfaceHolderCallback = new SurfaceHolderCallback();
        surfaceView = new View(surfaceHolderCallback, context);
    }

    class View extends SurfaceView {
        public View(SurfaceHolder.Callback callback, Context context) {
            super(context);
            getHolder().addCallback(callback);
        }

        public View(SurfaceHolder.Callback callback, Context context, AttributeSet attrs) {
            super(context, attrs);
            getHolder().addCallback(callback);
        }

        public View(SurfaceHolder.Callback callback, Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            getHolder().addCallback(callback);
        }

        public View(SurfaceHolder.Callback callback, Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
            super(context, attrs, defStyle, defStyleRes);
            getHolder().addCallback(callback);
        }
    }

    class SurfaceHolderCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            nativeSetSurface(holder.getSurface());
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            nativeSetSurface(null);
        }
    }
}
