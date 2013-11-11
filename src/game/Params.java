package game;

// Holder for general purpose settings of our game
public class Params {
	
	public static final boolean editorMode = true;	//True if running level editor, false if playing game
	
	public static final int screenWidth = 1280;
	public static final int screenHeight = 1024;
	
	public static final int projectionWidth = 1280;
	public static final int projectionHeight = 1024;
	
	public static final boolean fullScreen = false; 	// Not Yet Implemented.
	
	public static final int targetFPS = 60;
	
	public static final float metersToPixels = 30.0f;
	public static final float pixelsToMeters = 1.0f / metersToPixels;		// conversion ratio
	
	public static final float [] obstacleOutlineColor3f = new float [] {0f, .7f, 0f};
	public static final float [] obstacleFillColor3f = new float [] {.1f, .1f, 1f};
}
