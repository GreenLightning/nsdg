package eu.greenlightning.nsdg.elements;

import java.awt.Color;
import java.awt.Graphics2D;

public class Procedure extends Block {

	public Procedure(String text) {
		super(text);
	}

	public Procedure(Text text) {
		super(text);
	}

	@Override
	public int getWidth(Graphics2D g) {
		return 10 + super.getWidth(g) + 10;
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width, height);
		g.drawLine(10, 0, 10, height);
		g.drawLine(width - 10, 0, width - 10, height);
		int x = (width - text.getWidth(g)) / 2;
		int y = (height - text.getHeight(g)) / 2;
		text.paint(g, x, y);
	}

}
