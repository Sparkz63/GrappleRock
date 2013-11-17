package util;

import org.jbox2d.common.Vec2;
import org.lwjgl.input.Mouse;

import static game.Params.*;

public class GRMouse {
	
	private static Camera camera;

	public static void setCamera(Camera cam){
		camera = cam;
	}
	
	//public static Vec2 coords(){
	//	Vec2 coord = InputHandler.mouse();
	//	coord.x = camera.getWorldXCoordinate(coord.x * windowToProjCoords);
	//	coord.y = camera.getWorldYCoordinate(coord.y * windowToProjCoords);
	//	return coord;
	//}
	
	public static float x(){
		return camera.getWorldXCoordinate(Mouse.getX() * windowToProjCoords);
	}
	
	public static float y(){
		return camera.getWorldYCoordinate(InputHandler.mouse().y * windowToProjCoords);
	}
}
