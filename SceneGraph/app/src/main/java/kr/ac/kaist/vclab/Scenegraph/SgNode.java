package kr.ac.kaist.vclab.Scenegraph;

public class SgNode extends Object{
    public boolean accept(SgNodeVisitor visitor){
        boolean result = true;
        return result;
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
        final int prime = 34;
        int result = 1;
        result = prime * result;
        return result;
    }
}
