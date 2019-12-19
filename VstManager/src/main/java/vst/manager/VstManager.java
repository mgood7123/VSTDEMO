package vst.manager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class VstManager {

    private String ADDONS = ".addons.";

    public VST loadPackage(Activity activity, String vstName) throws IllegalArgumentException {
        if (activity == null) throw new IllegalArgumentException("activity must not be null");
        if (vstName == null) throw new IllegalArgumentException("vstName must not be null");
        VST vst = new VST();
        vst.activity = activity;
        vst.activityApplicationContext = vst.activity.getApplicationContext();
        vst.VST = vst.activityApplicationContext.getPackageName() + ADDONS + vstName;
        try {
            vst.context = vst.activityApplicationContext.createPackageContext(
                    vst.VST,
                    Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY
            );
            return vst;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // IllegalArgumentException extends RuntimeException
    @SuppressWarnings("DuplicateThrows")
    public int getResourceId(VST vst, String directory, String file) throws IllegalArgumentException, RuntimeException {
        if (vst == null) throw new IllegalArgumentException("vst must not be null");
        if (directory == null) throw new IllegalArgumentException("directory must not be null");
        if (file == null) throw new IllegalArgumentException("file must not be null");
        if (vst.context != null) {
            int id = vst.context.getResources().getIdentifier(file, directory, vst.VST);
            if (id == 0)
                throw new RuntimeException(
                        "Resource '" + directory + "/" + file
                                + ".xml' does not exist in the package '" + vst.VST + "'"
                );
            return id;
        }
        return 0;
    }

    // should this be renamed to loadXML ?
    public View loadView(VST vst, int resourceId) throws IllegalArgumentException {
        if (vst == null) throw new IllegalArgumentException("vst must not be null");
        if (vst.context != null)
            return LayoutInflater
                    .from(vst.context)
                    .inflate(resourceId, null, false);
        return null;
    }

    public <T extends View> T findViewById(VST vst, int id) throws IllegalArgumentException {
        if (vst == null) throw new IllegalArgumentException("vst must not be null");
        // this will not work in a VST that wants to modify its layout
        return vst.activity.findViewById(id);
    }

    public Object initialize(VST vst, String className) throws IllegalArgumentException {
        if (vst == null) throw new IllegalArgumentException("vst must not be null");
        if (className == null) throw new IllegalArgumentException("className must not be null");
        try {
            Log.i("VstManager", "loading " + vst.VST + "." + className);
            Class<?> c = vst.context.getClassLoader().loadClass(vst.VST + "." + className);
            try {
                Log.i("VstManager", "loaded " + vst.VST + "." + className);
                Log.i("VstManager", "obtaining constructor");
                Constructor<?> constructor = c.getConstructor(Activity.class);
                Log.i("VstManager", "obtained constructor");
                Log.i("VstManager", "constructing new instance");
                try {
                    return constructor.newInstance(vst.activity);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public VST.CLASS loadClass(VST vst, String className) throws IllegalArgumentException {
        if (vst == null) throw new IllegalArgumentException("vst must not be null");
        if (className == null) throw new IllegalArgumentException("className must not be null");
        Log.i("VstManager", "loading class: " + vst.VST + "." + className);
        // check for an existing class
        if (vst.classes != null) {
            for (VST.CLASS CLASS : vst.classes)
                if (CLASS.className.equals(className)) {
                    return CLASS;
                }
        }
        Class<?> c;
        try {
            c = vst.context.getClassLoader().loadClass(vst.VST + "." + className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("VstManager", "failed to load class: " + vst.VST + "." + className);
            return null;
        }
        if (vst.classes == null) vst.classes = new ArrayList<>();
        VST.CLASS x = new VST.CLASS();
        x.className = className;
        x.CLASS = c;
        vst.classes.add(x);
        Log.i("VstManager", "loaded class: " + vst.VST + "." + className);
        return x;
    }

    public Object newInstance(VST.CLASS CLASS, String className) throws IllegalArgumentException {
        if (CLASS == null) throw new IllegalArgumentException("CLASS must not be null");
        if (className == null) throw new IllegalArgumentException("className must not be null");
        Log.i("VstManager", "Creating a new instance of " + className);
        if (CLASS.ClassInstances == null) CLASS.ClassInstances = new ArrayList<>();
        Object i;
        try {
            i = CLASS.CLASS.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            Log.e("VstManager", "failed to create a new instance of " + className);
            return null;
        }
        CLASS.ClassInstances.add(i);
        Log.i("VstManager", "Created a new instance of " + className);
        return i;
    }

    public Object invokeMethod(VST.CLASS CLASS, Object classInstance, String methodName, Class<?>[] parameterTypes, Object[] parameterValues) throws IllegalArgumentException {
        if (CLASS == null) throw new IllegalArgumentException("CLASS must not be null");
        if (classInstance == null) throw new IllegalArgumentException("classInstance must not be null");
        if (methodName == null) throw new IllegalArgumentException("methodName must not be null");
        Log.i("VstManager", "obtaining method: " + methodName);
        // check for an existing method
        VST.CLASS.Methods m = null;
        if (CLASS.methods != null)
            for (VST.CLASS.Methods METHOD : CLASS.methods)
                if (METHOD.methodName != null && METHOD.method != null)
                    if (METHOD.methodName.equals(methodName)) {
                        m = METHOD;
                        break;
                    }
        if (m == null) {
            m = new VST.CLASS.Methods();
            try {
                m.method = CLASS.CLASS.getMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                Log.i("VstManager", "failed to obtain method: " + methodName);
                return null;
            }
            m.methodName = methodName;
            if (CLASS.methods == null) CLASS.methods = new ArrayList<>();
            CLASS.methods.add(m);
        }
        Log.i("VstManager", "obtained method: " + methodName);
        Log.i("VstManager", "invoking method: " + methodName);
        Object o = null;
        try {
            o = m.method.invoke(classInstance, parameterValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            Log.i("VstManager", "failed to invoke method: " + methodName);
            return null;
        }
        Log.i("VstManager", "invoked method: " + methodName);
        if (m.methodInstances == null) m.methodInstances = new ArrayList<>();
        m.methodInstances.add(o);
        return o;
    }
}
