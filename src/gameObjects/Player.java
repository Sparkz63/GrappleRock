package gameObjects;

import static game.Params.pixelsToMeters;
import game.Game;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import util.Renderer;

public class Player implements IGameObject {
	private Vec2 position;
	private Body body;
	
	public Player(Vec2 position){
		this.position = position;
		
		BodyDef boxDef = new BodyDef();
		
		boxDef.type = BodyType.DYNAMIC;
		boxDef.position.set(position.x * pixelsToMeters, position.y * pixelsToMeters);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(10 * pixelsToMeters, 30 * pixelsToMeters);
		
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = 1f;
		boxFixture.shape = boxShape;
		boxFixture.friction = .0f;
		
		body = Game.world.createBody(boxDef);
		body.createFixture(boxFixture);
	}
	
	public void initialize(){
		
	}
	
	public void update(long deltaTime){
		
	}
	
	public void render(){
		Renderer.renderSolid(body);
	}
	
	public void destroy(){
		
	}
}
