package com.example.vst_tests_static_new_execution_instance;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.InvocationTargetException;

import vst.manager.VST;
import vst.manager.VstManager;
import vst.manager.VstUi;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VstManager vstMan = new VstManager();

        VST pkg = vstMan.loadPackage(this, getPackageName(), false);
        VST.CLASS vstClass = vstMan.loadClass(pkg, "test");
        Object vstClassInstance = vstMan.newInstance(vstClass, "test");
        vstMan.invokeMethod(vstClass, vstClassInstance, "test");

        // INSTANCE ONE

            // new context
            VST vst0 = new VST(); vst0.activity = this; vst0.activityApplicationContext = vst0.activity.getApplicationContext(); vst0.VST = getPackageName(); try { vst0.context = vst0.activityApplicationContext.createPackageContext(vst0.VST, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY); } catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }

            // load class
            Class<?> c = null; try { c = vst0.context.getClassLoader().loadClass(vst0.VST + ".test"); } catch (ClassNotFoundException e) { e.printStackTrace(); }

            // new instance
            Object i = null; try { i = c.newInstance(); } catch (IllegalAccessException | InstantiationException e) { e.printStackTrace(); }

            // invoke method
            VST.CLASS.Methods m = new VST.CLASS.Methods(); try { m.method = c.getMethod("test1"); } catch (NoSuchMethodException e) { e.printStackTrace(); }
            Object o = null; try { o = m.method.invoke(i); } catch (IllegalAccessException | InvocationTargetException e) { e.printStackTrace(); }

        // INSTANCE TWO

            // new context
            VST vst1 = new VST(); vst1.activity = this; vst1.activityApplicationContext = vst1.activity.getApplicationContext(); vst1.VST = getPackageName(); try { vst1.context = vst1.activityApplicationContext.createPackageContext(vst1.VST, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY); } catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }

            // load class
            Class<?> c1 = null; try { c1 = vst1.context.getClassLoader().loadClass(vst1.VST + ".test"); } catch (ClassNotFoundException e) { e.printStackTrace(); }

            // new instance
            Object i1 = null; try { i1 = c1.newInstance(); } catch (IllegalAccessException | InstantiationException e) { e.printStackTrace(); }

            // invoke method
            VST.CLASS.Methods m1 = new VST.CLASS.Methods(); try { m1.method = c1.getMethod("test1"); } catch (NoSuchMethodException e) { e.printStackTrace(); }
            Object o1 = null; try { o1 = m.method.invoke(i1); } catch (IllegalAccessException | InvocationTargetException e) { e.printStackTrace(); }

    }
}
