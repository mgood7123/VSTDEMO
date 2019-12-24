package vst.manager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.TextView;

import com.example.liblayout.Builder;
import com.example.liblayout.BuilderKt;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import vst.manager.VstManager;
import vst.manager.VstUi;

public class MainActivity extends AppCompatActivity {
    VstManager vst = null;
    VstUi vstUi = null;
    Builder UI = null;

    private class MyOnClickListener implements View.OnClickListener {
        Activity a;

        public void init(Activity b) {
            a = b;
        }

        @Override
        public void onClick(View v) {
            // save current view for back navigation
            vstUi.commitView(a, vstUi.vstPicker(a, vst));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vst = new VstManager();
        vstUi = new VstUi();
        vstUi.commitView(this, R.layout.main);
        MyOnClickListener t = new MyOnClickListener();
        t.init(this);
        findViewById(R.id.button).setOnClickListener(t);
    }
}