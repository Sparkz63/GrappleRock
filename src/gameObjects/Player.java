package gameObjects;

import static game.Params.*;
import game.Game;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.RopeJointDef;
import org.lwjgl.input.Keyboard;

import util.GRMouse;
import util.InputHandler;
import util.Renderer;

public class Player implements IGameObject {
	private Vec2 position;
	private Rope rope = new Rope();
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
		position = body.getPosition().mul(metersToPixels);
		
		//Fire rope on click
		if(InputHandler.leftMouseDown())
			fireRope();
		
		//Lean left
		if(InputHandler.keyIsDown(Keyboard.KEY_A))
			body.applyForceToCenter(new Vec2(-2.0f, 0.0f));

		//Lean right
		if(InputHandler.keyIsDown(Keyboard.KEY_D))
			body.applyForceToCenter(new Vec2(2.0f, 0.0f));
		
		//Lean back, lean back, lean back, lean back
		
		rope.update(deltaTime);
	}
	
	public void render(){
		Renderer.renderSolid(body);
		rope.render();
	}
	
	public void fireRope(){
		rope.initialize(body, GRMouse.coords());
	}
	
	public Vec2 position(){
		return position.clone();
	}
	
	public void destroy(){
		
	}
}
