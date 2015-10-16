package org.onemoreturn.rome;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class OpenGL2DSurfaceViewOld extends GLSurfaceView {

	private final OpenGL2DSurfaceViewOld mRenderer;
	
	public OpenGL2DSurfaceViewOld(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new OpenGL2DSurfaceViewOld(context);
//        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mRenderer.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mRenderer.onResume();
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent e) {
//		mRenderer.processTouchEvent(e);
		return true;
	}

}
