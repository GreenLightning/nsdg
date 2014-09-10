package eu.greenlightning.nsdg.elements;

import java.awt.Graphics2D;

public interface Element {

	int getWidth(Graphics2D g);
	int getHeight(Graphics2D g);
	void paint(Graphics2D g, int width, int height);

}
