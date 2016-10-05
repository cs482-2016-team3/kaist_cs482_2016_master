package kr.ac.kaist.vclab.Scenegraph;

import java.util.Vector;
import android.opengl.Matrix;
import kr.ac.kaist.vclab.util.Quaternion;

/**
 * Created by PCPC on 2016-10-05.
 */
public class Drawer extends SgNodeVisitor{

    protected Vector<RigTForm> rbtStack;
    ShaderState curSS;

    public Drawer(RigTForm initialRbt, ShaderState curSS){
        this.curSS = curSS;
        rbtStack = new Vector<>();
        rbtStack.add(initialRbt);
    }

    @Override
    public boolean visit(SgTransformNode node){
        //Problem
        //Update the Rigid Transform in the hierachy
        RigTForm tempRBT = rbtStack.lastElement();
        RigTForm nodeRBT = node.getRbt();
        rbtStack.add(tempRBT.multiply(nodeRBT));
        return true;
    }

    @Override
    public boolean postVisit(SgTransformNode node){
        rbtStack.remove(rbtStack.size() - 1);
        return true;
    }

    @Override
    public boolean visit(SgShapeNode node){

        //Problem
        //Calculate a ModelViewMatrix and NormalizeMatrix
        //and send to the shader
        //ref: ShareState Class
        float[] MVM = new float [16];

        float[] temp = new float[16];
        float[] trans = new float[16];
        float[] transVec = rbtStack.lastElement().getTranslation();
        float[] rotate;
        Quaternion quat = rbtStack.lastElement().getRotation();
        rotate = quat.quatToMat(quat);
        Matrix.setIdentityM(trans, 0);
        Matrix.translateM(trans, 0, transVec[0], transVec[1], transVec[2]);
        Matrix.multiplyMM(temp, 0, trans, 0, rotate, 0);

        Matrix.multiplyMM(MVM, 0, temp, 0, node.getAffineM(), 0);
        curSS.sendModelViewNormalMatrix(curSS, MVM);

        node.draw(curSS);
        return true;
    }

    @Override
    public boolean postVisit(SgShapeNode node) {
        return true;
    }

    ShaderState getCurSS() {
        return curSS;
    }

}
