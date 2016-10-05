package kr.ac.kaist.vclab.Scenegraph;

import java.util.Vector;

public class RbtAccumVisitor extends SgNodeVisitor {
    protected Vector<RigTForm> rbtStack;
    SgTransformNode target;
    boolean found;

    public RbtAccumVisitor(SgTransformNode target){
        this.target = target;
        found = false;
        rbtStack = new Vector<>();
    }

    final RigTForm getAccumulatedRbt(int offsetFromStackTop){
        if(!found){
            throw new RuntimeException("RbtAccumVisitor target never reached");
        }
        return rbtStack.get(offsetFromStackTop);
    }
    @Override
    public boolean visit(SgTransformNode node){
        if (rbtStack.isEmpty())
            rbtStack.add(node.getRbt());
        else
            //Problem
            //Update the Rigid Transform in the hierachy

        if(target != null) {
            if (target.equals(node)) {
                found = true;
                return false;
            }
        }
        return true;

    }

    @Override
    public boolean postVisit(SgTransformNode node){
        rbtStack.remove(rbtStack.size() - 1);
        return true;
    }

    public static RigTForm getPathAccumRbt(
            SgTransformNode source,
            SgTransformNode destination,
            int offsetFromDestination) {

        RbtAccumVisitor accum = new RbtAccumVisitor(destination);
        source.accept(accum);
        return accum.getAccumulatedRbt(offsetFromDestination);
    }

    public static RigTForm getPathAccumRbt(
            SgTransformNode source,
            SgTransformNode destination) {

        RbtAccumVisitor accum = new RbtAccumVisitor(destination);
        source.accept(accum);
        return accum.getAccumulatedRbt(0);
    }
}

