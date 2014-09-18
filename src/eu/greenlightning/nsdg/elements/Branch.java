/* Copyright (c) 2014, Green Lightning <GreenLightning.online@googlemail.com>
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
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
		return Math.max(getConditionWidth(g), getLabelledWidth(g));
	}

	private int getConditionWidth(Graphics2D g) {
		return 10 + condition.getWidth(g) + 10;
	}

	private int getLabelledWidth(Graphics2D g) {
		return left.getWidth(g, 10) + right.getWidth(g, 10);
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
		double leftPercentage = left.getWidth(g, 10) / (double) getLabelledWidth(g);
		int center = (int) (leftPercentage * width);
		int titleHeight = getTitleHeight(g);
		g.drawRect(0, 0, width, height);
		g.drawLine(0, 0, center, titleHeight);
		g.drawLine(center, titleHeight, width - 1, 0);
		condition.paint(g, center - (int) (leftPercentage * condition.getWidth(g)), 5);
		int labelOffset = 5 + condition.getHeight(g) + 5;
		left.getLabel().paint(g, 10, labelOffset);
		right.getLabel().paint(g, width - 10 - right.getLabel().getWidth(g), labelOffset);
		g.translate(0, titleHeight);
		left.getChild().paint(g, center, height - titleHeight);
		g.translate(center, 0);
		right.getChild().paint(g, width - center, height - titleHeight);
		g.translate(-center, -titleHeight);
	}

}
