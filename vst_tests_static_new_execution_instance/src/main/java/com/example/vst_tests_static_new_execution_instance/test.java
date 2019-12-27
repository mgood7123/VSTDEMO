package com.example.vst_tests_static_new_execution_instance;

import android.util.Log;


public class test {
    String TAG = "test";

    static int static_i = 5;
    int local_i = 5;

    public void test() {
        Log.e(TAG, "I AM A TEST");
    }

    public void test1() {
        Log.e(TAG, "static_i before: " + static_i);
        static_i = 8;
        Log.e(TAG, "static_i after: " + static_i);
        Log.e(TAG, "local_i before: " + local_i);
        local_i = 8;
        Log.e(TAG, "local_i after: " + local_i);
    }
}
