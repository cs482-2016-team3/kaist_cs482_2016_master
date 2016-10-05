package kr.ac.kaist.vclab.robotObj;

/**
 * Created by PCPC on 2016-10-05.
 */
public class JointDesc {
    int parent;
    float x, y, z;

    public JointDesc(int p, float x, float y, float z){
        parent = p;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public JointDesc(int p){
        parent = p;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
}
