package vst.manager;

import android.app.Activity;
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

import java.util.Stack;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import vst.manager.Helpers.ViewStack;

public class VstUi {
    public ViewStack viewStack = new ViewStack();
    Builder UI = null;

    public View.OnClickListener vstPickerOnClickListener(final Activity activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vstPicker(activity);
            }
        };
    }

    public void vstPicker(final Activity activity) {
        viewStackSelect(activity);
//        UiThread ui = new UiThread(activity);
//        Builder UI = BuilderKt.Builder(ui, viewStack.lastElement());
//        UI.row();
//        Button back = new Button(activity);
//        back.setText("Tap me to go back to the previous VST");
//        back.setOnClickListener(undoCommitViewOnClickListener(activity));
//        UI.column(back);
//        UI.row();
//        Button add = new Button(activity);
//        add.setText("Tap me to add a new VST, An Example VST");
//        add.setOnClickListener(vstPickerOnClickListener(activity));
//        UI.column(add);
//        UI.post(
//                new Function1<AbsoluteLayout, Unit>() {
//                    @Override
//                    public Unit invoke(AbsoluteLayout absoluteLayout) {
//                        // dont catch throws here
//                        commitView(activity, absoluteLayout);
//                        return null;
//                    }
//                }
//        );
//        UI.executeInNewThread();
    }

    public void viewStackSelect(final Activity activity) {
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
                UI.row();
                Stack<LinearLayout> linearLayouts = new Stack();
                for (PackageInfo pkg: a) {
                    if (index == maxColumns) {
                        UI.row();
                    }
                    LinearLayout ll = new LinearLayout(activity);
                    linearLayouts.push(ll);
                    UI.column(ll);
                }
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
                    INFO.row();
                    INFO.height(80);
                    INFO.column(title);
                    Button preview = new Button(activity);
                    preview.setText("Load VST");
                    preview.setOnClickListener(viewStackSelectOnClickListener(activity, a));
                    INFO.row();
                    INFO.column(preview);
                    INFO.build();
                }
            }
        }
    }

    public View.OnClickListener viewStackSelectOnClickListener(final Activity activity, final Stack<PackageInfo> a) {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!viewStack.empty()) {
                    if (a != null) {
                        UiThread ui = new UiThread(activity);
                        Builder UI = BuilderKt.Builder(ui, v, false);
                        UI.row();
                        UI.height(80);
                        Button back = new Button(activity);
                        back.setText("Tap me to go back to the previous VST");
                        back.setOnClickListener(undoCommitViewOnClickListener(activity));
                        UI.column(back);
                        int maxColumns = 4;
                        int index = 0;
                        UI.row();
                        Stack<LinearLayout> linearLayouts = new Stack();
                        for (PackageInfo pkg: a) {
                            if (index == maxColumns) {
                                UI.row();
                            }
                            LinearLayout ll = new LinearLayout(activity);
                            linearLayouts.push(ll);
                            UI.column(ll);
                        }
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
                            INFO.row();
                            INFO.height(80);
                            INFO.column(title);
                            Button preview = new Button(activity);
                            preview.setText("Load VST");
                            INFO.row();
                            INFO.column(preview);
                            INFO.build();
                        }
                    }
//                    vg.addView(new android.widget.CheckBox(activity));
//                    ViewGroup vgp = (ViewGroup) v.getParent();
//                    View c = ((ViewGroup) v).getChildAt(0);
//                    ((ViewGroup) v).removeView(c);
//                    vgp.removeView(v);
//                    vgp.addView(c);
                }
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
