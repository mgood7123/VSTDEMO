package vst.manager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import vstmanager.R;

public class VstPickerAdapter extends RecyclerView.Adapter<VstPickerAdapter.MyViewHolder> {
    static String TAG = "VstPickerAdapter";
    private final Activity mActivity;
    private final VstManager mVstMan;
    private final VstUi mVstUi;
    private final VstGridAdapter mVstGridAdapter;
    private PackageInfo[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyPreviewViewHolder extends MyViewHolder {
        static String TAG = "VstPickerAdapter:MyPreviewViewHolder";
        public ImageView image;

        // each data item is just a string in this case
        public MyPreviewViewHolder(View plugin_preview) {
            super(plugin_preview);
            Log.w(TAG, "MyPreviewViewHolder(View plugin_preview)");
            hasPreview = true;
            title = plugin_preview.findViewById(R.id.plugin_title);
            image = plugin_preview.findViewById(R.id.plugin_preview_image);
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        static String TAG = "VstPickerAdapter:MyViewHolder";
        public Boolean hasPreview = false;
        public TextView title;

        // each data item is just a string in this case
        public MyViewHolder(View plugin) {
            super(plugin);
            Log.w(TAG, "MyViewHolder(View plugin)");
            title = plugin.findViewById(R.id.plugin_title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public VstPickerAdapter(Activity activity, VstManager vstMan, VstUi vstUi, VstGridAdapter vstGridAdapter) {
        Log.w(TAG, "VstPickerAdapter()");
        mActivity = activity;
        mVstMan = vstMan;
        mVstUi = vstUi;
        mVstGridAdapter = vstGridAdapter;
    }

    public void update() {
        Log.w(TAG, "update(String[] myDataset)");
        mDataset = mVstMan.getPackages(mActivity);
        notifyDataSetChanged();
    }

    int PACKAGE_TYPE_HAS_PREVIEW = 1;
    int PACKAGE_TYPE_HAS_NO_PREVIEW;

    @Override
    public int getItemViewType(final int position) {
//        PackageInfo pkg = mDataset[position];
        return PACKAGE_TYPE_HAS_NO_PREVIEW;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.w(TAG, "onCreateViewHolder(ViewGroup parent, int viewType)");
        if (viewType == PACKAGE_TYPE_HAS_PREVIEW) {
            return new MyPreviewViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.picker_recycler_view_item_with_preview, parent, false));
        } else {
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picker_recycler_view_item_without_preview, parent, false));
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.w(TAG, "onBindViewHolder(MyViewHolder holder, int position)");
        Log.w(TAG, "holder.hasPreview: " + holder.hasPreview.toString());
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(mDataset[position].packageName);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVstGridAdapter.pkg = mVstMan.loadPackage(mActivity, mDataset[position].packageName, false);
                mVstGridAdapter.vstClass = mVstMan.loadClass(mVstGridAdapter.pkg, "main");
                mVstGridAdapter.vstClassInstance = mVstMan.newInstance(mVstGridAdapter.vstClass, "main");
                ViewGroup r = (ViewGroup) mVstMan.invokeMethod(
                        mVstGridAdapter.vstClass, mVstGridAdapter.vstClassInstance,
                        "onViewRequest", Context.class,
                        mVstGridAdapter.pkg.activityApplicationContext
                );
                RelativeLayout x = new RelativeLayout(mActivity);
                x.addView(r);
                mVstGridAdapter.viewList.add(mVstGridAdapter.viewList.size() - 1, x);
                mVstMan.invokeMethod(mVstGridAdapter.vstClass, mVstGridAdapter.vstClassInstance, "onCreate",
                        new Class[] { Activity.class, ViewGroup.class},
                        new Object[] { mVstGridAdapter.pkg.activity, r }
                );
                mVstMan.invokeMethod(mVstGridAdapter.vstClass, mVstGridAdapter.vstClassInstance, "onStart");
                mVstMan.invokeMethod(mVstGridAdapter.vstClass, mVstGridAdapter.vstClassInstance, "onResume");
                mVstGridAdapter.notifyItemRangeChanged(mVstGridAdapter.viewList.size() - 2, mVstGridAdapter.viewList.size() - 1);
                mVstUi.undoCommitView(mActivity);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return (mDataset == null) ? 0 : mDataset.length;
    }
}
