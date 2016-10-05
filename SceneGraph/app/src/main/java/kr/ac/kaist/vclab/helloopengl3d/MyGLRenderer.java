package kr.ac.kaist.vclab.helloopengl3d;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import kr.ac.kaist.vclab.Scenegraph.Drawer;
import kr.ac.kaist.vclab.Scenegraph.RbtAccumVisitor;
import kr.ac.kaist.vclab.Scenegraph.RigTForm;
import kr.ac.kaist.vclab.Scenegraph.SgGeometryShapeNode;
import kr.ac.kaist.vclab.Scenegraph.SgRbtNode;
import kr.ac.kaist.vclab.Scenegraph.SgRootNode;
import kr.ac.kaist.vclab.Scenegraph.ShaderState;
import kr.ac.kaist.vclab.robotObj.MyRobot;
import kr.ac.kaist.vclab.util.Constants;
import kr.ac.kaist.vclab.util.MatOperator;
import kr.ac.kaist.vclab.util.Quaternion;

/**
 * Created by sjjeon on 16. 9. 20.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";

    // -------- Shaders
    static final int g_numShaders = 3, g_numRegularShaders = 2;
    static final int PICKING_SHADER = 2;
    public Vector<ShaderState> g_shaderStates;
    public int g_activeShader = 0;

    SgRootNode g_world;
    SgRbtNode g_skyNode, g_groundNode, g_robot1Node, g_Arcball;
    MyRobot robotFactory;

    SgRbtNode g_currentCameraNode;
    SgRbtNode g_currentPickedRbtNode;

    public static final int ARCBALL_ON_PICKED = 0;
    public static final int ARCBALL_ON_SKY = 1;
    public static final int EGO_MOTION = 2;
    private int manipMode;

    private int viewWidth;
    private int viewHeight;
    private float g_frustMinFov = 60.0f;
    private float g_frustFovY = g_frustMinFov;


    public static float g_arcballScale = 0.1f;
    public static final float arcBallRadius = 0.4f;
    private Sphere mArcBall;
    private float[] mTempMatrix = new float[16];
    public float [] mArcBallRotationMatrix = new float[16];
    public float [] mArcBallTranslationMatrix = new float[16];
    public float[] mArcBallModelMatrix = new float[16];
    private float[] mArcBallModelViewMatrix = new float[16];
    private float[] mArcBallNormalMatrix = new float[16];

    public float [] mIdentityMatrix = new float[16];

    public float [] mViewRotationMatrix = new float[16];
    public float [] mViewTranslationMatrix = new float[16];
    private float[] mViewMatrix = new float[16];

    public float [] mOrthoMatrix = new float[16];
    private float[] mProjMatrix = new float[16];

    private float[] mLight = new float[3];
    private float[] mLight2 = new float[3];

    float ratio = 0;
    float left = -ratio;
    float right = ratio;
    final float bottom = -1.0f;
    final float top = 1.0f;
    final float near = 1f;
    final float far = 100.0f;

    float touchedX = -1, touchedY = -1;
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        manipMode = EGO_MOTION;


        // Set the background frame color
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);


        mLight = new float[] {2.0f, 3.0f, 14.0f};
        mLight2 = new float[] {-2.0f, -3.0f, -5.0f};


        // Initialize arcball
        mArcBall = new Sphere();
        mArcBall.color = new float[] {0.5f, 0.5f, 0.5f};


        robotFactory = new MyRobot();

        initShaders();
        initScene();

    }

    public float[] getSphereCenterScreen() {
        float[] temp = new float[4];
        float[] coord = new float[4];

        Matrix.multiplyMV(temp, 0, mArcBallModelViewMatrix, 0, new float[]{0, 0, 0, 1.0f}, 0);
        Matrix.multiplyMV(coord, 0, mProjMatrix, 0, temp, 0);

        coord[0] /= coord[3];
        coord[1] /= coord[3];
        coord[2] /= coord[3];
        coord[3] /= coord[3];

        coord[0] *= viewWidth / 2;
        coord[1] *= -viewHeight / 2;

        coord[0] += viewWidth / 2;
        coord[1] += viewHeight / 2;

        return coord;
    }
    public float getSphereRadius() {
        return arcBallRadius * viewHeight / 2;
    }

    private void normalMatrix(float[] dst, int dstOffset, float[] src, int srcOffset) {
        Matrix.invertM(dst, dstOffset, src, srcOffset);
        dst[12] = 0;
        dst[13] = 0;
        dst[14] = 0;

        float[] temp = Arrays.copyOf(dst, 16);

        Matrix.transposeM(dst, dstOffset, temp, 0);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glUseProgram(g_shaderStates.get(g_activeShader).mProgram);
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        drawStuff(g_shaderStates.get(g_activeShader), false);



    }

//    public void drawArcball(){
//        // Calculate Sphere ModelMatrix
//        Matrix.setIdentityM(mArcBallModelMatrix, 0);
//        Matrix.multiplyMM(mTempMatrix, 0, mArcBallRotationMatrix, 0, mArcBallModelMatrix, 0);
//        System.arraycopy(mTempMatrix, 0, mArcBallModelMatrix, 0, 16);
//        Matrix.multiplyMM(mTempMatrix, 0, mArcBallTranslationMatrix, 0, mArcBallModelMatrix, 0);
//        System.arraycopy(mTempMatrix, 0, mArcBallModelMatrix, 0, 16);
//        Matrix.scaleM(mArcBallModelMatrix, 0, arcBallRadius, arcBallRadius, arcBallRadius);
//        Matrix.multiplyMM(mArcBallModelViewMatrix, 0, mViewMatrix, 0, mArcBallModelMatrix, 0);
//        normalMatrix(mArcBallNormalMatrix, 0, mArcBallModelViewMatrix, 0);
//        //mArcBall.draw(mProjMatrix, mArcBallModelViewMatrix, mArcBallNormalMatrix, mLight, mLight2);
//
//    }
    public void updateViewMatrix(){

    }
    public void drawStuff(ShaderState curSS, boolean picking){
        final RigTForm eyeRbt = RbtAccumVisitor.getPathAccumRbt(g_world, g_currentCameraNode);
        final RigTForm invEyeRbt = RigTForm.inv(eyeRbt);


        float mL[] = {mLight[0], mLight[1], mLight[2], 1};
        float mL2[] = {mLight2[0], mLight2[1], mLight2[2], 1};
        final float eyeLight1[] = invEyeRbt.multiply(mL);
        final float eyeLight2[] = invEyeRbt.multiply(mL2);


        GLES20.glUniform3f(curSS.mLightHandle, eyeLight1[0], eyeLight1[1], eyeLight1[2]);
        GLES20.glUniform3f(curSS.mLight2Handle, eyeLight2[0], eyeLight2[1], eyeLight2[2]);

        Drawer drawer = new Drawer(invEyeRbt, curSS);
        g_world.accept(drawer);



        if (shouldUseArcball()) {
            //drawArcBall(curSS);
        }

    }
    public void setArcballTransM(){
        //mArcball.resetTranslationM();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        viewWidth = width;
        viewHeight = height;

        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.

        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
        Matrix.orthoM(mOrthoMatrix, 0, left, right, bottom, top, near, far);
        GLES20.glUniformMatrix4fv(g_shaderStates.get(g_activeShader).mProjMatrixHandle, 1, false, mProjMatrix, 0);
    }

    private void resetViewMatrix() {
        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = 10.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }

    public void initShaders() {
        g_shaderStates = new Vector<>();
        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShaderFromFile(
                GLES20.GL_VERTEX_SHADER, "basic-gl2.vshader");
        int fragmentShader = MyGLRenderer.loadShaderFromFile(
                GLES20.GL_FRAGMENT_SHADER, "diffuse-gl2.fshader");
        g_shaderStates.add(new ShaderState(vertexShader, fragmentShader));

    }

    public void initScene(){
        g_world = new SgRootNode();

        g_skyNode = new SgRbtNode(new RigTForm(new float[]{0f,0.25f, 4.0f}));

        float[] trans = {0,-1,0};
        g_groundNode= new SgRbtNode();
        //public SgGeometryShapeNode(Geometry g, float[] color, float[] translate, float[] eulerAngles, float[] scales)
        g_groundNode.addChild(new SgGeometryShapeNode(new Square(), new float[]{0.3f,0.9f, 0.9f}, new float[]{0,-3,-8}, new float[]{-90f, 1.0f, 0,0}, new float[]{8,8,8} ));


        g_world.addChild(g_skyNode);
        g_world.addChild(g_groundNode);

        //Problem
        //Construct a robot and link to the world (g_world)
        float[] tempColor = {0.0f, 1.0f, 0.0f};

        g_world.addChild(robotFactory.constructRobot(tempColor));

        g_currentCameraNode = g_skyNode;
    }


    public int getManipMode() {
        // if nothing is picked or the picked transform is the transfrom we are viewing from
        if (g_currentPickedRbtNode == null || g_currentPickedRbtNode == g_currentCameraNode) {
            if (g_currentCameraNode == g_skyNode )
                return ARCBALL_ON_SKY;
            else
                return EGO_MOTION;
        }
        else
            return ARCBALL_ON_PICKED;
    }

    public boolean shouldUseArcball() {
        return getManipMode() != EGO_MOTION;
    }

    // The translation part of the aux frame either comes from the current
// active object, or is the identity matrix when
    public RigTForm getArcballRbt() {
        switch (getManipMode()) {
            case ARCBALL_ON_PICKED:
                return RbtAccumVisitor.getPathAccumRbt(g_world, g_currentPickedRbtNode);
            case ARCBALL_ON_SKY:
                return new RigTForm();
            case EGO_MOTION:
                return RbtAccumVisitor.getPathAccumRbt(g_world, g_currentCameraNode);
            default:
                throw new RuntimeException("Invalid ManipMode");
        }
    }

    public void updateArcballScale() {
        RigTForm arcballEye = RigTForm.inv(RbtAccumVisitor.getPathAccumRbt(g_world, g_currentCameraNode)).multiply(getArcballRbt());
        float depth = arcballEye.getTranslation()[2];
        if (depth > -Constants.CS175_EPS)
            g_arcballScale = 0.02f;
        else
            g_arcballScale = getScreenToEyeScale(depth);
    }
    float getScreenToEyeScale(float z) {
        return (float)(-(z * Math.tan(g_frustFovY * Constants.CS175_PI / 360.0)) * 2 / viewHeight);
    }
    public RigTForm moveArcball(float[] p0, float[] p1) {
        RigTForm eyeInverse = RigTForm.inv(RbtAccumVisitor.getPathAccumRbt(g_world, g_currentCameraNode));
        float[] arcballCenter = getArcballRbt().getTranslation();
        float[] centerTemp = new float[4];
        System.arraycopy(centerTemp, 0,arcballCenter, 0, 3);
        centerTemp[3] = 1;
        float[] temp = eyeInverse.multiply(centerTemp);
        float[] arcballCenter_ec = new float[3];
        System.arraycopy(arcballCenter_ec, 0, temp, 0, 3);


        if (arcballCenter_ec[2] > -Constants.CS175_EPS)
            return new RigTForm();

        float[] ballScreenCenter = getSphereCenterScreen();
        p0[0] -= ballScreenCenter[0];
        p1[1] -= ballScreenCenter[1];
        p0[0] -= ballScreenCenter[0];
        p1[1] -= ballScreenCenter[1];
        float scrRad = getSphereRadius();
        float[] v0 = getArcballDireciton(p0, scrRad);
        float[] v1 = getArcballDireciton(p1, scrRad);

        return new RigTForm(new Quaternion(0.0f, v1[0], v1[1], v1[2]).multiply(new Quaternion(0.0f, -v0[0], -v0[1], -v0[2])));
    }

    public float[] getArcballDireciton(float[] p, float r) {
        float n2 = p[0] * p[0] + p[1] * p[1];

        if (n2 >= r * r) {
            return MatOperator.normalize(new float[]{p[0], -p[1], 0});
        } else {
            return MatOperator.normalize(new float[]{p[0], -p[1], (float) Math.sqrt(r * r - n2)});
        }
    }

    public RigTForm getMRbt(float dx, float dy) {
        RigTForm M;
        if (shouldUseArcball())
            M = moveArcball(new float[]{touchedX, touchedY}, new float[]{touchedX + dx, touchedY + dy});
        else
            M = new RigTForm(Quaternion.makeXRotation(-dy).multiply(Quaternion.makeYRotation(dx)));


        switch (getManipMode()) {
            case ARCBALL_ON_PICKED:
                break;
            case ARCBALL_ON_SKY:
                M = RigTForm.inv(M);
                break;
            case EGO_MOTION:
                //if (g_mouseLClickButton && !g_mouseRClickButton) // only invert rotation
                    M = RigTForm.inv(M);
                break;
        }
        return M;
    }

    static RigTForm makeMixedFrame( RigTForm objRbt,  RigTForm eyeRbt) {
        return RigTForm.transFact(objRbt).multiply(RigTForm.linFact(eyeRbt));
    }

// l = w X Y Z
// o = l O
// a = w A = l (Z Y X)^1 A = l A'
// o = a (A')^-1 O
//   => a M (A')^-1 O = l A' M (A')^-1 O

    public void motion(float x, float y) {

        if(touchedX > -1) {
            float dx = x - touchedX;
            float dy = viewHeight - y - 1 - touchedY;

            RigTForm M = getMRbt(dx, dy);   // the "action" matrix

            // the matrix for the auxiliary frame (the w.r.t.)
            RigTForm A = makeMixedFrame(getArcballRbt(), RbtAccumVisitor.getPathAccumRbt(g_world, g_currentCameraNode));

            SgRbtNode target = new SgRbtNode();
            switch (getManipMode()) {
                case ARCBALL_ON_PICKED:
                    target = g_currentPickedRbtNode;
                    break;
                case ARCBALL_ON_SKY:
                    target = g_skyNode;
                    break;
                case EGO_MOTION:
                    target = g_currentCameraNode;
                    break;
            }

            A = RigTForm.inv(RbtAccumVisitor.getPathAccumRbt(g_world, target, 1)).multiply(A);

            target.setRbt(doMtoOwrtA(M, target.getRbt(), A));

            touchedX += dx;
            touchedX += dy;
        }else{
            touchedX = 0;
            touchedY = 0;
        }
    }
    public RigTForm doMtoOwrtA(RigTForm M, RigTForm O, RigTForm A) {
        return A.multiply(M).multiply(RigTForm.inv(A)).multiply(O);
    }

    public void drawArcBall(ShaderState curSS) {
        // switch to wire frame mode


        RigTForm arcballEye = RigTForm.inv(RbtAccumVisitor.getPathAccumRbt(g_world, g_currentCameraNode)).multiply(getArcballRbt());

        float[] MVM = new float[16];
                MatOperator.multiply(RigTForm.rigTFormToMatrix(arcballEye), g_arcballScale * getSphereRadius());
        ShaderState.sendModelViewNormalMatrix(curSS, MVM);

        // Draw the geometry with triangles

        mArcBall.draw(curSS,GLES20.GL_LINES);
    }

    // Utility method for compiling a OpenGL shader.
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
    void updateFrustFovY() {
        if (viewWidth >= viewHeight)
            g_frustFovY = g_frustMinFov;
        else {
            final double RAD_PER_DEG = 0.5 * Constants.CS175_PI/180;
            g_frustFovY = (float)(Math.atan2(Math.sin(g_frustMinFov * RAD_PER_DEG) * viewHeight / viewWidth, Math.cos(g_frustMinFov * RAD_PER_DEG)) / RAD_PER_DEG);
        }
    }
    public static int loadShader(int type, InputStream shaderFile) {
        String shaderCode = null;
        try {
            shaderCode = IOUtils.toString(shaderFile, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadShader(type, shaderCode);
    }

    public static int loadShaderFromFile(int type, String fileName) {
        try {
            return loadShader(type, MainActivity.context.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Bitmap loadImage(String fileName) {
        try {
            Bitmap tmp = BitmapFactory.decodeStream(MainActivity.context.getAssets().open(fileName));
            android.graphics.Matrix matrix = new android.graphics.Matrix();
            matrix.preScale(1.0f, -1.0f);
            Bitmap image = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);
            tmp.recycle();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

}