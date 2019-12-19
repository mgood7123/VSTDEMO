package vst.manager;

import android.app.Activity;
import android.content.Context;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class VST {
    public String VST = null;
    public Activity activity = null;
    public Context activityApplicationContext;
    public Context context = null;
    public static class CLASS {
        String className = null;
        Class<?> CLASS = null;
        public ArrayList<Object> ClassInstances = null;
        public static class Methods {
            String methodName = null;
            Method method = null;
            public ArrayList<Object> methodInstances = null;
        }
        public ArrayList<Methods> methods = null;
    }
    public ArrayList<CLASS> classes = null;
}
