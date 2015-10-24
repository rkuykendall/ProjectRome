package org.onemoreturn.rome.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import org.onemoreturn.rome.MainActivity;
import org.onemoreturn.rome.logic.Map;
import org.onemoreturn.rome.physics.SimpleRectanglePhysics;
import org.onemoreturn.rome.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGL2DRenderer implements Renderer {
	protected static final int DEFAULT_BG_COLOR = Color.argb(255, 0, 0, 0);
	protected int mClearColor;

	protected final float mProjectionMatrix[] = new float[16];
	protected final float mViewMatrix[] = new float[16];
	protected final float mRotationMatrix[] = new float[16];
	protected final float mMVPMatrix[] = new float[16];

	protected volatile float mCameraX = 0f, mCameraY = 0f, mCameraZ = 0f;

	protected Context mContext;

	public double angle = 0d;

	protected int mWidth, mHeight;
	protected float mAspectRatio;


    public Sprite[] mSprites;
	public Sprite mSprite;
    public Map mMap;

	public OpenGL2DRenderer(Context context) {
		mContext = context;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		clear(DEFAULT_BG_COLOR);

        GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		int map_x = 4;
		int map_y = 6;
		mMap = new Map(map_x, map_y, mContext);
        mSprites = new Sprite[map_x * map_y];
        mSprites = mMap.getSprites();

        for (int i = 0; i < mSprites.length; i++) {
            mSprites[i].init();
        }
    }

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		mWidth = width;
		mHeight = height;

		mAspectRatio = (float) width / height;
		Matrix.frustumM(mProjectionMatrix, 0, -mAspectRatio, mAspectRatio, -1,
                1, 3, 7);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1f, 0f);
        Matrix.translateM(mViewMatrix, 0, mCameraX, mCameraY, mCameraZ);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        for (int i = 0; i < mSprites.length; i++) {
            mSprites[i].draw(mMVPMatrix);
        }
    }

    public void onPause()
    {
		/* Do stuff to pause the renderer */
    }

    public void onResume()
    {
		/* Do stuff to resume the renderer */
    }

	public void processTouchEvent(MotionEvent event)
	{
		Log.i("RENDERER", Float.toString(event.getY()));
        Log.i("RENDERER", Float.toString(event.getX()));
	}


	protected void clearColor() {
		GLES20.glClearColor(Color.red(mClearColor), Color.green(mClearColor),
				Color.blue(mClearColor), Color.alpha(mClearColor));
	}

	public void clear(int color) {
		mClearColor = color;
		clearColor();
	}

	public void moveCamera(float x, float y, float z) {
		mCameraX += x;
		mCameraY += y;
		mCameraZ += z;
	}

	public static int loadShader(int type, String shaderCode) {
		int shader = GLES20.glCreateShader(type);

		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	/**
	 * Utility method for debugging OpenGL calls. Provide the name of the call
	 * just after making it:
	 *
	 * <pre>
	 * mColorHandle = GLES20.glGetUniformLocation(mProgram, &quot;vColor&quot;);
	 * MyGLRenderer.checkGlError(&quot;glGetUniformLocation&quot;);
	 * </pre>
	 *
	 * If the operation is not successful, the check throws an error.
	 *
	 * @param glOperation
	 *            - Name of the OpenGL call to check.
	 */
	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(OpenGL2DRenderer.class.getName(), glOperation + ": glError "
					+ error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

}
