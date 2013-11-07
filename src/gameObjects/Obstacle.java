package gameObjects;

import game.Game;
import game.GameSettings;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import util.Location;
import util.Rectangle;
import util.Renderer;
import util.Texture;
import static org.lwjgl.opengl.GL11.*;

public class Obstacle implements IGameObject {
	float		rotation = 0;
	Rectangle 	shape;
	Body		box;
	
	int			breakAwayTimer = -1;
	Path		followPath;
	boolean		deadly;
	boolean 	willKnockTheGuyOffTheRope;
	
	Texture		texture;
	
	// Constructors...

	public Obstacle (int x, int y, int width, int height) {
		shape = new Rectangle();
		
		shape.position.x = x;
		shape.position.y = y;
		shape.width = width;
		shape.height = height;
	}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		BodyDef boxDef = new BodyDef();
		
		boxDef.type = BodyType.STATIC;
		boxDef.position.set(shape.position.x/GameSettings.pixelsToMeters, shape.position.y/GameSettings.pixelsToMeters);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox((float) shape.width / 2 / GameSettings.pixelsToMeters, shape.height / 2 / GameSettings.pixelsToMeters);
		
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = 1f;
		boxFixture.shape = boxShape;
		
		box = Game.world.createBody(boxDef);
		box.createFixture(boxFixture);
	}

	@Override
	public void update(long deltaTime) {}

	@Override
	public void render() {
		Renderer.renderBody(shape);
		//glRectf(shape.position.x, shape.position.y, shape.position.x + shape.width, shape.position.y + shape.height);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
