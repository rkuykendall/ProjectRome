package org.onemoreturn.rome;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.onemoreturn.rome.graphics.Sprite;
import org.onemoreturn.rome.R;
import org.onemoreturn.rome.logic.Map;

public class MainActivity extends Activity {

	protected FrameLayout mSurfaceContainer;
	protected OpenGL2DSurfaceView mGLView;
	protected ImageView mControlA, mControlB, mControlLeft, mControlRight;
	public static float speedX = 0.0f;
	public Map mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		makeFullscreen();

		setContentView(R.layout.activity_main);

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.gamelayout);

		this.mMap = new Map(7, 17);
		mGLView = new OpenGL2DSurfaceView(this);
        mGLView.setMap(this.mMap);
        layout.addView(mGLView);
	}

	protected void makeFullscreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			makeFullscreenJellybean();
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	protected void makeFullscreenJellybean() {
		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
		getWindow().getDecorView().setSystemUiVisibility(uiOptions);
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	protected void makeFullescreenKitkat() {
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
			makeFullescreenKitkat();
		}
	}
}
