package org.onemoreturn.rome;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;

class MyGLSurfaceView extends GLSurfaceView {
    private static final String ZOOM = "Zoom";
    private static final String DRAG = "Drag";

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    // private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private final float TOUCH_SCALE_FACTOR = 0.02f;
    private float mPreviousX = 0;
    private float mPreviousY = 0;
    private float mPreviousZ = 1;
	private String mode = DRAG;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, we are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();
        float z = -1;

        switch (e.getAction()) {
        	case MotionEvent.ACTION_POINTER_DOWN:
        		Log.e("Development","Mode=Zoom");
        		mode = ZOOM;
        		break;

            case MotionEvent.ACTION_MOVE:

            	if (mode == DRAG) {
            		if (e.getPointerCount() >= 2) {
                    	float sx = x - e.getX(1);
                    	float sy = y - e.getY(1);
                    	z = FloatMath.sqrt(sx*sx + sy*sy);
                    	if (mPreviousZ == -1) { mPreviousZ = z; }

                    	// Log.d(TAG, "newDist=" + newDist);
                    	// if (newDist > 10f) {
                    	// matrix.set(savedMatrix);
                    	float dz = z - mPreviousZ;
                    	// Log.e("Development","dz: "+dz+" = "+z+"-"+mPreviousZ);
                    	
                    	// Log.e("Development","Scale: "+scale);
                    	// float tmp = mRenderer.getViewZ();
                    	mRenderer.setViewZ(mRenderer.getViewZ()+(dz * TOUCH_SCALE_FACTOR));
                    	// Log.d("Development","Z: "+mRenderer.getViewZ()+" = "+tmp+"-("+dz+"*"+TOUCH_SCALE_FACTOR+")");

            		} else {
                		float dx = x - mPreviousX;
                    	float dy = y - mPreviousY;
                    	mPreviousZ = -1;

                    	float zScale = mRenderer.getViewZ();
                    	zScale = (zScale < 0) ? -zScale : zScale;
                    	zScale *= 0.05;
                    	zScale += 0.8f;
                    	mRenderer.setViewX(mRenderer.getViewX()+(dx * TOUCH_SCALE_FACTOR * zScale));
                    	mRenderer.setViewY(mRenderer.getViewY()+(dy * TOUCH_SCALE_FACTOR * zScale));
            		}

            	} else if (mode == ZOOM) {
            	}
            	
            	break;
        }

        mPreviousX = x;
        mPreviousY = y;
        mPreviousZ = z;
    	requestRender();
        return true;
    }    
}
