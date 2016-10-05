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

    private float mCurrentX = 0;
    private float mCurrentY = 0;

    private float mPreviousX;
    private float mPreviousY;

    private float[] temp1 = new float[16];
    private float[] temp2 = new float[16];

    private float[] arcBallRotation = new float[16];

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

    public float[] normalize(float[] vector) {
        float sum = 0;
        for (float v : vector) {
            sum += v * v;
        }
        float length = (float) Math.sqrt(sum);
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= length;
        }
        return vector;
    }


    public static void cross(float[] p1, float[] p2, float[] result) {
        result[0] = p1[1] * p2[2] - p2[1] * p1[2];
        result[1] = p1[2] * p2[0] - p2[2] * p1[0];
        result[2] = p1[0] * p2[1] - p2[0] * p1[1];
    }

    public static float dot(float[] p1, float[] p2) {
        return p1[0] * p2[0] + p1[1] * p2[1] + p1[2] * p2[2];
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

        float[] sphereCenter = mRenderer.getSphereCenterScreen();

        if (dx == 0 || dy == 0) {
            return false;
        }

        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            switch (mode) {
                case 0:
                    if (count == 1) {
                        // Rotate camera
                        //mRenderer.moveArcball(new float[]{x, y}, new float[]{mPreviousX, mPreviousY});
                        mRenderer.motion(x,y);
                    } else if (count == 2) {
                        // Translate camera
                        Matrix.translateM(mRenderer.mViewTranslationMatrix, 0, dx/ 100, -dy / 100, 0);
                        mCurrentX += dx/100;
                        mCurrentY -= dy/100;
                    }
                    break;
                case 1:
                    if (count == 1) {
                        // Rotate cube1
                        //moveArcball(new float[] {x, y}, new float[] {mPreviousX, mPreviousY}, sphereCenter, mRenderer.mViewRotationMatrix, mRenderer.mCubeRotationMatrix);
                    } else if (count == 2) {
                        // Translate cube1
                        //Matrix.translateM(mRenderer.mCubeTranslationMatrix, 0, dx/ 100, -dy / 100, 0);
                    }
                    break;
            }
        }

        recalculateArcballTranslation();



        mPreviousX = x;
        mPreviousY = y;

        requestRender();
        return true;
    }

    public void recalculateArcballTranslation() {
        switch (mode) {
            case 0:
                Matrix.setIdentityM(mRenderer.mArcBallTranslationMatrix, 0);
                break;
            case 1:
                //System.arraycopy(mRenderer.mCubeTranslationMatrix, 0, mRenderer.mArcBallTranslationMatrix, 0, 16);
                break;
        }
    }

}
