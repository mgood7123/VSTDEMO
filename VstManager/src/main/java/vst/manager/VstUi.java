package vst.manager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liblayout.Builder;
import com.example.liblayout.BuilderKt;
import com.example.liblayout.UiThread;

import java.util.Stack;

import kotlin.Triple;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import vst.manager.Helpers.ViewStack;
import vstmanager.R;

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

    class MyOnClickListener implements View.OnClickListener {
        Activity a;

        public void init(Activity b) {
            a = b;
        }

        @Override
        public void onClick(View v) {
            undoCommitView(a);
        }
    }

    private RecyclerView recyclerView;
    private VstPickerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout recyclerViewMain;
    private PackageInfo[] packages;
    private LinearLayout noPackagesFound;

    public LinearLayout vstPicker(final Activity activity, VstManager vstMan) {
        return vstPicker(activity, vstMan, null, null);
    }
    public LinearLayout vstPicker(
            final Activity activity, VstManager vstMan,
            VstUi vstUi, VstGridAdapter vstGridAdapter) {
        packages = vstMan.getPackages(activity);
        if (packages == null) {
            if (noPackagesFound == null) {
                noPackagesFound = (LinearLayout) LayoutInflater.from(activity.getApplicationContext())
                        .inflate(R.layout.picker_recycler_view_no_packages_found, null, false);
                MyOnClickListener t = new MyOnClickListener();
                t.init(activity);
                noPackagesFound
                        .findViewById(R.id.BackButton)
                        .setOnClickListener(t);
            }
            return noPackagesFound;
        }
        if (recyclerViewMain == null)
            recyclerViewMain = (LinearLayout) LayoutInflater.from(activity.getApplicationContext())
                    .inflate(R.layout.picker_recycler_view, null, false);
        if (recyclerView == null) {
            recyclerView = recyclerViewMain
                    .findViewById(R.id.RecyclerView);

            MyOnClickListener t = new MyOnClickListener();
            t.init(activity);
            recyclerViewMain
                    .findViewById(R.id.BackButton)
                    .setOnClickListener(t);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            recyclerView.setHasFixedSize(true);
        }
        if (layoutManager == null) {
            // use a linear layout manager
            layoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(layoutManager);
        }
        if (mAdapter == null) {
            // specify an adapter (see also next example)
            mAdapter = new VstPickerAdapter(activity, vstMan, vstUi, vstGridAdapter);
            recyclerView.setAdapter(mAdapter);
        }
        mAdapter.update();
        return recyclerViewMain;
    }

    public void commitView(Activity activity, View view) {
        viewStack.push(view);
        activity.setContentView(view);
    }

    public void commitView(Activity activity, int layoutResId) {
        View v = LayoutInflater.from(activity.getApplicationContext())
                .inflate(layoutResId, null, false);
        viewStack.push(v);
        activity.setContentView(v);
    }

    public boolean undoCommitView(Activity activity) {
        View x = popAndReturnNewLastElement();
        boolean succeeded = x != null;
        if (succeeded) activity.setContentView(x);
        return succeeded;
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
