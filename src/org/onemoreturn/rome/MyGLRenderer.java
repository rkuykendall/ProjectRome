package org.onemoreturn.rome;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer {
	// private HexTile mHex;
    private Tile mTile;
    private float mAngle;
	
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background frame color
        gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        // gl.glHint(GL10.GL_POLYGON_SMOOTH_HINT, GL10.GL_NICEST);

        
       mTile = new Tile();
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        // Draw background color
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Set GL_MODELVIEW transformation mode
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();   // reset the matrix to its default state

        // When using GL_MODELVIEW, you must set the view point
        GLU.gluLookAt(gl, 
        			  0, -2, (mAngle/20)-6, 		// Eye xyz
        			  0f, 0f, 0f, 		// Center xyz
        			  0f, 100.0f, 0.0f	// Up xyz
        );
                
        int SizeY = 10;
        int SizeX = 8;
        float SpaceY = 1.6f;
        float SpaceX = 2.1f;
        
        // int [][] map = new int[SizeX][SizeY];

        gl.glTranslatef(-0.5f*SpaceX*SizeX,((SpaceY*SizeY)/-2)+3,0.0f);
        for(int i=0; i<SizeY; i++) {
            for(int j=0; j<SizeX; j++) {
                mTile.draw(gl);

                // Move left 1
                gl.glTranslatef(SpaceX,0.0f,0.0f);
            }
            // Carriage return
            gl.glTranslatef(-1*SpaceX*SizeX,0.0f,0.0f);

            // Move up 1 row
            gl.glTranslatef(0.0f,SpaceY,0.0f);

            // Offset
            if (i%2 == 0) {
                gl.glTranslatef(SpaceX/2,0.0f,0.0f);
            } else {
                gl.glTranslatef(SpaceX/-2,0.0f,0.0f);
            }
        }
        
        // Draw tile
//        mTile.draw(gl);
//        gl.glTranslatef(2.1f,0.0f,0.0f);
//        mTile.draw(gl);
//        gl.glTranslatef(-4.2f,0.0f,0.0f);
//        mTile.draw(gl);
//        gl.glTranslatef(2.1f,0.0f,0.0f);
//
//        gl.glTranslatef(1.05f,1.6f,0.0f);
//        mTile.draw(gl);
//        gl.glTranslatef(2.1f,0.0f,0.0f);
//        // mTile.draw(gl);
//        gl.glTranslatef(-4.2f,0.0f,0.0f);
//        mTile.draw(gl);
//
//        gl.glTranslatef(2.1f,-3.2f,0.0f);
//        mTile.draw(gl);
//        gl.glTranslatef(2.1f,0.0f,0.0f);
//        // mTile.draw(gl);
//        gl.glTranslatef(-4.2f,0.0f,0.0f);
//        mTile.draw(gl);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Adjust the viewport based on geometry changes
        // such as screen rotations
        gl.glViewport(0, 0, width, height);

        // make adjustments for screen ratio
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
        gl.glLoadIdentity();                        // reset the matrix to its default state
        gl.glFrustumf(
        		-ratio, // Left
        		ratio,  // Right
        		-1, 	// Bottom
        		1, 		// Top
        		1, 		// Near
        		10 		// Far
        );  // apply the projection matrix
    }

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    	String TAG = "Development";
    	Log.e(TAG,"Test: "+mAngle);

    }
}
