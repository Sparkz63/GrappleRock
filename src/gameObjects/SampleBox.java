package gameObjects;

import game.Game;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import util.Renderer;

public class SampleBox implements IGameObject {
	Body box;
	
	float x, y;
	boolean isStatic;
	
	public SampleBox (float x, float y, boolean stat) {
		this.x = x;
		this.y = y;
		this.isStatic = stat;
	}
	
	@Override
	public void initialize() {
		BodyDef boxDef = new BodyDef();
		
		if(!isStatic)
			boxDef.type = BodyType.DYNAMIC;
		else
			boxDef.type = BodyType.STATIC;
		
		boxDef.position.set(x,y);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(.75f, .75f);
		
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = 1f;
		boxFixture.shape = boxShape;
		
		box = Game.world.createBody(boxDef);
		box.createFixture(boxFixture);
	}

	@Override
	public void update(long deltaTime) {
	}

	@Override
	public void render() {
		Renderer.renderSolid(box);
	}

	@Override
	public void destroy() {
	}

}
