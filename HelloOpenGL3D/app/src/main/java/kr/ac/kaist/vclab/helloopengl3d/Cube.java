package kr.ac.kaist.vclab.helloopengl3d;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by sjjeon on 16. 9. 20.
 */

public class Cube {

    private final int mProgram;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mNormalBuffer;

    // attribute handles
    private int mPositionHandle;
    private int mNormalHandle;

    // uniform handles
    private int mProjMatrixHandle;
    private int mModelViewMatrixHandle;
    private int mNormalMatrixHandle;

    private int mLightHandle;
    private int mLight2Handle;
    private int mColorHandle;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    private static float vertices[] = {
            // Front face
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            // Right face
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,

            // Back face
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            // Left face
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,

            // Top face
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,

            // Bottom face
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
    };

    private static float normals[] = {
            // Front face
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,

            // Right face
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,

            // Back face
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,

            // Left face
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,

            // Top face
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,

            // Bottom face
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f
    };

    float color[] = { 0.2f, 0.709803922f, 0.898039216f };


    public Cube() {
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mNormalBuffer = byteBuf.asFloatBuffer();
        mNormalBuffer.put(normals);
        mNormalBuffer.position(0);


        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShaderFromFile(
                GLES20.GL_VERTEX_SHADER, "basic-gl2.vshader");
        int fragmentShader = MyGLRenderer.loadShaderFromFile(
                GLES20.GL_FRAGMENT_SHADER, "diffuse-gl2.fshader");

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    public void draw(float[] projMatrix,
                     float[] modelViewMatrix,
                     float[] normalMatrix,
                     float[] light,
                     float[] light2) {
        GLES20.glUseProgram(mProgram);

        // uniforms
        mProjMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uProjMatrix");
        mModelViewMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uModelViewMatrix");
        mNormalMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uNormalMatrix");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");
        mLightHandle = GLES20.glGetUniformLocation(mProgram, "uLight");
        mLight2Handle = GLES20.glGetUniformLocation(mProgram, "uLight2");

        GLES20.glUniformMatrix4fv(mProjMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glUniformMatrix4fv(mModelViewMatrixHandle, 1, false, modelViewMatrix, 0);
        GLES20.glUniformMatrix4fv(mNormalMatrixHandle, 1, false, normalMatrix, 0);

        GLES20.glUniform3fv(mColorHandle, 1, color, 0);
        GLES20.glUniform3fv(mLightHandle, 1, light, 0);
        GLES20.glUniform3fv(mLight2Handle, 1, light2, 0);

        // attributes
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mNormalHandle);

        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mVertexBuffer);

        GLES20.glVertexAttribPointer(
                mNormalHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mNormalBuffer);

        // Draw the cube
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length / 3);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mNormalHandle);
    }
}
