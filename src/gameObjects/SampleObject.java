package gameObjects;

import org.lwjgl.input.*;

import static org.lwjgl.opengl.GL11.*;

public class SampleObject implements IGameObject {
	int x, y;
	
	@Override
	public void initialize() {
		update(0);
	}

	@Override
	public void update(long deltaTime) {
		x = Mouse.getX();
		y = Mouse.getY();
	}

	@Override
	public void render() {
		
		glRectf(x-10, y-10, x+10, y+10);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}
