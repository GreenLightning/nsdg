package eu.greenlightning.nsdg.elements;

import java.awt.Graphics2D;
import java.util.List;

public class Sequence implements Element {

	private final List<Element> children;

	public Sequence(List<Element> children) {
		this.children = children;
	}

	@Override
	public int getWidth(Graphics2D g) {
		int max = 0;
		for (int i = 0; i < children.size(); i++) {
			max = Math.max(max, children.get(i).getWidth(g));
		}
		return max;
	}

	@Override
	public int getHeight(Graphics2D g) {
		int sum = 0;
		for (int i = 0; i < children.size(); i++) {
			sum += children.get(i).getHeight(g);
		}
		return sum;
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		double referenceHeight = getHeight(g);
		int running = 0;
		for (int i = 0; i < children.size() - 1; i++) {
			Element child = children.get(i);
			double percentage = child.getHeight(g) / referenceHeight;
			int childHeight = (int) (percentage * height);
			g.translate(0, running);
			child.paint(g, width, childHeight);
			g.translate(0, -running);
			running += childHeight - 1;
		}
		g.translate(0, running);
		children.get(children.size() - 1).paint(g, width, height - running);
		g.translate(0, -running);
	}

}
