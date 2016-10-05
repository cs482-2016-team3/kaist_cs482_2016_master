package kr.ac.kaist.vclab.util;

import android.opengl.Matrix;

public class Quaternion {

    public float quat[] = new float[4];

    public Quaternion(){
        quat[0] = 1f;
        quat[1] = 0f;
        quat[2] = 0f;
        quat[3] = 0f;
    }
    public Quaternion(Quaternion q){
        quat[0] = q.quat[0];
        quat[1] = q.quat[1];
        quat[2] = q.quat[2];
        quat[3] = q.quat[3];
    }
    public Quaternion(float w, float vec3[]){
        quat[0] = w;
        quat[1] = vec3[0];
        quat[2] = vec3[1];
        quat[3] = vec3[2];
    }
    public Quaternion(float w, float x, float y, float z){
        quat[0] = w;
        quat[1] = x;
        quat[2] = y;
        quat[3] = z;
    }
    public float get(int i){
        return quat[i];
    }

    public void add(Quaternion q){
        quat[0] += q.quat[0];
        quat[1] += q.quat[1];
        quat[2] += q.quat[2];
        quat[3] += q.quat[3];
    }

    public void subtract(Quaternion q){
        quat[0] -= q.quat[0];
        quat[1] -= q.quat[1];
        quat[2] -= q.quat[2];
        quat[3] -= q.quat[3];
    }

    public void multiply(float s){
        quat[0] *= s;
        quat[1] *= s;
        quat[2] *= s;
        quat[3] *= s;
    }

    public Quaternion multiply(Quaternion q){
        float y0 = quat[0] * q.quat[0] - quat[1] * q.quat[1] - quat[2] * q.quat[2] - quat[3] * q.quat[3];
        float y1 = quat[0] * q.quat[1] + quat[1] * q.quat[0] + quat[2] * q.quat[3] - quat[3] * q.quat[2];
        float y2 = quat[0] * q.quat[2] - quat[1] * q.quat[3] + quat[2] * q.quat[0] + quat[3] * q.quat[1];
        float y3 = quat[0] * q.quat[3] + quat[1] * q.quat[2] - quat[2] * q.quat[1] + quat[3] * q.quat[0];


        return  new Quaternion(y0, y1, y2, y3);

    }




    //float[4]
    public float[] multiply(float[] a){
        float[] result = {a[0]*quat[0] - a[1]*quat[1] - a[2] * quat[2] - a[3] * quat[3],
                a[0]*quat[1] + a[1]*quat[0] - a[2] * quat[3] + a[3] * quat[2],
                a[0]*quat[2] + a[1]*quat[3] + a[2] * quat[0] - a[3] * quat[1],
                a[0]*quat[3] - a[1]*quat[2] + a[2] * quat[1] + a[3] * quat[0]};
        return result;
    }

    public static Quaternion makeXRotation(double ang) {
        Quaternion r = new Quaternion();
        final double h = 0.5 * ang * Constants.CS175_PI/180;
        r.quat[1] = (float)Math.sin(h);
        r.quat[0] = (float)Math.cos(h);
        return r;
    }

    public static Quaternion makeYRotation(double ang) {
        Quaternion r = new Quaternion();
        final double h = 0.5 * ang * Constants.CS175_PI/180;
        r.quat[2] = (float)Math.sin(h);
        r.quat[0] =(float) Math.cos(h);
        return r;
    }

    public static Quaternion makeZRotation(double ang) {
        Quaternion r = new Quaternion();
        final double h = 0.5 * ang * Constants.CS175_PI/180;
        r.quat[3] = (float)Math.sin(h);
        r.quat[0] = (float)Math.cos(h);
        return r;
    }

    public static float dot(Quaternion q1, Quaternion q2){
        float s = 0f;
        for(int i =0; i < 4; ++i){
            s+= q1.quat[i] * q2.quat[i];
        }
        return s;
    }

    public static float norm2(Quaternion q1){
        return dot(q1,q1);
    }

    public static Quaternion inv(Quaternion q){
        final float n = norm2(q);
        assert (n > Constants.CS175_EPS2);
        return new Quaternion(q.quat[0], -q.quat[1], -q.quat[2], (float) (-q.quat[3] * (1.0/n)));
    }

    public static Quaternion normalize(Quaternion q){
        float temp = (float)Math.sqrt(norm2(q));
        q.multiply( 1.0f/temp );
        return new Quaternion(q);
    }

    public static float[] quatToMat(Quaternion q){
        float[] temp = new float[16];
        for(int i = 0; i < 16; i++){
            temp[i] = 0f;
        }
        final float n = norm2(q);
        if(n < Constants.CS175_EPS2){
            return temp;
        }
        Matrix.setIdentityM(temp,0);
        final float two_over_n = 2/n;
        temp[0] -= (q.get(2) * q.get(2) + q.get(3) * q.get(3)) * two_over_n;
        temp[1] += (q.get(1) * q.get(2) + q.get(0) * q.get(3)) * two_over_n;
        temp[2] += (q.get(1) * q.get(3) + q.get(2) * q.get(0)) * two_over_n;

        temp[4] += (q.get(1) * q.get(2) + q.get(0) * q.get(3)) * two_over_n;
        temp[5] -= (q.get(1) * q.get(1) + q.get(3) * q.get(3)) * two_over_n;
        temp[6] += (q.get(2) * q.get(3) + q.get(1) * q.get(0)) * two_over_n;

        temp[8] += (q.get(1) * q.get(3) + q.get(2) * q.get(0)) * two_over_n;
        temp[9] += (q.get(2) * q.get(3) + q.get(1) * q.get(0)) * two_over_n;
        temp[10] -= (q.get(1) * q.get(1) + q.get(2) * q.get(2)) * two_over_n;
        //assert (isAffine(temp));
        return temp;
    }

}
