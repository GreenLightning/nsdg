package eu.greenlightning.nsdg.elements;

import java.awt.Color;
import java.awt.Graphics2D;

public class Empty extends Block {

	public Empty() {
		super("%");
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width, height);
		int x = (width - text.getWidth(g)) / 2;
		int y = (height - text.getHeight(g)) / 2;
		text.paint(g, x, y);
	}

}
