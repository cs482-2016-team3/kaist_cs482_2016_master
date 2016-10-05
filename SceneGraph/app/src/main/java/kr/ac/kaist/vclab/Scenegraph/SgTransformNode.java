package kr.ac.kaist.vclab.Scenegraph;

import java.util.Vector;

public class SgTransformNode extends SgNode {
    private Vector<SgNode> children = new Vector<>();

    @Override
    public boolean accept(SgNodeVisitor visitor){
        if (!visitor.visit(this))
            return false;
        for (int i = 0, n = children.size(); i < n; ++i) {
            if (!children.get(i).accept(visitor))
                return false;
        }
        return visitor.postVisit(this);
    }

    public RigTForm getRbt(){
        return null;
    }

    public void addChild(SgNode child){
        children.add(child);
    }

    public void removeChild(SgNode child){
        children.remove(child);
    }

    public int getNumChild(){
        return children.size();
    }

    public SgNode getChild(int i){
        return children.get(i);
    }



    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        SgTransformNode node = (SgTransformNode)obj;
        if(children.equals(node.children))
            return true;

        return  false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        int hash = children.hashCode();
        hash = hash < 0? 0 : hash;
        result = prime * result  + hash;
        return result;
    }

}
