package gameObjects;

import static game.Params.pixelsToMeters;
import game.Game;

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import util.Renderer;

class Link{
	
	public Body body, attachment;
	private Link parent;
	
	public Link(){
		parent = null;
	}
	
	public Link(float x, float y){
		parent = null;
		
		float width = 10;
		float height = 40;
		
		BodyDef boxDef = new BodyDef();
		
		boxDef.type = BodyType.STATIC;
		boxDef.position.set(x * pixelsToMeters, y * pixelsToMeters);
		
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
	
	public Link(Link p, Link head){
		float width = 40 * pixelsToMeters;
		float height = 10 * pixelsToMeters;
		
		parent = p;
		
		float x = parent.body.getPosition().x;
		float y = parent.body.getPosition().y;
		
		BodyDef boxDef = new BodyDef();
		
		boxDef.type = BodyType.DYNAMIC;
		//boxDef.position.set(x, y - height + width);
		boxDef.position.set(x + width - height, y);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(width / 2.0f, height / 2.0f);
		
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = 0.1f;
		boxFixture.shape = boxShape;
		boxFixture.friction = 0.2f;
		
		body = Game.world.createBody(boxDef);
		body.createFixture(boxFixture);

		RevoluteJointDef jointDef = new RevoluteJointDef();
		//Vec2 anchor = new Vec2(x, y - height / 2.0f + width / 2.0f);
		Vec2 anchor = new Vec2(x + width / 2.0f - height / 2.0f, y);
		jointDef.collideConnected = false;
		jointDef.initialize(body, parent.body, anchor);
		

		Game.world.createJoint(jointDef);
		
		BodyDef bodyDef = new BodyDef();
        bodyDef.position = anchor;
        attachment = Game.world.createBody(bodyDef);
        
        MassData massData = new MassData();
        massData.mass = body.getMass();
        massData.I = body.getInertia();
        attachment.setMassData(massData);
        
        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(attachment, head.body, attachment.getPosition());
        Game.world.createJoint(revoluteJointDef);
		
        PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
        prismaticJointDef.initialize(attachment, body, anchor, new Vec2(0, 1));
        prismaticJointDef.enableLimit = true;
        prismaticJointDef.lowerTranslation = 0;
        prismaticJointDef.upperTranslation = (body.getPosition().y - anchor.y);
        Game.world.createJoint(prismaticJointDef);
		
		jointDef.initialize(body, head.body, anchor);
		
		//Game.world.createJoint(jointDef);
	}
	
	public void makeDeadWeight(float weight){
		body.getFixtureList().setDensity(weight);
	}
	
	public void update(){
		if(parent != null)
			parent.update();
		body.applyForceToCenter(new Vec2(0, 3));
	}
	
	public void render(){
		if(parent != null)
			parent.render();
		Renderer.renderFrame(body);
		Renderer.renderFrame(attachment);
	}
}

public class Rope implements IGameObject {
	
	Link head, tail;
	
	public Rope(){
		initialize();
		//initTest(false);
	}

	 /*public void initTest(boolean deserialized) {
		    if (deserialized) {
		      return;
		    }

		    Body ground = null;
		    {
		      BodyDef bd = new BodyDef();
		      bd.position.set(800, 100);
		      ground = Game.world.createBody(bd);

		      PolygonShape shape = new PolygonShape();
		      shape.setAsBox(20.0f, 0.0f);//new Vec2(-40.0f, 0.0f), new Vec2(40.0f, 0.0f));
		      ground.createFixture(shape, 0.0f);
		    }

		    {
		      PolygonShape shape = new PolygonShape();
		      //shape.setAsBox(0.6f, 0.125f);
		      shape.setAsBox(0.125f,1.2f);

		      FixtureDef fd = new FixtureDef();
		      fd.shape = shape;
		      fd.density = 20.0f;
		      fd.friction = 0.2f;

		      RevoluteJointDef jd = new RevoluteJointDef();
		      jd.collideConnected = false;

		      final float y = 25.0f;
		      Body prevBody = ground;
		      for (int i = 20; i < 35; ++i) {
		        BodyDef bd = new BodyDef();
		        bd.type = BodyType.DYNAMIC;
		        bd.position.set(y, 60f - 2 * i);
		        Body body = Game.world.createBody(bd);
		        body.createFixture(fd);

		        Vec2 anchor = new Vec2(y, 2 * i);
		        jd.initialize(prevBody, body, anchor);
		        Game.world.createJoint(jd);

		        prevBody = body;
		      }
		    }
		  }*/
	
	public void initialize(){
		head = tail = new Link(200, 700);
		
		for(int a = 0; a < 20; a++)
			addLink();
		
		tail.makeDeadWeight(1);
		
		Vec2 temp = new Vec2(tail.body.getPosition().x + 5, tail.body.getPosition().y + 5);
		tail.body.setTransform(temp, 0.0f);
	}
	
	public void addLink(){
		tail = new Link(tail, head);
	}
	
	public void update(long deltaTime){
		if(tail != null)
			tail.update();
	}
	
	public void render(){
		if(tail != null)
			tail.render();
	}
	
	public void destroy(){
		
	}
	
}
