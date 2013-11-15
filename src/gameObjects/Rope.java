package gameObjects;

import static game.Params.pixelsToMeters;
import game.Game;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.RopeJointDef;

import util.Renderer;

class Link{
	
	public Body body;
	private Link parent;
	
	public Link(float x, float y){
		parent = null;
		
		float width = 20;
		float height = 40;
		
		BodyDef boxDef = new BodyDef();
		
		boxDef.type = BodyType.STATIC;
		boxDef.position.set((x + width / 2.0f) * pixelsToMeters, (y + height / 2.0f) * pixelsToMeters);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(width * pixelsToMeters / 2.0f, height * pixelsToMeters / 2.0f);
		
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = 1f;
		boxFixture.shape = boxShape;
		boxFixture.friction = 0.0f;
		boxFixture.restitution = 0.0f;
		
		body = Game.world.createBody(boxDef);
		body.createFixture(boxFixture);
	}
	
	public Link(Link p){
		float width = 4 * pixelsToMeters;
		float height = 20 * pixelsToMeters;
		
		parent = p;
		
		float x = parent.body.getPosition().x;
		float y = parent.body.getPosition().y;
		
		
		BodyDef boxDef = new BodyDef();
		
		boxDef.type = BodyType.DYNAMIC;
		boxDef.position.set(x, y - height / 2.0f);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(width / 2.0f, height / 2.0f);
		
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = 1;
		boxFixture.shape = boxShape;
		boxFixture.friction = 0.0f;
		boxFixture.restitution = 0.0f;
		
		
		body = Game.world.createBody(boxDef);
		body.createFixture(boxFixture);

		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.bodyA = body;
		jointDef.bodyB = parent.body;
		jointDef.localAnchorA.set(0, height / 2.0f - width / 2.0f);//height / 2.0f);
		jointDef.localAnchorB.set(0, - height / 2.0f + width / 2.0f);//- height / 2.0f);
		//jointDef.length = 1 * pixelsToMeters;
		//jointDef.dampingRatio = 0f;
		//jointDef.frequencyHz = 0;
		
		//jointDef.maxLength = 1 * pixelsToMeters;
		jointDef.collideConnected = false;
		Game.world.createJoint(jointDef);
	}
	
	public void render(){
		if(parent != null)
			parent.render();
		Renderer.renderFrame(body);
	}
	
}

public class Rope implements IGameObject {
	
	Link head, tail;
	
	public Rope(){
		initialize();
	}

	public void initialize(){
		head = tail = new Link(100, 600);
		
		for(int a = 0; a < 20; a++)
			addLink();
		Vec2 temp = new Vec2(tail.body.getPosition().x + 25, tail.body.getPosition().y + 25);
		tail.body.setTransform(temp, 0.0f);
	}
	
	public void addLink(){
		tail = new Link(tail);
	}
	
	public void update(long deltaTime){
		
	}
	
	public void render(){
		tail.render();
	}
	
	public void destroy(){
		
	}
	
}
