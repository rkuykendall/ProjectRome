package org.onemoreturn.rome;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 1.0/1.1.
 */
public class Tile {
    private final FloatBuffer vertexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

    private float vertices[] = {     // 0.0f,   0.0f, 0.0f,    //center
    		0.0f,   1.0f, 0.0f,    // top
           -1.0f,   0.5f, 0.0f,    // left top
           -1.0f,  -0.5f, 0.0f,    // left bottom
            0.0f,  -1.0f, 0.0f,    // bottom
            1.0f,  -0.5f, 0.0f,    // right bottom
            1.0f,  0.5f, 0.0f,     // right top
    		0.0f,   1.0f, 0.0f,    // top
    };

    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 0.0f };
    // float color[] = { 0.552f, 0.768f, 0.22265625f, 0.207f };

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Tile() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
        // (number of coordinate values * 4 bytes per float)
        vertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(vertices);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param gl - The OpenGL ES context in which to draw this shape.
     */
    public void draw(GL10 gl) {
        // Since this shape uses vertex arrays, enable them
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        // draw the shape
        gl.glColor4f( // set color:
                color[0], color[1],
                color[2], color[3]);
        gl.glVertexPointer( // point to vertex data:
                COORDS_PER_VERTEX,
                GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glDrawArrays(    // draw shape:
                GL10.GL_TRIANGLE_FAN, 0,
                vertices.length / COORDS_PER_VERTEX);

        // Disable vertex array drawing to avoid
        // conflicts with shapes that don't use it
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
