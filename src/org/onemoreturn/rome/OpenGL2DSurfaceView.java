package org.onemoreturn.rome;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import org.onemoreturn.rome.graphics.OpenGL2DRenderer;
import org.onemoreturn.rome.logic.Map;

public class OpenGL2DSurfaceView extends GLSurfaceView {
	protected OpenGL2DRenderer mRenderer;
	float speedX = 0.0f;
	public Map mMap;

    public OpenGL2DSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public OpenGL2DSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

    public void setMap(Map map) {
        this.mMap = map;
        mRenderer.setMap(mMap);
    }

    protected void init(Context context) {
		setEGLContextClientVersion(2);
        mRenderer = new OpenGL2DRenderer(context);
		setRenderer(mRenderer);
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
		mRenderer.processTouchEvent(e);
		return true;
	}
}
