package com.example.vstmanager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class VstManager {

    private String ADDONS = ".addons.";

    public VST loadPackage(Activity activity, String vstName) {
        VST vst = new VST();
        vst.activity = activity;
        vst.applicationContext = vst.activity.getApplicationContext();
        vst.VST = vst.applicationContext.getPackageName() + ADDONS + vstName;
        try {
            vst.context = vst.applicationContext.createPackageContext(
                    vst.VST,
                    Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY
            );
            return vst;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getResourceId(VST vst, String directory, String file) throws RuntimeException {
        if (vst !=  null) {
            if (vst.context != null) {
                int id = vst.context.getResources().getIdentifier(file, directory, vst.VST);
                if (id == 0)
                    throw new RuntimeException(
                            "Resource '" + directory + "/" + file
                                    + ".xml' does not exist in the package '" + vst.VST + "'"
                    );
                return id;
            }
        }
        return 0;
    }

    // should this be renamed to loadXML ?
    public View loadView(VST vst, int resourceId) throws RuntimeException {
        if (vst !=  null) {
            if (vst.context != null)
                return LayoutInflater
                        .from(vst.context)
                        .inflate(resourceId, null, false);
        }
        return null;
    }

    public <T extends View> T findViewById(VST vst, int id) {
        // this will not work in a VST that wants to modify its layout
        return vst.activity.findViewById(id);
    }

    public Object initialize(VST vst, String className) {
        try {
            Log.i("VstManager", "loading " + vst.VST + "." + className);
            Class c = vst.context.getClassLoader().loadClass(vst.VST + "." + className);
            try {
                Log.i("VstManager", "loaded " + vst.VST + "." + className);
                Log.i("VstManager", "obtaining constructor");
                Constructor constructor = c.getConstructor(Activity.class);
                try {
                    Log.i("VstManager", "obtained constructor");
                    Log.i("VstManager", "constructing new instance");
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

//    public Object loadClass(VST vst, String className) {
//        try {
//            Class c = vst.context.getClassLoader().loadClass("Init");
//            try {
//                try {
//                    Constructor constructor = c.getConstructor(Activity.class);
//                    try {
//                        return constructor.newInstance(vst.activity);
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
