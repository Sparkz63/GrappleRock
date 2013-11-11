package util;

import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import static org.lwjgl.opengl.GL11.*;
import static game.Params.*;

public class Renderer {
	
	public static void renderSolid(Body body){
		glPushMatrix();
		
		Fixture f = body.getFixtureList();
		
		PolygonShape polygonShape = (PolygonShape) f.m_shape;
		Vec2 vertices [] = polygonShape.m_vertices;
		int numVertices = polygonShape.m_count;
		
		Vec2 bodyPosition = body.getPosition().mul(metersToPixels);
		
		glTranslatef(bodyPosition.x, bodyPosition.y, 0);
		glRotated(Math.toDegrees(body.getAngle()), 0, 0, 1);
		glColor3f(obstacleOutlineColor3f[0], obstacleOutlineColor3f[2],obstacleOutlineColor3f[2]);
		glBegin(GL_TRIANGLE_FAN);
		
		for (int i = 0; i < numVertices; i++){
			Vec2 vertex = vertices[i];
			//Vec2 vertex2 = vertices[(i+1) % numVertices];
			
			vertex = vertex.mul(metersToPixels);
			//vertex2 = vertex2.mul(metersToPixels);
			
			glVertex2f(vertex.x, vertex.y);
			//glVertex2f(vertex2.x, vertex2.y);
		}
		
		glEnd();
		
		glPopMatrix();
	}
	
	public static void renderFrame(Body body) {
		glPushMatrix();
		
		for (Fixture f = body.getFixtureList(); null != f ; f = f.getNext()) {
			ShapeType shapeType = f.getType();
			
			if (shapeType == ShapeType.POLYGON) {
				PolygonShape polygonShape = (PolygonShape) f.m_shape;
				Vec2 vertices [] = polygonShape.m_vertices;
				int numVertices = polygonShape.m_count;
				
				Vec2 bodyPosition = body.getPosition().mul(metersToPixels);
				
				glTranslatef(bodyPosition.x, bodyPosition.y, 0);
				glRotated(Math.toDegrees(body.getAngle()), 0, 0, 1);
				glColor3f(obstacleOutlineColor3f[0], obstacleOutlineColor3f[2],obstacleOutlineColor3f[2]);
				glBegin(GL_LINES);
				
				for (int i = 0; i < numVertices; i ++){
					Vec2 vertex = vertices[i];
					Vec2 vertex2 = vertices[(i+1) % numVertices];
					
					vertex = vertex.mul(metersToPixels);
					vertex2 = vertex2.mul(metersToPixels);
					
					glVertex2f(vertex.x, vertex.y);
					glVertex2f(vertex2.x, vertex2.y);
				}
				
				glEnd();
				
			}
		}
		
		glPopMatrix();
	}
}