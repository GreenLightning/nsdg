package eu.greenlightning.nsdg.elements;

import java.awt.Color;
import java.awt.Graphics2D;

public class Break extends Block {

	public Break(String text) {
		super(text);
	}

	public Break(Text text) {
		super(text);
	}

	@Override
	public int getWidth(Graphics2D g) {
		return 10 + super.getWidth(g);
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width, height);
		g.drawLine(10, 0, 0, height / 2);
		g.drawLine(0, height / 2, 10, height - 1);
		int x = 10 + (width - 10 - text.getWidth(g)) / 2;
		int y = (height - text.getHeight(g)) / 2;
		text.paint(g, x, y);
	}

}
