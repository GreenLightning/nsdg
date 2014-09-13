package eu.greenlightning.nsdg.elements;

import java.awt.Graphics2D;

public class Diagram implements Element {

	private final Text title;
	private final Element child;

	public Diagram(String title, Element child) {
		this(title == null ? null : new Text(title), child);
	}

	public Diagram(Text title, Element child) {
		this.title = title;
		this.child = child;
	}

	private boolean hasTitle() {
		return title != null;
	}

	@Override
	public int getWidth(Graphics2D g) {
		if (hasTitle()) {
			return 10 + Math.max(title.getWidth(g), child.getWidth(g)) + 10;
		} else {
			return 10 + child.getWidth(g) + 10;
		}
	}

	@Override
	public int getHeight(Graphics2D g) {
		if (hasTitle()) {
			return 10 + title.getHeight(g) + 10 + child.getHeight(g) + 10;
		} else {
			return 10 + child.getHeight(g) + 10;
		}
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		int x = 10;
		int y = 10;
		if (hasTitle()) {
			title.paint(g, x, y);
			y += title.getHeight(g);
			y += 10;
		}
		g.translate(x, y);
		child.paint(g, width - x - 10, height - y - 10);
		g.translate(-x, -y);
	}

}
