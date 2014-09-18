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

public class Diagram implements Element {

	private final Text title;
	private final Element child;

	public Diagram(String title, Element child) {
		this(title == null ? null : new Text(title), child);
	}

	public Diagram(Text title, Element child) {
		this.title = title;
		this.child = child;
	}

	private boolean hasTitle() {
		return title != null;
	}

	@Override
	public int getWidth(Graphics2D g) {
		if (hasTitle()) {
			return 10 + Math.max(title.getWidth(g), child.getWidth(g)) + 1 + 10;
		} else {
			return 10 + child.getWidth(g) + 1 + 10;
		}
	}

	@Override
	public int getHeight(Graphics2D g) {
		if (hasTitle()) {
			return 10 + title.getHeight(g) + 10 + child.getHeight(g) + 1 + 10;
		} else {
			return 10 + child.getHeight(g) + 1 + 10;
		}
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		int x = 10;
		int y = 10;
		if (hasTitle()) {
			title.paint(g, x, y);
			y += title.getHeight(g);
			y += 10;
		}
		g.translate(x, y);
		child.paint(g, width - x - 1 - 10, height - y - 1 - 10);
		g.translate(-x, -y);
	}

}
