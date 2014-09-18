package eu.greenlightning.nsdg.elements;

import java.awt.Graphics2D;

/**
 * An element of a Nassi-Shneiderman diagram.
 * <p>
 * The element provides its width and height and can paint itself. Potential children must be laid out
 * correctly, included in the width and height calculation and painted with the element.
 */
public interface Element {

	/**
	 * Calculates the minimum and preferred width of this element.
	 * <p>
	 * If possible it should be painted at this width, but it might be enlarged to align with other elements.
	 * Attempts to paint it at a smaller width might result in unspecified behavior and / or erroneous
	 * painting.
	 * <p>
	 * A {@link Graphics2D} object must be passed to this method to provide information about the context in
	 * which it will be painted.
	 * 
	 * @param g the {@link Graphics2D} object which will be used to paint this element
	 */
	int getWidth(Graphics2D g);

	/**
	 * Calculates the minimum and preferred height of this element.
	 * <p>
	 * If possible it should be painted at this height, but it might be enlarged to align with other elements.
	 * Attempts to paint it at a smaller height might result in unspecified behavior and / or erroneous
	 * painting.
	 * <p>
	 * A {@link Graphics2D} object must be passed to this method to provide information about the context in
	 * which it will be painted.
	 * 
	 * @param g the {@link Graphics2D} object which will be used to paint this element
	 */
	int getHeight(Graphics2D g);

	/**
	 * Paint this element in the given size. Note that the size must be at least as big as specified by
	 * {@link #getWidth(Graphics2D)} and {@link #getHeight(Graphics2D)} to assure correct painting.
	 */
	void paint(Graphics2D g, int width, int height);

}
