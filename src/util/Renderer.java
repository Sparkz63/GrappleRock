package util;

import game.GameSettings;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
	public static void renderBody (Body body) {
		glPushMatrix();
		Vec2 bodyPosition = body.getPosition().mul(GameSettings.pixelsToMeters);
		glTranslatef(bodyPosition.x, bodyPosition.y, 0);
		glRotated(Math.toDegrees(body.getAngle()), 0, 0, 1);
		glRectf(-.75f * GameSettings.pixelsToMeters,
				-.75f * GameSettings.pixelsToMeters,
				.75f * GameSettings.pixelsToMeters,
				.75f * GameSettings.pixelsToMeters);
		glPopMatrix();
	}
	
	public static void renderBody (Rectangle rectangle) {
		glPushMatrix();
		glTranslatef(rectangle.position.x, rectangle.position.y, 0);
		//glRotated(Math.toDegrees(body.getAngle()), 0, 0, 1);
		glRecti(0,
				0,
				rectangle.width,
				rectangle.height);
		glPopMatrix();
	}
}
