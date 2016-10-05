package kr.ac.kaist.vclab.Scenegraph;

public class SgRootNode extends SgTransformNode {

    public RigTForm getRbt() {
        return new RigTForm();
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
        final int prime = 35;
        int result = 1;
        result = prime * result;
        return result;
    }
}
