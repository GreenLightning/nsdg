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
			running += childHeight;
		}
		g.translate(0, running);
		children.get(children.size() - 1).paint(g, width, height - running);
		g.translate(0, -running);
	}

}
