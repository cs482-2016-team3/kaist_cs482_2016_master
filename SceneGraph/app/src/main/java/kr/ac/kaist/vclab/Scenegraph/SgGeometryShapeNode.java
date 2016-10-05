package kr.ac.kaist.vclab.Scenegraph;

import android.opengl.GLES20;
import android.opengl.Matrix;

import kr.ac.kaist.vclab.robotObj.Geometry;

public class SgGeometryShapeNode extends SgShapeNode {
    private  Geometry geo;
    float[] affineM = new float[16];
    float[] color = new float[3];

    public SgGeometryShapeNode(Geometry g, float[] color){
        geo = g;
        System.arraycopy(color, 0,this.color, 0, 3);

        float transM[] = {0,0,0};
        float eulerAngle[] = {0,0,0,0};
        float scale[] = {1f,1f,1f};
        Matrix.setIdentityM(affineM, 0);


    }
    public SgGeometryShapeNode(Geometry g, float[] color, float[] translate, float[] eulerAngles, float[] scales){
        geo = g;
        System.arraycopy(color, 0, this.color, 0, 3);
        float[] temp = new float[16];
        float[] trans = new float[16];
        Matrix.setIdentityM(trans, 0);
        Matrix.translateM(trans, 0, translate[0], translate[1], translate[2]);
        Matrix.setIdentityM(temp, 0);
        Matrix.scaleM(temp, 0, scales[0], scales[1], scales[2]);
        Matrix.rotateM(temp, 0, eulerAngles[0], 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(temp, 0, eulerAngles[1], 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(temp, 0, eulerAngles[2], 0.0f, 0.0f, 1.0f);

        Matrix.multiplyMM(affineM, 0, trans, 0, temp, 0);



        //Problem
        //Make a Affine Matrix (affineM) based on given input.

    }

    @Override
    public float[] getAffineM(){
        return affineM;
    }

    @Override
    public void draw(ShaderState curSS){

        GLES20.glUniform3fv(curSS.mColorHandle, 1, color, 0);
        geo.draw(curSS);
    }

}
