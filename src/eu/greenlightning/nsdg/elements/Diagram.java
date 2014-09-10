package eu.greenlightning.nsdg.elements;

import java.awt.Graphics2D;

public class Diagram implements Element {

	private final Element child;

	public Diagram(Element child) {
		this.child = child;
	}

	@Override
	public int getWidth(Graphics2D g) {
		return child.getWidth(g) + 20;
	}

	@Override
	public int getHeight(Graphics2D g) {
		return child.getHeight(g) + 20;
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		g.translate(10, 10);
		child.paint(g, width - 20, height - 20);
		g.translate(-10, -10);
	}

}
