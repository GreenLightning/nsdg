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
import java.util.List;

public class Switch implements Element {

	private final Text expression;
	private final List<Labelled> cases;
	private final Labelled defaultLabelled;

	public Switch(String expression, List<Labelled> cases, Labelled defaultLabelled) {
		this(new Text(expression), cases, defaultLabelled);
	}

	public Switch(Text expression, List<Labelled> cases, Labelled defaultLabelled) {
		this.expression = expression;
		this.cases = cases;
		this.defaultLabelled = defaultLabelled;
	}

	@Override
	public int getWidth(Graphics2D g) {
		int width = getLabelledWidth(g);
		return Math.max(width, 10 + expression.getWidth(g) + 10);
	}

	private int getLabelledWidth(Graphics2D g) {
		return getCaseWidth(g) + getDefaultWidth(g);
	}

	private int getCaseWidth(Graphics2D g) {
		int caseWidth = 0;
		for (Labelled caseLabelled : cases) {
			caseWidth += caseLabelled.getWidth(g);
		}
		return caseWidth;
	}

	private int getDefaultWidth(Graphics2D g) {
		if (defaultLabelled != null) {
			return defaultLabelled.getWidth(g);
		} else {
			return 0;
		}
	}

	@Override
	public int getHeight(Graphics2D g) {
		return getTitleHeight(g) + getBlockHeight(g);
	}

	private int getTitleHeight(Graphics2D g) {
		int labelHeight = 0;
		for (Labelled caseLabelled : cases) {
			labelHeight = Math.max(labelHeight, caseLabelled.getLabel().getHeight(g));
		}
		if (defaultLabelled != null) {
			labelHeight = Math.max(labelHeight, defaultLabelled.getLabel().getHeight(g));
		}
		return 5 + expression.getHeight(g) + 5 + labelHeight + 5;
	}

	private int getBlockHeight(Graphics2D g) {
		int blockHeight = 0;
		for (Labelled caseLabelled : cases) {
			blockHeight = Math.max(blockHeight, caseLabelled.getChild().getHeight(g));
		}
		if (defaultLabelled != null) {
			blockHeight = Math.max(blockHeight, defaultLabelled.getChild().getHeight(g));
		}
		return blockHeight;
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		double dividerPercentage = getCaseWidth(g) / (double) getLabelledWidth(g);
		int divider = (int) (width * dividerPercentage);
		int titleHeight = getTitleHeight(g);
		g.drawRect(0, 0, width, height);
		g.drawLine(0, 0, divider, titleHeight);
		g.drawLine(divider, titleHeight, width, 0);
		if (defaultLabelled != null) {
			int offset = (int) ((double) divider / width * expression.getWidth(g));
			expression.paint(g, divider - offset, 5);
		} else {
			expression.paint(g, width - expression.getWidth(g) - 10, 5);
		}
		int labelHeight = 5 + expression.getHeight(g) + 5;
		int offset = 0;
		for (int i = 0; i < cases.size(); i++) {
			Labelled caseLabelled = cases.get(i);
			int caseWidth;
			if (i != cases.size() - 1) {
				double casePercentage = caseLabelled.getWidth(g) / (double) getLabelledWidth(g);
				caseWidth = (int) (casePercentage * width);
			} else {
				caseWidth = divider - offset;
			}
			if (i != cases.size() - 1) {
				int lineOffset = (int) (titleHeight * ((offset + caseWidth) / (double) divider));
				g.drawLine(offset + caseWidth, lineOffset, offset + caseWidth, titleHeight);
			}
			g.translate(offset, titleHeight);
			Text label = caseLabelled.getLabel();
			label.paint(g, (caseWidth - label.getWidth(g)) / 2, -5 - label.getHeight(g));
			caseLabelled.getChild().paint(g, caseWidth, height - titleHeight);
			g.translate(-offset, -titleHeight);
			offset += caseWidth;
		}
		if (defaultLabelled != null) {
			int position = divider + (width - divider - defaultLabelled.getLabel().getWidth(g)) / 2;
			defaultLabelled.getLabel().paint(g, position, labelHeight);
			g.translate(divider, titleHeight);
			defaultLabelled.getChild().paint(g, width - divider, height - titleHeight);
			g.translate(-divider, -titleHeight);
		}
	}

}
