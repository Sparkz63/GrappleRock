package gameObjects;

import org.jbox2d.common.Vec2;
import org.lwjgl.input.Keyboard;

import util.Camera;
import gameObjects.IGameObject;



public class CameraController{
	private static final float movementSpeed = 1.5f;
	//private Camera Camera;
	
	private CameraController(){
		
	}

	public static void initialize() {
	}

	public static void followPlayer(Vec2 playerPos, long deltaTime){
		Camera.setPosition(playerPos.x, playerPos.y);
	}
	
	public static void followKeys(long deltaTime){
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			Camera.setPosition(Camera.getX(), (int) (Camera.getY() + movementSpeed * deltaTime));
		else if (Keyboard.isKeyDown(Keyboard.KEY_S)) 
			Camera.setPosition(Camera.getX(), (int) (Camera.getY() - movementSpeed * deltaTime));
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) 
			Camera.setPosition((int) (Camera.getX() + movementSpeed * deltaTime), Camera.getY());
		else if (Keyboard.isKeyDown(Keyboard.KEY_A)) 
			Camera.setPosition((int) (Camera.getX() - movementSpeed * deltaTime), Camera.getY());
	}

}
