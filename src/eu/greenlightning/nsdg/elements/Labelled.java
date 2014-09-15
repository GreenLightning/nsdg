package eu.greenlightning.nsdg.elements;

import java.awt.Graphics2D;

public class Labelled {

	private final Text label;
	private final Element child;

	public Labelled() {
		this("", new Block());
	}

	public Labelled(String label, Element child) {
		this(new Text(label), child);
	}

	public Labelled(Text label, Element child) {
		this.label = label;
		this.child = child;
	}

	public Text getLabel() {
		return label;
	}

	public Element getChild() {
		return child;
	}

	public int getWidth(Graphics2D g) {
		return Math.max(5 + label.getWidth(g) + 5, child.getWidth(g));
	}

}
