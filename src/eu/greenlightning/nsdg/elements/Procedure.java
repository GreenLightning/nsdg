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
		g.drawRect(0, 0, width - 1, height - 1);
		g.translate(10, 0);
		super.paint(g, width - 20, height);
		g.translate(-10, 0);
	}

}
