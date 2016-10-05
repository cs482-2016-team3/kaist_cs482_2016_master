package kr.ac.kaist.vclab.Scenegraph;

import android.opengl.Matrix;

import kr.ac.kaist.vclab.util.Quaternion;

public class RigTForm extends Object {
    float translate[];
    Quaternion rotation;

    public RigTForm(){
        translate = new float[]{0,0,0};
        rotation = new Quaternion();
    }
    public RigTForm(RigTForm obj){
        float[] temp = obj.getTranslation();
        Quaternion tempQ = obj.getRotation();
        translate = new float[]{temp[0], temp[1], temp[2]};
        rotation = new Quaternion(tempQ);
        temp = null;
        tempQ = null;
    }

    public RigTForm(float[] t, Quaternion r){
        translate = new float[]{t[0], t[1], t[2]};
        rotation = new Quaternion(r);
    }
    public RigTForm(float[] t){
        translate = new float[]{t[0], t[1], t[2]};
        rotation = new Quaternion();
    }
    public RigTForm(Quaternion r){
        translate = new float[]{0,0,0};
        rotation = new Quaternion(r);
    }


    public float[] getTranslation(){
        return translate;
    }
    public void setTranslation(float[] t){
        translate[0] = t[0];
        translate[1] = t[1];
        translate[2] = t[2];
    }

    public Quaternion getRotation(){
        return rotation;
    }

    public float[] multiply(float a[]){
        float[] result= new float[4];
        result[0] = translate[0] * a[3];
        result[1] = translate[1] * a[3];
        result[2] = translate[2] * a[3];
        result[3] = 0;
        float temp[] = rotation.multiply(a);
        result[0] += temp[0];
        result[1] += temp[1];
        result[2] += temp[2];
        result[3] += temp[3];
        return result;
    }

    public RigTForm multiply(RigTForm rtf){
        RigTForm result;
        float temp[] = {translate[0], translate[1], translate[2]};
        float temp2[] = {rtf.translate[0], rtf.translate[1], rtf.translate[2], 0};
        float mult[] = rotation.multiply(temp2);
        temp[0] += mult[0];
        temp[1] += mult[1];
        temp[2] += mult[2];
        result = new RigTForm(temp, rotation.multiply(rtf.getRotation()));
        return result;
    }

    public static RigTForm inv(RigTForm tForm){
        Quaternion invRot = Quaternion.inv(tForm.getRotation());
        float[] temp = {-tForm.translate[0],-tForm.translate[1],-tForm.translate[2], 1};
        return new RigTForm(invRot.multiply(temp), invRot );
    }
    public static RigTForm transFact(RigTForm tForm){
        return new RigTForm(tForm.getTranslation());
    }
    public static RigTForm linFact(RigTForm tForm){
        return new RigTForm(tForm.getRotation());
    }

    public static float[] rigTFormToMatrix(RigTForm tForm) {
        float[] temp = tForm.getTranslation();
        float[] m = Quaternion.quatToMat(tForm.getRotation());
        float[] t = new float[16];
        Matrix.setIdentityM(t,0);
        Matrix.translateM(t,0, temp[0], temp[1], temp[2]);
        temp = new float[16];
        Matrix.multiplyMM(temp, 0, t, 0, m,0);

        return temp;
    }
}
