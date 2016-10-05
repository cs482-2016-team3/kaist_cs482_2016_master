package kr.ac.kaist.vclab.Scenegraph;

public class SgRbtNode extends SgTransformNode {
    private RigTForm rbt;

    public SgRbtNode(){
        rbt = new RigTForm();
    }
    public SgRbtNode(RigTForm _rbt){
        rbt = new RigTForm(_rbt);
    }
    @Override
    public RigTForm getRbt(){
        return rbt;
    }

    public void setRbt(RigTForm _rbt){
        rbt = new RigTForm(_rbt);
    }
}
