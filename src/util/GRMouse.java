package util;


import org.jbox2d.common.Vec2;
import org.lwjgl.input.Mouse;

import static game.Params.*;

public class GRMouse {
	
	//private static Camera camera;

	public static void setCamera(Camera cam){
		//camera = cam;
	}
	
	public static Vec2 coords(){
		return new Vec2(x(), y());
	}
	
	public static float x(){
		float x = Mouse.getX() * windowToProjCoords - projectionWidth / 2.0f;
		
		//if(camera != null)
		//	x = camera.getWorldXCoordinate(x);
		x = Camera.getWorldXCoordinate(x);
		
		return x;
	}
	
	public static float y(){
		float y = Mouse.getY() * windowToProjCoords - projectionHeight / 2.0f;
		
		//if(camera != null)
			y = Camera.getWorldYCoordinate(y);
		
		return y;
	}
}
