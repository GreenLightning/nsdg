package eu.greenlightning.nsdg.elements;

import java.awt.Graphics2D;

public class TestLoop implements Element {

	private final boolean testFirst;
	private final Text condition;
	private final Text side;
	private final Element child;

	public TestLoop(boolean testFirst, String condition, String side, Element child) {
		this(testFirst, new Text(condition), new Text(side), child);
	}

	public TestLoop(boolean testFirst, Text condition, Text side, Element child) {
		this.testFirst = testFirst;
		this.condition = condition;
		this.side = side;
		this.child = child;
	}

	@Override
	public int getWidth(Graphics2D g) {
		int max = 0;
		max = Math.max(max, condition.getWidth(g) + 20);
		max = Math.max(max, side.getWidth(g) + 20 + child.getWidth(g));
		return max;
	}

	@Override
	public int getHeight(Graphics2D g) {
		int middle = Math.max(side.getHeight(g) + 10, child.getHeight(g));
		return 5 + condition.getHeight(g) + 5 + middle + 5;
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		double conditionPercentage = (5 + condition.getHeight(g) + 5) / (double) getHeight(g);
		double sidePercentage = (10 + side.getWidth(g) + 10) / (double) getWidth(g);
		int conditionHeight = (int) (conditionPercentage * height);
		int sideWidth = (int) (sidePercentage * width);
		g.drawRect(0, 0, width - 1, height - 1);
		if (testFirst) {
			condition.paint(g, 10, (conditionHeight - condition.getHeight(g)) / 2);
			side.paint(g, (sideWidth - side.getWidth(g)) / 2, conditionHeight
				+ (height - conditionHeight - side.getHeight(g)) / 2);
			g.translate(sideWidth, conditionHeight);
			child.paint(g, width - sideWidth, height - conditionHeight);
			g.translate(-sideWidth, -conditionHeight);
		} else {
			side.paint(g, (sideWidth - side.getWidth(g)) / 2, (height - conditionHeight - side
				.getHeight(g)) / 2);
			g.translate(sideWidth, 0);
			child.paint(g, width - sideWidth, height - conditionHeight);
			g.translate(-sideWidth, 0);
			condition.paint(g, 10, height - conditionHeight
				+ (conditionHeight - condition.getHeight(g)) / 2);
		}
	}

}
