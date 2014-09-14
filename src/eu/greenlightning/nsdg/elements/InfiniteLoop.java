package eu.greenlightning.nsdg.elements;

import java.awt.Graphics2D;

public class InfiniteLoop implements Element {

	private final Text top;
	private final Text side;
	private final Text bottom;
	private final Element child;

	public InfiniteLoop(String top, String side, String bottom, Element child) {
		this(new Text(top), new Text(side), new Text(bottom), child);
	}

	public InfiniteLoop(Text top, Text side, Text bottom, Element child) {
		this.top = top;
		this.side = side;
		this.bottom = bottom;
		this.child = child;
	}

	@Override
	public int getWidth(Graphics2D g) {
		int max = 0;
		max = Math.max(max, top.getWidth(g) + 20);
		max = Math.max(max, side.getWidth(g) + 20 + child.getWidth(g));
		max = Math.max(max, bottom.getWidth(g) + 20);
		return max;
	}

	@Override
	public int getHeight(Graphics2D g) {
		int middle = Math.max(side.getHeight(g) + 10, child.getHeight(g));
		return 5 + top.getHeight(g) + 5 + middle + 5 + bottom.getHeight(g) + 5;
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		double topPercentage = (5 + top.getHeight(g) + 5) / (double) getHeight(g);
		double bottomPercentage = (5 + bottom.getHeight(g) + 5) / (double) getHeight(g);
		double sidePercentage = (10 + side.getWidth(g) + 10) / (double) getWidth(g);
		int topHeight = (int) (topPercentage * height);
		int bottomHeight = (int) (bottomPercentage * height);
		int sideWidth = (int) (sidePercentage * width);
		g.drawRect(0, 0, width - 1, height - 1);
		top.paint(g, (width - top.getWidth(g)) / 2, (topHeight - top.getHeight(g)) / 2);
		side.paint(g, (sideWidth - side.getWidth(g)) / 2, topHeight
			+ (height - topHeight - bottomHeight - side.getHeight(g)) / 2);
		bottom.paint(g, (width - bottom.getWidth(g)) / 2, height - bottomHeight
			+ (bottomHeight - bottom.getHeight(g)) / 2);
		g.translate(sideWidth, topHeight);
		child.paint(g, width - sideWidth, height - topHeight - bottomHeight + 1);
		g.translate(-sideWidth, -topHeight);
	}

}
