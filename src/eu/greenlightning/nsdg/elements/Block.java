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

import java.awt.Color;
import java.awt.Graphics2D;

public class Block implements Element {

	protected final Text text;

	public Block(String text) {
		this(new Text(text));
	}

	public Block(Text text) {
		this.text = text;
	}

	@Override
	public int getWidth(Graphics2D g) {
		return 10 + text.getWidth(g) + 10;
	}

	@Override
	public int getHeight(Graphics2D g) {
		return 5 + text.getHeight(g) + 5;
	}

	@Override
	public void paint(Graphics2D g, int width, int height) {
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width, height);
		int x = 10;
		int y = (height - text.getHeight(g)) / 2;
		text.paint(g, x, y);
	}

}
