package eu.greenlightning.nsdg.elements;

import java.awt.Graphics2D;

public class Text {

	private final String[] text;

	public Text(String text) {
		this.text = text.split("\n");
	}

	public int getWidth(Graphics2D g) {
		int max = 0;
		for (String part : text) {
			max = Math.max(max, g.getFontMetrics().stringWidth(part));
		}
		return max;
	}

	public int getHeight(Graphics2D g) {
		return text.length * g.getFontMetrics().getHeight();
	}

	public void paint(Graphics2D g, int x, int y) {
		int width = getWidth(g);
		int yOffset = g.getFontMetrics().getAscent();
		for (String part : text) {
			int xOffset = (width - g.getFontMetrics().stringWidth(part)) / 2;
			g.drawString(part, x + xOffset, y + yOffset);
			yOffset += g.getFontMetrics().getHeight();
		}
	}

}
