package eu.greenlightning.nsdg.elements;

import java.awt.Graphics2D;

public class Branch implements Element {

	private final Text condition;
	private final Labelled left;
	private final Labelled right;

	public Branch(String condition, Labelled left, Labelled right) {
		this(new Text(condition), left, right);
	}

	public Branch(Text condition, Labelled left, Labelled right) {
		this.condition = condition;
		this.left = left;
		this.right = right;
	}

	@Override
	public int getWidth(Graphics2D g) {
		return Math.max(getTitleWidth(g), getBlockWidth(g));
	}

	private int getTitleWidth(Graphics2D g) {
		return 10 + left.getLabel().getWidth(g) + 10 + condition.getWidth(g) + 10
			+ right.getLabel().getWidth(g) + 10;
	}

	private int getBlockWidth(Graphics2D g) {
		return left.getChild().getWidth(g) + right.getChild().getWidth(g);
	}

	@Override
	public int getHeight(Graphics2D g) {
		return getTitleHeight(g) + getBlockHeight(g);
	}

	private int getTitleHeight(Graphics2D g) {
		return 5 + condition.getHeight(g) + 5
			+ Math.max(left.getLabel().getHeight(g), right.getLabel().getHeight(g)) + 5;
	}

	private int getBlockHeight(Graphics2D g) {
		return Math.max(left.getChild().getHeight(g), right.getChild().getHeight(g));
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		double leftPercentage = left.getChild().getWidth(g)
			/ (double) (left.getChild().getWidth(g) + right.getChild().getWidth(g));
		int center = (int) (leftPercentage * width);
		int titleHeight = getTitleHeight(g);
		g.drawRect(0, 0, width - 1, height - 1);
		g.drawLine(0, 0, center, titleHeight);
		g.drawLine(center, titleHeight, width - 1, 0);
		g.drawLine(0, titleHeight, width - 1, titleHeight);
		condition.paint(g, center - (int) (leftPercentage * condition.getWidth(g)), 5);
		int labelOffset = 5 + condition.getHeight(g) + 5;
		left.getLabel().paint(g, 10, labelOffset);
		right.getLabel().paint(g, width - 10 - right.getLabel().getWidth(g), labelOffset);
		g.translate(0, titleHeight);
		left.getChild().paint(g, center + 1, height - titleHeight);
		g.translate(center, 0);
		right.getChild().paint(g, width - center, height - titleHeight);
		g.translate(-center, -titleHeight);
	}

}
