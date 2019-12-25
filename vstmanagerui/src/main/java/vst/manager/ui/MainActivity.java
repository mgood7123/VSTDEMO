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
import vst.manager.VstGrid;
import vst.manager.VstManager;
import vst.manager.VstUi;

public class MainActivity extends AppCompatActivity {
    VstManager vstMan;
    VstUi vstUi;
    VstGrid vstGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vstMan = new VstManager();
        vstUi = new VstUi();
        vstGrid = new VstGrid(this, vstMan, vstUi);
        vstUi.commitView(this, R.layout.main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // outer this, this@MainActivity in Kotlin
                vstUi.commitView(MainActivity.this, vstGrid.getView());
            }
        });
    }
}