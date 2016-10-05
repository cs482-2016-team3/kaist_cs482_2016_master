package kr.ac.kaist.vclab.Scenegraph;

public class SgShapeNode extends SgNode{
    @Override
    public boolean accept(SgNodeVisitor visitor){
        if (!visitor.visit(this))
            return false;
        return visitor.postVisit(this);
    }

    public float[] getAffineM(){
        return null;
    }

    public void draw(ShaderState curSS){

    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return  false;
    }

    @Override
    public int hashCode() {
        final int prime = 36;
        int result = 1;
        result = prime * result;
        return result;
    }
}
