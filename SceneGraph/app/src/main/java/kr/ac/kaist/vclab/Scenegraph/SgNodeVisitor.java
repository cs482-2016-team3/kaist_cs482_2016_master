package kr.ac.kaist.vclab.Scenegraph;

public class SgNodeVisitor {
    public boolean visit(SgTransformNode node){
        return true;
    }
    public boolean visit(SgShapeNode node){
        return true;
    }
    public boolean postVisit(SgTransformNode node){
        return true;
    }
    public boolean postVisit(SgShapeNode node){
        return true;
    }


}
