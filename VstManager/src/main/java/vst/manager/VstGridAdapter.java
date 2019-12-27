package vst.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Stack;

import vstmanager.R;

public class VstGridAdapter extends RecyclerView.Adapter<VstGridAdapter.MyViewHolder> {
    static String TAG = "VstGridAdapter";
    private final Activity mActivity;
    private final VstManager mVstMan;
    private VstUi mVstUI;
    public Stack<Object> viewList = new Stack<>();

    public VST pkg;
    public VST.CLASS vstClass;
    public Object vstClassInstance;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        String TAG = "VstGridAdapter:MyViewHolder";
        public boolean isAddButton;
        public ConstraintLayout button;
        public ViewGroup plugin;
        public VST pkg;
        public VST.CLASS vstClass;
        public Object vstClassInstance;

        public MyViewHolder(ViewGroup pluginToAdd) {
            super(pluginToAdd);
            Log.w(TAG, "MyViewHolder(View pluginToAdd)");
            plugin = pluginToAdd;

            pkg = VstGridAdapter.this.pkg;
            VstGridAdapter.this.pkg = null;

            vstClass = VstGridAdapter.this.vstClass;
            VstGridAdapter.this.vstClass = null;

            vstClassInstance = VstGridAdapter.this.vstClassInstance;
            VstGridAdapter.this.vstClassInstance = null;
        }

        public MyViewHolder(ConstraintLayout plugin) {
            super(plugin);
            Log.w(TAG, "MyViewHolder(ConstraintLayout plugin)");
            isAddButton = true;
            button = plugin;
        }
    }

    public VstGridAdapter(Activity activity, VstManager vstMan, VstUi vstUi) {
        Log.w(TAG, "VstGridAdapter()");
        mActivity = activity;
        mVstMan = vstMan;
        mVstUI = vstUi;
        viewList.add("BUTTON");
        notifyItemInserted(0);
    }

    public void update() {}

    @Override
    public int getItemViewType(final int position) {
        Log.w(TAG, "getItemViewType");
        return position;
    }

    public Boolean isEnd(final int position) {
        if (position == viewList.size()-1) return true;
        return false;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.w(TAG, "onCreateViewHolder(ViewGroup parent, int viewType)");
        if (!isEnd(viewType)) {
            // isStart
            LinearLayout x = new LinearLayout(mActivity);
            x.addView(
                    (ViewGroup) viewList.get(viewType),
                    new ViewGroup.LayoutParams(500, 500)
            );
            return new MyViewHolder(x);
        } else {
            // isEnd
            // TODO: cache this
            return new MyViewHolder((ConstraintLayout) LayoutInflater.from(mActivity)
                    .inflate(R.layout.vst_grid_add_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.w(TAG, "onBindViewHolder(MyViewHolder holder, int position) (" + position + ")");
        if (holder.isAddButton) {
            Log.w(TAG, "isAddButton");
            holder.button.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.w(TAG, "onClick");
                    mVstUI.commitView(mActivity, mVstUI.vstPicker(mActivity, mVstMan, mVstUI, VstGridAdapter.this));
                }
            });
        } else {
            Log.w(TAG, "isNotAddButton");
            Log.w(TAG, "holder.vstClass = " + holder.vstClass);
        }
    }

    @Override
    public int getItemCount() {
        return viewList.size();
    }
}