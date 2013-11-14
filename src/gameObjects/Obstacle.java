package gameObjects;

import game.Game;
import static game.Params.*;
import static game.Global.*;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import util.Renderer;
//import util.Texture;

public class Obstacle implements IGameObject {
	float		rotation = 0;
	Body		box;
	
	int			breakAwayTimer = -1;
	//Path		followPath;
	boolean		deadly;
	boolean 	willKnockTheGuyOffTheRope;
	
	//Texture		texture;
	
	// Constructors...

	public Obstacle (float x, float y, int width, int height) {
		BodyDef boxDef = new BodyDef();
		
		boxDef.type = BodyType.STATIC;
		boxDef.position.set(x * pixelsToMeters + width / 2.0f, y * pixelsToMeters + height / 2.0f);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(width * pixelsToMeters / 2.0f, height * pixelsToMeters / 2.0f);
		
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = 1f;
		boxFixture.shape = boxShape;
		boxFixture.friction = .0f;
		
		box = Game.world.createBody(boxDef);
		box.createFixture(boxFixture);
	}
	
	public Obstacle (float x, float y, Vec2 vertices []){		
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(x * pixelsToMeters, y * pixelsToMeters);
		
		PolygonShape bodyShape = new PolygonShape();
		bodyShape.set(vectorPixelsToMeters(vertices), vertices.length);
		
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = 1f;
		boxFixture.shape = bodyShape;
		boxFixture.friction = .0f;
		
		box = Game.world.createBody(bodyDef);
		box.createFixture(boxFixture);
	}
	
	public void setPosition(Vec2 pos){
		pos.x *= pixelsToMeters;
		pos.y *= pixelsToMeters;
		box.setTransform(pos,  0);
	}
	
	public Vec2 getPosition(){
		Vec2 temp = new Vec2(box.getPosition());
		temp.x = temp.x * metersToPixels;
		temp.y = temp.y * metersToPixels;

		return temp;
	}
	
	public boolean testPoint(Vec2 point){
		return box.getFixtureList().testPoint(new Vec2(point.x * pixelsToMeters, point.y * pixelsToMeters));
	}
	
	//public Vec2 vertexNear(Vec2 point){
		//Vec2 localPosition = new Vec2(point.x - (box.getPosition().x * metersToPixels), point.y - (box.getPosition().y * metersToPixels));
		
		//return new Vec2();
	//}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(long deltaTime) {}

	@Override
	public void render() {
		Renderer.renderSolid(box);
		Renderer.renderFrame(box);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
