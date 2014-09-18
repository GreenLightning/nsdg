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
package eu.greenlightning.nsdg;

import static java.awt.image.BufferedImage.*;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import eu.greenlightning.nsdg.elements.Element;

public class ImageSaver {

	private static final Font font;

	static {
		Font tmpFont;
		try (InputStream input = ImageSaver.class.getResourceAsStream("SourceSansPro-Semibold.ttf")) {
			tmpFont = Font.createFont(Font.TRUETYPE_FONT, input).deriveFont(Font.PLAIN, 18);
		} catch (FontFormatException | IOException e) {
			tmpFont = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
		}
		font = tmpFont;
	}

	public void save(Element element, OutputStream out) throws IOException {
		BufferedImage helper = new BufferedImage(1, 1, TYPE_INT_ARGB);
		Graphics2D g = configureGraphics(helper.createGraphics());
		int width = element.getWidth(g);
		int height = element.getHeight(g);
		BufferedImage result = new BufferedImage(width, height, TYPE_INT_ARGB);
		g = configureGraphics(result.createGraphics());
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		element.paint(g, width, height);
		g.dispose();
		ImageIO.write(result, "PNG", out);
	}

	private Graphics2D configureGraphics(Graphics2D g) {
		g.setFont(font);
		Map<Key, Object> renderingHints = new HashMap<>();
		renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.addRenderingHints(renderingHints);
		return g;
	}

}
