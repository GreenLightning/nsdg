package eu.greenlightning.nsdg.elements;

import java.awt.Color;
import java.awt.Graphics2D;

public class Block implements Element {

	private final String text;

	public Block(String text) {
		this.text = text;
	}

	@Override
	public int getWidth(Graphics2D g) {
		return g.getFontMetrics().stringWidth(text) + 20;
	}

	@Override
	public int getHeight(Graphics2D g) {
		return g.getFontMetrics().getHeight() + 10;
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);
		int x = (width - g.getFontMetrics().stringWidth(text)) / 2;
		int y = (height + g.getFontMetrics().getAscent() * 3 / 4) / 2;
		g.drawString(text, x, y);
	}

}
