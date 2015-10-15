package org.onemoreturn.rome;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

public class GLRendererOld implements Renderer {

	// Our matrices
	private final float[] mtrxProjection = new float[16];
	private final float[] mtrxView = new float[16];
	private final float[] mtrxProjectionAndView = new float[16];
	
	// Geometric variables
	public static float vertices[];
	public static short indices[];
	public static float uvs[];
	public FloatBuffer vertexBuffer;
	public ShortBuffer drawListBuffer;
	public FloatBuffer uvBuffer;
	public Sprite sprite;
	public TextManager tm;
	// Our screenresolution
	float	mScreenWidth = 1280;
	float	mScreenHeight = 768;
	float 	ssu = 1.0f;
	float 	ssx = 1.0f;
	float 	ssy = 1.0f;
	float 	swp = 320.0f;
	float 	shp = 480.0f;

    // Tiles
    int num_x = 10;
    int num_y = 20;
    int num_tiles = num_x * num_y;

    // Misc
	Context mContext;
	long mLastTime;
	int mProgram;
	
	public GLRendererOld(Context c)
	{
		mContext = c;
		mLastTime = System.currentTimeMillis() + 100;
        sprite = new Sprite();
	}
	
	public void onPause()
	{
		/* Do stuff to pause the renderer */
	}
	
	public void onResume()
	{
		/* Do stuff to resume the renderer */
		mLastTime = System.currentTimeMillis();
	}
	
	@Override
	public void onDrawFrame(GL10 unused) {
		
		// Get the current time
    	long now = System.currentTimeMillis();
    	
    	// We should make sure we are valid and sane
    	if (mLastTime > now) return;
        
    	// Get the amount of time the last frame took.
    	long elapsed = now - mLastTime;
		
		// Update our example
		UpdateSprite();
		
		// Render our example
		Render(mtrxProjectionAndView);
		
		// Render the text
		if(tm!=null)
			tm.Draw(mtrxProjectionAndView);
		
		// Save the current time to see how long it took :).
        mLastTime = now;
		
	}
	
	private void Render(float[] m) {
		// Set our shaderprogram to image shader
		GLES20.glUseProgram(riGraphicTools.sp_Image);
		
		// clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        // get handle to vertex shader's vPosition member and add vertices
	    int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "vPosition");
	    GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
	    GLES20.glEnableVertexAttribArray(mPositionHandle);
	    
	    // Get handle to texture coordinates location and load the texture uvs
	    int mTexCoordLoc = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "a_texCoord" );
	    GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);
	    GLES20.glEnableVertexAttribArray ( mTexCoordLoc );
	    
	    // Get handle to shape's transformation matrix and add our matrix
        int mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_Image, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);
        
        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (riGraphicTools.sp_Image, "s_texture" );



        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i (mSamplerLoc, 2);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);



        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
	}
	

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		// We need to know the current width and height.
		mScreenWidth = width;
		mScreenHeight = height;
		
		// Redo the Viewport, making it fullscreen.
		GLES20.glViewport(0, 0, (int)mScreenWidth, (int)mScreenHeight);
		
		// Clear our matrices
	    for(int i=0;i<16;i++)
	    {
	    	mtrxProjection[i] = 0.0f;
	    	mtrxView[i] = 0.0f;
	    	mtrxProjectionAndView[i] = 0.0f;
	    }
	    
	    // Setup our screen width and height for normal sprite translation.
	    Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);
	    
	    // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
        
        // Setup our scaling system
		SetupScaling();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		// Setup our scaling system
		SetupScaling();
		// Create the triangles
		SetupTriangle();
		// Create the image information
		SetupImage();
		// Create our texts
		SetupText();
		
		// Set the clear color to black
		GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1);	
		
		GLES20.glEnable(GLES20.GL_BLEND);
	    GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	    
	    // Create the shaders, images
	    int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_Image);
	    int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_Image);

	    riGraphicTools.sp_Image = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(riGraphicTools.sp_Image, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(riGraphicTools.sp_Image, fragmentShader); // add the fragment shader to program
	    GLES20.glLinkProgram(riGraphicTools.sp_Image);                  // creates OpenGL ES program executables
	    
	    // Text shader
	    int vshadert = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_Text);
	    int fshadert = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_Text);
	    
	    riGraphicTools.sp_Text = GLES20.glCreateProgram();
	    GLES20.glAttachShader(riGraphicTools.sp_Text, vshadert);
	    GLES20.glAttachShader(riGraphicTools.sp_Text, fshadert); 		// add the fragment shader to program
	    GLES20.glLinkProgram(riGraphicTools.sp_Text);                  // creates OpenGL ES program executables
	    
	    // Set our shader programm
		GLES20.glUseProgram(riGraphicTools.sp_Image);
	}
	
	public void SetupText()
	{
		// Create our text manager
		tm = new TextManager();
		
		// Tell our text manager to use index 1 of textures loaded
		tm.setTextureID(1);
		
		// Pass the uniform scale
		tm.setUniformscale(ssu);
		
		// Create our new textobject
		TextObject txt = new TextObject("Project Rome", 10f, 10f);
		
		// Add it to our manager
		tm.addText(txt);
		
		// Prepare the text for rendering
		tm.PrepareDraw();
	}
	
	public void SetupScaling()
	{
		// The screen resolutions
		swp = (int) (mContext.getResources().getDisplayMetrics().widthPixels);
		shp = (int) (mContext.getResources().getDisplayMetrics().heightPixels);
		
		// Orientation is assumed portrait
		ssx = swp / 320.0f;
		ssy = shp / 480.0f;
		
		// Get our uniform scaler
		if(ssx > ssy)
    		ssu = ssy;
    	else
    		ssu = ssx;
	}
	
	public void processTouchEvent(MotionEvent event)
	{
        // Get the half of screen value
		int screenhalf = (int) (mScreenWidth / 2);
		int screenheightpart = (int) (mScreenHeight / 3);
		if(event.getX()<screenhalf)
		{
			// Left screen touch
			if(event.getY() < screenheightpart)
				sprite.scale(-0.01f);
			else if(event.getY() < (screenheightpart*2))
				sprite.translate(-10f*ssu, -10f*ssu);
			else
				sprite.rotate(0.01f);
		}
		else
		{
			// Right screen touch
			if(event.getY() < screenheightpart)
				sprite.scale(0.01f);
			else if(event.getY() < (screenheightpart*2))
				sprite.translate(10f*ssu, 10f*ssu);
			else
				sprite.rotate(-0.01f);
		}
	}
	
	public void UpdateSprite()
	{
		// Get new transformed vertices
//		vertices = sprite.getTransformedVertices();

		// The vertex buffer.
//		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
//		bb.order(ByteOrder.nativeOrder());

//		Log.v("debug", "did something");
//		vertexBuffer = bb.asFloatBuffer();
//		vertexBuffer.put(vertices);
//		vertexBuffer.position(0);
	}
	
	
	public void SetupImage()
	{
        float[] uvs = {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f};

		// The texture buffer
		ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * num_tiles * 4);
		bb.order(ByteOrder.nativeOrder());
		uvBuffer = bb.asFloatBuffer();
        for(int i=0; i<num_tiles; i++) {
            uvBuffer.put(uvs);
        }
		uvBuffer.position(0);

        String[] texture_file_names = {"hex_grass_grid", "font",  "hex_ice_grid"};

        // Generate Textures, if more needed, alter these numbers.
        int[] texturenames = new int[texture_file_names.length];
        GLES20.glGenTextures(texture_file_names.length, texturenames, 0);

        for(int i = 0; i < texture_file_names.length; i++) {
            // Retrieve our image from resources.
            int id = mContext.getResources().getIdentifier("drawable/" + texture_file_names[i], null, mContext.getPackageName());

            // Temporary create a bitmap
            Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);

            // Bind texture to texturename
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[i]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

            // We are done using the bitmap so we should recycle it.
            bmp.recycle();
        }
	}
	
	public void SetupTriangle()
	{
        // Our collection of vertices
		vertices = new float[num_tiles*4*3];

        // Create the vertex data
        for(int y=0; y<num_y; y++) {
            for (int x=0; x<num_x; x++) {
                Tile t = new Tile(x, y);
                float[] tile_verts = t.getVertices();
                int tilenum = y*num_x+x;
                for (int i=0;i<tile_verts.length;i++) {
                    vertices[(4*3*tilenum)+i] = tile_verts[i];
                }
            }
        }
		
		// The indices for all textured quads
		indices = new short[num_tiles*6];
		int last = 0;
		for(int i=0;i<num_tiles;i++)
		{
			// We need to set the new indices for the new quad
			indices[(i*6) + 0] = (short) (last + 0);
			indices[(i*6) + 1] = (short) (last + 1);
			indices[(i*6) + 2] = (short) (last + 2);
			indices[(i*6) + 3] = (short) (last + 0);
			indices[(i*6) + 4] = (short) (last + 2);
			indices[(i*6) + 5] = (short) (last + 3);
			
			// Our indices are connected to the vertices so we need to keep them
			// in the correct order.
			// normal quad = 0,1,2,0,2,3 so the next one will be 4,5,6,4,6,7
			last = last + 4;
		}

		// The vertex buffer.
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		// initialize byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(indices);
		drawListBuffer.position(0);
	}

    class Tile
    {
        public int x;
        public int y;

        public Tile(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public float[] getVertices()
        {
            float[] tile_verts = new float[4*3];
            int offset_x = (int) ((30.0f*ssu*x) + ((y%2)*(15.0f*ssu)));
            int offset_y = (int) 2000 - (int) (17.0f*ssu*y);

            // Create the 2D parts of our 3D vertices, others are default 0.0f
            tile_verts[0] = offset_x;
            tile_verts[1] = offset_y + (30.0f*ssu);
            tile_verts[2] = 0f;

            tile_verts[3] = offset_x;
            tile_verts[4] = offset_y;
            tile_verts[5] = 0f;

            tile_verts[6] = offset_x + (30.0f*ssu);
            tile_verts[7] = offset_y;
            tile_verts[8] = 0f;

            tile_verts[9] = offset_x + (30.0f*ssu);
            tile_verts[10] = offset_y + (30.0f*ssu);
            tile_verts[11] = 0f;


            return tile_verts;
        }

    }

	class Sprite
	{
		float angle;
		float scale;
		RectF base;
		PointF translation;
		
		public Sprite()
		{
			// Initialise our intital size around the 0,0 point
			base = new RectF(-50f*ssu,50f*ssu, 50f*ssu, -50f*ssu);
			
			// Initial translation
			translation = new PointF(50f*ssu,50f*ssu);
			
			// We start with our inital size
			scale = 1f;
			
			// We start in our inital angle
			angle = 0f;
		}
		
		
		public void translate(float deltax, float deltay)
		{
			// Update our location.
			translation.x += deltax;
			translation.y += deltay;
		}
		
		public void scale(float deltas)
		{
			scale += deltas;
		}
		
		public void rotate(float deltaa)
		{
			angle += deltaa;
		}
		
		public float[] getTransformedVertices()
		{
			// Start with scaling
			float x1 = base.left * scale;
			float x2 = base.right * scale;
			float y1 = base.bottom * scale;
			float y2 = base.top * scale;
			
			// We now detach from our Rect because when rotating, 
			// we need the seperate points, so we do so in opengl order
			PointF one = new PointF(x1, y2);
			PointF two = new PointF(x1, y1);
			PointF three = new PointF(x2, y1);
			PointF four = new PointF(x2, y2);
			
			// We create the sin and cos function once, 
			// so we do not have calculate them each time.
			float s = (float) Math.sin(angle);
			float c = (float) Math.cos(angle);
			
			// Then we rotate each point
			one.x = x1 * c - y2 * s;
			one.y = x1 * s + y2 * c;
			two.x = x1 * c - y1 * s;
			two.y = x1 * s + y1 * c;
			three.x = x2 * c - y1 * s;
			three.y = x2 * s + y1 * c;
			four.x = x2 * c - y2 * s;
			four.y = x2 * s + y2 * c;
			
			// Finally we translate the sprite to its correct position.
			one.x += translation.x;
			one.y += translation.y;
			two.x += translation.x;
			two.y += translation.y;
			three.x += translation.x;
			three.y += translation.y;
			four.x += translation.x;
			four.y += translation.y;
			
			// We now return our float array of vertices.
			return new float[]
	        {
					one.x, one.y, 0.0f,
	       			two.x, two.y, 0.0f,
	       			three.x, three.y, 0.0f,
	       			four.x, four.y, 0.0f,
	        };
		}
	}
}
