package kr.ac.kaist.vclab.robotObj;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import kr.ac.kaist.vclab.Scenegraph.ShaderState;

/**
 * Created by PCPC on 2016-10-04.
 */
public class Geometry {

    public FloatBuffer mVertexBuffer;
    public FloatBuffer mNormalBuffer;

    public static final int COORDS_PER_VERTEX = 3;
    public static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    public float vertices[];

    public float normals[];

    public float color[] = { 0.2f, 0.709803922f, 0.898039216f };


    public Geometry() {

    }

    public Geometry(Geometry g){
        mNormalBuffer = g.mNormalBuffer.duplicate();
        mVertexBuffer = g.mVertexBuffer.duplicate();

        vertices = new float[g.vertices.length];
        normals = new float[g.normals.length];
        System.arraycopy(vertices, 0,g.vertices,0,vertices.length);
        System.arraycopy(normals, 0,g.normals,0,normals.length);
        System.arraycopy(color, 0,g.color,0,color.length);
    }


    public void draw(ShaderState curSS) {
        // Enable the attributes used by our shader
        GLES20.glEnableVertexAttribArray(curSS.mPositionHandle);
        GLES20.glEnableVertexAttribArray(curSS.mNormalHandle);

        // bind vbo
        GLES20.glVertexAttribPointer(
                curSS.mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mVertexBuffer);

        GLES20.glVertexAttribPointer(
                curSS.mNormalHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mNormalBuffer);

        // Draw the geometry with triangles
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length / 3);

        GLES20.glDisableVertexAttribArray(curSS.mPositionHandle);
        GLES20.glDisableVertexAttribArray(curSS.mNormalHandle);
    }
    public void draw(ShaderState curSS, int mode) {
        // Enable the attributes used by our shader
        GLES20.glEnableVertexAttribArray(curSS.mPositionHandle);
        GLES20.glEnableVertexAttribArray(curSS.mNormalHandle);

        // bind vbo
        GLES20.glVertexAttribPointer(
                curSS.mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mVertexBuffer);

        GLES20.glVertexAttribPointer(
                curSS.mNormalHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mNormalBuffer);

        // Draw the geometry with triangles
        GLES20.glDrawArrays(mode, 0, vertices.length / 3);

        GLES20.glDisableVertexAttribArray(curSS.mPositionHandle);
        GLES20.glDisableVertexAttribArray(curSS.mNormalHandle);
    }
    public void setVertices(float[] vert){
        vertices = new float[vert.length];
        System.arraycopy(vert, 0, vertices, 0, vert.length);
    }
    public void setNormals(float[] norm){
        normals = new float[norm.length];
        System.arraycopy(norm, 0, normals, 0, norm.length);
    }
    public void setBuffer(){

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
    }
    public void setColor(float r, float g, float b){
        color[0] = r;
        color[1] = g;
        color[2] = b;
    }
}