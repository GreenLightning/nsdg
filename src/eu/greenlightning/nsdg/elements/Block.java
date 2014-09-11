package eu.greenlightning.nsdg.elements;

import java.awt.Color;
import java.awt.Graphics2D;

public class Block implements Element {

	private final Text text;

	public Block() {
		this("%");
	}

	public Block(String text) {
		this(new Text(text));
	}

	public Block(Text text) {
		this.text = text;
	}

	@Override
	public int getWidth(Graphics2D g) {
		return text.getWidth(g) + 20;
	}

	@Override
	public int getHeight(Graphics2D g) {
		return text.getHeight(g) + 10;
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);
		int x = (width - text.getWidth(g)) / 2;
		int y = (height - text.getHeight(g)) / 2;
		text.paint(g, x, y);
	}

}
