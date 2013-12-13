package org.onemoreturn.rome;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "MyGLRenderer";
    private Tile mTile;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    
    private float viewX = 0;
    private float viewY = 0;
	private float viewZ = 0;

//	private int mDisplayWidth;
//	private int mDisplayHeight;	
//	public float[] unproject(float rx, float ry, float rz) {
//	    float[] xyzw = {0, 0, 0, 0};
//	    int[] viewport = {0, 0, mDisplayWidth, mDisplayHeight};
//	    android.opengl.GLU.gluUnProject(rx, ry, rz, mMatrixGrabber.mModelView, 0, mMatrixGrabber.mProjection, 0, viewport, 0, xyzw, 0);
//	    xyzw[0] /= xyzw[3];
//	    xyzw[1] /= xyzw[3];
//	    xyzw[2] /= xyzw[3];
//	    xyzw[3] = 1;
//	    return xyzw;
//	}
	
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
    	GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);			// Grey
        // GLES20.glClearColor(0.552f, 0.768f, 0.207f, 1.0f);	// Green
        // GLES20.glClearColor(0.376f, 0.509f, 0.659f, 1.0f);	// Blue

        
       mTile = new Tile();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        // Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.setLookAtM(mViewMatrix, 0,
		  viewX, viewY-2, viewZ-5.9f, 	// Eye xyz
		  viewX, viewY, 0f, 			// Center xyz
		  0f, 100.0f, 0.0f				// Up xyz
    	);

        
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Draw square
        // mTile.draw(mMVPMatrix);

        // Create a rotation for the triangle

        // Use the following code to generate constant rotation.
        // Leave this code out when using TouchEvents.
        // long time = SystemClock.uptimeMillis() % 4000L;
        // float angle = 0.090f * ((int) time);

        // Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        // Draw triangle
        // mTile.draw(scratch);
    	
    	
        int SizeY = 20;
        int SizeX = 20;
        float SpaceY = 1.6f;
        float SpaceX = 2.1f;
        
        // int [][] map = new int[SizeX][SizeY];

        Matrix.translateM(mMVPMatrix,0, -0.5f*SpaceX*SizeX,((SpaceY*SizeY)/-2)+3,0.0f);
        for(int i=0; i<SizeY; i++) {
            for(int j=0; j<SizeX; j++) {
                mTile.draw(mMVPMatrix);

                // Move left 1
                Matrix.translateM(mMVPMatrix,0, SpaceX,0.0f,0.0f);
            }
            // Carriage return
            Matrix.translateM(mMVPMatrix,0, -1*SpaceX*SizeX,0.0f,0.0f);

            // Move up 1 row
            Matrix.translateM(mMVPMatrix,0, 0.0f,SpaceY,0.0f);

            // Offset
            if (i%2 == 0) {
            	Matrix.translateM(mMVPMatrix,0, SpaceX/2,0.0f,0.0f);
            } else {
            	Matrix.translateM(mMVPMatrix,0, SpaceX/-2,0.0f,0.0f);
            }
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        // make adjustments for screen ratio
        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(
        		mProjectionMatrix, 0, 
        		-ratio, 		// Left
        		ratio, 			// Right
        		-1, 			// Bottom
        		1, 				// Top
        		1, 				// Near
        		30				// Far
        );	// apply the projection matrix
  }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    *
    * <pre>
    * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
    *
    * If the operation is not successful, the check throws an error.
    *
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
    
    public float getViewX() { return viewX; }
    public float getViewY() { return viewY; }
    public float getViewZ() { return viewZ; }

    public void setViewX(float f) { viewX = f; }
    public void setViewY(float f) { viewY = f; }
    public void setViewZ(float f) {
    	if (f < -20) { viewZ = -20; }
    	else if (f>1) { viewZ = 1; }
    	else { viewZ = f; }
    }
}
