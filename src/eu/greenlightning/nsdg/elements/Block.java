package eu.greenlightning.nsdg.elements;

import java.awt.Color;
import java.awt.Graphics2D;

public class Block implements Element {

	protected final Text text;

	public Block(String text) {
		this(new Text(text));
	}

	public Block(Text text) {
		this.text = text;
	}

	@Override
	public int getWidth(Graphics2D g) {
		return 10 + text.getWidth(g) + 10;
	}

	@Override
	public int getHeight(Graphics2D g) {
		return 5 + text.getHeight(g) + 5;
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width, height);
		int x = 10;
		int y = (height - text.getHeight(g)) / 2;
		text.paint(g, x, y);
	}

}
