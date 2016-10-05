package kr.ac.kaist.vclab.helloopengl3d;

import kr.ac.kaist.vclab.robotObj.Geometry;

/**
 * Created by sjjeon on 16. 9. 21.
 */

public class Square extends Geometry{


    private static float vertices1[] = {
            -1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            1.0f, 1.0f, 0.0f
    };

    private static float normals1[] = {
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
    };

    public float color[] = { 1f, 1f, 1f };


    public Square() {
        setVertices(vertices1);
        setNormals(normals1);
        setBuffer();
    }

}