package vst.manager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.liblayout.Builder;
import com.example.liblayout.BuilderKt;
import com.example.liblayout.UiThread;

import java.util.Objects;
import java.util.Stack;

import kotlin.Pair;
import kotlin.Triple;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import vst.manager.Helpers.ViewStack;

public class VstUi {
    public ViewStack viewStack = new ViewStack();
    public Builder UI = null;

    public View.OnClickListener vstPickerOnClickListener(final Activity activity, final VstManager vstMan) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vstPicker(activity, vstMan);
            }
        };
    }

    public void vstPicker(final Activity activity, VstManager vstMan) {
        viewStackSelect(activity, vstMan);
    }

    public void viewStackSelect(final Activity activity, VstManager vstMan) {
        if (!viewStack.empty()) {
            Helpers.PackageManager pm = new Helpers.PackageManager(activity);
            Stack<PackageInfo> a = pm.getPackageInfo(".addons.");
            if (a != null) {
                UiThread ui = new UiThread(activity);
                Builder UI = BuilderKt.Builder(ui, viewStack.lastElement());
                UI.row();
                UI.height(80);
                Button back = new Button(activity);
                back.setText("Tap me to go back to the previous VST");
                back.setOnClickListener(undoCommitViewOnClickListener(activity));
                UI.column(back);
                int maxColumns = 4;
                int index = 0;
                int row = 0;
                UI.row("VST" + String.valueOf(row));
                Stack<LinearLayout> linearLayouts = new Stack();
                for (PackageInfo pkg: a) {
                    if (index == maxColumns) {
                        row++;
                        UI.row("VST" + String.valueOf(row));
                    }
                    LinearLayout ll = new LinearLayout(activity);
                    linearLayouts.push(ll);
                    UI.column("VST" + String.valueOf(index), ll);
                }
                UI.buildView();
                UI.post(
                        new Function1<AbsoluteLayout, Unit>() {
                            @Override
                            public Unit invoke(AbsoluteLayout absoluteLayout) {
                                // dont catch throws here
                                commitView(activity, absoluteLayout);
                                return null;
                            }
                        }
                );
                UI.executeInNewThread();
                for (int i = 0, linearLayoutsSize = linearLayouts.size(); i < linearLayoutsSize; i++) {
                    LinearLayout ll = linearLayouts.get(i);
                    PackageInfo pkg = a.get(i);
                    Builder INFO = BuilderKt.Builder(ui, ll, false);
                    Button title = new Button(activity);
                    title.setText("VST NAME: " + pkg.packageName);
                    INFO.row("TITLE");
                    INFO.height(80);
                    INFO.column("VST NAME: " + pkg.packageName, title);
                    Button preview = new Button(activity);
                    preview.setText("Load VST");
                    preview.setOnClickListener(viewStackSelectOnClickListenerLoadVST(activity, a, INFO, pkg.packageName, vstMan));
                    INFO.row("PREVIEW");
                    INFO.column("VST NAME: " + pkg.packageName, preview);
                    INFO.build();
                }
            }
        }
    }

    public View.OnClickListener viewStackSelectOnClickListenerLoadVST(final Activity activity, final Stack<PackageInfo> a, final Builder INFO, final String CLICKED_VST, final VstManager vstMan) {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                UiThread ui = new UiThread(activity);
                final Builder UI = BuilderKt.Builder(ui, v, false);
                UI.setMaxHeight(1);
                UI.setMaxWidth(1);
                UI.row();
//                UI.height(80);
                UI.column(
                    new Function1<Object, View>() {
                        @Override
                        public View invoke(Object data) {
                            Triple<Activity, String, VstManager> p = (Triple<Activity, String, VstManager>) data;
                            VstManager vstManager = p.component3();
                            VST v = vstManager.loadPackage(p.component1(), CLICKED_VST, false);
                            VST.CLASS c = vstManager.loadClass(v, "main");
                            Object i = vstManager.newInstance(c, "main");
                            Object r = vstManager.invokeMethod(
                                    c, i,
                                    "onViewRequest",
                                    new Class[] { Activity.class, Context.class},
                                    new Object[] { v.activity, v.activityApplicationContext }
                            );
                            vstManager.invokeMethod(
                                    c, i,
                                    "onStart",
                                    null,
                                    null
                            );
                            vstManager.invokeMethod(
                                    c, i,
                                    "onResume",
                                    null,
                                    null
                            );
                            return (View) r;
                        }
                    }, new Triple(activity, CLICKED_VST, vstMan)
                );
//                UI.row();
//                UI.height(20);
//                Button unload = new Button(activity);
//                unload.setText("Unload VST");
//                UI.column(unload);
                View x = UI.buildReturn(activity);
//                unload.setOnClickListener(viewStackSelectOnClickListenerUnloadVST(activity, a, INFO, CLICKED_VST, x, v));
                INFO.replaceView(v, x);
                return;
            }
        };
    }

    public View.OnClickListener viewStackSelectOnClickListenerUnloadVST(final Activity activity, final Stack<PackageInfo> a, final Builder INFO, final String CLICKED_VST, final View viewToRestore, final View originalClickedView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                undoCommitView(activity);
                return;
            }
        };
    }

    public View.OnClickListener commitViewOnClickListener(final Activity activity, final View view) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitView(activity, view);
            }
        };
    }

    public void commitView(Activity activity, View view) {
        viewStack.push(view);
        activity.setContentView(view);
    }

    public View.OnClickListener popOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        };
    }

    public View pop() {
        if (!viewStack.empty())
            return viewStack.pop();
        return null;
    }

    public View.OnClickListener removeLastOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLast();
            }
        };
    }

    public void removeLast() {
        if (!viewStack.empty()) {
            viewStack.pop();
            Runtime.getRuntime().gc();
        }
    }


    public View.OnClickListener popAndReturnNewLastElementOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAndReturnNewLastElement();
            }
        };
    }

    public View popAndReturnNewLastElement() {
        if (viewStack.size() == 1) return null;
        removeLast();
        if (!viewStack.empty())
            return viewStack.lastElement();
        return null;
    }

    public View.OnClickListener undoCommitViewOnClickListener(final Activity activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoCommitView(activity);
            }
        };
    }

    public boolean undoCommitView(Activity activity) {
        View x = popAndReturnNewLastElement();
        boolean succeeded = x != null;
        if (succeeded) activity.setContentView(x);
        return succeeded;
    }

    public View.OnClickListener replaceTopViewOnClickListener(final View view) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceTopView(view);
            }
        };
    }

    public boolean replaceTopView(View view) {
        if (!viewStack.empty()) {
            viewStack.pop();
            viewStack.push(view);
            return true;
        }
        return false;
    }
}
