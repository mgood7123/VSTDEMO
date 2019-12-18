package com.example.vst.demo.opengl;

public class graphicsManager {

    static {
        System.loadLibrary("internal_loader");
        load();
    }

    private static native void load();
    public static native void create();
    public static native void resize(int width, int height);
    public static native void step();
}
