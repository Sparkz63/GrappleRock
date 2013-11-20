package game;
import java.awt.Toolkit;

// Holder for general purpose settings of our game
public class Params {
	
	public static boolean editorMode = true;	//True if running level editor, false if playing game
	public static final	int	editorSnapValue = 32;
	
	public static float widthHeightRatio = 1.25f;
	public static float screenHeightFactor = 0.75f; //Window height will be 0.75 of hardware screen height
	
	public static final int screenHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * screenHeightFactor);
	public static final int screenWidth = (int) (screenHeight * widthHeightRatio);
	
	public static final int projectionHeight = 1024;
	public static final int projectionWidth = (int) (projectionHeight * widthHeightRatio);
	
	public static final float windowToProjCoords = (float)projectionHeight / screenHeight;
	
	public static final boolean fullScreen = false; 	// Not Yet Implemented.
	
	public static final int targetFPS = 30;
	
	public static final float metersToPixels = 30.0f;
	public static final float pixelsToMeters = 1.0f / metersToPixels;		// conversion ratio
	
	public static final float [] obstacleOutlineColor4f = new float [] {1.0f, 0f, 0f, .7f};		
	public static final int obstacleOutlineSize = 1;
	public static final float [] obstacleFillColor4f = new float [] {.1f, .1f, 1f, .5f};

		
	public static final int editorGridPointSize = 2;
	public static final int editorGridLineWidth = 2;
	public static final float [] editorObstacleCreationColor4f = new float [] {0f, .7f, 0f, .5f};
	public static final float [] editorSnapGridColor4f = new float [] {0f, 0f, 0f, .5f};
}
