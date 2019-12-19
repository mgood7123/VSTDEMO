package vst.manager.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;

import com.example.liblayout.Builder;
import com.example.liblayout.BuilderKt;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import vst.manager.VstManager;
import vst.manager.VstUi;

public class MainActivity extends AppCompatActivity {
    VstManager vst = null;
    VstUi vstUi = null;
    Builder UI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vst = new VstManager();
        vstUi = new VstUi();
        UI = BuilderKt.Builder(this);
        UI.row();
        Button add = new Button(this);
        add.setText("Tap me to add a new VST");
        add.setOnClickListener(vstUi.vstPickerOnClickListener(this));
        UI.column(add);
        final Activity a = this;
        UI.post(
                new Function1<AbsoluteLayout, Unit>() {
                    @Override
                    public Unit invoke(AbsoluteLayout absoluteLayout) {
                        // dont catch throws here
                        vstUi.commitView(a, absoluteLayout);
                        return null;
                    }
                }
        );
        UI.executeInNewThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
