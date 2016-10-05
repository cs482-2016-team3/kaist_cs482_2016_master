package kr.ac.kaist.vclab.helloopengl3d;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

/**
 * Created by sjjeon on 16. 9. 20.
 */

public class MainActivity extends Activity {

    public static Context context;
    private MyGLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new MyGLSurfaceView(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);

        ToggleButton worldButton = new ToggleButton(this);
        ToggleButton cube1Button = new ToggleButton(this);
        ToggleButton cube2Button = new ToggleButton(this);

        setButtonText(worldButton, "World");
        setButtonText(cube1Button, "Cube1");
        setButtonText(cube2Button, "Cube2");

        buttonLayout.addView(worldButton);
        buttonLayout.addView(cube1Button);
        buttonLayout.addView(cube2Button);

        final ToggleButton[] buttons = {worldButton, cube1Button, cube2Button};

        for (int i = 0; i < buttons.length; i++) {
            final ToggleButton button = buttons[i];

            final int finalI = i;
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (ToggleButton toggleButton : buttons) {
                            if (toggleButton != buttonView) {
                                toggleButton.setChecked(false);
                            }
                            mGLView.mode = finalI;
                        }
                    }
                }
            });
        }
        worldButton.setChecked(true);

        LinearLayout.LayoutParams glParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        glParams.weight = 1;

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        layout.addView(buttonLayout, buttonParams);
        layout.addView(mGLView, glParams);

        setContentView(layout);
    }

    private void setButtonText(ToggleButton button, String text) {
        button.setText(text);
        button.setTextOn(text);
        button.setTextOff(text);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
}
