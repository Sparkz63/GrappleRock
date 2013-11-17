package gameObjects;

import org.lwjgl.input.Keyboard;
import util.Camera;
import gameObjects.IGameObject;



public class CameraController implements IGameObject {
	private static final float movementSpeed = 1.5f;
	private Camera camera;
	
	public CameraController(Camera camera) {
		this.camera = camera;
	}
	@Override
	public void initialize() {
	}

	@Override
	public void update(long deltaTime) {
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			camera.setPosition(camera.getX(), (int) (camera.getY() + movementSpeed * deltaTime));
		else if (Keyboard.isKeyDown(Keyboard.KEY_S)) 
			camera.setPosition(camera.getX(), (int) (camera.getY() - movementSpeed * deltaTime));
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) 
			camera.setPosition((int) (camera.getX() + movementSpeed * deltaTime), camera.getY());
		else if (Keyboard.isKeyDown(Keyboard.KEY_A)) 
			camera.setPosition((int) (camera.getX() - movementSpeed * deltaTime), camera.getY());
	}

	@Override
	public void render() {
	}

	@Override
	public void destroy() {
	}

}
