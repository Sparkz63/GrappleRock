package util;

import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.opengl.GL11.*;
import static game.Params.*;

// Might have to change everything to be based off of projectionWidth and projectionHeight later. IDK.
public class Camera {
	private static float x, y;					// Location of the camera in world coordinates
	
	private static int xMin = -1;
	private static int xMax = -1;
	
	private static int yMin = -1;
	private static int yMax = -1;	// Limiting camera coordinates, using edges of screen. -1 = no limits.
	
	private Camera(){
		
	}
	
	public static void init(float x, float y) {
		Camera.x = x;
		Camera.y = y;
	}
	
	public static void adjustViewMatrix() {
		glLoadIdentity();
		gluLookAt(	x , y , 1,
					x , y , 0,
					0, 1, 0);
		
		//System.out.println(x + ", " + y);
	}
	
	public static void setPosition(float xPosition, float yPosition) {
		x = xPosition;
		y = yPosition;
		
		/*if (xMin != -1 && x < xMin)
			x = xMin;
		
		if (yMin != -1 && y < yMin) 
			y = yMin;
		
		if (xMax != -1 && x + screenWidth > xMax)
			x = xMax - screenWidth;
		
		if (yMax != -1 && y + screenHeight > yMax)
			y = yMax - screenHeight;*/
		
		//System.out.println(x + ", " + y);
	}
	
	public static float getX() {
		return x;
	}
	
	public static float getY() {
		return y;
	}
	
	public static void setRestrictingCoordinates(int xMin, int yMin, int xMax, int yMax) throws Exception {
		// Set smallest and largest coordinates the camera can see.
		
		System.out.println("(" + xMin + ", " + yMin + ") (" + xMax + ", " + yMax + ")");
		System.out.println("Projection: " + screenWidth + ", " + screenHeight);
		
		// Check for invalid arguments
		if (yMax - yMin < screenHeight) {
			throw new IllegalArgumentException("Invalid Camera Height Restriction.");
		}
		
		if (xMax - xMin < screenWidth) {
			throw new IllegalArgumentException("Invalid Camera Width Restriction.");
		}
		
		Camera.xMin = xMin;
		Camera.yMin = yMin;
		
		Camera.xMax = xMax;
		Camera.yMax = yMax;
		

	}
	
	public static float getWorldXCoordinate(float screenX) {
		return x + screenX;
	}	
	
	public static float getWorldYCoordinate(float screenY) {
		return y + screenY;
	}
}
