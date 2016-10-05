package kr.ac.kaist.vclab.helloopengl3d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

/**
 * Created by sjjeon on 16. 9. 20.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    private float mPreviousX;
    private float mPreviousY;

    public int mode;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        int count = e.getPointerCount();

        float x = e.getX(0);
        float y = e.getY(0);

        if (count == 2) {
            x = (x + e.getX(1)) / 2;
            y = (y + e.getY(1)) / 2;
        }

        float dx = Math.max(Math.min(x - mPreviousX, 10f), -10f);
        float dy = Math.max(Math.min(y - mPreviousY, 10f), -10f);

        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            switch (mode) {
                case 0:
                    if (count == 1) {
                        // Rotate world
                    } else if (count == 2) {
                        // Translate world
                    }
                    break;
                case 1:
                    if (count == 1) {
                        // Rotate cube1
                    } else if (count == 2) {
                        // Translate cube1
                    }
                    break;
                case 2:
                    if (count == 1) {
                        // Rotate cube2
                    } else if (count == 2) {
                        // Translate cube2
                    }
                    break;
            }
        }

        mPreviousX = x;
        mPreviousY = y;

        requestRender();
        return true;
    }

}
