package kr.ac.kaist.vclab.Scenegraph;

import android.opengl.GLES20;

import kr.ac.kaist.vclab.util.MatOperator;

/**
 * Created by PCPC on 2016-10-05.
 */
public class ShaderState {
    public int  mProgram;

    // Handles to uniform variables
    public int mLightHandle, mLight2Handle;
    public int mProjMatrixHandle;
    public int mModelViewMatrixHandle;
    public int mNormalMatrixHandle;
    public int mColorHandle;
    public int h_uIdColor;

    // Handles to vertex attributes
    public int mPositionHandle;
    public int mNormalHandle;

    public ShaderState(int vertexShader, int fragmentShader){
        // prepare shaders and OpenGL program

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

        GLES20.glUseProgram(mProgram);

        // uniforms
        mProjMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uProjMatrix");
        mModelViewMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uModelViewMatrix");
        mNormalMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uNormalMatrix");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");
        mLightHandle = GLES20.glGetUniformLocation(mProgram, "uLight");
        mLight2Handle = GLES20.glGetUniformLocation(mProgram, "uLight2");


        // attributes
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
    }

    public static void sendModelViewNormalMatrix(ShaderState ss, float[] MVM){
        float NM[] = new float[16];
        MatOperator.normalMatrix(NM,0,MVM,0);
        GLES20.glUniformMatrix4fv(ss.mModelViewMatrixHandle, 1, false, MVM, 0);
        GLES20.glUniformMatrix4fv(ss.mNormalMatrixHandle, 1, false, NM, 0);
    }
}
