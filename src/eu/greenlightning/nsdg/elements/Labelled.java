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

public class Labelled {

	private final Text label;
	private final Element child;

	public Labelled() {
		this("", new Empty());
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
		return getWidth(g, 5);
	}

	public int getWidth(Graphics2D g, int padding) {
		return Math.max(padding + label.getWidth(g) + padding, child.getWidth(g));
	}

}
